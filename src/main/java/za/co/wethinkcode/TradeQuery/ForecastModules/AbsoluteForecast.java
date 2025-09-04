package za.co.wethinkcode.TradeQuery.ForecastModules;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Supplier;

import za.co.wethinkcode.TradeQuery.StatisticsModule.CentralTendency;
import za.co.wethinkcode.TradeQuery.StatisticsModule.DeviationAndDistribution;

public class AbsoluteForecast {

    private List<BigDecimal> dataList;
    private BigDecimal fromValue;
    private int probabilityBias = 0;
    private int differenceBias = 0;
    private CentralTendency centralTendency;
    private DeviationAndDistribution devDistrInsance;

    public AbsoluteForecast(CentralTendency tendencyInstance, Supplier<BigDecimal> tendencyFunction, List<BigDecimal> dataList) {
        this.dataList = dataList;
        this.fromValue = dataList.getLast();
        this.centralTendency = tendencyInstance;
        this.devDistrInsance = new DeviationAndDistribution(tendencyInstance, tendencyFunction, dataList);
    }

    public void setProbabilityBias(int probabilityBias) {
        this.probabilityBias = probabilityBias;
    }

    public void setDifferenceBias(int differenceBias) {
        this.differenceBias = differenceBias;
    }

}
