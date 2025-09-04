package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Implementation {

    public static void main(String[] args) {

        List<BigDecimal> dataList = new ArrayList<>(Arrays.asList(
            new BigDecimal("1.0"),
            new BigDecimal("2.0"),
            new BigDecimal("3.0"),
            new BigDecimal("4.5"),
            new BigDecimal("5.0"),
            new BigDecimal("5.5"),
            new BigDecimal("6.0"),
            new BigDecimal("6.5")
        ));

        CentralTendency centralTendency = new CentralTendency(dataList);
        Supplier <BigDecimal> tendencyFunction = centralTendency::meanLeastDifference;
        DeviationAndDistribution devDistr = new DeviationAndDistribution(centralTendency, tendencyFunction, dataList);
        devDistr.setUseMean(false);
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


