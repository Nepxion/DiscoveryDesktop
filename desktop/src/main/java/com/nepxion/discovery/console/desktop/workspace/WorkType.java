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

public enum WorkType {
    BLUE_GREEN(DiscoveryConstant.BLUE_GREEN, "蓝绿部署"),
    GRAY(DiscoveryConstant.GRAY, "灰度发布"),
    BLACKLIST(DiscoveryConstant.BLACKLIST, "实例摘除");

    private String value;
    private String description;

    private WorkType(String value, String description) {
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

    public static WorkType fromString(String value) {
        for (WorkType type : WorkType.values()) {
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