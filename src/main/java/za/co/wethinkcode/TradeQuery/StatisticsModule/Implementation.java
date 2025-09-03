package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Implementation {

    public static void main(String[] args) {

        BigDecimal expectation = BigDecimal.ZERO;

        List<BigDecimal> dataList = new ArrayList<>(Arrays.asList(
            new BigDecimal("2.0"),
            new BigDecimal("4.0"),
            new BigDecimal("6.0"),
            new BigDecimal("8.0"),
            new BigDecimal("10.0")
        ));

        new StatisticsBase(dataList);

        CentralTendency centralTendency = new CentralTendency();
        Expectation exp = new Expectation();

        System.out.println("Data from central tendency is " + centralTendency.getDataList());
        System.out.println("Data from cent tend initialied in expectation: " + exp.centralTendency.getDataList());

    }

}


