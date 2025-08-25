package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class LeastDifference {

    Difference diffMethods;
    List<BigDecimal> dataList;
    boolean useAbsolute = true;
    int power = 1;

    public LeastDifference(boolean useAbsolute, int power, List<BigDecimal>  dataList) {
        if (dataList == null || dataList.isEmpty()) {
        throw new IllegalArgumentException("Data list is empty");
        }
        this.dataList = dataList;
        this.diffMethods = new Difference(dataList);
        this.useAbsolute = useAbsolute;
        this.power = power;
    }


    /**
     * Returns the variable(s) from the original data list that have the least sum of absolute differences
     * when compared to all other variables in the list. This can be thought of as the variables for which is 'closest' to every other variable.
     * If multiple variables have the same least sum, all such variables are returned.
     * @return
     */
    public List<BigDecimal> variableLeastDifference(){
        List<BigDecimal> indices = indicesLeastComparitiveSums();
        List<BigDecimal> variables = new ArrayList<>();

        for (int n = 0; n < indices.size(); n++){
            variables.add(dataList.get(indices.get(n).intValue()));
        }
        return variables;
    }


    private List<BigDecimal> listComparitiveSums(){
        List<BigDecimal> listSum = new ArrayList<>();
        for(int n = 0; n < dataList.size();n++){
            listSum.add(diffMethods.sumComparitiveDifference(useAbsolute, power, dataList.get(n)));
        }
        return listSum;
    } 

    private List<BigDecimal> indicesLeastComparitiveSums(){
        List<BigDecimal> listSum = listComparitiveSums();
        BigDecimal min = listSum.stream().min(BigDecimal::compareTo).orElseThrow();

        List<BigDecimal> indices = new ArrayList<>();
        for(int n = 0; n < listSum.size();n++){
            if(listSum.get(n).compareTo(min) == 0){
                indices.add(BigDecimal.valueOf(n));
            }
        }
        return indices;
    }

}
