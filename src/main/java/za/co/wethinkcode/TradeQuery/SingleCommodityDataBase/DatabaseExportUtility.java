package za.co.wethinkcode.TradeQuery.SingleCommodityDataBase;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class DatabaseExportUtility {
    
    private static final String DB_URL = "jdbc:sqlite:btc_data.db";
    
    public static void exportDatabaseToSQL(String outputFileName) throws SQLException, IOException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             FileWriter writer = new FileWriter(outputFileName)) {
            
            // Write header
            writer.write("-- SQLite Database Export\n");
            writer.write("-- Generated: " + new java.util.Date() + "\n\n");
            
            // Get all table names
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
            
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                exportTable(conn, writer, tableName);
            }
            
            System.out.println("Database exported to: " + outputFileName);
        }
    }
    
    private static void exportTable(Connection conn, FileWriter writer, String tableName) 
            throws SQLException, IOException {
        
        // Export table schema
        writer.write("-- Table: " + tableName + "\n");
        writer.write("DROP TABLE IF EXISTS " + tableName + ";\n");
        
        // Get CREATE TABLE statement
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT sql FROM sqlite_master WHERE type='table' AND name='" + tableName + "'")) {
            
            if (rs.next()) {
                writer.write(rs.getString("sql") + ";\n\n");
            }
        }
        
        // Export table data
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (rs.next()) {
                writer.write("INSERT INTO " + tableName + " VALUES (");
                
                for (int i = 1; i <= columnCount; i++) {
                    if (i > 1) writer.write(", ");
                    
                    String value = rs.getString(i);
                    if (value == null) {
                        writer.write("NULL");
                    } else {
                        // Escape single quotes and wrap in quotes for non-numeric values
                        writer.write("'" + value.replace("'", "''") + "'");
                    }
                }
                
                writer.write(");\n");
            }
        }
        
        writer.write("\n");
    }
    
    public static void exportTableToSQL(String tableName, String outputFileName) 
            throws SQLException, IOException {
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             FileWriter writer = new FileWriter(outputFileName)) {
            
            writer.write("-- Table Export: " + tableName + "\n");
            writer.write("-- Generated: " + new java.util.Date() + "\n\n");
            
            exportTable(conn, writer, tableName);
            
            System.out.println("Table " + tableName + " exported to: " + outputFileName);
        }
    }
    
    public static void main(String[] args) {
        try {
            // Export entire database
            exportDatabaseToSQL("btc_database_backup.sql");
            
            // Export specific table
            exportTableToSQL("BTC_data_daily", "btc_table_backup.sql");
            
        } catch (Exception e) {
            System.err.println("Export failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}