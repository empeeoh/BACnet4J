/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2009 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 */
package com.serotonin.bacnet4j.test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.serotonin.bacnet4j.apdu.APDU;
import com.serotonin.bacnet4j.apdu.ComplexACK;
import com.serotonin.bacnet4j.apdu.ConfirmedRequest;
import com.serotonin.bacnet4j.apdu.Segmentable;
import com.serotonin.bacnet4j.apdu.SimpleACK;
import com.serotonin.bacnet4j.apdu.UnconfirmedRequest;
import com.serotonin.bacnet4j.enums.DayOfWeek;
import com.serotonin.bacnet4j.enums.MaxApduLength;
import com.serotonin.bacnet4j.enums.MaxSegments;
import com.serotonin.bacnet4j.enums.Month;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.VendorServiceKey;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.acknowledgement.AtomicReadFileAck;
import com.serotonin.bacnet4j.service.acknowledgement.AtomicWriteFileAck;
import com.serotonin.bacnet4j.service.acknowledgement.AuthenticateAck;
import com.serotonin.bacnet4j.service.acknowledgement.ConfirmedPrivateTransferAck;
import com.serotonin.bacnet4j.service.acknowledgement.CreateObjectAck;
import com.serotonin.bacnet4j.service.acknowledgement.GetAlarmSummaryAck;
import com.serotonin.bacnet4j.service.acknowledgement.GetEnrollmentSummaryAck;
import com.serotonin.bacnet4j.service.acknowledgement.GetEventInformationAck;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyConditionalAck;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyMultipleAck;
import com.serotonin.bacnet4j.service.acknowledgement.ReadRangeAck;
import com.serotonin.bacnet4j.service.acknowledgement.VtDataAck;
import com.serotonin.bacnet4j.service.acknowledgement.VtOpenAck;
import com.serotonin.bacnet4j.service.confirmed.AcknowledgeAlarmRequest;
import com.serotonin.bacnet4j.service.confirmed.AddListElementRequest;
import com.serotonin.bacnet4j.service.confirmed.AtomicReadFileRequest;
import com.serotonin.bacnet4j.service.confirmed.AtomicWriteFileRequest;
import com.serotonin.bacnet4j.service.confirmed.AuthenticateRequest;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedCovNotificationRequest;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedEventNotificationRequest;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedPrivateTransferRequest;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedTextMessageRequest;
import com.serotonin.bacnet4j.service.confirmed.CreateObjectRequest;
import com.serotonin.bacnet4j.service.confirmed.DeleteObjectRequest;
import com.serotonin.bacnet4j.service.confirmed.DeviceCommunicationControlRequest;
import com.serotonin.bacnet4j.service.confirmed.DeviceCommunicationControlRequest.EnableDisable;
import com.serotonin.bacnet4j.service.confirmed.GetAlarmSummaryRequest;
import com.serotonin.bacnet4j.service.confirmed.GetEnrollmentSummaryRequest;
import com.serotonin.bacnet4j.service.confirmed.GetEnrollmentSummaryRequest.PriorityFilter;
import com.serotonin.bacnet4j.service.confirmed.GetEventInformation;
import com.serotonin.bacnet4j.service.confirmed.LifeSafetyOperationRequest;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyConditionalRequest;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyConditionalRequest.ObjectSelectionCriteria;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyConditionalRequest.ObjectSelectionCriteria.SelectionCriteria;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyConditionalRequest.ObjectSelectionCriteria.SelectionCriteria.RelationSpecifier;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyConditionalRequest.ObjectSelectionCriteria.SelectionLogic;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyMultipleRequest;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.confirmed.ReinitializeDeviceRequest;
import com.serotonin.bacnet4j.service.confirmed.ReinitializeDeviceRequest.ReinitializedStateOfDevice;
import com.serotonin.bacnet4j.service.confirmed.RemoveListElementRequest;
import com.serotonin.bacnet4j.service.confirmed.RequestKeyRequest;
import com.serotonin.bacnet4j.service.confirmed.SubscribeCOVPropertyRequest;
import com.serotonin.bacnet4j.service.confirmed.SubscribeCOVRequest;
import com.serotonin.bacnet4j.service.confirmed.VtCloseRequest;
import com.serotonin.bacnet4j.service.confirmed.VtDataRequest;
import com.serotonin.bacnet4j.service.confirmed.VtOpenRequest;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyMultipleRequest;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.IAmRequest;
import com.serotonin.bacnet4j.service.unconfirmed.IHaveRequest;
import com.serotonin.bacnet4j.service.unconfirmed.TimeSynchronizationRequest;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedCovNotificationRequest;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedEventNotificationRequest;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedPrivateTransferRequest;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedRequestService;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedTextMessageRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoHasRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.SequenceDefinition;
import com.serotonin.bacnet4j.type.SequenceDefinition.ElementSpecification;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.constructed.BaseType;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.constructed.EventTransitionBits;
import com.serotonin.bacnet4j.type.constructed.LogRecord;
import com.serotonin.bacnet4j.type.constructed.PropertyReference;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult.Result;
import com.serotonin.bacnet4j.type.constructed.ReadAccessSpecification;
import com.serotonin.bacnet4j.type.constructed.Recipient;
import com.serotonin.bacnet4j.type.constructed.RecipientProcess;
import com.serotonin.bacnet4j.type.constructed.ResultFlags;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.ServicesSupported;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.constructed.WriteAccessSpecification;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.EventType;
import com.serotonin.bacnet4j.type.enumerated.FileAccessMethod;
import com.serotonin.bacnet4j.type.enumerated.LifeSafetyOperation;
import com.serotonin.bacnet4j.type.enumerated.MessagePriority;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Reliability;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.enumerated.VtClass;
import com.serotonin.bacnet4j.type.error.BaseError;
import com.serotonin.bacnet4j.type.notificationParameters.OutOfRange;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Date;
import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.bacnet4j.type.primitive.Time;
import com.serotonin.bacnet4j.type.primitive.Unsigned16;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class AnnexFEncodingTest {
    public static void main(String[] args) {
        new AnnexFEncodingTest().executeAll();
    }

    private final ServicesSupported servicesSupported;

    public AnnexFEncodingTest() {
        servicesSupported = new ServicesSupported();
        servicesSupported.setAll(true);
    }

    public void e1_1aTest() {
        AcknowledgeAlarmRequest acknowledgeAlarmRequest = new AcknowledgeAlarmRequest(new UnsignedInteger(1),
                new ObjectIdentifier(ObjectType.analogInput, 2), EventState.highLimit, new TimeStamp(
                        new UnsignedInteger(16)), new CharacterString(CharacterString.Encodings.ANSI_X3_4, "MDL"),
                new TimeStamp(new DateTime(new Date(1992, Month.JUNE, 21, DayOfWeek.UNSPECIFIED),
                        new Time(13, 3, 41, 9))));

        ConfirmedRequest pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 7, (byte) 0, 0, acknowledgeAlarmRequest);

        byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x07, (byte) 0x00, (byte) 0x09, (byte) 0x01,
                (byte) 0x1c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x29, (byte) 0x03, (byte) 0x3e,
                (byte) 0x19, (byte) 0x10, (byte) 0x3f, (byte) 0x4c, (byte) 0x00, (byte) 0x4d, (byte) 0x44, (byte) 0x4c,
                (byte) 0x5e, (byte) 0x2e, (byte) 0xa4, (byte) 0x5c, (byte) 0x06, (byte) 0x15, (byte) 0xff, (byte) 0xb4,
                (byte) 0x0d, (byte) 0x03, (byte) 0x29, (byte) 0x09, (byte) 0x2f, (byte) 0x5f };

        compare(pdu, expectedResult);
    }

    public void e1_1bTest() {
        SimpleACK simpleACK = new SimpleACK((byte) 7, 0);
        byte[] expectedResult = { (byte) 0x20, (byte) 0x07, (byte) 0x00 };
        compare(simpleACK, expectedResult);
    }

    public void e1_2aTest() {
        List<PropertyValue> list = new ArrayList<PropertyValue>();
        list.add(new PropertyValue(PropertyIdentifier.presentValue, null, new Real(65), null));
        list.add(new PropertyValue(PropertyIdentifier.statusFlags, null, new StatusFlags(false, false, false, false),
                null));

        SequenceOf<PropertyValue> listOfValues = new SequenceOf<PropertyValue>(list);

        ConfirmedCovNotificationRequest confirmedCovNotification = new ConfirmedCovNotificationRequest(
                new UnsignedInteger(18), new ObjectIdentifier(ObjectType.device, 4), new ObjectIdentifier(
                        ObjectType.analogInput, 10), new UnsignedInteger(0), listOfValues);

        ConfirmedRequest pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 15, (byte) 0, 0, confirmedCovNotification);

        byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x0f, (byte) 0x01, (byte) 0x09, (byte) 0x12,
                (byte) 0x1c, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x2c, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x0a, (byte) 0x39, (byte) 0x00, (byte) 0x4e, (byte) 0x09, (byte) 0x55, (byte) 0x2e,
                (byte) 0x44, (byte) 0x42, (byte) 0x82, (byte) 0x00, (byte) 0x00, (byte) 0x2f, (byte) 0x09, (byte) 0x6f,
                (byte) 0x2e, (byte) 0x82, (byte) 0x04, (byte) 0x00, (byte) 0x2f, (byte) 0x4f };

        compare(pdu, expectedResult);
    }

    public void e1_2bTest() {
        SimpleACK pdu = new SimpleACK((byte) 15, 1);
        byte[] expectedResult = { (byte) 0x20, (byte) 0x0f, (byte) 0x01 };
        compare(pdu, expectedResult);
    }

    public void e1_3Test() {
        List<PropertyValue> list = new ArrayList<PropertyValue>();
        list.add(new PropertyValue(PropertyIdentifier.presentValue, null, new Real(65), null));
        list.add(new PropertyValue(PropertyIdentifier.statusFlags, null, new StatusFlags(false, false, false, false),
                null));

        SequenceOf<PropertyValue> listOfValues = new SequenceOf<PropertyValue>(list);

        UnconfirmedCovNotificationRequest unconfirmedCovNotificationRequest = new UnconfirmedCovNotificationRequest(
                new UnsignedInteger(18), new ObjectIdentifier(ObjectType.device, 4), new ObjectIdentifier(
                        ObjectType.analogInput, 10), new UnsignedInteger(0), listOfValues);

        UnconfirmedRequest pdu = new UnconfirmedRequest(unconfirmedCovNotificationRequest);

        byte[] expectedResult = { (byte) 0x10, (byte) 0x02, (byte) 0x09, (byte) 0x12, (byte) 0x1c, (byte) 0x02,
                (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x2c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0a,
                (byte) 0x39, (byte) 0x00, (byte) 0x4e, (byte) 0x09, (byte) 0x55, (byte) 0x2e, (byte) 0x44, (byte) 0x42,
                (byte) 0x82, (byte) 0x00, (byte) 0x00, (byte) 0x2f, (byte) 0x09, (byte) 0x6f, (byte) 0x2e, (byte) 0x82,
                (byte) 0x04, (byte) 0x00, (byte) 0x2f, (byte) 0x4f };

        compare(pdu, expectedResult);
    }

    public void e1_4aTest() {
        ConfirmedEventNotificationRequest confirmedEventNotificationRequest = new ConfirmedEventNotificationRequest(
                new UnsignedInteger(1), new ObjectIdentifier(ObjectType.device, 4), new ObjectIdentifier(
                        ObjectType.analogInput, 2), new TimeStamp(new UnsignedInteger(16)), new UnsignedInteger(4),
                new UnsignedInteger(100), EventType.outOfRange, null, NotifyType.alarm,
                new com.serotonin.bacnet4j.type.primitive.Boolean(true), EventState.normal, EventState.highLimit,
                new OutOfRange(new Real(80.1f), new StatusFlags(true, false, false, false), new Real(1), new Real(80)));

        ConfirmedRequest pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 16, (byte) 0, 0, confirmedEventNotificationRequest);

        byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x10, (byte) 0x02, (byte) 0x09, (byte) 0x01,
                (byte) 0x1c, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x2c, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x02, (byte) 0x3e, (byte) 0x19, (byte) 0x10, (byte) 0x3f, (byte) 0x49, (byte) 0x04,
                (byte) 0x59, (byte) 0x64, (byte) 0x69, (byte) 0x05, (byte) 0x89, (byte) 0x00, (byte) 0x99, (byte) 0x01,
                (byte) 0xa9, (byte) 0x00, (byte) 0xb9, (byte) 0x03, (byte) 0xce, (byte) 0x5e, (byte) 0x0c, (byte) 0x42,
                (byte) 0xa0, (byte) 0x33, (byte) 0x33, (byte) 0x1a, (byte) 0x04, (byte) 0x80, (byte) 0x2c, (byte) 0x3f,
                (byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x3c, (byte) 0x42, (byte) 0xa0, (byte) 0x00, (byte) 0x00,
                (byte) 0x5f, (byte) 0xcf };

        compare(pdu, expectedResult);
    }

    public void e1_4bTest() {
        SimpleACK pdu = new SimpleACK((byte) 16, 2);
        byte[] expectedResult = { (byte) 0x20, (byte) 0x10, (byte) 0x02 };
        compare(pdu, expectedResult);
    }

    public void e1_5Test() {
        UnconfirmedEventNotificationRequest unconfirmedEventNotificationRequest = new UnconfirmedEventNotificationRequest(
                new UnsignedInteger(1), new ObjectIdentifier(ObjectType.device, 9), new ObjectIdentifier(
                        ObjectType.analogInput, 2), new TimeStamp(new UnsignedInteger(16)), new UnsignedInteger(4),
                new UnsignedInteger(100), EventType.outOfRange, null, NotifyType.alarm,
                new com.serotonin.bacnet4j.type.primitive.Boolean(true), EventState.normal, EventState.highLimit,
                new OutOfRange(new Real(80.1f), new StatusFlags(true, false, false, false), new Real(1), new Real(80)));

        UnconfirmedRequest pdu = new UnconfirmedRequest(unconfirmedEventNotificationRequest);

        byte[] expectedResult = { (byte) 0x10, (byte) 0x03, (byte) 0x09, (byte) 0x01, (byte) 0x1c, (byte) 0x02,
                (byte) 0x00, (byte) 0x00, (byte) 0x09, (byte) 0x2c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02,
                (byte) 0x3e, (byte) 0x19, (byte) 0x10, (byte) 0x3f, (byte) 0x49, (byte) 0x04, (byte) 0x59, (byte) 0x64,
                (byte) 0x69, (byte) 0x05, (byte) 0x89, (byte) 0x00, (byte) 0x99, (byte) 0x01, (byte) 0xa9, (byte) 0x00,
                (byte) 0xb9, (byte) 0x03, (byte) 0xce, (byte) 0x5e, (byte) 0x0c, (byte) 0x42, (byte) 0xa0, (byte) 0x33,
                (byte) 0x33, (byte) 0x1a, (byte) 0x04, (byte) 0x80, (byte) 0x2c, (byte) 0x3f, (byte) 0x80, (byte) 0x00,
                (byte) 0x00, (byte) 0x3c, (byte) 0x42, (byte) 0xa0, (byte) 0x00, (byte) 0x00, (byte) 0x5f, (byte) 0xcf };

        compare(pdu, expectedResult);
    }

    public void e1_6aTest() {
        GetAlarmSummaryRequest getAlarmSummaryRequest = new GetAlarmSummaryRequest();

        ConfirmedRequest pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 1, (byte) 0, 0, getAlarmSummaryRequest);

        byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x01, (byte) 0x03 };

        compare(pdu, expectedResult);
    }

    public void e1_6bTest() {
        List<GetAlarmSummaryAck.AlarmSummary> alarmSummaries = new ArrayList<GetAlarmSummaryAck.AlarmSummary>();
        alarmSummaries.add(new GetAlarmSummaryAck.AlarmSummary(new ObjectIdentifier(ObjectType.analogInput, 2),
                EventState.highLimit, new EventTransitionBits(false, true, true)));
        alarmSummaries.add(new GetAlarmSummaryAck.AlarmSummary(new ObjectIdentifier(ObjectType.analogInput, 3),
                EventState.lowLimit, new EventTransitionBits(true, true, true)));

        GetAlarmSummaryAck getAlarmSummaryAck = new GetAlarmSummaryAck(new SequenceOf<GetAlarmSummaryAck.AlarmSummary>(
                alarmSummaries));

        ComplexACK pdu = new ComplexACK(false, false, (byte) 1, 0, 0, getAlarmSummaryAck);

        byte[] expectedResult = { (byte) 0x30, (byte) 0x01, (byte) 0x03, (byte) 0xc4, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x02, (byte) 0x91, (byte) 0x03, (byte) 0x82, (byte) 0x05, (byte) 0x60, (byte) 0xc4,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x91, (byte) 0x04, (byte) 0x82, (byte) 0x05,
                (byte) 0xe0 };

        compare(pdu, expectedResult);
    }

    public void e1_7aTest() {
        GetEnrollmentSummaryRequest getEnrollmentSummaryRequest = new GetEnrollmentSummaryRequest(new Enumerated(
                GetEnrollmentSummaryRequest.AcknowledgmentFilters.notAcked), null, null, null, null, null);

        ConfirmedRequest pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 1, (byte) 0, 0, getEnrollmentSummaryRequest);

        byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x01, (byte) 0x04, (byte) 0x09, (byte) 0x02 };

        compare(pdu, expectedResult);
    }

    public void e1_7bTest() {
        List<GetEnrollmentSummaryAck.EnrollmentSummary> enrollmentSummaries = new ArrayList<GetEnrollmentSummaryAck.EnrollmentSummary>();
        enrollmentSummaries.add(new GetEnrollmentSummaryAck.EnrollmentSummary(new ObjectIdentifier(
                ObjectType.analogInput, 2), EventType.outOfRange, EventState.highLimit, new UnsignedInteger(100),
                new UnsignedInteger(4)));
        enrollmentSummaries.add(new GetEnrollmentSummaryAck.EnrollmentSummary(new ObjectIdentifier(
                ObjectType.eventEnrollment, 6), EventType.changeOfState, EventState.normal, new UnsignedInteger(50),
                new UnsignedInteger(2)));

        GetEnrollmentSummaryAck getEnrollmentSummaryAck = new GetEnrollmentSummaryAck(
                new SequenceOf<GetEnrollmentSummaryAck.EnrollmentSummary>(enrollmentSummaries));

        ComplexACK pdu = new ComplexACK(false, false, (byte) 1, 0, 0, getEnrollmentSummaryAck);

        byte[] expectedResult = { (byte) 0x30, (byte) 0x01, (byte) 0x04, (byte) 0xc4, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x02, (byte) 0x91, (byte) 0x05, (byte) 0x91, (byte) 0x03, (byte) 0x21, (byte) 0x64,
                (byte) 0x21, (byte) 0x04, (byte) 0xc4, (byte) 0x02, (byte) 0x40, (byte) 0x00, (byte) 0x06, (byte) 0x91,
                (byte) 0x01, (byte) 0x91, (byte) 0x00, (byte) 0x21, (byte) 0x32, (byte) 0x21, (byte) 0x02 };

        compare(pdu, expectedResult);
    }

    public void e1_7cTest() {
        GetEnrollmentSummaryRequest getEnrollmentSummaryRequest = new GetEnrollmentSummaryRequest(new Enumerated(
                GetEnrollmentSummaryRequest.AcknowledgmentFilters.all), new RecipientProcess(new Recipient(
                new ObjectIdentifier(ObjectType.device, 17)), new UnsignedInteger(9)), null, null, new PriorityFilter(
                new UnsignedInteger(6), new UnsignedInteger(10)), null);

        ConfirmedRequest pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 2, (byte) 0, 0, getEnrollmentSummaryRequest);

        byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x02, (byte) 0x04, (byte) 0x09, (byte) 0x00,
                (byte) 0x1e, (byte) 0x0e, (byte) 0x0c, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x11, (byte) 0x0f,
                (byte) 0x19, (byte) 0x09, (byte) 0x1f, (byte) 0x4e, (byte) 0x09, (byte) 0x06, (byte) 0x19, (byte) 0x0a,
                (byte) 0x4f };

        compare(pdu, expectedResult);
    }

    public void e1_7dTest() {
        List<GetEnrollmentSummaryAck.EnrollmentSummary> enrollmentSummaries = new ArrayList<GetEnrollmentSummaryAck.EnrollmentSummary>();
        enrollmentSummaries.add(new GetEnrollmentSummaryAck.EnrollmentSummary(new ObjectIdentifier(
                ObjectType.analogInput, 2), EventType.outOfRange, EventState.normal, new UnsignedInteger(8),
                new UnsignedInteger(4)));
        enrollmentSummaries.add(new GetEnrollmentSummaryAck.EnrollmentSummary(new ObjectIdentifier(
                ObjectType.analogInput, 3), EventType.outOfRange, EventState.normal, new UnsignedInteger(8),
                new UnsignedInteger(4)));
        enrollmentSummaries.add(new GetEnrollmentSummaryAck.EnrollmentSummary(new ObjectIdentifier(
                ObjectType.analogInput, 4), EventType.outOfRange, EventState.normal, new UnsignedInteger(8),
                new UnsignedInteger(4)));
        enrollmentSummaries.add(new GetEnrollmentSummaryAck.EnrollmentSummary(new ObjectIdentifier(
                ObjectType.eventEnrollment, 7), EventType.floatingLimit, EventState.normal, new UnsignedInteger(3),
                new UnsignedInteger(8)));

        GetEnrollmentSummaryAck getEnrollmentSummaryAck = new GetEnrollmentSummaryAck(
                new SequenceOf<GetEnrollmentSummaryAck.EnrollmentSummary>(enrollmentSummaries));

        ComplexACK pdu = new ComplexACK(false, false, (byte) 2, 0, 0, getEnrollmentSummaryAck);

        byte[] expectedResult = { (byte) 0x30, (byte) 0x02, (byte) 0x04, (byte) 0xc4, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x02, (byte) 0x91, (byte) 0x05, (byte) 0x91, (byte) 0x00, (byte) 0x21, (byte) 0x08,
                (byte) 0x21, (byte) 0x04, (byte) 0xc4, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x91,
                (byte) 0x05, (byte) 0x91, (byte) 0x00, (byte) 0x21, (byte) 0x08, (byte) 0x21, (byte) 0x04, (byte) 0xc4,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x91, (byte) 0x05, (byte) 0x91, (byte) 0x00,
                (byte) 0x21, (byte) 0x08, (byte) 0x21, (byte) 0x04, (byte) 0xc4, (byte) 0x02, (byte) 0x40, (byte) 0x00,
                (byte) 0x07, (byte) 0x91, (byte) 0x04, (byte) 0x91, (byte) 0x00, (byte) 0x21, (byte) 0x03, (byte) 0x21,
                (byte) 0x08 };

        compare(pdu, expectedResult);
    }

    public void e1_8aTest() {
        GetEventInformation getEventInformation = new GetEventInformation(null);

        ConfirmedRequest pdu = new ConfirmedRequest(false, false, true, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 1, (byte) 0, 0, getEventInformation);

        byte[] expectedResult = { (byte) 0x02, (byte) 0x02, (byte) 0x01, (byte) 0x1d };

        compare(pdu, expectedResult);
    }

    public void e1_8bTest() {
        List<GetEventInformationAck.EventSummary> eventSummaries = new ArrayList<GetEventInformationAck.EventSummary>();
        eventSummaries.add(new GetEventInformationAck.EventSummary(new ObjectIdentifier(ObjectType.analogInput, 2),
                EventState.highLimit, new EventTransitionBits(false, true, true),
                new TimeStamp(new Time(15, 35, 0, 20)), new TimeStamp(new Time(255, 255, 255, 255)), new TimeStamp(
                        new Time(255, 255, 255, 255)), NotifyType.alarm, new EventTransitionBits(true, true, true),
                new UnsignedInteger(15), new UnsignedInteger(15), new UnsignedInteger(20)));
        eventSummaries.add(new GetEventInformationAck.EventSummary(new ObjectIdentifier(ObjectType.analogInput, 3),
                EventState.normal, new EventTransitionBits(true, true, false), new TimeStamp(new Time(15, 40, 0, 0)),
                new TimeStamp(new Time(255, 255, 255, 255)), new TimeStamp(new Time(15, 45, 30, 30)), NotifyType.alarm,
                new EventTransitionBits(true, true, true), new UnsignedInteger(15), new UnsignedInteger(15),
                new UnsignedInteger(20)));

        GetEventInformationAck getEventInformationAck = new GetEventInformationAck(
                new SequenceOf<GetEventInformationAck.EventSummary>(eventSummaries), new Boolean(false));

        ComplexACK pdu = new ComplexACK(false, false, (byte) 1, 0, 0, getEventInformationAck);

        byte[] expectedResult = { (byte) 0x30, (byte) 0x01, (byte) 0x1d, (byte) 0x0e, (byte) 0x0c, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x19, (byte) 0x03, (byte) 0x2a, (byte) 0x05, (byte) 0x60,
                (byte) 0x3e, (byte) 0x0c, (byte) 0x0f, (byte) 0x23, (byte) 0x00, (byte) 0x14, (byte) 0x0c, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x0c, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0x3f, (byte) 0x49, (byte) 0x00, (byte) 0x5a, (byte) 0x05, (byte) 0xe0, (byte) 0x6e, (byte) 0x21,
                (byte) 0x0f, (byte) 0x21, (byte) 0x0f, (byte) 0x21, (byte) 0x14, (byte) 0x6f, (byte) 0x0c, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x19, (byte) 0x00, (byte) 0x2a, (byte) 0x05, (byte) 0xc0,
                (byte) 0x3e, (byte) 0x0c, (byte) 0x0f, (byte) 0x28, (byte) 0x00, (byte) 0x00, (byte) 0x0c, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x0c, (byte) 0x0f, (byte) 0x2d, (byte) 0x1e, (byte) 0x1e,
                (byte) 0x3f, (byte) 0x49, (byte) 0x00, (byte) 0x5a, (byte) 0x05, (byte) 0xe0, (byte) 0x6e, (byte) 0x21,
                (byte) 0x0f, (byte) 0x21, (byte) 0x0f, (byte) 0x21, (byte) 0x14, (byte) 0x6f, (byte) 0x0f, (byte) 0x19,
                (byte) 0x00 };

        compare(pdu, expectedResult);
    }

    public void e1_9aTest() {
        LifeSafetyOperationRequest lifeSafetyOperationRequest = new LifeSafetyOperationRequest(new UnsignedInteger(18),
                new CharacterString(CharacterString.Encodings.ANSI_X3_4, "MDL"), LifeSafetyOperation.reset,
                new ObjectIdentifier(ObjectType.lifeSafetyPoint, 1));

        ConfirmedRequest pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 15, (byte) 0, 0, lifeSafetyOperationRequest);

        byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x0f, (byte) 0x1b, (byte) 0x09, (byte) 0x12,
                (byte) 0x1c, (byte) 0x00, (byte) 0x4d, (byte) 0x44, (byte) 0x4c, (byte) 0x29, (byte) 0x04, (byte) 0x3c,
                (byte) 0x05, (byte) 0x40, (byte) 0x00, (byte) 0x01 };

        compare(pdu, expectedResult);
    }

    public void e1_9bTest() {
        SimpleACK pdu = new SimpleACK((byte) 15, 27);
        byte[] expectedResult = { (byte) 0x20, (byte) 0x0f, (byte) 0x1b };
        compare(pdu, expectedResult);
    }

    public void e1_10aTest() {
        SubscribeCOVRequest subscribeCOVRequest = new SubscribeCOVRequest(new UnsignedInteger(18),
                new ObjectIdentifier(ObjectType.analogInput, 10), new Boolean(true), new UnsignedInteger(0));

        ConfirmedRequest pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 15, (byte) 0, 0, subscribeCOVRequest);

        byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x0f, (byte) 0x05, (byte) 0x09, (byte) 0x12,
                (byte) 0x1c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0a, (byte) 0x29, (byte) 0x01, (byte) 0x39,
                (byte) 0x00 };

        compare(pdu, expectedResult);
    }

    public void e1_10bTest() {
        SimpleACK pdu = new SimpleACK((byte) 15, 5);
        byte[] expectedResult = { (byte) 0x20, (byte) 0x0f, (byte) 0x05 };
        compare(pdu, expectedResult);
    }

    public void e1_11aTest() {
        SubscribeCOVPropertyRequest subscribeCOVPropertyRequest = new SubscribeCOVPropertyRequest(new UnsignedInteger(
                18), new ObjectIdentifier(ObjectType.analogInput, 10), new Boolean(true), new UnsignedInteger(60),
                new PropertyReference(PropertyIdentifier.presentValue, null), new Real(1));

        ConfirmedRequest pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 15, (byte) 0, 0, subscribeCOVPropertyRequest);

        byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x0f, (byte) 0x1c, (byte) 0x09, (byte) 0x12,
                (byte) 0x1c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0a, (byte) 0x29, (byte) 0x01, (byte) 0x39,
                (byte) 0x3c, (byte) 0x4e, (byte) 0x09, (byte) 0x55, (byte) 0x4f, (byte) 0x5c, (byte) 0x3f, (byte) 0x80,
                (byte) 0x00, (byte) 0x00 };

        compare(pdu, expectedResult);
    }

    public void e1_11bTest() {
        SimpleACK pdu = new SimpleACK((byte) 15, 28);
        byte[] expectedResult = { (byte) 0x20, (byte) 0x0f, (byte) 0x1c };
        compare(pdu, expectedResult);
    }

    public void e2_1aTest() {
        ConfirmedRequestService service = new AtomicReadFileRequest(new ObjectIdentifier(ObjectType.file, 1), false,
                new SignedInteger(0), new UnsignedInteger(27));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_206,
                (byte) 0, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x06, (byte) 0xC4, (byte) 0x02,
                (byte) 0x80, (byte) 0x00, (byte) 0x01, (byte) 0x0E, (byte) 0x31, (byte) 0x00, (byte) 0x21, (byte) 0x1B,
                (byte) 0x0F };
        compare(pdu, expectedResult);
    }

    public void e2_1bTest() {
        AcknowledgementService service = new AtomicReadFileAck(new Boolean(false), new SignedInteger(0),
                new OctetString("Chiller01 On-Time=4.3 Hours".getBytes()));
        APDU pdu = new ComplexACK(false, false, (byte) 0, 0, 0, service);
        byte[] expectedResult = { (byte) 0x30, (byte) 0x00, (byte) 0x06, (byte) 0x10, (byte) 0x0E, (byte) 0x31,
                (byte) 0x00, (byte) 0x65, (byte) 0x1B, (byte) 0x43, (byte) 0x68, (byte) 0x69, (byte) 0x6C, (byte) 0x6C,
                (byte) 0x65, (byte) 0x72, (byte) 0x30, (byte) 0x31, (byte) 0x20, (byte) 0x4F, (byte) 0x6E, (byte) 0x2D,
                (byte) 0x54, (byte) 0x69, (byte) 0x6D, (byte) 0x65, (byte) 0x3D, (byte) 0x34, (byte) 0x2E, (byte) 0x33,
                (byte) 0x20, (byte) 0x48, (byte) 0x6F, (byte) 0x75, (byte) 0x72, (byte) 0x73, (byte) 0x0F };
        compare(pdu, expectedResult);
    }

    public void e2_1cTest() {
        ConfirmedRequestService service = new AtomicReadFileRequest(new ObjectIdentifier(ObjectType.file, 2), true,
                new SignedInteger(14), new UnsignedInteger(3));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_206,
                (byte) 18, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x12, (byte) 0x06, (byte) 0xC4, (byte) 0x02,
                (byte) 0x80, (byte) 0x00, (byte) 0x02, (byte) 0x1E, (byte) 0x31, (byte) 0x0E, (byte) 0x21, (byte) 0x03,
                (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    public void e2_1dTest() {
        List<OctetString> strings = new ArrayList<OctetString>();
        strings.add(new OctetString("12:00,45.6".getBytes()));
        strings.add(new OctetString("12:15,44.8".getBytes()));
        AcknowledgementService service = new AtomicReadFileAck(new Boolean(true), new SignedInteger(14),
                new UnsignedInteger(2), new SequenceOf<OctetString>(strings));
        APDU pdu = new ComplexACK(false, false, (byte) 18, 0, 0, service);
        byte[] expectedResult = { (byte) 0x30, (byte) 0x12, (byte) 0x06, (byte) 0x11, (byte) 0x1E, (byte) 0x31,
                (byte) 0x0E, (byte) 0x21, (byte) 0x02, (byte) 0x65, (byte) 0x0A, (byte) 0x31, (byte) 0x32, (byte) 0x3A,
                (byte) 0x30, (byte) 0x30, (byte) 0x2C, (byte) 0x34, (byte) 0x35, (byte) 0x2E, (byte) 0x36, (byte) 0x65,
                (byte) 0x0A, (byte) 0x31, (byte) 0x32, (byte) 0x3A, (byte) 0x31, (byte) 0x35, (byte) 0x2C, (byte) 0x34,
                (byte) 0x34, (byte) 0x2E, (byte) 0x38, (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    public void e2_2aTest() {
        ConfirmedRequestService service = new AtomicWriteFileRequest(new ObjectIdentifier(ObjectType.file, 1),
                new SignedInteger(30), new OctetString("Chiller01 On-Time=4.3 Hours".getBytes()));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_206,
                (byte) 85, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x55, (byte) 0x07, (byte) 0xC4, (byte) 0x02,
                (byte) 0x80, (byte) 0x00, (byte) 0x01, (byte) 0x0E, (byte) 0x31, (byte) 0x1E, (byte) 0x65, (byte) 0x1B,
                (byte) 0x43, (byte) 0x68, (byte) 0x69, (byte) 0x6C, (byte) 0x6C, (byte) 0x65, (byte) 0x72, (byte) 0x30,
                (byte) 0x31, (byte) 0x20, (byte) 0x4F, (byte) 0x6E, (byte) 0x2D, (byte) 0x54, (byte) 0x69, (byte) 0x6D,
                (byte) 0x65, (byte) 0x3D, (byte) 0x34, (byte) 0x2E, (byte) 0x33, (byte) 0x20, (byte) 0x48, (byte) 0x6F,
                (byte) 0x75, (byte) 0x72, (byte) 0x73, (byte) 0x0F };
        compare(pdu, expectedResult);
    }

    public void e2_2bTest() {
        AcknowledgementService service = new AtomicWriteFileAck(false, new SignedInteger(30));
        APDU pdu = new ComplexACK(false, false, (byte) 85, 0, 0, service);
        byte[] expectedResult = { (byte) 0x30, (byte) 0x55, (byte) 0x07, (byte) 0x09, (byte) 0x1E };
        compare(pdu, expectedResult);
    }

    public void e2_2cTest() {
        List<OctetString> strings = new ArrayList<OctetString>();
        strings.add(new OctetString("12:00,45.6".getBytes()));
        strings.add(new OctetString("12:15,44.8".getBytes()));
        ConfirmedRequestService service = new AtomicWriteFileRequest(new ObjectIdentifier(ObjectType.file, 2),
                new SignedInteger(-1), new UnsignedInteger(2), new SequenceOf<OctetString>(strings));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_206,
                (byte) 85, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x55, (byte) 0x07, (byte) 0xC4, (byte) 0x02,
                (byte) 0x80, (byte) 0x00, (byte) 0x02, (byte) 0x1E, (byte) 0x31, (byte) 0xFF, (byte) 0x21, (byte) 0x02,
                (byte) 0x65, (byte) 0x0A, (byte) 0x31, (byte) 0x32, (byte) 0x3A, (byte) 0x30, (byte) 0x30, (byte) 0x2C,
                (byte) 0x34, (byte) 0x35, (byte) 0x2E, (byte) 0x36, (byte) 0x65, (byte) 0x0A, (byte) 0x31, (byte) 0x32,
                (byte) 0x3A, (byte) 0x31, (byte) 0x35, (byte) 0x2C, (byte) 0x34, (byte) 0x34, (byte) 0x2E, (byte) 0x38,
                (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    public void e2_2dTest() {
        AcknowledgementService service = new AtomicWriteFileAck(true, new SignedInteger(14));
        APDU pdu = new ComplexACK(false, false, (byte) 85, 0, 0, service);
        byte[] expectedResult = { (byte) 0x30, (byte) 0x55, (byte) 0x07, (byte) 0x19, (byte) 0x0E };
        compare(pdu, expectedResult);
    }

    public void e3_1aTest() {
        List<PropertyReference> propertyReferences = new ArrayList<PropertyReference>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        propertyReferences.add(new PropertyReference(PropertyIdentifier.reliability, null));

        List<ReadAccessSpecification> elements = new ArrayList<ReadAccessSpecification>();
        elements.add(new ReadAccessSpecification(new ObjectIdentifier(ObjectType.analogInput, 15),
                new SequenceOf<PropertyReference>(propertyReferences)));
        ConfirmedRequestService service = new AddListElementRequest(new ObjectIdentifier(ObjectType.group, 3),
                PropertyIdentifier.listOfGroupMembers, null, new SequenceOf<ReadAccessSpecification>(elements));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_206,
                (byte) 1, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x01, (byte) 0x08, (byte) 0x0C, (byte) 0x02,
                (byte) 0xC0, (byte) 0x00, (byte) 0x03, (byte) 0x19, (byte) 0x35, (byte) 0x3E, (byte) 0x0C, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x0F, (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x09, (byte) 0x67,
                (byte) 0x1F, (byte) 0x3F };
        compare(pdu, expectedResult);
    }

    public void e3_1bTest() {
        APDU pdu = new SimpleACK((byte) 1, 8);
        byte[] expectedResult = { (byte) 0x20, (byte) 0x01, (byte) 0x08 };
        compare(pdu, expectedResult);
    }

    public void e3_2aTest() {
        List<ReadAccessSpecification> readAccessSpecs = new ArrayList<ReadAccessSpecification>();

        List<PropertyReference> propertyReferences = new ArrayList<PropertyReference>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        propertyReferences.add(new PropertyReference(PropertyIdentifier.reliability, null));
        propertyReferences.add(new PropertyReference(PropertyIdentifier.description, null));
        readAccessSpecs.add(new ReadAccessSpecification(new ObjectIdentifier(ObjectType.analogInput, 12),
                new SequenceOf<PropertyReference>(propertyReferences)));

        propertyReferences = new ArrayList<PropertyReference>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        propertyReferences.add(new PropertyReference(PropertyIdentifier.reliability, null));
        propertyReferences.add(new PropertyReference(PropertyIdentifier.description, null));
        readAccessSpecs.add(new ReadAccessSpecification(new ObjectIdentifier(ObjectType.analogInput, 13),
                new SequenceOf<PropertyReference>(propertyReferences)));

        ConfirmedRequestService service = new RemoveListElementRequest(new ObjectIdentifier(ObjectType.group, 3),
                PropertyIdentifier.listOfGroupMembers, null, new SequenceOf<ReadAccessSpecification>(readAccessSpecs));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_206,
                (byte) 52, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x34, (byte) 0x09, (byte) 0x0C, (byte) 0x02,
                (byte) 0xC0, (byte) 0x00, (byte) 0x03, (byte) 0x19, (byte) 0x35, (byte) 0x3E, (byte) 0x0C, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x0C, (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x09, (byte) 0x67,
                (byte) 0x09, (byte) 0x1C, (byte) 0x1F, (byte) 0x0C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0D,
                (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x09, (byte) 0x67, (byte) 0x09, (byte) 0x1C, (byte) 0x1F,
                (byte) 0x3F };
        compare(pdu, expectedResult);
    }

    public void e3_2bTest() {
        APDU pdu = new SimpleACK((byte) 52, 9);
        byte[] expectedResult = { (byte) 0x20, (byte) 0x34, (byte) 0x09 };
        compare(pdu, expectedResult);
    }

    public void e3_2cTest() {
        List<ReadAccessSpecification> readAccessSpecs = new ArrayList<ReadAccessSpecification>();

        List<PropertyReference> propertyReferences = new ArrayList<PropertyReference>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        propertyReferences.add(new PropertyReference(PropertyIdentifier.reliability, null));
        readAccessSpecs.add(new ReadAccessSpecification(new ObjectIdentifier(ObjectType.analogInput, 12),
                new SequenceOf<PropertyReference>(propertyReferences)));

        propertyReferences = new ArrayList<PropertyReference>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        propertyReferences.add(new PropertyReference(PropertyIdentifier.reliability, null));
        readAccessSpecs.add(new ReadAccessSpecification(new ObjectIdentifier(ObjectType.analogInput, 13),
                new SequenceOf<PropertyReference>(propertyReferences)));

        ConfirmedRequestService service = new AddListElementRequest(new ObjectIdentifier(ObjectType.group, 3),
                PropertyIdentifier.listOfGroupMembers, null, new SequenceOf<ReadAccessSpecification>(readAccessSpecs));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_206,
                (byte) 53, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x35, (byte) 0x08, (byte) 0x0C, (byte) 0x02,
                (byte) 0xC0, (byte) 0x00, (byte) 0x03, (byte) 0x19, (byte) 0x35, (byte) 0x3E, (byte) 0x0C, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x0C, (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x09, (byte) 0x67,
                (byte) 0x1F, (byte) 0x0C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0D, (byte) 0x1E, (byte) 0x09,
                (byte) 0x55, (byte) 0x09, (byte) 0x67, (byte) 0x1F, (byte) 0x3F };
        compare(pdu, expectedResult);
    }

    public void e3_2dTest() {
        APDU pdu = new SimpleACK((byte) 53, 8);
        byte[] expectedResult = { (byte) 0x20, (byte) 0x35, (byte) 0x08 };
        compare(pdu, expectedResult);
    }

    public void e3_3aTest() {
        List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();
        propertyValues.add(new PropertyValue(PropertyIdentifier.objectName, null, new CharacterString(
                CharacterString.Encodings.ANSI_X3_4, "Trend 1"), null));
        propertyValues.add(new PropertyValue(PropertyIdentifier.fileAccessMethod, null, FileAccessMethod.recordAccess,
                null));
        ConfirmedRequestService service = new CreateObjectRequest(ObjectType.file, new SequenceOf<PropertyValue>(
                propertyValues));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 86, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x56, (byte) 0x0A, (byte) 0x0E, (byte) 0x09,
                (byte) 0x0A, (byte) 0x0F, (byte) 0x1E, (byte) 0x09, (byte) 0x4D, (byte) 0x2E, (byte) 0x75, (byte) 0x08,
                (byte) 0x00, (byte) 0x54, (byte) 0x72, (byte) 0x65, (byte) 0x6E, (byte) 0x64, (byte) 0x20, (byte) 0x31,
                (byte) 0x2F, (byte) 0x09, (byte) 0x29, (byte) 0x2E, (byte) 0x91, (byte) 0x00, (byte) 0x2F, (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    public void e3_3bTest() {
        AcknowledgementService service = new CreateObjectAck(new ObjectIdentifier(ObjectType.file, 13));
        APDU pdu = new ComplexACK(false, false, (byte) 86, 0, 0, service);
        byte[] expectedResult = { (byte) 0x30, (byte) 0x56, (byte) 0x0A, (byte) 0xC4, (byte) 0x02, (byte) 0x80,
                (byte) 0x00, (byte) 0x0D };
        compare(pdu, expectedResult);
    }

    public void e3_4aTest() {
        ConfirmedRequestService service = new DeleteObjectRequest(new ObjectIdentifier(ObjectType.group, 6));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 87, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x57, (byte) 0x0B, (byte) 0xC4, (byte) 0x02,
                (byte) 0xC0, (byte) 0x00, (byte) 0x06 };
        compare(pdu, expectedResult);
    }

    public void e3_4bTest() {
        APDU pdu = new SimpleACK((byte) 87, 11);
        byte[] expectedResult = { (byte) 0x20, (byte) 0x57, (byte) 0x0B };
        compare(pdu, expectedResult);
    }

    public void e3_4cTest() {
        ConfirmedRequestService service = new DeleteObjectRequest(new ObjectIdentifier(ObjectType.group, 7));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 88, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x58, (byte) 0x0B, (byte) 0xC4, (byte) 0x02,
                (byte) 0xC0, (byte) 0x00, (byte) 0x07 };
        compare(pdu, expectedResult);
    }

    public void e3_4dTest() {
        BaseError baseError = new BaseError((byte) 11, new BACnetError(ErrorClass.object,
                ErrorCode.objectDeletionNotPermitted));
        APDU pdu = new com.serotonin.bacnet4j.apdu.Error((byte) 88, baseError);
        byte[] expectedResult = { (byte) 0x50, (byte) 0x58, (byte) 0x0B, (byte) 0x91, (byte) 0x01, (byte) 0x91,
                (byte) 0x17 };
        compare(pdu, expectedResult);
    }

    public void e3_5aTest() {
        ConfirmedRequestService service = new ReadPropertyRequest(new ObjectIdentifier(ObjectType.analogInput, 5),
                PropertyIdentifier.presentValue, null);
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_50, (byte) 1,
                (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x0C, (byte) 0x0C, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x19, (byte) 0x55 };
        compare(pdu, expectedResult);
    }

    public void e3_5bTest() {
        AcknowledgementService service = new ReadPropertyAck(new ObjectIdentifier(ObjectType.analogInput, 5),
                PropertyIdentifier.presentValue, null, new Real(72.3f));
        APDU pdu = new ComplexACK(false, false, (byte) 1, 0, 0, service);
        byte[] expectedResult = { (byte) 0x30, (byte) 0x01, (byte) 0x0C, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x05, (byte) 0x19, (byte) 0x55, (byte) 0x3E, (byte) 0x44, (byte) 0x42, (byte) 0x90,
                (byte) 0x99, (byte) 0x9A, (byte) 0x3F };
        compare(pdu, expectedResult);
    }

    public void e3_6aTest() {
        List<SelectionCriteria> selectionCriteria = new ArrayList<SelectionCriteria>();
        selectionCriteria.add(new SelectionCriteria(PropertyIdentifier.objectType, null, RelationSpecifier.equal,
                ObjectType.binaryInput));
        ObjectSelectionCriteria objectSelectionCriteria = new ObjectSelectionCriteria(SelectionLogic.and,
                new SequenceOf<SelectionCriteria>(selectionCriteria));
        ConfirmedRequestService service = new ReadPropertyConditionalRequest(objectSelectionCriteria, null);
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 81, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x51, (byte) 0x0D, (byte) 0x0E, (byte) 0x09,
                (byte) 0x00, (byte) 0x1E, (byte) 0x09, (byte) 0x4F, (byte) 0x29, (byte) 0x00, (byte) 0x3E, (byte) 0x91,
                (byte) 0x03, (byte) 0x3F, (byte) 0x1F, (byte) 0x0F };
        compare(pdu, expectedResult);
    }

    public void e3_6bTest() {
        List<ReadAccessResult> readAccessResults = new ArrayList<ReadAccessResult>();
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.binaryInput, 1), null));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.binaryInput, 2), null));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.binaryInput, 3), null));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.binaryInput, 4), null));
        AcknowledgementService service = new ReadPropertyConditionalAck(new SequenceOf<ReadAccessResult>(
                readAccessResults));
        APDU pdu = new ComplexACK(false, false, (byte) 81, 0, 0, service);
        byte[] expectedResult = { (byte) 0x30, (byte) 0x51, (byte) 0x0D, (byte) 0x0C, (byte) 0x00, (byte) 0xC0,
                (byte) 0x00, (byte) 0x01, (byte) 0x0C, (byte) 0x00, (byte) 0xC0, (byte) 0x00, (byte) 0x02, (byte) 0x0C,
                (byte) 0x00, (byte) 0xC0, (byte) 0x00, (byte) 0x03, (byte) 0x0C, (byte) 0x00, (byte) 0xC0, (byte) 0x00,
                (byte) 0x04 };
        compare(pdu, expectedResult);
    }

    public void e3_6cTest() {
        List<SelectionCriteria> selectionCriteria = new ArrayList<SelectionCriteria>();
        selectionCriteria.add(new SelectionCriteria(PropertyIdentifier.objectType, null, RelationSpecifier.equal,
                ObjectType.analogInput));
        selectionCriteria.add(new SelectionCriteria(PropertyIdentifier.presentValue, null,
                RelationSpecifier.greaterThan, new Real(100)));
        ObjectSelectionCriteria objectSelectionCriteria = new ObjectSelectionCriteria(SelectionLogic.and,
                new SequenceOf<SelectionCriteria>(selectionCriteria));
        ConfirmedRequestService service = new ReadPropertyConditionalRequest(objectSelectionCriteria, null);
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 82, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x52, (byte) 0x0D, (byte) 0x0E, (byte) 0x09,
                (byte) 0x00, (byte) 0x1E, (byte) 0x09, (byte) 0x4F, (byte) 0x29, (byte) 0x00, (byte) 0x3E, (byte) 0x91,
                (byte) 0x00, (byte) 0x3F, (byte) 0x09, (byte) 0x55, (byte) 0x29, (byte) 0x03, (byte) 0x3E, (byte) 0x44,
                (byte) 0x42, (byte) 0xC8, (byte) 0x00, (byte) 0x00, (byte) 0x3F, (byte) 0x1F, (byte) 0x0F };
        compare(pdu, expectedResult);
    }

    public void e3_6dTest() {
        List<ReadAccessResult> readAccessResults = new ArrayList<ReadAccessResult>();

        List<Result> results = new ArrayList<Result>();
        results.add(new Result(PropertyIdentifier.presentValue, null, new Real(102.3f)));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.analogInput, 1),
                new SequenceOf<Result>(results)));

        results = new ArrayList<Result>();
        results.add(new Result(PropertyIdentifier.presentValue, null, new Real(180.7f)));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.analogInput, 2),
                new SequenceOf<Result>(results)));

        results = new ArrayList<Result>();
        results.add(new Result(PropertyIdentifier.presentValue, null, new Real(142.1f)));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.analogInput, 3),
                new SequenceOf<Result>(results)));

        AcknowledgementService service = new ReadPropertyConditionalAck(new SequenceOf<ReadAccessResult>(
                readAccessResults));
        APDU pdu = new ComplexACK(false, false, (byte) 82, 0, 0, service);
        byte[] expectedResult = { (byte) 0x30, (byte) 0x52, (byte) 0x0D, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x01, (byte) 0x1E, (byte) 0x29, (byte) 0x55, (byte) 0x4E, (byte) 0x44, (byte) 0x42,
                (byte) 0xCC, (byte) 0x99, (byte) 0x9A, (byte) 0x4F, (byte) 0x1F, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x02, (byte) 0x1E, (byte) 0x29, (byte) 0x55, (byte) 0x4E, (byte) 0x44, (byte) 0x43,
                (byte) 0x34, (byte) 0xB3, (byte) 0x33, (byte) 0x4F, (byte) 0x1F, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x03, (byte) 0x1E, (byte) 0x29, (byte) 0x55, (byte) 0x4E, (byte) 0x44, (byte) 0x43,
                (byte) 0x0E, (byte) 0x19, (byte) 0x9A, (byte) 0x4F, (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    public void e3_6eTest() {
        List<SelectionCriteria> selectionCriteria = new ArrayList<SelectionCriteria>();
        selectionCriteria.add(new SelectionCriteria(PropertyIdentifier.reliability, null, RelationSpecifier.notEqual,
                Reliability.noFaultDetected));
        selectionCriteria.add(new SelectionCriteria(PropertyIdentifier.outOfService, null, RelationSpecifier.equal,
                new Boolean(true)));
        ObjectSelectionCriteria objectSelectionCriteria = new ObjectSelectionCriteria(SelectionLogic.or,
                new SequenceOf<SelectionCriteria>(selectionCriteria));

        List<PropertyReference> propertyReferences = new ArrayList<PropertyReference>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        propertyReferences.add(new PropertyReference(PropertyIdentifier.reliability, null));
        propertyReferences.add(new PropertyReference(PropertyIdentifier.outOfService, null));

        ConfirmedRequestService service = new ReadPropertyConditionalRequest(objectSelectionCriteria,
                new SequenceOf<PropertyReference>(propertyReferences));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 83, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x53, (byte) 0x0D, (byte) 0x0E, (byte) 0x09,
                (byte) 0x01, (byte) 0x1E, (byte) 0x09, (byte) 0x67, (byte) 0x29, (byte) 0x01, (byte) 0x3E, (byte) 0x91,
                (byte) 0x00, (byte) 0x3F, (byte) 0x09, (byte) 0x51, (byte) 0x29, (byte) 0x00, (byte) 0x3E, (byte) 0x11,
                (byte) 0x3F, (byte) 0x1F, (byte) 0x0F, (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x09, (byte) 0x67,
                (byte) 0x09, (byte) 0x51, (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    public void e3_6fTest() {
        List<ReadAccessResult> readAccessResults = new ArrayList<ReadAccessResult>();

        List<Result> results = new ArrayList<Result>();
        results.add(new Result(PropertyIdentifier.presentValue, null, BinaryPV.active));
        results.add(new Result(PropertyIdentifier.reliability, null, Reliability.noFaultDetected));
        results.add(new Result(PropertyIdentifier.outOfService, null, new Boolean(true)));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.binaryInput, 1),
                new SequenceOf<Result>(results)));

        results = new ArrayList<Result>();
        results.add(new Result(PropertyIdentifier.presentValue, null, new Real(250)));
        results.add(new Result(PropertyIdentifier.reliability, null, Reliability.unreliableOther));
        results.add(new Result(PropertyIdentifier.outOfService, null, new Boolean(false)));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.analogInput, 2),
                new SequenceOf<Result>(results)));

        AcknowledgementService service = new ReadPropertyConditionalAck(new SequenceOf<ReadAccessResult>(
                readAccessResults));
        APDU pdu = new ComplexACK(false, false, (byte) 83, 0, 0, service);
        byte[] expectedResult = { (byte) 0x30, (byte) 0x53, (byte) 0x0D, (byte) 0x0C, (byte) 0x00, (byte) 0xC0,
                (byte) 0x00, (byte) 0x01, (byte) 0x1E, (byte) 0x29, (byte) 0x55, (byte) 0x4E, (byte) 0x91, (byte) 0x01,
                (byte) 0x4F, (byte) 0x29, (byte) 0x67, (byte) 0x4E, (byte) 0x91, (byte) 0x00, (byte) 0x4F, (byte) 0x29,
                (byte) 0x51, (byte) 0x4E, (byte) 0x11, (byte) 0x4F, (byte) 0x1F, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x02, (byte) 0x1E, (byte) 0x29, (byte) 0x55, (byte) 0x4E, (byte) 0x44, (byte) 0x43,
                (byte) 0x7A, (byte) 0x00, (byte) 0x00, (byte) 0x4F, (byte) 0x29, (byte) 0x67, (byte) 0x4E, (byte) 0x91,
                (byte) 0x07, (byte) 0x4F, (byte) 0x29, (byte) 0x51, (byte) 0x4E, (byte) 0x10, (byte) 0x4F, (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    public void e3_6gTest() {
        List<SelectionCriteria> selectionCriteria = new ArrayList<SelectionCriteria>();
        selectionCriteria.add(new SelectionCriteria(PropertyIdentifier.objectName, null, RelationSpecifier.equal,
                new CharacterString(CharacterString.Encodings.ANSI_X3_4, "C* Pressure")));
        selectionCriteria.add(new SelectionCriteria(PropertyIdentifier.objectName, null, RelationSpecifier.equal,
                new CharacterString(CharacterString.Encodings.ANSI_X3_4, "AC? Supply Temp")));
        ObjectSelectionCriteria objectSelectionCriteria = new ObjectSelectionCriteria(SelectionLogic.or,
                new SequenceOf<SelectionCriteria>(selectionCriteria));

        List<PropertyReference> propertyReferences = new ArrayList<PropertyReference>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.objectName, null));

        ConfirmedRequestService service = new ReadPropertyConditionalRequest(objectSelectionCriteria,
                new SequenceOf<PropertyReference>(propertyReferences));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 84, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x54, (byte) 0x0D, (byte) 0x0E, (byte) 0x09,
                (byte) 0x01, (byte) 0x1E, (byte) 0x09, (byte) 0x4D, (byte) 0x29, (byte) 0x00, (byte) 0x3E, (byte) 0x75,
                (byte) 0x0C, (byte) 0x00, (byte) 0x43, (byte) 0x2A, (byte) 0x20, (byte) 0x50, (byte) 0x72, (byte) 0x65,
                (byte) 0x73, (byte) 0x73, (byte) 0x75, (byte) 0x72, (byte) 0x65, (byte) 0x3F, (byte) 0x09, (byte) 0x4D,
                (byte) 0x29, (byte) 0x00, (byte) 0x3E, (byte) 0x75, (byte) 0x10, (byte) 0x00, (byte) 0x41, (byte) 0x43,
                (byte) 0x3F, (byte) 0x20, (byte) 0x53, (byte) 0x75, (byte) 0x70, (byte) 0x70, (byte) 0x6C, (byte) 0x79,
                (byte) 0x20, (byte) 0x54, (byte) 0x65, (byte) 0x6D, (byte) 0x70, (byte) 0x3F, (byte) 0x1F, (byte) 0x0F,
                (byte) 0x1E, (byte) 0x09, (byte) 0x4D, (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    public void e3_6hTest() {
        List<ReadAccessResult> readAccessResults = new ArrayList<ReadAccessResult>();

        List<Result> results = new ArrayList<Result>();
        results.add(new Result(PropertyIdentifier.objectName, null, new CharacterString(
                CharacterString.Encodings.ANSI_X3_4, "AC1 Supply Temp")));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.analogInput, 4),
                new SequenceOf<Result>(results)));

        results = new ArrayList<Result>();
        results.add(new Result(PropertyIdentifier.objectName, null, new CharacterString(
                CharacterString.Encodings.ANSI_X3_4, "CWP1 Pressure")));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.analogInput, 7),
                new SequenceOf<Result>(results)));

        results = new ArrayList<Result>();
        results.add(new Result(PropertyIdentifier.objectName, null, new CharacterString(
                CharacterString.Encodings.ANSI_X3_4, "Chiller 1 Freon Pressure")));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.analogInput, 8),
                new SequenceOf<Result>(results)));

        results = new ArrayList<Result>();
        results.add(new Result(PropertyIdentifier.objectName, null, new CharacterString(
                CharacterString.Encodings.ANSI_X3_4, "AC2 Supply Temp")));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.analogInput, 10),
                new SequenceOf<Result>(results)));

        results = new ArrayList<Result>();
        results.add(new Result(PropertyIdentifier.objectName, null, new CharacterString(
                CharacterString.Encodings.ANSI_X3_4, "AC3 Supply Temp")));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.analogInput, 12),
                new SequenceOf<Result>(results)));

        AcknowledgementService service = new ReadPropertyConditionalAck(new SequenceOf<ReadAccessResult>(
                readAccessResults));
        APDU pdu = new ComplexACK(false, false, (byte) 84, 0, 0, service);
        byte[] expectedResult = { (byte) 0x30, (byte) 0x54, (byte) 0x0D, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x04, (byte) 0x1E, (byte) 0x29, (byte) 0x4D, (byte) 0x4E, (byte) 0x75, (byte) 0x10,
                (byte) 0x00, (byte) 0x41, (byte) 0x43, (byte) 0x31, (byte) 0x20, (byte) 0x53, (byte) 0x75, (byte) 0x70,
                (byte) 0x70, (byte) 0x6C, (byte) 0x79, (byte) 0x20, (byte) 0x54, (byte) 0x65, (byte) 0x6D, (byte) 0x70,
                (byte) 0x4F, (byte) 0x1F, (byte) 0x0C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0x1E,
                (byte) 0x29, (byte) 0x4D, (byte) 0x4E, (byte) 0x75, (byte) 0x0E, (byte) 0x00, (byte) 0x43, (byte) 0x57,
                (byte) 0x50, (byte) 0x31, (byte) 0x20, (byte) 0x50, (byte) 0x72, (byte) 0x65, (byte) 0x73, (byte) 0x73,
                (byte) 0x75, (byte) 0x72, (byte) 0x65, (byte) 0x4F, (byte) 0x1F, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x08, (byte) 0x1E, (byte) 0x29, (byte) 0x4D, (byte) 0x4E, (byte) 0x75, (byte) 0x19,
                (byte) 0x00, (byte) 0x43, (byte) 0x68, (byte) 0x69, (byte) 0x6C, (byte) 0x6C, (byte) 0x65, (byte) 0x72,
                (byte) 0x20, (byte) 0x31, (byte) 0x20, (byte) 0x46, (byte) 0x72, (byte) 0x65, (byte) 0x6F, (byte) 0x6E,
                (byte) 0x20, (byte) 0x50, (byte) 0x72, (byte) 0x65, (byte) 0x73, (byte) 0x73, (byte) 0x75, (byte) 0x72,
                (byte) 0x65, (byte) 0x4F, (byte) 0x1F, (byte) 0x0C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0A,
                (byte) 0x1E, (byte) 0x29, (byte) 0x4D, (byte) 0x4E, (byte) 0x75, (byte) 0x10, (byte) 0x00, (byte) 0x41,
                (byte) 0x43, (byte) 0x32, (byte) 0x20, (byte) 0x53, (byte) 0x75, (byte) 0x70, (byte) 0x70, (byte) 0x6C,
                (byte) 0x79, (byte) 0x20, (byte) 0x54, (byte) 0x65, (byte) 0x6D, (byte) 0x70, (byte) 0x4F, (byte) 0x1F,
                (byte) 0x0C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0C, (byte) 0x1E, (byte) 0x29, (byte) 0x4D,
                (byte) 0x4E, (byte) 0x75, (byte) 0x10, (byte) 0x00, (byte) 0x41, (byte) 0x43, (byte) 0x33, (byte) 0x20,
                (byte) 0x53, (byte) 0x75, (byte) 0x70, (byte) 0x70, (byte) 0x6C, (byte) 0x79, (byte) 0x20, (byte) 0x54,
                (byte) 0x65, (byte) 0x6D, (byte) 0x70, (byte) 0x4F, (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    public void e3_7aTest() {
        List<ReadAccessSpecification> readAccessSpecs = new ArrayList<ReadAccessSpecification>();
        List<PropertyReference> propertyReferences = new ArrayList<PropertyReference>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        propertyReferences.add(new PropertyReference(PropertyIdentifier.reliability, null));
        readAccessSpecs.add(new ReadAccessSpecification(new ObjectIdentifier(ObjectType.analogInput, 16),
                new SequenceOf<PropertyReference>(propertyReferences)));
        ConfirmedRequestService service = new ReadPropertyMultipleRequest(new SequenceOf<ReadAccessSpecification>(
                readAccessSpecs));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 241, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0xF1, (byte) 0x0E, (byte) 0x0C, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x09, (byte) 0x67,
                (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    public void e3_7bTest() {
        List<ReadAccessResult> readAccessResults = new ArrayList<ReadAccessResult>();
        List<Result> results = new ArrayList<Result>();
        results.add(new Result(PropertyIdentifier.presentValue, null, new Real(72.3f)));
        results.add(new Result(PropertyIdentifier.reliability, null, Reliability.noFaultDetected));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.analogInput, 16),
                new SequenceOf<Result>(results)));
        AcknowledgementService service = new ReadPropertyMultipleAck(
                new SequenceOf<ReadAccessResult>(readAccessResults));
        APDU pdu = new ComplexACK(false, false, (byte) 241, 0, 0, service);
        byte[] expectedResult = { (byte) 0x30, (byte) 0xF1, (byte) 0x0E, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x10, (byte) 0x1E, (byte) 0x29, (byte) 0x55, (byte) 0x4E, (byte) 0x44, (byte) 0x42,
                (byte) 0x90, (byte) 0x99, (byte) 0x9A, (byte) 0x4F, (byte) 0x29, (byte) 0x67, (byte) 0x4E, (byte) 0x91,
                (byte) 0x00, (byte) 0x4F, (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    public void e3_7cTest() {
        List<ReadAccessSpecification> readAccessSpecs = new ArrayList<ReadAccessSpecification>();

        List<PropertyReference> propertyReferences = new ArrayList<PropertyReference>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        readAccessSpecs.add(new ReadAccessSpecification(new ObjectIdentifier(ObjectType.analogInput, 33),
                new SequenceOf<PropertyReference>(propertyReferences)));

        propertyReferences = new ArrayList<PropertyReference>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        readAccessSpecs.add(new ReadAccessSpecification(new ObjectIdentifier(ObjectType.analogInput, 50),
                new SequenceOf<PropertyReference>(propertyReferences)));

        propertyReferences = new ArrayList<PropertyReference>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        readAccessSpecs.add(new ReadAccessSpecification(new ObjectIdentifier(ObjectType.analogInput, 35),
                new SequenceOf<PropertyReference>(propertyReferences)));

        ConfirmedRequestService service = new ReadPropertyMultipleRequest(new SequenceOf<ReadAccessSpecification>(
                readAccessSpecs));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 2, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x02, (byte) 0x0E, (byte) 0x0C, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x21, (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x1F, (byte) 0x0C,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x32, (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x1F,
                (byte) 0x0C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x23, (byte) 0x1E, (byte) 0x09, (byte) 0x55,
                (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    public void e3_7dTest() {
        List<ReadAccessResult> readAccessResults = new ArrayList<ReadAccessResult>();

        List<Result> results = new ArrayList<Result>();
        results.add(new Result(PropertyIdentifier.presentValue, null, new Real(42.3f)));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.analogInput, 33),
                new SequenceOf<Result>(results)));

        results = new ArrayList<Result>();
        results.add(new Result(PropertyIdentifier.presentValue, null, new BACnetError(ErrorClass.object,
                ErrorCode.unknownObject)));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.analogInput, 50),
                new SequenceOf<Result>(results)));

        results = new ArrayList<Result>();
        results.add(new Result(PropertyIdentifier.presentValue, null, new Real(435.7f)));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.analogInput, 35),
                new SequenceOf<Result>(results)));

        AcknowledgementService service = new ReadPropertyMultipleAck(
                new SequenceOf<ReadAccessResult>(readAccessResults));
        APDU pdu = new ComplexACK(false, false, (byte) 2, 0, 0, service);
        byte[] expectedResult = { (byte) 0x30, (byte) 0x02, (byte) 0x0E, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x21, (byte) 0x1E, (byte) 0x29, (byte) 0x55, (byte) 0x4E, (byte) 0x44, (byte) 0x42,
                (byte) 0x29, (byte) 0x33, (byte) 0x33, (byte) 0x4F, (byte) 0x1F, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x32, (byte) 0x1E, (byte) 0x29, (byte) 0x55, (byte) 0x5E, (byte) 0x91, (byte) 0x01,
                (byte) 0x91, (byte) 0x1F, (byte) 0x5F, (byte) 0x1F, (byte) 0x0C, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x23, (byte) 0x1E, (byte) 0x29, (byte) 0x55, (byte) 0x4E, (byte) 0x44, (byte) 0x43, (byte) 0xD9,
                (byte) 0xD9, (byte) 0x9A, (byte) 0x4F, (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    public void e3_8aTest() {
        // Deprecated parameters.
    }

    public void e3_8bTest() {
        List<BaseType> itemData = new ArrayList<BaseType>();
        itemData.add(new LogRecord(new DateTime(new Date(1998, Month.MARCH, 23, DayOfWeek.MONDAY), new Time(19, 54, 27,
                0)), false, new Real(18), new StatusFlags(false, false, false, false)));
        itemData.add(new LogRecord(new DateTime(new Date(1998, Month.MARCH, 23, DayOfWeek.MONDAY), new Time(19, 56, 27,
                0)), false, new Real(18.1f), new StatusFlags(false, false, false, false)));
        AcknowledgementService service = new ReadRangeAck(new ObjectIdentifier(ObjectType.trendLog, 1),
                PropertyIdentifier.logBuffer, null, new ResultFlags(true, true, false), new UnsignedInteger(2),
                new SequenceOf<BaseType>(itemData), null);
        APDU pdu = new ComplexACK(false, false, (byte) 1, 0, 0, service);
        byte[] expectedResult = { (byte) 0x30, (byte) 0x01, (byte) 0x1A, (byte) 0x0C, (byte) 0x05, (byte) 0x00,
                (byte) 0x00, (byte) 0x01, (byte) 0x19, (byte) 0x83, (byte) 0x3A, (byte) 0x05, (byte) 0xC0, (byte) 0x49,
                (byte) 0x02, (byte) 0x5E, (byte) 0x0E, (byte) 0xA4, (byte) 0x62, (byte) 0x03, (byte) 0x17, (byte) 0x01,
                (byte) 0xB4, (byte) 0x13, (byte) 0x36, (byte) 0x1B, (byte) 0x00, (byte) 0x0F, (byte) 0x1E, (byte) 0x2C,
                (byte) 0x41, (byte) 0x90, (byte) 0x00, (byte) 0x00, (byte) 0x1F, (byte) 0x2A, (byte) 0x04, (byte) 0x00,
                (byte) 0x0E, (byte) 0xA4, (byte) 0x62, (byte) 0x03, (byte) 0x17, (byte) 0x01, (byte) 0xB4, (byte) 0x13,
                (byte) 0x38, (byte) 0x1B, (byte) 0x00, (byte) 0x0F, (byte) 0x1E, (byte) 0x2C, (byte) 0x41, (byte) 0x90,
                (byte) 0xCC, (byte) 0xCD, (byte) 0x1F, (byte) 0x2A, (byte) 0x04, (byte) 0x00, (byte) 0x5F };
        compare(pdu, expectedResult);
    }

    public void e3_9aTest() {
        ConfirmedRequestService service = new WritePropertyRequest(new ObjectIdentifier(ObjectType.analogValue, 1),
                PropertyIdentifier.presentValue, null, new Real(180), null);
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 89, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x59, (byte) 0x0F, (byte) 0x0C, (byte) 0x00,
                (byte) 0x80, (byte) 0x00, (byte) 0x01, (byte) 0x19, (byte) 0x55, (byte) 0x3E, (byte) 0x44, (byte) 0x43,
                (byte) 0x34, (byte) 0x00, (byte) 0x00, (byte) 0x3F };
        compare(pdu, expectedResult);
    }

    public void e3_9bTest() {
        APDU pdu = new SimpleACK((byte) 89, 15);
        byte[] expectedResult = { (byte) 0x20, (byte) 0x59, (byte) 0x0F };
        compare(pdu, expectedResult);
    }

    public void e3_10aTest() {
        List<WriteAccessSpecification> writeAccessSpecs = new ArrayList<WriteAccessSpecification>();

        List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();
        propertyValues.add(new PropertyValue(PropertyIdentifier.presentValue, null, new Real(67), null));
        writeAccessSpecs.add(new WriteAccessSpecification(new ObjectIdentifier(ObjectType.analogValue, 5),
                new SequenceOf<PropertyValue>(propertyValues)));

        propertyValues = new ArrayList<PropertyValue>();
        propertyValues.add(new PropertyValue(PropertyIdentifier.presentValue, null, new Real(67), null));
        writeAccessSpecs.add(new WriteAccessSpecification(new ObjectIdentifier(ObjectType.analogValue, 6),
                new SequenceOf<PropertyValue>(propertyValues)));

        propertyValues = new ArrayList<PropertyValue>();
        propertyValues.add(new PropertyValue(PropertyIdentifier.presentValue, null, new Real(72), null));
        writeAccessSpecs.add(new WriteAccessSpecification(new ObjectIdentifier(ObjectType.analogValue, 7),
                new SequenceOf<PropertyValue>(propertyValues)));
        ConfirmedRequestService service = new WritePropertyMultipleRequest(new SequenceOf<WriteAccessSpecification>(
                writeAccessSpecs));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 1, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x01, (byte) 0x10, (byte) 0x0C, (byte) 0x00,
                (byte) 0x80, (byte) 0x00, (byte) 0x05, (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x2E, (byte) 0x44,
                (byte) 0x42, (byte) 0x86, (byte) 0x00, (byte) 0x00, (byte) 0x2F, (byte) 0x1F, (byte) 0x0C, (byte) 0x00,
                (byte) 0x80, (byte) 0x00, (byte) 0x06, (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x2E, (byte) 0x44,
                (byte) 0x42, (byte) 0x86, (byte) 0x00, (byte) 0x00, (byte) 0x2F, (byte) 0x1F, (byte) 0x0C, (byte) 0x00,
                (byte) 0x80, (byte) 0x00, (byte) 0x07, (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x2E, (byte) 0x44,
                (byte) 0x42, (byte) 0x90, (byte) 0x00, (byte) 0x00, (byte) 0x2F, (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    public void e3_10bTest() {
        APDU pdu = new SimpleACK((byte) 1, 16);
        byte[] expectedResult = { (byte) 0x20, (byte) 0x01, (byte) 0x10 };
        compare(pdu, expectedResult);
    }

    public void e4_1aTest() {
        ConfirmedRequestService service = new DeviceCommunicationControlRequest(new UnsignedInteger(5),
                EnableDisable.disable, new CharacterString(CharacterString.Encodings.ANSI_X3_4, "#egbdf!"));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 5, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x05, (byte) 0x11, (byte) 0x09, (byte) 0x05,
                (byte) 0x19, (byte) 0x01, (byte) 0x2D, (byte) 0x08, (byte) 0x00, (byte) 0x23, (byte) 0x65, (byte) 0x67,
                (byte) 0x62, (byte) 0x64, (byte) 0x66, (byte) 0x21 };
        compare(pdu, expectedResult);
    }

    public void e4_1bTest() {
        APDU pdu = new SimpleACK((byte) 5, 17);
        byte[] expectedResult = { (byte) 0x20, (byte) 0x05, (byte) 0x11 };
        compare(pdu, expectedResult);
    }

    public void e4_2aTest() {
        List<ElementSpecification> elements = new ArrayList<ElementSpecification>();
        elements.add(new ElementSpecification("value1", Real.class, false, false));
        elements.add(new ElementSpecification("value2", OctetString.class, false, false));
        SequenceDefinition def = new SequenceDefinition(elements);

        Map<String, Encodable> values = new HashMap<String, Encodable>();
        values.put("value1", new Real(72.4f));
        values.put("value2", new OctetString(new byte[] { 0x16, 0x49 }));
        Sequence parameters = new Sequence(def, values);

        ConfirmedPrivateTransferRequest.vendorServiceResolutions.put(new VendorServiceKey(new UnsignedInteger(25),
                new UnsignedInteger(8)), def);
        ConfirmedRequestService service = new ConfirmedPrivateTransferRequest(new UnsignedInteger(25),
                new UnsignedInteger(8), parameters);
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 85, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x55, (byte) 0x12, (byte) 0x09, (byte) 0x19,
                (byte) 0x19, (byte) 0x08, (byte) 0x2E, (byte) 0x44, (byte) 0x42, (byte) 0x90, (byte) 0xCC, (byte) 0xCD,
                (byte) 0x62, (byte) 0x16, (byte) 0x49, (byte) 0x2F };
        compare(pdu, expectedResult);
    }

    public void e4_2bTest() {
        AcknowledgementService service = new ConfirmedPrivateTransferAck(new UnsignedInteger(25),
                new UnsignedInteger(8), null);
        APDU pdu = new ComplexACK(false, false, (byte) 85, 0, 0, service);
        byte[] expectedResult = { (byte) 0x30, (byte) 0x55, (byte) 0x12, (byte) 0x09, (byte) 0x19, (byte) 0x19,
                (byte) 0x08 };
        compare(pdu, expectedResult);
    }

    public void e4_3Test() {
        List<ElementSpecification> elements = new ArrayList<ElementSpecification>();
        elements.add(new ElementSpecification("value1", Real.class, false, false));
        elements.add(new ElementSpecification("value2", OctetString.class, false, false));
        SequenceDefinition def = new SequenceDefinition(elements);

        Map<String, Encodable> values = new HashMap<String, Encodable>();
        values.put("value1", new Real(72.4f));
        values.put("value2", new OctetString(new byte[] { 0x16, 0x49 }));
        Sequence parameters = new Sequence(def, values);

        UnconfirmedPrivateTransferRequest.vendorServiceResolutions.put(new VendorServiceKey(new UnsignedInteger(25),
                new UnsignedInteger(8)), def);
        UnconfirmedRequestService service = new UnconfirmedPrivateTransferRequest(new UnsignedInteger(25),
                new UnsignedInteger(8), parameters);
        APDU pdu = new UnconfirmedRequest(service);
        byte[] expectedResult = { (byte) 0x10, (byte) 0x04, (byte) 0x09, (byte) 0x19, (byte) 0x19, (byte) 0x08,
                (byte) 0x2E, (byte) 0x44, (byte) 0x42, (byte) 0x90, (byte) 0xCC, (byte) 0xCD, (byte) 0x62, (byte) 0x16,
                (byte) 0x49, (byte) 0x2F };
        compare(pdu, expectedResult);
    }

    public void e4_4aTest() {
        ConfirmedRequestService service = new ReinitializeDeviceRequest(ReinitializedStateOfDevice.warmstart,
                new CharacterString(CharacterString.Encodings.ANSI_X3_4, "AbCdEfGh"));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_128,
                (byte) 2, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x14, (byte) 0x09, (byte) 0x01,
                (byte) 0x1D, (byte) 0x09, (byte) 0x00, (byte) 0x41, (byte) 0x62, (byte) 0x43, (byte) 0x64, (byte) 0x45,
                (byte) 0x66, (byte) 0x47, (byte) 0x68 };
        compare(pdu, expectedResult);
    }

    public void e4_4bTest() {
        APDU pdu = new SimpleACK((byte) 2, 20);
        byte[] expectedResult = { (byte) 0x20, (byte) 0x02, (byte) 0x14 };
        compare(pdu, expectedResult);
    }

    public void e4_5aTest() {
        ConfirmedRequestService service = new ConfirmedTextMessageRequest(new ObjectIdentifier(ObjectType.device, 5),
                MessagePriority.normal, new CharacterString(CharacterString.Encodings.ANSI_X3_4,
                        "PM required for PUMP347"));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_128,
                (byte) 3, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x01, (byte) 0x03, (byte) 0x13, (byte) 0x0C, (byte) 0x02,
                (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x29, (byte) 0x00, (byte) 0x3D, (byte) 0x18, (byte) 0x00,
                (byte) 0x50, (byte) 0x4D, (byte) 0x20, (byte) 0x72, (byte) 0x65, (byte) 0x71, (byte) 0x75, (byte) 0x69,
                (byte) 0x72, (byte) 0x65, (byte) 0x64, (byte) 0x20, (byte) 0x66, (byte) 0x6F, (byte) 0x72, (byte) 0x20,
                (byte) 0x50, (byte) 0x55, (byte) 0x4D, (byte) 0x50, (byte) 0x33, (byte) 0x34, (byte) 0x37 };
        compare(pdu, expectedResult);
    }

    public void e4_5bTest() {
        APDU pdu = new SimpleACK((byte) 3, 19);
        byte[] expectedResult = { (byte) 0x20, (byte) 0x03, (byte) 0x13 };
        compare(pdu, expectedResult);
    }

    public void e4_6Test() {
        UnconfirmedRequestService service = new UnconfirmedTextMessageRequest(
                new ObjectIdentifier(ObjectType.device, 5), MessagePriority.normal, new CharacterString(
                        CharacterString.Encodings.ANSI_X3_4, "PM required for PUMP347"));
        APDU pdu = new UnconfirmedRequest(service);
        byte[] expectedResult = { (byte) 0x10, (byte) 0x05, (byte) 0x0C, (byte) 0x02, (byte) 0x00, (byte) 0x00,
                (byte) 0x05, (byte) 0x29, (byte) 0x00, (byte) 0x3D, (byte) 0x18, (byte) 0x00, (byte) 0x50, (byte) 0x4D,
                (byte) 0x20, (byte) 0x72, (byte) 0x65, (byte) 0x71, (byte) 0x75, (byte) 0x69, (byte) 0x72, (byte) 0x65,
                (byte) 0x64, (byte) 0x20, (byte) 0x66, (byte) 0x6F, (byte) 0x72, (byte) 0x20, (byte) 0x50, (byte) 0x55,
                (byte) 0x4D, (byte) 0x50, (byte) 0x33, (byte) 0x34, (byte) 0x37 };
        compare(pdu, expectedResult);
    }

    public void e4_7Test() {
        UnconfirmedRequestService service = new TimeSynchronizationRequest(new DateTime(new Date(1992, Month.NOVEMBER,
                17, DayOfWeek.UNSPECIFIED), new Time(22, 45, 30, 70)));
        APDU pdu = new UnconfirmedRequest(service);
        byte[] expectedResult = { (byte) 0x10, (byte) 0x06, (byte) 0xA4, (byte) 0x5C, (byte) 0x0B, (byte) 0x11,
                (byte) 0xFF, (byte) 0xB4, (byte) 0x16, (byte) 0x2D, (byte) 0x1E, (byte) 0x46 };
        compare(pdu, expectedResult);
    }

    public void e4_8aTest() {
        UnconfirmedRequestService service = new WhoHasRequest(null, new CharacterString(
                CharacterString.Encodings.ANSI_X3_4, "OATemp"));
        APDU pdu = new UnconfirmedRequest(service);
        byte[] expectedResult = { (byte) 0x10, (byte) 0x07, (byte) 0x3D, (byte) 0x07, (byte) 0x00, (byte) 0x4F,
                (byte) 0x41, (byte) 0x54, (byte) 0x65, (byte) 0x6D, (byte) 0x70 };
        compare(pdu, expectedResult);
    }

    public void e4_8bTest() {
        UnconfirmedRequestService service = new IHaveRequest(new ObjectIdentifier(ObjectType.device, 8),
                new ObjectIdentifier(ObjectType.analogInput, 3), new CharacterString(
                        CharacterString.Encodings.ANSI_X3_4, "OATemp"));
        APDU pdu = new UnconfirmedRequest(service);
        byte[] expectedResult = { (byte) 0x10, (byte) 0x01, (byte) 0xC4, (byte) 0x02, (byte) 0x00, (byte) 0x00,
                (byte) 0x08, (byte) 0xC4, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x75, (byte) 0x07,
                (byte) 0x00, (byte) 0x4F, (byte) 0x41, (byte) 0x54, (byte) 0x65, (byte) 0x6D, (byte) 0x70 };
        compare(pdu, expectedResult);
    }

    public void e4_8cTest() {
        UnconfirmedRequestService service = new WhoHasRequest(null, new ObjectIdentifier(ObjectType.analogInput, 3));
        APDU pdu = new UnconfirmedRequest(service);
        byte[] expectedResult = { (byte) 0x10, (byte) 0x07, (byte) 0x2C, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x03 };
        compare(pdu, expectedResult);
    }

    public void e4_8dTest() {
        UnconfirmedRequestService service = new IHaveRequest(new ObjectIdentifier(ObjectType.device, 8),
                new ObjectIdentifier(ObjectType.analogInput, 3), new CharacterString(
                        CharacterString.Encodings.ANSI_X3_4, "OATemp"));
        APDU pdu = new UnconfirmedRequest(service);
        byte[] expectedResult = { (byte) 0x10, (byte) 0x01, (byte) 0xC4, (byte) 0x02, (byte) 0x00, (byte) 0x00,
                (byte) 0x08, (byte) 0xC4, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x75, (byte) 0x07,
                (byte) 0x00, (byte) 0x4F, (byte) 0x41, (byte) 0x54, (byte) 0x65, (byte) 0x6D, (byte) 0x70 };
        compare(pdu, expectedResult);
    }

    public void e4_9aTest() {
        UnconfirmedRequestService service = new WhoIsRequest(new UnsignedInteger(3), new UnsignedInteger(3));
        APDU pdu = new UnconfirmedRequest(service);
        byte[] expectedResult = { (byte) 0x10, (byte) 0x08, (byte) 0x09, (byte) 0x03, (byte) 0x19, (byte) 0x03 };
        compare(pdu, expectedResult);
    }

    public void e4_9bTest() {
        UnconfirmedRequestService service = new IAmRequest(new ObjectIdentifier(ObjectType.device, 3),
                new UnsignedInteger(1024), Segmentation.noSegmentation, new UnsignedInteger(99));
        APDU pdu = new UnconfirmedRequest(service);
        byte[] expectedResult = { (byte) 0x10, (byte) 0x00, (byte) 0xC4, (byte) 0x02, (byte) 0x00, (byte) 0x00,
                (byte) 0x03, (byte) 0x22, (byte) 0x04, (byte) 0x00, (byte) 0x91, (byte) 0x03, (byte) 0x21, (byte) 0x63 };
        compare(pdu, expectedResult);
    }

    public void e4_9cTest() {
        UnconfirmedRequestService service = new WhoIsRequest();
        APDU pdu = new UnconfirmedRequest(service);
        byte[] expectedResult = { (byte) 0x10, (byte) 0x08 };
        compare(pdu, expectedResult);
    }

    public void e4_9dTest() {
        UnconfirmedRequestService service = new IAmRequest(new ObjectIdentifier(ObjectType.device, 1),
                new UnsignedInteger(480), Segmentation.segmentedTransmit, new UnsignedInteger(99));
        APDU pdu = new UnconfirmedRequest(service);
        byte[] expectedResult = { (byte) 0x10, (byte) 0x00, (byte) 0xC4, (byte) 0x02, (byte) 0x00, (byte) 0x00,
                (byte) 0x01, (byte) 0x22, (byte) 0x01, (byte) 0xE0, (byte) 0x91, (byte) 0x01, (byte) 0x21, (byte) 0x63 };
        compare(pdu, expectedResult);
    }

    public void e4_9eTest() {
        UnconfirmedRequestService service = new IAmRequest(new ObjectIdentifier(ObjectType.device, 2),
                new UnsignedInteger(206), Segmentation.segmentedReceive, new UnsignedInteger(33));
        APDU pdu = new UnconfirmedRequest(service);
        byte[] expectedResult = { (byte) 0x10, (byte) 0x00, (byte) 0xC4, (byte) 0x02, (byte) 0x00, (byte) 0x00,
                (byte) 0x02, (byte) 0x21, (byte) 0xCE, (byte) 0x91, (byte) 0x02, (byte) 0x21, (byte) 0x21 };
        compare(pdu, expectedResult);
    }

    public void e4_9fTest() {
        UnconfirmedRequestService service = new IAmRequest(new ObjectIdentifier(ObjectType.device, 3),
                new UnsignedInteger(1024), Segmentation.noSegmentation, new UnsignedInteger(99));
        APDU pdu = new UnconfirmedRequest(service);
        byte[] expectedResult = { (byte) 0x10, (byte) 0x00, (byte) 0xC4, (byte) 0x02, (byte) 0x00, (byte) 0x00,
                (byte) 0x03, (byte) 0x22, (byte) 0x04, (byte) 0x00, (byte) 0x91, (byte) 0x03, (byte) 0x21, (byte) 0x63 };
        compare(pdu, expectedResult);
    }

    public void e4_9gTest() {
        UnconfirmedRequestService service = new IAmRequest(new ObjectIdentifier(ObjectType.device, 4),
                new UnsignedInteger(128), Segmentation.segmentedBoth, new UnsignedInteger(66));
        APDU pdu = new UnconfirmedRequest(service);
        byte[] expectedResult = { (byte) 0x10, (byte) 0x00, (byte) 0xC4, (byte) 0x02, (byte) 0x00, (byte) 0x00,
                (byte) 0x04, (byte) 0x21, (byte) 0x80, (byte) 0x91, (byte) 0x00, (byte) 0x21, (byte) 0x42 };
        compare(pdu, expectedResult);
    }

    public void e5_aTest() {
        ConfirmedRequestService service = new VtOpenRequest(VtClass.ansi_x3_64, new UnsignedInteger(5));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_128,
                (byte) 80, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x01, (byte) 0x50, (byte) 0x15, (byte) 0x91, (byte) 0x01,
                (byte) 0x21, (byte) 0x05 };
        compare(pdu, expectedResult);
    }

    public void e5_bTest() {
        AcknowledgementService service = new VtOpenAck(new UnsignedInteger(29));
        APDU pdu = new ComplexACK(false, false, (byte) 80, 0, 0, service);
        byte[] expectedResult = { (byte) 0x30, (byte) 0x50, (byte) 0x15, (byte) 0x21, (byte) 0x1D };
        compare(pdu, expectedResult);
    }

    public void e5_cTest() {
        byte[] data = "\r\nEnter User Name:".getBytes();
        ConfirmedRequestService service = new VtDataRequest(new UnsignedInteger(5), new OctetString(data),
                new UnsignedInteger(0));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_128,
                (byte) 81, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x01, (byte) 0x51, (byte) 0x17, (byte) 0x21, (byte) 0x05,
                (byte) 0x65, (byte) 0x12, (byte) 0x0D, (byte) 0x0A, (byte) 0x45, (byte) 0x6E, (byte) 0x74, (byte) 0x65,
                (byte) 0x72, (byte) 0x20, (byte) 0x55, (byte) 0x73, (byte) 0x65, (byte) 0x72, (byte) 0x20, (byte) 0x4E,
                (byte) 0x61, (byte) 0x6D, (byte) 0x65, (byte) 0x3A, (byte) 0x21, (byte) 0x00 };
        compare(pdu, expectedResult);
    }

    public void e5_dTest() {
        AcknowledgementService service = new VtDataAck(new Boolean(true), null);
        APDU pdu = new ComplexACK(false, false, (byte) 81, 0, 0, service);
        byte[] expectedResult = { (byte) 0x30, (byte) 0x51, (byte) 0x17, (byte) 0x09, (byte) 0x01 };
        compare(pdu, expectedResult);
    }

    public void e5_eTest() {
        ConfirmedRequestService service = new VtDataRequest(new UnsignedInteger(29), new OctetString(
                "FRED\r".getBytes()), new UnsignedInteger(0));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_128,
                (byte) 82, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x01, (byte) 0x52, (byte) 0x17, (byte) 0x21, (byte) 0x1D,
                (byte) 0x65, (byte) 0x05, (byte) 0x46, (byte) 0x52, (byte) 0x45, (byte) 0x44, (byte) 0x0D, (byte) 0x21,
                (byte) 0x00 };
        compare(pdu, expectedResult);
    }

    public void e5_fTest() {
        AcknowledgementService service = new VtDataAck(new Boolean(true), null);
        APDU pdu = new ComplexACK(false, false, (byte) 82, 0, 0, service);
        byte[] expectedResult = { (byte) 0x30, (byte) 0x52, (byte) 0x17, (byte) 0x09, (byte) 0x01 };
        compare(pdu, expectedResult);
    }

    public void e5_gTest() {
        ConfirmedRequestService service = new VtDataRequest(new UnsignedInteger(5), new OctetString(
                "FRED\r\nEnter Password:".getBytes()), new UnsignedInteger(1));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_128,
                (byte) 83, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x01, (byte) 0x53, (byte) 0x17, (byte) 0x21, (byte) 0x05,
                (byte) 0x65, (byte) 0x15, (byte) 0x46, (byte) 0x52, (byte) 0x45, (byte) 0x44, (byte) 0x0D, (byte) 0x0A,
                (byte) 0x45, (byte) 0x6E, (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x20, (byte) 0x50, (byte) 0x61,
                (byte) 0x73, (byte) 0x73, (byte) 0x77, (byte) 0x6F, (byte) 0x72, (byte) 0x64, (byte) 0x3A, (byte) 0x21,
                (byte) 0x01 };
        compare(pdu, expectedResult);
    }

    public void e5_hTest() {
        AcknowledgementService service = new VtDataAck(new Boolean(true), null);
        APDU pdu = new ComplexACK(false, false, (byte) 83, 0, 0, service);
        byte[] expectedResult = { (byte) 0x30, (byte) 0x53, (byte) 0x17, (byte) 0x09, (byte) 0x01 };
        compare(pdu, expectedResult);
    }

    public void e5_iTest() {
        List<UnsignedInteger> ids = new ArrayList<UnsignedInteger>();
        ids.add(new UnsignedInteger(29));
        ConfirmedRequestService service = new VtCloseRequest(new SequenceOf<UnsignedInteger>(ids));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_128,
                (byte) 84, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x01, (byte) 0x54, (byte) 0x16, (byte) 0x21, (byte) 0x1D };
        compare(pdu, expectedResult);
    }

    public void e5_jTest() {
        APDU pdu = new SimpleACK((byte) 84, 22);
        byte[] expectedResult = { (byte) 0x20, (byte) 0x54, (byte) 0x16 };
        compare(pdu, expectedResult);
    }

    public void e6_aTest() {
        ConfirmedRequestService service = new RequestKeyRequest(new ObjectIdentifier(ObjectType.device, 1),
                new Address(new Unsigned16(2), new OctetString(new byte[] { 17 })), new ObjectIdentifier(
                        ObjectType.device, 2), new Address(new Unsigned16(2), new OctetString(new byte[] { 34 })));
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1476,
                (byte) 15, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x05, (byte) 0x0f, (byte) 0x19, (byte) 0xc4, (byte) 0x02,
                (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x21, (byte) 0x02, (byte) 0x61, (byte) 0x11, (byte) 0xc4,
                (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x21, (byte) 0x02, (byte) 0x61, (byte) 0x22 };
        compare(pdu, expectedResult);
    }

    public void e6_bTest() {
        ConfirmedRequestService service = new AuthenticateRequest(new UnsignedInteger(305419896), new UnsignedInteger(
                15), null, null, null);
        APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1476,
                (byte) 1, (byte) 0, 0, service);
        byte[] expectedResult = { (byte) 0x00, (byte) 0x05, (byte) 0x01, (byte) 0x18, (byte) 0x0c, (byte) 0x12,
                (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x19, (byte) 0x0f };
        compare(pdu, expectedResult);
    }

    public void e6_cTest() {
        AcknowledgementService service = new AuthenticateAck(new UnsignedInteger(BigInteger.valueOf(2478168049l)));
        APDU pdu = new ComplexACK(false, false, (byte) 1, 0, 0, service);
        byte[] expectedResult = { (byte) 0x30, (byte) 0x01, (byte) 0x18, (byte) 0x24, (byte) 0x93, (byte) 0xb5,
                (byte) 0xd7, (byte) 0xf1 };
        compare(pdu, expectedResult);
    }

    public void executeAll() {
        e1_1aTest();
        e1_1bTest();
        e1_2aTest();
        e1_2bTest();
        e1_3Test();
        e1_4aTest();
        e1_4bTest();
        e1_5Test();
        e1_6aTest();
        e1_6bTest();
        e1_7aTest();
        e1_7bTest();
        e1_7cTest();
        e1_7dTest();
        e1_8aTest();
        e1_8bTest();
        e1_9aTest();
        e1_9bTest();
        e1_10aTest();
        e1_10bTest();
        e1_11aTest();
        e1_11bTest();

        e2_1aTest();
        e2_1bTest();
        e2_1cTest();
        e2_1dTest();
        e2_2aTest();
        e2_2bTest();
        e2_2cTest();
        e2_2dTest();

        e3_1aTest();
        e3_1bTest();
        e3_2aTest();
        e3_2bTest();
        e3_2cTest();
        e3_2dTest();
        e3_3aTest();
        e3_3bTest();
        e3_4aTest();
        e3_4bTest();
        e3_4cTest();
        e3_4dTest();
        e3_5aTest();
        e3_5bTest();
        e3_6aTest();
        e3_6bTest();
        e3_6cTest();
        e3_6dTest();
        e3_6eTest();
        e3_6fTest();
        e3_6gTest();
        e3_6hTest();
        e3_7aTest();
        e3_7bTest();
        e3_7cTest();
        e3_7dTest();
        e3_8aTest();
        e3_8bTest();
        e3_9aTest();
        e3_9bTest();
        e3_10aTest();
        e3_10bTest();

        e4_1aTest();
        e4_1bTest();
        e4_2aTest();
        e4_2bTest();
        e4_3Test();
        e4_4aTest();
        e4_4bTest();
        e4_5aTest();
        e4_5bTest();
        e4_6Test();
        e4_7Test();
        e4_8aTest();
        e4_8bTest();
        e4_8cTest();
        e4_8dTest();
        e4_9aTest();
        e4_9bTest();
        e4_9cTest();
        e4_9dTest();
        e4_9eTest();
        e4_9fTest();
        e4_9gTest();

        e5_aTest();
        e5_bTest();
        e5_cTest();
        e5_dTest();
        e5_eTest();
        e5_fTest();
        e5_gTest();
        e5_hTest();
        e5_iTest();
        e5_jTest();

        e6_aTest();
        e6_bTest();
        e6_cTest();
    }

    private void compare(APDU pdu, byte[] expectedResult) {
        ByteQueue queue = new ByteQueue();
        pdu.write(queue);

        if (queue.size() != expectedResult.length)
            throw new RuntimeException("Size of queue differs from expected: " + queue);

        for (int i = 0; i < expectedResult.length; i++) {
            if (queue.peek(i) != expectedResult[i])
                throw new RuntimeException("Unexpected content at index " + i + ": " + queue);
        }

        APDU parsedAPDU;
        try {
            parsedAPDU = APDU.createAPDU(servicesSupported, queue);
            if (parsedAPDU instanceof Segmentable)
                ((Segmentable) parsedAPDU).parseServiceData();
        }
        catch (BACnetException e) {
            throw new RuntimeException(e);
        }

        if (queue.size() != 0)
            throw new RuntimeException("Queue not empty after parse");
        if (!parsedAPDU.equals(pdu)) {
            parsedAPDU.equals(pdu); // For debugging
            parsedAPDU.equals(pdu);
            parsedAPDU.equals(pdu);
            throw new RuntimeException("Parsed APDU does not equal given APDU");
        }
    }
}
