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

import org.apache.commons.lang3.StringUtils;

import com.nepxion.cots.twaver.element.TElementManager;
import com.nepxion.cots.twaver.element.TLink;
import com.nepxion.cots.twaver.element.TNode;
import com.nepxion.discovery.common.entity.ElementType;
import com.nepxion.discovery.common.entity.RegionWeightEntity;
import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.common.entity.StrategyConditionGrayEntity;
import com.nepxion.discovery.common.entity.StrategyCustomizationEntity;
import com.nepxion.discovery.common.entity.StrategyEntity;
import com.nepxion.discovery.common.entity.StrategyRouteEntity;
import com.nepxion.discovery.common.entity.StrategyRouteType;
import com.nepxion.discovery.common.entity.VersionWeightEntity;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.console.desktop.workspace.type.StrategyType;
import com.nepxion.discovery.console.entity.Instance;
import com.nepxion.discovery.plugin.framework.parser.xml.XmlConfigConstant;

public class GrayStrategyProcessor extends AbstractStrategyProcessor {
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
            Map<String, String> grayStrategyMap = new LinkedHashMap<String, String>();
            Map<String, String> stableStrategyMap = new LinkedHashMap<String, String>();
            String grayCondition = null;
            String stableCondition = null;

            List<TNode> nodes = TElementManager.getNodes(dataBox);
            for (int i = nodes.size() - 1; i >= 0; i--) {
                TNode node = nodes.get(i);
                Instance instance = (Instance) node.getUserObject();
                ElementType nodeType = (ElementType) node.getBusinessObject();
                String serviceId = instance.getServiceId();
                String metadata = instance.getMetadata().get(strategyType.toString());
                switch (nodeType) {
                    case GRAY:
                        grayStrategyMap.put(serviceId, metadata);
                        break;
                    case STABLE:
                        stableStrategyMap.put(serviceId, metadata);
                        break;
                }
            }

            List<TLink> links = TElementManager.getLinks(dataBox);
            for (int i = links.size() - 1; i >= 0; i--) {
                TLink link = links.get(i);
                ElementType linkType = (ElementType) link.getBusinessObject();
                switch (linkType) {
                    case GRAY:
                        grayCondition = link.getUserObject().toString();
                        break;
                    case STABLE:
                        stableCondition = link.getUserObject().toString();
                        break;
                }
            }

            String grayConditionId = ElementType.GRAY + "-" + XmlConfigConstant.CONDITION_ELEMENT_NAME;
            String grayRouteId = ElementType.GRAY + "-" + strategyType + "-" + XmlConfigConstant.ROUTE_ELEMENT_NAME;
            String stableRouteId = ElementType.STABLE + "-" + strategyType + "-" + XmlConfigConstant.ROUTE_ELEMENT_NAME;

            Map<String, Integer> weightMap = new LinkedHashMap<String, Integer>();
            weightMap.put(grayRouteId, Integer.valueOf(grayCondition));
            weightMap.put(stableRouteId, Integer.valueOf(stableCondition));

            StrategyConditionGrayEntity strategyConditionGrayEntity = new StrategyConditionGrayEntity();
            strategyConditionGrayEntity.setId(grayConditionId);

            StrategyRouteEntity grayStrategyRouteEntity = new StrategyRouteEntity();
            grayStrategyRouteEntity.setId(grayRouteId);
            grayStrategyRouteEntity.setValue(JsonUtil.toJson(grayStrategyMap));

            StrategyRouteEntity stableStrategyRouteEntity = new StrategyRouteEntity();
            stableStrategyRouteEntity.setId(stableRouteId);
            stableStrategyRouteEntity.setValue(JsonUtil.toJson(stableStrategyMap));

            switch (strategyType) {
                case VERSION:
                    VersionWeightEntity versionWeightEntity = new VersionWeightEntity();
                    versionWeightEntity.setWeightMap(weightMap);
                    strategyConditionGrayEntity.setVersionWeightEntity(versionWeightEntity);
                    grayStrategyRouteEntity.setType(StrategyRouteType.VERSION);
                    stableStrategyRouteEntity.setType(StrategyRouteType.VERSION);
                    break;
                case REGION:
                    RegionWeightEntity regionWeightEntity = new RegionWeightEntity();
                    regionWeightEntity.setWeightMap(weightMap);
                    strategyConditionGrayEntity.setRegionWeightEntity(regionWeightEntity);
                    grayStrategyRouteEntity.setType(StrategyRouteType.REGION);
                    stableStrategyRouteEntity.setType(StrategyRouteType.REGION);
                    break;
            }

            List<StrategyConditionGrayEntity> strategyConditionGrayEntityList = new ArrayList<StrategyConditionGrayEntity>();
            strategyConditionGrayEntityList.add(strategyConditionGrayEntity);

            List<StrategyRouteEntity> strategyRouteEntityList = new ArrayList<StrategyRouteEntity>();
            strategyRouteEntityList.add(grayStrategyRouteEntity);
            strategyRouteEntityList.add(stableStrategyRouteEntity);

            strategyCustomizationEntity.setStrategyConditionGrayEntityList(strategyConditionGrayEntityList);
            strategyCustomizationEntity.setStrategyRouteEntityList(strategyRouteEntityList);
        }

        RuleEntity ruleEntity = new RuleEntity();
        ruleEntity.setStrategyEntity(strategyEntity);
        ruleEntity.setStrategyCustomizationEntity(strategyCustomizationEntity);

        return StrategyProcessorFactory.getXmlConfigDeparser().deparse(ruleEntity);
    }
}