package za.co.wethinkcode.TradeQuery.ForecastModules;

import java.math.BigDecimal;
import java.util.List;
import za.co.wethinkcode.TradeQuery.StatisticsModule.Difference;

public class AbsoluteForecast extends ForecastBase {

    Difference diffMethods = new Difference();

    public AbsoluteForecast(List<BigDecimal> dataList) {
        super(dataList);
    }

    public List<BigDecimal> getAbsoluteDifference(){
        return diffMethods.absoluteDifference();
    }
}
