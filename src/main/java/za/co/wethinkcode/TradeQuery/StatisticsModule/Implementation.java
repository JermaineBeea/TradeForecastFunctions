package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.util.List;

public class Implementation {

    public static void main(String[] args) {

        List<BigDecimal> dataList = List.of(
        new BigDecimal("10.2"),
        new BigDecimal("20.3"),
        new BigDecimal("10.5"),
        new BigDecimal("30.7"),
        new BigDecimal("40.2")
    );

    StatisticsBase statsBase = new StatisticsBase(dataList);
        statsBase.changeUseAbsolute(false);
        statsBase.changePower(2);
    }

}


