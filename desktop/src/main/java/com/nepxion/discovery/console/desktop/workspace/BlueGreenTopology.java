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

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import com.nepxion.discovery.console.desktop.workspace.processor.BlueGreenStrategyProcessor;
import com.nepxion.discovery.console.desktop.workspace.processor.StrategyProcessor;
import com.nepxion.discovery.console.desktop.workspace.topology.LinkUI;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeImageType;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeSizeType;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeUI;
import com.nepxion.discovery.console.desktop.workspace.type.LinkType;
import com.nepxion.discovery.console.desktop.workspace.type.NodeType;
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
import com.nepxion.swing.textfield.JBasicTextField;

public class BlueGreenTopology extends AbstractReleaseTopology {
    private static final long serialVersionUID = 1L;

    protected NodeUI serviceYellowNodeUI = new NodeUI(NodeImageType.SERVICE_YELLOW, NodeSizeType.MIDDLE, true);
    protected NodeUI serviceBlueNodeUI = new NodeUI(NodeImageType.SERVICE_BLUE, NodeSizeType.MIDDLE, true);
    protected NodeUI serviceGreenNodeUI = new NodeUI(NodeImageType.SERVICE_GREEN, NodeSizeType.MIDDLE, true);

    protected JBasicComboBox blueMetadataComboBox;
    protected JBasicComboBox greenMetadataComboBox;
    protected JBasicComboBox basicMetadataComboBox;
    protected JBasicTextField blueConditionTextField;
    protected JBasicTextField greenConditionTextField;

    protected TNode blueNode;
    protected TNode greenNode;
    protected TNode basicNode;

    protected StrategyProcessor strategyProcessor = new BlueGreenStrategyProcessor();

    public BlueGreenTopology() {
        super();

        initializeContentBar();
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
        JClassicButton refreshServicesButton = new JClassicButton(createRefreshServiceListAction());
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
        serviceToolBar.add(new JClassicButton(createAddServiceStrategyAction()));
        serviceToolBar.add(new JClassicButton(createRemoveServiceStrategyAction()));
        serviceToolBar.add(new JClassicButton(createModifyServiceStrategyAction()));

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
        conditionToolBar.add(new JClassicButton(createValidateConditionAction()));
        conditionToolBar.add(new JClassicButton(createModifyConditionAction()));

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
            TLink blueLink = addLink(gatewayNode, newBlueNode, LinkUI.BLUE);
            blueLink.setDisplayName("蓝版本路由");
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
            TLink greenLink = addLink(gatewayNode, newGreenNode, LinkUI.GREEN);
            greenLink.setDisplayName("绿版本路由");
            greenLink.setToolTipText(greenCondition);
            greenLink.setUserObject(greenCondition);
            greenLink.setBusinessObject(LinkType.GREEN);
        } else {
            TLink link = addLink(greenNode, newGreenNode, null);
            link.setBusinessObject(LinkType.UNDEFINED);
        }
        greenNode = newGreenNode;

        TNode newBasicNode = addNode(ButtonManager.getHtmlText(serviceId + "\n" + strategyType.toString() + "=" + basicMetadata), serviceYellowNodeUI);
        Instance newBasicInstance = new Instance();
        newBasicInstance.setServiceId(serviceId);
        Map<String, String> newBasicMetadataMap = new HashMap<String, String>();
        newBasicMetadataMap.put(strategyType.toString(), basicMetadata);
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

    public JSecurityAction createValidateConditionAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("validate_text"), ConsoleIconFactory.getSwingIcon("config.png"), ConsoleLocaleFactory.getString("validate_condition_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {

            }
        };

        return action;
    }

    public JSecurityAction createModifyConditionAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("modify_text"), ConsoleIconFactory.getSwingIcon("paste.png"), ConsoleLocaleFactory.getString("modify_condition_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                String blueCondition = blueConditionTextField.getText().trim();
                String greenCondition = greenConditionTextField.getText().trim();

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
    public void reset() {
        blueNode = null;
        greenNode = null;
        basicNode = null;
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
            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), ConsoleLocaleFactory.getString("condition_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

            return;
        }

        addNodes(serviceId, blueMetadata, greenMetadata, basicMetadata, blueCondition, greenCondition);

        layoutActionListener.actionPerformed(null);
    }

    @Override
    public void removeServiceStrategy() {
        removeNodes();
    }

    @Override
    public void modifyServiceStrategy(String serviceId) {
        String blueMetadata = blueMetadataComboBox.getSelectedItem().toString().trim();
        String greenMetadata = greenMetadataComboBox.getSelectedItem().toString().trim();
        String basicMetadata = basicMetadataComboBox.getSelectedItem().toString().trim();

        if (StringUtils.isBlank(blueMetadata) || StringUtils.isBlank(greenMetadata) || StringUtils.isBlank(basicMetadata)) {
            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), "版本号必填", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

            return;
        }

        modifyNodes(serviceId, blueMetadata, greenMetadata, basicMetadata);
    }

    @Override
    public StrategyProcessor getStrategyProcessor() {
        return strategyProcessor;
    }
}