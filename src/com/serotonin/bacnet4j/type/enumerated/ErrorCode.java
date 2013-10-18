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
package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import org.free.bacnet4j.util.ByteQueue;

public class ErrorCode extends Enumerated {
    private static final long serialVersionUID = -6627023845995429296L;
    public static final ErrorCode other = new ErrorCode(0);
    public static final ErrorCode authenticationFailed = new ErrorCode(1);
    public static final ErrorCode configurationInProgress = new ErrorCode(2);
    public static final ErrorCode deviceBusy = new ErrorCode(3);
    public static final ErrorCode dynamicCreationNotSupported = new ErrorCode(4);
    public static final ErrorCode fileAccessDenied = new ErrorCode(5);
    public static final ErrorCode incompatibleSecurityLevels = new ErrorCode(6);
    public static final ErrorCode inconsistentParameters = new ErrorCode(7);
    public static final ErrorCode inconsistentSelectionCriterion = new ErrorCode(8);
    public static final ErrorCode invalidDataType = new ErrorCode(9);
    public static final ErrorCode invalidFileAccessMethod = new ErrorCode(10);
    public static final ErrorCode invalidFileStartPosition = new ErrorCode(11);
    public static final ErrorCode invalidOperatorName = new ErrorCode(12);
    public static final ErrorCode invalidParameterDataType = new ErrorCode(13);
    public static final ErrorCode invalidTimeStamp = new ErrorCode(14);
    public static final ErrorCode keyGenerationError = new ErrorCode(15);
    public static final ErrorCode missingRequiredParameter = new ErrorCode(16);
    public static final ErrorCode noObjectsOfSpecifiedType = new ErrorCode(17);
    public static final ErrorCode noSpaceForObject = new ErrorCode(18);
    public static final ErrorCode noSpaceToAddListElement = new ErrorCode(19);
    public static final ErrorCode noSpaceToWriteProperty = new ErrorCode(20);
    public static final ErrorCode noVtSessionsAvailable = new ErrorCode(21);
    public static final ErrorCode propertyIsNotAList = new ErrorCode(22);
    public static final ErrorCode objectDeletionNotPermitted = new ErrorCode(23);
    public static final ErrorCode objectIdentifierAlreadyExists = new ErrorCode(24);
    public static final ErrorCode operationalProblem = new ErrorCode(25);
    public static final ErrorCode passwordFailure = new ErrorCode(26);
    public static final ErrorCode readAccessDenied = new ErrorCode(27);
    public static final ErrorCode securityNotSupported = new ErrorCode(28);
    public static final ErrorCode serviceRequestDenied = new ErrorCode(29);
    public static final ErrorCode timeout = new ErrorCode(30);
    public static final ErrorCode unknownObject = new ErrorCode(31);
    public static final ErrorCode unknownProperty = new ErrorCode(32);
    public static final ErrorCode unknownVtClass = new ErrorCode(34);
    public static final ErrorCode unknownVtSession = new ErrorCode(35);
    public static final ErrorCode unsupportedObjectType = new ErrorCode(36);
    public static final ErrorCode valueOutOfRange = new ErrorCode(37);
    public static final ErrorCode vtSessionAlreadyClosed = new ErrorCode(38);
    public static final ErrorCode vtSessionTerminationFailure = new ErrorCode(39);
    public static final ErrorCode writeAccessDenied = new ErrorCode(40);
    public static final ErrorCode characterSetNotSupported = new ErrorCode(41);
    public static final ErrorCode invalidArrayIndex = new ErrorCode(42);
    public static final ErrorCode covSubscriptionFailed = new ErrorCode(43);
    public static final ErrorCode notCovProperty = new ErrorCode(44);
    public static final ErrorCode optionalFunctionalityNotSupported = new ErrorCode(45);
    public static final ErrorCode invalidConfigurationData = new ErrorCode(46);
    public static final ErrorCode datatypeNotSupported = new ErrorCode(47);
    public static final ErrorCode duplicateName = new ErrorCode(48);
    public static final ErrorCode duplicateObjectId = new ErrorCode(49);
    public static final ErrorCode propertyIsNotAnArray = new ErrorCode(50);
    public static final ErrorCode abortBufferOverflow = new ErrorCode(51);
    public static final ErrorCode abortInvalidApduInThisState = new ErrorCode(52);
    public static final ErrorCode abortPreemptedByHigherPriorityTask = new ErrorCode(53);
    public static final ErrorCode abortSegmentationNotSupported = new ErrorCode(54);
    public static final ErrorCode abortProprietary = new ErrorCode(55);
    public static final ErrorCode abortOther = new ErrorCode(56);
    public static final ErrorCode invalidTag = new ErrorCode(57);
    public static final ErrorCode networkDown = new ErrorCode(58);
    public static final ErrorCode rejectBufferOverflow = new ErrorCode(59);
    public static final ErrorCode rejectInconsistentParameters = new ErrorCode(60);
    public static final ErrorCode rejectInvalidParameterDataType = new ErrorCode(61);
    public static final ErrorCode rejectInvalidTag = new ErrorCode(62);
    public static final ErrorCode rejectMissingRequiredParameter = new ErrorCode(63);
    public static final ErrorCode rejectParameterOutOfRange = new ErrorCode(64);
    public static final ErrorCode rejectTooManyArguments = new ErrorCode(65);
    public static final ErrorCode rejectUndefinedEnumeration = new ErrorCode(66);
    public static final ErrorCode rejectUnrecognizedService = new ErrorCode(67);
    public static final ErrorCode rejectProprietary = new ErrorCode(68);
    public static final ErrorCode rejectOther = new ErrorCode(69);
    public static final ErrorCode unknownDevice = new ErrorCode(70);
    public static final ErrorCode unknownRoute = new ErrorCode(71);
    public static final ErrorCode valueNotInitialized = new ErrorCode(72);
    public static final ErrorCode invalidEventState = new ErrorCode(73);
    public static final ErrorCode noAlarmConfigured = new ErrorCode(74);
    public static final ErrorCode logBufferFull = new ErrorCode(75);
    public static final ErrorCode loggedValuePurged = new ErrorCode(76);
    public static final ErrorCode noPropertySpecified = new ErrorCode(77);
    public static final ErrorCode notConfiguredForTriggeredLogging = new ErrorCode(78);
    public static final ErrorCode communicationDisabled = new ErrorCode(83);
    public static final ErrorCode unknownFileSize = new ErrorCode(122);

    public static final ErrorCode[] ALL = { other, authenticationFailed, configurationInProgress, deviceBusy,
            dynamicCreationNotSupported, fileAccessDenied, incompatibleSecurityLevels, inconsistentParameters,
            inconsistentSelectionCriterion, invalidDataType, invalidFileAccessMethod, invalidFileStartPosition,
            invalidOperatorName, invalidParameterDataType, invalidTimeStamp, keyGenerationError,
            missingRequiredParameter, noObjectsOfSpecifiedType, noSpaceForObject, noSpaceToAddListElement,
            noSpaceToWriteProperty, noVtSessionsAvailable, propertyIsNotAList, objectDeletionNotPermitted,
            objectIdentifierAlreadyExists, operationalProblem, passwordFailure, readAccessDenied, securityNotSupported,
            serviceRequestDenied, timeout, unknownObject, unknownProperty, unknownVtClass, unknownVtSession,
            unsupportedObjectType, valueOutOfRange, vtSessionAlreadyClosed, vtSessionTerminationFailure,
            writeAccessDenied, characterSetNotSupported, invalidArrayIndex, covSubscriptionFailed, notCovProperty,
            optionalFunctionalityNotSupported, invalidConfigurationData, datatypeNotSupported, duplicateName,
            duplicateObjectId, propertyIsNotAnArray, abortBufferOverflow, abortInvalidApduInThisState,
            abortPreemptedByHigherPriorityTask, abortSegmentationNotSupported, abortProprietary, abortOther,
            invalidTag, networkDown, rejectBufferOverflow, rejectInconsistentParameters,
            rejectInvalidParameterDataType, rejectInvalidTag, rejectMissingRequiredParameter,
            rejectParameterOutOfRange, rejectTooManyArguments, rejectUndefinedEnumeration, rejectUnrecognizedService,
            rejectProprietary, rejectOther, unknownDevice, unknownRoute, valueNotInitialized, invalidEventState,
            noAlarmConfigured, logBufferFull, loggedValuePurged, noPropertySpecified, notConfiguredForTriggeredLogging,
            communicationDisabled, unknownFileSize, };

    public ErrorCode(int value) {
        super(value);
    }

    public ErrorCode(ByteQueue queue) {
        super(queue);
    }

    @Override
    public String toString() {
        int type = intValue();
        if (type == other.intValue())
            return "Other";
        if (type == authenticationFailed.intValue())
            return "Authentication failed";
        if (type == configurationInProgress.intValue())
            return "Configuration in progress";
        if (type == deviceBusy.intValue())
            return "Device busy";
        if (type == dynamicCreationNotSupported.intValue())
            return "Dynamic creation not supported";
        if (type == fileAccessDenied.intValue())
            return "File access denied";
        if (type == incompatibleSecurityLevels.intValue())
            return "Incompatible security levels";
        if (type == inconsistentParameters.intValue())
            return "Inconsistent parameters";
        if (type == inconsistentSelectionCriterion.intValue())
            return "Inconsistent selection criterion";
        if (type == invalidDataType.intValue())
            return "Invalid data type";
        if (type == invalidFileAccessMethod.intValue())
            return "Invalid file access method";
        if (type == invalidFileStartPosition.intValue())
            return "Invalid file start position";
        if (type == invalidOperatorName.intValue())
            return "Invalid operator name";
        if (type == invalidParameterDataType.intValue())
            return "Invalid parameter data type";
        if (type == invalidTimeStamp.intValue())
            return "Invalid time stamp";
        if (type == keyGenerationError.intValue())
            return "Key generation error";
        if (type == missingRequiredParameter.intValue())
            return "Missing required parameter";
        if (type == noObjectsOfSpecifiedType.intValue())
            return "No objects of specified type";
        if (type == noSpaceForObject.intValue())
            return "No space for object";
        if (type == noSpaceToAddListElement.intValue())
            return "No space to add list element";
        if (type == noSpaceToWriteProperty.intValue())
            return "No space to write property";
        if (type == noVtSessionsAvailable.intValue())
            return "No VT sessions available";
        if (type == propertyIsNotAList.intValue())
            return "Property is not a list";
        if (type == objectDeletionNotPermitted.intValue())
            return "Object deletion not permitted";
        if (type == objectIdentifierAlreadyExists.intValue())
            return "Object identifier already exists";
        if (type == operationalProblem.intValue())
            return "Operational problem";
        if (type == passwordFailure.intValue())
            return "Password failure";
        if (type == readAccessDenied.intValue())
            return "Read access denied";
        if (type == securityNotSupported.intValue())
            return "Security not supported";
        if (type == serviceRequestDenied.intValue())
            return "Service request denied";
        if (type == timeout.intValue())
            return "Timeout";
        if (type == unknownObject.intValue())
            return "Unknown object";
        if (type == unknownProperty.intValue())
            return "Unknown property";
        if (type == unknownVtClass.intValue())
            return "Unknown VT class";
        if (type == unknownVtSession.intValue())
            return "Unknown VT session";
        if (type == unsupportedObjectType.intValue())
            return "Unsupported object type";
        if (type == valueOutOfRange.intValue())
            return "Value out of range";
        if (type == vtSessionAlreadyClosed.intValue())
            return "VT session already closed";
        if (type == vtSessionTerminationFailure.intValue())
            return "VT session termination failure";
        if (type == writeAccessDenied.intValue())
            return "Write access denied";
        if (type == characterSetNotSupported.intValue())
            return "Character set not supported";
        if (type == invalidArrayIndex.intValue())
            return "Invalid array index";
        if (type == covSubscriptionFailed.intValue())
            return "Cov subscription failed";
        if (type == notCovProperty.intValue())
            return "Not COV property";
        if (type == optionalFunctionalityNotSupported.intValue())
            return "Optional functionality not supported";
        if (type == invalidConfigurationData.intValue())
            return "Invalid configuration data";
        if (type == datatypeNotSupported.intValue())
            return "Data type not supported";
        if (type == duplicateName.intValue())
            return "Duplicate name";
        if (type == duplicateObjectId.intValue())
            return "Duplicate object id";
        if (type == propertyIsNotAnArray.intValue())
            return "Property is not an array";
        if (type == abortBufferOverflow.intValue())
            return "Abort Buffer Overflow";
        if (type == abortInvalidApduInThisState.intValue())
            return "Abort Invalid Apdu In This State";
        if (type == abortPreemptedByHigherPriorityTask.intValue())
            return "Abort Preempted By Higher Priority Task";
        if (type == abortSegmentationNotSupported.intValue())
            return "Abort Segmentation Not Supported";
        if (type == abortProprietary.intValue())
            return "Abort Proprietary";
        if (type == abortOther.intValue())
            return "Abort Other";
        if (type == invalidTag.intValue())
            return "Invalid Tag";
        if (type == networkDown.intValue())
            return "Network Down";
        if (type == rejectBufferOverflow.intValue())
            return "Reject Buffer Overflow";
        if (type == rejectInconsistentParameters.intValue())
            return "Reject Inconsistent Parameters";
        if (type == rejectInvalidParameterDataType.intValue())
            return "Reject Invalid Parameter Data Type";
        if (type == rejectInvalidTag.intValue())
            return "Reject Invalid Tag";
        if (type == rejectMissingRequiredParameter.intValue())
            return "Reject Missing Required Parameter";
        if (type == rejectParameterOutOfRange.intValue())
            return "Reject Parameter Out Of Range";
        if (type == rejectTooManyArguments.intValue())
            return "Reject Too Many Arguments";
        if (type == rejectUndefinedEnumeration.intValue())
            return "Reject Undefined Enumeration";
        if (type == rejectUnrecognizedService.intValue())
            return "Reject Unrecognized Service";
        if (type == rejectProprietary.intValue())
            return "Reject Proprietary";
        if (type == rejectOther.intValue())
            return "Reject Other";
        if (type == unknownDevice.intValue())
            return "Unknown Device";
        if (type == unknownRoute.intValue())
            return "Unknown Route";
        if (type == valueNotInitialized.intValue())
            return "Value Not Initialized";
        if (type == invalidEventState.intValue())
            return "Invalid Event State";
        if (type == noAlarmConfigured.intValue())
            return "No Alarm Configured";
        if (type == logBufferFull.intValue())
            return "Log Buffer Full";
        if (type == loggedValuePurged.intValue())
            return "Logged Value Purged";
        if (type == noPropertySpecified.intValue())
            return "No Property Specified";
        if (type == notConfiguredForTriggeredLogging.intValue())
            return "Not Configured For Triggered Logging";
        if (type == communicationDisabled.intValue())
            return "Communication Disabled";
        if (type == unknownFileSize.intValue())
            return "Unknown File Size";
        return "Unknown: " + type;
    }
}
