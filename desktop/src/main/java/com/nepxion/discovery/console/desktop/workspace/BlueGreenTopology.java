package com.nepxion.discovery.console.desktop.workspace;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import twaver.Generator;
import twaver.Link;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.nepxion.cots.twaver.element.TElementManager;
import com.nepxion.cots.twaver.element.TLink;
import com.nepxion.cots.twaver.element.TNode;
import com.nepxion.cots.twaver.graph.TGraphBackground;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.console.controller.ServiceController;
import com.nepxion.discovery.console.desktop.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.topology.AbstractTopology;
import com.nepxion.discovery.console.desktop.topology.LinkUI;
import com.nepxion.discovery.console.desktop.topology.NodeImageType;
import com.nepxion.discovery.console.desktop.topology.NodeLocation;
import com.nepxion.discovery.console.desktop.topology.NodeSizeType;
import com.nepxion.discovery.console.desktop.topology.NodeUI;
import com.nepxion.discovery.console.entity.Instance;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.ButtonManager;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.checkbox.JBasicCheckBox;
import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.dialog.JExceptionDialog;
import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.icon.IconFactory;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.listener.DisplayAbilityListener;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.optionpane.JBasicOptionPane;
import com.nepxion.swing.radiobutton.JBasicRadioButton;
import com.nepxion.swing.scrollpane.JBasicScrollPane;
import com.nepxion.swing.selector.checkbox.JCheckBoxSelector;
import com.nepxion.swing.textarea.JBasicTextArea;
import com.nepxion.swing.textfield.JBasicTextField;

public class BlueGreenTopology extends AbstractTopology {
    private static final long serialVersionUID = 1L;

    private NodeLocation nodeLocation = new NodeLocation(440, 50, 200, 0);
    private NodeUI gatewayBlackNodeUI = new NodeUI(NodeImageType.GATEWAY_BLACK, NodeSizeType.LARGE, true);
    private NodeUI serviceYellowNodeUI = new NodeUI(NodeImageType.SERVICE_YELLOW, NodeSizeType.MIDDLE, true);
    private NodeUI serviceBlueNodeUI = new NodeUI(NodeImageType.SERVICE_BLUE, NodeSizeType.MIDDLE, true);
    private NodeUI serviceGreenNodeUI = new NodeUI(NodeImageType.SERVICE_GREEN, NodeSizeType.MIDDLE, true);

    private TGraphBackground background;
    private JBasicComboBox serviceIdComboBox;
    private JBasicComboBox blueMetadataComboBox;
    private JBasicComboBox greenMetadataComboBox;
    private JBasicComboBox basicMetadataComboBox;
    private JBasicTextField blueConditionTextField;
    private JBasicTextField greenConditionTextField;

    private TNode gatewayNode;
    private TNode blueNode;
    private TNode greenNode;
    private TNode basicNode;

    private String operationName;
    private String group;
    private Instance gateway;
    private OperationType operationType;
    private StrategyType strategyType;
    private ConfigType configType;
    private Map<String, List<Instance>> instanceMap;

    private JBasicTextArea strategyTextArea;
    private JBasicScrollPane strategyScrollPane;
    private JBasicTextField layoutTextField = new JBasicTextField();

    public BlueGreenTopology() {
        initializeContentBar();
        initializeToolBar();
        initializeTopology();
        initializeListener();

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void initializeContentBar() {
        serviceIdComboBox = new JBasicComboBox();
        serviceIdComboBox.setEditable(true);
        serviceIdComboBox.setPreferredSize(new Dimension(300, layoutTextField.getPreferredSize().height));
        serviceIdComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (serviceIdComboBox.getSelectedItem() != e.getItem()) {
                    setMetadataUI();
                }
            }
        });
        JClassicButton refreshServicesButton = new JClassicButton(createRefreshServicesAction());
        refreshServicesButton.setPreferredSize(new Dimension(30, refreshServicesButton.getPreferredSize().height));

        blueMetadataComboBox = new JBasicComboBox();
        blueMetadataComboBox.setEditable(true);
        JClassicButton blueMetadataButton = new JClassicButton(createMetadataSelectorAction(blueMetadataComboBox));
        blueMetadataButton.setPreferredSize(new Dimension(30, blueMetadataButton.getPreferredSize().height));

        greenMetadataComboBox = new JBasicComboBox();
        greenMetadataComboBox.setEditable(true);
        JClassicButton greenMetadataButton = new JClassicButton(createMetadataSelectorAction(greenMetadataComboBox));
        greenMetadataButton.setPreferredSize(new Dimension(30, greenMetadataButton.getPreferredSize().height));

        basicMetadataComboBox = new JBasicComboBox();
        basicMetadataComboBox.setEditable(true);
        JClassicButton basicMetadataButton = new JClassicButton(createMetadataSelectorAction(basicMetadataComboBox));
        basicMetadataButton.setPreferredSize(new Dimension(30, basicMetadataButton.getPreferredSize().height));

        double[][] serviceSize = {
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED },
                { TableLayout.PREFERRED }
        };

        TableLayout serviceTableLayout = new TableLayout(serviceSize);
        serviceTableLayout.setHGap(5);
        serviceTableLayout.setVGap(5);

        JPanel servicePanel = new JPanel();
        servicePanel.setLayout(serviceTableLayout);
        servicePanel.add(serviceIdComboBox, "0, 0");
        servicePanel.add(refreshServicesButton, "1, 0");
        servicePanel.add(new JBasicLabel("蓝版本"), "2, 0");
        servicePanel.add(blueMetadataComboBox, "3, 0");
        servicePanel.add(blueMetadataButton, "4, 0");
        servicePanel.add(new JBasicLabel("绿版本"), "5, 0");
        servicePanel.add(greenMetadataComboBox, "6, 0");
        servicePanel.add(greenMetadataButton, "7, 0");
        servicePanel.add(new JBasicLabel("兜底版本"), "8, 0");
        servicePanel.add(basicMetadataComboBox, "9, 0");
        servicePanel.add(basicMetadataButton, "10, 0");

        JPanel serviceToolBar = new JPanel();
        serviceToolBar.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 0));
        serviceToolBar.add(new JClassicButton(createAddNodesAction()));
        serviceToolBar.add(new JClassicButton(createRemoveNodesAction()));
        serviceToolBar.add(new JClassicButton(createModifyNodesAction()));
        serviceToolBar.add(new JClassicButton(createClearNodesAction()));

        blueConditionTextField = new JBasicTextField("#H['a'] == '1' && #H['b'] <= '2'");
        greenConditionTextField = new JBasicTextField("#H['a'] == '3'");

        double[][] conditionSize = {
                { TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL },
                { TableLayout.PREFERRED }
        };

        TableLayout conditionTableLayout = new TableLayout(conditionSize);
        conditionTableLayout.setHGap(5);
        conditionTableLayout.setVGap(5);

        JPanel conditionPanel = new JPanel();
        conditionPanel.setLayout(conditionTableLayout);
        conditionPanel.add(new JBasicLabel("蓝条件"), "0, 0");
        conditionPanel.add(blueConditionTextField, "1, 0");
        conditionPanel.add(new JBasicLabel("绿条件"), "2, 0");
        conditionPanel.add(greenConditionTextField, "3, 0");

        JPanel conditionToolBar = new JPanel();
        conditionToolBar.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 0));
        conditionToolBar.add(new JClassicButton(createValidateAction()));
        conditionToolBar.add(new JClassicButton(createModifyLinksAction()));

        double[][] size = {
                { TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED },
                { TableLayout.PREFERRED, TableLayout.PREFERRED }
        };

        TableLayout tableLayout = new TableLayout(size);
        tableLayout.setHGap(10);
        tableLayout.setVGap(5);

        JPanel toolBar = new JPanel();
        toolBar.setLayout(tableLayout);
        toolBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        toolBar.add(new JBasicLabel("蓝绿服务"), "0, 0");
        toolBar.add(servicePanel, "1, 0");
        toolBar.add(serviceToolBar, "2, 0");
        toolBar.add(new JBasicLabel("蓝绿条件"), "0, 1");
        toolBar.add(conditionPanel, "1, 1");
        toolBar.add(conditionToolBar, "2, 1");

        add(toolBar, BorderLayout.NORTH);
    }

    private void initializeToolBar() {
        JClassicButton previewButton = new JClassicButton(createPreviewAction());

        JClassicButton controlButton = (JClassicButton) graph.getToolbar().getComponent(0);
        controlButton.setPreferredSize(new Dimension(controlButton.getPreferredSize().width + 5, previewButton.getPreferredSize().height));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(controlButton);
        buttonPanel.add(previewButton);
        buttonPanel.add(new JClassicButton(createInspectorAction()));
        buttonPanel.add(new JClassicButton(createRouterAction()));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(new JClassicButton(createLayoutAction()));
        buttonPanel.add(new JClassicButton(createSaveAction()));

        JPanel toolBar = new JPanel();
        toolBar.setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 5));
        toolBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        toolBar.add(buttonPanel);

        add(toolBar, BorderLayout.SOUTH);
    }

    private void initializeTopology() {
        background = graph.getGraphBackground();
        graph.setElementStateOutlineColorGenerator(new Generator() {
            public Object generate(Object object) {
                return null;
            }
        });
        graph.getToolbar().setVisible(false);
    }

    private void initializeListener() {
        addHierarchyListener(new DisplayAbilityListener() {
            public void displayAbilityChanged(HierarchyEvent e) {
                showLayoutBar(150, 50, 200, 60);
                toggleLayoutBar();

                removeHierarchyListener(this);
            }
        });
    }

    public void initializeData(String operationName, String group, Instance gateway, OperationType operationType, StrategyType strategyType, ConfigType configType) {
        this.operationName = operationName;
        this.group = group;
        this.gateway = gateway;
        this.operationType = operationType;
        this.strategyType = strategyType;
        this.configType = configType;

        refreshData();
    }

    public void refreshData() {
        try {
            this.instanceMap = ServiceController.getInstanceMap(Arrays.asList(group));
        } catch (Exception e) {
            JExceptionDialog.traceException(HandleManager.getFrame(this), ConsoleLocaleFactory.getString("query_data_failure"), e);
        }
    }

    public void initializeUI() {
        setTitle();
        setGatewayNode();

        refreshUI();
    }

    public void refreshUI() {
        setServiceUI();
        setMetadataUI();
    }

    public String getOperationName() {
        return operationName;
    }

    public String getGroup() {
        return group;
    }

    public Instance getGateway() {
        return gateway;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public StrategyType getStrategyType() {
        return strategyType;
    }

    public ConfigType getConfigType() {
        return configType;
    }

    private void setTitle() {
        background.setTitle(operationName + " | " + group + " | " + operationType.getDescription() + " | " + strategyType.getDescription() + " | " + configType.getDescription());
    }

    private void setGatewayNode() {
        dataBox.clear();

        blueNode = null;
        greenNode = null;
        basicNode = null;

        gatewayNode = addNode(gateway.getServiceId(), gatewayBlackNodeUI);
        gatewayNode.setUserObject(gateway);
        gatewayNode.setBusinessObject(NodeType.GATEWAY);
    }

    @SuppressWarnings("unchecked")
    private void setServiceUI() {
        serviceIdComboBox.setModel(new DefaultComboBoxModel<>(instanceMap.keySet().toArray()));
    }

    @SuppressWarnings("unchecked")
    private void setMetadataUI() {
        List<String> metadatas = new ArrayList<String>();
        Object selectedItem = serviceIdComboBox.getSelectedItem();
        if (selectedItem != null) {
            String serviceId = selectedItem.toString().trim();
            List<Instance> instances = instanceMap.get(serviceId);
            if (CollectionUtils.isNotEmpty(instances)) {
                for (Instance instance : instances) {
                    String metadata = instance.getMetadata().get(strategyType.getValue());
                    metadatas.add(metadata);
                }
            }
        }
        metadatas.add(DiscoveryConstant.DEFAULT);

        blueMetadataComboBox.setModel(new DefaultComboBoxModel<>(metadatas.toArray()));
        greenMetadataComboBox.setModel(new DefaultComboBoxModel<>(metadatas.toArray()));
        basicMetadataComboBox.setModel(new DefaultComboBoxModel<>(metadatas.toArray()));
    }

    private void addNodes(String serviceId, String blueMetadata, String greenMetadata, String basicMetadata, String blueCondition, String greenCondition) {
        TNode newBlueNode = addNode(ButtonManager.getHtmlText(serviceId + "\n[" + strategyType.getLabel() + "=" + blueMetadata + "]"), serviceBlueNodeUI);
        Instance newBlueInstance = new Instance();
        newBlueInstance.setServiceId(serviceId);
        Map<String, String> newBlueMetadataMap = new HashMap<String, String>();
        newBlueMetadataMap.put(strategyType.getValue(), blueMetadata);
        newBlueInstance.setMetadata(newBlueMetadataMap);
        newBlueNode.setUserObject(newBlueInstance);
        newBlueNode.setBusinessObject(NodeType.BLUE);
        if (blueNode == null) {
            TLink blueLink = addLink(gatewayNode, newBlueNode, LinkUI.BLUE);
            blueLink.setDisplayName("蓝路由");
            blueLink.setToolTipText(blueCondition);
            blueLink.setUserObject(blueCondition);
            blueLink.setBusinessObject(LinkType.BLUE);
        } else {
            TLink link = addLink(blueNode, newBlueNode, null);
            link.setBusinessObject(LinkType.UNDEFINED);
        }
        blueNode = newBlueNode;

        TNode newGreenNode = addNode(ButtonManager.getHtmlText(serviceId + "\n[" + strategyType.getLabel() + "=" + greenMetadata + "]"), serviceGreenNodeUI);
        Instance newGreenInstance = new Instance();
        newGreenInstance.setServiceId(serviceId);
        Map<String, String> newGreenMetadataMap = new HashMap<String, String>();
        newGreenMetadataMap.put(strategyType.getValue(), greenMetadata);
        newGreenInstance.setMetadata(newGreenMetadataMap);
        newGreenNode.setUserObject(newGreenInstance);
        newGreenNode.setBusinessObject(NodeType.GREEN);
        if (greenNode == null) {
            TLink greenLink = addLink(gatewayNode, newGreenNode, LinkUI.GREEN);
            greenLink.setDisplayName("绿路由");
            greenLink.setToolTipText(greenCondition);
            greenLink.setUserObject(greenCondition);
            greenLink.setBusinessObject(LinkType.GREEN);
        } else {
            TLink link = addLink(greenNode, newGreenNode, null);
            link.setBusinessObject(LinkType.UNDEFINED);
        }
        greenNode = newGreenNode;

        TNode newBasicNode = addNode(ButtonManager.getHtmlText(serviceId + "\n[" + strategyType.getLabel() + "=" + basicMetadata + "]"), serviceYellowNodeUI);
        Instance newBasicInstance = new Instance();
        newBasicInstance.setServiceId(serviceId);
        Map<String, String> newBasicMetadataMap = new HashMap<String, String>();
        newBasicMetadataMap.put(strategyType.getValue(), basicMetadata);
        newBasicInstance.setMetadata(newBasicMetadataMap);
        newBasicNode.setUserObject(newBasicInstance);
        newBasicNode.setBusinessObject(NodeType.BASIC);
        if (basicNode == null) {
            TLink basicLink = addLink(gatewayNode, newBasicNode, LinkUI.YELLOW);
            basicLink.setDisplayName("兜底路由");
            basicLink.setBusinessObject(LinkType.BASIC);
        } else {
            TLink link = addLink(basicNode, newBasicNode, null);
            link.setBusinessObject(LinkType.UNDEFINED);
        }
        basicNode = newBasicNode;
    }

    @SuppressWarnings("unchecked")
    private void removeNodes() {
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

    @SuppressWarnings({ "unchecked", "incomplete-switch" })
    private void modifyNodes(String serviceId, String blueMetadata, String greenMetadata, String basicMetadata) {
        List<TNode> nodes = TElementManager.getNodes(dataBox);
        for (TNode node : nodes) {
            Instance instance = (Instance) node.getUserObject();
            if (StringUtils.equalsIgnoreCase(instance.getServiceId(), serviceId)) {
                NodeType nodeType = (NodeType) node.getBusinessObject();
                switch (nodeType) {
                    case BLUE:
                        node.setName(ButtonManager.getHtmlText(serviceId + "\n[" + strategyType.getLabel() + "=" + blueMetadata + "]"));
                        instance.getMetadata().put(strategyType.getValue(), blueMetadata);
                        break;
                    case GREEN:
                        node.setName(ButtonManager.getHtmlText(serviceId + "\n[" + strategyType.getLabel() + "=" + greenMetadata + "]"));
                        instance.getMetadata().put(strategyType.getValue(), greenMetadata);
                        break;
                    case BASIC:
                        node.setName(ButtonManager.getHtmlText(serviceId + "\n[" + strategyType.getLabel() + "=" + basicMetadata + "]"));
                        instance.getMetadata().put(strategyType.getValue(), basicMetadata);
                        break;
                }
            }
        }
    }

    private void clearNodes() {
        dataBox.clear();
        dataBox.addElement(gatewayNode);

        blueNode = null;
        greenNode = null;
        basicNode = null;
    }

    @SuppressWarnings("unchecked")
    private boolean hasNodes(String serviceId) {
        List<TNode> nodes = TElementManager.getNodes(dataBox);
        for (TNode node : nodes) {
            Instance instance = (Instance) node.getUserObject();
            if (StringUtils.equalsIgnoreCase(instance.getServiceId(), serviceId)) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings({ "unchecked", "incomplete-switch" })
    private void modifyLinks(String blueCondition, String greenCondition) {
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

    private TNode addNode(String name, NodeUI topologyEntity) {
        TNode node = createNode(name, topologyEntity, nodeLocation, 0);

        dataBox.addElement(node);

        return node;
    }

    @SuppressWarnings("unchecked")
    private TLink addLink(TNode fromNode, TNode toNode, Color linkFlowingColor) {
        List<TLink> links = TElementManager.getLinks(dataBox);
        for (TLink link : links) {
            if (link.getFrom() == fromNode && link.getTo() == toNode) {
                return null;
            }
        }

        TLink link = createLink(fromNode, toNode, linkFlowingColor != null);
        if (linkFlowingColor != null) {
            link.putLinkToArrowColor(Color.yellow);
            link.putLinkFlowing(true);
            link.putLinkFlowingColor(linkFlowingColor);
            link.putLinkFlowingWidth(3);
        }

        dataBox.addElement(link);

        return link;
    }

    private void fromXml() {

    }

    @SuppressWarnings({ "unchecked", "incomplete-switch" })
    private String toXml() {
        if (TElementManager.getNodes(dataBox).size() <= 1) {
            return StringUtils.EMPTY;
        }

        String strategyValue = strategyType.getValue();

        StringBuilder basicStrategyStringBuilder = new StringBuilder();
        StringBuilder blueStrategyStringBuilder = new StringBuilder();
        StringBuilder greenStrategyStringBuilder = new StringBuilder();
        List<TNode> nodes = TElementManager.getNodes(dataBox);
        for (TNode node : nodes) {
            Instance instance = (Instance) node.getUserObject();
            NodeType nodeType = (NodeType) node.getBusinessObject();
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
        String basicStrategy = basicStrategyStringBuilder.toString();
        basicStrategy = basicStrategy.substring(0, basicStrategy.length() - 2);
        String blueStrategy = blueStrategyStringBuilder.toString();
        blueStrategy = blueStrategy.substring(0, blueStrategy.length() - 2);
        String greenStrategy = greenStrategyStringBuilder.toString();
        greenStrategy = greenStrategy.substring(0, greenStrategy.length() - 2);

        String blueCondition = null;
        String greenCondition = null;
        List<TLink> links = TElementManager.getLinks(dataBox);
        for (TLink link : links) {
            LinkType linkType = (LinkType) link.getBusinessObject();
            switch (linkType) {
                case BLUE:
                    blueCondition = link.getUserObject().toString();
                    break;
                case GREEN:
                    greenCondition = link.getUserObject().toString();
                    break;
            }
        }

        StringBuilder strategyStringBuilder = new StringBuilder();
        strategyStringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        strategyStringBuilder.append("<rule>\n");
        strategyStringBuilder.append("    <strategy>\n");
        strategyStringBuilder.append("        <" + strategyValue + ">{" + basicStrategy + "}</" + strategyValue + ">\n");
        strategyStringBuilder.append("    </strategy>\n\n");
        strategyStringBuilder.append("    <strategy-customization>\n");
        strategyStringBuilder.append("        <conditions type=\"" + OperationType.BLUE_GREEN + "\">\n");
        strategyStringBuilder.append("            <condition id=\"blue-condition\" header=\"" + EscapeType.escape(blueCondition) + "\" " + strategyValue + "-id=\"blue-" + strategyValue + "-route\"/>\n");
        strategyStringBuilder.append("            <condition id=\"green-condition\" header=\"" + EscapeType.escape(greenCondition) + "\" " + strategyValue + "-id=\"green-" + strategyValue + "-route\"/>\n");
        strategyStringBuilder.append("        </conditions>\n\n");
        strategyStringBuilder.append("        <routes>\n");
        strategyStringBuilder.append("            <route id=\"blue-" + strategyValue + "-route\" type=\"" + strategyValue + "\">{" + blueStrategy + "}</route>\n");
        strategyStringBuilder.append("            <route id=\"green-" + strategyValue + "-route\" type=\"" + strategyValue + "\">{" + greenStrategy + "}</route>\n");
        strategyStringBuilder.append("        </routes>\n");
        strategyStringBuilder.append("    </strategy-customization>\n");
        strategyStringBuilder.append("</rule>");

        return strategyStringBuilder.toString();
    }

    private void save() {

    }

    private JSecurityAction createRefreshServicesAction() {
        JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("refresh.png"), "刷新服务列表") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                refreshData();
                refreshUI();
            }
        };

        return action;
    }

    private JSecurityAction createMetadataSelectorAction(JBasicComboBox metadataComboBox) {
        JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("direction_south.png"), "版本选取") {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings({ "unchecked", "rawtypes" })
            public void execute(ActionEvent e) {
                ComboBoxModel metadataComboBoxModel = metadataComboBox.getModel();
                List<ElementNode> metadataElementNodes = new ArrayList<ElementNode>();
                for (int i = 0; i < metadataComboBoxModel.getSize(); i++) {
                    String metadata = metadataComboBoxModel.getElementAt(i).toString();
                    metadataElementNodes.add(new ElementNode(metadata, IconFactory.getSwingIcon("component/file_chooser_16.png"), metadata, metadata));
                }

                JCheckBoxSelector checkBoxSelector = new JCheckBoxSelector(HandleManager.getFrame(BlueGreenTopology.this), "版本选取", new Dimension(400, 350), metadataElementNodes);
                checkBoxSelector.setVisible(true);
                checkBoxSelector.dispose();

                if (!checkBoxSelector.isConfirmed()) {
                    return;
                }

                List<String> selectedMetadatas = checkBoxSelector.getSelectedUserObjects();
                if (CollectionUtils.isEmpty(selectedMetadatas)) {
                    return;
                }

                StringBuilder StringBuilder = new StringBuilder();
                int index = 0;
                for (String selectedMetadata : selectedMetadatas) {
                    StringBuilder.append(selectedMetadata);
                    if (index < selectedMetadatas.size() - 1) {
                        StringBuilder.append(DiscoveryConstant.SEPARATE);
                    }

                    index++;
                }

                metadataComboBox.setSelectedItem(StringBuilder.toString());
            }
        };

        return action;
    }

    private JSecurityAction createAddNodesAction() {
        JSecurityAction action = new JSecurityAction("添加", ConsoleIconFactory.getSwingIcon("add.png"), "添加") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                if (TElementManager.getNodes(dataBox).size() == 0) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), "无入口网关或者服务", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                String serviceId = serviceIdComboBox.getSelectedItem() != null ? serviceIdComboBox.getSelectedItem().toString().trim() : null;
                if (StringUtils.isBlank(serviceId)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), "服务名必填", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                if (hasNodes(serviceId)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), serviceId + "已存在", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                String blueMetadata = blueMetadataComboBox.getSelectedItem() != null ? blueMetadataComboBox.getSelectedItem().toString().trim() : null;
                String greenMetadata = greenMetadataComboBox.getSelectedItem() != null ? greenMetadataComboBox.getSelectedItem().toString().trim() : null;
                String basicMetadata = basicMetadataComboBox.getSelectedItem() != null ? basicMetadataComboBox.getSelectedItem().toString().trim() : null;
                String blueCondition = blueConditionTextField.getText().trim();
                String greenCondition = greenConditionTextField.getText().trim();

                if (StringUtils.isBlank(blueMetadata) || StringUtils.isBlank(greenMetadata) || StringUtils.isBlank(basicMetadata)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), "版本号必填", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                if (StringUtils.isBlank(blueCondition) || StringUtils.isBlank(greenCondition)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), "条件必填", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                addNodes(serviceId, blueMetadata, greenMetadata, basicMetadata, blueCondition, greenCondition);

                layoutActionListener.actionPerformed(null);
            }
        };

        return action;
    }

    private JSecurityAction createRemoveNodesAction() {
        JSecurityAction action = new JSecurityAction("删除", ConsoleIconFactory.getSwingIcon("delete.png"), "删除") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                removeNodes();
            }
        };

        return action;
    }

    private JSecurityAction createModifyNodesAction() {
        JSecurityAction action = new JSecurityAction("修改", ConsoleIconFactory.getSwingIcon("paste.png"), "修改") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                String serviceId = serviceIdComboBox.getSelectedItem().toString();
                if (StringUtils.isBlank(serviceId)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), "服务名必填", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                String blueMetadata = blueMetadataComboBox.getSelectedItem().toString().trim();
                String greenMetadata = greenMetadataComboBox.getSelectedItem().toString().trim();
                String basicMetadata = basicMetadataComboBox.getSelectedItem().toString().trim();

                if (StringUtils.isBlank(blueMetadata) || StringUtils.isBlank(greenMetadata) || StringUtils.isBlank(basicMetadata)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), "版本号必填", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                modifyNodes(serviceId, blueMetadata, greenMetadata, basicMetadata);
            }
        };

        return action;
    }

    private JSecurityAction createClearNodesAction() {
        JSecurityAction action = new JSecurityAction("清空", ConsoleIconFactory.getSwingIcon("paint.png"), "清空") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                clearNodes();
            }
        };

        return action;
    }

    private JSecurityAction createValidateAction() {
        JSecurityAction action = new JSecurityAction("校验", ConsoleIconFactory.getSwingIcon("config.png"), "校验") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {

            }
        };

        return action;
    }

    private JSecurityAction createModifyLinksAction() {
        JSecurityAction action = new JSecurityAction("修改", ConsoleIconFactory.getSwingIcon("paste.png"), "修改") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                String blueCondition = blueConditionTextField.getText().trim();
                String greenCondition = greenConditionTextField.getText().trim();

                if (StringUtils.isBlank(blueCondition) || StringUtils.isBlank(greenCondition)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), "条件必填", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                modifyLinks(blueCondition, greenCondition);
            }
        };

        return action;
    }

    private JSecurityAction createPreviewAction() {
        JSecurityAction action = new JSecurityAction("文本预览", ConsoleIconFactory.getSwingIcon("ticket.png"), "文本预览") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                String xml = toXml();
                if (StringUtils.isEmpty(xml)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), "策略为空", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                if (strategyTextArea == null) {
                    strategyTextArea = new JBasicTextArea();
                    strategyScrollPane = new JBasicScrollPane(strategyTextArea);
                    strategyScrollPane.setPreferredSize(new Dimension(900, 400));
                }
                strategyTextArea.setText(xml);

                JBasicOptionPane.showOptionDialog(HandleManager.getFrame(BlueGreenTopology.this), strategyScrollPane, "策略文本预览", JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/property.png"), new Object[] { SwingLocale.getString("close") }, null, true);
            }
        };

        return action;
    }

    private JSecurityAction createInspectorAction() {
        JSecurityAction action = new JSecurityAction("链路侦测", ConsoleIconFactory.getSwingIcon("relation.png"), "链路侦测") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {

            }
        };

        return action;
    }

    private JSecurityAction createRouterAction() {
        JSecurityAction action = new JSecurityAction("路由拓扑", ConsoleIconFactory.getSwingIcon("rotate.png"), "路由拓扑") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                OperationPanel operationPanel = new OperationPanel(OperationType.BLUE_GREEN);
                operationPanel.setPreferredSize(new Dimension(470, 170));

                int selectedOption = JBasicOptionPane.showOptionDialog(HandleManager.getFrame(BlueGreenTopology.this), operationPanel, "新增蓝绿部署", JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/net.png"), new Object[] { SwingLocale.getString("confirm"), SwingLocale.getString("cancel") }, null, true);
                if (selectedOption != 0) {
                    return;
                }

                String operationName = operationPanel.getOperationName();
                if (StringUtils.isBlank(operationName)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), "名称不能为空", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                String group = operationPanel.getGroup();
                if (StringUtils.isBlank(group)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), "组不能为空", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                String gatewayId = operationPanel.getGatewayId();
                if (StringUtils.isBlank(gatewayId)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), "服务名不能为空", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                Instance gateway = new Instance();
                gateway.setServiceId(gatewayId);
                Map<String, String> metadataMap = new HashMap<String, String>();
                gateway.setMetadata(metadataMap);

                OperationType operationType = operationPanel.getOperationType();
                StrategyType strategyType = operationPanel.getStrategyType();
                ConfigType configType = operationPanel.getConfigType();

                initializeData(operationName, group, gateway, operationType, strategyType, configType);
                initializeUI();
            }
        };

        return action;
    }

    private JSecurityAction createLayoutAction() {
        JSecurityAction action = new JSecurityAction("布局", ConsoleIconFactory.getSwingIcon("layout.png"), "布局") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                toggleLayoutBar();
            }
        };

        return action;
    }

    private JSecurityAction createSaveAction() {
        JSecurityAction action = new JSecurityAction("保存", ConsoleIconFactory.getSwingIcon("save.png"), "保存") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                String xml = toXml();
                if (StringUtils.isEmpty(xml)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), "策略为空", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                save();
            }
        };

        return action;
    }

    private class OperationPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        private JBasicTextField operationNameTextField;

        private JBasicComboBox groupComboBox;
        private JBasicComboBox gatewayIdComboBox;
        private JBasicCheckBox showOnlyGatewayCheckBox;

        private ButtonGroup strategyButtonGroup;
        private ButtonGroup configButtonGroup;

        private OperationType operationType;

        public OperationPanel(OperationType operationType) {
            this.operationType = operationType;

            operationNameTextField = new JBasicTextField();

            groupComboBox = new JBasicComboBox();
            groupComboBox.setEditable(true);
            groupComboBox.setPreferredSize(new Dimension(300, layoutTextField.getPreferredSize().height));
            groupComboBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (groupComboBox.getSelectedItem() != e.getItem()) {
                        setGatewayIds();
                    }
                }
            });

            gatewayIdComboBox = new JBasicComboBox();
            gatewayIdComboBox.setEditable(true);
            gatewayIdComboBox.setPreferredSize(new Dimension(300, layoutTextField.getPreferredSize().height));

            showOnlyGatewayCheckBox = new JBasicCheckBox("只显示网关", true);

            double[][] gatewaySize = {
                    { TableLayout.FILL, TableLayout.PREFERRED },
                    { TableLayout.PREFERRED }
            };

            TableLayout gatewayTableLayout = new TableLayout(gatewaySize);
            gatewayTableLayout.setHGap(5);
            gatewayTableLayout.setVGap(5);

            JPanel gatewayPanel = new JPanel();
            gatewayPanel.setLayout(gatewayTableLayout);
            gatewayPanel.add(gatewayIdComboBox, "0, 0");
            gatewayPanel.add(showOnlyGatewayCheckBox, "1, 0");

            JPanel strategyPanel = new JPanel();
            strategyPanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 10));
            strategyButtonGroup = new ButtonGroup();
            StrategyType[] strategyTypes = StrategyType.values();
            for (int i = 0; i < strategyTypes.length; i++) {
                StrategyType strategyType = strategyTypes[i];

                JBasicRadioButton strategyRadioButton = new JBasicRadioButton(strategyType.getDescription(), strategyType.getDescription());
                strategyRadioButton.setName(strategyType.getValue());
                strategyPanel.add(strategyRadioButton);
                strategyButtonGroup.add(strategyRadioButton);

                if (i == 0) {
                    strategyRadioButton.setSelected(true);
                }
            }

            JPanel configPanel = new JPanel();
            configPanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 10));
            configButtonGroup = new ButtonGroup();
            ConfigType[] configTypes = ConfigType.values();
            for (int i = 0; i < configTypes.length; i++) {
                ConfigType configType = configTypes[i];

                JBasicRadioButton configRadioButton = new JBasicRadioButton(configType.getDescription(), configType.getDescription());
                configRadioButton.setName(configType.getValue());
                configPanel.add(configRadioButton);
                configButtonGroup.add(configRadioButton);

                if (i == 0) {
                    configRadioButton.setSelected(true);
                }
            }

            double[][] size = {
                    { TableLayout.PREFERRED, TableLayout.FILL },
                    { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
            };

            TableLayout tableLayout = new TableLayout(size);
            tableLayout.setHGap(10);
            tableLayout.setVGap(5);

            setLayout(tableLayout);
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            add(new JBasicLabel("名称"), "0, 0");
            add(operationNameTextField, "1, 0");
            add(new JBasicLabel("所属组"), "0, 1");
            add(groupComboBox, "1, 1");
            add(new JBasicLabel("所属服务"), "0, 2");
            add(gatewayPanel, "1, 2");
            add(new JBasicLabel("策略"), "0, 3");
            add(strategyPanel, "1, 3");
            add(new JBasicLabel("模式"), "0, 4");
            add(configPanel, "1, 4");

            setGroups();
            setGatewayIds();
        }

        @SuppressWarnings("unchecked")
        private void setGroups() {
            Object[] groups = getGroups();
            if (groups != null) {
                groupComboBox.setModel(new DefaultComboBoxModel<>(groups));
            }
        }

        @SuppressWarnings("unchecked")
        private void setGatewayIds() {
            Object selectedItem = groupComboBox.getSelectedItem();
            if (selectedItem != null) {
                Object[] gatewayIds = getGatewayIds(selectedItem.toString().trim());
                if (gatewayIds != null) {
                    gatewayIdComboBox.setModel(new DefaultComboBoxModel<>(gatewayIds));
                }
            }
        }

        private Object[] getGroups() {
            try {
                return ServiceController.getGroups().toArray();
            } catch (Exception e) {
                JExceptionDialog.traceException(HandleManager.getFrame(this), ConsoleLocaleFactory.getString("query_data_failure"), e);
            }

            return null;
        }

        public Object[] getGatewayIds(String group) {
            try {
                return ServiceController.getInstanceMap(Arrays.asList(group)).keySet().toArray();
            } catch (Exception e) {
                JExceptionDialog.traceException(HandleManager.getFrame(this), ConsoleLocaleFactory.getString("query_data_failure"), e);
            }

            return null;
        }

        private String getRationButtonName(ButtonGroup buttonGroup) {
            for (Enumeration<AbstractButton> enumeration = buttonGroup.getElements(); enumeration.hasMoreElements();) {
                AbstractButton button = enumeration.nextElement();
                if (button.isSelected()) {
                    return button.getName();
                }
            }

            return null;
        }

        public String getOperationName() {
            return operationNameTextField.getText().trim();
        }

        public String getGroup() {
            return groupComboBox.getSelectedItem() != null ? groupComboBox.getSelectedItem().toString().trim() : null;
        }

        public String getGatewayId() {
            return gatewayIdComboBox.getSelectedItem() != null ? gatewayIdComboBox.getSelectedItem().toString().trim() : null;
        }

        public OperationType getOperationType() {
            return operationType;
        }

        public StrategyType getStrategyType() {
            String rationButtonName = getRationButtonName(strategyButtonGroup);

            return StrategyType.fromString(rationButtonName);
        }

        public ConfigType getConfigType() {
            String rationButtonName = getRationButtonName(configButtonGroup);

            return ConfigType.fromString(rationButtonName);
        }
    }
}