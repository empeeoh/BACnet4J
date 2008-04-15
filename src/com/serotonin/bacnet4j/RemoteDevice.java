package com.serotonin.bacnet4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.ServicesSupported;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

public class RemoteDevice {
    private final int instanceNumber;
    private final Address address;
    private int maxAPDULengthAccepted;
    private Segmentation segmentationSupported;
    private int vendorId;
    private String name;
    private ServicesSupported servicesSupported;
    private final Map<ObjectIdentifier, RemoteObject> objects = new HashMap<ObjectIdentifier, RemoteObject>();
    private Object userData;
    
    public RemoteDevice(int instanceNumber, Address address) {
        this.instanceNumber = instanceNumber;
        this.address = address;
    }
    
    public ObjectIdentifier getObjectIdentifier() {
        return new ObjectIdentifier(ObjectType.device, instanceNumber);
    }
    
    @Override
    public String toString() {
        return "RemoteDevice(instanceNumber="+ instanceNumber +", address="+ address +")";
    }

    public String toExtendedString() {
        return "RemoteDevice(instanceNumber="+ instanceNumber
                +", address="+ address
                +", maxAPDULengthAccepted="+ maxAPDULengthAccepted
                +", segmentationSupported="+ segmentationSupported
                +", vendorId="+ vendorId
                +", name="+ name
                +", servicesSupported="+ servicesSupported
                +", objects="+ objects +")";
    }

    public void setObject(RemoteObject o) {
        objects.put(o.getObjectIdentifier(), o);
    }
    
    public RemoteObject getObject(ObjectIdentifier oid) {
        return objects.get(oid);
    }
    
    public List<RemoteObject> getObjects() {
        return new ArrayList<RemoteObject>(objects.values());
    }
    
    public Address getAddress() {
        return address;
    }

    public int getMaxAPDULengthAccepted() {
        return maxAPDULengthAccepted;
    }

    public void setMaxAPDULengthAccepted(int maxAPDULengthAccepted) {
        this.maxAPDULengthAccepted = maxAPDULengthAccepted;
    }

    public Segmentation getSegmentationSupported() {
        return segmentationSupported;
    }

    public void setSegmentationSupported(Segmentation segmentationSupported) {
        this.segmentationSupported = segmentationSupported;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getInstanceNumber() {
        return instanceNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServicesSupported getServicesSupported() {
        return servicesSupported;
    }

    public void setServicesSupported(ServicesSupported servicesSupported) {
        this.servicesSupported = servicesSupported;
    }

    public Object getUserData() {
        return userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((address == null) ? 0 : address.hashCode());
        result = PRIME * result + instanceNumber;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final RemoteDevice other = (RemoteDevice) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        }
        else if (!address.equals(other.address))
            return false;
        if (instanceNumber != other.instanceNumber)
            return false;
        return true;
    }
}
