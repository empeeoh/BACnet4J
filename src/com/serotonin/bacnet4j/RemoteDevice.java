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
package com.serotonin.bacnet4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.ServicesSupported;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class RemoteDevice implements Serializable {
    private static final long serialVersionUID = 6338537708566242078L;
    private final int instanceNumber;
    private final Address address;
    private final OctetString linkService;
    
    private int maxAPDULengthAccepted;
    private Segmentation segmentationSupported;
    private int vendorId;
    private String vendorName;
    private String name;
    private String modelName;
    private String firmwareRevision; 
    private String applicationSoftwareVersion;
    private UnsignedInteger protocolVersion;
    private UnsignedInteger protocolRevision;
    private ServicesSupported servicesSupported;
    private final Map<ObjectIdentifier, RemoteObject> objects = new HashMap<ObjectIdentifier, RemoteObject>();
    private Object userData;
    private int maxReadMultipleReferences = -1;

    public RemoteDevice(int instanceNumber, Address address, OctetString linkService) {
        this.instanceNumber = instanceNumber;
        this.address = address;
        this.linkService = linkService;
    }

    public ObjectIdentifier getObjectIdentifier() {
        return new ObjectIdentifier(ObjectType.device, instanceNumber);
    }

    @Override
    public String toString() {
        return "RemoteDevice(instanceNumber=" + instanceNumber + ", address=" + address + ", linkServiceAddress="
                + linkService + ")";
    }

    public String toExtendedString() {
        return "RemoteDevice(instanceNumber=" + instanceNumber + 
        		", address=" + address + ", linkServiceAddress=" + linkService +
        		", maxAPDULengthAccepted=" + maxAPDULengthAccepted + 
        		", segmentationSupported=" + segmentationSupported + 
        		", vendorId=" + vendorId + ", vendorName=" + vendorName + 
        		", name=" + name + ", firmwareRevision=" + firmwareRevision +
        		", applicationSoftwareVersion=" + applicationSoftwareVersion + 
        		", servicesSupported=" + servicesSupported + ", objects=" + objects + ")";
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

    public void clearObjects() {
        objects.clear();
    }

    public Address getAddress() {
        return address;
    }

    public OctetString getLinkService() {
        return linkService;
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

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
    
    public String getFirmwareRevision() {
        return firmwareRevision;
    }

    public void setFirmwareRevision(String firmwareRevision) {
        this.firmwareRevision = firmwareRevision;
    }

    public String getApplicationSoftwareVersion() {
        return applicationSoftwareVersion;
    }

    public void setApplicationSoftwareVersion(String applicationSoftwareVersion) {
        this.applicationSoftwareVersion = applicationSoftwareVersion;
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

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public UnsignedInteger getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(UnsignedInteger protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public UnsignedInteger getProtocolRevision() {
        return protocolRevision;
    }

    public void setProtocolRevision(UnsignedInteger protocolRevision) {
        this.protocolRevision = protocolRevision;
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

    public int getMaxReadMultipleReferences() {
        if (maxReadMultipleReferences == -1)
            maxReadMultipleReferences = segmentationSupported.hasTransmitSegmentation() ? 200 : 20;
        return maxReadMultipleReferences;
    }

    public void reduceMaxReadMultipleReferences() {
        if (maxReadMultipleReferences > 1)
            maxReadMultipleReferences = (int) (maxReadMultipleReferences * 0.75);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + instanceNumber;
        result = prime * result + ((linkService == null) ? 0 : linkService.hashCode());
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
        if (linkService == null) {
            if (other.linkService != null)
                return false;
        }
        else if (!linkService.equals(other.linkService))
            return false;
        return true;
    }
}
