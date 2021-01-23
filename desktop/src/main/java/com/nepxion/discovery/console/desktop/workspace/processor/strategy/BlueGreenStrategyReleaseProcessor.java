package com.nepxion.discovery.console.desktop.workspace.processor.strategy;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import twaver.TDataBox;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.nepxion.cots.twaver.element.TElementManager;
import com.nepxion.cots.twaver.element.TLink;
import com.nepxion.cots.twaver.element.TNode;
import com.nepxion.discovery.common.entity.BlueGreenRouteType;
import com.nepxion.discovery.common.entity.ElementType;
import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.common.entity.StrategyConditionBlueGreenEntity;
import com.nepxion.discovery.common.entity.StrategyCustomizationEntity;
import com.nepxion.discovery.common.entity.StrategyEntity;
import com.nepxion.discovery.common.entity.StrategyHeaderEntity;
import com.nepxion.discovery.common.entity.StrategyRouteEntity;
import com.nepxion.discovery.common.entity.StrategyRouteType;
import com.nepxion.discovery.common.exception.DiscoveryException;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.workspace.BlueGreenTopology;
import com.nepxion.discovery.console.desktop.workspace.processor.ReleaseProcessorUtil;
import com.nepxion.discovery.console.desktop.workspace.type.TypeLocale;
import com.nepxion.discovery.console.entity.Instance;

public class BlueGreenStrategyReleaseProcessor extends AbstractStrategyReleaseProcessor {
    private BlueGreenTopology blueGreenTopology;

    @SuppressWarnings({ "unchecked" })
    @Override
    public void fromConfig(RuleEntity ruleEntity, TDataBox dataBox) throws Exception {
        StrategyEntity strategyEntity = ruleEntity.getStrategyEntity();

        BlueGreenRouteType blueGreenRouteType = blueGreenTopology.getBlueGreenRouteType();

        String blueConditionId = ReleaseProcessorUtil.getStrategyBlueConditionId();
        String greenConditionId = ReleaseProcessorUtil.getStrategyGreenConditionId();
        String blueRouteId = ReleaseProcessorUtil.getStrategyBlueRouteId(strategyType);
        String greenRouteId = ReleaseProcessorUtil.getStrategyGreenRouteId(strategyType);

        String basicStrategy = null;
        switch (strategyType) {
            case VERSION:
                basicStrategy = strategyEntity.getVersionValue();
                break;
            case REGION:
                basicStrategy = strategyEntity.getRegionValue();
                break;
        }

        if (StringUtils.isBlank(basicStrategy)) {
            throw new DiscoveryException(ConsoleLocaleFactory.getString("basic_route_services_missing"));
        }

        Map<String, String> basicStrategyMap = JsonUtil.fromJson(basicStrategy, Map.class);

        StrategyConditionBlueGreenEntity strategyConditionBlueEntity = ReleaseProcessorUtil.getStrategyConditionBlueGreenEntity(ruleEntity, blueConditionId);
        if (strategyConditionBlueEntity == null) {
            throw new DiscoveryException(ConsoleLocaleFactory.getString("blue_condition_missing"));
        }

        StrategyConditionBlueGreenEntity strategyConditionGreenEntity = ReleaseProcessorUtil.getStrategyConditionBlueGreenEntity(ruleEntity, greenConditionId);
        if (blueGreenRouteType == BlueGreenRouteType.BLUE_GREEN_BASIC && strategyConditionGreenEntity == null) {
            throw new DiscoveryException(ConsoleLocaleFactory.getString("green_condition_missing"));
        }

        String blueCondition = strategyConditionBlueEntity.getExpression();
        if (StringUtils.isBlank(blueCondition)) {
            throw new DiscoveryException(ConsoleLocaleFactory.getString("blue_condition_expression_missing"));
        }

        String greenCondition = null;
        if (strategyConditionGreenEntity != null) {
            greenCondition = strategyConditionGreenEntity.getExpression();
            if (blueGreenRouteType == BlueGreenRouteType.BLUE_GREEN_BASIC && StringUtils.isBlank(greenCondition)) {
                throw new DiscoveryException(ConsoleLocaleFactory.getString("green_condition_expression_missing"));
            }
        }

        StrategyRouteEntity strategyRouteBlueEntity = ReleaseProcessorUtil.getStrategyRouteEntity(ruleEntity, blueRouteId);
        if (strategyRouteBlueEntity == null) {
            throw new DiscoveryException(ConsoleLocaleFactory.getString("blue_route_missing"));
        }

        StrategyRouteEntity strategyRouteGreenEntity = ReleaseProcessorUtil.getStrategyRouteEntity(ruleEntity, greenRouteId);
        if (blueGreenRouteType == BlueGreenRouteType.BLUE_GREEN_BASIC && strategyRouteGreenEntity == null) {
            throw new DiscoveryException(ConsoleLocaleFactory.getString("green_route_missing"));
        }

        String blueStrategy = strategyRouteBlueEntity.getValue();
        if (StringUtils.isBlank(blueStrategy)) {
            throw new DiscoveryException(ConsoleLocaleFactory.getString("blue_route_services_missing"));
        }

        String greenStrategy = null;
        if (strategyRouteGreenEntity != null) {
            greenStrategy = strategyRouteGreenEntity.getValue();
            if (blueGreenRouteType == BlueGreenRouteType.BLUE_GREEN_BASIC && StringUtils.isBlank(greenStrategy)) {
                throw new DiscoveryException(ConsoleLocaleFactory.getString("green_route_services_missing"));
            }
        }

        Map<String, String> blueStrategyMap = JsonUtil.fromJson(blueStrategy, Map.class);

        Map<String, String> greenStrategyMap = null;
        if (greenStrategy != null) {
            greenStrategyMap = JsonUtil.fromJson(greenStrategy, Map.class);
        }

        if (blueGreenRouteType == BlueGreenRouteType.BLUE_GREEN_BASIC && blueStrategyMap.size() != greenStrategyMap.size()) {
            throw new DiscoveryException(ConsoleLocaleFactory.getString("blue_services_not_equals_green_services"));
        }

        if (blueStrategyMap.size() != basicStrategyMap.size()) {
            throw new DiscoveryException(ConsoleLocaleFactory.getString("blue_services_not_equals_basic_services"));
        }

        if (blueGreenRouteType == BlueGreenRouteType.BLUE_GREEN_BASIC && greenStrategyMap.size() != basicStrategyMap.size()) {
            throw new DiscoveryException(ConsoleLocaleFactory.getString("green_services_not_equals_basic_services"));
        }

        for (Map.Entry<String, String> entry : basicStrategyMap.entrySet()) {
            String serviceId = entry.getKey();
            String basicMetadata = entry.getValue();
            String blueMetadata = blueStrategyMap.get(serviceId);

            String greenMetadata = null;
            if (greenStrategyMap != null) {
                greenMetadata = greenStrategyMap.get(serviceId);
            }

            if (StringUtils.isBlank(blueMetadata)) {
                throw new DiscoveryException(ConsoleLocaleFactory.getString("service") + "【" + serviceId + "】" + TypeLocale.getDescription(ElementType.BLUE) + "【" + TypeLocale.getName(strategyType) + "】" + ConsoleLocaleFactory.getString("missing"));
            }

            if (blueGreenRouteType == BlueGreenRouteType.BLUE_GREEN_BASIC && StringUtils.isBlank(greenMetadata)) {
                throw new DiscoveryException(ConsoleLocaleFactory.getString("service") + "【" + serviceId + "】" + TypeLocale.getDescription(ElementType.GREEN) + "【" + TypeLocale.getName(strategyType) + "】" + ConsoleLocaleFactory.getString("missing"));
            }

            blueGreenTopology.addNodes(serviceId, blueMetadata, greenMetadata, basicMetadata, blueCondition, greenCondition);
        }
    }

    @SuppressWarnings({ "unchecked", "incomplete-switch" })
    @Override
    public String toConfig(RuleEntity ruleEntity, TDataBox dataBox) {
        if (strategyType == null) {
            return StringUtils.EMPTY;
        }

        StrategyEntity strategyEntity = new StrategyEntity();
        StrategyCustomizationEntity strategyCustomizationEntity = new StrategyCustomizationEntity();

        if (TElementManager.getNodes(dataBox).size() > 1) {
            Map<String, String> blueStrategyMap = new LinkedHashMap<String, String>();
            Map<String, String> greenStrategyMap = new LinkedHashMap<String, String>();
            Map<String, String> basicStrategyMap = new LinkedHashMap<String, String>();
            String blueCondition = null;
            String greenCondition = null;

            List<TNode> nodes = TElementManager.getNodes(dataBox);
            for (int i = nodes.size() - 1; i >= 0; i--) {
                TNode node = nodes.get(i);
                Instance instance = (Instance) node.getUserObject();
                ElementType nodeType = (ElementType) node.getBusinessObject();
                String serviceId = instance.getServiceId();
                String metadata = instance.getMetadata().get(strategyType.toString());
                switch (nodeType) {
                    case BLUE:
                        blueStrategyMap.put(serviceId, metadata);
                        break;
                    case GREEN:
                        greenStrategyMap.put(serviceId, metadata);
                        break;
                    case BASIC:
                        basicStrategyMap.put(serviceId, metadata);
                        break;
                }
            }

            List<TLink> links = TElementManager.getLinks(dataBox);
            for (int i = links.size() - 1; i >= 0; i--) {
                TLink link = links.get(i);
                ElementType linkType = (ElementType) link.getBusinessObject();
                switch (linkType) {
                    case BLUE:
                        blueCondition = link.getUserObject().toString();
                        break;
                    case GREEN:
                        greenCondition = link.getUserObject().toString();
                        break;
                }
            }

            String blueConditionId = ReleaseProcessorUtil.getStrategyBlueConditionId();
            String greenConditionId = ReleaseProcessorUtil.getStrategyGreenConditionId();
            String blueRouteId = ReleaseProcessorUtil.getStrategyBlueRouteId(strategyType);
            String greenRouteId = ReleaseProcessorUtil.getStrategyGreenRouteId(strategyType);

            StrategyConditionBlueGreenEntity strategyConditionBlueEntity = new StrategyConditionBlueGreenEntity();
            strategyConditionBlueEntity.setId(blueConditionId);
            strategyConditionBlueEntity.setExpression(blueCondition);

            StrategyConditionBlueGreenEntity strategyConditionGreenEntity = new StrategyConditionBlueGreenEntity();
            strategyConditionGreenEntity.setId(greenConditionId);
            strategyConditionGreenEntity.setExpression(greenCondition);

            StrategyRouteEntity blueStrategyRouteEntity = new StrategyRouteEntity();
            blueStrategyRouteEntity.setId(blueRouteId);
            blueStrategyRouteEntity.setValue(JsonUtil.toJson(blueStrategyMap));

            StrategyRouteEntity greenStrategyRouteEntity = new StrategyRouteEntity();
            greenStrategyRouteEntity.setId(greenRouteId);
            greenStrategyRouteEntity.setValue(JsonUtil.toJson(greenStrategyMap));

            switch (strategyType) {
                case VERSION:
                    strategyEntity.setVersionValue(JsonUtil.toJson(basicStrategyMap));
                    strategyConditionBlueEntity.setVersionId(blueRouteId);
                    strategyConditionGreenEntity.setVersionId(greenRouteId);
                    blueStrategyRouteEntity.setType(StrategyRouteType.VERSION);
                    greenStrategyRouteEntity.setType(StrategyRouteType.VERSION);
                    break;
                case REGION:
                    strategyEntity.setRegionValue(JsonUtil.toJson(basicStrategyMap));
                    strategyConditionBlueEntity.setRegionId(blueRouteId);
                    strategyConditionGreenEntity.setRegionId(greenRouteId);
                    blueStrategyRouteEntity.setType(StrategyRouteType.REGION);
                    greenStrategyRouteEntity.setType(StrategyRouteType.REGION);
                    break;
            }

            List<StrategyConditionBlueGreenEntity> strategyConditionBlueGreenEntityList = new ArrayList<StrategyConditionBlueGreenEntity>();
            strategyConditionBlueGreenEntityList.add(strategyConditionBlueEntity);
            if (StringUtils.isNotEmpty(greenCondition)) {
                strategyConditionBlueGreenEntityList.add(strategyConditionGreenEntity);
            }

            List<StrategyRouteEntity> strategyRouteEntityList = new ArrayList<StrategyRouteEntity>();
            strategyRouteEntityList.add(blueStrategyRouteEntity);
            if (MapUtils.isNotEmpty(greenStrategyMap)) {
                strategyRouteEntityList.add(greenStrategyRouteEntity);
            }

            Map<String, String> strategyHeaderMap = (Map<String, String>) dataBox.getID();
            StrategyHeaderEntity strategyHeaderEntity = new StrategyHeaderEntity();
            strategyHeaderEntity.setHeaderMap(strategyHeaderMap);

            strategyCustomizationEntity.setStrategyConditionBlueGreenEntityList(strategyConditionBlueGreenEntityList);
            strategyCustomizationEntity.setStrategyRouteEntityList(strategyRouteEntityList);
            strategyCustomizationEntity.setStrategyHeaderEntity(strategyHeaderEntity);
        }

        ruleEntity.setStrategyEntity(strategyEntity);
        ruleEntity.setStrategyCustomizationEntity(strategyCustomizationEntity);

        return deparseConfig(ruleEntity);
    }

    public void setBlueGreenTopology(BlueGreenTopology blueGreenTopology) {
        this.blueGreenTopology = blueGreenTopology;
    }
}