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
package com.serotonin.bacnet4j.service.confirmed;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.NotImplementedException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.AmbiguousValue;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.BaseType;
import com.serotonin.bacnet4j.type.constructed.PropertyReference;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class ReadPropertyConditionalRequest extends ConfirmedRequestService {
    private static final long serialVersionUID = 5502083051063390548L;

    public static final byte TYPE_ID = 13;

    private final ObjectSelectionCriteria objectSelectionCriteria;
    private final SequenceOf<PropertyReference> listOfPropertyReferences;

    public ReadPropertyConditionalRequest(ObjectSelectionCriteria objectSelectionCriteria,
            SequenceOf<PropertyReference> listOfPropertyReferences) {
        this.objectSelectionCriteria = objectSelectionCriteria;
        this.listOfPropertyReferences = listOfPropertyReferences;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public AcknowledgementService handle(LocalDevice localDevice, Address from, OctetString linkService)
            throws BACnetException {
        throw new NotImplementedException();
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, objectSelectionCriteria, 0);
        writeOptional(queue, listOfPropertyReferences, 1);
    }

    ReadPropertyConditionalRequest(ByteQueue queue) throws BACnetException {
        objectSelectionCriteria = read(queue, ObjectSelectionCriteria.class, 0);
        listOfPropertyReferences = readOptionalSequenceOf(queue, PropertyReference.class, 1);
    }

    public static class ObjectSelectionCriteria extends BaseType {
        private static final long serialVersionUID = 7736612332488174653L;
        private final SelectionLogic selectionLogic;
        private final SequenceOf<SelectionCriteria> listOfSelectionCriteria;

        public ObjectSelectionCriteria(SelectionLogic selectionLogic,
                SequenceOf<SelectionCriteria> listOfSelectionCriteria) {
            this.selectionLogic = selectionLogic;
            this.listOfSelectionCriteria = listOfSelectionCriteria;
        }

        @Override
        public void write(ByteQueue queue) {
            write(queue, selectionLogic, 0);
            writeOptional(queue, listOfSelectionCriteria, 1);
        }

        public ObjectSelectionCriteria(ByteQueue queue) throws BACnetException {
            selectionLogic = read(queue, SelectionLogic.class, 0);
            listOfSelectionCriteria = readOptionalSequenceOf(queue, SelectionCriteria.class, 1);
        }

        public static class SelectionLogic extends Enumerated {
            private static final long serialVersionUID = -2364163447254123848L;
            public static final SelectionLogic and = new SelectionLogic(0);
            public static final SelectionLogic or = new SelectionLogic(1);
            public static final SelectionLogic all = new SelectionLogic(2);

            private SelectionLogic(int value) {
                super(value);
            }

            public SelectionLogic(ByteQueue queue) {
                super(queue);
            }
        }

        public static class SelectionCriteria extends BaseType {
            private static final long serialVersionUID = 5715604510097038002L;
            private final PropertyIdentifier propertyIdentifier;
            private final UnsignedInteger propertyArrayIndex;
            private final RelationSpecifier relationSpecifier;
            private final Encodable comparisonValue;

            public SelectionCriteria(PropertyIdentifier propertyIdentifier, UnsignedInteger propertyArrayIndex,
                    RelationSpecifier relationSpecifier, Encodable comparisonValue) {
                this.propertyIdentifier = propertyIdentifier;
                this.propertyArrayIndex = propertyArrayIndex;
                this.relationSpecifier = relationSpecifier;
                this.comparisonValue = comparisonValue;
            }

            @Override
            public void write(ByteQueue queue) {
                write(queue, propertyIdentifier, 0);
                writeOptional(queue, propertyArrayIndex, 1);
                write(queue, relationSpecifier, 2);
                writeEncodable(queue, comparisonValue, 3);
            }

            public SelectionCriteria(ByteQueue queue) throws BACnetException {
                propertyIdentifier = read(queue, PropertyIdentifier.class, 0);
                propertyArrayIndex = readOptional(queue, UnsignedInteger.class, 1);
                relationSpecifier = read(queue, RelationSpecifier.class, 2);
                comparisonValue = new AmbiguousValue(queue, 3);
            }

            public static class RelationSpecifier extends Enumerated {
                private static final long serialVersionUID = -1;
                public static final RelationSpecifier equal = new RelationSpecifier(0);
                public static final RelationSpecifier notEqual = new RelationSpecifier(1);
                public static final RelationSpecifier lessThan = new RelationSpecifier(2);
                public static final RelationSpecifier greaterThan = new RelationSpecifier(3);
                public static final RelationSpecifier lessThanOrEqual = new RelationSpecifier(4);
                public static final RelationSpecifier greaterThanOrEqual = new RelationSpecifier(5);

                private RelationSpecifier(int value) {
                    super(value);
                }

                public RelationSpecifier(ByteQueue queue) {
                    super(queue);
                }
            }

            @Override
            public int hashCode() {
                final int PRIME = 31;
                int result = 1;
                result = PRIME * result + ((comparisonValue == null) ? 0 : comparisonValue.hashCode());
                result = PRIME * result + ((propertyArrayIndex == null) ? 0 : propertyArrayIndex.hashCode());
                result = PRIME * result + ((propertyIdentifier == null) ? 0 : propertyIdentifier.hashCode());
                result = PRIME * result + ((relationSpecifier == null) ? 0 : relationSpecifier.hashCode());
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
                final SelectionCriteria other = (SelectionCriteria) obj;
                if (comparisonValue == null) {
                    if (other.comparisonValue != null)
                        return false;
                }
                else if (!comparisonValue.equals(other.comparisonValue))
                    return false;
                if (propertyArrayIndex == null) {
                    if (other.propertyArrayIndex != null)
                        return false;
                }
                else if (!propertyArrayIndex.equals(other.propertyArrayIndex))
                    return false;
                if (propertyIdentifier == null) {
                    if (other.propertyIdentifier != null)
                        return false;
                }
                else if (!propertyIdentifier.equals(other.propertyIdentifier))
                    return false;
                if (relationSpecifier == null) {
                    if (other.relationSpecifier != null)
                        return false;
                }
                else if (!relationSpecifier.equals(other.relationSpecifier))
                    return false;
                return true;
            }
        }

        @Override
        public int hashCode() {
            final int PRIME = 31;
            int result = 1;
            result = PRIME * result + ((listOfSelectionCriteria == null) ? 0 : listOfSelectionCriteria.hashCode());
            result = PRIME * result + ((selectionLogic == null) ? 0 : selectionLogic.hashCode());
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
            final ObjectSelectionCriteria other = (ObjectSelectionCriteria) obj;
            if (listOfSelectionCriteria == null) {
                if (other.listOfSelectionCriteria != null)
                    return false;
            }
            else if (!listOfSelectionCriteria.equals(other.listOfSelectionCriteria))
                return false;
            if (selectionLogic == null) {
                if (other.selectionLogic != null)
                    return false;
            }
            else if (!selectionLogic.equals(other.selectionLogic))
                return false;
            return true;
        }
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((listOfPropertyReferences == null) ? 0 : listOfPropertyReferences.hashCode());
        result = PRIME * result + ((objectSelectionCriteria == null) ? 0 : objectSelectionCriteria.hashCode());
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
        final ReadPropertyConditionalRequest other = (ReadPropertyConditionalRequest) obj;
        if (listOfPropertyReferences == null) {
            if (other.listOfPropertyReferences != null)
                return false;
        }
        else if (!listOfPropertyReferences.equals(other.listOfPropertyReferences))
            return false;
        if (objectSelectionCriteria == null) {
            if (other.objectSelectionCriteria != null)
                return false;
        }
        else if (!objectSelectionCriteria.equals(other.objectSelectionCriteria))
            return false;
        return true;
    }
}
