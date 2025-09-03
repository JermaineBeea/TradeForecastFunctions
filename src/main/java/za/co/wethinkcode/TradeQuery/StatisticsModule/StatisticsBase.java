package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.util.List;

public class StatisticsBase {
    protected static List<BigDecimal> dataList;


    public StatisticsBase(List<BigDecimal> dataList) {
        validateDataList(dataList);
        StatisticsBase.dataList = dataList;
    }

    public StatisticsBase() {
       
    }

    public List<BigDecimal> getDataList() {
        return dataList;
    }

    public void setDataList(List<BigDecimal> dataList) {
        validateDataList(dataList);
        StatisticsBase.dataList.clear();
        StatisticsBase.dataList.addAll(dataList);
    }

    private void validateDataList(List<BigDecimal> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            throw new IllegalArgumentException("Data list is empty");
        }
    }


}