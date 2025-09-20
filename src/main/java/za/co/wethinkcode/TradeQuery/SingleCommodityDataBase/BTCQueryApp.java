package za.co.wethinkcode.TradeQuery.SingleCommodityDataBase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import za.co.wethinkcode.TradeQuery.SingleCommodityDataBase.BTCDataService.*;

/**
 * Main application class for interactive BTC data querying
 * Handles user interface and delegates data operations to BTCDataService
 */
public class BTCQueryApp {
    
    private final BTCDataService dataService;
    private final Scanner scanner;
    
    public BTCQueryApp() {
        this.dataService = new BTCDataService();
        this.scanner = new Scanner(System.in);
    }
    
    public static void main(String[] args) {
        BTCQueryApp app = new BTCQueryApp();
        try {
            app.run();
        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            app.cleanup();
        }
    }
    
    public void run() throws Exception {
        System.out.println("=== BTC Data Query Application ===");
        
        // Quick health check
        performHealthCheck();
        
        // Main menu loop
        while (true) {
            showMenu();
            String choice = scanner.nextLine().trim();
            
            if (choice.equals("0")) {
                System.out.println("Goodbye!");
                break;
            }
            
            try {
                handleChoice(choice);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
            
            System.out.println(); // Add spacing between operations
        }
    }
    
    private void performHealthCheck() {
        System.out.println("Checking database status...");
        try {
            DatabaseStatus status = dataService.checkDatabaseStatus();
            
            if (status.isHealthy()) {
                System.out.println("✅ Database is healthy");
                System.out.println("   - Table exists: " + status.isTableExists());
                System.out.println("   - Row count: " + status.getRowCount());
                System.out.println("   - Column count: " + status.getColumnCount());
                
                // Quick data test
                BigDecimal testValue = dataService.getParameterForDate("close", "2025-03-21");
                if (testValue != null) {
                    System.out.println("✅ Sample data retrieval successful: close for 2025-03-21 = " + testValue);
                }
            } else {
                System.out.println("❌ Database has issues: " + status);
            }
        } catch (Exception e) {
            System.out.println("❌ Database health check failed: " + e.getMessage());
        }
        System.out.println();
    }
    
    private void showMenu() {
        System.out.println("Choose an option:");
        System.out.println("1. Get parameter for specific date");
        System.out.println("2. Get all data for a parameter");
        System.out.println("3. Get all data for a date");
        System.out.println("4. Get parameter for date range");
        System.out.println("5. Show available dates");
        System.out.println("6. Show available parameters");
        System.out.println("7. Quick queries (presets)");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }
    
    private void handleChoice(String choice) throws Exception {
        switch (choice) {
            case "1":
                handleGetParameterForDate();
                break;
            case "2":
                handleGetAllDataForParameter();
                break;
            case "3":
                handleGetAllDataForDate();
                break;
            case "4":
                handleGetParameterForDateRange();
                break;
            case "5":
                handleShowAvailableDates();
                break;
            case "6":
                handleShowAvailableParameters();
                break;
            case "7":
                handleQuickQueries();
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
        }
    }
    
    private void handleGetParameterForDate() throws Exception {
        System.out.print("Enter parameter (open/high/low/close/volume): ");
        String parameter = scanner.nextLine().trim();
        
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine().trim();
        
        BigDecimal value = dataService.getParameterForDate(parameter, date);
        
        if (value != null) {
            System.out.println("Result: " + parameter.toUpperCase() + " for " + date + " = " + value);
        } else {
            System.out.println("No data found for " + parameter + " on " + date);
        }
    }
    
    private void handleGetAllDataForParameter() throws Exception {
        System.out.print("Enter parameter (open/high/low/close/volume): ");
        String parameter = scanner.nextLine().trim();
        
        List<BTCDataPoint> dataPoints = dataService.getAllDataForParameter(parameter);
        
        if (!dataPoints.isEmpty()) {
            System.out.println("\n=== All " + parameter.toUpperCase() + " data ===");
            System.out.printf("%-12s%-15s%n", "Date", "Value");
            System.out.println("-".repeat(27));
            
            // Show first 20 results, then ask if user wants to see more
            int count = 0;
            for (BTCDataPoint point : dataPoints) {
                System.out.printf("%-12s%-15s%n", point.getDate(), point.getValue());
                count++;
                
                if (count == 20 && dataPoints.size() > 20) {
                    System.out.print("\nShowing first 20 of " + dataPoints.size() + " results. Continue? (y/n): ");
                    String response = scanner.nextLine().trim().toLowerCase();
                    if (!response.equals("y") && !response.equals("yes")) {
                        break;
                    }
                }
            }
            
            System.out.println("\nTotal results: " + count + " of " + dataPoints.size());
        } else {
            System.out.println("No data found for parameter: " + parameter);
        }
    }
    
    private void handleGetAllDataForDate() throws Exception {
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine().trim();
        
        BTCDayData dayData = dataService.getAllDataForDate(date);
        
        if (dayData.hasData()) {
            System.out.println("\n=== All data for " + date + " ===");
            System.out.printf("%-12s%-15s%n", "Parameter", "Value");
            System.out.println("-".repeat(27));
            
            if (dayData.getOpen() != null) 
                System.out.printf("%-12s%-15s%n", "OPEN", dayData.getOpen());
            if (dayData.getHigh() != null) 
                System.out.printf("%-12s%-15s%n", "HIGH", dayData.getHigh());
            if (dayData.getLow() != null) 
                System.out.printf("%-12s%-15s%n", "LOW", dayData.getLow());
            if (dayData.getClose() != null) 
                System.out.printf("%-12s%-15s%n", "CLOSE", dayData.getClose());
            if (dayData.getVolume() != null) 
                System.out.printf("%-12s%-15s%n", "VOLUME", dayData.getVolume());
        } else {
            System.out.println("No data found for date: " + date);
        }
    }
    
    private void handleGetParameterForDateRange() throws Exception {
        System.out.print("Enter parameter (open/high/low/close/volume): ");
        String parameter = scanner.nextLine().trim();
        
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine().trim();
        
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDate = scanner.nextLine().trim();
        
        List<BTCDataPoint> dataPoints = dataService.getParameterForDateRange(parameter, startDate, endDate);
        
        if (!dataPoints.isEmpty()) {
            System.out.println("\n=== " + parameter.toUpperCase() + " data from " + startDate + " to " + endDate + " ===");
            System.out.printf("%-12s%-15s%n", "Date", "Value");
            System.out.println("-".repeat(27));
            
            for (BTCDataPoint point : dataPoints) {
                System.out.printf("%-12s%-15s%n", point.getDate(), point.getValue());
            }
            
            System.out.println("\nTotal results: " + dataPoints.size());
        } else {
            System.out.println("No data found for " + parameter + " in date range " + startDate + " to " + endDate);
        }
    }
    
    private void handleShowAvailableDates() throws Exception {
        List<String> dates = dataService.getAvailableDates();
        
        System.out.println("\n=== Available Dates ===");
        System.out.println("Total dates: " + dates.size());
        
        if (!dates.isEmpty()) {
            System.out.println("Date range: " + dates.get(dates.size() - 1) + " to " + dates.get(0));
            
            System.out.println("\nFirst 10 dates:");
            for (int i = 0; i < Math.min(10, dates.size()); i++) {
                System.out.println("  " + dates.get(i));
            }
            
            if (dates.size() > 10) {
                System.out.print("\nShow all dates? (y/n): ");
                String response = scanner.nextLine().trim().toLowerCase();
                if (response.equals("y") || response.equals("yes")) {
                    for (int i = 10; i < dates.size(); i++) {
                        System.out.println("  " + dates.get(i));
                    }
                }
            }
        }
    }
    
    private void handleShowAvailableParameters() throws Exception {
        List<String> parameters = dataService.getAvailableParameters();
        
        System.out.println("\n=== Available Parameters ===");
        for (String parameter : parameters) {
            System.out.println("  " + parameter.toUpperCase());
        }
    }
    
    private void handleQuickQueries() throws Exception {
        System.out.println("\nQuick Queries:");
        System.out.println("1. Latest close price (2025-03-21)");
        System.out.println("2. Latest full day data (2025-03-21)");
        System.out.println("3. Close prices for last 5 days");
        System.out.println("4. All March 2025 close prices");
        System.out.print("Choose quick query: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                BigDecimal latestClose = dataService.getParameterForDate("close", "2025-03-21");
                System.out.println("Latest close price (2025-03-21): " + latestClose);
                break;
                
            case "2":
                BTCDayData latestDay = dataService.getAllDataForDate("2025-03-21");
                System.out.println("Latest day data: " + latestDay);
                break;
                
            case "3":
                List<BTCDataPoint> last5 = dataService.getParameterForDateRange("close", "2025-03-17", "2025-03-21");
                System.out.println("Close prices for last 5 days:");
                last5.forEach(point -> System.out.println("  " + point));
                break;
                
            case "4":
                List<BTCDataPoint> march = dataService.getParameterForDateRange("close", "2025-03-01", "2025-03-31");
                System.out.println("March 2025 close prices (" + march.size() + " days):");
                march.forEach(point -> System.out.println("  " + point));
                break;
                
            default:
                System.out.println("Invalid quick query choice!");
        }
    }
    
    private void cleanup() {
        if (scanner != null) {
            scanner.close();
        }
    }
}