package com.nepxion.discovery.console.desktop.workspace.type;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;

public enum LinkType {
    BLUE("blue"),
    GREEN("green"),
    BASIC("basic"),
    GRAY("gray"),
    STABLE("stable"),
    UNDEFINED("undefined");

    private String value;

    private LinkType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return ConsoleLocaleFactory.getString(value + "_type");
    }

    public static LinkType fromString(String value) {
        for (LinkType type : LinkType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("No matched type with value=" + value);
    }

    @Override
    public String toString() {
        return value;
    }
}