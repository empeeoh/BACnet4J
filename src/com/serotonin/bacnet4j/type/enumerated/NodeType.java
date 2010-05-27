/*
    Copyright (C) 2006-2009 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

/**
 * @author Matthew Lohbihler
 */
public class NodeType extends Enumerated {
    private static final long serialVersionUID = -1462203629019212150L;
    public static final NodeType unknown = new NodeType(0);
    public static final NodeType system = new NodeType(1);
    public static final NodeType network = new NodeType(2);
    public static final NodeType device = new NodeType(3);
    public static final NodeType organizational = new NodeType(4);
    public static final NodeType area = new NodeType(5);
    public static final NodeType equipment = new NodeType(6);
    public static final NodeType point = new NodeType(7);
    public static final NodeType collection = new NodeType(8);
    public static final NodeType property = new NodeType(9);
    public static final NodeType functional = new NodeType(10);
    public static final NodeType other = new NodeType(11);

    public NodeType(int value) {
        super(value);
    }

    public NodeType(ByteQueue queue) {
        super(queue);
    }
}
