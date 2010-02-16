/*
    Copyright (C) 2006-2009 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.bacnet4j.obj;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetRuntimeException;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.enumerated.FileAccessMethod;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.io.StreamUtils;

/**
 * @author Matthew Lohbihler
 */
public class FileObject extends BACnetObject {
    /**
     * The actual file that this object represents.
     */
    private final File file;
    
    public FileObject(LocalDevice localDevice, ObjectIdentifier oid, File file, FileAccessMethod fileAccessMethod) {
        super(localDevice, oid);
        this.file = file;
        
        if (file.isDirectory())
            throw new BACnetRuntimeException("File is a directory");
        
        updateProperties();
        
        try {
            setProperty(PropertyIdentifier.fileAccessMethod, fileAccessMethod);
        }
        catch (BACnetServiceException e) {
            // Should never happen, but wrap in an unchecked just in case.
            throw new BACnetRuntimeException(e);
        }
    }
    
    public void updateProperties() {
        try {
            // TODO this is only a snapshot. Property read methods need to be overridden to report real time values.
            setProperty(PropertyIdentifier.fileSize, new UnsignedInteger(new BigInteger(Long.toString(length()))));
            setProperty(PropertyIdentifier.modificationDate, new DateTime(file.lastModified()));
            setProperty(PropertyIdentifier.readOnly, new Boolean(!file.canWrite()));
        }
        catch (BACnetServiceException e) {
            // Should never happen, but wrap in an unchecked just in case.
            throw new BACnetRuntimeException(e);
        }
    }
    
    public long length() {
        return file.length();
    }
    
    public OctetString readData(int start, int length) throws IOException {
        FileInputStream in = new FileInputStream(file);
        try {
            in.skip(start);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            StreamUtils.transfer(in, out, length);
            return new OctetString(out.toByteArray());
        }
        finally {
            in.close();
        }
    }
//    
//    public SequenceOf<OctetString> readRecords(int start, int length) throws IOException {
//        
//    }
}
