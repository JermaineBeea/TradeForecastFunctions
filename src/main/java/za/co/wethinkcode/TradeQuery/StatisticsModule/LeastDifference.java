package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class LeastDifference {

    Difference diffMethods;
    List<BigDecimal> dataList;
    boolean useAbsolute = true;
    int power = 1;

    public LeastDifference(List<BigDecimal>  dataList) {
        if (dataList == null || dataList.isEmpty()) {
        throw new IllegalArgumentException("Data list is empty");
        }
        this.dataList = dataList;
        this.diffMethods = new Difference(dataList);
   
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
            listSum.add(diffMethods.sumComparitiveDifference(dataList.get(n)));
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
