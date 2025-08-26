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
    boolean useAbsolute = true;
    int power = 1;

    public CentralTendency(List<BigDecimal>  dataList) {
        if (dataList == null || dataList.isEmpty()) {
            throw new IllegalArgumentException("Data list is empty");
        }
        this.dataList = dataList;
    }
    
    public void changeUseAbsolute(boolean useAbsolute){
        this.useAbsolute = useAbsolute;
    }

    public void changePower(int power){
        if (power < 1){
            throw new IllegalArgumentException("Power must be at least 1");
        }
        this.power = power;
    }

    public List<BigDecimal> leastDifferenceMean() {
        return new LeastDifference(dataList).variableLeastDifference();
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
