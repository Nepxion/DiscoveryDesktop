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

public interface ConfigProcessor {
    void fromConfig(RuleEntity ruleEntity, TDataBox dataBox) throws Exception;

    String toConfig(RuleEntity ruleEntity, TDataBox dataBox);

    RuleEntity parseConfig(String config);

    String deparseConfig(RuleEntity ruleEntity);

    String getConfig(String group, String serviceId);

    String saveConfig(String group, String serviceId, String config);
}
