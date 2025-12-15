# Trade Forecast Functions

A Java-based statistical analysis and forecasting framework for financial time series data, with specialized support for cryptocurrency market analysis.

## Overview

This project implements advanced statistical methods for analyzing sequential data and generating probabilistic forecasts. It combines custom statistical algorithms with database integration to provide comprehensive market data analysis capabilities.

## Core Features

### Statistical Analysis Module
- **Central Tendency Calculations**: Mean, median, mode, and least-difference algorithms
- **Deviation Analysis**: Configurable power-based deviation calculations with distribution modeling
- **Difference Operations**: Sequential differencing (absolute, positive, negative, nth-order)
- **Expectation Values**: Probability-weighted outcome calculations

### Forecasting Capabilities
- **Magnitude-Weighted Forecasting**: Analyzes absolute change magnitudes with directional probability weighting
- **Asymmetric Trend Forecasting**: Separate modeling of positive and negative movements
- **Distribution-Based Predictions**: Three-point forecasts (lower bound, central tendency, upper bound)
- **Configurable Probability Bias**: Adjustable optimistic/pessimistic forecasting bias

### Database Integration
- **SQLite Database**: Efficient storage of daily OHLCV (Open, High, Low, Close, Volume) data
- **JSON Import**: Automated table creation and data population from JSON sources
- **Query Service**: Comprehensive API for data retrieval by date, parameter, or range
- **Interactive CLI**: User-friendly command-line interface for database queries

## Project Structure

```
src/main/java/za/co/wethinkcode/TradeQuery/
├── ForecastModules/
│   └── ForecastBase.java              # Core forecasting algorithms
├── StatisticsModule/
│   ├── CentralTendency.java           # Mean, median, mode calculations
│   ├── DeviationAndDistribution.java  # Distribution analysis
│   ├── Difference.java                # Sequential difference operations
│   ├── Expectation.java               # Expected value calculations
│   └── LeastDeviation.java            # Least deviation algorithms
├── SingleCommodityDataBase/
│   ├── BTCDataService.java            # Database query service
│   ├── BTCDataTableCreator.java       # Database initialization
│   ├── BTCQueryApp.java               # Interactive CLI application
│   └── DatabaseExportUtility.java     # Database backup tools
└── Implementation.java                # Usage examples
```

## Technical Details

### Key Technologies
- **Java 21**: Modern Java features and syntax
- **Maven**: Dependency management and build automation
- **SQLite JDBC**: Lightweight database integration
- **Gson**: JSON parsing and data import
- **JUnit Jupiter**: Unit testing framework
- **BigDecimal**: High-precision financial calculations

### Statistical Methods

#### Forecasting Approaches

1. **Magnitude-Weighted Forecast**
   - Analyzes absolute differences between sequential values
   - Applies directional probability weighting
   - Returns three-point distribution: [lower, central, upper]

2. **Asymmetric Trend Forecast**
   - Separately models positive and negative price movements
   - Captures market asymmetry (bull vs bear behavior)
   - Weighted by historical directional probabilities

#### Core Algorithms
- **Least Deviation**: Finds values minimizing total deviation from dataset
- **Distribution Modeling**: Calculates central tendency with upper/lower bounds
- **Comparative Difference**: Configurable power-based difference calculations (default: squared differences)

## Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- SQLite (bundled with JDBC driver)

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/SequenceQueryLanguage.git
cd SequenceQueryLanguage
```

2. Build the project:
```bash
mvn clean install
```

3. Set up the environment (Linux/Mac):
```bash
source setPath.sh
```

### Database Setup

Create and populate the BTC database:
```bash
mvn exec:java -Dexec.mainClass="za.co.wethinkcode.TradeQuery.SingleCommodityDataBase.BTCDataTableCreator"
```

### Running the Application

#### Interactive Query Application
```bash
mvn exec:java -Dexec.mainClass="za.co.wethinkcode.TradeQuery.SingleCommodityDataBase.BTCQueryApp"
```

Features:
- Query specific parameters for dates
- Retrieve all data for a parameter across time
- Get complete daily snapshots
- Query date ranges
- Browse available dates and parameters

#### Forecasting Example
```bash
mvn exec:java -Dexec.mainClass="za.co.wethinkcode.TradeQuery.Implementation"
```

## Usage Examples

### Statistical Analysis

```java
List<BigDecimal> priceData = Arrays.asList(
    new BigDecimal("1.0"),
    new BigDecimal("2.0"),
    new BigDecimal("1.5"),
    new BigDecimal("2.5")
);

CentralTendency tendency = new CentralTendency(priceData);
BigDecimal mean = tendency.mean();
BigDecimal median = tendency.median();
```

### Forecasting

```java
CentralTendency tendencyInstance = new CentralTendency();
ForecastBase forecast = new ForecastBase(
    tendencyInstance, 
    tendencyInstance::meanLeastDifference, 
    priceData
);

// Get three-point forecast: [lower, central, upper]
List<BigDecimal> prediction = forecast.magnitudeWeightedForecast();
```

### Database Queries

```java
BTCDataService service = new BTCDataService();

// Get closing price for specific date
BigDecimal closePrice = service.getParameterForDate("close", "2025-03-21");

// Get all closing prices
List<BTCDataPoint> allClosePrices = service.getAllDataForParameter("close");

// Get date range
List<BTCDataPoint> march = service.getParameterForDateRange(
    "close", "2025-03-01", "2025-03-31"
);
```

## Testing

Run unit tests:
```bash
mvn test
```

Test coverage includes:
- Difference calculations (absolute, positive, negative)
- Central tendency algorithms
- Deviation and distribution modeling

## Database Schema

The `BTC_data_daily` table structure:
- **parameter**: Type of data (open, high, low, close, volume)
- **Dynamic date columns**: One column per date (format: YYYY_MM_DD)
- Values stored as `DECIMAL(15,4)` for precision

## Configuration

### Forecast Parameters
- **Probability Bias**: Set to -1 (bearish), 0 (neutral), or 1 (bullish)
- **Deviation Power**: Adjust sensitivity (default: 2 for squared differences)
- **Tendency Function**: Choose mean, median, mode, or least-difference

## Dependencies

```xml
<dependencies>
    <!-- SQLite Database -->
    <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
        <version>3.45.3.0</version>
    </dependency>
    
    <!-- JSON Processing -->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>
    
    <!-- Testing -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.9.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Future Enhancements

- [ ] Multi-commodity support beyond BTC
- [ ] Advanced time series decomposition
- [ ] Machine learning integration
- [ ] Real-time data streaming
- [ ] Web-based visualization dashboard
- [ ] REST API for remote access
- [ ] Additional statistical indicators (RSI, MACD, Bollinger Bands)

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is open source and available under the [MIT License](LICENSE).

## Author

WeThinkCode Student Project

## Acknowledgments

- Statistical methods based on classical time series analysis
- Database design optimized for financial data queries
- Forecasting algorithms incorporate probability theory and expectation mathematics
