package com.nepxion.discovery.console.desktop.workspace.processor;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import twaver.TDataBox;

import com.nepxion.discovery.console.desktop.workspace.type.StrategyType;

public interface StrategyProcessor {
    TDataBox fromConfig();

    String toConfig(StrategyType strategyType, TDataBox dataBox);

    void saveConfig(String group, String serviceId, String config);
}
