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

import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;

public enum StrategyType {
    VERSION(DiscoveryConstant.VERSION, 0),
    REGION(DiscoveryConstant.REGION, 0),
    ID_BLACKLIST(DiscoveryConstant.ID_BLACKLIST, 1),
    ADDRESS_BLACKLIST(DiscoveryConstant.ADDRESS_BLACKLIST, 1);

    private String value;
    private int category;

    private StrategyType(String value, int category) {
        this.value = value;
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public String getCapitalizeValue() {
        return StringUtils.capitalize(value);
    }

    public int getCategory() {
        return category;
    }

    public String getName() {
        return ConsoleLocaleFactory.getString(value);
    }

    public String getDescription() {
        return ConsoleLocaleFactory.getString(value + "_strategy");
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