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

import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.console.desktop.workspace.type.StrategyType;

public class BlacklistStrategyProcessor extends AbstractStrategyProcessor {
    @Override
    public void fromConfig(RuleEntity ruleEntity, StrategyType strategyType, TDataBox dataBox) throws Exception {

    }

    @Override
    public String toConfig(RuleEntity ruleEntity, StrategyType strategyType, TDataBox dataBox) {
        return null;
    }
}