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
import org.apache.commons.lang3.StringUtils;

import com.nepxion.discovery.common.entity.BlueGreenRouteType;
import com.nepxion.discovery.common.entity.ElementType;
import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.common.entity.StrategyConditionBlueGreenEntity;
import com.nepxion.discovery.common.entity.StrategyConditionGrayEntity;
import com.nepxion.discovery.common.entity.StrategyReleaseEntity;
import com.nepxion.discovery.common.entity.StrategyRouteEntity;
import com.nepxion.discovery.console.desktop.workspace.type.ReleaseType;
import com.nepxion.discovery.console.desktop.workspace.type.StrategyType;
import com.nepxion.discovery.plugin.framework.parser.xml.XmlConfigConstant;

public class ReleaseProcessorUtil {
    public static ReleaseType getReleaseType(RuleEntity ruleEntity) {
        StrategyReleaseEntity strategyReleaseEntity = ruleEntity.getStrategyReleaseEntity();
        if (strategyReleaseEntity == null) {
            return null;
        }

        List<StrategyConditionBlueGreenEntity> strategyConditionBlueGreenEntityList = strategyReleaseEntity.getStrategyConditionBlueGreenEntityList();
        if (CollectionUtils.isNotEmpty(strategyConditionBlueGreenEntityList)) {
            return ReleaseType.BLUE_GREEN;
        }

        List<StrategyConditionGrayEntity> strategyConditionGrayEntityList = strategyReleaseEntity.getStrategyConditionGrayEntityList();
        if (CollectionUtils.isNotEmpty(strategyConditionGrayEntityList)) {
            return ReleaseType.GRAY;
        }

        return null;
    }

    @SuppressWarnings("incomplete-switch")
    public static StrategyType getStrategyType(RuleEntity ruleEntity) {
        StrategyReleaseEntity strategyReleaseEntity = ruleEntity.getStrategyReleaseEntity();
        if (strategyReleaseEntity == null) {
            return null;
        }

        List<StrategyRouteEntity> strategyRouteEntityList = strategyReleaseEntity.getStrategyRouteEntityList();
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

    public static String getStrategyBlueConditionId() {
        return ElementType.BLUE + "-" + XmlConfigConstant.CONDITION_ELEMENT_NAME;
    }

    public static String getStrategyGreenConditionId() {
        return ElementType.GREEN + "-" + XmlConfigConstant.CONDITION_ELEMENT_NAME;
    }

    public static String getStrategyBlueRouteId(StrategyType strategyType) {
        return ElementType.BLUE + "-" + strategyType + "-" + XmlConfigConstant.ROUTE_ELEMENT_NAME;
    }

    public static String getStrategyGreenRouteId(StrategyType strategyType) {
        return ElementType.GREEN + "-" + strategyType + "-" + XmlConfigConstant.ROUTE_ELEMENT_NAME;
    }

    public static String getStrategyGrayConditionId() {
        return ElementType.GRAY + "-" + XmlConfigConstant.CONDITION_ELEMENT_NAME;
    }

    public static String getStrategyGrayRouteId(StrategyType strategyType) {
        return ElementType.GRAY + "-" + strategyType + "-" + XmlConfigConstant.ROUTE_ELEMENT_NAME;
    }

    public static String getStrategyStableRouteId(StrategyType strategyType) {
        return ElementType.STABLE + "-" + strategyType + "-" + XmlConfigConstant.ROUTE_ELEMENT_NAME;
    }

    public static StrategyConditionBlueGreenEntity getStrategyConditionBlueGreenEntity(RuleEntity ruleEntity, String strategyConditionBlueGreenId) {
        StrategyReleaseEntity strategyReleaseEntity = ruleEntity.getStrategyReleaseEntity();
        List<StrategyConditionBlueGreenEntity> strategyConditionBlueGreenEntityList = strategyReleaseEntity.getStrategyConditionBlueGreenEntityList();
        for (StrategyConditionBlueGreenEntity strategyConditionBlueGreenEntity : strategyConditionBlueGreenEntityList) {
            String conditionId = strategyConditionBlueGreenEntity.getId();
            if (StringUtils.equals(conditionId, strategyConditionBlueGreenId)) {
                return strategyConditionBlueGreenEntity;
            }
        }

        return null;
    }

    public static StrategyConditionGrayEntity getStrategyConditionGrayEntity(RuleEntity ruleEntity, String strategyConditionGrayId) {
        StrategyReleaseEntity strategyReleaseEntity = ruleEntity.getStrategyReleaseEntity();
        List<StrategyConditionGrayEntity> strategyConditionGrayEntityList = strategyReleaseEntity.getStrategyConditionGrayEntityList();
        for (StrategyConditionGrayEntity strategyConditionGrayEntity : strategyConditionGrayEntityList) {
            String conditionId = strategyConditionGrayEntity.getId();
            if (StringUtils.equals(conditionId, strategyConditionGrayId)) {
                return strategyConditionGrayEntity;
            }
        }

        return null;
    }

    public static StrategyRouteEntity getStrategyRouteEntity(RuleEntity ruleEntity, String strategyRouteId) {
        StrategyReleaseEntity strategyReleaseEntity = ruleEntity.getStrategyReleaseEntity();
        List<StrategyRouteEntity> strategyRouteEntityList = strategyReleaseEntity.getStrategyRouteEntityList();
        for (StrategyRouteEntity strategyRouteEntity : strategyRouteEntityList) {
            String routeId = strategyRouteEntity.getId();
            if (StringUtils.equals(routeId, strategyRouteId)) {
                return strategyRouteEntity;
            }
        }

        return null;
    }

    public static BlueGreenRouteType getBlueGreenRouteType(RuleEntity ruleEntity, StrategyType strategyType) {
        String greenConditionId = getStrategyGreenConditionId();
        StrategyConditionBlueGreenEntity strategyConditionGreenEntity = getStrategyConditionBlueGreenEntity(ruleEntity, greenConditionId);

        String greenRouteId = getStrategyGreenRouteId(strategyType);
        StrategyRouteEntity strategyRouteGreenEntity = getStrategyRouteEntity(ruleEntity, greenRouteId);

        if (strategyConditionGreenEntity != null && strategyRouteGreenEntity != null) {
            return BlueGreenRouteType.BLUE_GREEN_BASIC;
        } else {
            return BlueGreenRouteType.BLUE_BASIC;
        }
    }

    public static boolean isEmptyStrategy(RuleEntity ruleEntity) {
        return ruleEntity.getStrategyEntity() == null && ruleEntity.getStrategyReleaseEntity() == null;
    }
}