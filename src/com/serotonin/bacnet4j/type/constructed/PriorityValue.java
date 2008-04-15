package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.AmbiguousValue;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class PriorityValue extends BaseType {
    private Null nullValue;
    private Real realValue;
    private BinaryPV binaryValue;
    private UnsignedInteger integerValue;
    private Encodable constructedValue;
    
    public PriorityValue(Null nullValue) {
        this.nullValue = nullValue;
    }
    
    public PriorityValue(Real realValue) {
        this.realValue = realValue;
    }
    
    public PriorityValue(BinaryPV binaryValue) {
        this.binaryValue = binaryValue;
    }
    
    public PriorityValue(UnsignedInteger integerValue) {
        this.integerValue = integerValue;
    }
    
    public PriorityValue(BaseType constructedValue) {
        this.constructedValue = constructedValue;
    }

    public void write(ByteQueue queue) {
        if (nullValue != null)
            nullValue.write(queue);
        else if (realValue != null)
            realValue.write(queue);
        else if (binaryValue != null)
            binaryValue.write(queue);
        else if (integerValue != null)
            integerValue.write(queue);
        else
            constructedValue.write(queue, 0);
    }
    
    public PriorityValue(ByteQueue queue) throws BACnetException {
        // Sweet Jesus...
        int tag = (queue.peek(0) & 0xff);
        if ((tag & 8) == 8) {
            // A class tag, so this is a constructed value.
            constructedValue = new AmbiguousValue(queue, 0);
        }
        else {
            // A primitive value
            tag = tag >> 4;
            if (tag == Null.TYPE_ID)
                nullValue = new Null(queue);
            else if (tag == Real.TYPE_ID)
                realValue = new Real(queue);
            else if (tag == BinaryPV.TYPE_ID)
                binaryValue = new BinaryPV(queue);
            else if (tag == UnsignedInteger.TYPE_ID)
                integerValue = new UnsignedInteger(queue);
            else
                throw new BACnetErrorException(ErrorClass.property, ErrorCode.invalidDataType,
                        "Unsupported primitive id: "+ tag);
        }
    }
}
