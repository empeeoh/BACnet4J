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
package com.serotonin.bacnet4j.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SequenceDefinition implements Serializable {
    private static final long serialVersionUID = 6464244006575549887L;

    private final List<ElementSpecification> elements;

    public SequenceDefinition(ElementSpecification... specs) {
        elements = new ArrayList<SequenceDefinition.ElementSpecification>();
        for (ElementSpecification spec : specs)
            elements.add(spec);
    }

    public SequenceDefinition(List<ElementSpecification> elements) {
        this.elements = elements;
    }

    public List<ElementSpecification> getElements() {
        return elements;
    }

    public static class ElementSpecification {
        private final String id;
        private final Class<? extends Encodable> clazz;
        private final int contextId;
        private final boolean isSequenceOf;
        private final boolean isOptional;

        public ElementSpecification(final String id, 
        							final Class<? extends Encodable> clazz, 
        							final boolean isSequenceOf, 
        							final boolean isOptional) {
            this.id = id;
            this.clazz = clazz;
            this.contextId = -1;
            this.isSequenceOf = isSequenceOf;
            this.isOptional = isOptional;
        }

        public ElementSpecification(final String id, 
        							final Class<? extends Encodable> clazz, 
        							final int contextId, 
        							final boolean isSequenceOf,
        							final boolean isOptional) {
            this.id = id;
            this.clazz = clazz;
            this.contextId = contextId;
            this.isSequenceOf = isSequenceOf;
            this.isOptional = isOptional;
        }

        public String getId() {
            return id;
        }

        public Class<? extends Encodable> getClazz() {
            return clazz;
        }

        public int getContextId() {
            return contextId;
        }

        public boolean isOptional() {
            return isOptional;
        }

        public boolean isSequenceOf() {
            return isSequenceOf;
        }

        public boolean hasContextId() {
            return contextId != -1;
        }
    }
}
