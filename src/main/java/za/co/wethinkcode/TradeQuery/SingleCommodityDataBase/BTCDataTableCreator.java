package za.co.wethinkcode.TradeQuery.SingleCommodityDataBase;

import java.sql.*;
import java.util.Set;
import java.util.LinkedHashSet;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BTCDataTableCreator {
    
    private static final String DB_URL = "jdbc:sqlite:btc_data.db";
    private static final String JSON_FILE_PATH = "src/main/java/za/co/wethinkcode/TradeQuery/Data/BTC_(data_daily).json";
    
    public static void main(String[] args) {
        BTCDataTableCreator creator = new BTCDataTableCreator();
        try {
            creator.createTableAndInsertData();
            System.out.println("BTC data table created and populated successfully!");
        } catch (Exception e) {
            System.err.println("Error creating table: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void createTableAndInsertData() throws SQLException, IOException {
        System.out.println("Starting BTC data table creation...");
        
        // First, verify JSON file exists and is readable
        if (!new java.io.File(JSON_FILE_PATH).exists()) {
            throw new IOException("JSON file not found: " + JSON_FILE_PATH);
        }
        System.out.println("JSON file found: " + JSON_FILE_PATH);
        
        // Read and parse JSON to get dates
        Set<String> dates = extractDatesFromJson();
        System.out.println("Extracted " + dates.size() + " dates from JSON");
        
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            System.out.println("Connected to database: " + DB_URL);
            
            // Enable better error reporting
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
                stmt.execute("PRAGMA journal_mode = WAL");
            }
            
            // Create the table
            createTable(conn, dates);
            
            // Insert the data with better error handling
            insertDataImproved(conn, dates);
            
            // Verify the data was inserted
            verifyData(conn);
        }
    }
    
    private Set<String> extractDatesFromJson() throws IOException {
        Set<String> dates = new LinkedHashSet<>();
        System.out.println("Reading JSON file...");
        
        try (FileReader reader = new FileReader(JSON_FILE_PATH)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            
            if (!jsonObject.has("Time Series (Daily)")) {
                throw new IOException("JSON does not contain 'Time Series (Daily)' key");
            }
            
            JsonObject timeSeries = jsonObject.getAsJsonObject("Time Series (Daily)");
            System.out.println("Found Time Series data with " + timeSeries.size() + " entries");
            
            for (String date : timeSeries.keySet()) {
                dates.add(date);
                if (dates.size() <= 5) { // Show first 5 dates for verification
                    System.out.println("  Date found: " + date);
                }
            }
            
            if (dates.size() > 5) {
                System.out.println("  ... and " + (dates.size() - 5) + " more dates");
            }
        }
        
        return dates;
    }
    
    private void createTable(Connection conn, Set<String> dates) throws SQLException {
        System.out.println("Creating table...");
        
        // Drop table if it exists
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS BTC_data_daily");
            System.out.println("Dropped existing table if it existed");
        }
        
        // Build the CREATE TABLE statement
        StringBuilder createTableSQL = new StringBuilder();
        createTableSQL.append("CREATE TABLE BTC_data_daily (\n");
        createTableSQL.append("    parameter TEXT PRIMARY KEY NOT NULL");
        
        // Add columns for dates
        for (String date : dates) {
            String columnName = date.replace("-", "_");
            createTableSQL.append(",\n    \"").append(columnName).append("\" DECIMAL(15,4)");
        }
        createTableSQL.append("\n)");
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL.toString());
            System.out.println("Table created with " + dates.size() + " date columns");
        }
    }
    
    private void insertDataImproved(Connection conn, Set<String> dates) throws SQLException, IOException {
        System.out.println("Starting data insertion...");
        
        // Read JSON data again
        JsonObject jsonData;
        try (FileReader reader = new FileReader(JSON_FILE_PATH)) {
            jsonData = JsonParser.parseReader(reader).getAsJsonObject();
        }
        
        JsonObject timeSeries = jsonData.getAsJsonObject("Time Series (Daily)");
        
        // Parameters to insert
        String[] parameters = {"open", "high", "low", "close", "volume"};
        String[] jsonKeys = {"1. open", "2. high", "3. low", "4. close", "5. volume"};
        
        // Build the INSERT SQL
        StringBuilder columnsSQL = new StringBuilder("parameter");
        StringBuilder placeholdersSQL = new StringBuilder("?");
        
        for (String date : dates) {
            String columnName = date.replace("-", "_");
            columnsSQL.append(", \"").append(columnName).append("\"");
            placeholdersSQL.append(", ?");
        }
        
        String insertSQL = "INSERT INTO BTC_data_daily (" + columnsSQL + ") VALUES (" + placeholdersSQL + ")";
        System.out.println("Insert SQL prepared for " + dates.size() + " columns");
        
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            
            for (int paramIndex = 0; paramIndex < parameters.length; paramIndex++) {
                System.out.println("Inserting data for parameter: " + parameters[paramIndex]);
                
                // Set parameter name
                pstmt.setString(1, parameters[paramIndex]);
                
                // Set values for each date
                int columnIndex = 2;
                int successfulValues = 0;
                int nullValues = 0;
                
                for (String date : dates) {
                    JsonObject dayData = timeSeries.getAsJsonObject(date);
                    
                    if (dayData != null && dayData.has(jsonKeys[paramIndex])) {
                        try {
                            String valueStr = dayData.get(jsonKeys[paramIndex]).getAsString();
                            BigDecimal value = new BigDecimal(valueStr);
                            pstmt.setBigDecimal(columnIndex, value);
                            successfulValues++;
                        } catch (Exception e) {
                            System.err.println("Error parsing value for " + parameters[paramIndex] + " on " + date + ": " + e.getMessage());
                            pstmt.setNull(columnIndex, Types.DECIMAL);
                            nullValues++;
                        }
                    } else {
                        pstmt.setNull(columnIndex, Types.DECIMAL);
                        nullValues++;
                    }
                    
                    columnIndex++;
                }
                
                // Execute the insert
                try {
                    int rowsAffected = pstmt.executeUpdate();
                    System.out.println("  Successfully inserted " + parameters[paramIndex] + 
                                     " (rows affected: " + rowsAffected + 
                                     ", successful values: " + successfulValues + 
                                     ", null values: " + nullValues + ")");
                } catch (SQLException e) {
                    System.err.println("  Failed to insert " + parameters[paramIndex] + ": " + e.getMessage());
                    throw e;
                }
            }
        }
        
        System.out.println("Data insertion completed!");
    }
    
    private void verifyData(Connection conn) throws SQLException {
        System.out.println("\n=== Data Verification ===");
        
        // Count rows
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM BTC_data_daily")) {
            
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("Total rows in table: " + count);
                
                if (count == 0) {
                    System.err.println("WARNING: Table is empty!");
                    return;
                }
            }
        }
        
        // Show parameters
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT parameter FROM BTC_data_daily ORDER BY parameter")) {
            
            System.out.println("Parameters in table:");
            while (rs.next()) {
                System.out.println("  " + rs.getString("parameter"));
            }
        }
        
        // Show sample data (first 3 columns)
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM BTC_data_daily LIMIT 3")) {
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            System.out.println("\nSample data (first 3 columns):");
            
            // Headers
            for (int i = 1; i <= Math.min(3, columnCount); i++) {
                System.out.printf("%-15s", metaData.getColumnName(i));
            }
            System.out.println();
            System.out.println("-".repeat(45));
            
            // Data
            while (rs.next()) {
                for (int i = 1; i <= Math.min(3, columnCount); i++) {
                    String value = rs.getString(i);
                    if (value == null) value = "NULL";
                    System.out.printf("%-15s", value.length() > 14 ? value.substring(0, 14) : value);
                }
                System.out.println();
            }
        }
    }
}