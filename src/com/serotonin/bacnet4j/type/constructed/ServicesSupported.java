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

public class ServicesSupported extends BitString {
    private static final long serialVersionUID = -6528563197920379775L;

    public ServicesSupported() {
        super(new boolean[40]);
    }

    public ServicesSupported(ByteQueue queue) {
        super(queue);
    }

    public boolean isAcknowledgeAlarm() {
        return getValue()[0];
    }

    public void setAcknowledgeAlarm(boolean acknowledgeAlarm) {
        getValue()[0] = acknowledgeAlarm;
    }

    public boolean isConfirmedCovNotification() {
        return getValue()[1];
    }

    public void setConfirmedCovNotification(boolean confirmedCovNotification) {
        getValue()[1] = confirmedCovNotification;
    }

    public boolean isConfirmedEventNotification() {
        return getValue()[2];
    }

    public void setConfirmedEventNotification(boolean confirmedEventNotification) {
        getValue()[2] = confirmedEventNotification;
    }

    public boolean isGetAlarmSummary() {
        return getValue()[3];
    }

    public void setGetAlarmSummary(boolean getAlarmSummary) {
        getValue()[3] = getAlarmSummary;
    }

    public boolean isGetEnrollmentSummary() {
        return getValue()[4];
    }

    public void setGetEnrollmentSummary(boolean getEnrollmentSummary) {
        getValue()[4] = getEnrollmentSummary;
    }

    public boolean isSubscribeCov() {
        return getValue()[5];
    }

    public void setSubscribeCov(boolean subscribeCov) {
        getValue()[5] = subscribeCov;
    }

    public boolean isAtomicReadFile() {
        return getValue()[6];
    }

    public void setAtomicReadFile(boolean atomicReadFile) {
        getValue()[6] = atomicReadFile;
    }

    public boolean isAtomicWriteFile() {
        return getValue()[7];
    }

    public void setAtomicWriteFile(boolean atomicWriteFile) {
        getValue()[7] = atomicWriteFile;
    }

    public boolean isAddListElement() {
        return getValue()[8];
    }

    public void setAddListElement(boolean addListElement) {
        getValue()[8] = addListElement;
    }

    public boolean isRemoveListElement() {
        return getValue()[9];
    }

    public void setRemoveListElement(boolean removeListElement) {
        getValue()[9] = removeListElement;
    }

    public boolean isCreateObject() {
        return getValue()[10];
    }

    public void setCreateObject(boolean createObject) {
        getValue()[10] = createObject;
    }

    public boolean isDeleteObject() {
        return getValue()[11];
    }

    public void setDeleteObject(boolean deleteObject) {
        getValue()[11] = deleteObject;
    }

    public boolean isReadProperty() {
        return getValue()[12];
    }

    public void setReadProperty(boolean readProperty) {
        getValue()[12] = readProperty;
    }

    public boolean isReadPropertyConditional() {
        return getValue()[13];
    }

    public void setReadPropertyConditional(boolean readPropertyConditional) {
        getValue()[13] = readPropertyConditional;
    }

    public boolean isReadPropertyMultiple() {
        return getValue()[14];
    }

    public void setReadPropertyMultiple(boolean readPropertyMultiple) {
        getValue()[14] = readPropertyMultiple;
    }

    public boolean isWriteProperty() {
        return getValue()[15];
    }

    public void setWriteProperty(boolean writeProperty) {
        getValue()[15] = writeProperty;
    }

    public boolean isWritePropertyMultiple() {
        return getValue()[16];
    }

    public void setWritePropertyMultiple(boolean writePropertyMultiple) {
        getValue()[16] = writePropertyMultiple;
    }

    public boolean isDeviceCommunicationControl() {
        return getValue()[17];
    }

    public void setDeviceCommunicationControl(boolean deviceCommunicationControl) {
        getValue()[17] = deviceCommunicationControl;
    }

    public boolean isConfirmedPrivateTransfer() {
        return getValue()[18];
    }

    public void setConfirmedPrivateTransfer(boolean confirmedPrivateTransfer) {
        getValue()[18] = confirmedPrivateTransfer;
    }

    public boolean isConfirmedTextMessage() {
        return getValue()[19];
    }

    public void setConfirmedTextMessage(boolean confirmedTextMessage) {
        getValue()[19] = confirmedTextMessage;
    }

    public boolean isReinitializeDevice() {
        return getValue()[20];
    }

    public void setReinitializeDevice(boolean reinitializeDevice) {
        getValue()[20] = reinitializeDevice;
    }

    public boolean isVtOpen() {
        return getValue()[21];
    }

    public void setVtOpen(boolean vtOpen) {
        getValue()[21] = vtOpen;
    }

    public boolean isVtClose() {
        return getValue()[22];
    }

    public void setVtClose(boolean vtClose) {
        getValue()[22] = vtClose;
    }

    public boolean isVtData() {
        return getValue()[23];
    }

    public void setVtData(boolean vtData) {
        getValue()[23] = vtData;
    }

    public boolean isAuthenticate() {
        return getValue()[24];
    }

    public void setAuthenticate(boolean authenticate) {
        getValue()[24] = authenticate;
    }

    public boolean isRequestKey() {
        return getValue()[25];
    }

    public void setRequestKey(boolean requestKey) {
        getValue()[25] = requestKey;
    }

    public boolean isIAm() {
        return getValue()[26];
    }

    public void setIAm(boolean iAm) {
        getValue()[26] = iAm;
    }

    public boolean isIHave() {
        return getValue()[27];
    }

    public void setIHave(boolean iHave) {
        getValue()[27] = iHave;
    }

    public boolean isUnconfirmedCovNotification() {
        return getValue()[28];
    }

    public void setUnconfirmedCovNotification(boolean unconfirmedCovNotification) {
        getValue()[28] = unconfirmedCovNotification;
    }

    public boolean isUnconfirmedEventNotification() {
        return getValue()[29];
    }

    public void setUnconfirmedEventNotification(boolean unconfirmedEventNotification) {
        getValue()[29] = unconfirmedEventNotification;
    }

    public boolean isUnconfirmedPrivateTransfer() {
        return getValue()[30];
    }

    public void setUnconfirmedPrivateTransfer(boolean unconfirmedPrivateTransfer) {
        getValue()[30] = unconfirmedPrivateTransfer;
    }

    public boolean isUnconfirmedTextMessage() {
        return getValue()[31];
    }

    public void setUnconfirmedTextMessage(boolean unconfirmedTextMessage) {
        getValue()[31] = unconfirmedTextMessage;
    }

    public boolean isTimeSynchronization() {
        return getValue()[32];
    }

    public void setTimeSynchronization(boolean timeSynchronization) {
        getValue()[32] = timeSynchronization;
    }

    public boolean isWhoHas() {
        return getValue()[33];
    }

    public void setWhoHas(boolean whoHas) {
        getValue()[33] = whoHas;
    }

    public boolean isWhoIs() {
        return getValue()[34];
    }

    public void setWhoIs(boolean whoIs) {
        getValue()[34] = whoIs;
    }

    public boolean isReadRange() {
        return getValue()[35];
    }

    public void setReadRange(boolean readRange) {
        getValue()[36] = readRange;
    }

    public boolean isUtcTimeSynchronization() {
        return getValue()[36];
    }

    public void setUtcTimeSynchronization(boolean utcTimeSynchronization) {
        getValue()[36] = utcTimeSynchronization;
    }

    public boolean isLifeSafetyOperation() {
        return getValue()[37];
    }

    public void setLifeSafetyOperation(boolean lifeSafetyOperation) {
        getValue()[37] = lifeSafetyOperation;
    }

    public boolean isSubscribeCovProperty() {
        return getValue()[38];
    }

    public void setSubscribeCovProperty(boolean subscribeCovProperty) {
        getValue()[38] = subscribeCovProperty;
    }

    public boolean isGetEventInformation() {
        return getValue()[39];
    }

    public void setGetEventInformation(boolean getEventInformation) {
        getValue()[39] = getEventInformation;
    }
}
