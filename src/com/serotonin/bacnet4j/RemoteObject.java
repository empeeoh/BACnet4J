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
 * When signing a commercial license the following extension to GPL is made.
 * A special exception to the GPL is included to allow you to distribute a
 * combined work that includes BAcnet4J without being obliged to provide
 * the source code for any proprietary components.
 */
package com.serotonin.bacnet4j;

import java.io.Serializable;

import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

public class RemoteObject implements Serializable {
    private static final long serialVersionUID = 2962046198697775365L;
    private final ObjectIdentifier oid;
    private String objectName;

    public RemoteObject(ObjectIdentifier oid) {
        this.oid = oid;
    }

    public ObjectIdentifier getObjectIdentifier() {
        return oid;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectName() {
        return objectName;
    }

    @Override
    public String toString() {
        return oid.toString();
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((oid == null) ? 0 : oid.hashCode());
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
        final RemoteObject other = (RemoteObject) obj;
        if (oid == null) {
            if (other.oid != null)
                return false;
        }
        else if (!oid.equals(other.oid))
            return false;
        return true;
    }
}
