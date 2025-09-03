package za.co.wethinkcode.TradeQuery.ForecastModules;

import java.math.BigDecimal;
import java.util.List;

import za.co.wethinkcode.TradeQuery.StatisticsModule.CentralTendency;
import za.co.wethinkcode.TradeQuery.StatisticsModule.DevationAndDistribution;
import za.co.wethinkcode.TradeQuery.StatisticsModule.Difference;
import java.util.function.Supplier;

public class AbsoluteForecast extends ForecastBase {

    BigDecimal fromValue;
    Difference diffMethods = new Difference();
    CentralTendency centralTendency = new CentralTendency();
    DevationAndDistribution deviation = new DevationAndDistribution(getTendency(), getAbsoluteDifference());


    // Default value is the least absolute difference
    Supplier<BigDecimal> tendencyFunction = () -> centralTendency.meanLeastDifference();

    public AbsoluteForecast(List<BigDecimal> dataList) {
        super(dataList);
    }

    public void setTendencyFunction(Supplier<BigDecimal> tendencyFunction) {
        this.tendencyFunction = tendencyFunction;
    }

    private List<BigDecimal> getAbsoluteDifference(){
        return diffMethods.absoluteDifference();
    }

    private BigDecimal getTendency(){
        return this.tendencyFunction.get();
    }

    private List<BigDecimal> differenceDistribution(){
        return deviation.distribution();
    }
}
