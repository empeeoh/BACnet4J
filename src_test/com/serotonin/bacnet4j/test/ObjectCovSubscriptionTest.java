package com.serotonin.bacnet4j.test;

import com.serotonin.bacnet4j.obj.ObjectCovSubscription;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Real;

/**
 * COV Threshold Tests for {@link ObjectCovSubscription}
 * 
 * @author japearson
 * 
 */
public class ObjectCovSubscriptionTest {

   public static void main(String[] args) {
      new ObjectCovSubscriptionTest().executeAll();
   }

   public void testNoNotificationsWithinThreshold() {
      // With a threshold of 5
      ObjectCovSubscription subscription = new ObjectCovSubscription(null, null, null, new Real(5));

      // Initial value of 10
      assertEqual(subscription.isNotificationRequired(PropertyIdentifier.presentValue, new Real(10.1f)),
                  true,
                  "initial value");

      assertEqual(subscription.isNotificationRequired(PropertyIdentifier.presentValue, new Real(10.5f)),
                  false,
                  "increment .4, within threshold");

      assertEqual(subscription.isNotificationRequired(PropertyIdentifier.presentValue, new Real(5.2f)),
                  false,
                  "lower value near end of threshold");

      assertEqual(subscription.isNotificationRequired(PropertyIdentifier.presentValue, new Real(15f)),
                  false,
                  "upper value near end of threshold");
   }

   public void testNotificationSurpassingThreshold() {
      // With a threshold of 0.5
      ObjectCovSubscription subscription = new ObjectCovSubscription(null, null, null, new Real(0.5f));

      // And an Initial value of 10
      assertEqual(subscription.isNotificationRequired(PropertyIdentifier.presentValue, new Real(10.1f)),
                  true,
                  "initial value");

      // Increment up 2 and expect to be outside threshold
      assertEqual(subscription.isNotificationRequired(PropertyIdentifier.presentValue, new Real(12.1f)),
                  true,
                  "increment 2, outside threshold");

      // De-increment down 0.25, within new threshold
      assertEqual(subscription.isNotificationRequired(PropertyIdentifier.presentValue, new Real(11.85f)),
                  false,
            "de-increment 0.25, within threshold");
      
      // De-increment down 0.35, now outside threshold
      assertEqual(subscription.isNotificationRequired(PropertyIdentifier.presentValue, new Real(11.5f)),
                  true,
                  "de-increment 0.35, outside threshold");
   }

   public void executeAll() {
      testNoNotificationsWithinThreshold();
      testNotificationSurpassingThreshold();
   }

   private void assertEqual(Object one,
                            Object two,
                            String message) {
      if (!one.equals(two)) {
         throw new RuntimeException("Expected both objects to be equal for test case [" + message + "]");
      }
   }

}
