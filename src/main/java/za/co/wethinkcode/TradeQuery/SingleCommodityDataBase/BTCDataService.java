package za.co.wethinkcode.TradeQuery.SingleCommodityDataBase;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

/**
 * Service class for querying BTC data from the database
 * Handles all database operations without UI concerns
 */
public class BTCDataService {
    
    private static final String DB_URL = "jdbc:sqlite:btc_data.db";
    
    /**
     * Get a specific parameter value for a given date
     * @param parameter The parameter (open, high, low, close, volume)
     * @param date The date in YYYY-MM-DD format
     * @return The value as BigDecimal, or null if not found
     */
    public BigDecimal getParameterForDate(String parameter, String date) throws SQLException {
        String columnName = date.replace("-", "_");
        String sql = "SELECT \"" + columnName + "\" FROM BTC_data_daily WHERE parameter = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, parameter.toLowerCase());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String value = rs.getString(1);
                    return value != null ? new BigDecimal(value) : null;
                }
                return null;
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("no such column")) {
                throw new SQLException("Date " + date + " not found in database");
            }
            throw e;
        }
    }
    
    /**
     * Get all data for a specific parameter across all dates
     * @param parameter The parameter (open, high, low, close, volume)
     * @return List of BTCDataPoint containing date and value pairs
     */
    public List<BTCDataPoint> getAllDataForParameter(String parameter) throws SQLException {
        String sql = "SELECT * FROM BTC_data_daily WHERE parameter = ?";
        List<BTCDataPoint> dataPoints = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, parameter.toLowerCase());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    
                    // Skip parameter column (index 1) and process date columns
                    for (int i = 2; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        String value = rs.getString(i);
                        
                        if (value != null) {
                            String date = columnName.replace("_", "-").replace("\"", "");
                            dataPoints.add(new BTCDataPoint(date, new BigDecimal(value)));
                        }
                    }
                }
            }
        }
        
        return dataPoints;
    }
    
    /**
     * Get all parameters for a specific date
     * @param date The date in YYYY-MM-DD format
     * @return BTCDayData object containing all parameters for the date
     */
    public BTCDayData getAllDataForDate(String date) throws SQLException {
        String columnName = date.replace("-", "_");
        String sql = "SELECT parameter, \"" + columnName + "\" FROM BTC_data_daily ORDER BY " +
                     "CASE parameter " +
                     "WHEN 'open' THEN 1 " +
                     "WHEN 'high' THEN 2 " +
                     "WHEN 'low' THEN 3 " +
                     "WHEN 'close' THEN 4 " +
                     "WHEN 'volume' THEN 5 " +
                     "ELSE 6 END";
        
        BTCDayData dayData = new BTCDayData(date);
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String parameter = rs.getString("parameter");
                String value = rs.getString(2);
                
                if (value != null) {
                    BigDecimal bdValue = new BigDecimal(value);
                    
                    switch (parameter.toLowerCase()) {
                        case "open":
                            dayData.setOpen(bdValue);
                            break;
                        case "high":
                            dayData.setHigh(bdValue);
                            break;
                        case "low":
                            dayData.setLow(bdValue);
                            break;
                        case "close":
                            dayData.setClose(bdValue);
                            break;
                        case "volume":
                            dayData.setVolume(bdValue);
                            break;
                    }
                }
            }
            
        } catch (SQLException e) {
            if (e.getMessage().contains("no such column")) {
                throw new SQLException("Date " + date + " not found in database");
            }
            throw e;
        }
        
        return dayData;
    }
    
    /**
     * Get all available dates in the database
     * @return List of dates in YYYY-MM-DD format
     */
    public List<String> getAvailableDates() throws SQLException {
        List<String> dates = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, "BTC_data_daily", null);
            
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                if (!columnName.equals("parameter")) {
                    String date = columnName.replace("_", "-").replace("\"", "");
                    dates.add(date);
                }
            }
        }
        
        return dates;
    }
    
    /**
     * Get available parameters in the database
     * @return List of parameter names
     */
    public List<String> getAvailableParameters() throws SQLException {
        List<String> parameters = new ArrayList<>();
        String sql = "SELECT DISTINCT parameter FROM BTC_data_daily ORDER BY " +
                     "CASE parameter " +
                     "WHEN 'open' THEN 1 " +
                     "WHEN 'high' THEN 2 " +
                     "WHEN 'low' THEN 3 " +
                     "WHEN 'close' THEN 4 " +
                     "WHEN 'volume' THEN 5 " +
                     "ELSE 6 END";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                parameters.add(rs.getString("parameter"));
            }
        }
        
        return parameters;
    }
    
    /**
     * Check if the database and table exist and have data
     * @return DatabaseStatus object with status information
     */
    public DatabaseStatus checkDatabaseStatus() throws SQLException {
        DatabaseStatus status = new DatabaseStatus();
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            // Check if table exists
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='BTC_data_daily'");
            if (rs.next()) {
                status.setTableExists(rs.getInt(1) > 0);
            }
            
            if (status.isTableExists()) {
                // Check row count
                rs = stmt.executeQuery("SELECT COUNT(*) FROM BTC_data_daily");
                if (rs.next()) {
                    status.setRowCount(rs.getInt(1));
                }
                
                // Check column count
                DatabaseMetaData metaData = conn.getMetaData();
                ResultSet columns = metaData.getColumns(null, null, "BTC_data_daily", null);
                int columnCount = 0;
                while (columns.next()) {
                    columnCount++;
                }
                status.setColumnCount(columnCount);
            }
        }
        
        return status;
    }
    
    /**
     * Get data for a date range
     * @param parameter The parameter to query
     * @param startDate Start date (inclusive)
     * @param endDate End date (inclusive)
     * @return List of BTCDataPoint for the date range
     */
    public List<BTCDataPoint> getParameterForDateRange(String parameter, String startDate, String endDate) throws SQLException {
        List<BTCDataPoint> allData = getAllDataForParameter(parameter);
        List<BTCDataPoint> filteredData = new ArrayList<>();
        
        for (BTCDataPoint point : allData) {
            String date = point.getDate();
            if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
                filteredData.add(point);
            }
        }
        
        return filteredData;
    }
    
    // Data classes for structured returns
    
    public static class BTCDataPoint {
        private String date;
        private BigDecimal value;
        
        public BTCDataPoint(String date, BigDecimal value) {
            this.date = date;
            this.value = value;
        }
        
        public String getDate() { return date; }
        public BigDecimal getValue() { return value; }
        
        @Override
        public String toString() {
            return date + ": " + value;
        }
    }
    
    public static class BTCDayData {
        private String date;
        private BigDecimal open;
        private BigDecimal high;
        private BigDecimal low;
        private BigDecimal close;
        private BigDecimal volume;
        
        public BTCDayData(String date) {
            this.date = date;
        }
        
        // Getters and setters
        public String getDate() { return date; }
        public BigDecimal getOpen() { return open; }
        public BigDecimal getHigh() { return high; }
        public BigDecimal getLow() { return low; }
        public BigDecimal getClose() { return close; }
        public BigDecimal getVolume() { return volume; }
        
        public void setOpen(BigDecimal open) { this.open = open; }
        public void setHigh(BigDecimal high) { this.high = high; }
        public void setLow(BigDecimal low) { this.low = low; }
        public void setClose(BigDecimal close) { this.close = close; }
        public void setVolume(BigDecimal volume) { this.volume = volume; }
        
        public boolean hasData() {
            return open != null || high != null || low != null || close != null || volume != null;
        }
        
        @Override
        public String toString() {
            return String.format("BTCDayData{date='%s', open=%s, high=%s, low=%s, close=%s, volume=%s}", 
                               date, open, high, low, close, volume);
        }
    }
    
    public static class DatabaseStatus {
        private boolean tableExists;
        private int rowCount;
        private int columnCount;
        
        public boolean isTableExists() { return tableExists; }
        public int getRowCount() { return rowCount; }
        public int getColumnCount() { return columnCount; }
        
        public void setTableExists(boolean tableExists) { this.tableExists = tableExists; }
        public void setRowCount(int rowCount) { this.rowCount = rowCount; }
        public void setColumnCount(int columnCount) { this.columnCount = columnCount; }
        
        public boolean isHealthy() {
            return tableExists && rowCount > 0 && columnCount > 1;
        }
        
        @Override
        public String toString() {
            return String.format("DatabaseStatus{tableExists=%s, rowCount=%d, columnCount=%d}", 
                               tableExists, rowCount, columnCount);
        }
    }
}