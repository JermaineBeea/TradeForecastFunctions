package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Implementation {

    public static void main(String[] args) {

        BigDecimal expectation = BigDecimal.ZERO;

        List<BigDecimal> dataList = List.of(
        new BigDecimal("2.0"),
        new BigDecimal("4.0"),
        new BigDecimal("6.0"),
        new BigDecimal("8.0"),
        new BigDecimal("10.0")
    );

        StatisticsBase statsBase = new StatisticsBase(dataList);

        Expectation expectationInsta = new Expectation();

        expectation = expectationInsta.expectation();

        System.out.println("Expectation: " + expectation);

    }

}


