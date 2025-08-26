package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.util.List;

public class StatisticsBase {
    protected List<BigDecimal> dataList;
    protected boolean useAbsolute = true;
    protected int power = 1;

    public StatisticsBase(List<BigDecimal> dataList) {
        validateDataList(dataList);
        this.dataList = dataList;
    }

    public StatisticsBase() {
       
    }

    public List<BigDecimal> getDataList() {
        return dataList;
    }

    public void setDataList(List<BigDecimal> dataList) {
        validateDataList(dataList);
        this.dataList.clear();
        this.dataList.addAll(dataList);
    }

    private void validateDataList(List<BigDecimal> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            throw new IllegalArgumentException("Data list is empty");
        }
    }

    public void changeUseAbsolute(boolean useAbsolute) {
        this.useAbsolute = useAbsolute;
    }

    public void changePower(int power) {
        if (power < 1) {
            throw new IllegalArgumentException("Power must be at least 1");
        }
        this.power = power;
    }
}