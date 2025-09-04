package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Implementation {

    public static void main(String[] args) {

        List<BigDecimal> dataList = new ArrayList<>(Arrays.asList(
            new BigDecimal("2.0"),
            new BigDecimal("4.0"),
            new BigDecimal("6.0"),
            new BigDecimal("8.0"),
            new BigDecimal("10.0")
        ));

        Expectation exp = new Expectation(dataList);

        System.out.println("Expectation: " + exp);

    }

}


