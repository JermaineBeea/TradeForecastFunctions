package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Difference{

    private List<BigDecimal> dataList;
    private boolean useComparitiveAbsolute = true;
    private boolean includeZero = false;
    private int comparitivePower = 2;

    public Difference(List<BigDecimal> dataList) {
        this.dataList = dataList;
    }

    public void setUseAbsolute(boolean useAbsolute) {
        this.useComparitiveAbsolute = useAbsolute;
    }

    public void setIncludeZero(boolean argument){
        this.includeZero = argument;
    }

    public void setComparitivePower(int power) {
        if (power < 1) {
            throw new IllegalArgumentException("Power must be at least 1");
        }
        comparitivePower = power;
    }

    public int returnComparitivePower(){
        return comparitivePower;
    }

    public List<BigDecimal> comparativeDifference(BigDecimal variable) {
        List<BigDecimal> differenceList = new ArrayList<>();
        for (BigDecimal value : dataList) {
            BigDecimal diff = value.subtract(variable).pow(comparitivePower);
            differenceList.add(useComparitiveAbsolute ? diff.abs() : diff);
        }
        return differenceList;
    }

    public BigDecimal sumComparitiveDifference(BigDecimal variable) {
        return comparativeDifference(variable).stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<BigDecimal> difference() {
        List<BigDecimal> differenceList = new ArrayList<>();
        for (int n = 1; n < dataList.size(); n++) {
            differenceList.add(dataList.get(n).subtract(dataList.get(n-1)));
        }
        return differenceList;
    }

    public List<BigDecimal> absoluteDifference() {
        List<BigDecimal> differenceList = new ArrayList<>();
        for (int n = 1; n < dataList.size(); n++) {
            differenceList.add(dataList.get(n).subtract(dataList.get(n-1)).abs());
        }
        return differenceList;
    }

    public List<BigDecimal> positiveDifference() {
        List<BigDecimal> differenceList = new ArrayList<>();
        int min = includeZero ? -1 : 0;

        for (int n = 1; n < dataList.size(); n++) {
            BigDecimal difference = dataList.get(n).subtract(dataList.get(n-1));
            if (difference.compareTo(BigDecimal.valueOf(min)) > 0) {
                differenceList.add(difference);
            }
        }
        return differenceList;
    }

    public List<BigDecimal> negativeDifference() {
        List<BigDecimal> differenceList = new ArrayList<>();
        int max = includeZero ? 1 : 0;

        for (int n = 1; n < dataList.size(); n++) {
            BigDecimal difference = dataList.get(n).subtract(dataList.get(n-1));
            if (difference.compareTo(BigDecimal.valueOf(max)) < 0) {
                differenceList.add(difference);
            }
        }
        return differenceList;
    }

    public List<BigDecimal> NthOrderDifference(int order, Function<List<BigDecimal>, List<BigDecimal>> function) {
        if (order < 1) {
            throw new IllegalArgumentException("Order must be at least 1");
        }
        List<BigDecimal> result = new ArrayList<>(dataList);
        for (int n = 0; n < order; n++) {
            result = function.apply(result);
        }
        return result;
    }
}