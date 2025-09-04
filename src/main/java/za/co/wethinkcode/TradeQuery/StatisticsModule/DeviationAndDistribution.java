package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeviationAndDistribution {
    Logger logger = Logger.getLogger(DeviationAndDistribution.class.getName());
    private CentralTendency tendencyInstance;
    private Supplier<BigDecimal> tendencyFunction;
    private List<BigDecimal> dataList;
    private BigDecimal dataListSize;
    private int deviationPower;
    private Difference diffMethods;
    private BigDecimal deviation;
    private BigDecimal distributionTendency;
    private BigDecimal distributionMin;
    private BigDecimal distributionMax;
    private BigDecimal lowerBoundProbability;
    private BigDecimal upperBoundProbability;
    private BigDecimal lowerBoundTendency;
    private BigDecimal upperBoundTendency;
    private List<BigDecimal> lowerBoundValues;
    private List<BigDecimal> upperBoundValues;
    private boolean useMean = true;

    {
        logger.setLevel(Level.OFF);
    }
    public DeviationAndDistribution(CentralTendency tendencyInstance, Supplier<BigDecimal> tendencyFunction, List<BigDecimal> dataList) {
        this.dataList = dataList;
        this.tendencyInstance = tendencyInstance;
        this.tendencyFunction = tendencyFunction;
        this.dataListSize = BigDecimal.valueOf(dataList.size());
        this.diffMethods = new Difference(dataList);
        this.deviationPower = diffMethods.returnComparitivePower(); // Fixed method name
        
        // Initialize the lists before using them
        this.lowerBoundValues = new ArrayList<>();
        this.upperBoundValues = new ArrayList<>();
        
        // Fixed order: get tendency first, then calculate everything else
        calculateTendencies();
        calculateDeviation();
        calculateDistributionValues();
        calculateBoundaryValues();
    }

    public BigDecimal deviation(){
        return this.deviation;
    }
    
    public void setDeviationComparitivePower(int power){
        if (power < 1) {
            throw new IllegalArgumentException("Power must be at least 1");
        }
        this.deviationPower = power;
        this.diffMethods.setComparitivePower(power); // Fixed method name based on your Difference class
    }

    public int returnDeviationPower(){
        return deviationPower;
    }

    public void setUseMean(boolean booleanArg){
        this.useMean = booleanArg;
    }

    public void setTendencyFunction(Supplier<BigDecimal> tendencyFunction){
        this.tendencyFunction = tendencyFunction;
    }

    public List<BigDecimal> returnList(){
        return this.dataList;
    }

    public List<BigDecimal> distribution() {
        return Arrays.asList(
            distributionMin, 
            distributionTendency, 
            distributionMax
        );
    }

    public void displayValues(){
        System.out.println("Lower bound tendency: " + lowerBoundTendency);
        System.out.println("Upper bound tendency: " + upperBoundTendency);
        System.out.println("Lower bound probability: " + lowerBoundProbability);
        System.out.println("Upper bound probability: " + upperBoundProbability);
        System.out.println("Deviation: " + deviation);
    }

    // Getters for the boundary values
    public BigDecimal getLowerBoundProbability() {
        return lowerBoundProbability;            

    }

    public BigDecimal getUpperBoundProbability() {
        return upperBoundProbability;
    }

    public BigDecimal getLowerBoundTendency() {
        return lowerBoundTendency;
    }

    public BigDecimal getUpperBoundTendency() {
        return upperBoundTendency;
    }

    public List<BigDecimal> getLowerBoundValues() {
        return new ArrayList<>(lowerBoundValues);
    }

    public List<BigDecimal> getUpperBoundValues() {
        return new ArrayList<>(upperBoundValues);
    }

    private void calculateDeviation() {
        List<BigDecimal> sum = diffMethods.comparativeDifference(distributionTendency);
        BigDecimal mean = sum.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(sum.size()), 10, RoundingMode.HALF_UP);
        this.deviation = mean.pow(1 / deviationPower, new MathContext(10, RoundingMode.HALF_UP));
    }

    private void calculateDistributionValues(){
        this.distributionMax = this.distributionTendency.add(this.deviation);
        this.distributionMin = this.distributionTendency.subtract(this.deviation);
    }

    private void calculateTendencies(){
        // First, get the main tendency using original data
        this.tendencyInstance.setData(this.dataList);
        this.distributionTendency = tendencyFunction.get();
        
        // Note: boundary tendencies will be calculated after boundary values are calculateed
    }

    private void calculateBoundaryValues(){
        int upperCount = 0;
        int lowerCount = 0;
        
        // Clear the lists first
        this.lowerBoundValues.clear();
        this.upperBoundValues.clear();
        
        for(BigDecimal value: dataList){
            if(value.compareTo(distributionMin) >= 0 && value.compareTo(distributionTendency) < 0){
                lowerCount++;
                lowerBoundValues.add(value);
                logger.info("Value: " + value + " DistrMin: " + distributionMin + " comparison: " + value.compareTo(distributionMin));
            }
            if(value.compareTo(distributionTendency) > 0 && value.compareTo(distributionMax) <= 0){
                upperCount++;
                upperBoundValues.add(value);
                logger.info("Value: " + value + " DistrMax: " + distributionMax + " comparison: " + value.compareTo(distributionMax));
            }
        }
        logger.info("Lower count is: " + lowerCount);
        logger.info("Upper count is: " + upperCount);
        this.lowerBoundProbability = BigDecimal.valueOf(lowerCount).divide(dataListSize, 10, RoundingMode.HALF_UP);
        this.upperBoundProbability = BigDecimal.valueOf(upperCount).divide(dataListSize,10,  RoundingMode.HALF_UP);
        logger.info("Lower probability is: " + lowerBoundProbability);
        logger.info("Upper probability is: " + upperBoundProbability);
        
        // Now calculate boundary tendencies after we have the boundary values
        calculateBoundaryTendencies();
    }

    private void calculateBoundaryTendencies() {
        if (lowerBoundValues.isEmpty()) {
            this.lowerBoundTendency = distributionMin;
        } else {
            if (useMean) {
                this.lowerBoundTendency = distributionTendency.add(distributionMin)
                    .divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_UP);
            } else {
                this.tendencyInstance.setData(lowerBoundValues);
                this.lowerBoundTendency = tendencyFunction.get();
            }
        }
        
        if (upperBoundValues.isEmpty()) {
            this.upperBoundTendency = distributionMax;
        } else {
            if (useMean) {
                this.upperBoundTendency = distributionTendency.add(distributionMax)
                    .divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_UP);
            } else {
                this.tendencyInstance.setData(upperBoundValues);
                this.upperBoundTendency = tendencyFunction.get();
            }
        }
        
        // Restore original data to the tendency instance
        this.tendencyInstance.setData(this.dataList);
    }
}