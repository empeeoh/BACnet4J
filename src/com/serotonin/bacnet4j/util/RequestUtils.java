package com.serotonin.bacnet4j.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.RemoteObject;
import com.serotonin.bacnet4j.exception.AbortAPDUException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetTimeoutException;
import com.serotonin.bacnet4j.exception.ErrorAPDUException;
import com.serotonin.bacnet4j.obj.ObjectProperties;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyMultipleAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyMultipleRequest;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.constructed.ObjectPropertyReference;
import com.serotonin.bacnet4j.type.constructed.PropertyReference;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult.Result;
import com.serotonin.bacnet4j.type.constructed.ReadAccessSpecification;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.ServicesSupported;
import com.serotonin.bacnet4j.type.enumerated.AbortReason;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class RequestUtils {
    private static final Logger LOG = Logger.getLogger(RequestUtils.class.toString());

    /**
     * Does not work with aggregate PIDs like "all".
     */
    public static Encodable getProperty(LocalDevice localDevice, RemoteDevice d, PropertyIdentifier pid)
            throws BACnetException {
        return getProperty(localDevice, d, d.getObjectIdentifier(), pid);
    }

    /**
     * Does not work with aggregate PIDs like "all".
     */
    public static Encodable getProperty(LocalDevice localDevice, RemoteDevice d, ObjectIdentifier oid,
            PropertyIdentifier pid) throws BACnetException {
        Map<PropertyIdentifier, Encodable> map = getProperties(localDevice, d, oid, null, pid);
        return map.get(pid);
    }

    public static Map<PropertyIdentifier, Encodable> getProperties(LocalDevice localDevice, RemoteDevice d,
            RequestListener callback, PropertyIdentifier... pids) throws BACnetException {
        return getProperties(localDevice, d, d.getObjectIdentifier(), callback, pids);
    }

    public static Map<PropertyIdentifier, Encodable> getProperties(LocalDevice localDevice, RemoteDevice d,
            ObjectIdentifier obj, RequestListener callback, PropertyIdentifier... pids) throws BACnetException {
        List<ObjectPropertyReference> refs = new ArrayList<ObjectPropertyReference>(pids.length);
        for (int i = 0; i < pids.length; i++)
            refs.add(new ObjectPropertyReference(obj, pids[i]));
        return getProperties(localDevice, d, callback, refs);
    }

    private static Map<PropertyIdentifier, Encodable> getProperties(LocalDevice localDevice, RemoteDevice d,
            RequestListener callback, List<ObjectPropertyReference> refs) throws BACnetException {
        List<Pair<ObjectPropertyReference, Encodable>> values = readProperties(localDevice, d, refs, callback);

        Map<PropertyIdentifier, Encodable> map = new HashMap<PropertyIdentifier, Encodable>(values.size());
        for (Pair<ObjectPropertyReference, Encodable> pair : values)
            map.put(pair.getLeft().getPropertyIdentifier(), pair.getRight());
        return map;
    }

    public static Encodable sendReadPropertyAllowNull(LocalDevice localDevice, RemoteDevice d, ObjectIdentifier oid,
            PropertyIdentifier pid) throws BACnetException {
        return sendReadPropertyAllowNull(localDevice, d, oid, pid, null, null);
    }

    /**
     * Sends a ReadProperty-Request and ignores Error responses where the class is Property and the code is
     * unknownProperty. Returns null in this case.
     */
    public static Encodable sendReadPropertyAllowNull(LocalDevice localDevice, RemoteDevice d, ObjectIdentifier oid,
            PropertyIdentifier pid, UnsignedInteger propertyArrayIndex, RequestListener callback)
            throws BACnetException {
        try {
            ReadPropertyAck ack = (ReadPropertyAck) localDevice.send(d, new ReadPropertyRequest(oid, pid,
                    propertyArrayIndex));
            if (callback != null)
                callback.requestProgress(1, oid, pid, propertyArrayIndex, ack.getValue());
            return ack.getValue();
        }
        catch (AbortAPDUException e) {
            if (e.getApdu().getAbortReason() == AbortReason.bufferOverflow.intValue()
                    || e.getApdu().getAbortReason() == AbortReason.segmentationNotSupported.intValue()) {
                // The response may be too long to send. If the property is a sequence...
                if (ObjectProperties.getPropertyTypeDefinition(oid.getObjectType(), pid).isSequence()) {
                    LOG.info("Received abort exception on sequence request. Sending chunked reference request instead");

                    // ... then try getting it by sending requests for indices. Find out how many there are.
                    int len = ((UnsignedInteger) sendReadPropertyAllowNull(localDevice, d, oid, pid,
                            new UnsignedInteger(0), null)).intValue();

                    // Create a list of individual property references.
                    PropertyReferences refs = new PropertyReferences();
                    for (int i = 1; i <= len; i++)
                        refs.add(oid, new PropertyReference(pid, new UnsignedInteger(i)));

                    // Send the request. Use the method that automatically partitions the request.
                    PropertyValues pvs = readProperties(localDevice, d, refs, callback);

                    // We know that the original request property was a sequence, so create one to store the result.
                    SequenceOf<Encodable> list = new SequenceOf<Encodable>();
                    for (int i = 1; i <= len; i++)
                        list.add(pvs.getNoErrorCheck(oid, new PropertyReference(pid, new UnsignedInteger(i))));

                    // And there you go.
                    return list;
                }
                throw e;
            }
            throw e;
        }
        catch (ErrorAPDUException e) {
            if (e.getBACnetError().equals(ErrorClass.property, ErrorCode.unknownProperty))
                return null;
            throw e;
        }
    }

    public static void getExtendedDeviceInformation(LocalDevice localDevice, RemoteDevice d) throws BACnetException {
        ObjectIdentifier oid = d.getObjectIdentifier();

        // Get the device's supported services
        if (d.getServicesSupported() == null) {
            ReadPropertyAck supportedServicesAck = (ReadPropertyAck) localDevice.send(d, new ReadPropertyRequest(oid,
                    PropertyIdentifier.protocolServicesSupported));
            d.setServicesSupported((ServicesSupported) supportedServicesAck.getValue());
        }

        // Uses the readProperties method here because this list will probably be extended.
        PropertyReferences properties = new PropertyReferences();
        properties.add(oid, PropertyIdentifier.objectName);
        properties.add(oid, PropertyIdentifier.protocolVersion);
        //        properties.add(oid, PropertyIdentifier.protocolRevision);

        PropertyValues values = readProperties(localDevice, d, properties, null);

        d.setName(values.getString(oid, PropertyIdentifier.objectName));
        d.setProtocolVersion((UnsignedInteger) values.getNullOnError(oid, PropertyIdentifier.protocolVersion));
        //        d.setProtocolRevision((UnsignedInteger) values.getNullOnError(oid, PropertyIdentifier.protocolRevision));
    }

    /**
     * This version of the readProperties method will preserve the order of properties given in the list in the results.
     * 
     * @param d
     *            the device to which to send the request
     * @param oprs
     *            the list of property references to request
     * @return a list of the original property reference objects wrapped with their values
     * @throws BACnetException
     */
    public static List<Pair<ObjectPropertyReference, Encodable>> readProperties(LocalDevice localDevice,
            RemoteDevice d, List<ObjectPropertyReference> oprs, RequestListener callback) throws BACnetException {
        PropertyReferences refs = new PropertyReferences();
        for (ObjectPropertyReference opr : oprs)
            refs.add(opr.getObjectIdentifier(), opr.getPropertyIdentifier());

        PropertyValues pvs = readProperties(localDevice, d, refs, callback);

        // Read the properties in the same order.
        List<Pair<ObjectPropertyReference, Encodable>> results = new ArrayList<Pair<ObjectPropertyReference, Encodable>>();
        for (ObjectPropertyReference opr : oprs)
            results.add(new ImmutablePair<ObjectPropertyReference, Encodable>(opr, pvs.getNoErrorCheck(opr)));

        return results;
    }

    public static PropertyValues readProperties(LocalDevice localDevice, RemoteDevice d, PropertyReferences refs,
            RequestListener callback) throws BACnetException {
        Map<ObjectIdentifier, List<PropertyReference>> properties;
        PropertyValues propertyValues = new PropertyValues();
        RequestListenerUpdater updater = new RequestListenerUpdater(callback, propertyValues, refs.size());

        boolean multipleSupported = d.getServicesSupported() != null
                && d.getServicesSupported().isReadPropertyMultiple();

        boolean forceMultiple = false;
        // Check if a "special" property identifier is contained in the references.
        for (List<PropertyReference> prs : refs.getProperties().values()) {
            for (PropertyReference pr : prs) {
                PropertyIdentifier pi = pr.getPropertyIdentifier();
                if (pi.equals(PropertyIdentifier.all) || pi.equals(PropertyIdentifier.required)
                        || pi.equals(PropertyIdentifier.optional)) {
                    forceMultiple = true;
                    break;
                }
            }

            if (forceMultiple)
                break;
        }

        if (forceMultiple && !multipleSupported)
            throw new BACnetException("Cannot send request. ReadPropertyMultiple is required but not supported.");

        if (forceMultiple || (refs.size() > 1 && multipleSupported)) {
            // Read property multiple can be used. Determine the max references

            int maxRef = d.getMaxReadMultipleReferences();

            // If the device supports read property multiple, send them all at once, or at least in partitions.
            List<PropertyReferences> partitions = refs.getPropertiesPartitioned(maxRef);
            int counter = 0;
            for (PropertyReferences partition : partitions) {
                properties = partition.getProperties();
                List<ReadAccessSpecification> specs = new ArrayList<ReadAccessSpecification>();
                for (ObjectIdentifier oid : properties.keySet())
                    specs.add(new ReadAccessSpecification(oid, new SequenceOf<PropertyReference>(properties.get(oid))));

                ReadPropertyMultipleRequest request = new ReadPropertyMultipleRequest(
                        new SequenceOf<ReadAccessSpecification>(specs));

                ReadPropertyMultipleAck ack;
                try {
                    ack = (ReadPropertyMultipleAck) localDevice.send(d, request);
                    counter++;

                    List<ReadAccessResult> results = ack.getListOfReadAccessResults().getValues();
                    ObjectIdentifier oid;
                    for (ReadAccessResult objectResult : results) {
                        oid = objectResult.getObjectIdentifier();
                        for (Result result : objectResult.getListOfResults().getValues()) {
                            updater.increment(oid, result.getPropertyIdentifier(), result.getPropertyArrayIndex(),
                                    result.getReadResult().getDatum());
                            if (updater.cancelled())
                                break;
                        }

                        if (updater.cancelled())
                            break;
                    }
                }
                catch (AbortAPDUException e) {
                    LOG.warning("Chunked request failed.");
                    if (e.getApdu().getAbortReason() == AbortReason.bufferOverflow.intValue()
                            || e.getApdu().getAbortReason() == AbortReason.segmentationNotSupported.intValue()) {
                        if (counter > 0)
                            sendOneAtATime(localDevice, d, partition, updater);
                        else {
                            // Failed on the first partition. Send all one at a time, reduce the device's max
                            // references, and quit.
                            sendOneAtATime(localDevice, d, refs, updater);
                            d.reduceMaxReadMultipleReferences();
                            break;
                        }
                    }
                    else
                        throw new BACnetException("Completed " + counter + " requests. Excepted on: " + request, e);
                }
                catch (BACnetTimeoutException e) {
                    BACnetError error = new BACnetError(ErrorClass.communication, ErrorCode.timeout);
                    for (ObjectIdentifier oid : properties.keySet()) {
                        for (PropertyReference ref : properties.get(oid))
                            updater.increment(oid, ref.getPropertyIdentifier(), ref.getPropertyArrayIndex(), error);
                    }
                }
                catch (BACnetException e) {
                    throw new BACnetException("Completed " + counter + " requests. Excepted on: " + request, e);
                }

                if (updater.cancelled())
                    break;
            }
        }
        else
            // If it doesn't support read property multiple, send them one at a time.
            sendOneAtATime(localDevice, d, refs, updater);

        return propertyValues;
    }

    private static void sendOneAtATime(LocalDevice localDevice, RemoteDevice d, PropertyReferences refs,
            RequestListenerUpdater updater) throws BACnetException {
        LOG.info("Making property reference requests one at a time");
        List<PropertyReference> refList;
        ReadPropertyRequest request;
        ReadPropertyAck ack;
        Map<ObjectIdentifier, List<PropertyReference>> properties = refs.getProperties();
        for (ObjectIdentifier oid : properties.keySet()) {
            refList = properties.get(oid);
            for (PropertyReference ref : refList) {
                request = new ReadPropertyRequest(oid, ref.getPropertyIdentifier(), ref.getPropertyArrayIndex());
                try {
                    ack = (ReadPropertyAck) localDevice.send(d, request);
                    updater.increment(oid, ack.getPropertyIdentifier(), ack.getPropertyArrayIndex(), ack.getValue());
                }
                catch (BACnetTimeoutException e) {
                    updater.increment(oid, ref.getPropertyIdentifier(), ref.getPropertyArrayIndex(), new BACnetError(
                            ErrorClass.communication, ErrorCode.timeout));
                }
                catch (ErrorAPDUException e) {
                    updater.increment(oid, ref.getPropertyIdentifier(), ref.getPropertyArrayIndex(), e.getBACnetError());
                }

                if (updater.cancelled())
                    break;
            }

            if (updater.cancelled())
                break;
        }
    }

    public static PropertyValues readPresentValues(LocalDevice localDevice, RemoteDevice d, RequestListener callback)
            throws BACnetException {
        return readPresentValues(localDevice, d, d.getObjects(), callback);
    }

    public static PropertyValues readPresentValues(LocalDevice localDevice, RemoteDevice d, List<RemoteObject> objs,
            RequestListener callback) throws BACnetException {
        List<ObjectIdentifier> oids = new ArrayList<ObjectIdentifier>(objs.size());
        for (RemoteObject o : d.getObjects())
            oids.add(o.getObjectIdentifier());
        return readOidPresentValues(localDevice, d, oids, callback);
    }

    public static PropertyValues readOidPresentValues(LocalDevice localDevice, RemoteDevice d,
            List<ObjectIdentifier> oids, RequestListener callback) throws BACnetException {
        if (oids.size() == 0)
            return new PropertyValues();

        PropertyReferences refs = new PropertyReferences();
        for (ObjectIdentifier oid : oids)
            refs.add(oid, PropertyIdentifier.presentValue);

        return readProperties(localDevice, d, refs, callback);
    }

    public static void setProperty(LocalDevice localDevice, RemoteDevice d, ObjectIdentifier oid,
            PropertyIdentifier pid, Encodable value) throws BACnetException {
        localDevice.send(d, new WritePropertyRequest(oid, pid, null, value, null));
    }

    public static void setPresentValue(LocalDevice localDevice, RemoteDevice d, ObjectIdentifier oid, Encodable value)
            throws BACnetException {
        setProperty(localDevice, d, oid, PropertyIdentifier.presentValue, value);
    }
}
