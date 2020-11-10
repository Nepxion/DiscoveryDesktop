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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.nepxion.cots.twaver.element.TElementManager;
import com.nepxion.cots.twaver.element.TLink;
import com.nepxion.cots.twaver.element.TNode;
import com.nepxion.cots.twaver.graph.TGraphBackground;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.console.desktop.entity.Instance;
import com.nepxion.discovery.console.desktop.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.topology.AbstractTopology;
import com.nepxion.discovery.console.desktop.topology.LinkUI;
import com.nepxion.discovery.console.desktop.topology.NodeImageType;
import com.nepxion.discovery.console.desktop.topology.NodeLocation;
import com.nepxion.discovery.console.desktop.topology.NodeSizeType;
import com.nepxion.discovery.console.desktop.topology.NodeUI;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.ButtonManager;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.icon.IconFactory;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.listener.DisplayAbilityListener;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.optionpane.JBasicOptionPane;
import com.nepxion.swing.selector.checkbox.JCheckBoxSelector;
import com.nepxion.swing.textfield.JBasicTextField;

public class BlueGreenTopology extends AbstractTopology {
    private static final long serialVersionUID = 1L;

    private static final String GATEWAY_NODE = "GATEWAY-NODE";
    private static final String BLUE_NODE = "BLUE-NODE";
    private static final String GREEN_NODE = "GREEN-NODE";
    private static final String BASIC_NODE = "BASIC-NODE";

    private static final String BLUE_LINK = "BLUE-LINK";
    private static final String GREEN_LINK = "GREEN-LINK";
    private static final String BASIC_LINK = "BASIC-LINK";

    private static final String VERSION = "version";

    private NodeLocation nodeLocation = new NodeLocation(100, 200, 200, 0);
    private NodeUI gatewayBlackNodeUI = new NodeUI(NodeImageType.GATEWAY_BLACK, NodeSizeType.LARGE, true);
    private NodeUI serviceYellowNodeUI = new NodeUI(NodeImageType.SERVICE_YELLOW, NodeSizeType.MIDDLE, true);
    private NodeUI serviceBlueNodeUI = new NodeUI(NodeImageType.SERVICE_BLUE, NodeSizeType.MIDDLE, true);
    private NodeUI serviceGreenNodeUI = new NodeUI(NodeImageType.SERVICE_GREEN, NodeSizeType.MIDDLE, true);

    private TGraphBackground background;
    private JBasicComboBox serviceIdComboBox;
    private JBasicTextField blueMetadataTextField;
    private JBasicTextField greenMetadataTextField;
    private JBasicTextField basicMetadataTextField;
    private JBasicTextField blueConditionTextField;
    private JBasicTextField greenConditionTextField;

    private TNode gatewayNode;
    private TNode blueNode;
    private TNode greenNode;
    private TNode basicNode;

    public BlueGreenTopology() {
        initializeContentBar();
        initializeToolBar();
        initializeTopology();
        initializeListener();

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void initializeContentBar() {
        blueMetadataTextField = new JBasicTextField();
        blueMetadataTextField.setPreferredSize(new Dimension(150, blueMetadataTextField.getPreferredSize().height));
        JClassicButton blueMetadataButton = new JClassicButton(createMetadataSelectorAction(blueMetadataTextField));
        blueMetadataButton.setPreferredSize(new Dimension(30, blueMetadataButton.getPreferredSize().height));

        greenMetadataTextField = new JBasicTextField();
        greenMetadataTextField.setPreferredSize(new Dimension(150, greenMetadataTextField.getPreferredSize().height));
        JClassicButton greenMetadataButton = new JClassicButton(createMetadataSelectorAction(greenMetadataTextField));
        greenMetadataButton.setPreferredSize(new Dimension(30, greenMetadataButton.getPreferredSize().height));

        basicMetadataTextField = new JBasicTextField();
        basicMetadataTextField.setPreferredSize(new Dimension(150, basicMetadataTextField.getPreferredSize().height));
        JClassicButton basicMetadataButton = new JClassicButton(createMetadataSelectorAction(basicMetadataTextField));
        basicMetadataButton.setPreferredSize(new Dimension(30, basicMetadataButton.getPreferredSize().height));

        String[] serviceIds = new String[] { "serivice-a", "serivice-b", "serivice-c", "serivice-d", "serivice-e", "serivice-f" };
        serviceIdComboBox = new JBasicComboBox(serviceIds);
        serviceIdComboBox.setEditable(true);
        serviceIdComboBox.setPreferredSize(new Dimension(250, basicMetadataTextField.getPreferredSize().height));

        JPanel servicePanel = new JPanel();
        servicePanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 0));
        servicePanel.add(new JBasicLabel("蓝绿服务"));
        servicePanel.add(Box.createHorizontalStrut(10));
        servicePanel.add(serviceIdComboBox);
        servicePanel.add(Box.createHorizontalStrut(10));
        servicePanel.add(new JBasicLabel("蓝版本"));
        servicePanel.add(Box.createHorizontalStrut(10));
        servicePanel.add(blueMetadataTextField);
        servicePanel.add(blueMetadataButton);
        servicePanel.add(Box.createHorizontalStrut(10));
        servicePanel.add(new JBasicLabel("绿版本"));
        servicePanel.add(Box.createHorizontalStrut(10));
        servicePanel.add(greenMetadataTextField);
        servicePanel.add(greenMetadataButton);
        servicePanel.add(Box.createHorizontalStrut(10));
        servicePanel.add(new JBasicLabel("兜底版本"));
        servicePanel.add(Box.createHorizontalStrut(10));
        servicePanel.add(basicMetadataTextField);
        servicePanel.add(basicMetadataButton);
        servicePanel.add(Box.createHorizontalStrut(10));
        servicePanel.add(new JClassicButton(createAddServiceAction()));
        servicePanel.add(new JClassicButton(createRemoveServiceAction()));
        servicePanel.add(new JClassicButton(createModifyServiceAction()));

        blueConditionTextField = new JBasicTextField("#H['a'] == '1' && #H['b'] <= '2'");
        blueConditionTextField.setPreferredSize(new Dimension(436, blueConditionTextField.getPreferredSize().height));

        greenConditionTextField = new JBasicTextField("#H['a'] == '3'");
        greenConditionTextField.setPreferredSize(new Dimension(436, greenConditionTextField.getPreferredSize().height));

        JPanel conditionPanel = new JPanel();
        conditionPanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 0));
        conditionPanel.add(new JBasicLabel("蓝绿条件"));
        conditionPanel.add(Box.createHorizontalStrut(10));
        conditionPanel.add(new JBasicLabel("蓝条件"));
        conditionPanel.add(Box.createHorizontalStrut(10));
        conditionPanel.add(blueConditionTextField);
        conditionPanel.add(Box.createHorizontalStrut(10));
        conditionPanel.add(new JBasicLabel("绿条件"));
        conditionPanel.add(Box.createHorizontalStrut(10));
        conditionPanel.add(greenConditionTextField);
        conditionPanel.add(Box.createHorizontalStrut(10));
        conditionPanel.add(new JClassicButton("校验", ConsoleIconFactory.getSwingIcon("config.png")));
        conditionPanel.add(new JClassicButton("使用", ConsoleIconFactory.getSwingIcon("paste.png")));

        JPanel toolBar = new JPanel();
        toolBar.setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 5));
        toolBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        toolBar.add(servicePanel);
        toolBar.add(conditionPanel);

        add(toolBar, BorderLayout.NORTH);
    }

    private void initializeToolBar() {
        JClassicButton previewButton = new JClassicButton("文本预览", ConsoleIconFactory.getSwingIcon("ticket.png"));

        JClassicButton controlButton = (JClassicButton) graph.getToolbar().getComponent(0);
        controlButton.setPreferredSize(new Dimension(controlButton.getPreferredSize().width + 5, previewButton.getPreferredSize().height));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(controlButton);
        buttonPanel.add(previewButton);
        buttonPanel.add(new JClassicButton("链路侦测", ConsoleIconFactory.getSwingIcon("relation.png")));
        buttonPanel.add(new JClassicButton("路由拓扑", ConsoleIconFactory.getSwingIcon("rotate.png")));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(new JClassicButton("布局", ConsoleIconFactory.getSwingIcon("layout.png")));
        buttonPanel.add(new JClassicButton("保存", ConsoleIconFactory.getSwingIcon("save.png")));
        buttonPanel.add(new JClassicButton("清空", ConsoleIconFactory.getSwingIcon("paint.png")));

        JPanel toolBar = new JPanel();
        toolBar.setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 5));
        toolBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        toolBar.add(buttonPanel);

        add(toolBar, BorderLayout.SOUTH);
    }

    private void initializeTopology() {
        background = graph.getGraphBackground();
        background.setTitle("My Blue Green | My Group | 蓝绿部署 | 版本策略 | 局部订阅模式 ");
        graph.setElementStateOutlineColorGenerator(new Generator() {
            public Object generate(Object object) {
                return null;
            }
        });
        graph.getToolbar().setVisible(false);

        gatewayNode = addNode("Discovery Gateway", gatewayBlackNodeUI);
        Instance gatewayInstance = new Instance();
        gatewayInstance.setServiceId("Discovery Gateway");
        gatewayNode.setUserObject(gatewayInstance);
        gatewayNode.setBusinessObject(GATEWAY_NODE);
    }

    private void initializeListener() {
        addHierarchyListener(new DisplayAbilityListener() {
            public void displayAbilityChanged(HierarchyEvent e) {
                showLayoutBar(150, 50, 200, 60);

                removeHierarchyListener(this);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private boolean hasNode(String serviceId) {
        List<TNode> nodes = TElementManager.getNodes(dataBox);
        for (TNode node : nodes) {
            Instance instance = (Instance) node.getUserObject();
            if (StringUtils.equalsIgnoreCase(instance.getServiceId(), serviceId)) {
                return true;
            }
        }

        return false;
    }

    private void addNode(String serviceId, String blueMetadata, String greenMetadata, String basicMetadata, String blueCondition, String greenCondition) {
        TNode newBlueNode = addNode(ButtonManager.getHtmlText(serviceId + "\n[" + StringUtils.capitalize(VERSION) + "=" + blueMetadata + "]"), serviceBlueNodeUI);
        Instance newBlueInstance = new Instance();
        newBlueInstance.setServiceId(serviceId);
        Map<String, String> newBlueMetadataMap = new HashMap<String, String>();
        newBlueMetadataMap.put(VERSION, blueMetadata);
        newBlueInstance.setMetadata(newBlueMetadataMap);
        newBlueNode.setUserObject(newBlueInstance);
        newBlueNode.setBusinessObject(BLUE_NODE);
        if (blueNode == null) {
            TLink blueLink = addLink(gatewayNode, newBlueNode, LinkUI.BLUE);
            blueLink.setDisplayName("蓝路由");
            blueLink.setToolTipText(blueCondition);
            blueLink.setBusinessObject(BLUE_LINK);
        } else {
            addLink(blueNode, newBlueNode, null);
        }
        blueNode = newBlueNode;

        TNode newGreenNode = addNode(ButtonManager.getHtmlText(serviceId + "\n[" + StringUtils.capitalize(VERSION) + "=" + greenMetadata + "]"), serviceGreenNodeUI);
        Instance newGreenInstance = new Instance();
        newGreenInstance.setServiceId(serviceId);
        Map<String, String> newGreenMetadataMap = new HashMap<String, String>();
        newGreenMetadataMap.put(VERSION, greenMetadata);
        newGreenInstance.setMetadata(newGreenMetadataMap);
        newGreenNode.setUserObject(newGreenInstance);
        newGreenNode.setBusinessObject(GREEN_NODE);
        if (greenNode == null) {
            TLink greenLink = addLink(gatewayNode, newGreenNode, LinkUI.GREEN);
            greenLink.setDisplayName("绿路由");
            greenLink.setToolTipText(greenCondition);
            greenLink.setBusinessObject(GREEN_LINK);
        } else {
            addLink(greenNode, newGreenNode, null);
        }
        greenNode = newGreenNode;

        TNode newBasicNode = addNode(ButtonManager.getHtmlText(serviceId + "\n[" + StringUtils.capitalize(VERSION) + "=" + basicMetadata + "]"), serviceYellowNodeUI);
        Instance newBasicInstance = new Instance();
        newBasicInstance.setServiceId(serviceId);
        Map<String, String> newBasicMetadataMap = new HashMap<String, String>();
        newBasicMetadataMap.put(VERSION, basicMetadata);
        newBasicInstance.setMetadata(newBasicMetadataMap);
        newBasicNode.setUserObject(newBasicInstance);
        newBasicNode.setBusinessObject(BASIC_NODE);
        if (basicNode == null) {
            TLink basicLink = addLink(gatewayNode, newBasicNode, LinkUI.YELLOW);
            basicLink.setDisplayName("兜底路由");
        } else {
            addLink(basicNode, newBasicNode, null);
        }
        basicNode = newBasicNode;
    }

    @SuppressWarnings("unchecked")
    private void removeNode() {
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

    @SuppressWarnings("unchecked")
    private void modifyNode(String serviceId, String blueMetadata, String greenMetadata, String basicMetadata) {
        List<TNode> nodes = TElementManager.getNodes(dataBox);
        for (TNode node : nodes) {
            Instance instance = (Instance) node.getUserObject();
            if (StringUtils.equalsIgnoreCase(instance.getServiceId(), serviceId)) {
                if (StringUtils.equals(node.getBusinessObject().toString(), BLUE_NODE)) {
                    node.setName(ButtonManager.getHtmlText(serviceId + "\n[" + StringUtils.capitalize(VERSION) + "=" + blueMetadata + "]"));
                    instance.getMetadata().put(VERSION, blueMetadata);
                } else if (StringUtils.equals(node.getBusinessObject().toString(), GREEN_NODE)) {
                    node.setName(ButtonManager.getHtmlText(serviceId + "\n[" + StringUtils.capitalize(VERSION) + "=" + greenMetadata + "]"));
                    instance.getMetadata().put(VERSION, greenMetadata);
                } else if (StringUtils.equals(node.getBusinessObject().toString(), BASIC_NODE)) {
                    node.setName(ButtonManager.getHtmlText(serviceId + "\n[" + StringUtils.capitalize(VERSION) + "=" + basicMetadata + "]"));
                    instance.getMetadata().put(VERSION, basicMetadata);
                }
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

    private JSecurityAction createAddServiceAction() {
        JSecurityAction action = new JSecurityAction("添加", ConsoleIconFactory.getSwingIcon("add.png"), "添加") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                String serviceId = serviceIdComboBox.getSelectedItem().toString();
                if (StringUtils.isBlank(serviceId)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), "服务名必填", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                if (hasNode(serviceId)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), serviceId + "已存在", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                String blueMetadata = blueMetadataTextField.getText().trim();
                String greenMetadata = greenMetadataTextField.getText().trim();
                String basicMetadata = basicMetadataTextField.getText().trim();
                String blueCondition = blueConditionTextField.getText().trim();
                String greenCondition = greenConditionTextField.getText().trim();

                if (StringUtils.isBlank(blueMetadata) || StringUtils.isBlank(greenMetadata) || StringUtils.isBlank(basicMetadata)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), "版本号必填", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                addNode(serviceId, blueMetadata, greenMetadata, basicMetadata, blueCondition, greenCondition);

                layoutActionListener.actionPerformed(null);
            }
        };

        return action;
    }

    private JSecurityAction createRemoveServiceAction() {
        JSecurityAction action = new JSecurityAction("删除", ConsoleIconFactory.getSwingIcon("delete.png"), "删除") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                removeNode();
            }
        };

        return action;
    }

    private JSecurityAction createModifyServiceAction() {
        JSecurityAction action = new JSecurityAction("修改", ConsoleIconFactory.getSwingIcon("property.png"), "修改") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                String serviceId = serviceIdComboBox.getSelectedItem().toString();
                if (StringUtils.isBlank(serviceId)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), "服务名必填", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                String blueMetadata = blueMetadataTextField.getText().trim();
                String greenMetadata = greenMetadataTextField.getText().trim();
                String basicMetadata = basicMetadataTextField.getText().trim();

                if (StringUtils.isBlank(blueMetadata) || StringUtils.isBlank(greenMetadata) || StringUtils.isBlank(basicMetadata)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), "版本号必填", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                modifyNode(serviceId, blueMetadata, greenMetadata, basicMetadata);
            }
        };

        return action;
    }

    private JSecurityAction createMetadataSelectorAction(JBasicTextField metadataTextField) {
        JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("direction_south.png"), "版本选取") {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            public void execute(ActionEvent e) {
                String[] metadatas = new String[] { "20201111-001", "20201111-002", "20201111-003", DiscoveryConstant.DEFAULT };

                List<ElementNode> metadataElementNodes = new ArrayList<ElementNode>();
                for (String metadata : metadatas) {
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

                metadataTextField.setText(StringBuilder.toString());
            }
        };

        return action;
    }
}