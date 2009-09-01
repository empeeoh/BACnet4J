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
package com.serotonin.bacnet4j.type.constructed;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.SequenceDefinition;
import com.serotonin.bacnet4j.type.SequenceDefinition.ElementSpecification;
import com.serotonin.util.queue.ByteQueue;

public class Sequence extends BaseType {
    private final SequenceDefinition definition;
    private final Map<String, Encodable> values;
    
    public Sequence(SequenceDefinition definition, Map<String, Encodable> values) {
        this.definition = definition;
        this.values = values;
    }

    @Override
    public void write(ByteQueue queue) {
        List<ElementSpecification> specs = definition.getElements();
        for (ElementSpecification spec : specs) {
            if (spec.isOptional()) {
                if (spec.hasContextId())
                    writeOptional(queue, values.get(spec.getId()), spec.getContextId());
                else
                    writeOptional(queue, values.get(spec.getId()));
            }
            else {
                if (spec.hasContextId())
                    write(queue, values.get(spec.getId()), spec.getContextId());
                else
                    write(queue, values.get(spec.getId()));
            }
        }
    }
    
    public Sequence(SequenceDefinition definition, ByteQueue queue) throws BACnetException {
        this.definition = definition;
        values = new HashMap<String, Encodable>();
        List<ElementSpecification> specs = definition.getElements();
        for (int i=0; i<specs.size(); i++) {
            ElementSpecification spec = specs.get(i);
            if (spec.isSequenceOf()) {
                if (spec.isOptional())
                    values.put(spec.getId(), readOptionalSequenceOf(queue, spec.getClazz(), spec.getContextId()));
                else {
                    if (spec.hasContextId())
                        values.put(spec.getId(), readSequenceOf(queue, spec.getClazz(), spec.getContextId()));
                    else
                        values.put(spec.getId(), readSequenceOf(queue, spec.getClazz()));
                }
            }
            else if (spec.isOptional())
                values.put(spec.getId(), readOptional(queue, spec.getClazz(), spec.getContextId()));
            else if (spec.hasContextId())
                values.put(spec.getId(), read(queue, spec.getClazz(), spec.getContextId()));
            else
                values.put(spec.getId(), read(queue, spec.getClazz()));
        }
    }

    public SequenceDefinition getDefinition() {
        return definition;
    }

    public Map<String, Encodable> getValues() {
        return values;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((values == null) ? 0 : values.hashCode());
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
        final Sequence other = (Sequence) obj;
        if (values == null) {
            if (other.values != null)
                return false;
        }
        else if (!values.equals(other.values))
            return false;
        return true;
    }
}
