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
package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class BackupState extends Enumerated {
    private static final long serialVersionUID = -4811672152182130623L;
    public static final BackupState idle = new BackupState(0);
    public static final BackupState preparingForBackup = new BackupState(1);
    public static final BackupState preparingForRestore = new BackupState(2);
    public static final BackupState performingABackup = new BackupState(3);
    public static final BackupState performingARestore = new BackupState(4);
    public static final BackupState backupFailure = new BackupState(5);
    public static final BackupState restoreFailure = new BackupState(6);

    public BackupState(int value) {
        super(value);
    }

    public BackupState(ByteQueue queue) {
        super(queue);
    }
}
