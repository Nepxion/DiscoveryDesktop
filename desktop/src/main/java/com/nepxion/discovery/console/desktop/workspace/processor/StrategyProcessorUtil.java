package com.nepxion.discovery.console.desktop.workspace.processor;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.common.entity.StrategyConditionBlueGreenEntity;
import com.nepxion.discovery.common.entity.StrategyConditionGrayEntity;
import com.nepxion.discovery.common.entity.StrategyCustomizationEntity;
import com.nepxion.discovery.common.entity.StrategyRouteEntity;
import com.nepxion.discovery.console.desktop.workspace.type.ReleaseType;
import com.nepxion.discovery.console.desktop.workspace.type.StrategyType;

public class StrategyProcessorUtil {
    public static ReleaseType getReleaseType(RuleEntity ruleEntity) {
        StrategyCustomizationEntity strategyCustomizationEntity = ruleEntity.getStrategyCustomizationEntity();
        if (strategyCustomizationEntity == null) {
            return null;
        }

        List<StrategyConditionBlueGreenEntity> strategyConditionBlueGreenEntityList = strategyCustomizationEntity.getStrategyConditionBlueGreenEntityList();
        if (CollectionUtils.isNotEmpty(strategyConditionBlueGreenEntityList)) {
            return ReleaseType.BLUE_GREEN;
        }

        List<StrategyConditionGrayEntity> strategyConditionGrayEntityList = strategyCustomizationEntity.getStrategyConditionGrayEntityList();
        if (CollectionUtils.isNotEmpty(strategyConditionGrayEntityList)) {
            return ReleaseType.GRAY;
        }

        return null;
    }

    @SuppressWarnings("incomplete-switch")
    public static StrategyType getStrategyType(RuleEntity ruleEntity) {
        StrategyCustomizationEntity strategyCustomizationEntity = ruleEntity.getStrategyCustomizationEntity();
        if (strategyCustomizationEntity == null) {
            return null;
        }

        List<StrategyRouteEntity> strategyRouteEntityList = strategyCustomizationEntity.getStrategyRouteEntityList();
        for (StrategyRouteEntity strategyRouteEntity : strategyRouteEntityList) {
            switch (strategyRouteEntity.getType()) {
                case VERSION:
                    return StrategyType.VERSION;
                case REGION:
                    return StrategyType.REGION;
            }
        }

        return null;
    }
}
