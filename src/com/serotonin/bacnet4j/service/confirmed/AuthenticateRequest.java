package com.serotonin.bacnet4j.service.confirmed;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.Network;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.NotImplementedException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class AuthenticateRequest extends ConfirmedRequestService {
    public static final byte TYPE_ID = 24;
    
    private final UnsignedInteger pseudoRandomNumber;
    private final UnsignedInteger expectedInvokeID;
    private final CharacterString operatorName;
    private final CharacterString operatorPassword;
    private final Boolean startEncipheredSession;
    
    public AuthenticateRequest(UnsignedInteger pseudoRandomNumber, UnsignedInteger expectedInvokeID, 
            CharacterString operatorName, CharacterString operatorPassword, Boolean startEncipheredSession) {
        this.pseudoRandomNumber = pseudoRandomNumber;
        this.expectedInvokeID = expectedInvokeID;
        this.operatorName = operatorName;
        this.operatorPassword = operatorPassword;
        this.startEncipheredSession = startEncipheredSession;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public AcknowledgementService handle(LocalDevice localDevice, Address from, Network network)
            throws BACnetException {
        throw new NotImplementedException();
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, pseudoRandomNumber, 0);
        writeOptional(queue, expectedInvokeID, 1);
        writeOptional(queue, operatorName, 2);
        writeOptional(queue, operatorPassword, 3);
        writeOptional(queue, startEncipheredSession, 4);
    }
    
    AuthenticateRequest(ByteQueue queue) throws BACnetException {
        pseudoRandomNumber = read(queue, UnsignedInteger.class, 0);
        expectedInvokeID = readOptional(queue, UnsignedInteger.class, 1);
        operatorName = readOptional(queue, CharacterString.class, 2);
        operatorPassword = readOptional(queue, CharacterString.class, 3);
        startEncipheredSession = readOptional(queue, Boolean.class, 4);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((expectedInvokeID == null) ? 0 : expectedInvokeID.hashCode());
        result = PRIME * result + ((operatorName == null) ? 0 : operatorName.hashCode());
        result = PRIME * result + ((operatorPassword == null) ? 0 : operatorPassword.hashCode());
        result = PRIME * result + ((pseudoRandomNumber == null) ? 0 : pseudoRandomNumber.hashCode());
        result = PRIME * result + ((startEncipheredSession == null) ? 0 : startEncipheredSession.hashCode());
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
        final AuthenticateRequest other = (AuthenticateRequest) obj;
        if (expectedInvokeID == null) {
            if (other.expectedInvokeID != null)
                return false;
        }
        else if (!expectedInvokeID.equals(other.expectedInvokeID))
            return false;
        if (operatorName == null) {
            if (other.operatorName != null)
                return false;
        }
        else if (!operatorName.equals(other.operatorName))
            return false;
        if (operatorPassword == null) {
            if (other.operatorPassword != null)
                return false;
        }
        else if (!operatorPassword.equals(other.operatorPassword))
            return false;
        if (pseudoRandomNumber == null) {
            if (other.pseudoRandomNumber != null)
                return false;
        }
        else if (!pseudoRandomNumber.equals(other.pseudoRandomNumber))
            return false;
        if (startEncipheredSession == null) {
            if (other.startEncipheredSession != null)
                return false;
        }
        else if (!startEncipheredSession.equals(other.startEncipheredSession))
            return false;
        return true;
    }
}
