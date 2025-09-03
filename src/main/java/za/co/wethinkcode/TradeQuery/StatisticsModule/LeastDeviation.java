package za.co.wethinkcode.TradeQuery.StatisticsModule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LeastDeviation extends StatisticsBase {

    private final Difference diffMethods;
    protected final List<BigDecimal> dataList;


    public LeastDeviation(List<BigDecimal> dataList) {
        super(dataList);
        this.diffMethods = new Difference(dataList);
        this.dataList = dataList;
    }

    public LeastDeviation() {
        super();
        this.dataList = getDataList();
        this.diffMethods = new Difference(dataList);
    }

    public List<BigDecimal> variableLeastDifference() {
        List<BigDecimal> indices = indicesLeastComparitiveSums();
        List<BigDecimal> variables = new ArrayList<>();

        for (BigDecimal index : indices) {
            variables.add(dataList.get(index.intValue()));
        }
        return variables;
    }

    private List<BigDecimal> listComparitiveSums() {
        List<BigDecimal> listSum = new ArrayList<>();
        for (BigDecimal value : dataList) {
            listSum.add(diffMethods.sumComparitiveDifference(value));
        }
        return listSum;
    }

    private List<BigDecimal> indicesLeastComparitiveSums() {
        List<BigDecimal> listSum = listComparitiveSums();
        BigDecimal min = listSum.stream().min(BigDecimal::compareTo).orElseThrow();

        List<BigDecimal> indices = new ArrayList<>();
        for (int n = 0; n < listSum.size(); n++) {
            if (listSum.get(n).compareTo(min) == 0) {
                indices.add(BigDecimal.valueOf(n));
            }
        }
        return indices;
    }
}