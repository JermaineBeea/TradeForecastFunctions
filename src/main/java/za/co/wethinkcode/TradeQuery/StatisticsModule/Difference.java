package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Difference {

    List<BigDecimal> dataList;
    boolean useAbsolute = true;
    int power = 1;

    public Difference(List<BigDecimal>  dataList) {
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

    public List<BigDecimal> comparativeDifference(BigDecimal variable){
        List<BigDecimal> differenceList = new ArrayList<>();
        for(int n = 0; n < dataList.size();n++){
            if(useAbsolute) {
                differenceList.add((dataList.get(n).subtract(variable)).pow(power).abs());
            } else {
                differenceList.add((dataList.get(n).subtract(variable)).pow(power));
            }
        }
        return differenceList;
    }

    public BigDecimal sumComparitiveDifference(BigDecimal variable){   
        return comparativeDifference(variable).stream()
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    

    public List<BigDecimal> difference(){
        List<BigDecimal> differenceList = new ArrayList<>();
        for(int n = 1; n < dataList.size();n++){
            differenceList.add(dataList.get(n).subtract(dataList.get(n-1)));
        }
        return differenceList;
    }

    public List<BigDecimal> absoluteDifference(){
        List<BigDecimal> differenceList = new ArrayList<>();
        for(int n = 1; n < dataList.size();n++){
            differenceList.add(dataList.get(n).subtract(dataList.get(n-1)).abs());
        }
        return differenceList;
    }

    public List<BigDecimal> positiveDifference(boolean includeZero){
        List<BigDecimal> differenceList = new ArrayList<>();
        int min = includeZero ? -1 : 0;

        for(int n = 1; n < dataList.size();n++){
            BigDecimal difference = dataList.get(n).subtract(dataList.get(n-1));
            if(difference.compareTo(BigDecimal.valueOf(min)) > 0){
                differenceList.add(difference);
            } 
        }
        return differenceList;
    }

    public List<BigDecimal> negativeDifference(boolean includeZero){
        List<BigDecimal> differenceList = new ArrayList<>();
        int max = includeZero ? 1 : 0;

        for(int n = 1; n < dataList.size();n++){
            BigDecimal difference = dataList.get(n).subtract(dataList.get(n-1));
            if(difference.compareTo(BigDecimal.valueOf(max)) < 0){
                differenceList.add(difference);
            }
        }
        return differenceList;
    }

    public List<BigDecimal> NthOrderDifference(int order, Function<List<BigDecimal>, List<BigDecimal>> function){
        if(order < 1){
            throw new IllegalArgumentException("Order must be at least 1");
        }
        List<BigDecimal> result = new ArrayList<>(dataList);
        for(int n = 0; n < order; n++){
            result = function.apply(result);
        }
        return result;
    }

}
    


   
    

