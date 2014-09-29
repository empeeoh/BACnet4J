/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * When signing a commercial license with Serotonin Software Technologies Inc.,
 * the following extension to GPL is made. A special exception to the GPL is 
 * included to allow you to distribute a combined work that includes BAcnet4J 
 * without being obliged to provide the source code for any proprietary components.
 */
package com.serotonin.bacnet4j.event;

import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.RemoteObject;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.service.confirmed.ReinitializeDeviceRequest.ReinitializedStateOfDevice;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Choice;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.EventType;
import com.serotonin.bacnet4j.type.enumerated.MessagePriority;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;
import com.serotonin.bacnet4j.type.notificationParameters.NotificationParameters;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

/**
 * There are two versions of each method. The "regular" version runs in a worker
 * thread so that client code need not be concerned with completing its processing
 * in any timely manner. The "sync" version runs within the dispatching thread, 
 * and so MUST return as quickly as possible. In particular, overrides of the 
 * sync methods MUST NOT send requests.
 * 
 * @author Matthew
 */
public interface DeviceEventListener {
    /**
     * Notification of an exception while calling a listener method.
     */
    void listenerException(Throwable e);

    /**
     * Notification of receipt of an IAm message.
     * 
     * @param d
     */
    void iAmReceived(RemoteDevice d);

    /**
     * Allow a listener to veto an attempt by another device to write a property in a local object.
     * 
     * @param obj
     * @param pv
     * @return true if the write should be allowed.
     */
    boolean allowPropertyWrite(BACnetObject obj, PropertyValue pv);

    /**
     * Notification that a property of a local object was written by another device.
     * 
     * @param obj
     * @param pv
     */
    void propertyWritten(BACnetObject obj, PropertyValue pv);

    /**
     * Notification of receipt of an IHave message.
     * 
     * @param d
     * @param o
     */
    void iHaveReceived(RemoteDevice d, RemoteObject o);

    /**
     * Notification of either an UnconfirmedCovNotificationRequest or a ConfirmedCovNotificationRequest. The latter will
     * be automatically confirmed by the service handler.
     * 
     * @param subscriberProcessIdentifier
     * @param initiatingDevice
     * @param monitoredObjectIdentifier
     * @param timeRemaining
     * @param listOfValues
     */
    void covNotificationReceived(UnsignedInteger subscriberProcessIdentifier, RemoteDevice initiatingDevice,
            ObjectIdentifier monitoredObjectIdentifier, UnsignedInteger timeRemaining,
            SequenceOf<PropertyValue> listOfValues);

    /**
     * Notification of either an UnconfirmedEventNotificationRequest or a ConfirmedEventNotificationRequest. The latter
     * will be automatically confirmed by the service handler.
     * 
     * @param processIdentifier
     * @param initiatingDevice
     * @param eventObjectIdentifier
     * @param timeStamp
     * @param notificationClass
     * @param priority
     * @param eventType
     * @param messageText
     * @param notifyType
     * @param ackRequired
     * @param fromState
     * @param toState
     * @param eventValues
     */
    void eventNotificationReceived(UnsignedInteger processIdentifier, RemoteDevice initiatingDevice,
            ObjectIdentifier eventObjectIdentifier, TimeStamp timeStamp, UnsignedInteger notificationClass,
            UnsignedInteger priority, EventType eventType, CharacterString messageText, NotifyType notifyType,
            Boolean ackRequired, EventState fromState, EventState toState, NotificationParameters eventValues);

    /**
     * Notification of either an UnconfirmedTextMessageRequest or a ConfirmedTextMessageRequest. The latter will be
     * automatically confirmed by the service handler.
     * 
     * @param textMessageSourceDevice
     * @param messageClass
     * @param messagePriority
     * @param message
     */
    void textMessageReceived(RemoteDevice textMessageSourceDevice, Choice messageClass,
            MessagePriority messagePriority, CharacterString message);

    /**
     * Notification of either an UnconfirmedPrivateTransferRequest or a ConfirmedPrivateTransferRequest. The latter will
     * be automatically confirmed by the service handler.
     * 
     * @param vendorId
     * @param serviceNumber
     * @param serviceParameters
     */
    void privateTransferReceived(UnsignedInteger vendorId, UnsignedInteger serviceNumber, Encodable serviceParameters);

    /**
     * Notification that the device should be reinitialized. The local device's password has already been validated at
     * this point, the the indicated action should be carried out.
     * 
     * @param reinitializedStateOfDevice
     */
    void reinitializeDevice(ReinitializedStateOfDevice reinitializedStateOfDevice);

    /**
     * Notification that the device should synchronize its time to the given date/time value. The local device has
     * already been checked at this point to ensure that it supports time synchronization.
     * 
     * @param dateTime
     * @param utc
     *            true if a UTCTimeSynchronizationRequest was sent, false if TimeSynchronizationRequest.
     */
    void synchronizeTime(DateTime dateTime, boolean utc);
}