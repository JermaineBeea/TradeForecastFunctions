package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CentralTendency extends StatisticsBase {

    protected final List<BigDecimal> dataList;

    public CentralTendency(List<BigDecimal> dataList) {
        super(dataList);
        this.dataList = dataList;
    }

    public CentralTendency() {
        super();
        this.dataList = getDataList();
    }


    public List<BigDecimal> leastDifferenceMean() {
        return new LeastDeviation(dataList).variableLeastDifference();
    }

    public BigDecimal mean() {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal value : dataList) {
            sum = sum.add(value);
        }
        return sum.divide(BigDecimal.valueOf(dataList.size()), RoundingMode.HALF_UP);
    }

    public BigDecimal median() {
        List<BigDecimal> sortedList = new ArrayList<>(dataList);
        Collections.sort(sortedList);
        int middle = sortedList.size() / 2;
        if (sortedList.size() % 2 == 1) {
            return sortedList.get(middle);
        } else {
            return sortedList.get(middle - 1).add(sortedList.get(middle))
                   .divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
        }
    }

    public BigDecimal mode() {
        Map<BigDecimal, Integer> frequencyMap = new HashMap<>();
        for (BigDecimal value : dataList) {
            frequencyMap.put(value, frequencyMap.getOrDefault(value, 0) + 1);
        }
        return Collections.max(frequencyMap.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}