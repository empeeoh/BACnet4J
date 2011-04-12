/*
    Copyright (C) 2006-2009 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.bacnet4j.test;

import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.serotonin.bacnet4j.service.Service;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedRequestService;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedTextMessageRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.constructed.DeviceObjectPropertyReference;
import com.serotonin.bacnet4j.type.constructed.PriorityArray;
import com.serotonin.bacnet4j.type.constructed.PriorityValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.ServicesSupported;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.constructed.TimeValue;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.MessagePriority;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.error.BaseError;
import com.serotonin.bacnet4j.type.error.ChangeListError;
import com.serotonin.bacnet4j.type.eventParameter.BufferReady;
import com.serotonin.bacnet4j.type.eventParameter.EventParameter;
import com.serotonin.bacnet4j.type.eventParameter.Extended;
import com.serotonin.bacnet4j.type.notificationParameters.ChangeOfValue;
import com.serotonin.bacnet4j.type.notificationParameters.CommandFailure;
import com.serotonin.bacnet4j.type.notificationParameters.FloatingLimit;
import com.serotonin.bacnet4j.type.notificationParameters.NotificationParameters;
import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Date;
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

        ServicesSupported servicesSupported = new ServicesSupported();
        servicesSupported.setAll(true);

        Encodable deserialized;
        if (BaseError.class.isAssignableFrom(encodable.getClass()))
            deserialized = BaseError.createBaseError(queue);
        else if (NotificationParameters.class.isAssignableFrom(encodable.getClass()))
            deserialized = NotificationParameters.createNotificationParameters(queue);
        else if (EventParameter.class.isAssignableFrom(encodable.getClass()))
            deserialized = EventParameter.createEventParameter(queue);
        else if (AcknowledgementService.class.isAssignableFrom(encodable.getClass()))
            deserialized = AcknowledgementService.createAcknowledgementService(((Service) encodable).getChoiceId(),
                    queue);
        else if (UnconfirmedRequestService.class.isAssignableFrom(encodable.getClass()))
            deserialized = UnconfirmedRequestService.createUnconfirmedRequestService(servicesSupported,
                    ((Service) encodable).getChoiceId(), queue);
        else {
            Constructor<? extends Encodable> c = encodable.getClass().getConstructor(ByteQueue.class);
            deserialized = c.newInstance(queue);
        }

        if (!deserialized.equals(encodable))
            throw new Exception("Unequal deserialization in class " + encodable.getClass());
    }

    private static final Encodable[] encodables = {
            new ReadPropertyAck(
                    new ObjectIdentifier(ObjectType.eventEnrollment, 123),
                    PropertyIdentifier.eventParameters,
                    null,
                    new com.serotonin.bacnet4j.type.eventParameter.FloatingLimit(
                            new UnsignedInteger(234),
                            new DeviceObjectPropertyReference(new ObjectIdentifier(ObjectType.analogValue, 345),
                                    PropertyIdentifier.presentValue, null, new ObjectIdentifier(ObjectType.device, 456)),
                            new Real(567), new Real(678), new Real(789))), //

            new UnconfirmedTextMessageRequest(new ObjectIdentifier(ObjectType.device, 123), MessagePriority.urgent,
                    new CharacterString(CharacterString.Encodings.ISO_8859_1, "This is the message")), //

            // Primitives
            new BitString(new boolean[] { true, false, true, false, true }), //
            new com.serotonin.bacnet4j.type.primitive.Boolean(true), //
            new CharacterString("My test character string"), //
            new Date(new GregorianCalendar(2008, Calendar.MARCH, 22)), //
            new com.serotonin.bacnet4j.type.primitive.Double(123.456), //
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
            new FloatingLimit(new Real(123.45F), new StatusFlags(false, true, false, true), new Real(234.56F),
                    new Real(345.67F)), //
            new ChangeOfValue(new BitString(7, true), new StatusFlags(false, true, false, true)), //
            new ChangeOfValue(new Real(987.65F), new StatusFlags(false, true, false, true)), //
            new CommandFailure(new Time(), new StatusFlags(false, true, false, true), new Real(123.45F)), //
            new CommandFailure(new Real(123.45F), new StatusFlags(false, true, false, true), new Time()), //

            new BufferReady(new UnsignedInteger(17), new UnsignedInteger(19)), //
            new Extended(new UnsignedInteger(17), new UnsignedInteger(19), new SequenceOf<Extended.Parameter>()), //

            new ReadPropertyAck(new ObjectIdentifier(ObjectType.analogValue, 7), PropertyIdentifier.eventTimeStamps,
                    null, new SequenceOf<TimeStamp>()), //
            new ReadPropertyAck(new ObjectIdentifier(ObjectType.analogValue, 7), PropertyIdentifier.eventTimeStamps,
                    new UnsignedInteger(0), new UnsignedInteger(21)), //
            new ReadPropertyAck(new ObjectIdentifier(ObjectType.analogValue, 7), PropertyIdentifier.eventTimeStamps,
                    new UnsignedInteger(1), new TimeStamp(new DateTime())), //

            new ReadPropertyAck(new ObjectIdentifier(ObjectType.analogValue, 7), PropertyIdentifier.priorityArray,
                    null, new PriorityArray()), //
            new ReadPropertyAck(new ObjectIdentifier(ObjectType.analogValue, 7), PropertyIdentifier.priorityArray,
                    new UnsignedInteger(0), new UnsignedInteger(23)), //
            new ReadPropertyAck(new ObjectIdentifier(ObjectType.analogValue, 7), PropertyIdentifier.priorityArray,
                    new UnsignedInteger(11), new PriorityValue(new Null())), //
    };
}
