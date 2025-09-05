package za.co.mainTests;


import java.math.BigDecimal;
import org.junit.jupiter.api.*;
// import java.math.RoundingMode;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import za.co.wethinkcode.TradeQuery.StatisticsModule.Difference;


public class TestDifferenceModule {

    List <BigDecimal> testData = List.of(
        new BigDecimal("10.0"),
        new BigDecimal("12.0"),
        new BigDecimal("12.0"),
        new BigDecimal("14.0"),
        new BigDecimal("13.0"),
        new BigDecimal("11.0")
    );

    Difference diff = new Difference(testData);

    @Test
    public void testAbsoluteDifferenceFunction(){
        List<BigDecimal> result = diff.absoluteDifference();
        List<BigDecimal> expected = List.of(
            new BigDecimal("2.0"),
            new BigDecimal("0.0"),
            new BigDecimal("2.0"),
            new BigDecimal("1.0"),
            new BigDecimal("2.0")
         
        );
        assertEquals(expected, result);
    }

    @Test
    public void testDifferenceFunction(){
        List<BigDecimal> result = diff.difference();
        List<BigDecimal> expected = List.of(
            new BigDecimal("2.0"),
            new BigDecimal("0.0"),  
            new BigDecimal("2.0"),
            new BigDecimal("-1.0"),
            new BigDecimal("-2.0")
        );
        assertEquals(expected, result); 
    }

    @Test
    public void testPositiveDifferenceFunction(){
        diff.setIncludeZero(true);
        List<BigDecimal> result = diff.positiveDifference();
        List<BigDecimal> expected = List.of(
            new BigDecimal("2.0"),
            new BigDecimal("0.0"),
            new BigDecimal("2.0")
        );

        diff.setIncludeZero(false);
        List<BigDecimal> result2 = diff.positiveDifference();
        List<BigDecimal> expected2 = List.of(
            new BigDecimal("2.0"),
            new BigDecimal("2.0")
        );

        assertEquals(expected, result);
        assertEquals(expected2, result2);
    }

    @Test
    public void testNegativeDifferenceFunction(){
        diff.setIncludeZero(true);
        List<BigDecimal> result = diff.negativeDifference();
        List<BigDecimal> expected = List.of(
            new BigDecimal("0.0"),
            new BigDecimal("-1.0"),
            new BigDecimal("-2.0")
        );

        diff.setIncludeZero(false);
        List<BigDecimal> result2 = diff.negativeDifference();
        List<BigDecimal> expected2 = List.of(
            new BigDecimal("-1.0"),
            new BigDecimal("-2.0")
        );

        assertEquals(expected, result);
        assertEquals(expected2, result2);
    }
}
