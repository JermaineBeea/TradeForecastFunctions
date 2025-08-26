package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Arrays;

public class Deviation {

    List<BigDecimal> dataList;
    Difference diffMethods;
    boolean useAbsolute = true;
    int power = 1;
    BigDecimal centralTendency;


    public Deviation(BigDecimal centralTendency, List<BigDecimal>  dataList) {
        if (dataList == null || dataList.isEmpty()) {
            throw new IllegalArgumentException("Data list is empty");
        }
        this.diffMethods = new Difference(dataList);
        this.dataList = dataList;
        this.centralTendency = centralTendency;
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


    public BigDecimal deviation(){
        List<BigDecimal> sum = diffMethods.comparativeDifference(centralTendency);
       BigDecimal mean = sum.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
               .divide(BigDecimal.valueOf(sum.size()), MathContext.DECIMAL128);
       return mean.pow(1/power, MathContext.DECIMAL128).setScale(10, RoundingMode.HALF_UP);
    }

    public List <BigDecimal> distribution(){
        return Arrays.asList(centralTendency.subtract(deviation()), centralTendency, centralTendency.add(deviation()));
    }
}

