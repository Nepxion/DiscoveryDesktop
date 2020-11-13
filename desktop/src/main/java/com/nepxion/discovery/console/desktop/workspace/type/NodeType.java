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

public enum NodeType {
    PORTAL("portal"),
    BLUE("blue"),
    GREEN("green"),
    BASIC("basic"),
    STABLE("stable"),
    GRAY("gray"),
    UNDEFINED("undefined");

    private String value;

    private NodeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return ConsoleLocaleFactory.getString(value + "_type");
    }

    public static NodeType fromString(String value) {
        for (NodeType type : NodeType.values()) {
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