package com.nepxion.discovery.console.desktop.workspace.type;

import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;

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