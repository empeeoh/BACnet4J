/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2009 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 */
package com.serotonin.bacnet4j.exception;

import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;

public class BACnetServiceException extends Exception {
    private static final long serialVersionUID = -1;
    
    private final ErrorClass errorClass;
    private final ErrorCode errorCode;
    
    public BACnetServiceException(ErrorClass errorClass, ErrorCode errorCode) {
        this.errorClass = errorClass;
        this.errorCode = errorCode;
    }

    public BACnetServiceException(ErrorClass errorClass, ErrorCode errorCode, String message) {
        super(message);
        this.errorClass = errorClass;
        this.errorCode = errorCode;
    }

    public ErrorClass getErrorClass() {
        return errorClass;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
    
    public boolean equals(ErrorClass errorClass, ErrorCode errorCode) {
        return this.errorClass.equals(errorClass) && this.errorCode.equals(errorCode);
    }

    @Override
    public String getMessage() {
        String message = "class="+ errorClass +", code="+ errorCode;
        String userDesc = super.getMessage();
        if (userDesc != null)
            message += ", message="+ userDesc;
        return message;
    }
}
