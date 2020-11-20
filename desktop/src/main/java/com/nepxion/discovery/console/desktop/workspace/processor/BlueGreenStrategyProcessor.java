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
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.nepxion.cots.twaver.element.TElementManager;
import com.nepxion.cots.twaver.element.TLink;
import com.nepxion.cots.twaver.element.TNode;
import com.nepxion.discovery.common.entity.EscapeType;
import com.nepxion.discovery.console.desktop.workspace.type.ElementType;
import com.nepxion.discovery.console.desktop.workspace.type.ReleaseType;
import com.nepxion.discovery.console.desktop.workspace.type.StrategyType;
import com.nepxion.discovery.console.entity.Instance;

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

        String strategyValue = strategyType.toString();
        String basicStrategy = null;
        String blueStrategy = null;
        String greenStrategy = null;
        String blueCondition = null;
        String greenCondition = null;

        if (TElementManager.getNodes(dataBox).size() > 1) {
            StringBuilder blueStrategyStringBuilder = new StringBuilder();
            StringBuilder greenStrategyStringBuilder = new StringBuilder();
            StringBuilder basicStrategyStringBuilder = new StringBuilder();
            List<TNode> nodes = TElementManager.getNodes(dataBox);
            for (int i = nodes.size() - 1; i >= 0; i--) {
                TNode node = nodes.get(i);
                Instance instance = (Instance) node.getUserObject();
                ElementType nodeType = (ElementType) node.getBusinessObject();
                String serviceId = instance.getServiceId();
                String metadata = instance.getMetadata().get(strategyValue);
                switch (nodeType) {
                    case BLUE:
                        blueStrategyStringBuilder.append("\"" + serviceId + "\":\"" + metadata + "\", ");
                        break;
                    case GREEN:
                        greenStrategyStringBuilder.append("\"" + serviceId + "\":\"" + metadata + "\", ");
                        break;
                    case BASIC:
                        basicStrategyStringBuilder.append("\"" + serviceId + "\":\"" + metadata + "\", ");
                        break;
                }
            }
            blueStrategy = blueStrategyStringBuilder.toString();
            blueStrategy = blueStrategy.substring(0, blueStrategy.length() - 2);
            greenStrategy = greenStrategyStringBuilder.toString();
            if (StringUtils.isNotEmpty(greenStrategy)) {
                greenStrategy = greenStrategy.substring(0, greenStrategy.length() - 2);
            }
            basicStrategy = basicStrategyStringBuilder.toString();
            basicStrategy = basicStrategy.substring(0, basicStrategy.length() - 2);

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
        }

        Map<String, String> parameterMap = (Map<String, String>) dataBox.getID();

        StringBuilder strategyStringBuilder = new StringBuilder();
        strategyStringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        strategyStringBuilder.append("<rule>\n");
        if (TElementManager.getNodes(dataBox).size() > 1) {
            strategyStringBuilder.append("    <strategy>\n");
            strategyStringBuilder.append("        <" + strategyValue + ">{" + basicStrategy + "}</" + strategyValue + ">\n");
            strategyStringBuilder.append("    </strategy>\n\n");
            strategyStringBuilder.append("    <strategy-customization>\n");
            strategyStringBuilder.append("        <conditions type=\"" + ReleaseType.BLUE_GREEN.toString() + "\">\n");
            strategyStringBuilder.append("            <condition id=\"blue-condition\" header=\"" + EscapeType.escape(blueCondition) + "\" " + strategyValue + "-id=\"blue-" + strategyValue + "-route\"/>\n");
            if (StringUtils.isNotEmpty(greenCondition)) {
                strategyStringBuilder.append("            <condition id=\"green-condition\" header=\"" + EscapeType.escape(greenCondition) + "\" " + strategyValue + "-id=\"green-" + strategyValue + "-route\"/>\n");
            }
            strategyStringBuilder.append("        </conditions>\n\n");
            strategyStringBuilder.append("        <routes>\n");
            strategyStringBuilder.append("            <route id=\"blue-" + strategyValue + "-route\" type=\"" + strategyValue + "\">{" + blueStrategy + "}</route>\n");
            if (StringUtils.isNotEmpty(greenStrategy)) {
                strategyStringBuilder.append("            <route id=\"green-" + strategyValue + "-route\" type=\"" + strategyValue + "\">{" + greenStrategy + "}</route>\n");
            }
            strategyStringBuilder.append("        </routes>\n");
            if (MapUtils.isNotEmpty(parameterMap)) {
                strategyStringBuilder.append("\n");
                strategyStringBuilder.append("        <headers>\n");
                for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                    strategyStringBuilder.append("            <header key=\"" + entry.getKey() + "\" value=\"" + entry.getValue() + "\"/>\n");
                }
                strategyStringBuilder.append("        </headers>\n");
            }
            strategyStringBuilder.append("    </strategy-customization>\n");
        }
        strategyStringBuilder.append("</rule>");

        return strategyStringBuilder.toString();
    }
}