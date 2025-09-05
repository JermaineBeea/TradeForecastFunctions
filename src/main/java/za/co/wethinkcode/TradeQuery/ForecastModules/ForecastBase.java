package za.co.wethinkcode.TradeQuery.ForecastModules;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import za.co.wethinkcode.TradeQuery.StatisticsModule.CentralTendency;
import za.co.wethinkcode.TradeQuery.StatisticsModule.DeviationAndDistribution;
import za.co.wethinkcode.TradeQuery.StatisticsModule.Difference;
import za.co.wethinkcode.TradeQuery.StatisticsModule.Expectation;

public class ForecastBase {

    // Class Instances
    private Difference differenceInstance;
    private CentralTendency tendencyInstance;
    private DeviationAndDistribution absDeviationDistrInstance;
    private DeviationAndDistribution posDeviationDistrInstance;
    private DeviationAndDistribution negDeviationDistrInstance;

    private int probailityBias = 0;

    // Data parameters
    private List<BigDecimal> differenceData;
    private List<BigDecimal> posDifferenceData;
    private List<BigDecimal> negDifferenceData;
    private List<BigDecimal> absDifferenceData;

    // Value parameters
    private BigDecimal fromValue;
    private BigDecimal negativeDiffProbaility;
    private BigDecimal positiveDiffProbability;

    
    public ForecastBase(CentralTendency tendencyInstance, Supplier<BigDecimal> tendencyFunction, List<BigDecimal> dataList) {
        this.differenceInstance = new Difference(dataList);
        differenceInstance.setIncludeZero(false);
        this.absDifferenceData = differenceInstance.absoluteDifference();
        this.posDifferenceData = differenceInstance.positiveDifference();
        this.negDifferenceData = differenceInstance.negativeDifference();
        tendencyInstance.setData(absDifferenceData);
        this.absDeviationDistrInstance = new DeviationAndDistribution(tendencyInstance, tendencyFunction, absDifferenceData);
        tendencyInstance.setData(posDifferenceData);
        this.posDeviationDistrInstance = new DeviationAndDistribution(tendencyInstance, tendencyFunction, posDifferenceData);
        tendencyInstance.setData(negDifferenceData);
        this.negDeviationDistrInstance = new DeviationAndDistribution(tendencyInstance, tendencyFunction, negDifferenceData);
        calculateProbailities();
        biasProbability();
    }

    public void setProbabilityBias(int biasArgument){
        this.probailityBias = biasArgument;
        biasProbability();
    }

    public CentralTendency returnTendencyInstance(){
        return tendencyInstance;
    }

    public List<BigDecimal> absoluteForecastDistribution(){
        BigDecimal absDiffCentralTendency = absDeviationDistrInstance.getDistributionTendency();
        BigDecimal absDiffLowerBoundTendency = absDeviationDistrInstance.getLowerBoundTendency();
        BigDecimal absDiffUpperBoundTendency  = absDeviationDistrInstance.getUpperBoundTendency();
        BigDecimal lowerBoundDiffExpecation = new Expectation(absDiffLowerBoundTendency.negate(), absDiffLowerBoundTendency, negativeDiffProbaility, positiveDiffProbability).expectation();
        BigDecimal centralDiffExpecation = new Expectation(absDiffCentralTendency.negate(), absDiffCentralTendency, negativeDiffProbaility, positiveDiffProbability).expectation();
        BigDecimal upperBoundDiffExpecation = new Expectation(absDiffUpperBoundTendency.negate(), absDiffUpperBoundTendency, negativeDiffProbaility, positiveDiffProbability).expectation();
        List<BigDecimal> result = new ArrayList<>();
        result.add(fromValue.add(lowerBoundDiffExpecation));
        result.add(fromValue.add(centralDiffExpecation));
        result.add(fromValue.add(upperBoundDiffExpecation));
        return result;
    }


    private void calculateProbailities(){
        BigDecimal differnceSize = BigDecimal.valueOf(differenceData.size());
        this.negativeDiffProbaility = BigDecimal.valueOf(negDifferenceData.size()).divide(differnceSize, 10, RoundingMode.HALF_UP);
        this.positiveDiffProbability = BigDecimal.valueOf(posDifferenceData.size()).divide(differnceSize, 10, RoundingMode.HALF_UP);
    }

    private void biasProbability(){
        boolean comparison = negativeDiffProbaility.compareTo(positiveDiffProbability) > 0;
        if(!comparison && probailityBias == -1 || comparison && probailityBias == 1){
            BigDecimal temporary = negativeDiffProbaility;
            negativeDiffProbaility = positiveDiffProbability;
            positiveDiffProbability = temporary;
        }
    }
}
