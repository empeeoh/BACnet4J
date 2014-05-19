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
package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import org.free.bacnet4j.util.ByteQueue;

public class ProgramError extends Enumerated {
    private static final long serialVersionUID = 4478176770591341682L;
    public static final ProgramError normal = new ProgramError(0);
    public static final ProgramError loadFailed = new ProgramError(1);
    public static final ProgramError internal = new ProgramError(2);
    public static final ProgramError program = new ProgramError(3);
    public static final ProgramError other = new ProgramError(4);

    public static final ProgramError[] ALL = { normal, loadFailed, internal, program, other, };

    public ProgramError(int value) {
        super(value);
    }

    public ProgramError(ByteQueue queue) {
        super(queue);
    }
}
