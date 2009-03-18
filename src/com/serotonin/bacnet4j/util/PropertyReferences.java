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
package com.serotonin.bacnet4j.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.serotonin.bacnet4j.type.constructed.PropertyReference;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

public class PropertyReferences {
    private final Map<ObjectIdentifier, List<PropertyReference>> properties = 
            new HashMap<ObjectIdentifier, List<PropertyReference>>();
    
    public void add(ObjectIdentifier oid, PropertyReference ref) {
        List<PropertyReference> refs = properties.get(oid);
        if (refs == null) {
            refs = new ArrayList<PropertyReference>();
            properties.put(oid, refs);
        }
        refs.add(ref);
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
            List<PropertyReference> refs;
            for (ObjectIdentifier oid : properties.keySet()) {
                refs = properties.get(oid);
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
