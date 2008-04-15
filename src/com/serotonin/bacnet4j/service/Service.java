package com.serotonin.bacnet4j.service;

import com.serotonin.bacnet4j.type.constructed.BaseType;

abstract public class Service extends BaseType {
    abstract public byte getChoiceId();
}
