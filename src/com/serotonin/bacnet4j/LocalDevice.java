package com.serotonin.bacnet4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.serotonin.bacnet4j.apdu.Abort;
import com.serotonin.bacnet4j.apdu.AckAPDU;
import com.serotonin.bacnet4j.apdu.ComplexACK;
import com.serotonin.bacnet4j.apdu.Error;
import com.serotonin.bacnet4j.apdu.Reject;
import com.serotonin.bacnet4j.apdu.SimpleACK;
import com.serotonin.bacnet4j.event.DefaultExceptionListener;
import com.serotonin.bacnet4j.event.DeviceEventHandler;
import com.serotonin.bacnet4j.event.ExceptionListener;
import com.serotonin.bacnet4j.exception.AbortAPDUException;
import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetRuntimeException;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.exception.ErrorAPDUException;
import com.serotonin.bacnet4j.exception.NotImplementedException;
import com.serotonin.bacnet4j.exception.RejectAPDUException;
import com.serotonin.bacnet4j.npdu.RequestHandler;
import com.serotonin.bacnet4j.npdu.ip.IpMessageControl;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyMultipleAck;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedEventNotificationRequest;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyMultipleRequest;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.IAmRequest;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedEventNotificationRequest;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedRequestService;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.Destination;
import com.serotonin.bacnet4j.type.constructed.EventTransitionBits;
import com.serotonin.bacnet4j.type.constructed.ObjectTypesSupported;
import com.serotonin.bacnet4j.type.constructed.PropertyReference;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult;
import com.serotonin.bacnet4j.type.constructed.ReadAccessSpecification;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.ServicesSupported;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult.Result;
import com.serotonin.bacnet4j.type.enumerated.DeviceStatus;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.EventType;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.notificationParameters.NotificationParameters;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.Unsigned16;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.PropertyReferences;
import com.serotonin.bacnet4j.util.PropertyValues;
import com.serotonin.util.ObjectUtils;

/**
 * Enhancements:
 * - default character string encoding
 * - BIBBs (B-OWS) (services to implement)
 *   - AE-N-A
 *   - AE-ACK-A
 *   - AE-INFO-A
 *   - AE-ESUM-A
 *   - SCHED-A
 *   - T-VMT-A
 *   - T-ATR-A
 *   - DM-DDB-A,B
 *   - DM-DOB-A,B
 *   - DM-DCC-A
 *   - DM-TS-A
 *   - DM-UTC-A
 *   - DM-RD-A
 *   - DM-BR-A
 *   - NM-CE-A
 * 
 * @author mlohbihler
 */
public class LocalDevice implements RequestHandler {
    public static final int DEFAULT_PORT = 0xBAC0;
    private static final int VENDOR_ID = 236; // Serotonin Software
    private static ExceptionListener exceptionListener = new DefaultExceptionListener();
    
    public static void setExceptionListener(ExceptionListener l) {
        if (l == null)
            l = new DefaultExceptionListener();
        exceptionListener = l;
    }
    
    public static ExceptionListener getExceptionListener() {
        return exceptionListener;
    }
    
    
    private final IpMessageControl messageControl;
    private BACnetObject configuration;
    private final List<BACnetObject> localObjects = new CopyOnWriteArrayList<BACnetObject>();
    private final List<RemoteDevice> remoteDevices = new CopyOnWriteArrayList<RemoteDevice>();
    
    // Misc configuration.
    private int maxReadMultipleReferences = 200;
    
    // Event listeners
    private final DeviceEventHandler eventHandler = new DeviceEventHandler();
    
    public LocalDevice(int deviceId, String broadcastAddress) {
        messageControl = new IpMessageControl();
        messageControl.setPort(DEFAULT_PORT);
        messageControl.setBroadcastAddress(broadcastAddress);
        messageControl.setRequestHandler(this);
        
        try {
            configuration = new BACnetObject(this, new ObjectIdentifier(ObjectType.device, deviceId));
            configuration.setProperty(PropertyIdentifier.maxApduLengthAccepted, new UnsignedInteger(1476));
            configuration.setProperty(PropertyIdentifier.vendorIdentifier, new Unsigned16(VENDOR_ID));
            configuration.setProperty(PropertyIdentifier.vendorName,
                    new CharacterString("Serotonin Software Technologies, Inc."));
            configuration.setProperty(PropertyIdentifier.segmentationSupported, Segmentation.segmentedBoth);
            configuration.setProperty(PropertyIdentifier.objectList, new SequenceOf<ObjectIdentifier>());
            
            // Set up the supported services indicators. Remove lines as services get implemented.
            ServicesSupported servicesSupported = new ServicesSupported();
            servicesSupported.setAll(true);
            servicesSupported.setAcknowledgeAlarm(false);
            servicesSupported.setGetAlarmSummary(false);
            servicesSupported.setGetEnrollmentSummary(false);
            servicesSupported.setAtomicReadFile(false);
            servicesSupported.setAtomicWriteFile(false);
            servicesSupported.setAddListElement(false);
            servicesSupported.setRemoveListElement(false);
            servicesSupported.setReadPropertyConditional(false);
            servicesSupported.setDeviceCommunicationControl(false);
            servicesSupported.setReinitializeDevice(false);
            servicesSupported.setVtOpen(false);
            servicesSupported.setVtClose(false);
            servicesSupported.setVtData(false);
            servicesSupported.setAuthenticate(false);
            servicesSupported.setRequestKey(false);
            servicesSupported.setTimeSynchronization(false);
            servicesSupported.setReadRange(false);
            servicesSupported.setUtcTimeSynchronization(false);
            servicesSupported.setLifeSafetyOperation(false);
            servicesSupported.setSubscribeCovProperty(false);
            servicesSupported.setGetEventInformation(false);
            configuration.setProperty(PropertyIdentifier.protocolServicesSupported, servicesSupported);
            
            // Set up the object types supported.
            ObjectTypesSupported objectTypesSupported = new ObjectTypesSupported();
            objectTypesSupported.setAll(true);
            configuration.setProperty(PropertyIdentifier.protocolObjectTypesSupported, objectTypesSupported);
            
            // Set some other required values to defaults
            configuration.setProperty(PropertyIdentifier.objectName, new CharacterString("BACnet device"));
            configuration.setProperty(PropertyIdentifier.systemStatus, DeviceStatus.operational);
            configuration.setProperty(PropertyIdentifier.modelName, new CharacterString("BACnet4J"));
            configuration.setProperty(PropertyIdentifier.firmwareRevision, new CharacterString("not set"));
            configuration.setProperty(PropertyIdentifier.applicationSoftwareVersion, new CharacterString("1.0.1"));
            configuration.setProperty(PropertyIdentifier.protocolVersion, new UnsignedInteger(1));
            configuration.setProperty(PropertyIdentifier.protocolRevision, new UnsignedInteger(0));
            configuration.setProperty(PropertyIdentifier.databaseRevision, new UnsignedInteger(0));
        }
        catch (BACnetServiceException e) {
            // Should never happen, but wrap in an unchecked just in case.
            throw new BACnetRuntimeException(e);
        }
    }
    
    public void initialize() throws IOException {
        eventHandler.initialize();
        messageControl.initialize();
    }
    
    public void terminate() {
        messageControl.terminate();
        eventHandler.terminate();
    }
    
    public BACnetObject getConfiguration() {
        return configuration;
    }
    
    public DeviceEventHandler getEventHandler() {
        return eventHandler;
    }

    //
    // Controller configuration.
    //
    public void setBroadcastAddress(String broadcastAddress) {
        messageControl.setBroadcastAddress(broadcastAddress);
    }
    
    public String getBroadcastAddress() {
        return messageControl.getBroadcastAddress();
    }
    
    public void setPort(int port) {
        messageControl.setPort(port);
    }
    
    public int getPort() {
        return messageControl.getPort();
    }
    
    public void setTimeout(int timeout) {
        messageControl.setTimeout(timeout);
    }
    
    public int getTimeout() {
        return messageControl.getTimeout();
    }
    
    public void setSegTimeout(int segTimeout) {
        messageControl.setSegTimeout(segTimeout);
    }
    
    public int getSegTimeout() {
        return messageControl.getSegTimeout();
    }
    
    public void setSegWindow(int segWindow) {
        messageControl.setSegWindow(segWindow);
    }
    
    public int getSegWindow() {
        return messageControl.getSegWindow();
    }
    
    public void setRetries(int retries) {
        messageControl.setRetries(retries);
    }
    
    public int getRetries() {
        return messageControl.getRetries();
    }
    
    public int getMaxReadMultipleReferences() {
        return maxReadMultipleReferences;
    }

    public void setMaxReadMultipleReferences(int maxReadMultipleReferences) {
        this.maxReadMultipleReferences = maxReadMultipleReferences;
    }

    
    
    
    //
    ///
    /// Local object management
    ///
    //
    public BACnetObject getObjectRequired(ObjectIdentifier id) throws BACnetServiceException {
        BACnetObject o = getObject(id);
        if (o == null)
            throw new BACnetServiceException(ErrorClass.object, ErrorCode.unknownObject);
        return o;
    }
    
    public List<BACnetObject> getLocalObjects() {
        return localObjects;
    }
    
    public BACnetObject getObject(ObjectIdentifier id) {
        if (id.getObjectType().intValue() == ObjectType.device.intValue()) {
            // Check if we need to look into the local device.
            if (id.getInstanceNumber() == 0x3FFFFF || id.getInstanceNumber() == configuration.getInstanceId())
                return configuration;
        }
        
        for (BACnetObject obj : localObjects) {
            if (obj.getId().equals(id))
                return obj;
        }
        return null;
    }
    
    public BACnetObject getObject(String name) {
        // Check if we need to look into the local device.
        if (name.equals(configuration.getObjectName()))
            return configuration;
        
        for (BACnetObject obj : localObjects) {
            if (name.equals(obj.getObjectName()))
                return obj;
        }
        return null;
    }
    
    public void addObject(BACnetObject obj) throws BACnetServiceException {
        if (getObject(obj.getId()) != null)
            throw new BACnetServiceException(ErrorClass.object, ErrorCode.objectIdentifierAlreadyExists);
        obj.validate();
        localObjects.add(obj);
        
        // Create a reference in the device's object list for the new object.
        getObjectList().add(obj.getId());
    }
    
    public ObjectIdentifier getNextInstanceObjectIdentifier(ObjectType objectType) {
        // Make a list of existing ids.
        List<Integer> ids = new ArrayList<Integer>();
        int type = objectType.intValue();
        ObjectIdentifier id;
        for (BACnetObject obj : localObjects) {
            id = obj.getId();
            if (id.getObjectType().intValue() == type)
                ids.add(id.getInstanceNumber());
        }
        
        // Sort the list.
        Collections.sort(ids);
        
        // Find the first hole in the list.
        int i=0;
        for (; i<ids.size(); i++) {
            if (ids.get(i) != i)
                break;
        }
        return new ObjectIdentifier(objectType, i);
    }
    
    public void removeObject(ObjectIdentifier id) throws BACnetServiceException {
        BACnetObject obj = getObject(id);
        if (obj != null)
            localObjects.remove(obj);
        else
            throw new BACnetServiceException(ErrorClass.object, ErrorCode.unknownObject);
        
        // Remove the reference in the device's object list for this id.
        getObjectList().add(id);
    }
    
    @SuppressWarnings("unchecked")
    private SequenceOf<ObjectIdentifier> getObjectList() {
        try {
            return (SequenceOf<ObjectIdentifier>)configuration.getProperty(PropertyIdentifier.objectList);
        }
        catch (BACnetServiceException e) {
            // Should never happen, so just wrap in a RuntimeException
            throw new RuntimeException(e);
        }
    }
    
    //
    ///
    /// Message sending
    ///
    //
    public AcknowledgementService send(RemoteDevice d, ConfirmedRequestService serviceRequest) throws BACnetException {
        return send(d.getAddress(), d.getNetwork(), d.getMaxAPDULengthAccepted(), d.getSegmentationSupported(),
                serviceRequest);
    }
    
    public AcknowledgementService send(Address address, Network network, int maxAPDULengthAccepted,
            Segmentation segmentationSupported, ConfirmedRequestService serviceRequest) throws BACnetException {
        try {
            return send(new InetSocketAddress(
                    InetAddress.getByAddress(address.getMacAddress().getBytes()),
                    address.getNetworkNumber().intValue()),
                    network, maxAPDULengthAccepted, segmentationSupported, serviceRequest);
        }
        catch (UnknownHostException e) {
            throw new BACnetException(e);
        }
    }
    
    public AcknowledgementService send(InetSocketAddress addr, Network network, int maxAPDULengthAccepted, 
            Segmentation segmentationSupported, ConfirmedRequestService serviceRequest) throws BACnetException {
        AckAPDU apdu = messageControl.send(addr, network, maxAPDULengthAccepted, segmentationSupported, serviceRequest);
            
        if (apdu instanceof SimpleACK)
            return null;
            
        if (apdu instanceof ComplexACK)
            return ((ComplexACK)apdu).getService();
        
        if (apdu instanceof Error)
            throw new ErrorAPDUException((Error)apdu);
        
        if (apdu instanceof Reject)
            throw new RejectAPDUException((Reject)apdu);
        
        if (apdu instanceof Abort)
            throw new AbortAPDUException((Abort)apdu);
        
        throw new BACnetException("Unexpected APDU: "+ apdu);
    }
    
    public void sendUnconfirmed(Address address, Network network, UnconfirmedRequestService serviceRequest)
            throws BACnetException {
        try {
            sendUnconfirmed(new InetSocketAddress(
                    InetAddress.getByAddress(address.getMacAddress().getBytes()), 
                    address.getNetworkNumber().intValue()), network, serviceRequest);
        }
        catch (UnknownHostException e) {
            throw new BACnetException(e);
        }
    }
    
    public void sendUnconfirmed(InetSocketAddress addr, Network network, UnconfirmedRequestService serviceRequest)
            throws BACnetException {
        messageControl.sendUnconfirmed(addr, network, serviceRequest);
    }
    
    public void sendBroadcast(UnconfirmedRequestService serviceRequest) throws BACnetException {
        messageControl.sendBroadcast(new Network(0xffff, new byte[0]), serviceRequest);
    }
    
    public void sendBroadcast(int port, UnconfirmedRequestService serviceRequest) throws BACnetException {
        messageControl.sendBroadcast(port, new Network(0xffff, new byte[0]), serviceRequest);
    }
    
    public void sendBroadcast(Network network, UnconfirmedRequestService serviceRequest) throws BACnetException {
        messageControl.sendBroadcast(network, serviceRequest);
    }
    
    public void sendBroadcast(int port, Network network, UnconfirmedRequestService serviceRequest)
            throws BACnetException {
        messageControl.sendBroadcast(port, network, serviceRequest);
    }
    
    
    //
    ///
    /// Remote device management
    ///
    //
    public RemoteDevice getRemoteDevice(int instanceId, Address address) throws BACnetException {
        RemoteDevice d = getRemoteDeviceImpl(instanceId, address);
        if (d == null)
            throw new BACnetException("Unknown device: instance id="+ instanceId +", address="+ address);
        return d;
    }
    
    public RemoteDevice getRemoteDeviceCreate(int instanceId, Address address, Network network) {
        RemoteDevice d = getRemoteDeviceImpl(instanceId, address);
        if (d == null) {
            d = new RemoteDevice(instanceId, address, network);
            remoteDevices.add(d);
        }
        return d;
    }
    
    public void addRemoteDevice(RemoteDevice d) {
        remoteDevices.add(d);
    }
    
    private RemoteDevice getRemoteDeviceImpl(int instanceId, Address address) {
        for (RemoteDevice d : remoteDevices) {
            if (d.getInstanceNumber() == instanceId && d.getAddress().equals(address))
                return d;
        }
        return null;
    }
    
    public List<RemoteDevice> getRemoteDevices() {
        return remoteDevices;
    }
    
    public RemoteDevice getRemoteDevice(Address peer) {
        for (RemoteDevice d : remoteDevices) {
            if (d.getAddress().equals(peer))
                return d;
        }
        return null;
    }
    
    public RemoteDevice getRemoteDevice(int instanceNumber) {
        for (RemoteDevice d : remoteDevices) {
            if (d.getInstanceNumber() == instanceNumber)
                return d;
        }
        return null;
    }
    
    public RemoteDevice getRemoteDeviceByUserData(Object userData) {
        for (RemoteDevice d : remoteDevices) {
            if (ObjectUtils.isEqual(userData, d.getUserData()))
                return d;
        }
        return null;
    }
    
    
    
    //
    ///
    /// Intrinsic events
    ///
    //
    @SuppressWarnings("unchecked")
    public List<BACnetException> sendIntrinsicEvent(ObjectIdentifier eventObjectIdentifier, TimeStamp timeStamp,
            int notificationClassId, EventType eventType, CharacterString messageText, NotifyType notifyType, 
            EventState fromState, EventState toState, NotificationParameters eventValues) throws BACnetException {
        
        // Try to find a notification class with the given id in the local objects.
        BACnetObject nc = null;
        for (BACnetObject obj : localObjects) {
            if (ObjectType.notificationClass.equals(obj.getId().getObjectType())) {
                try {
                    UnsignedInteger ncId = (UnsignedInteger)obj.getProperty(PropertyIdentifier.notificationClass);
                    if (ncId != null && ncId.intValue() == notificationClassId) {
                        nc = obj;
                        break;
                    }
                }
                catch (BACnetServiceException e) {
                    // Should never happen, so wrap in a RTE
                    throw new RuntimeException(e);
                }
            }
        }
        
        if (nc == null)
            throw new BACnetException("Notification class object not found for given id: "+ notificationClassId);
        
        // Get the required properties from the notification class object.
        SequenceOf<Destination> recipientList = null;
        Boolean ackRequired = null;
        UnsignedInteger priority = null;
        try {
            recipientList = (SequenceOf<Destination>)nc.getPropertyRequired(PropertyIdentifier.recipientList);
            ackRequired = new Boolean(
                    ((EventTransitionBits)nc.getPropertyRequired(PropertyIdentifier.ackRequired)).contains(toState));
            
            // Determine which priority value to use based upon the toState.
            SequenceOf<UnsignedInteger> priorities =
                (SequenceOf<UnsignedInteger>)nc.getPropertyRequired(PropertyIdentifier.priority);
            if (toState.equals(EventState.normal))
                priority = priorities.get(3);
            else if (toState.equals(EventState.fault))
                priority = priorities.get(2);
            else // everything else is offnormal
                priority = priorities.get(1);
        }
        catch (BACnetServiceException e) {
            // Should never happen, so wrap in a RTE
            throw new RuntimeException(e);
        }
        
        // Send the message to the destinations that are interested in it, while recording any exceptions in the result
        // list
        List<BACnetException> sendExceptions = new ArrayList<BACnetException>();
        for (Destination destination : recipientList) {
            if (destination.isSuitableForEvent(timeStamp, toState)) {
                if (destination.getIssueConfirmedNotifications().booleanValue()) {
                    RemoteDevice remoteDevice = null;
                    if (destination.getRecipient().isAddress())
                        remoteDevice = getRemoteDevice(destination.getRecipient().getAddress());
                    else
                        remoteDevice = getRemoteDevice(
                                destination.getRecipient().getObjectIdentifier().getInstanceNumber());
                    
                    if (remoteDevice != null) {
                        ConfirmedEventNotificationRequest req = new ConfirmedEventNotificationRequest(
                                destination.getProcessIdentifier(), configuration.getId(), eventObjectIdentifier, 
                                timeStamp, new UnsignedInteger(notificationClassId), priority, eventType, messageText, 
                                notifyType, ackRequired, fromState, toState, eventValues);
                        
                        try {
                            send(remoteDevice, req);
                        }
                        catch (BACnetException e) {
                            sendExceptions.add(e);
                        }
                    }
                }
                else {
                    Address address = null;
                    if (destination.getRecipient().isAddress())
                        address = destination.getRecipient().getAddress();
                    else {
                        RemoteDevice remoteDevice = getRemoteDevice(
                                destination.getRecipient().getObjectIdentifier().getInstanceNumber());
                        if (remoteDevice != null)
                            address = remoteDevice.getAddress();
                    }
                    
                    if (address != null) {
                        UnconfirmedEventNotificationRequest req = new UnconfirmedEventNotificationRequest(
                                destination.getProcessIdentifier(), configuration.getId(), eventObjectIdentifier,
                                timeStamp, new UnsignedInteger(notificationClassId), priority, eventType, messageText, 
                                notifyType, ackRequired, fromState, toState, eventValues);
                        try {
                            sendUnconfirmed(address, null, req);
                        }
                        catch (BACnetException e) {
                            sendExceptions.add(e);
                        }
                    }
                }
            }
        }
            
        return sendExceptions;
    }
    
    


    //
    ///
    /// Request Handler
    ///
    //
    public AcknowledgementService handleConfirmedRequest(Address from, Network network, byte invokeId, 
            ConfirmedRequestService serviceRequest) throws BACnetException {
        try {
            return serviceRequest.handle(this, from, network);
        }
        catch (NotImplementedException e) {
            System.out.println("Unsupported confirmed request: invokeId="+ invokeId +", from="+ from 
                    +", request="+ serviceRequest.getClass().getName());
            throw new BACnetErrorException(ErrorClass.services, ErrorCode.serviceRequestDenied);
        }
        catch (BACnetErrorException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BACnetErrorException(ErrorClass.device, ErrorCode.operationalProblem);
        }
    }

    public void handleUnconfirmedRequest(Address from, Network network, UnconfirmedRequestService serviceRequest) {
        try {
            serviceRequest.handle(this, from, network);
        }
        catch (BACnetException e) {
            getExceptionListener().receivedException(e);
        }
    }
    
    
    
    //
    ///
    /// Convenience methods
    ///
    //
    public Address getAddress() {
        try {
            return new Address(new UnsignedInteger(messageControl.getPort()),
                    new OctetString(InetAddress.getLocalHost().getAddress()));
        }
        catch (UnknownHostException e) {
            // Should never happen, so just wrap in a RuntimeException
            throw new RuntimeException(e);
        }
    }
    
    public IAmRequest getIAm() throws BACnetException {
        try {
            return new IAmRequest(configuration.getId(), 
                    (UnsignedInteger)configuration.getProperty(PropertyIdentifier.maxApduLengthAccepted),
                    (Segmentation)configuration.getProperty(PropertyIdentifier.segmentationSupported),
                    (Unsigned16)configuration.getProperty(PropertyIdentifier.vendorIdentifier));
        }
        catch (BACnetServiceException e) {
            // Should never happen, so just wrap in a RuntimeException
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Sends a ReadProperty-Request and ignores Error responses where the class is Property and the code is 
     * unknownProperty. Returns null in this case.
     */
    public Encodable sendReadPropertyAllowNull(RemoteDevice d, ObjectIdentifier oid, PropertyIdentifier pid)
            throws BACnetException{
        try {
            ReadPropertyAck ack = (ReadPropertyAck)send(d, new ReadPropertyRequest(oid, pid));
            return ack.getValue();
        }
        catch (ErrorAPDUException e) {
            if (e.getBACnetError().equals(ErrorClass.property, ErrorCode.unknownProperty))
                return null;
            throw e;
        }
    }
    
    public void getExtendedDeviceInformation(RemoteDevice d) throws BACnetException {
        // Get the device's supported services
        ReadPropertyAck supportedServicesAck = (ReadPropertyAck)send(d, new ReadPropertyRequest(
                d.getObjectIdentifier(), PropertyIdentifier.protocolServicesSupported));
        d.setServicesSupported((ServicesSupported)supportedServicesAck.getValue());
        
        // Uses the readProperties method here because this list will probably be extended.
        PropertyReferences properties = new PropertyReferences();
        properties.add(d.getObjectIdentifier(), PropertyIdentifier.objectName);
        
        PropertyValues values = readProperties(d, properties);
        
        d.setName(values.getString(d.getObjectIdentifier(), PropertyIdentifier.objectName));
    }

    public PropertyValues readProperties(RemoteDevice d, PropertyReferences refs) throws BACnetException {
        Map<ObjectIdentifier, List<PropertyReference>> properties;
        PropertyValues propertyValues = new PropertyValues();
        
        if (refs.size() > 1 && d.getServicesSupported() != null && d.getServicesSupported().isReadPropertyMultiple()) {
            // If the device supports read property multiple, send them all at once, or at least in partitions.
            List<PropertyReferences> partitions = refs.getPropertiesPartitioned(maxReadMultipleReferences);
            for (PropertyReferences partition : partitions) {
                properties = partition.getProperties();
                List<ReadAccessSpecification> specs = new ArrayList<ReadAccessSpecification>();
                for (ObjectIdentifier oid : properties.keySet())
                    specs.add(new ReadAccessSpecification(oid, new SequenceOf<PropertyReference>(properties.get(oid))));
                
                ReadPropertyMultipleRequest request = new ReadPropertyMultipleRequest(
                        new SequenceOf<ReadAccessSpecification>(specs));
                ReadPropertyMultipleAck ack = (ReadPropertyMultipleAck)send(d, request);
                
                List<ReadAccessResult> results = ack.getListOfReadAccessResults().getValues();
                ObjectIdentifier oid;
                for (ReadAccessResult objectResult : results) {
                    oid = objectResult.getObjectIdentifier();
                    for (Result result : objectResult.getListOfResults().getValues())
                        propertyValues.add(oid, result.getPropertyIdentifier(), result.getPropertyArrayIndex(), 
                                result.getReadResult().getDatum());
                }
            }
        }
        else {
            // If it doesn't support read property multiple, send them one at a time.
            List<PropertyReference> refList;
            ReadPropertyRequest request;
            ReadPropertyAck ack;
            properties = refs.getProperties();
            for (ObjectIdentifier oid : properties.keySet()) {
                refList = properties.get(oid);
                for (PropertyReference ref : refList) {
                    request = new ReadPropertyRequest(oid, ref.getPropertyIdentifier(), ref.getPropertyArrayIndex());
                    try {
                        ack = (ReadPropertyAck)send(d, request);
                        propertyValues.add(oid, ack.getPropertyIdentifier(), ack.getPropertyArrayIndex(),
                                ack.getValue());
                    }
                    catch (ErrorAPDUException e) {
                        propertyValues.add(oid, ref.getPropertyIdentifier(), ref.getPropertyArrayIndex(),
                                e.getBACnetError());
                    }
                }
            }
        }
        
        return propertyValues;
    }
    
    public PropertyValues readPresentValues(RemoteDevice d) throws BACnetException {
        return readPresentValues(d, d.getObjects());
    }
    
    public PropertyValues readPresentValues(RemoteDevice d, List<RemoteObject> objs) throws BACnetException {
        List<ObjectIdentifier> oids = new ArrayList<ObjectIdentifier>(objs.size());
        for (RemoteObject o : d.getObjects())
            oids.add(o.getObjectIdentifier());
        return readOidPresentValues(d, oids);
    }
    
    public PropertyValues readOidPresentValues(RemoteDevice d, List<ObjectIdentifier> oids) throws BACnetException {
        if (oids.size() == 0)
            return new PropertyValues();
        
        PropertyReferences refs = new PropertyReferences();
        for (ObjectIdentifier oid : oids)
            refs.add(oid, PropertyIdentifier.presentValue);
        
        return readProperties(d, refs);
    }
    
    public void setProperty(RemoteDevice d, ObjectIdentifier oid, PropertyIdentifier pid, Encodable value)
            throws BACnetException {
        send(d, new WritePropertyRequest(oid, pid, null, value, null));
    }
    
    public void setPresentValue(RemoteDevice d, ObjectIdentifier oid, Encodable value ) throws BACnetException {
        setProperty(d, oid, PropertyIdentifier.presentValue, value);
    }
    
    
}
