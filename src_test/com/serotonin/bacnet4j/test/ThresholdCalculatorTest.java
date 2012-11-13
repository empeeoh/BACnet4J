package com.serotonin.bacnet4j.test;

import com.serotonin.bacnet4j.obj.ObjectCovSubscription.ThresholdCalculator;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Real;

/**
 * Test Cases for {@link ThresholdCalculator}
 * 
 * @author japearson
 * 
 */
public class ThresholdCalculatorTest {
   public static void main(String[] args) {
      new ThresholdCalculatorTest().runTestCases();
   }

   public void runTestCases() {
      Object[][] cases = new Object[][] {
            { "everything null should be true", null, null, null, true },
            { "two equal strings should be false", null, new CharacterString("1234"), new CharacterString("1234"),
                  false },
            { "two different strings should be true", null, new CharacterString("1234"), new CharacterString("12345"),
                  true },
            { "null original value should be true", new Real(1), null, new Real(10), true },
            { "Real value within threshold but greater than original should be false", new Real(0.5f), new Real(10.1f),
                  new Real(10.2f), false },
            { "Real value within threshold but less than original should be false", new Real(0.5f), new Real(10.1f),
                  new Real(10), false },
            { "Real value at threshold and greater than original should be false", new Real(0.5f), new Real(10.1f),
                  new Real(10.6f), false },
            { "Real value at threshold and less than original should be false", new Real(0.5f), new Real(10.1f),
                  new Real(9.6f), false },
            { "Real value over threshold should be true", new Real(0.5f), new Real(10.1f), new Real(10.61f), true },
            { "Real value over negative threshold should be true", new Real(-0.5f), new Real(10.1f), new Real(10.61f),
                  true }, };

      // Run each test case
      for (Object[] testcase : cases) {
         this.testIsValueOutsideOfThreshold((String) testcase[0],
                                            (Real) testcase[1],
                                            (Encodable) testcase[2],
                                            (Encodable) testcase[3],
                                            (Boolean) testcase[4]);
      }
   }

   public void testIsValueOutsideOfThreshold(String testCase,
                                             Real threshold,
                                             Encodable originalValue,
                                             Encodable newValue,
                                             boolean expectedResult) {
      // For a given set of threshold, original and new values

      // After checking if the value is outside the threshold
      boolean valueOutsideOfThreshold = ThresholdCalculator.isValueOutsideOfThreshold(threshold,
                                                                                      originalValue,
                                                                                      newValue);

      // We have the value we expect
      if (valueOutsideOfThreshold != expectedResult) {
         throw new RuntimeException("Unexpected result for test case [" + testCase + "], got ["
                                    + valueOutsideOfThreshold + "] exected [" + expectedResult + "]");
      }
   }
}
