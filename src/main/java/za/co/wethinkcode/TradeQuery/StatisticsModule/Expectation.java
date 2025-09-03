package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Expectation extends StatisticsBase {

    protected final List<BigDecimal> dataList;

    private Difference diffMethods = new Difference();
    private CentralTendency centralTendency = new CentralTendency();
    private DevationAndDistribution devationAndDistribution;

    private BigDecimal absDiffTendency;
    private List<BigDecimal> absDifferenceList = new ArrayList<>();
    private List<BigDecimal> asbDistribution = new ArrayList<>();
    private BigDecimal lowerBoundProbability;
    private BigDecimal upperBoundProbability;
    private List<BigDecimal> lowerBoundValues = new ArrayList<>();
    private List<BigDecimal> upperBoundValues = new ArrayList<>();
    private BigDecimal lowerBoundTendency;
    private BigDecimal upperBoundTendency;

    private Supplier<BigDecimal> tendencyFunction = centralTendency::meanLeastDifference;

    public Expectation(List<BigDecimal> dataList) {
        super(dataList);
        this.dataList = dataList;
        subConstructor();
    }

    public Expectation() {
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
