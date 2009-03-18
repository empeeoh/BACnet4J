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
package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class Segmentation extends Enumerated {
    public static final Segmentation segmentedBoth = new Segmentation(0);
    public static final Segmentation segmentedTransmit = new Segmentation(1);
    public static final Segmentation segmentedReceive = new Segmentation(2);
    public static final Segmentation noSegmentation = new Segmentation(3);

    public Segmentation(int value) {
        super(value);
    }
    
    public Segmentation(ByteQueue queue) {
        super(queue);
    }
    
    public boolean hasTransmitSegmentation() {
        return this.equals(segmentedBoth) || this.equals(segmentedTransmit);
    }
    
    public boolean hasReceiveSegmentation() {
        return this.equals(segmentedBoth) || this.equals(segmentedReceive);
    }
    
    @Override
    public String toString() {
        int type = intValue();
        if (type == segmentedBoth.intValue())
            return "both";
        if (type == segmentedTransmit.intValue())
            return "transmit";
        if (type == segmentedReceive.intValue())
            return "receive";
        if (type == noSegmentation.intValue())
            return "none";
        return "Unknown: "+ type;
    }
}
