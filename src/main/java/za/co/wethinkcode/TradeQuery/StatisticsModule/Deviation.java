package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Arrays;

public class Deviation extends StatisticsBase {
    private final Difference diffMethods;
    private final BigDecimal centralTendency;

    public Deviation(BigDecimal centralTendency, List<BigDecimal> dataList) {
        super(dataList);
        this.diffMethods = new Difference(dataList);
        this.centralTendency = centralTendency;
    }

    public BigDecimal deviation() {
        List<BigDecimal> sum = diffMethods.comparativeDifference(centralTendency);
        BigDecimal mean = sum.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(sum.size()), MathContext.DECIMAL128);
        return mean.pow(1 / power, new MathContext(10, RoundingMode.HALF_UP));
    }

    public List<BigDecimal> distribution() {
        return Arrays.asList(
            centralTendency.subtract(deviation()), 
            centralTendency, 
            centralTendency.add(deviation())
        );
    }
}