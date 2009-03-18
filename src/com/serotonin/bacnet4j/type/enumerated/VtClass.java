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

public class VtClass extends Enumerated {
    public static final VtClass defaultTerminal = new VtClass(0);
    public static final VtClass ansi_x3_64 = new VtClass(1);
    public static final VtClass dec_vt52 = new VtClass(2);
    public static final VtClass dec_vt100 = new VtClass(3);
    public static final VtClass dec_vt220 = new VtClass(4);
    public static final VtClass hp_700_94 = new VtClass(5);
    public static final VtClass ibm_3130 = new VtClass(6);

    public VtClass(int value) {
        super(value);
    }
    
    public VtClass(ByteQueue queue) {
        super(queue);
    }
}
