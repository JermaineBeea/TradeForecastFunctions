package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Arrays;

public class DevationAndDistribution {
    private final Difference diffMethods;
    private final BigDecimal tendency;
    protected final List<BigDecimal> dataList;
    int power;

    public DevationAndDistribution(BigDecimal tendencyArg, List<BigDecimal> dataList) {
        this.dataList = dataList;
        this.diffMethods = new Difference(dataList);
        this.tendency = tendencyArg;
        this.power = diffMethods.comparitivePower;
    }


    public BigDecimal deviation() {
        List<BigDecimal> sum = diffMethods.comparativeDifference(tendency);
        BigDecimal mean = sum.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(sum.size()), MathContext.DECIMAL128);
        return mean.pow(1 / power, new MathContext(10, RoundingMode.HALF_UP));
    }

    public List<BigDecimal> distribution() {
        return Arrays.asList(
            tendency.subtract(deviation()), 
            tendency, 
            tendency.add(deviation())
        );
    }
}