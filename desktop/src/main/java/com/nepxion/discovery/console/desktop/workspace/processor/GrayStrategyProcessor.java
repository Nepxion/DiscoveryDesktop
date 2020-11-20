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

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.cots.twaver.element.TElementManager;
import com.nepxion.cots.twaver.element.TLink;
import com.nepxion.cots.twaver.element.TNode;
import com.nepxion.discovery.console.desktop.workspace.type.ElementType;
import com.nepxion.discovery.console.desktop.workspace.type.ReleaseType;
import com.nepxion.discovery.console.desktop.workspace.type.StrategyType;
import com.nepxion.discovery.console.entity.Instance;

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

        String strategyValue = strategyType.toString();
        String grayCondition = null;
        String stableCondition = null;
        String grayStrategy = null;
        String stableStrategy = null;

        if (TElementManager.getNodes(dataBox).size() > 1) {
            StringBuilder grayStrategyStringBuilder = new StringBuilder();
            StringBuilder stableStrategyStringBuilder = new StringBuilder();
            List<TNode> nodes = TElementManager.getNodes(dataBox);
            for (int i = nodes.size() - 1; i >= 0; i--) {
                TNode node = nodes.get(i);
                Instance instance = (Instance) node.getUserObject();
                ElementType nodeType = (ElementType) node.getBusinessObject();
                String serviceId = instance.getServiceId();
                String metadata = instance.getMetadata().get(strategyValue);
                switch (nodeType) {
                    case GRAY:
                        grayStrategyStringBuilder.append("\"" + serviceId + "\":\"" + metadata + "\", ");
                        break;
                    case STABLE:
                        stableStrategyStringBuilder.append("\"" + serviceId + "\":\"" + metadata + "\", ");
                        break;
                }
            }
            grayStrategy = grayStrategyStringBuilder.toString();
            grayStrategy = grayStrategy.substring(0, grayStrategy.length() - 2);
            stableStrategy = stableStrategyStringBuilder.toString();
            stableStrategy = stableStrategy.substring(0, stableStrategy.length() - 2);

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
        }

        StringBuilder strategyStringBuilder = new StringBuilder();
        strategyStringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        strategyStringBuilder.append("<rule>\n");
        if (TElementManager.getNodes(dataBox).size() > 1) {
            strategyStringBuilder.append("    <strategy-customization>\n");
            strategyStringBuilder.append("        <conditions type=\"" + ReleaseType.GRAY.toString() + "\">\n");
            strategyStringBuilder.append("            <condition id=\"gray-condition\" " + strategyValue + "-id=\"gray-" + strategyValue + "-route=" + grayCondition + ";stable-" + strategyValue + "-route=" + stableCondition + "\"/>\n");
            strategyStringBuilder.append("        </conditions>\n\n");
            strategyStringBuilder.append("        <routes>\n");
            strategyStringBuilder.append("            <route id=\"gray-" + strategyValue + "-route\" type=\"" + strategyValue + "\">{" + grayStrategy + "}</route>\n");
            strategyStringBuilder.append("            <route id=\"stable-" + strategyValue + "-route\" type=\"" + strategyValue + "\">{" + stableStrategy + "}</route>\n");
            strategyStringBuilder.append("        </routes>\n");
            strategyStringBuilder.append("    </strategy-customization>\n");
        }
        strategyStringBuilder.append("</rule>");

        return strategyStringBuilder.toString();
    }
}