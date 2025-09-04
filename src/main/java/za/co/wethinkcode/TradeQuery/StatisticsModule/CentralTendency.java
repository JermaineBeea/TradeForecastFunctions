package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class CentralTendency {
    Logger logger = Logger.getLogger(CentralTendency.class.getName());
    
    private List<BigDecimal> dataList;
    
    public CentralTendency(List<BigDecimal> dataList) {
        this.dataList = new ArrayList<>(dataList); // Create defensive copy
    }


    public void setData(List<BigDecimal> newDataList){
        this.dataList = new ArrayList<>(newDataList); // Create defensive copy
    }
    
    public List<BigDecimal> getData() {
        return new ArrayList<>(dataList); // Return defensive copy
    }
    
    public List<BigDecimal> leastDifference() {
        return new LeastDeviation(dataList).variableLeastDifference();
    }
    
    public BigDecimal meanLeastDifference(){
        List<BigDecimal> leastDiff = leastDifference();
        if (leastDiff.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal value : leastDiff) {
            sum = sum.add(value);
        }
        return sum.divide(BigDecimal.valueOf(leastDiff.size()), 10, RoundingMode.HALF_UP);
    }
    
    public BigDecimal mean() {
        if (dataList.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal value : dataList) {
            sum = sum.add(value);
        }
        return sum.divide(BigDecimal.valueOf(dataList.size()), 10, RoundingMode.HALF_UP);
    }
    
    public BigDecimal median() {
        if (dataList.isEmpty()) {
            return BigDecimal.ZERO;
        }
        List<BigDecimal> sortedList = new ArrayList<>(dataList);
        Collections.sort(sortedList);
        int middle = sortedList.size() / 2;
        if (sortedList.size() % 2 == 1) {
            return sortedList.get(middle);
        } else {
            return sortedList.get(middle - 1).add(sortedList.get(middle))
                   .divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_UP);
        }
    }
    
    public BigDecimal mode() {
        if (dataList.isEmpty()) {
            return BigDecimal.ZERO;
        }
        Map<BigDecimal, Integer> frequencyMap = new HashMap<>();
        for (BigDecimal value : dataList) {
            frequencyMap.put(value, frequencyMap.getOrDefault(value, 0) + 1);
        }
        return Collections.max(frequencyMap.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}