package za.co.wethinkcode.TradeQuery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import za.co.wethinkcode.TradeQuery.ForecastModules.ForecastBase;
import za.co.wethinkcode.TradeQuery.StatisticsModule.CentralTendency;

public class Implementation {

    static int PROBABILITY_BIAS = 0;

    static List<BigDecimal> dataList = new ArrayList<>(Arrays.asList(
            new BigDecimal("1.0"),
            new BigDecimal("2.0"),
            new BigDecimal("1.5"),
            new BigDecimal("1.0"),
            new BigDecimal("1.5"),
            new BigDecimal("2.0"),
            new BigDecimal("2.5"),
            new BigDecimal("3.0"),
            new BigDecimal("3.5"),
            new BigDecimal("4.0"),
            new BigDecimal("4.5")
    ));


    public static void main(String[] args) {

        //Implementation2
        CentralTendency tendencyInstance = new CentralTendency();
        ForecastBase forecast = new ForecastBase(tendencyInstance, tendencyInstance :: meanLeastDifference, dataList);
        forecast.setProbabilityBias(PROBABILITY_BIAS);
        List<BigDecimal> forecastDistribution1 = forecast.magnitudeWeightedForecast();
        List<BigDecimal> forecastDistribution2 = forecast.asymmetricTrendForecast();
        System.out.println("Distribution for implementation 1 is: " + forecastDistribution1);
        System.out.println("\nDistribution for implementation 2 is: " + forecastDistribution2);
        System.out.println();
    }
}


