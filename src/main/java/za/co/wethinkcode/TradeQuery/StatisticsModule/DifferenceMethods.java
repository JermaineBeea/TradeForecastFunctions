package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DifferenceMethods {

    List<BigDecimal> dataList;

    public DifferenceMethods(List<BigDecimal>  dataList) {
        if (dataList == null || dataList.isEmpty()) {
        throw new IllegalArgumentException("Data list is empty");
        }
        this.dataList = dataList;
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
        int min = includeZero ? 0 : 1;

        for(int n = 1; n < dataList.size();n++){
            BigDecimal difference = dataList.get(n).subtract(dataList.get(n-1));
            if(difference.compareTo(BigDecimal.ZERO) > min){
                differenceList.add(difference);
            } else {
                differenceList.add(BigDecimal.ZERO);
            }
        }
        return differenceList;
    }

    public List<BigDecimal> negativeDifference(boolean includeZero){
        List<BigDecimal> differenceList = new ArrayList<>();
        int min = includeZero ? 0 : 1;

        for(int n = 1; n < dataList.size();n++){
            BigDecimal difference = dataList.get(n).subtract(dataList.get(n-1));
            if(difference.compareTo(BigDecimal.ZERO) < min){
                differenceList.add(difference.abs());
            } else if (includeZero) {
                differenceList.add(BigDecimal.ZERO);
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
    


   
    

