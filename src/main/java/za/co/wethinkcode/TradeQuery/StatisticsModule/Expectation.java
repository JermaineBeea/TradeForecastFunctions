package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;

public class Expectation {

    private BigDecimal lowerBoundValue;
    private BigDecimal upperBoundValue;
    private BigDecimal lowerBoundProbability;
    private BigDecimal upperBoundProbability;

    public Expectation(BigDecimal lowerBoundValue, BigDecimal upperBoundValue, BigDecimal lowerBoundProbability, BigDecimal upperBoundProbability) {
        this.lowerBoundValue = lowerBoundValue;
        this.upperBoundValue = upperBoundValue;
        this.lowerBoundProbability = lowerBoundProbability;
        this.upperBoundProbability = upperBoundProbability;
    }

    public Expectation(DeviationAndDistribution deviationAndDistributionInstance){
        this.lowerBoundValue = deviationAndDistributionInstance.getLowerBoundTendency();
        this.upperBoundValue = deviationAndDistributionInstance.getUpperBoundTendency();
        this.lowerBoundProbability = deviationAndDistributionInstance.getLowerBoundProbability();
        this.upperBoundProbability = deviationAndDistributionInstance.getUpperBoundProbability();
    }

    public BigDecimal expectation(){
        return lowerBoundValue.multiply(lowerBoundProbability)
               .add(upperBoundValue.multiply(upperBoundProbability));
    }

}
