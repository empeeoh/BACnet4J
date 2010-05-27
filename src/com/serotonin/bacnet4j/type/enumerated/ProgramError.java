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

public class ProgramError extends Enumerated {
    private static final long serialVersionUID = 4478176770591341682L;
    public static final ProgramError normal = new ProgramError(0);
    public static final ProgramError loadFailed = new ProgramError(1);
    public static final ProgramError internal = new ProgramError(2);
    public static final ProgramError program = new ProgramError(3);
    public static final ProgramError other = new ProgramError(4);

    public ProgramError(int value) {
        super(value);
    }

    public ProgramError(ByteQueue queue) {
        super(queue);
    }
}
