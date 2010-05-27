package com.serotonin.bacnet4j.type;

import java.io.Serializable;
import java.util.List;

public class SequenceDefinition implements Serializable {
    private static final long serialVersionUID = 6464244006575549887L;

    private final List<ElementSpecification> elements;

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
        private final boolean sequenceOf;
        private final boolean optional;

        public ElementSpecification(String id, Class<? extends Encodable> clazz, boolean sequenceOf, boolean optional) {
            this.id = id;
            this.clazz = clazz;
            this.contextId = -1;
            this.sequenceOf = sequenceOf;
            this.optional = optional;
        }

        public ElementSpecification(String id, Class<? extends Encodable> clazz, int contextId, boolean sequenceOf,
                boolean optional) {
            this.id = id;
            this.clazz = clazz;
            this.contextId = contextId;
            this.sequenceOf = sequenceOf;
            this.optional = optional;
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
            return optional;
        }

        public boolean isSequenceOf() {
            return sequenceOf;
        }

        public boolean hasContextId() {
            return contextId != -1;
        }
    }
}
