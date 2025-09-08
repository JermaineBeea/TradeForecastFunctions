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

    // Probaility Bias
    private int probailityBias = 0;

    // Data parameters
    private List<BigDecimal> differenceData;
    private List<BigDecimal> posDifferenceData;
    private List<BigDecimal> negDifferenceData;
    private List<BigDecimal> absDifferenceData;

    // Value parameters
    private BigDecimal fromValue;
    private BigDecimal negDiffProbability;
    private BigDecimal posDiffProbability;

    // Function
    Supplier<BigDecimal> tendencyFunction;

    
    public ForecastBase(CentralTendency tendencyInstance, Supplier<BigDecimal> tendencyFunction, List<BigDecimal> dataList) {
        this.fromValue = dataList.getLast();
        this.differenceInstance = new Difference(dataList);
        this.tendencyFunction = tendencyFunction;
        this.tendencyInstance = tendencyInstance;
        differenceInstance.setIncludeZero(false);
        this.differenceData = differenceInstance.difference();
        this.absDifferenceData = differenceInstance.absoluteDifference();
        this.posDifferenceData = differenceInstance.positiveDifference();
        this.negDifferenceData = differenceInstance.negativeDifference();
        calculateProbailities();
        applyBiasToProbability();
    }

    public void setProbabilityBias(int biasArgument){
        this.probailityBias = biasArgument;
        applyBiasToProbability();
    }

    public void setFromValue(BigDecimal fromArg){
        this.fromValue = fromArg;
    }

    public CentralTendency returnTendencyInstance(){
        return tendencyInstance;
    }

    public List<BigDecimal> magnitudeWeightedForecast(){
        tendencyInstance.setData(absDifferenceData);
        this.absDeviationDistrInstance = new DeviationAndDistribution(tendencyInstance, tendencyFunction, absDifferenceData);
        BigDecimal absDiffCentralTendency = absDeviationDistrInstance.getDistributionTendency();
        BigDecimal absDiffLowerBoundTendency = absDeviationDistrInstance.getLowerBoundTendency();
        BigDecimal absDiffUpperBoundTendency  = absDeviationDistrInstance.getUpperBoundTendency();
        BigDecimal lowerBoundDiffExpecation = new Expectation(absDiffLowerBoundTendency.negate(), absDiffLowerBoundTendency, negDiffProbability, posDiffProbability).expectation();
        BigDecimal centralDiffExpecation = new Expectation(absDiffCentralTendency.negate(), absDiffCentralTendency, negDiffProbability, posDiffProbability).expectation();
        BigDecimal upperBoundDiffExpecation = new Expectation(absDiffUpperBoundTendency.negate(), absDiffUpperBoundTendency, negDiffProbability, posDiffProbability).expectation();
        List<BigDecimal> result = new ArrayList<>();
        result.add(fromValue.add(lowerBoundDiffExpecation));
        result.add(fromValue.add(centralDiffExpecation));
        result.add(fromValue.add(upperBoundDiffExpecation));
        return result;
    }

    public List<BigDecimal> asymmetricTrendForecast(){
        BigDecimal posDistrLowerBoundTendency = BigDecimal.ZERO;
        BigDecimal posDistrCentralTendency = BigDecimal.ZERO;
        BigDecimal posDistrUpperBoundTendency = BigDecimal.ZERO;
        BigDecimal negDistrLowerBoundTendency = BigDecimal.ZERO;
        BigDecimal negDistrCentralTendency = BigDecimal.ZERO;
        BigDecimal negDistrUpperBoundTendency = BigDecimal.ZERO;

        if(posDifferenceData.size() > 0){
            tendencyInstance.setData(posDifferenceData);
            this.posDeviationDistrInstance = new DeviationAndDistribution(tendencyInstance, tendencyFunction, posDifferenceData);
            posDistrLowerBoundTendency = posDeviationDistrInstance.getLowerBoundTendency();
            posDistrCentralTendency = posDeviationDistrInstance.getDistributionTendency();
            posDistrUpperBoundTendency = posDeviationDistrInstance.getUpperBoundTendency();
        }
        if(negDifferenceData.size() > 0){
            tendencyInstance.setData(negDifferenceData);
            this.negDeviationDistrInstance = new DeviationAndDistribution(tendencyInstance, tendencyFunction, negDifferenceData);
            negDistrLowerBoundTendency = negDeviationDistrInstance.getLowerBoundTendency();
            negDistrCentralTendency = negDeviationDistrInstance.getDistributionTendency();
            negDistrUpperBoundTendency = negDeviationDistrInstance.getUpperBoundTendency();
        }
        BigDecimal lowerBoundDiffExpecation = new Expectation(negDistrLowerBoundTendency, posDistrLowerBoundTendency, negDiffProbability, posDiffProbability).expectation();
        BigDecimal centralDiffExpecation = new Expectation(negDistrCentralTendency, posDistrCentralTendency, negDiffProbability, posDiffProbability).expectation();
        BigDecimal upperBoundDiffExpecation = new Expectation(negDistrUpperBoundTendency, posDistrUpperBoundTendency, negDiffProbability, posDiffProbability).expectation();
        List<BigDecimal> result = new ArrayList<>();
        result.add(fromValue.add(lowerBoundDiffExpecation));
        result.add(fromValue.add(centralDiffExpecation));
        result.add(fromValue.add(upperBoundDiffExpecation));
        return result;
    }


    private void calculateProbailities(){
        BigDecimal differnceSize = BigDecimal.valueOf(differenceData.size());
        this.negDiffProbability = BigDecimal.valueOf(negDifferenceData.size()).divide(differnceSize, 10, RoundingMode.HALF_UP);
        this.posDiffProbability = BigDecimal.valueOf(posDifferenceData.size()).divide(differnceSize, 10, RoundingMode.HALF_UP);
    }

    private void applyBiasToProbability(){
        boolean comparison = negDiffProbability.compareTo(posDiffProbability) > 0;
        if(!comparison && probailityBias == -1 || comparison && probailityBias == 1){
            BigDecimal temporary = negDiffProbability;
            negDiffProbability = posDiffProbability;
            posDiffProbability = temporary;
        }
    }
}
