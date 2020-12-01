package com.nepxion.discovery.console.desktop.workspace.processor.strategy;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.console.desktop.workspace.processor.ConfigProcessor;
import com.nepxion.discovery.console.desktop.workspace.type.StrategyType;

public interface StrategyConfigProcessor extends ConfigProcessor {
    StrategyType getStrategyType();

    void setStrategyType(StrategyType strategyType);
}
