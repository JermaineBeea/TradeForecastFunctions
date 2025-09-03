package za.co.wethinkcode.TradeQuery.ForecastModules;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.HashMap;

import za.co.wethinkcode.TradeQuery.StatisticsModule.CentralTendency;
import za.co.wethinkcode.TradeQuery.StatisticsModule.StatisticsBase;

public class ForecastBase {

    protected List<BigDecimal> dataList;
    protected int probabilityBias = 0;
    protected int differenceBias = 0;
    protected StatisticsBase statsBase;
    protected CentralTendency centralTendency;
    protected HashMap<String, Function<BigDecimal, BigDecimal>> tendencyFunctions;

    public ForecastBase(List<BigDecimal> dataList) {
        this.dataList = dataList;
        this.statsBase = new StatisticsBase(dataList);
        this.centralTendency = new CentralTendency(dataList);
        this.tendencyFunctions = new HashMap<>();
        this.tendencyFunctions.put("leastdifference", centralTendency::leastDifferenceForecast);
        this.tendencyFunctions.put("mean", centralTendency::meanForecast);
        this.tendencyFunctions.put("median", centralTendency::medianForecast);
        this.tendencyFunctions.put("mode", centralTendency::modeForecast);
    }

    public void setProbabilityBias(int probabilityBias) {
        this.probabilityBias = probabilityBias;
    }

    public void setDifferenceBias(int differenceBias) {
        this.differenceBias = differenceBias;
    }

    public BigDecimal expectation(BigDecimal probA, BigDecimal eventA, BigDecimal probB, BigDecimal eventB){
        return probA.multiply(eventA).add(probB.multiply(eventB));
    }

}
