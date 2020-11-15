package com.nepxion.discovery.console.desktop.workspace;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import twaver.Link;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.nepxion.cots.twaver.element.TElementManager;
import com.nepxion.cots.twaver.element.TLink;
import com.nepxion.cots.twaver.element.TNode;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.util.ComboBoxUtil;
import com.nepxion.discovery.console.desktop.common.util.DimensionUtil;
import com.nepxion.discovery.console.desktop.workspace.panel.BlueGreenConditionPanel;
import com.nepxion.discovery.console.desktop.workspace.processor.BlueGreenStrategyProcessor;
import com.nepxion.discovery.console.desktop.workspace.processor.StrategyProcessor;
import com.nepxion.discovery.console.desktop.workspace.topology.LinkUI;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeImageType;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeSizeType;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeUI;
import com.nepxion.discovery.console.desktop.workspace.type.LinkType;
import com.nepxion.discovery.console.desktop.workspace.type.NodeType;
import com.nepxion.discovery.console.desktop.workspace.type.ReleaseType;
import com.nepxion.discovery.console.entity.Instance;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.ButtonManager;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.optionpane.JBasicOptionPane;
import com.nepxion.swing.shrinkbar.JShrinkShortcut;

public class BlueGreenTopology extends AbstractReleaseTopology {
    private static final long serialVersionUID = 1L;

    protected NodeUI serviceBlueNodeUI = new NodeUI(NodeImageType.SERVICE_BLUE, NodeSizeType.MIDDLE, true);
    protected NodeUI serviceGreenNodeUI = new NodeUI(NodeImageType.SERVICE_GREEN, NodeSizeType.MIDDLE, true);
    protected NodeUI serviceBasicNodeUI = new NodeUI(NodeImageType.SERVICE_YELLOW, NodeSizeType.MIDDLE, true);
    protected Color blueLinkUI = LinkUI.BLUE;
    protected Color greenLinkUI = LinkUI.GREEN;
    protected Color basicLinkUI = LinkUI.YELLOW;

    protected BlueGreenConditionPanel conditionPanel;
    protected JBasicComboBox blueMetadataComboBox;
    protected JBasicComboBox greenMetadataComboBox;
    protected JBasicComboBox basicMetadataComboBox;

    protected TNode blueNode;
    protected TNode greenNode;
    protected TNode basicNode;

    protected StrategyProcessor strategyProcessor = new BlueGreenStrategyProcessor();

    public BlueGreenTopology() {
        super(ReleaseType.BLUE_GREEN);
    }

    @Override
    public void initializeOperationBar() {
        conditionPanel = new BlueGreenConditionPanel();

        JPanel conditionToolBar = new JPanel();
        conditionToolBar.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 0));
        conditionToolBar.add(new JClassicButton(createModifyConditionAction()));

        JShrinkShortcut serviceShrinkShortcut = new JShrinkShortcut();
        serviceShrinkShortcut.setTitle(ConsoleLocaleFactory.getString(releaseType.toString() + "_service"));
        serviceShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        serviceShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString(releaseType.toString() + "_service"));

        serviceIdComboBox = new JBasicComboBox();
        serviceIdComboBox.setEditable(true);
        serviceIdComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (serviceIdComboBox.getSelectedItem() != e.getItem()) {
                    setMetadataUI();
                }
            }
        });
        JClassicButton refreshServicesButton = new JClassicButton(createRefreshServiceListAction());
        DimensionUtil.setWidth(refreshServicesButton, 30);

        blueMetadataComboBox = new JBasicComboBox();
        blueMetadataComboBox.setEditable(true);
        JClassicButton blueMetadataButton = new JClassicButton(createMetadataSelectorAction(blueMetadataComboBox));
        DimensionUtil.setWidth(blueMetadataButton, 30);

        greenMetadataComboBox = new JBasicComboBox();
        greenMetadataComboBox.setEditable(true);
        JClassicButton greenMetadataButton = new JClassicButton(createMetadataSelectorAction(greenMetadataComboBox));
        DimensionUtil.setWidth(greenMetadataButton, 30);

        basicMetadataComboBox = new JBasicComboBox();
        basicMetadataComboBox.setEditable(true);
        JClassicButton basicMetadataButton = new JClassicButton(createMetadataSelectorAction(basicMetadataComboBox));
        DimensionUtil.setWidth(basicMetadataButton, 30);

        double[][] serviceSize = {
                { TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
        };

        TableLayout serviceTableLayout = new TableLayout(serviceSize);
        serviceTableLayout.setHGap(0);
        serviceTableLayout.setVGap(5);

        JPanel servicePanel = new JPanel();
        servicePanel.setLayout(serviceTableLayout);
        servicePanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("service")), 5), "0, 0");
        servicePanel.add(serviceIdComboBox, "1, 0");
        servicePanel.add(refreshServicesButton, "2, 0");
        servicePanel.add(DimensionUtil.addWidth(new JBasicLabel(NodeType.BLUE.getDescription()), 5), "0, 1");
        servicePanel.add(blueMetadataComboBox, "1, 1");
        servicePanel.add(blueMetadataButton, "2, 1");
        servicePanel.add(DimensionUtil.addWidth(new JBasicLabel(NodeType.GREEN.getDescription()), 5), "0, 2");
        servicePanel.add(greenMetadataComboBox, "1, 2");
        servicePanel.add(greenMetadataButton, "2, 2");
        servicePanel.add(DimensionUtil.addWidth(new JBasicLabel(NodeType.BASIC.getDescription()), 5), "0, 3");
        servicePanel.add(basicMetadataComboBox, "1, 3");
        servicePanel.add(basicMetadataButton, "2, 3");

        JPanel serviceToolBar = new JPanel();
        serviceToolBar.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 0));
        serviceToolBar.add(new JClassicButton(createAddServiceStrategyAction()));
        serviceToolBar.add(new JClassicButton(createModifyServiceStrategyAction()));

        JShrinkShortcut conditionShrinkShortcut = new JShrinkShortcut();
        conditionShrinkShortcut.setTitle(ConsoleLocaleFactory.getString(releaseType.toString() + "_condition"));
        conditionShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        conditionShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString(releaseType.toString() + "_condition"));

        double[][] size = {
                { TableLayout.FILL },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, 10, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
        };

        TableLayout tableLayout = new TableLayout(size);
        tableLayout.setHGap(0);
        tableLayout.setVGap(5);

        operationBar.setLayout(tableLayout);
        operationBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        operationBar.add(conditionShrinkShortcut, "0, 0");
        operationBar.add(conditionPanel, "0, 1");
        operationBar.add(conditionToolBar, "0, 2");
        operationBar.add(serviceShrinkShortcut, "0, 4");
        operationBar.add(servicePanel, "0, 5");
        operationBar.add(serviceToolBar, "0, 6");
    }

    public void addNodes(String serviceId, String blueMetadata, String greenMetadata, String basicMetadata, String blueCondition, String greenCondition) {
        TNode newBlueNode = addNode(ButtonManager.getHtmlText(serviceId + "\n" + strategyType.toString() + "=" + blueMetadata), serviceBlueNodeUI);
        Instance newBlueInstance = new Instance();
        newBlueInstance.setServiceId(serviceId);
        Map<String, String> newBlueMetadataMap = new HashMap<String, String>();
        newBlueMetadataMap.put(strategyType.toString(), blueMetadata);
        newBlueInstance.setMetadata(newBlueMetadataMap);
        newBlueNode.setUserObject(newBlueInstance);
        newBlueNode.setBusinessObject(NodeType.BLUE);
        if (blueNode == null) {
            TLink blueLink = addLink(gatewayNode, newBlueNode, blueLinkUI);
            blueLink.setName(ConsoleLocaleFactory.getString("blue_route"));
            blueLink.setToolTipText(blueCondition);
            blueLink.setUserObject(blueCondition);
            blueLink.setBusinessObject(LinkType.BLUE);
        } else {
            TLink link = addLink(blueNode, newBlueNode, null);
            link.setBusinessObject(LinkType.UNDEFINED);
        }
        blueNode = newBlueNode;

        TNode newGreenNode = addNode(ButtonManager.getHtmlText(serviceId + "\n" + strategyType.toString() + "=" + greenMetadata), serviceGreenNodeUI);
        Instance newGreenInstance = new Instance();
        newGreenInstance.setServiceId(serviceId);
        Map<String, String> newGreenMetadataMap = new HashMap<String, String>();
        newGreenMetadataMap.put(strategyType.toString(), greenMetadata);
        newGreenInstance.setMetadata(newGreenMetadataMap);
        newGreenNode.setUserObject(newGreenInstance);
        newGreenNode.setBusinessObject(NodeType.GREEN);
        if (greenNode == null) {
            TLink greenLink = addLink(gatewayNode, newGreenNode, greenLinkUI);
            greenLink.setName(ConsoleLocaleFactory.getString("green_route"));
            greenLink.setToolTipText(greenCondition);
            greenLink.setUserObject(greenCondition);
            greenLink.setBusinessObject(LinkType.GREEN);
        } else {
            TLink link = addLink(greenNode, newGreenNode, null);
            link.setBusinessObject(LinkType.UNDEFINED);
        }
        greenNode = newGreenNode;

        TNode newBasicNode = addNode(ButtonManager.getHtmlText(serviceId + "\n" + strategyType.toString() + "=" + basicMetadata), serviceBasicNodeUI);
        Instance newBasicInstance = new Instance();
        newBasicInstance.setServiceId(serviceId);
        Map<String, String> newBasicMetadataMap = new HashMap<String, String>();
        newBasicMetadataMap.put(strategyType.toString(), basicMetadata);
        newBasicInstance.setMetadata(newBasicMetadataMap);
        newBasicNode.setUserObject(newBasicInstance);
        newBasicNode.setBusinessObject(NodeType.BASIC);
        if (basicNode == null) {
            TLink basicLink = addLink(gatewayNode, newBasicNode, basicLinkUI);
            basicLink.setName(ConsoleLocaleFactory.getString("basic_route"));
            basicLink.setBusinessObject(LinkType.BASIC);
        } else {
            TLink link = addLink(basicNode, newBasicNode, null);
            link.setBusinessObject(LinkType.UNDEFINED);
        }
        basicNode = newBasicNode;
    }

    @SuppressWarnings("unchecked")
    public void removeNodes() {
        if (blueNode != null) {
            List<Link> blueLinks = blueNode.getAllLinks();
            if (CollectionUtils.isNotEmpty(blueLinks)) {
                TNode currentBlueNode = (TNode) blueLinks.get(0).getFrom();
                dataBox.removeElement(blueNode);
                if (currentBlueNode != gatewayNode) {
                    blueNode = currentBlueNode;
                } else {
                    blueNode = null;
                }
            }
        }

        if (greenNode != null) {
            List<Link> greenLinks = greenNode.getAllLinks();
            if (CollectionUtils.isNotEmpty(greenLinks)) {
                TNode currentGreenNode = (TNode) greenLinks.get(0).getFrom();
                dataBox.removeElement(greenNode);
                if (currentGreenNode != gatewayNode) {
                    greenNode = currentGreenNode;
                } else {
                    greenNode = null;
                }
            }
        }

        if (basicNode != null) {
            List<Link> basicLinks = basicNode.getAllLinks();
            if (CollectionUtils.isNotEmpty(basicLinks)) {
                TNode currentBasicNode = (TNode) basicLinks.get(0).getFrom();
                dataBox.removeElement(basicNode);
                if (currentBasicNode != gatewayNode) {
                    basicNode = currentBasicNode;
                } else {
                    basicNode = null;
                }
            }
        }
    }

    public void resetNodes() {
        blueNode = null;
        greenNode = null;
        basicNode = null;
    }

    @SuppressWarnings({ "unchecked", "incomplete-switch" })
    public void modifyNodes(String serviceId, String blueMetadata, String greenMetadata, String basicMetadata) {
        List<TNode> nodes = TElementManager.getNodes(dataBox);
        for (TNode node : nodes) {
            Instance instance = (Instance) node.getUserObject();
            if (StringUtils.equalsIgnoreCase(instance.getServiceId(), serviceId)) {
                NodeType nodeType = (NodeType) node.getBusinessObject();
                switch (nodeType) {
                    case BLUE:
                        node.setName(ButtonManager.getHtmlText(serviceId + "\n" + strategyType.toString() + "=" + blueMetadata));
                        instance.getMetadata().put(strategyType.toString(), blueMetadata);
                        break;
                    case GREEN:
                        node.setName(ButtonManager.getHtmlText(serviceId + "\n" + strategyType.toString() + "=" + greenMetadata));
                        instance.getMetadata().put(strategyType.toString(), greenMetadata);
                        break;
                    case BASIC:
                        node.setName(ButtonManager.getHtmlText(serviceId + "\n" + strategyType.toString() + "=" + basicMetadata));
                        instance.getMetadata().put(strategyType.toString(), basicMetadata);
                        break;
                }
            }
        }
    }

    @SuppressWarnings({ "unchecked", "incomplete-switch" })
    public void modifyLinks(String blueCondition, String greenCondition) {
        List<TLink> links = TElementManager.getLinks(dataBox);
        for (TLink link : links) {
            LinkType linkType = (LinkType) link.getBusinessObject();
            switch (linkType) {
                case BLUE:
                    link.setToolTipText(blueCondition);
                    link.setUserObject(blueCondition);
                    break;
                case GREEN:
                    link.setToolTipText(greenCondition);
                    link.setUserObject(greenCondition);
                    break;
            }
        }
    }

    public JSecurityAction createModifyConditionAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("modify_text"), ConsoleIconFactory.getSwingIcon("adjust.png"), ConsoleLocaleFactory.getString("modify_condition_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                String blueCondition = conditionPanel.getBlueCondition();
                String greenCondition = conditionPanel.getGreenCondition();

                if (StringUtils.isBlank(blueCondition) || StringUtils.isBlank(greenCondition)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), ConsoleLocaleFactory.getString("condition_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                modifyLinks(blueCondition, greenCondition);
            }
        };

        return action;
    }

    @Override
    public void remove() {
        removeNodes();
    }

    @Override
    public void reset() {
        resetNodes();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setMetadataUI(Object[] metadatas) {
        blueMetadataComboBox.setModel(new DefaultComboBoxModel<>(metadatas));
        greenMetadataComboBox.setModel(new DefaultComboBoxModel<>(metadatas));
        basicMetadataComboBox.setModel(new DefaultComboBoxModel<>(metadatas));
    }

    @Override
    public void addServiceStrategy(String serviceId) {
        String blueMetadata = ComboBoxUtil.getSelectedValue(blueMetadataComboBox);
        String greenMetadata = ComboBoxUtil.getSelectedValue(greenMetadataComboBox);
        String basicMetadata = ComboBoxUtil.getSelectedValue(basicMetadataComboBox);
        String blueCondition = conditionPanel.getBlueCondition();
        String greenCondition = conditionPanel.getGreenCondition();

        if (StringUtils.isBlank(blueMetadata) || StringUtils.isBlank(greenMetadata) || StringUtils.isBlank(basicMetadata)) {
            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), strategyType.getName() + " " + ConsoleLocaleFactory.getString("not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

            return;
        }

        if (StringUtils.isBlank(blueCondition) || StringUtils.isBlank(greenCondition)) {
            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), ConsoleLocaleFactory.getString("condition_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

            return;
        }

        addNodes(serviceId, blueMetadata, greenMetadata, basicMetadata, blueCondition, greenCondition);

        executeLayout();
    }

    @Override
    public void modifyServiceStrategy(String serviceId) {
        String blueMetadata = ComboBoxUtil.getSelectedValue(blueMetadataComboBox);
        String greenMetadata = ComboBoxUtil.getSelectedValue(greenMetadataComboBox);
        String basicMetadata = ComboBoxUtil.getSelectedValue(basicMetadataComboBox);

        if (StringUtils.isBlank(blueMetadata) || StringUtils.isBlank(greenMetadata) || StringUtils.isBlank(basicMetadata)) {
            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), strategyType.getName() + " " + ConsoleLocaleFactory.getString("not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

            return;
        }

        modifyNodes(serviceId, blueMetadata, greenMetadata, basicMetadata);
    }

    @Override
    public StrategyProcessor getStrategyProcessor() {
        return strategyProcessor;
    }
}