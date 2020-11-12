package com.nepxion.discovery.console.desktop.workspace;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.console.desktop.locale.ConsoleLocaleFactory;

public enum DeployType {
    DOMAIN_GATEWAY("domain-gateway"),
    NON_DOMAIN_GATEWAY("non-domain-gateway");

    private String value;

    private DeployType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return ConsoleLocaleFactory.getString(value + "_deploy");
    }

    public static DeployType fromString(String value) {
        for (DeployType type : DeployType.values()) {
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