package com.smaugslair.thitracker.util;

import com.smaugslair.thitracker.data.ThiEntity;

import java.util.StringJoiner;

public class NameValue {

    private final String name;
    private final Object value;

    public NameValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NameValue.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("value=" + value)
                .toString();
    }
}
