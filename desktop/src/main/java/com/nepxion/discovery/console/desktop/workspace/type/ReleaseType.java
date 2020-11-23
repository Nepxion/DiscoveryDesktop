package com.nepxion.discovery.console.desktop.workspace.type;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.common.constant.DiscoveryConstant;

public enum ReleaseType {
    BLUE_GREEN(DiscoveryConstant.BLUE_GREEN),
    GRAY(DiscoveryConstant.GRAY),
    BLACKLIST(DiscoveryConstant.BLACKLIST),
    DATABASE_BLUE_GREEN("database-blue-green"),
    MESSAGE_QUEUE_BLUE_GREEN("message-queue-blue-green");

    private String value;

    private ReleaseType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ReleaseType fromString(String value) {
        for (ReleaseType type : ReleaseType.values()) {
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