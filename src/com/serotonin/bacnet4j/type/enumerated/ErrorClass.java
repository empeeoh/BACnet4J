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

public class ErrorClass extends Enumerated {
    private static final long serialVersionUID = 1756743535333051828L;
    public static final ErrorClass device = new ErrorClass(0);
    public static final ErrorClass object = new ErrorClass(1);
    public static final ErrorClass property = new ErrorClass(2);
    public static final ErrorClass resources = new ErrorClass(3);
    public static final ErrorClass security = new ErrorClass(4);
    public static final ErrorClass services = new ErrorClass(5);
    public static final ErrorClass vt = new ErrorClass(6);
    public static final ErrorClass communication = new ErrorClass(7);

    public static final ErrorClass[] ALL = { device, object, property, resources, security, services, vt,
            communication, };

    public ErrorClass(int value) {
        super(value);
    }

    public ErrorClass(ByteQueue queue) {
        super(queue);
    }

    @Override
    public String toString() {
        int type = intValue();
        if (type == device.intValue())
            return "Device";
        if (type == object.intValue())
            return "Object";
        if (type == property.intValue())
            return "Property";
        if (type == resources.intValue())
            return "Resources";
        if (type == security.intValue())
            return "Security";
        if (type == services.intValue())
            return "Services";
        if (type == vt.intValue())
            return "VT";
        if (type == communication.intValue())
            return "Communication";
        return "Unknown: " + type;
    }
}
