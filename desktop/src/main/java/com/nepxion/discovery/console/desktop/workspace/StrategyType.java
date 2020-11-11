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

public enum StrategyType {
    VERSION(DiscoveryConstant.VERSION, "版本策略"),
    REGION(DiscoveryConstant.REGION, "区域策略"),
    ADDRESS(DiscoveryConstant.ADDRESS, "IP地址和端口策略");

    private String value;
    private String description;

    private StrategyType(String value, String description) {
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

    public static StrategyType fromString(String value) {
        for (StrategyType type : StrategyType.values()) {
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