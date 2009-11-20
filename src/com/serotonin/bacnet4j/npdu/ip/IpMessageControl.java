/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2009 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 */
package com.serotonin.bacnet4j.npdu.ip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.Network;
import com.serotonin.bacnet4j.apdu.APDU;
import com.serotonin.bacnet4j.apdu.AckAPDU;
import com.serotonin.bacnet4j.apdu.ComplexACK;
import com.serotonin.bacnet4j.apdu.ConfirmedRequest;
import com.serotonin.bacnet4j.apdu.Error;
import com.serotonin.bacnet4j.apdu.Reject;
import com.serotonin.bacnet4j.apdu.SegmentACK;
import com.serotonin.bacnet4j.apdu.Segmentable;
import com.serotonin.bacnet4j.apdu.SimpleACK;
import com.serotonin.bacnet4j.apdu.UnconfirmedRequest;
import com.serotonin.bacnet4j.base.BACnetUtils;
import com.serotonin.bacnet4j.enums.MaxApduLength;
import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetRejectException;
import com.serotonin.bacnet4j.exception.BACnetTimeoutException;
import com.serotonin.bacnet4j.npdu.NPCI;
import com.serotonin.bacnet4j.npdu.RequestHandler;
import com.serotonin.bacnet4j.npdu.WaitingRoom;
import com.serotonin.bacnet4j.npdu.WaitingRoom.Key;
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
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

/**
 * @author mlohbihler
 */
public class IpMessageControl extends Thread {
    private static final MaxApduLength APDU_LENGTH = MaxApduLength.UP_TO_1476;
    private static final int MESSAGE_LENGTH = 2048;
    private static final int MAX_SEGMENTS = 7; // Greater than 64.
    
    
    // Config
    private int port;
    private String localBindAddress = "0.0.0.0";
    private String broadcastAddress = "255.255.255.255";
    private int timeout = 5000;
    private int segTimeout = 1000;
    private int segWindow = 5;
    private int retries = 2;
    private RequestHandler requestHandler;
    
    // Runtime
    private byte nextInvokeId;
    private DatagramSocket socket;
    private final WaitingRoom waitingRoom = new WaitingRoom();
    private ExecutorService incomingExecutorService;
    
    public RequestHandler getRequestHandler() {
        return requestHandler;
    }
    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public int getPort() {
        return port;
    }
    public String getLocalBindAddress() {
        return localBindAddress;
    }
    public void setLocalBindAddress(String localBindAddress) {
        this.localBindAddress = localBindAddress;
    }
    public void setBroadcastAddress(String  broadcastAddress) {
        this.broadcastAddress = broadcastAddress;
    }
    public String getBroadcastAddress() {
        return broadcastAddress;
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

    public void initialize() throws IOException {
        incomingExecutorService = Executors.newCachedThreadPool();
        socket = new DatagramSocket(port, InetAddress.getByName(localBindAddress));
        socket.setBroadcast(true);
        start();
    }
    
    public void terminate() {
        if (socket != null)
            socket.close();
        
        // Close the executor service.
        incomingExecutorService.shutdown();
        try {
            incomingExecutorService.awaitTermination(3, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            LocalDevice.getExceptionListener().receivedException(e);
        }
    }
    
    private byte getNextInvokeId() {
        return nextInvokeId++;
    }
    
    public AckAPDU send(String host, Network network, int maxAPDULengthAccepted, Segmentation segmentationSupported, 
            ConfirmedRequestService serviceRequest) throws BACnetException {
        return send(host, port, network, maxAPDULengthAccepted, segmentationSupported, serviceRequest);
    }
    
    public AckAPDU send(String host, int port, Network network, int maxAPDULengthAccepted,
            Segmentation segmentationSupported, ConfirmedRequestService serviceRequest) throws BACnetException {
        return send(new InetSocketAddress(host, port), network, maxAPDULengthAccepted, segmentationSupported,
                serviceRequest);
    }
    
    public AckAPDU send(byte[] host, int port, Network network, int maxAPDULengthAccepted,
            Segmentation segmentationSupported, ConfirmedRequestService serviceRequest) throws BACnetException {
        try {
            return send(new InetSocketAddress(InetAddress.getByAddress(host), port), network, maxAPDULengthAccepted,
                    segmentationSupported, serviceRequest);
        }
        catch (UnknownHostException e) {
            throw new BACnetException(e);
        }
    }
    
    public AckAPDU send(InetSocketAddress addr, Network network, int maxAPDULengthAccepted,
            Segmentation segmentationSupported, ConfirmedRequestService serviceRequest) throws BACnetException {
        // Get an invoke id.
        byte id = getNextInvokeId();
        
        // Serialize the service request.
        ByteQueue serviceData = new ByteQueue();
        serviceRequest.write(serviceData);
        
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
            
            Key key = waitingRoom.enter(addr, network, id, false);
            try {
                return sendSegmentedRequest(key, maxAPDULengthAccepted, maxServiceData, serviceRequest.getChoiceId(),
                        serviceData);
            }
            finally {
                waitingRoom.leave(key);
            }
        }
        else {
            // We can send the whole APDU in one shot.
            ConfirmedRequest apdu = new ConfirmedRequest(false, false, true, MAX_SEGMENTS, APDU_LENGTH, id, (byte)0, 0,
                    serviceRequest.getChoiceId(), serviceData);
            return send(addr, network, id, timeout, apdu, false);
        }
    }
    
    private void sendResponse(InetSocketAddress addr, Network network, ConfirmedRequest request,
            AcknowledgementService response) throws BACnetException {
        if (response == null) {
            sendImpl(new SimpleACK(request.getInvokeId(), request.getServiceRequest().getChoiceId()), false, addr,
                    network);
            return;
        }
        
        // A complex ack response. Serialize the data.
        ByteQueue serviceData = new ByteQueue();
        response.write(serviceData);
        
        // Check if we need to segment the message.
        if (serviceData.size() > request.getMaxApduLengthAccepted().getMaxLength() - ComplexACK.getHeaderSize(false)) {
            int maxServiceData = request.getMaxApduLengthAccepted().getMaxLength() - ComplexACK.getHeaderSize(true);
            // Check if the device can accept what we want to send.
            if (!request.isSegmentedResponseAccepted())
                throw new BACnetException("Response too big to send to device without segmentation");
            int segmentsRequired = serviceData.size() / maxServiceData + 1;
            if (segmentsRequired > request.getMaxSegmentsAccepted() || segmentsRequired > 128)
                throw new BACnetException("Response too big to send to device; too many segments required");
            
            Key key = waitingRoom.enter(addr, network, request.getInvokeId(), true);
            try {
                sendSegmentedResponse(key, request.getMaxApduLengthAccepted().getMaxLength(), maxServiceData,
                        response.getChoiceId(), serviceData);
            }
            finally {
                waitingRoom.leave(key);
            }
        }
        else
            // We can send the whole APDU in one shot.
            sendImpl(new ComplexACK(false, false, request.getInvokeId(), 0, 0, response), false, addr, network);
    }
    
    private AckAPDU send(InetSocketAddress addr, Network network, byte id, int timeout, APDU apdu, boolean server)
            throws BACnetException {
        Key key = waitingRoom.enter(addr, network, id, server);
        try {
            return send(key, timeout, new APDU[] {apdu});
        }
        finally {
            waitingRoom.leave(key);
        }
    }
    
    private AckAPDU send(Key key, int timeout, APDU[] apdu) throws BACnetException {
        byte[][] data = new byte[apdu.length][];
        for (int i=0; i<data.length; i++)
            data[i] = createMessageData(apdu[i], false, key.getNetwork());
        
        AckAPDU response = null;
        int attempts = retries + 1;
        
        // The retry loop.
        while (true) {
            for (int i=0; i<data.length; i++)
                sendImpl(data[i], key.getPeer());
            
            response = waitForAck(key, timeout, false);
            if (response == null) {
                attempts--;
                
                if (attempts > 0)
                    // Try again
                    continue;
                
                // Give up
                throw new BACnetTimeoutException("Timeout while waiting for response for id "+ key.getInvokeId());
            }
            
            // We got the response
            break;
        }
        
        return response;
    }
    
    public void sendUnconfirmed(String host, Network network, UnconfirmedRequestService serviceRequest)
            throws BACnetException {
        sendUnconfirmed(host, port, network, serviceRequest);
    }
    
    public void sendUnconfirmed(String host, int port, Network network, UnconfirmedRequestService serviceRequest)
            throws BACnetException {
        sendUnconfirmed(new InetSocketAddress(host, port), network, serviceRequest);
    }

    public void sendUnconfirmed(byte[] host, int port, Network network, UnconfirmedRequestService serviceRequest)
            throws BACnetException {
        try {
            sendUnconfirmed(new InetSocketAddress(InetAddress.getByAddress(host), port), network, serviceRequest);
        }
        catch (UnknownHostException e) {
            throw new BACnetException(e);
        }
    }

    public void sendUnconfirmed(InetSocketAddress addr, Network network, UnconfirmedRequestService serviceRequest)
            throws BACnetException {
        UnconfirmedRequest apdu = new UnconfirmedRequest(serviceRequest);
        byte[] data = createMessageData(apdu, false, network);
        sendImpl(data, addr);
    }
    
    public void sendBroadcast(Network network, UnconfirmedRequestService serviceRequest) throws BACnetException {
        sendBroadcast(port, network, serviceRequest);
    }
    
    public void sendBroadcast(int port, Network network, UnconfirmedRequestService serviceRequest)
            throws BACnetException {
        UnconfirmedRequest apdu = new UnconfirmedRequest(serviceRequest);
        byte[] data = createMessageData(apdu, true, network);
        sendImpl(data, new InetSocketAddress(broadcastAddress, port));
    }
    
    private void sendImpl(APDU apdu, boolean broadcast, InetSocketAddress addr, Network network)
            throws BACnetException {
        sendImpl(createMessageData(apdu, broadcast, network), addr);
    }
    
    private void sendImpl(byte[] data, InetSocketAddress addr) throws BACnetException {
        try {
            DatagramPacket packet = new DatagramPacket(data, data.length, addr);
            socket.send(packet);
        }
        catch (Exception e) {
            throw new BACnetException(e);
        }
    }
    
    private byte[] createMessageData(APDU apdu, boolean broadcast, Network network) {
        ByteQueue apduQueue = new ByteQueue();
        apdu.write(apduQueue);
        
        ByteQueue queue = new ByteQueue();
        
        // BACnet virtual link layer detail
        
        // BACnet/IP
        queue.push(0x81);
        
        // Original-Unicast-NPDU
        queue.push(broadcast ? 0xb : 0xa);
        
        // Length including the BACnet/IP identifier, function, length, protocol version, response expected and the
        // apdu
        int len = apduQueue.size() + 6;
        
        if (network != null)
            // Inclusion of network destination info increases the length by adding the network number (2),
            // address length (1), address (n), and the hop count (1)
            len += network.getNetworkAddress().length + 4;
        
        BACnetUtils.pushShort(queue, len);
        
        // Protocol version, always 1.
        queue.push((byte)1);
        
        // NPCI: Responses are expected for confirmed requests.
        int npci = 0;
        if (apdu.expectsReply())
            npci |= 4;
        if (network != null)
            npci |= 32;
        queue.push((byte)npci);
        
        // Add the npdu
        if (network != null) {
            // Network number
            BACnetUtils.pushShort(queue, network.getNetworkNumber());
            // Address length
            queue.push(network.getNetworkAddress().length);
            // Address
            queue.push(network.getNetworkAddress());
            // Hop count
            queue.push((byte)0xff);
        }
        
        // Add the apdu
        queue.push(apduQueue);
        
        return queue.popAll();
    }
    
    // For receiving
    @Override
    public void run() {
        byte[] buffer = new byte[MESSAGE_LENGTH];
        DatagramPacket p = new DatagramPacket(buffer, buffer.length);
        
        while (!socket.isClosed()) {
            try {
                socket.receive(p);
                incomingExecutorService.execute(new IncomingMessageExecutor(p));
                p.setData(buffer);
            }
            catch (IOException e) {
                // no op. This happens if the socket gets closed by the destroy method.
            }
        }
    }
    
    class IncomingMessageExecutor implements Runnable {
        ByteQueue originalQueue;
        ByteQueue queue;
        InetAddress fromAddr;
        int fromPort;
        Network fromNetwork;
        
        IncomingMessageExecutor(DatagramPacket p) {
            originalQueue = new ByteQueue(p.getData(), 0, p.getLength());
            queue = new ByteQueue(p.getData(), 0, p.getLength());
            fromAddr = p.getAddress();
            fromPort = p.getPort();
        }
        
        // Added for testing with the main method below.
        IncomingMessageExecutor() {
            
        }
        
        public void run() {
            try {
                runImpl();
            }
            catch (Exception e) {
                LocalDevice.getExceptionListener().receivedException(e);
            }
            catch (Throwable t) {
                LocalDevice.getExceptionListener().receivedThrowable(t);
            }
        }
        
        private void runImpl() throws Exception {
            // Initial parsing of IP message.
            // BACnet/IP
            if (queue.pop() != (byte)0x81)
                throw new MessageValidationAssertionException("Protocol id is not BACnet/IP (0x81)");
            
            byte function = queue.pop();
            if (function != 0xa && function != 0xb && function != 0x4)
                throw new MessageValidationAssertionException(
                        "Function is not unicast, broadcast, or forward (0xa, 0xb, or 0x4)");
            
            int length = BACnetUtils.popShort(queue);
            if (length != queue.size() + 4)
                throw new MessageValidationAssertionException("Length field does not match data: given="+ length
                        +", expected="+ (queue.size() + 4));
            
            if (function == 0x4)
                // A forward. Ignore the next 6 bytes.
                queue.pop(6);
            
            // Network layer protocol control information. See 6.2.2
            NPCI npci = new NPCI(queue);
            if (npci.getVersion() != 1)
                throw new MessageValidationAssertionException("Invalid protocol version: "+ npci.getVersion());
            if (npci.isNetworkMessage())
                throw new MessageValidationAssertionException("Network messages are not supported");
            
            if (npci.hasSourceInfo())
                fromNetwork = new Network(npci.getSourceNetwork(), npci.getSourceAddress());
            
            // Create the APDU.
            APDU apdu;
            try {
                apdu = APDU.createAPDU(queue);
            }
            catch (Exception e) {
                throw new BACnetException("Error while creating APDU: ", e);
            }
            
//            if (apdu.expectsReply() != npci.isExpectingReply())
//                throw new MessageValidationAssertionException("Inconsistent message: APDU expectsReply="+
//                        apdu.expectsReply() +" while NPCI isExpectingReply="+ npci.isExpectingReply());
            
            if (apdu instanceof ConfirmedRequest) {
                ConfirmedRequest confAPDU = (ConfirmedRequest)apdu;
                InetSocketAddress from = new InetSocketAddress(fromAddr, fromPort);
                byte invokeId = confAPDU.getInvokeId();
                
                if (confAPDU.isSegmentedMessage() && confAPDU.getSequenceNumber() > 0)
                    // This is a subsequent part of a segmented message. Notify the waiting room.
                    waitingRoom.notifyMember(from, fromNetwork, invokeId, false, confAPDU);
                else {
                    if (confAPDU.isSegmentedMessage()) {
                        // This is the initial part of a segmented message. Go and receive the subsequent parts.
                        Key key = waitingRoom.enter(from, fromNetwork, invokeId, true);
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
//                        AcknowledgementService ackService = requestHandler.handleConfirmedRequest(
//                                new Address(new UnsignedInteger(fromPort), new OctetString(fromAddr.getAddress())),
//                                fromNetwork, invokeId, confAPDU.getServiceRequest());
                        AcknowledgementService ackService = requestHandler.handleConfirmedRequest(
                                new Address(fromNetwork, fromAddr.getAddress(), fromPort),
                                fromNetwork, invokeId, confAPDU.getServiceRequest());
                        sendResponse(from, fromNetwork, confAPDU, ackService);
                    }
                    catch (BACnetErrorException e) {
                        sendImpl(new Error(invokeId, e.getError()), false, from, fromNetwork);
                    }
                    catch (BACnetRejectException e) {
                        sendImpl(new Reject(invokeId, e.getRejectReason()), false, from, fromNetwork);
                    }
                    catch (BACnetException e) {
                        sendImpl(new Error(confAPDU.getInvokeId(), new BaseError((byte)127,
                                new BACnetError(ErrorClass.services, ErrorCode.inconsistentParameters))),
                                false, from, fromNetwork);
                        LocalDevice.getExceptionListener().receivedThrowable(e.getCause());
                    }
                }
            }
            else if (apdu instanceof UnconfirmedRequest) {
                requestHandler.handleUnconfirmedRequest(
                        new Address(new UnsignedInteger(fromPort), new OctetString(fromAddr.getAddress())),
                        fromNetwork, ((UnconfirmedRequest)apdu).getService());
            }
            else {
                // An acknowledgement.
                AckAPDU ack = (AckAPDU)apdu;
                waitingRoom.notifyMember(new InetSocketAddress(fromAddr, fromPort), fromNetwork,
                        ack.getOriginalInvokeId(), ack.isServer(), ack);
            }
        }
    }
    
    class MessageValidationAssertionException extends Exception {
        private static final long serialVersionUID = -1;
        
        public MessageValidationAssertionException(String message) {
            super(message);
        }
    }
    
    private AckAPDU sendSegmentedRequest(Key key, int maxAPDULengthAccepted, int maxServiceData, byte serviceChoice, 
            ByteQueue serviceData) throws BACnetException {
        ConfirmedRequest template = new ConfirmedRequest(true, true, true, MAX_SEGMENTS, APDU_LENGTH, 
                key.getInvokeId(), 0, segWindow, serviceChoice, (ByteQueue)null);
        AckAPDU ack = sendSegmented(key, maxAPDULengthAccepted, maxServiceData, serviceData, template);
        if (ack != null)
            return ack;
        
        // Go to the waiting room to wait for a response.
        return waitForAck(key, timeout, true);
    }
    
    private void sendSegmentedResponse(Key key, int maxAPDULengthAccepted, int maxServiceData, byte serviceChoice,
            ByteQueue serviceData) throws BACnetException {
        ComplexACK template = new ComplexACK(true, true, key.getInvokeId(), (byte)0, segWindow, serviceChoice,
                (ByteQueue)null);
        AckAPDU ack = sendSegmented(key, maxAPDULengthAccepted, maxServiceData, serviceData, template);
        if (ack != null)
            throw new BACnetException("Invalid response from client while sending segmented response: "+ ack);
    }
    
    
    private AckAPDU sendSegmented(Key key, int maxAPDULengthAccepted, int maxServiceData, ByteQueue serviceData,
            Segmentable segmentTemplate) throws BACnetException {
        System.out.println("Send segmented: "+ (serviceData.size() / maxServiceData + 1) +" segments required");
        
        // Send an initial message to negotiate communication terms.
        ByteQueue segData = new ByteQueue(maxServiceData);
        byte[] data = new byte[maxServiceData];
        serviceData.pop(data);
        segData.push(data);
        
        APDU apdu = segmentTemplate.clone(true, 0, segmentTemplate.getProposedWindowSize(), segData);
        System.out.println("Sending segment 0");
        AckAPDU response = send(key, segTimeout, new APDU[] {apdu});
        if (!(response instanceof SegmentACK))
            return response;
        
        SegmentACK ack = (SegmentACK)response;
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
            System.out.println("Sending "+ window.length +" segments");
            for (int i=0; i<window.length; i++)
                window[i] = apduQueue.poll();
            
            response = send(key, segTimeout, window);
            if (response instanceof SegmentACK) {
                ack = (SegmentACK)response;
                if (ack.isNegativeAck())
                    System.out.println("NAK received: "+ ack);
                int receivedSeq = ack.getSequenceNumber() & 0xff;
                while (apduQueue.size() > 0 &&
                        (((Segmentable)apduQueue.peek()).getSequenceNumber() & 0xff) <= receivedSeq)
                    apduQueue.poll();
            }
            else
                return response;
        }
        
        return null;
    }
    
    private AckAPDU waitForAck(Key key, long timeout, boolean throwTimeout) throws BACnetException {
        AckAPDU ack = waitingRoom.getAck(key, timeout, throwTimeout);
        
        if (!(ack instanceof ComplexACK))
            return ack;
        
        ComplexACK firstPart = (ComplexACK)ack;
        if (firstPart.isSegmentedMessage())
            receiveSegmented(key, firstPart);
        
        firstPart.parseServiceData();
        return firstPart;
    }
    
    
    private void receiveSegmented(Key key, Segmentable firstPart) throws BACnetException {
        boolean server = !key.isFromServer();
        InetSocketAddress peer = key.getPeer();
        byte id = firstPart.getInvokeId();
        int window = firstPart.getProposedWindowSize();
        int lastSeq = firstPart.getSequenceNumber() & 0xff;
        //System.out.println("Receiving segmented: window="+ window);
        
        // Send a segment ack. Go with whatever window size was proposed.
        sendImpl(new SegmentACK(false, server, id, lastSeq, window, true), false, peer, key.getNetwork());
        
        // The loop for receiving windows of request parts.
        Segmentable segment;
        SegmentWindow segmentWindow = new SegmentWindow(window, lastSeq + 1);
        while (true) {
            segment = segmentWindow.getSegment(lastSeq + 1);
            if (segment == null) {
                // Wait for the next part of the message to arrive.
                segment = waitingRoom.getSegmentable(key, segTimeout*4, false);
                
                if (segment == null) {
                    // We timed out waiting for a segment.
                    if (segmentWindow.isEmpty())
                        // We didn't receive anything, so throw a timeout exception.
                        throw new BACnetTimeoutException("Timeout while waiting for segment part: invokeId="+
                                id +", sequenceId="+ (lastSeq + 1));
                    
                    // Return a NAK with the last sequence id received in order and start over.
                    sendImpl(new SegmentACK(true, server, id, lastSeq, window, true), false, peer, key.getNetwork());
                    segmentWindow.clear(lastSeq + 1);
                }
                else if (segmentWindow.fitsInWindow(segment))
                    segmentWindow.setSegment(segment);
                
                continue;
            }
            
            // We have the required segment. Append the part to the first part.
            //System.out.println("Received segment "+ segment.getSequenceNumber());
            firstPart.appendServiceData(segment.getServiceData());
            lastSeq++;
                
            // Do we need to send an ack?
            if (!segment.isMoreFollows() || segmentWindow.isLastSegment(lastSeq)) {
                // Return an acknowledgement
                sendImpl(new SegmentACK(false, server, id, lastSeq, window, segment.isMoreFollows()), false, peer,
                        key.getNetwork());
                segmentWindow.clear(lastSeq + 1);
            }
                
            if (!segment.isMoreFollows())
                // We're done.
                break;
        }
        //System.out.println("Finished receiving segmented");
    }
    
    // Testing 
    public static void main(String[] args) throws Exception {
        IpMessageControl ipMessageControl = new IpMessageControl();
        IncomingMessageExecutor ime = ipMessageControl.new IncomingMessageExecutor();
        
//        String input = "[81,b,0,c,1,20,ff,ff,0,ff,10,8]";
//        String input = "[81,b,0,14,1,0,10,0,c4,2,0,8,98,22,1,e0,91,0,21,8]";
//        String input = "[81,4,0,23,c0,a8,1,5a,ba,c0,1,8,27,28,6,0,40,ae,0,73,8f,10,0,c4,2,0,9,60,22,1,e0,91,0,21,8]";
//        String input = "[81,4,0,1e,c0,a8,1,5a,ba,c0,1,8,4e,38,1,3,10,0,c4,2,0,9,63,22,1,e0,91,0,21,8]";
//        String input = "[81,4,0,1e,c0,a8,1,5a,ba,c0,1,8,4e,36,1,1,10,0,c4,2,0,8,99,22,1,e0,91,0,21,8]";
//        String input = "[81,4,0,1e,c0,a8,1,5a,ba,c0,1,8,4e,38,1,4,10,0,c4,2,0,9,64,22,1,e0,91,0,21,8]";
//        String input = "[81,4,0,1e,c0,a8,1,5a,ba,c0,1,8,4e,36,1,2,10,0,c4,2,0,8,9a,22,1,e0,91,0,21,8]";
//        String input = "[81,b,0,1b,1,28,ff,ff,0,27,28,6,0,40,ae,0,73,8f,fe,10,8,a,9,62,1a,9,62]";
//        String input = "[81,b,0,1b,1,28,ff,ff,0,27,28,6,0,40,ae,0,73,8f,fe,10,8,a,9,61,1a,9,61]";
//        String input = "[81,b,0,12,1,88,27,28,6,0,40,ae,0,73,8f,0,9c,58]";
//        String input = "[81,b,0,2b,1,0,10,4,9,8,19,1,2e,c,2,0,8,98,19,0,29,0,3e,c,0,80,0,2e,19,55,3e,44,42,c8,0,0,3f,5b,4,0,0,3f,2f]";
//        String input = "[81,b,0,2b,1,0,10,4,9,8,19,1,2e,c,2,0,8,98,19,0,29,0,3e,c,0,80,0,2e,19,55,3e,44,42,c8,0,0,3f,5b,4,0,0,3f,2f]";

//        String input = "81|0a|00|17|01|20|ff|ff |00|ff|10|07|3d|09|00|65|69 |62|74|65|6d|70|31";
//        String input = "81 |0a|00|1d|01|00|10|01|c4|02|00|04 |40|c4|00|00|00|02|75|09|00|65|69 |62|74|65|6d|70|31";
        //String input = "81|0a|00|1d|01|20|ff|ff |00|ff|10|07|0a|04|40|1a|04|40 |3d|09|00|65|69|62|74|65|6d |70|31";
        
        // WhoIs sent by Delta equipment.
        //String input = "81 0b 00 1b 01 28 ff ff 00 27 d8 06 00 18 71 73 0d 8a fe 10 08 0a 11 5e 1a c3 4f";
        
        // IAm response
        String input = "81 0b 00 1b 01 28 ff ff 00 c4 80 01 04 fc 10 00 c4 02 00 76 c4 21 80 91 03 21 08";
        
        if (input.startsWith("["))
            input = input.substring(1);
        if (input.endsWith("]"))
            input = input.substring(0, input.length() - 1);
        //String[] parts = input.split(",");
        //String[] parts = input.split("\\|");
        String[] parts = input.split(" ");
        byte[] bytes = new byte[parts.length];
        
        for (int i=0; i<bytes.length; i++)
            bytes[i] = (byte)Integer.parseInt(parts[i].trim(), 16);
        
        ime.queue = new ByteQueue(bytes);
                
        ime.runImpl();
    }
    
//    public static void main(String[] args) {
//        long start = System.currentTimeMillis();
//        for (int i=0; i<1000000; i++)
//            new DatagramPacket(new byte[MESSAGE_LENGTH], MESSAGE_LENGTH);
//        System.out.println(""+ (System.currentTimeMillis() - start) +"ms");
//    }
}
