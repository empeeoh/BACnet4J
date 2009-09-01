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
    
    public Null getNullValue() {
        return nullValue;
    }

    public Real getRealValue() {
        return realValue;
    }

    public BinaryPV getBinaryValue() {
        return binaryValue;
    }

    public UnsignedInteger getIntegerValue() {
        return integerValue;
    }

    public Encodable getConstructedValue() {
        return constructedValue;
    }

    @Override
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
