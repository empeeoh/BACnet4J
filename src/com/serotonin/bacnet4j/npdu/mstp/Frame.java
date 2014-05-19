package com.serotonin.bacnet4j.npdu.mstp;

import org.free.bacnet4j.util.StreamUtils;

public class Frame implements Cloneable {
    private FrameType frameType;
    private byte destinationAddress;
    private byte sourceAddress;
    private int length;
    private byte[] data;

    public Frame() {
        // no op
    }

    public void reset() {
        frameType = null;
        destinationAddress = 0;
        sourceAddress = 0;
        length = 0;
        data = null;
    }

    public Frame copy() {
        try {
            return (Frame) clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public Frame(FrameType frameType, byte destinationAddress, byte sourceAddress) {
        this(frameType, destinationAddress, sourceAddress, null);
    }

    public Frame(FrameType frameType, byte destinationAddress, byte sourceAddress, byte[] data) {
        this.frameType = frameType;
        this.destinationAddress = destinationAddress;
        this.sourceAddress = sourceAddress;
        this.length = data == null ? 0 : data.length;
        this.data = data;
    }

    public boolean forStation(byte thisStation) {
        return destinationAddress == thisStation;
    }

    public boolean broadcast() {
        return destinationAddress == Constants.BROADCAST;
    }

    public boolean forStationOrBroadcast(byte thisStation) {
        return forStation(thisStation) || broadcast();
    }

    /**
     * @return the frameType
     */
    public FrameType getFrameType() {
        return frameType;
    }

    /**
     * @param frameType
     *            the frameType to set
     */
    public void setFrameType(FrameType frameType) {
        this.frameType = frameType;
    }

    /**
     * @return the destinationAddress
     */
    public byte getDestinationAddress() {
        return destinationAddress;
    }

    /**
     * @param destinationAddress
     *            the destinationAddress to set
     */
    public void setDestinationAddress(byte destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    /**
     * @return the sourceAddress
     */
    public byte getSourceAddress() {
        return sourceAddress;
    }

    /**
     * @param sourceAddress
     *            the sourceAddress to set
     */
    public void setSourceAddress(byte sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length
     *            the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(byte[] data) {
        this.length = data == null ? 0 : data.length;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Frame [frameType=" + frameType + ", destinationAddress=" + destinationAddress + ", sourceAddress="
                + sourceAddress + ", length=" + length + ", data="
                + (data == null ? "null" : StreamUtils.dumpHex(data)) + "]";
    }
}
