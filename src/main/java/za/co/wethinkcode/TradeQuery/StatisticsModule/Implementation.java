package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Implementation {

    public static void main(String[] args) {

        List<BigDecimal> dataList = new ArrayList<>(Arrays.asList(
            new BigDecimal("2.0"),
            new BigDecimal("4.0"),
            new BigDecimal("6.0"),
            new BigDecimal("8.0"),
            new BigDecimal("10.0")
        ));

        CentralTendency centralTendency = new CentralTendency(dataList);
        Supplier <BigDecimal> tendencyFunction = centralTendency::mean;
        DeviationAndDistribution devDistr = new DeviationAndDistribution(centralTendency, tendencyFunction, dataList);
        BigDecimal lowerBoundValue = devDistr.getLowerBoundTendency();
        BigDecimal upperBoundValue = devDistr.getUpperBoundTendency();
        BigDecimal lowerBoundProbability = devDistr.getLowerBoundProbability();
        BigDecimal upperBoundProbability = devDistr.getUpperBoundProbability();
        Expectation expectation = new Expectation(lowerBoundValue, upperBoundValue, lowerBoundProbability, upperBoundProbability);

        devDistr.displayValues();
        System.out.println("Distribution is: " + devDistr.distribution());
        System.out.println("Expectation is: " + expectation.expectation());
    }

}


