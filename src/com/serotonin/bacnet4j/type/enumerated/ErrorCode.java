package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class ErrorCode extends Enumerated {
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
    

    public ErrorCode(int value) {
        super(value);
    }
    
    public ErrorCode(ByteQueue queue) {
        super(queue);
    }
    
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
        return "Unknown: "+ type;
    }
}
