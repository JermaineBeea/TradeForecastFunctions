package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class CentralTendency {

    List<BigDecimal> dataList;

    public CentralTendency(List<BigDecimal>  dataList) {
        if (dataList == null || dataList.isEmpty()) {
            throw new IllegalArgumentException("Data list is empty");
        }
        this.dataList = dataList;
    }


    /*
     * Returns the variable(s) from the original data list that have the least sum of absolute differences
     * when compared to all other variables in the list. This can be thought of as the
     *  variables for which is 'closest' to every other variable.
     * If multiple variables have the same least sum, all such variables are returned.
     */
    public List<BigDecimal> leastDifferenceMean(boolean useAbsolute, int power) {
        return new LeastDifference(useAbsolute, power, dataList).variableLeastDifference();
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
            return sortedList.get(middle - 1).add(sortedList.get(middle)).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
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
