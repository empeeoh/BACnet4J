/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * When signing a commercial license with Serotonin Software Technologies Inc.,
 * the following extension to GPL is made. A special exception to the GPL is 
 * included to allow you to distribute a combined work that includes BAcnet4J 
 * without being obliged to provide the source code for any proprietary components.
 */
package com.serotonin.bacnet4j.type.constructed;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class LogData extends BaseType {
    private static final long serialVersionUID = -1976023645603339559L;

    public static Choice booleanElement(Boolean datum) {
        return new Choice(0, datum);
    }

    public static Choice realElement(Real datum) {
        return new Choice(1, datum);
    }

    public static Choice enumElement(Enumerated datum) {
        return new Choice(2, datum);
    }

    public static Choice unsignedElement(UnsignedInteger datum) {
        return new Choice(3, datum);
    }

    public static Choice signedElement(SignedInteger datum) {
        return new Choice(4, datum);
    }

    public static Choice bitstringElement(BitString datum) {
        return new Choice(5, datum);
    }

    public static Choice nullElement(Null datum) {
        return new Choice(6, datum);
    }

    public static Choice failureElement(BACnetError datum) {
        return new Choice(7, datum);
    }

    public static Choice anyElement(BaseType datum) {
        return new Choice(8, datum);
    }

    private static List<Class<? extends Encodable>> classes;
    static {
        classes = new ArrayList<Class<? extends Encodable>>();
        classes.add(Boolean.class);
        classes.add(Real.class);
        classes.add(Enumerated.class);
        classes.add(UnsignedInteger.class);
        classes.add(SignedInteger.class);
        classes.add(BitString.class);
        classes.add(Null.class);
        classes.add(BACnetError.class);
        classes.add(Encodable.class);
    }

    private final LogStatus logStatus;
    private final SequenceOf<Choice> logData;
    private final Real timeChange;

    public LogData(LogStatus logStatus, SequenceOf<Choice> logData, Real timeChange) {
        this.logStatus = logStatus;
        this.logData = logData;
        this.timeChange = timeChange;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, logStatus, 0);
        write(queue, logData, 1);
        write(queue, timeChange, 2);
    }

    public LogStatus getLogStatus() {
        return logStatus;
    }

    public SequenceOf<Choice> getLogData() {
        return logData;
    }

    public Real getTimeChange() {
        return timeChange;
    }

    public int getChoiceType(int indexBase1) {
        return logData.get(indexBase1).getContextId();
    }

    public Boolean getBoolean(int indexBase1) {
        return (Boolean) logData.get(indexBase1).getDatum();
    }

    public Real getReal(int indexBase1) {
        return (Real) logData.get(indexBase1).getDatum();
    }

    public Enumerated getEnumerated(int indexBase1) {
        return (Enumerated) logData.get(indexBase1).getDatum();
    }

    public UnsignedInteger getUnsignedInteger(int indexBase1) {
        return (UnsignedInteger) logData.get(indexBase1).getDatum();
    }

    public SignedInteger getSignedInteger(int indexBase1) {
        return (SignedInteger) logData.get(indexBase1).getDatum();
    }

    public BitString getBitString(int indexBase1) {
        return (BitString) logData.get(indexBase1).getDatum();
    }

    public Null getNull(int indexBase1) {
        return (Null) logData.get(indexBase1).getDatum();
    }

    public BACnetError getBACnetError(int indexBase1) {
        return (BACnetError) logData.get(indexBase1).getDatum();
    }

    public BaseType getAny(int indexBase1) {
        return (BaseType) logData.get(indexBase1).getDatum();
    }

    public LogData(ByteQueue queue) throws BACnetException {
        logStatus = read(queue, LogStatus.class, 0);
        logData = readSequenceOfChoice(queue, classes, 1);
        timeChange = read(queue, Real.class, 2);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((logData == null) ? 0 : logData.hashCode());
        result = prime * result + ((logStatus == null) ? 0 : logStatus.hashCode());
        result = prime * result + ((timeChange == null) ? 0 : timeChange.hashCode());
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
        final LogData other = (LogData) obj;
        if (logData == null) {
            if (other.logData != null)
                return false;
        }
        else if (!logData.equals(other.logData))
            return false;
        if (logStatus == null) {
            if (other.logStatus != null)
                return false;
        }
        else if (!logStatus.equals(other.logStatus))
            return false;
        if (timeChange == null) {
            if (other.timeChange != null)
                return false;
        }
        else if (!timeChange.equals(other.timeChange))
            return false;
        return true;
    }
}
