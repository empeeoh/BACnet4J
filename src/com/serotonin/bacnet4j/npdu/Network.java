package com.serotonin.bacnet4j.npdu;

import com.serotonin.bacnet4j.apdu.APDU;
import com.serotonin.bacnet4j.enums.MaxApduLength;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import org.free.bacnet4j.util.ByteQueue;

abstract public class Network {
    private final int localNetworkNumber;
    private Transport transport;

    public Network() {
        this(0);
    }

    public Network(int localNetworkNumber) {
        this.localNetworkNumber = localNetworkNumber;
    }

    public int getLocalNetworkNumber() {
        return localNetworkNumber;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public Transport getTransport() {
        return transport;
    }

    abstract public NetworkIdentifier getNetworkIdentifier();

    abstract public MaxApduLength getMaxApduLength();

    public void initialize(final Transport transport) throws Exception {
        this.transport = transport;
    }

    abstract public void terminate();

    abstract public Address getLocalBroadcastAddress();

    abstract public Address[] getAllLocalAddresses();

    abstract public void sendAPDU(final Address recipient, 
    							  final OctetString linkService, 
    							  final APDU apdu, 
    							  final boolean broadcast) throws BACnetException;

    abstract public void checkSendThread();

    protected void writeNpci(ByteQueue queue, Address recipient, OctetString link, APDU apdu) {
        NPCI npci;
        if (recipient.isGlobal())
            npci = new NPCI((Address) null);
        else if (isLocal(recipient)) {
            if (link != null)
                throw new RuntimeException("Invalid arguments: link service address provided for a local recipient");
            npci = new NPCI(null, null, apdu.expectsReply());
        }
        else {
            if (link == null)
                throw new RuntimeException(
                        "Invalid arguments: link service address not provided for a remote recipient");
            npci = new NPCI(recipient, null, apdu.expectsReply());
        }
        npci.write(queue);
    }

    protected boolean isLocal(Address recipient) {
        int nn = recipient.getNetworkNumber().intValue();
        return nn == Address.LOCAL_NETWORK || nn == localNetworkNumber;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + localNetworkNumber;
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
        Network other = (Network) obj;
        if (localNetworkNumber != other.localNetworkNumber)
            return false;
        return true;
    }
}
