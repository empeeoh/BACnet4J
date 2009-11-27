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

import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.error.BaseError;

public class BACnetErrorException extends BACnetException {
    private static final long serialVersionUID = -1;
    
    private final BaseError error;
    
    public BACnetErrorException(byte choice, ErrorClass errorClass, ErrorCode errorCode) {
        super(errorClass.toString() +": "+ errorCode.toString());
        error = new BaseError(choice, new BACnetError(errorClass, errorCode));
    }
    
    public BACnetErrorException(byte choice, BACnetServiceException e) {
        super(e);
        error = new BaseError(choice, new BACnetError(e.getErrorClass(), e.getErrorCode()));
    }
    
    public BACnetErrorException(ErrorClass errorClass, ErrorCode errorCode) {
        super(errorClass.toString() +": "+ errorCode.toString());
        error = new BaseError((byte)127, new BACnetError(errorClass, errorCode));
    }
    
    public BACnetErrorException(BACnetServiceException e) {
        super(e.getMessage());
        error = new BaseError((byte)127, new BACnetError(e.getErrorClass(), e.getErrorCode()));
    }
    
    public BACnetErrorException(ErrorClass errorClass, ErrorCode errorCode, String message) {
        super(message);
        error = new BaseError((byte)127, new BACnetError(errorClass, errorCode));
    }
    
    public BACnetErrorException(BaseError error) {
        this.error = error;
    }

    public BaseError getError() {
        return error;
    }
}
