package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Arrays;

public class Deviation {

    List<BigDecimal> dataList;
    Difference diffMethods;
    boolean useAbsolute;
    int power;
    BigDecimal centralTendency;


    public Deviation(boolean useAbsolute, int power, BigDecimal centralTendency, List<BigDecimal>  dataList) {
        if (dataList == null || dataList.isEmpty()) {
            throw new IllegalArgumentException("Data list is empty");
        }
        this.diffMethods = new Difference(dataList);
        this.dataList = dataList;
        this.useAbsolute = useAbsolute;
        this.power = power;
        this.centralTendency = centralTendency;
    }


    /**
     * Calculates the deviation of the data set from a given central tendency measure.
     * @param useAbsolute If true, uses absolute differences; if false, uses signed differences.
     * @param power The power to which differences are raised (e.g., 2 for variance). Where power = 2, this is equivalent to standard deviation.
     * @param centralTendency The central tendency measure (mean, median, mode, or least difference mean).
     * @return The calculated deviation as a BigDecimal.
     */
    public BigDecimal deviation(){
        List<BigDecimal> sum = diffMethods.comparativeDifference(useAbsolute, power, centralTendency);
       BigDecimal mean = sum.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
               .divide(BigDecimal.valueOf(sum.size()), MathContext.DECIMAL128);
       return mean.pow(1/power, MathContext.DECIMAL128).setScale(10, RoundingMode.HALF_UP);
    }

    public List <BigDecimal> distribution(){
        return Arrays.asList(centralTendency.subtract(deviation()), centralTendency, centralTendency.add(deviation()));
    }
}

