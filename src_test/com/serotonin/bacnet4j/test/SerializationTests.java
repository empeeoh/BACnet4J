/*
    Copyright (C) 2006-2009 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.bacnet4j.test;

import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.constructed.ServicesSupported;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.constructed.TimeValue;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.error.BaseError;
import com.serotonin.bacnet4j.type.error.ChangeListError;
import com.serotonin.bacnet4j.type.notificationParameters.CommandFailure;
import com.serotonin.bacnet4j.type.notificationParameters.NotificationParameters;
import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Date;
import com.serotonin.bacnet4j.type.primitive.Double;
import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.bacnet4j.type.primitive.Time;
import com.serotonin.bacnet4j.type.primitive.Unsigned16;
import com.serotonin.bacnet4j.type.primitive.Unsigned32;
import com.serotonin.bacnet4j.type.primitive.Unsigned8;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

/**
 * @author Matthew Lohbihler
 */
public class SerializationTests {
    public static void main(String[] args) throws Exception {
        for (Encodable encodable : encodables)
            testEncodable(encodable);
    }

    private static void testEncodable(Encodable encodable) throws Exception {
        ByteQueue queue = new ByteQueue();
        encodable.write(queue);

        Encodable deserialized;
        if (BaseError.class.isAssignableFrom(encodable.getClass()))
            deserialized = BaseError.createBaseError(queue);
        else if (NotificationParameters.class.isAssignableFrom(encodable.getClass()))
            deserialized = NotificationParameters.createNotificationParameters(queue);
        else {
            Constructor<? extends Encodable> c = encodable.getClass().getConstructor(ByteQueue.class);
            deserialized = c.newInstance(queue);
        }

        if (!encodable.equals(deserialized))
            throw new Exception("Unequal deserialization in class " + encodable.getClass());
    }

    private static final Encodable[] encodables = {
            // Primitives
            new BitString(new boolean[] { true, false, true, false, true }), //
            new Boolean(true), //
            new CharacterString("My test character string"), //
            new Date(new GregorianCalendar(2008, Calendar.MARCH, 22)), //
            new Double(123.456), //
            new Enumerated(4), //
            new Null(), //
            new ObjectIdentifier(ObjectType.averaging, 2), //
            new OctetString(new byte[] { 1, 2, 3, 4 }), //
            new Real(234.567F), //
            new SignedInteger(-345), //
            new Time(13, 23, 12, 45), //
            new Unsigned16(65500), //
            new Unsigned32(Integer.MAX_VALUE), //
            new Unsigned8(254), //
            new UnsignedInteger(new BigInteger(Long.toString(Long.MAX_VALUE))), //
            new ServicesSupported(), //

            // Constructed
            new TimeValue(new Time(13, 23, 12, 45), new Real(65.56F)), //
            new ChangeListError((byte) 9, new BACnetError(ErrorClass.object, ErrorCode.abortBufferOverflow),
                    new UnsignedInteger(13)), //
            new CommandFailure(new Real(123.45F), new StatusFlags(false, true, false, true), new Time()), //
    };
}
