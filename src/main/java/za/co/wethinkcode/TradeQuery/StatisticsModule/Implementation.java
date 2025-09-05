package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import za.co.wethinkcode.TradeQuery.ForecastModules.ForecastBase;

public class Implementation {

    static List<BigDecimal> dataList = new ArrayList<>(Arrays.asList(
            new BigDecimal("1.0"),
            new BigDecimal("2.0"),
            new BigDecimal("3.0"),
            new BigDecimal("4.5"),
            new BigDecimal("5.0"),
            new BigDecimal("5.5"),
            new BigDecimal("6.0"),
            new BigDecimal("6.5")
    ));


    public static void main(String[] args) {
        CentralTendency tendencyInstance = new CentralTendency();
        ForecastBase forecast = new ForecastBase(tendencyInstance, tendencyInstance :: meanLeastDifference, dataList);
        List<BigDecimal> forecastDistribution = forecast.absoluteForecastDistribution();
        System.out.println("Distribution is: " + forecastDistribution);
    }


    static public void implementation1(){
        CentralTendency centralTendency = new CentralTendency(dataList);
        Difference differenceInstance = new Difference(dataList);

        List<BigDecimal> differenceData = differenceInstance.absoluteDifference();

        Supplier <BigDecimal> tendencyFunction = centralTendency::meanLeastDifference;
        DeviationAndDistribution devDistr = new DeviationAndDistribution(centralTendency, tendencyFunction, differenceData);

        devDistr.setUseMean(true);
        BigDecimal lowerBoundValue = devDistr.getLowerBoundTendency();
        BigDecimal upperBoundValue = devDistr.getUpperBoundTendency();
        BigDecimal lowerBoundProbability = devDistr.getLowerBoundProbability();
        BigDecimal upperBoundProbability = devDistr.getUpperBoundProbability();
        Expectation expectationInstance = new Expectation(lowerBoundValue, upperBoundValue, lowerBoundProbability, upperBoundProbability);

        BigDecimal differenceExpectation = expectationInstance.expectation();
        BigDecimal expectedValue = dataList.getLast().add(differenceExpectation);
        devDistr.displayValues();
        System.out.println("Deviation power is: " + devDistr.returnDeviationPower());
        System.out.println("Distribution is: " + devDistr.distribution());
        System.out.println("Expectation is: " +expectedValue);
    }

}


