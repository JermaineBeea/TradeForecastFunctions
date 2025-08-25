package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class LeastAbsoluteDifference {

    DifferenceMethods diffMethods;
    List<BigDecimal> dataList;

    public LeastAbsoluteDifference(List<BigDecimal>  dataList) {
        if (dataList == null || dataList.isEmpty()) {
        throw new IllegalArgumentException("Data list is empty");
        }
        this.dataList = dataList;
        this.diffMethods = new DifferenceMethods(dataList);
    }

    private List<BigDecimal> comparativeAbsoluteDifference(BigDecimal variable){
        List<BigDecimal> differenceList = new ArrayList<>();
        for(int n = 0; n < dataList.size();n++){
            differenceList.add(dataList.get(n).subtract(variable).abs());
        }
        return differenceList;
    }

    private BigDecimal sumComparitiveAbsDifference(BigDecimal variable){   
        return comparativeAbsoluteDifference(variable).stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private List<BigDecimal> listComparitiveSums(){
        List<BigDecimal> sumList = new ArrayList<>();
        for(int n = 0; n < dataList.size();n++){
            sumList.add(sumComparitiveAbsDifference(dataList.get(n)));
        }
        return sumList;
    } 

    private List<BigDecimal> indicesLeastComparitiveSums(){
        List<BigDecimal> sumList = listComparitiveSums();
        BigDecimal min = sumList.stream().min(BigDecimal::compareTo).orElseThrow();

        List<BigDecimal> indices = new ArrayList<>();
        for(int n = 0; n < sumList.size();n++){
            if(sumList.get(n).compareTo(min) == 0){
                indices.add(BigDecimal.valueOf(n));
            }
        }
        return indices;
    }

    /**
     * Returns the variable(s) from the original data list that have the least sum of absolute differences
     * when compared to all other variables in the list. This can be thought of as the variables for which is 'closest' to every other variable.
     * If multiple variables have the same least sum, all such variables are returned.
     * @return
     */
    public List<BigDecimal> variableLeastComparitiveSum(){
        List<BigDecimal> indices = indicesLeastComparitiveSums();
        List<BigDecimal> variables = new ArrayList<>();

        for (int n = 0; n < indices.size(); n++){
            variables.add(dataList.get(indices.get(n).intValue()));
        }
        return variables;
    }

}
