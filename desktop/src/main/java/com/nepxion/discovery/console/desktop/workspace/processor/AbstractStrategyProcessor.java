package com.nepxion.discovery.console.desktop.workspace.processor;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.common.entity.RuleEntity;

public abstract class AbstractStrategyProcessor implements StrategyProcessor {
    @Override
    public RuleEntity parseConfig(String config) {
        return StrategyProcessorFactory.getXmlConfigParser().parse(config);
    }

    @Override
    public String deparseConfig(RuleEntity ruleEntity) {
        return StrategyProcessorFactory.getXmlConfigDeparser().deparse(ruleEntity);
    }

    @Override
    public void saveConfig(String group, String serviceId, String config) {

    }
}