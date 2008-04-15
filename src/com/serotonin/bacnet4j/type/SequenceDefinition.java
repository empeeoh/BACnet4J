package com.serotonin.bacnet4j.type;

import java.util.List;

public class SequenceDefinition {
    private List<ElementSpecification> elements;

    public SequenceDefinition(List<ElementSpecification> elements) {
        this.elements = elements;
    }

    public List<ElementSpecification> getElements() {
        return elements;
    }
    
    public static class ElementSpecification {
        private String id;
        private Class<? extends Encodable> clazz;
        private int contextId;
        private boolean sequenceOf;
        private boolean optional;
        
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
