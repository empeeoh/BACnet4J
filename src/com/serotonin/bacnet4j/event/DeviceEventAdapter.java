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
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

/**
 * A default class for easy implementation of the DeviceEventListener interface. 
 * Instead of having to implement all of the defined methods, listener classes 
 * can override this and only implement the desired methods.
 * 
 * @author Matthew Lohbihler
 */
public class DeviceEventAdapter implements DeviceEventListener {
    @Override
    public void listenerException(Throwable e) {
        // Override as required
        e.printStackTrace();
    }

    @Override
    public boolean allowPropertyWrite(BACnetObject obj, PropertyValue pv) {
        return true;
    }

    @Override
    public void iAmReceived(final RemoteDevice d) {
        // Override as required
    }

    @Override
    public void propertyWritten(final BACnetObject obj, final PropertyValue pv) {
        // Override as required
    }

    @Override
    public void iHaveReceived(final RemoteDevice d, final RemoteObject o) {
        // Override as required
    }

    @Override
    public void covNotificationReceived(final UnsignedInteger subscriberProcessIdentifier,
            final RemoteDevice initiatingDevice, final ObjectIdentifier monitoredObjectIdentifier,
            final UnsignedInteger timeRemaining, final SequenceOf<PropertyValue> listOfValues) {
        // Override as required
    }

    @Override
    public void eventNotificationReceived(final UnsignedInteger processIdentifier, final RemoteDevice initiatingDevice,
            final ObjectIdentifier eventObjectIdentifier, final TimeStamp timeStamp,
            final UnsignedInteger notificationClass, final UnsignedInteger priority, final EventType eventType,
            final CharacterString messageText, final NotifyType notifyType,
            final com.serotonin.bacnet4j.type.primitive.Boolean ackRequired, final EventState fromState,
            final EventState toState, final NotificationParameters eventValues) {
        // Override as required
    }

    @Override
    public void textMessageReceived(final RemoteDevice textMessageSourceDevice, final Choice messageClass,
            final MessagePriority messagePriority, final CharacterString message) {
        // Override as required
    }

    @Override
    public void privateTransferReceived(final UnsignedInteger vendorId, final UnsignedInteger serviceNumber,
            final Encodable serviceParameters) {
        // Override as required
    }

    @Override
    public void reinitializeDevice(final ReinitializedStateOfDevice reinitializedStateOfDevice) {
        // Override as required
    }

    @Override
    public void synchronizeTime(final DateTime dateTime, final boolean utc) {
        // Override as required
    }
}
