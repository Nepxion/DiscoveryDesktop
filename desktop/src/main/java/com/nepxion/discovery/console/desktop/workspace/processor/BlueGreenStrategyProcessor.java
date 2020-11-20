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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.nepxion.cots.twaver.element.TElementManager;
import com.nepxion.cots.twaver.element.TLink;
import com.nepxion.cots.twaver.element.TNode;
import com.nepxion.discovery.common.entity.ElementType;
import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.common.entity.StrategyConditionBlueGreenEntity;
import com.nepxion.discovery.common.entity.StrategyCustomizationEntity;
import com.nepxion.discovery.common.entity.StrategyEntity;
import com.nepxion.discovery.common.entity.StrategyHeaderEntity;
import com.nepxion.discovery.common.entity.StrategyRouteEntity;
import com.nepxion.discovery.common.entity.StrategyRouteType;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.console.desktop.workspace.type.StrategyType;
import com.nepxion.discovery.console.entity.Instance;
import com.nepxion.discovery.plugin.framework.parser.xml.XmlConfigConstant;

public class BlueGreenStrategyProcessor extends AbstractStrategyProcessor {
    @Override
    public TDataBox fromConfig() {
        return null;
    }

    @SuppressWarnings({ "unchecked", "incomplete-switch" })
    @Override
    public String toConfig(StrategyType strategyType, TDataBox dataBox) {
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

            String blueConditionId = ElementType.BLUE + "-" + XmlConfigConstant.CONDITION_ELEMENT_NAME;
            String greenConditionId = ElementType.GREEN + "-" + XmlConfigConstant.CONDITION_ELEMENT_NAME;
            String blueRouteId = ElementType.BLUE + "-" + strategyType + "-" + XmlConfigConstant.ROUTE_ELEMENT_NAME;
            String greenRouteId = ElementType.GREEN + "-" + strategyType + "-" + XmlConfigConstant.ROUTE_ELEMENT_NAME;

            StrategyConditionBlueGreenEntity strategyConditionBlueEntity = new StrategyConditionBlueGreenEntity();
            strategyConditionBlueEntity.setId(blueConditionId);
            strategyConditionBlueEntity.setConditionHeader(blueCondition);

            StrategyConditionBlueGreenEntity strategyConditionGreenEntity = new StrategyConditionBlueGreenEntity();
            strategyConditionGreenEntity.setId(greenConditionId);
            strategyConditionGreenEntity.setConditionHeader(greenCondition);

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

        RuleEntity ruleEntity = new RuleEntity();
        ruleEntity.setStrategyEntity(strategyEntity);
        ruleEntity.setStrategyCustomizationEntity(strategyCustomizationEntity);

        return StrategyProcessorFactory.getXmlConfigDeparser().deparse(ruleEntity);
    }
}