package com.nepxion.discovery.console.desktop.workspace;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;

import com.nepxion.discovery.common.constant.DiscoveryConstant;

public enum ReleaseType {
    BLUE_GREEN(DiscoveryConstant.BLUE_GREEN, "全链路蓝绿发布"),
    GRAY(DiscoveryConstant.GRAY, "全链路灰度发布"),
    BLACKLIST(DiscoveryConstant.BLACKLIST, "全链路实例摘除");

    private String value;
    private String description;

    private ReleaseType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String getLabel() {
        return StringUtils.capitalize(value);
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