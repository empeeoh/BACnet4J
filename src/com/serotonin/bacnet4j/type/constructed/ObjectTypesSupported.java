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

import com.serotonin.bacnet4j.type.primitive.BitString;
import org.free.bacnet4j.util.ByteQueue;

public class ObjectTypesSupported extends BitString {
    private static final long serialVersionUID = 4916909530588071979L;

    public ObjectTypesSupported() {
        super(new boolean[31]);
    }

    public ObjectTypesSupported(ByteQueue queue) {
        super(queue);
    }

    public boolean isAnalogInput() {
        return getValue()[0];
    }

    public void setAnalogInput(boolean analogInput) {
        getValue()[0] = analogInput;
    }

    public boolean isAnalogOutput() {
        return getValue()[1];
    }

    public void setAnalogOutput(boolean analogOutput) {
        getValue()[1] = analogOutput;
    }

    public boolean isAnalogValue() {
        return getValue()[2];
    }

    public void setAnalogValue(boolean analogValue) {
        getValue()[2] = analogValue;
    }

    public boolean isBinaryInput() {
        return getValue()[3];
    }

    public void setBinaryInput(boolean binaryInput) {
        getValue()[3] = binaryInput;
    }

    public boolean isBinaryOutput() {
        return getValue()[4];
    }

    public void setBinaryOutput(boolean binaryOutput) {
        getValue()[4] = binaryOutput;
    }

    public boolean isBinaryValue() {
        return getValue()[5];
    }

    public void setBinaryValue(boolean binaryValue) {
        getValue()[5] = binaryValue;
    }

    public boolean isCalendar() {
        return getValue()[6];
    }

    public void setCalendar(boolean calendar) {
        getValue()[6] = calendar;
    }

    public boolean isCommand() {
        return getValue()[7];
    }

    public void setCommand(boolean command) {
        getValue()[7] = command;
    }

    public boolean isDevice() {
        return getValue()[8];
    }

    public void setDevice(boolean device) {
        getValue()[8] = device;
    }

    public boolean isEventEnrollment() {
        return getValue()[9];
    }

    public void setEventEnrollment(boolean eventEnrollment) {
        getValue()[9] = eventEnrollment;
    }

    public boolean isFile() {
        return getValue()[10];
    }

    public void setFile(boolean file) {
        getValue()[10] = file;
    }

    public boolean isGroup() {
        return getValue()[11];
    }

    public void setGroup(boolean group) {
        getValue()[11] = group;
    }

    public boolean isLoop() {
        return getValue()[12];
    }

    public void setLoop(boolean loop) {
        getValue()[12] = loop;
    }

    public boolean isMultiStateInput() {
        return getValue()[13];
    }

    public void setMultiStateInput(boolean multiStateInput) {
        getValue()[13] = multiStateInput;
    }

    public boolean isMultiStateOutput() {
        return getValue()[14];
    }

    public void setMultiStateOutput(boolean multiStateOutput) {
        getValue()[14] = multiStateOutput;
    }

    public boolean isNotificationClass() {
        return getValue()[15];
    }

    public void setNotificationClass(boolean notificationClass) {
        getValue()[15] = notificationClass;
    }

    public boolean isProgram() {
        return getValue()[16];
    }

    public void setProgram(boolean program) {
        getValue()[16] = program;
    }

    public boolean isSchedule() {
        return getValue()[17];
    }

    public void setSchedule(boolean schedule) {
        getValue()[17] = schedule;
    }

    public boolean isAveraging() {
        return getValue()[18];
    }

    public void setAveraging(boolean averaging) {
        getValue()[18] = averaging;
    }

    public boolean isMultiStateValue() {
        return getValue()[19];
    }

    public void setMultiStateValue(boolean multiStateValue) {
        getValue()[19] = multiStateValue;
    }

    public boolean isTrendLog() {
        return getValue()[20];
    }

    public void setTrendLog(boolean trendLog) {
        getValue()[20] = trendLog;
    }

    public boolean isLifeSafetyPoint() {
        return getValue()[21];
    }

    public void setLifeSafetyPoint(boolean lifeSafetyPoint) {
        getValue()[21] = lifeSafetyPoint;
    }

    public boolean isLifeSafetyZone() {
        return getValue()[22];
    }

    public void setLifeSafetyZone(boolean lifeSafetyZone) {
        getValue()[22] = lifeSafetyZone;
    }

    public boolean isAccumulator() {
        return getValue()[23];
    }

    public void setAccumulator(boolean accumulator) {
        getValue()[23] = accumulator;
    }

    public boolean isPulseConverter() {
        return getValue()[24];
    }

    public void setPulseConverter(boolean pulseConverter) {
        getValue()[24] = pulseConverter;
    }

    public boolean isEventLog() {
        return getValue()[25];
    }

    public void setEventLog(boolean eventLog) {
        getValue()[25] = eventLog;
    }

    public boolean isTrendLogMultiple() {
        return getValue()[27];
    }

    public void setTrendLogMultiple(boolean trendLogMultiple) {
        getValue()[27] = trendLogMultiple;
    }

    public boolean isLoadControl() {
        return getValue()[28];
    }

    public void setLoadControl(boolean loadControl) {
        getValue()[28] = loadControl;
    }

    public boolean isStructuredView() {
        return getValue()[29];
    }

    public void setStructuredView(boolean structuredView) {
        getValue()[29] = structuredView;
    }

    public boolean isAccessDoor() {
        return getValue()[30];
    }

    public void setAccessDoor(boolean accessDoor) {
        getValue()[30] = accessDoor;
    }
}
