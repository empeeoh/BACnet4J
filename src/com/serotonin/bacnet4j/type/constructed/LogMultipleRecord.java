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

import com.serotonin.bacnet4j.exception.BACnetException;
import org.free.bacnet4j.util.ByteQueue;

public class LogMultipleRecord extends BaseType {
    private static final long serialVersionUID = 3817374635968734673L;
    private final DateTime timestamp;
    private final LogData logData;

    public LogMultipleRecord(DateTime timestamp, LogData logData) {
        this.timestamp = timestamp;
        this.logData = logData;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, timestamp, 0);
        write(queue, logData, 1);
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public LogData getLogData() {
        return logData;
    }

    public LogMultipleRecord(ByteQueue queue) throws BACnetException {
        timestamp = read(queue, DateTime.class, 0);
        logData = read(queue, LogData.class, 1);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((logData == null) ? 0 : logData.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
        final LogMultipleRecord other = (LogMultipleRecord) obj;
        if (logData == null) {
            if (other.logData != null)
                return false;
        }
        else if (!logData.equals(other.logData))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        }
        else if (!timestamp.equals(other.timestamp))
            return false;
        return true;
    }
}
