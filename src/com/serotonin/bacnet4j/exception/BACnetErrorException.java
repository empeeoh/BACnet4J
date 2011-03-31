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
package com.serotonin.bacnet4j.exception;

import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.error.BaseError;

public class BACnetErrorException extends BACnetException {
    private static final long serialVersionUID = -1;

    private final BaseError error;

    public BACnetErrorException(byte choice, ErrorClass errorClass, ErrorCode errorCode) {
        super(getBaseMessage(errorClass, errorCode, null));
        error = new BaseError(choice, new BACnetError(errorClass, errorCode));
    }

    public BACnetErrorException(byte choice, BACnetServiceException e) {
        super(e);
        error = new BaseError(choice, new BACnetError(e.getErrorClass(), e.getErrorCode()));
    }

    public BACnetErrorException(ErrorClass errorClass, ErrorCode errorCode) {
        super(getBaseMessage(errorClass, errorCode, null));
        error = new BaseError((byte) 127, new BACnetError(errorClass, errorCode));
    }

    public BACnetErrorException(BACnetServiceException e) {
        super(e.getMessage());
        error = new BaseError((byte) 127, new BACnetError(e.getErrorClass(), e.getErrorCode()));
    }

    public BACnetErrorException(ErrorClass errorClass, ErrorCode errorCode, String message) {
        super(getBaseMessage(errorClass, errorCode, message));
        error = new BaseError((byte) 127, new BACnetError(errorClass, errorCode));
    }

    public BACnetErrorException(BaseError error) {
        this.error = error;
    }

    public BaseError getError() {
        return error;
    }

    private static String getBaseMessage(ErrorClass errorClass, ErrorCode errorCode, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(errorClass.toString());
        sb.append(": ");
        sb.append(errorCode.toString());
        if (message != null)
            sb.append(" '").append(message).append("'");
        return sb.toString();
    }
}
