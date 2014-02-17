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
package com.serotonin.bacnet4j.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.serotonin.bacnet4j.type.constructed.PropertyReference;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

public class PropertyReferences implements Serializable {
    private static final long serialVersionUID = -1512876955215003611L;

    private final Map<ObjectIdentifier, List<PropertyReference>> properties = new LinkedHashMap<ObjectIdentifier, List<PropertyReference>>();

    public void add(ObjectIdentifier oid, PropertyReference ref) {
        List<PropertyReference> refs = properties.get(oid);
        if (refs == null) {
            refs = new ArrayList<PropertyReference>();
            properties.put(oid, refs);
        }
        refs.add(ref);//TODO: should this list be a set? can we have dups? does order matter?
    }

    public void add(ObjectIdentifier oid, PropertyIdentifier pid) {
        add(oid, new PropertyReference(pid));
    }

    public Map<ObjectIdentifier, List<PropertyReference>> getProperties() {
        return properties;
    }

    public List<PropertyReferences> getPropertiesPartitioned(int maxPartitionSize) {
        List<PropertyReferences> partitions = new ArrayList<PropertyReferences>();

        if (size() <= maxPartitionSize)
            partitions.add(this);
        else {
            PropertyReferences partition = null;
            for (ObjectIdentifier oid : properties.keySet()) {
            	final List<PropertyReference> refs = properties.get(oid);
                for (PropertyReference ref : refs) {
                    if (partition == null || partition.size() >= maxPartitionSize) {
                        partition = new PropertyReferences();
                        partitions.add(partition);
                    }
                    partition.add(oid, ref);
                }
            }
        }

        return partitions;
    }

    public int size() {
        int size = 0;
        for (ObjectIdentifier oid : properties.keySet())
            size += properties.get(oid).size();
        return size;
    }
}
