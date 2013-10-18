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
package com.serotonin.bacnet4j.type;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import org.free.bacnet4j.util.ByteQueue;

public class AmbiguousValue extends Encodable {
    private static final long serialVersionUID = -1554703777454557893L;
    private byte[] data;

    public AmbiguousValue(ByteQueue queue) {
        TagData tagData = new TagData();
        peekTagData(queue, tagData);
        readAmbiguousData(queue, tagData);
    }

    public AmbiguousValue(ByteQueue queue, int contextId) throws BACnetException {
        popStart(queue, contextId);

        TagData tagData = new TagData();
        while (true) {
            peekTagData(queue, tagData);
            if (tagData.isEndTag(contextId))
                break;
            readAmbiguousData(queue, tagData);
        }

        popEnd(queue, contextId);
    }

    @Override
    public void write(ByteQueue queue, int contextId) {
        throw new RuntimeException("Don't write ambigous values, convert to actual types first");
    }

    @Override
    public void write(ByteQueue queue) {
        throw new RuntimeException("Don't write ambigous values, convert to actual types first");
    }

    private void readAmbiguousData(ByteQueue queue, TagData tagData) {
        ByteQueue data = new ByteQueue();
        readAmbiguousData(queue, tagData, data);
        this.data = data.popAll();
    }

    private void readAmbiguousData(ByteQueue queue, TagData tagData, ByteQueue data) {
        if (!tagData.contextSpecific) {
            // Application class.
            if (tagData.tagNumber == Boolean.TYPE_ID)
                copyData(queue, 1, data);
            else
                copyData(queue, tagData.getTotalLength(), data);
        }
        else {
            // Context specific class.
            if (tagData.isStartTag()) {
                // Copy the start tag
                copyData(queue, 1, data);

                // Remember the context id
                int contextId = tagData.tagNumber;

                // Read ambiguous data until we find the end tag.
                while (true) {
                    peekTagData(queue, tagData);
                    if (tagData.isEndTag(contextId))
                        break;
                    readAmbiguousData(queue, tagData);
                }

                // Copy the end tag
                copyData(queue, 1, data);
            }
            else
                copyData(queue, tagData.getTotalLength(), data);
        }
    }

    @Override
    public String toString() {
        return "Ambiguous(" + data + ")";
    }

    private void copyData(ByteQueue queue, int length, ByteQueue data) {
        while (length-- > 0)
            data.push(queue.pop());
    }

    public boolean isNull() {
        return data.length == 1 && data[0] == 0;
    }

    public <T extends Encodable> T convertTo(Class<T> clazz) throws BACnetException {
        return read(new ByteQueue(data), clazz);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((data == null) ? 0 : data.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Encodable))
            return false;
        Encodable eobj = (Encodable) obj;

        try {
            return convertTo(eobj.getClass()).equals(obj);
        }
        catch (BACnetException e) {
            return false;
        }
    }
}
