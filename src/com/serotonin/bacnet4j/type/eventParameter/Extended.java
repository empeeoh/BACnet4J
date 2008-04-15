package com.serotonin.bacnet4j.type.eventParameter;

import com.serotonin.bacnet4j.type.constructed.BaseType;
import com.serotonin.bacnet4j.type.constructed.DeviceObjectPropertyReference;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.Primitive;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class Extended extends EventParameter {
    public static final byte TYPE_ID = 9;
    
    private UnsignedInteger vendorId;
    private UnsignedInteger extendedEventType;
    private SequenceOf<Parameter> parameters;
    
    public Extended(UnsignedInteger vendorId, UnsignedInteger extendedEventType, SequenceOf<Parameter> parameters) {
        this.vendorId = vendorId;
        this.extendedEventType = extendedEventType;
        this.parameters = parameters;
    }
    
    protected void writeImpl(ByteQueue queue) {
        vendorId.write(queue, 0);
        extendedEventType.write(queue, 1);
        parameters.write(queue, 2);
    }
    
    protected int getTypeId() {
        return TYPE_ID;
    }
    
    public static class Parameter extends BaseType {
        private Primitive primitive;
        private DeviceObjectPropertyReference reference;
        
        public Parameter(Null primitive) {
            this.primitive = primitive;
        }
        
        public Parameter(Real primitive) {
            this.primitive = primitive;
        }
        
        public Parameter(UnsignedInteger primitive) {
            this.primitive = primitive;
        }
        
        public Parameter(com.serotonin.bacnet4j.type.primitive.Boolean primitive) {
            this.primitive = primitive;
        }
        
        public Parameter(com.serotonin.bacnet4j.type.primitive.Double primitive) {
            this.primitive = primitive;
        }
        
        public Parameter(OctetString primitive) {
            this.primitive = primitive;
        }
        
        public Parameter(BitString primitive) {
            this.primitive = primitive;
        }
        
        public Parameter(Enumerated primitive) {
            this.primitive = primitive;
        }
        
        public Parameter(DeviceObjectPropertyReference reference) {
            this.reference = reference;
        }

        public void write(ByteQueue queue) {
            if (primitive != null)
                primitive.write(queue);
            else
                reference.write(queue, 0);
        }
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((extendedEventType == null) ? 0 : extendedEventType.hashCode());
        result = PRIME * result + ((parameters == null) ? 0 : parameters.hashCode());
        result = PRIME * result + ((vendorId == null) ? 0 : vendorId.hashCode());
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
        final Extended other = (Extended) obj;
        if (extendedEventType == null) {
            if (other.extendedEventType != null)
                return false;
        }
        else if (!extendedEventType.equals(other.extendedEventType))
            return false;
        if (parameters == null) {
            if (other.parameters != null)
                return false;
        }
        else if (!parameters.equals(other.parameters))
            return false;
        if (vendorId == null) {
            if (other.vendorId != null)
                return false;
        }
        else if (!vendorId.equals(other.vendorId))
            return false;
        return true;
    }
}
