package com.serotonin.bacnet4j.service.acknowledgement;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.BaseType;
import com.serotonin.bacnet4j.type.constructed.EventTransitionBits;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.util.queue.ByteQueue;

public class GetAlarmSummaryAck extends AcknowledgementService {
    public static final byte TYPE_ID = 3;
    
    private SequenceOf<AlarmSummary> values;
    
    public GetAlarmSummaryAck(SequenceOf<AlarmSummary> values) {
        this.values = values;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, values);
    }
    
    GetAlarmSummaryAck(ByteQueue queue) throws BACnetException {
        values = readSequenceOf(queue, AlarmSummary.class);
    }
    
    public static class AlarmSummary extends BaseType {
        private ObjectIdentifier objectIdentifier;
        private EventState alarmState;
        private EventTransitionBits acknowledgedTransitions;
        
        public AlarmSummary(ObjectIdentifier objectIdentifier, EventState alarmState,
                EventTransitionBits acknowledgedTransitions) {
            this.objectIdentifier = objectIdentifier;
            this.alarmState = alarmState;
            this.acknowledgedTransitions = acknowledgedTransitions;
        }

        @Override
        public void write(ByteQueue queue) {
            objectIdentifier.write(queue);
            alarmState.write(queue);
            acknowledgedTransitions.write(queue);
        }
        
        public AlarmSummary(ByteQueue queue) throws BACnetException {
            objectIdentifier = read(queue, ObjectIdentifier.class);
            alarmState = read(queue, EventState.class);
            acknowledgedTransitions = read(queue, EventTransitionBits.class);
        }

        @Override
        public int hashCode() {
            final int PRIME = 31;
            int result = 1;
            result = PRIME * result + ((acknowledgedTransitions == null) ? 0 : acknowledgedTransitions.hashCode());
            result = PRIME * result + ((alarmState == null) ? 0 : alarmState.hashCode());
            result = PRIME * result + ((objectIdentifier == null) ? 0 : objectIdentifier.hashCode());
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
            final AlarmSummary other = (AlarmSummary) obj;
            if (acknowledgedTransitions == null) {
                if (other.acknowledgedTransitions != null)
                    return false;
            }
            else if (!acknowledgedTransitions.equals(other.acknowledgedTransitions))
                return false;
            if (alarmState == null) {
                if (other.alarmState != null)
                    return false;
            }
            else if (!alarmState.equals(other.alarmState))
                return false;
            if (objectIdentifier == null) {
                if (other.objectIdentifier != null)
                    return false;
            }
            else if (!objectIdentifier.equals(other.objectIdentifier))
                return false;
            return true;
        }
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
        final GetAlarmSummaryAck other = (GetAlarmSummaryAck) obj;
        if (values == null) {
            if (other.values != null)
                return false;
        }
        else if (!values.equals(other.values))
            return false;
        return true;
    }
}
