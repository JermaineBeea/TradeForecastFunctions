package za.co.wethinkcode.TradeQuery.ForecastModules;

import java.math.BigDecimal;
import java.util.List;

import za.co.wethinkcode.TradeQuery.StatisticsModule.CentralTendency;
import za.co.wethinkcode.TradeQuery.StatisticsModule.StatisticsBase;

public class ForecastBase {

    protected List<BigDecimal> dataList;
    protected int probabilityBias = 0;
    protected int differenceBias = 0;
    protected StatisticsBase statsBase;
    protected CentralTendency centralTendency;

public ForecastBase(List<BigDecimal> dataList) {
    this.dataList = dataList;
    this.statsBase = new StatisticsBase(dataList);
    this.centralTendency = new CentralTendency(dataList);
    }

    public void setProbabilityBias(int probabilityBias) {
        this.probabilityBias = probabilityBias;
    }

    public void setDifferenceBias(int differenceBias) {
        this.differenceBias = differenceBias;
    }



}
