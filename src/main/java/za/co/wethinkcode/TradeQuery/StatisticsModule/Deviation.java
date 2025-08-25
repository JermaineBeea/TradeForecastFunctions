package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

public class Deviation {

    List<BigDecimal> dataList;
    DifferenceMethods diffMethods;


    public Deviation(List<BigDecimal>  dataList) {
        if (dataList == null || dataList.isEmpty()) {
            throw new IllegalArgumentException("Data list is empty");
        }
        this.diffMethods = new DifferenceMethods(dataList);
        this.dataList = dataList;
    }

    public BigDecimal normalDeviation(boolean useAbsolute, int power, BigDecimal centralTendency){
        List<BigDecimal> sum = diffMethods.comparativeDifference(useAbsolute, power, centralTendency);
       BigDecimal mean = sum.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
               .divide(BigDecimal.valueOf(sum.size()), MathContext.DECIMAL128);
       return mean.pow(1/power, MathContext.DECIMAL128).setScale(10, RoundingMode.HALF_UP);
    }
}

