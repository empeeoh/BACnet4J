package com.serotonin.bacnet4j.npdu.mstp;

import com.serotonin.bacnet4j.apdu.APDU;
import com.serotonin.bacnet4j.enums.MaxApduLength;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.IncomingRequestParser;
import com.serotonin.bacnet4j.npdu.MessageValidationAssertionException;
import com.serotonin.bacnet4j.npdu.Network;
import com.serotonin.bacnet4j.npdu.NetworkIdentifier;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import org.free.bacnet4j.util.ByteQueue;

public class MstpNetwork extends Network {
    private final MstpNode node;

    public MstpNetwork(MstpNode node) {
        this(node, 0);
    }

    public MstpNetwork(MstpNode node, int localNetworkNumber) {
        super(localNetworkNumber);
        this.node = node;
        node.setNetwork(this);
    }

    @Override
    public MaxApduLength getMaxApduLength() {
        return MaxApduLength.UP_TO_480;
    }

    @Override
    public void initialize(Transport transport) throws Exception {
        super.initialize(transport);
        node.initialize();
    }

    @Override
    public void terminate() {
        node.terminate();
    }

    @Override
    public NetworkIdentifier getNetworkIdentifier() {
        return new MstpNetworkIdentifier(node.getCommPortId());
    }

    @Override
    public Address getLocalBroadcastAddress() {
        return new Address(getLocalNetworkNumber(), (byte) 0xFF);
    }

    @Override
    public Address[] getAllLocalAddresses() {
        return new Address[] { new Address(getLocalNetworkNumber(), node.getThisStation()) };
    }

    @Override
    public void checkSendThread() {
        if (Thread.currentThread() == node.thread)
            throw new IllegalStateException("Cannot send a request in the socket listener thread.");
    }

    @Override
    public void sendAPDU(Address recipient, OctetString link, APDU apdu, boolean broadcast) throws BACnetException {
        ByteQueue queue = new ByteQueue();

        // NPCI
        writeNpci(queue, recipient, link, apdu);

        // APDU
        apdu.write(queue);

        byte[] data = queue.popAll();

        byte mstpAddress;
        if (recipient.isGlobal())
            mstpAddress = getLocalBroadcastAddress().getMacAddress().getMstpAddress();
        else if (link != null)
            mstpAddress = link.getMstpAddress();
        else
            mstpAddress = recipient.getMacAddress().getMstpAddress();

        if (apdu.expectsReply()) {
            if (node instanceof SlaveNode)
                throw new RuntimeException("Cannot originate a request from a slave node");

            ((MasterNode) node).queueFrame(FrameType.bacnetDataExpectingReply, mstpAddress, data);
        }
        else
            node.setReplyFrame(FrameType.bacnetDataNotExpectingReply, mstpAddress, data);
    }

    public void sendTestRequest(byte destination) {
        if (!(node instanceof MasterNode))
            throw new RuntimeException("Only master nodes can send test requests");
        ((MasterNode) node).queueFrame(FrameType.testRequest, destination, null);
    }

    //
    //
    //
    // Incoming frames
    //
    void receivedFrame(Frame frame) {
        new IncomingFrameHandler(this, frame).run();
    }

    class IncomingFrameHandler extends IncomingRequestParser {
        public IncomingFrameHandler(Network network, Frame frame) {
            super(network, new ByteQueue(frame.getData()), new OctetString(frame.getSourceAddress()));
        }

        @Override
        protected void parseFrame() throws MessageValidationAssertionException {
            // no op. The frame has already been parsed.
        }
    }

    //
    //
    // Convenience methods
    //
    public Address getAddress(byte station) {
        return new Address(getLocalNetworkNumber(), station);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((node == null) ? 0 : node.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        MstpNetwork other = (MstpNetwork) obj;
        if (node == null) {
            if (other.node != null)
                return false;
        }
        else if (!node.equals(other.node))
            return false;
        return true;
    }
}
