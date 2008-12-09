/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.bacnet4j.exception;

import com.serotonin.bacnet4j.type.constructed.BACnetError;

/**
 * @author Matthew Lohbihler
 */
public class PropertyValueException extends Exception {
    private static final long serialVersionUID = 1L;
    
    private final BACnetError error;

    public PropertyValueException(BACnetError error) {
        this.error = error;
    }

    public BACnetError getError() {
        return error;
    }
}
