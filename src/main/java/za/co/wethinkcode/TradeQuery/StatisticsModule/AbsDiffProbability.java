package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AbsDiffProbability extends StatisticsBase {

    protected final List<BigDecimal> dataList;

    private Difference diffMethods = new Difference();
    private CentralTendency centralTendency = new CentralTendency();
    private DevationAndDistribution devationAndDistribution;

    private BigDecimal absDiffTendency;
    private List<BigDecimal> absDifferenceList;
    private List<BigDecimal> asbDistribution;
    private BigDecimal lowerBoundProbability;
    private BigDecimal upperBoundProbability;
    private List<BigDecimal> lowerBoundValues;
    private List<BigDecimal> upperBoundValues;
    private BigDecimal lowerBoundTendency;
    private BigDecimal upperBoundTendency;

    private Supplier<BigDecimal> tendencyFunction = centralTendency::meanLeastDifference;

    public AbsDiffProbability(List<BigDecimal> dataList) {
        super(dataList);
        this.dataList = dataList;
        subConstructor();
    }

    public AbsDiffProbability() {
        super();
        this.dataList = getDataList();
        subConstructor();
    }

    public void subConstructor(){
        this.absDifferenceList = diffMethods.absoluteDifference();
        this.absDiffTendency = this.tendencyFunction.get();
        this.asbDistribution = getDifferenceDistribution();
        lowerBoundUtility();
        upperBoundUtility();
        setBoundaryTendencies();
    }

    public void setTendencyFunction(Supplier<BigDecimal> tendencyFunction) {
        this.tendencyFunction = tendencyFunction;
    }

    public BigDecimal expectation(){
        return lowerBoundTendency.multiply(lowerBoundProbability)
               .add(upperBoundTendency.multiply(upperBoundProbability));
    }

    private List<BigDecimal> getDifferenceDistribution(){
        this.devationAndDistribution = new DevationAndDistribution(absDiffTendency, absDifferenceList);
        return this.devationAndDistribution.distribution();
    }

    private void setBoundaryTendencies(){
        centralTendency.setLocalData(lowerBoundValues);
        this.lowerBoundTendency = tendencyFunction.get();    

        centralTendency.setLocalData(upperBoundValues);
        this.upperBoundTendency = tendencyFunction.get();

        // Reset central Tendency class data
        centralTendency.resetLocalData();
    }

    private void lowerBoundUtility(){
        int count = 0;
        for (BigDecimal value : absDifferenceList) {
            if ((value.compareTo(asbDistribution.get(0)) > 0) && (value.compareTo(asbDistribution.get(1)) < 0)) {
                count++;
                this.lowerBoundValues.add(value);
            }
        }
        this.lowerBoundProbability = BigDecimal.valueOf(count).divide(BigDecimal.valueOf(asbDistribution.size()), RoundingMode.HALF_UP);
    }

    private void upperBoundUtility(){
        int count = 0;
        for (BigDecimal value : absDifferenceList) {
            if ((value.compareTo(asbDistribution.get(1)) > 0) && (value.compareTo(asbDistribution.get(2)) < 0)) {
                count++;
                this.upperBoundValues.add(value);
            }
        }
        this.upperBoundProbability = BigDecimal.valueOf(count).divide(BigDecimal.valueOf(asbDistribution.size()), RoundingMode.HALF_UP);
    }
}
