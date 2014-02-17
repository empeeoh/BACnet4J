package com.serotonin.bacnet4j.transport;

import java.util.LinkedList;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.apdu.APDU;
import com.serotonin.bacnet4j.apdu.Abort;
import com.serotonin.bacnet4j.apdu.AckAPDU;
import com.serotonin.bacnet4j.apdu.ComplexACK;
import com.serotonin.bacnet4j.apdu.ConfirmedRequest;
import com.serotonin.bacnet4j.apdu.Error;
import com.serotonin.bacnet4j.apdu.Reject;
import com.serotonin.bacnet4j.apdu.SegmentACK;
import com.serotonin.bacnet4j.apdu.Segmentable;
import com.serotonin.bacnet4j.apdu.SimpleACK;
import com.serotonin.bacnet4j.apdu.UnconfirmedRequest;
import com.serotonin.bacnet4j.enums.MaxSegments;
import com.serotonin.bacnet4j.event.ExceptionDispatch;
import com.serotonin.bacnet4j.exception.AbortAPDUException;
import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetRejectException;
import com.serotonin.bacnet4j.exception.BACnetTimeoutException;
import com.serotonin.bacnet4j.exception.ErrorAPDUException;
import com.serotonin.bacnet4j.exception.NotImplementedException;
import com.serotonin.bacnet4j.exception.ReflectionException;
import com.serotonin.bacnet4j.exception.RejectAPDUException;
import com.serotonin.bacnet4j.npdu.Network;
import com.serotonin.bacnet4j.npdu.NetworkIdentifier;
import com.serotonin.bacnet4j.npdu.ip.SegmentWindow;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedRequestService;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.error.BaseError;
import com.serotonin.bacnet4j.type.primitive.OctetString;

import org.apache.log4j.Logger;
import org.free.bacnet4j.util.ByteQueue;

/**
 * Provides segmentation support for all data link types.
 * 
 * @author Matthew
 */
public class Transport {
    public static final int DEFAULT_TIMEOUT = 6000;
    public static final int DEFAULT_SEG_TIMEOUT = 5000;
    public static final int DEFAULT_SEG_WINDOW = 5;
    public static final int DEFAULT_RETRIES = 2;

    private static final Logger LOG = Logger.getLogger(LocalDevice.class);
    private static final MaxSegments MAX_SEGMENTS = MaxSegments.MORE_THAN_64;

    private LocalDevice localDevice;
    private final Network network;
    private final WaitingRoom waitingRoom = new WaitingRoom();

    private int timeout = DEFAULT_TIMEOUT;
    private int segTimeout = DEFAULT_SEG_TIMEOUT;
    private int segWindow = DEFAULT_SEG_WINDOW;
    private int retries = DEFAULT_RETRIES;

    public Transport(final Network network) {
        this.network = network;
    }

    public NetworkIdentifier getNetworkIdentifier() {
        return network.getNetworkIdentifier();
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setSegTimeout(int segTimeout) {
        this.segTimeout = segTimeout;
    }

    public int getSegTimeout() {
        return segTimeout;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getRetries() {
        return retries;
    }

    public void setSegWindow(int segWindow) {
        this.segWindow = segWindow;
    }

    public int getSegWindow() {
        return segWindow;
    }

    public Network getNetwork() {
        return network;
    }

    public LocalDevice getLocalDevice() {
        return localDevice;
    }

    public void setLocalDevice(LocalDevice localDevice) {
        this.localDevice = localDevice;
    }

    public void initialize() throws Exception {
        network.initialize(this);
    }

    public void terminate() {
        network.terminate();
    }

    //
    //
    // Sending messages - messages are initiated by the application layer
    //
    /**
     * Sends a confirmed service, and blocks until a reply is received, or timeout.
     * 
     * @param address
     * @param linkService
     * @param maxAPDULengthAccepted
     * @param segmentationSupported
     * @param service
     * @return
     * @throws BACnetException
     */
    public AcknowledgementService send(final Address address, 
    								   final OctetString linkService, 
    								   final int maxAPDULengthAccepted,
    								   final Segmentation segmentationSupported, 
    								   final ConfirmedRequestService service) throws BACnetException {
        if (address == null)
            throw new IllegalArgumentException("address cannot be null");
        network.checkSendThread();
        final OctetString ls; 
        if (address.equals(linkService)){
            ls = null;
        }else{
        	ls = linkService;
        }

        // Serialize the service request.
        ByteQueue serviceData = new ByteQueue();
        service.write(serviceData);

        AckAPDU ack;

        // Check if we need to segment the message.
        if (serviceData.size() > maxAPDULengthAccepted - ConfirmedRequest.getHeaderSize(false)) {
            int maxServiceData = maxAPDULengthAccepted - ConfirmedRequest.getHeaderSize(true);
            // Check if the device can accept what we want to send.
            if (segmentationSupported.intValue() == Segmentation.noSegmentation.intValue() ||
                segmentationSupported.intValue() == Segmentation.segmentedTransmit.intValue())
                throw new BACnetException("Request too big to send to device without segmentation");
            int segmentsRequired = serviceData.size() / maxServiceData + 1;
            if (segmentsRequired > 128)
                throw new BACnetException("Request too big to send to device; too many segments required");

            WaitingRoomKey key = waitingRoom.enterClient(address, ls);
            try {
                ConfirmedRequest template = new ConfirmedRequest(true, true, true, MAX_SEGMENTS,
                        network.getMaxApduLength(), key.getInvokeId(), 0, segWindow, service.getChoiceId(),
                        (ByteQueue) null);
                ack = sendSegmented(key, maxAPDULengthAccepted, maxServiceData, serviceData, template);
                if (ack == null)
                    // Go to the waiting room to wait for a response.
                    ack = waitForAck(key, timeout, true);
            }
            finally {
                waitingRoom.leave(key);
            }
        }
        else {
            // We can send the whole APDU in one shot.
            final WaitingRoomKey key = waitingRoom.enterClient(address, ls);
            final ConfirmedRequest apdu = new ConfirmedRequest(false, false, true, 
            							MAX_SEGMENTS, network.getMaxApduLength(),
            							key.getInvokeId(), (byte) 0, 0, 
            							service.getChoiceId(), serviceData);
            try {
                ack = sendSegments(key, timeout, new APDU[] { apdu });
            }
            finally {
                waitingRoom.leave(key);
            }
        }

        if (ack instanceof SimpleACK)
            return null;

        if (ack instanceof ComplexACK)
            return ((ComplexACK) ack).getService();

        if (ack instanceof Error)
            throw new ErrorAPDUException((Error) ack);

        if (ack instanceof Reject)
            throw new RejectAPDUException((Reject) ack);

        if (ack instanceof Abort)
            throw new AbortAPDUException((Abort) ack);

        throw new BACnetException("Unexpected ack APDU: " + ack);
    }

    public Address getLocalBroadcastAddress() {
        return network.getLocalBroadcastAddress();
    }

    public void sendUnconfirmed(final Address address, 
    							final OctetString linkService, 
    							final UnconfirmedRequestService serviceRequest,
    							final boolean broadcast) throws BACnetException {
    	if (address == null)
    		throw new IllegalArgumentException("Address cannot be null");
    	// Unconfirmed services will never have to be segmented, so just send it.
    	if (address.equals(linkService)){
    		network.sendAPDU(address, null, new UnconfirmedRequest(serviceRequest), broadcast);
    	}else{
    		network.sendAPDU(address, linkService, new UnconfirmedRequest(serviceRequest), broadcast);   
    	}
    }

    //
    // Receiving messages - messages are initiated externally, and received from the data link. Just call the handler
    // method in the service.
    //
    private AcknowledgementService handleConfirmedRequest(Address from, OctetString linkService, byte invokeId,
            ConfirmedRequestService service) throws BACnetException {
        try {
            return service.handle(localDevice, from, linkService);
        }
        catch (NotImplementedException e) {
            System.out.println("Unsupported confirmed request: invokeId=" + invokeId + ", from=" + from + ", request="
                    + service.getClass().getName());
            throw new BACnetErrorException(ErrorClass.services, ErrorCode.serviceRequestDenied);
        }
        catch (BACnetErrorException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BACnetErrorException(ErrorClass.device, ErrorCode.operationalProblem);
        }
    }

    //
    //
    // Details
    //
    private AckAPDU sendSegments(WaitingRoomKey key, int timeout, APDU[] apdu) throws BACnetException {
        AckAPDU response = null;
        int attempts = retries + 1;

        // The retry loop.
        while (true) {
            for (int i = 0; i < apdu.length; i++)
                network.sendAPDU(key.getAddress(), key.getLinkService(), apdu[i], false);

            response = waitForAck(key, timeout, false);
            if (response == null) {
                attempts--;

                if (attempts > 0)
                    // Try again
                    continue;

                // Give up
                throw new BACnetTimeoutException("Timeout while waiting for response for id " + key.getInvokeId());
            }

            // We got the response
            break;
        }

        return response;
    }

    void sendResponse(Address address, OctetString linkService, ConfirmedRequest request,
            AcknowledgementService response) throws BACnetException {
        if (response == null)
            network.sendAPDU(address, linkService, new SimpleACK(request.getInvokeId(), request.getServiceRequest()
                    .getChoiceId()), false);
        else {
            // A complex ack response. Serialize the data.
            ByteQueue serviceData = new ByteQueue();
            response.write(serviceData);

            // Check if we need to segment the message.
            if (serviceData.size() > request.getMaxApduLengthAccepted().getMaxLength()
                    - ComplexACK.getHeaderSize(false)) {
                int maxServiceData = request.getMaxApduLengthAccepted().getMaxLength() - ComplexACK.getHeaderSize(true);
                // Check if the device can accept what we want to send.
                if (!request.isSegmentedResponseAccepted())
                    throw new BACnetException("Response too big to send to device without segmentation");
                int segmentsRequired = serviceData.size() / maxServiceData + 1;
                if (segmentsRequired > request.getMaxSegmentsAccepted().getMaxSegments() || segmentsRequired > 128)
                    throw new BACnetException("Response too big to send to device; too many segments required");

                WaitingRoomKey key = waitingRoom.enterServer(address, linkService, request.getInvokeId());
                try {
                    ComplexACK template = new ComplexACK(true, true, key.getInvokeId(), (byte) 0, segWindow,
                            response.getChoiceId(), (ByteQueue) null);
                    AckAPDU ack = sendSegmented(key, request.getMaxApduLengthAccepted().getMaxLength(), maxServiceData,
                            serviceData, template);
                    if (ack != null)
                        throw new BACnetException("Invalid response from client while sending segmented response: "
                                + ack);
                }
                finally {
                    waitingRoom.leave(key);
                }
            }
            else
                // We can send the whole APDU in one shot.
                network.sendAPDU(address, linkService, new ComplexACK(false, false, request.getInvokeId(), 0, 0,
                        response), false);
        }
    }

    private AckAPDU sendSegmented(WaitingRoomKey key, int maxAPDULengthAccepted, int maxServiceData,
            ByteQueue serviceData, Segmentable segmentTemplate) throws BACnetException {
        // System.out.println("Send segmented: "+ (serviceData.size() / maxServiceData + 1) +" segments required");

        // Send an initial message to negotiate communication terms.
        ByteQueue segData = new ByteQueue(maxServiceData);
        byte[] data = new byte[maxServiceData];
        serviceData.pop(data);
        segData.push(data);

        APDU apdu = segmentTemplate.clone(true, 0, segmentTemplate.getProposedWindowSize(), segData);
        // System.out.println("Sending segment 0");
        AckAPDU response = sendSegments(key, segTimeout, new APDU[] { apdu });
        if (!(response instanceof SegmentACK))
            return response;

        SegmentACK ack = (SegmentACK) response;
        int actualSegWindow = ack.getActualWindowSize();

        // Create a queue of messages to send.
        LinkedList<APDU> apduQueue = new LinkedList<APDU>();
        boolean finalMessage;
        byte sequenceNumber = 1;
        while (serviceData.size() > 0) {
            finalMessage = serviceData.size() <= maxAPDULengthAccepted;
            segData = new ByteQueue();
            int count = serviceData.pop(data);
            segData.push(data, 0, count);
            apdu = segmentTemplate.clone(!finalMessage, sequenceNumber++, actualSegWindow, segData);
            apduQueue.add(apdu);
        }

        // Send the data in windows of size given by the recipient.
        while (apduQueue.size() > 0) {
            APDU[] window = new APDU[Math.min(apduQueue.size(), actualSegWindow)];
            // System.out.println("Sending "+ window.length +" segments");
            for (int i = 0; i < window.length; i++)
                window[i] = apduQueue.poll();

            response = sendSegments(key, segTimeout, window);
            if (response instanceof SegmentACK) {
                ack = (SegmentACK) response;
                // if (ack.isNegativeAck())
                // System.out.println("NAK received: "+ ack);
                int receivedSeq = ack.getSequenceNumber() & 0xff;
                while (apduQueue.size() > 0
                        && (((Segmentable) apduQueue.peek()).getSequenceNumber() & 0xff) <= receivedSeq)
                    apduQueue.poll();
            }
            else
                return response;
        }

        return null;
    }

    /**
     * Waits for an acknowledgement. If the ack is complex and segmented, all of the segments will be read.
     * 
     * @param key
     * @param timeout
     * @param throwTimeout
     * @return
     * @throws BACnetException
     */
    private AckAPDU waitForAck(WaitingRoomKey key, long timeout, boolean throwTimeout) throws BACnetException {
        AckAPDU ack = waitingRoom.getAck(key, timeout, throwTimeout);

        if (!(ack instanceof ComplexACK))
            return ack;

        ComplexACK firstPart = (ComplexACK) ack;
        if (firstPart.isSegmentedMessage())
            receiveSegmented(key, firstPart);

        ByteQueue copy = (ByteQueue) firstPart.getServiceData().clone();
        try {
            firstPart.parseServiceData();
        }
        catch (ReflectionException e) {
            // Detect runtime exceptions in order to provide the original service data that, we presume,
            // caused the problem.
            throw new RuntimeException("Exception while parsing service data: '" + copy + "'", e);
        }

        return firstPart;
    }

    /**
     * Manages the receipt of a segmented message.
     * 
     * @param key
     * @param firstPart
     * @throws BACnetException
     */
    void receiveSegmented(WaitingRoomKey key, Segmentable firstPart) throws BACnetException {
        if (LOG.isDebugEnabled())
            LOG.debug("receiveSegmented: start");

        boolean server = !key.isFromServer();
        byte id = firstPart.getInvokeId();
        int window = firstPart.getProposedWindowSize();
        int lastSeq = firstPart.getSequenceNumber() & 0xff;
        // System.out.println("Receiving segmented: window="+ window);

        // Send a segment ack. Go with whatever window size was proposed.
        network.sendAPDU(key.getAddress(), key.getLinkService(), new SegmentACK(false, server, id, lastSeq, window,
                true), false);

        // The loop for receiving windows of request parts.
        Segmentable segment;
        SegmentWindow segmentWindow = new SegmentWindow(window, lastSeq + 1);
        while (true) {
            segment = segmentWindow.getSegment(lastSeq + 1);
            if (segment == null) {
                // Wait for the next part of the message to arrive.
                segment = waitingRoom.getSegmentable(key, segTimeout * 4, false);

                if (segment == null) {
                    // We timed out waiting for a segment.
                    if (segmentWindow.isEmpty())
                        // We didn't receive anything, so throw a timeout exception.
                        throw new BACnetTimeoutException("Timeout while waiting for segment part: invokeId=" + id
                                + ", sequenceId=" + (lastSeq + 1));

                    // Return a NAK with the last sequence id received in order and start over.
                    network.sendAPDU(key.getAddress(), key.getLinkService(), new SegmentACK(true, server, id, lastSeq,
                            window, true), false);
                    segmentWindow.clear(lastSeq + 1);
                }
                else if (segmentWindow.fitsInWindow(segment))
                    segmentWindow.setSegment(segment);

                continue;
            }

            // We have the required segment. Append the part to the first part.
            // System.out.println("Received segment "+ segment.getSequenceNumber());
            firstPart.appendServiceData(segment.getServiceData());
            if (LOG.isDebugEnabled())
                LOG.debug("receiveSegmented: got segment " + lastSeq);
            lastSeq++;

            // Do we need to send an ack?
            if (!segment.isMoreFollows() || segmentWindow.isLastSegment(lastSeq)) {
                // Return an acknowledgement
                network.sendAPDU(key.getAddress(), key.getLinkService(), new SegmentACK(false, server, id, lastSeq,
                        window, segment.isMoreFollows()), false);

                segmentWindow.clear(lastSeq + 1);
            }

            if (!segment.isMoreFollows())
                // We're done.
                break;
        }

        if (LOG.isDebugEnabled())
            LOG.debug("receiveSegmented: done");
    }

    public void incomingApdu(APDU apdu, Address address, OctetString linkService) throws BACnetException {
        // if (apdu.expectsReply() != npci.isExpectingReply())
        // throw new MessageValidationAssertionException("Inconsistent message: APDU expectsReply="+
        // apdu.expectsReply() +" while NPCI isExpectingReply="+ npci.isExpectingReply());

        if (apdu instanceof ConfirmedRequest) {
            ConfirmedRequest confAPDU = (ConfirmedRequest) apdu;
            byte invokeId = confAPDU.getInvokeId();

            if (confAPDU.isSegmentedMessage() && confAPDU.getSequenceNumber() > 0)
                // This is a subsequent part of a segmented message. Notify the waiting room.
                waitingRoom.notifyMember(address, linkService, invokeId, false, confAPDU);
            else {
                if (confAPDU.isSegmentedMessage()) {
                    // This is the initial part of a segmented message. Go and receive the subsequent parts.
                    WaitingRoomKey key = waitingRoom.enterServer(address, linkService, invokeId);
                    try {
                        receiveSegmented(key, confAPDU);
                    }
                    finally {
                        waitingRoom.leave(key);
                    }
                }

                // Handle the request.
                try {
                    confAPDU.parseServiceData();
                    AcknowledgementService ackService = handleConfirmedRequest(address, linkService, invokeId,
                            confAPDU.getServiceRequest());
                    sendResponse(address, linkService, confAPDU, ackService);
                }
                catch (BACnetErrorException e) {
                    network.sendAPDU(address, linkService, new Error(invokeId, e.getError()), false);
                }
                catch (BACnetRejectException e) {
                    network.sendAPDU(address, linkService, new Reject(invokeId, e.getRejectReason()), false);
                }
                catch (BACnetException e) {
                    Error error = new Error(confAPDU.getInvokeId(), new BaseError((byte) 127, new BACnetError(
                            ErrorClass.services, ErrorCode.inconsistentParameters)));
                    network.sendAPDU(address, linkService, error, false);
                    ExceptionDispatch.fireReceivedException(e);
                }
            }
        }
        else if (apdu instanceof UnconfirmedRequest) {
            UnconfirmedRequest ur = (UnconfirmedRequest) apdu;

            try {
                ur.getService().handle(localDevice, address, linkService);
            }
            catch (BACnetException e) {
                ExceptionDispatch.fireReceivedException(e);
            }
        }
        else {
            // An acknowledgement.
            AckAPDU ack = (AckAPDU) apdu;

            // Used for testing only. This is required to test the parsing of service data in an ack.
            // ((ComplexACK) ack).parseServiceData();

            waitingRoom.notifyMember(address, linkService, ack.getOriginalInvokeId(), ack.isServer(), ack);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((network == null) ? 0 : network.hashCode());
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
        Transport other = (Transport) obj;
        if (network == null) {
            if (other.network != null)
                return false;
        }
        else if (!network.equals(other.network))
            return false;
        return true;
    }
}
