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
package com.serotonin.bacnet4j.type.constructed;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.SequenceDefinition;
import com.serotonin.bacnet4j.type.SequenceDefinition.ElementSpecification;
import org.free.bacnet4j.util.ByteQueue;

public class Sequence extends BaseType {
    private static final long serialVersionUID = -6678747788242195805L;

    private final SequenceDefinition definition;
    private final Map<String, Encodable> values;

    public Sequence(SequenceDefinition definition, Map<String, Encodable> values) {
        this.definition = definition;
        this.values = values;
    }


    public Sequence(SequenceDefinition definition, 
    				ByteQueue queue, int contextId) throws BACnetException {
        this(definition, popStart0(queue, contextId));
        Encodable.popEnd(queue, contextId);
    }

    // The constructor call must be the first statement in the constructor (a nuisance of a rule), so this static 
    // method is required as a workaround. Ugly, but it works.
    private static ByteQueue popStart0(ByteQueue queue, int contextId) throws BACnetException {
        Encodable.popStart(queue, contextId);
        return queue;
    }

    public Sequence(SequenceDefinition definition, ByteQueue queue) throws BACnetException {
        this.definition = definition;
        values = new HashMap<String, Encodable>();
        for (final ElementSpecification spec: definition.getElements()) {
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
