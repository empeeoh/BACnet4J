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

import java.util.concurrent.ConcurrentLinkedQueue;

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
 * Class to handle various events that occur on the local device. This class accepts 0 to many listeners, and dispatches
 * notifications synchronously.
 * 
 * @author Matthew Lohbihler
 */
public class DeviceEventHandler {
    final ConcurrentLinkedQueue<DeviceEventListener> listeners = new ConcurrentLinkedQueue<DeviceEventListener>();

    //
    //
    // Listener management
    //
    public void addListener(DeviceEventListener l) {
        listeners.add(l);
    }

    public void removeListener(DeviceEventListener l) {
        listeners.remove(l);
    }

    public int getListenerCount() {
        return listeners.size();
    }

    //
    //
    // Checks and notifications
    //
    public boolean checkAllowPropertyWrite(BACnetObject obj, PropertyValue pv) {
        for (DeviceEventListener l : listeners) {
            try {
                if (!l.allowPropertyWrite(obj, pv))
                    return false;
            }
            catch (Throwable e) {
                handleException(l, e);
            }
        }
        return true;
    }

    public void fireIAmReceived(final RemoteDevice d) {
        for (DeviceEventListener l : listeners) {
            try {
                l.iAmReceived(d);
            }
            catch (Throwable e) {
                handleException(l, e);
            }
        }
    }

    public void propertyWritten(final BACnetObject obj, final PropertyValue pv) {
        for (DeviceEventListener l : listeners) {
            try {
                l.propertyWritten(obj, pv);
            }
            catch (Throwable e) {
                handleException(l, e);
            }
        }
    }

    public void fireIHaveReceived(final RemoteDevice d, final RemoteObject o) {
        for (DeviceEventListener l : listeners) {
            try {
                l.iHaveReceived(d, o);
            }
            catch (Throwable e) {
                handleException(l, e);
            }
        }
    }

    public void fireCovNotification(final UnsignedInteger subscriberProcessIdentifier,
            final RemoteDevice initiatingDevice, final ObjectIdentifier monitoredObjectIdentifier,
            final UnsignedInteger timeRemaining, final SequenceOf<PropertyValue> listOfValues) {
        for (DeviceEventListener l : listeners) {
            try {
                l.covNotificationReceived(subscriberProcessIdentifier, initiatingDevice, monitoredObjectIdentifier,
                        timeRemaining, listOfValues);
            }
            catch (Throwable e) {
                handleException(l, e);
            }
        }
    }

    public void fireEventNotification(final UnsignedInteger processIdentifier, final RemoteDevice initiatingDevice,
            final ObjectIdentifier eventObjectIdentifier, final TimeStamp timeStamp,
            final UnsignedInteger notificationClass, final UnsignedInteger priority, final EventType eventType,
            final CharacterString messageText, final NotifyType notifyType, final Boolean ackRequired,
            final EventState fromState, final EventState toState, final NotificationParameters eventValues) {
        for (DeviceEventListener l : listeners) {
            try {
                l.eventNotificationReceived(processIdentifier, initiatingDevice, eventObjectIdentifier, timeStamp,
                        notificationClass, priority, eventType, messageText, notifyType, ackRequired, fromState,
                        toState, eventValues);
            }
            catch (Throwable e) {
                handleException(l, e);
            }
        }
    }

    public void fireTextMessage(final RemoteDevice textMessageSourceDevice, final Choice messageClass,
            final MessagePriority messagePriority, final CharacterString message) {
        for (DeviceEventListener l : listeners) {
            try {
                l.textMessageReceived(textMessageSourceDevice, messageClass, messagePriority, message);
            }
            catch (Throwable e) {
                handleException(l, e);
            }
        }
    }

    public void firePrivateTransfer(final UnsignedInteger vendorId, final UnsignedInteger serviceNumber,
            						final Encodable serviceParameters) {
        for (DeviceEventListener l : listeners) {
            try {
                l.privateTransferReceived(vendorId, serviceNumber, serviceParameters);
            }
            catch (Throwable e) {
                handleException(l, e);
            }
        }
    }

    public void reinitializeDevice(final ReinitializedStateOfDevice reinitializedStateOfDevice) {
        for (DeviceEventListener l : listeners) {
            try {
                l.reinitializeDevice(reinitializedStateOfDevice);
            }
            catch (Throwable e) {
                handleException(l, e);
            }
        }
    }

    public void synchronizeTime(final DateTime dateTime, final boolean utc) {
        for (DeviceEventListener l : listeners) {
            try {
                l.synchronizeTime(dateTime, utc);
            }
            catch (Throwable e) {
                handleException(l, e);
            }
        }
    }

    private void handleException(DeviceEventListener l, Throwable e) {
        try {
            l.listenerException(e);
        }
        catch (Throwable e1) {
            // no op
        }
    }
}
