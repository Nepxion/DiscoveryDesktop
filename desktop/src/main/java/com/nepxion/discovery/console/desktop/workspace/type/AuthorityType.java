package com.nepxion.discovery.console.desktop.workspace.type;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;

public enum AuthorityType {
    STRATEGY("strategy"),
    REGISTER("register"),
    DISCOVERY("discovery");

    private String value;

    private AuthorityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getCapitalizeValue() {
        return StringUtils.capitalize(value);
    }

    public static AuthorityType fromString(String value) {
        for (AuthorityType type : AuthorityType.values()) {
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