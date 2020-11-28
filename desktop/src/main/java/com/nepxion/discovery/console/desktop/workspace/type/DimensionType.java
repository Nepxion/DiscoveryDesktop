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

public enum DimensionType {
    VERSION(DiscoveryConstant.VERSION, "V"),
    REGION(DiscoveryConstant.REGION, "R"),
    ENVIRONMENT(DiscoveryConstant.ENVIRONMENT, "E"),
    ZONE(DiscoveryConstant.ZONE, "Z"),
    ADDRESS(DiscoveryConstant.ADDRESS, "H"),
    GROUP(DiscoveryConstant.GROUP, "G");

    private String value;
    private String key;

    private DimensionType(String value, String key) {
        this.value = value;
        this.key = key;
    }

    public String getValue() {
        return value;
    }
    
    public String getKey() {
        return key;
    }

    public static DimensionType fromString(String value) {
        for (DimensionType type : DimensionType.values()) {
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