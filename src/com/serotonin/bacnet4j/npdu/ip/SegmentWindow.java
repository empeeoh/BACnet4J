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
package com.serotonin.bacnet4j.npdu.ip;

import com.serotonin.bacnet4j.apdu.Segmentable;

public class SegmentWindow {
    private int firstSequenceId;
    private final Segmentable[] segments;

    public SegmentWindow(int windowSize, int firstSequenceId) {
        this.firstSequenceId = firstSequenceId;
        segments = new Segmentable[windowSize];
    }

    public Segmentable getSegment(int sequenceId) {
        return segments[sequenceId - firstSequenceId];
    }

    public void setSegment(Segmentable segment) {
        segments[segment.getSequenceNumber() - firstSequenceId] = segment;
    }

    public boolean fitsInWindow(Segmentable segment) {
        int index = segment.getSequenceNumber() - firstSequenceId;
        if (index < 0 || index >= segments.length)
            return false;
        return true;
    }

    public boolean isEmpty() {
        for (int i = 0; i < segments.length; i++) {
            if (segments[i] != null)
                return false;
        }
        return true;
    }

    public boolean isFull() {
        for (int i = 0; i < segments.length; i++) {
            if (segments[i] == null)
                return false;
        }
        return true;
    }

    public void clear(int firstSequenceId) {
        this.firstSequenceId = firstSequenceId;
        for (int i = 0; i < segments.length; i++)
            segments[i] = null;
    }

    public boolean isLastSegment(int sequenceId) {
        return sequenceId == segments.length + firstSequenceId - 1;
    }
}
