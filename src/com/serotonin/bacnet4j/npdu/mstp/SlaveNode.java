package com.serotonin.bacnet4j.npdu.mstp;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.free.bacnet4j.util.SerialParameters;

public class SlaveNode extends MstpNode {
    private static final Logger LOG = Logger.getLogger(SlaveNode.class.toString());

    private enum SlaveNodeState {
        idle, answerDataRequest
    }

    private SlaveNodeState state;

    private long replyDeadline;
    private Frame replyFrame;

    public SlaveNode(SerialParameters serialParams, byte thisStation) throws IllegalArgumentException {
        super(serialParams, thisStation);
        validate();
    }

    public SlaveNode(InputStream in, OutputStream out, byte thisStation) throws IllegalArgumentException {
        super(in, out, thisStation);
        validate();
    }

    private void validate() {
        int is = thisStation & 0xff;
        if (is > 254)
            throw new IllegalArgumentException("thisStation cannot be greater than 254");

        state = SlaveNodeState.idle;
    }

    @Override
    public void setReplyFrame(FrameType type, byte destination, byte[] data) {
        synchronized (this) {
            if (state == SlaveNodeState.answerDataRequest)
                // If there is still time to reply immediately...
                replyFrame = new Frame(type, frame.getSourceAddress(), thisStation, data);
        }
    }

    @Override
    protected void doCycle() {
        readFrame();

        if (state == SlaveNodeState.idle)
            idle();

        if (state == SlaveNodeState.answerDataRequest)
            answerDataRequest();
    }

    /**
     * In the IDLE state, the node waits for a frame.
     */
    private void idle() {
        if (receivedInvalidFrame != null) {
            // ReceivedInvalidFrame
            // debug("idle:ReceivedInvalidFrame");
            if (LOG.isLoggable(Level.FINE))
                LOG.fine("Received invalid frame: " + receivedInvalidFrame);
            receivedInvalidFrame = null;
            activity = true;
        }
        else if (receivedValidFrame) {
            FrameType type = frame.getFrameType();

            if (type == null) {
                // ReceivedUnwantedFrame
                if (LOG.isLoggable(Level.FINE))
                    LOG.fine("Unknown frame type");
            }
            else if (frame.broadcast()
                    && type.oneOf(FrameType.token, FrameType.bacnetDataExpectingReply, FrameType.testRequest)) {
                // ReceivedUnwantedFrame
                if (LOG.isLoggable(Level.FINE))
                    LOG.fine("Frame type should not be broadcast: " + type);
            }
            else if (type.oneOf(FrameType.pollForMaster))
                // ReceivedUnwantedFrame
                ; // It happens
            else if (type.oneOf(FrameType.token, FrameType.pollForMaster, FrameType.replyToPollForMaster,
                    FrameType.replyPostponed)) {
                // ReceivedUnwantedFrame
                if (LOG.isLoggable(Level.FINE))
                    LOG.fine("Received unwanted frame type: " + type);
            }
            else if (frame.forStationOrBroadcast(thisStation)
                    && type.oneOf(FrameType.bacnetDataNotExpectingReply, FrameType.testResponse)) {
                // ReceivedDataNoReply
                //                debug("idle:ReceivedDataNoReply");
                receivedDataNoReply(frame);
            }
            else if (frame.forStation(thisStation)
                    && type.oneOf(FrameType.bacnetDataExpectingReply, FrameType.testRequest)) {
                // ReceivedDataNeedingReply
                //                debug("idle:ReceivedDataNeedingReply");
                state = SlaveNodeState.answerDataRequest;
                replyDeadline = lastNonSilence + Constants.REPLY_DELAY;
                replyFrame = null;
                receivedDataNeedingReply(frame);
            }

            receivedValidFrame = false;
            activity = true;
        }
    }

    /**
     * The ANSWER_DATA_REQUEST state is entered when a BACnet Data Expecting Reply, a Test_Request, or a proprietary
     * frame that expects a reply is received.
     */
    private void answerDataRequest() {
        synchronized (this) {
            if (replyFrame != null) {
                // Reply
                //                debug("answerDataRequest:Reply");
                sendFrame(replyFrame);
                replyFrame = null;
                state = SlaveNodeState.idle;
                activity = true;
            }
            else if (replyDeadline >= timeSource.currentTimeMillis()) {
                // CannotReply
                //                debug("answerDataRequest:CannotReply");
                if (LOG.isLoggable(Level.FINE))
                    LOG.fine("Failed to respond to request: " + frame);
                state = SlaveNodeState.idle;
                activity = true;
            }
        }
    }
}
