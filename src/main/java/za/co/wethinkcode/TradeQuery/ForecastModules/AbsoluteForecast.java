package za.co.wethinkcode.TradeQuery.ForecastModules;

import java.math.BigDecimal;
import java.util.List;

import za.co.wethinkcode.TradeQuery.StatisticsModule.CentralTendency;
import za.co.wethinkcode.TradeQuery.StatisticsModule.DevationAndDistribution;
import za.co.wethinkcode.TradeQuery.StatisticsModule.Difference;
import java.util.function.Supplier;

public class AbsoluteForecast {

    BigDecimal fromValue;
    Difference diffMethods;
    CentralTendency centralTendency;
    DevationAndDistribution deviation = new DevationAndDistribution(getTendency(), getAbsoluteDifference());


    // Default value is the least absolute difference
    Supplier<BigDecimal> tendencyFunction = () -> centralTendency.meanLeastDifference();

    public AbsoluteForecast(List<BigDecimal> dataList) {
        this.centralTendency = new CentralTendency(dataList);
        this.diffMethods = new Difference(dataList);
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
