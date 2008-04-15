package com.serotonin.bacnet4j.type.primitive;

import java.io.UnsupportedEncodingException;

import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetRuntimeException;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.util.queue.ByteQueue;

public class CharacterString extends Primitive {
    public static final byte TYPE_ID = 7;
    
    public interface Encodings {
        byte ANSI_X3_4 = 0;
        byte IBM_MS_DBCS = 1;
        byte JIS_C_6226 = 2;
        byte ISO_10646_UCS_4 = 3;
        byte ISO_10646_UCS_2 = 4;
        byte ISO_8859_1 = 5;
    }
    
    private byte encoding;
    private String value;
    
    public CharacterString(String value) {
        encoding = Encodings.ANSI_X3_4;
        this.value = value;
    }
    
    public CharacterString(byte encoding, String value) {
        try {
            validateEncoding(encoding);
        }
        catch (BACnetErrorException e) {
            // This is an API constructor, so it doesn't need to throw checked exceptions. Convert to runtime.
            throw new BACnetRuntimeException(e);
        }
        this.encoding = encoding;
        this.value = value;
    }
    
    public byte getEncoding() {
        return encoding;
    }
    
    public String getValue() {
        return value;
    }
    
    //
    // Reading and writing
    //
    public CharacterString(ByteQueue queue) throws BACnetErrorException  {
        int length = (int)readTag(queue);
        
        byte encoding = queue.pop();
        validateEncoding(encoding);
        
        byte[] bytes = new byte[length - 1];
        queue.pop(bytes);
        
        value = decode(encoding, bytes);
    }
    
    public void writeImpl(ByteQueue queue) {
        queue.push(encoding);
        queue.push(encode(encoding, value));
    }

    protected long getLength() {
        return encode(encoding, value).length + 1;
    }

    protected byte getTypeId() {
        return TYPE_ID;
    }
    
    private static byte[] encode(byte encoding, String value) {
        try {
            switch (encoding) {
            case Encodings.ANSI_X3_4 :
                return value.getBytes();
            case Encodings.ISO_8859_1 :
                return value.getBytes("ISO-8859-1");
            }
        }
        catch (UnsupportedEncodingException e) {
            // Should never happen, so convert to a runtime exception.
            throw new RuntimeException(e);
        }
        return null;
    }
    
    private static String decode(byte encoding, byte[] bytes) {
        try {
            switch (encoding) {
            case Encodings.ANSI_X3_4 :
                return new String(bytes);
            case Encodings.ISO_8859_1 :
                return new String(bytes, "ISO-8859-1");
            }
        }
        catch (UnsupportedEncodingException e) {
            // Should never happen, so convert to a runtime exception.
            throw new RuntimeException(e);
        }
        return null;
    }
    
    private void validateEncoding(byte encoding) throws BACnetErrorException {
        if (encoding != Encodings.ANSI_X3_4 && encoding != Encodings.ISO_8859_1)
            throw new BACnetErrorException(ErrorClass.property, ErrorCode.characterSetNotSupported,
                    Byte.toString(encoding));
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((value == null) ? 0 : value.hashCode());
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
        final CharacterString other = (CharacterString) obj;
        if (encoding != other.encoding)
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        }
        else if (!value.equals(other.value))
            return false;
        return true;
    }
    
    public String toString() {
        return value;
    }
}
