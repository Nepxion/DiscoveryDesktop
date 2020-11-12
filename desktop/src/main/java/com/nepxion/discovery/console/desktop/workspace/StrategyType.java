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
import com.nepxion.discovery.console.desktop.locale.ConsoleLocaleFactory;

public enum StrategyType {
    VERSION(DiscoveryConstant.VERSION),
    REGION(DiscoveryConstant.REGION),
    ADDRESS(DiscoveryConstant.ADDRESS);

    private String value;

    private StrategyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return ConsoleLocaleFactory.getString(value + "_strategy");
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