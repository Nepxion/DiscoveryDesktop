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
import com.nepxion.discovery.console.desktop.workspace.panel.CreatePanel;
import com.nepxion.discovery.console.desktop.workspace.panel.GrayCreatePanel;
import com.nepxion.discovery.console.desktop.workspace.processor.GrayStrategyProcessor;
import com.nepxion.discovery.console.desktop.workspace.processor.StrategyProcessor;
import com.nepxion.discovery.console.desktop.workspace.topology.LinkUI;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeImageType;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeSizeType;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeUI;
import com.nepxion.discovery.console.desktop.workspace.type.ElementType;
import com.nepxion.discovery.console.desktop.workspace.type.ReleaseType;
import com.nepxion.discovery.console.desktop.workspace.type.TypeLocale;
import com.nepxion.discovery.console.entity.Instance;
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

public class GrayTopology extends AbstractReleaseTopology {
    private static final long serialVersionUID = 1L;

    protected NodeUI serviceGrayNodeUI = new NodeUI(NodeImageType.SERVICE_GRAY, NodeSizeType.MIDDLE, true);
    protected NodeUI serviceStableNodeUI = new NodeUI(NodeImageType.SERVICE_GREEN, NodeSizeType.MIDDLE, true);
    protected Color grayLinkUI = LinkUI.GRAY;
    protected Color stableLinkUI = LinkUI.GREEN;

    protected JPanel conditionToolBar;
    protected JPanel conditionPanel;
    protected JPanel orchestrationPanel;
    protected JPanel orchestrationToolBar;

    protected JBasicComboBox grayConditionComboBox;
    protected JBasicComboBox stableConditionComboBox;
    protected JBasicComboBox grayMetadataComboBox;
    protected JBasicComboBox stableMetadataComboBox;

    protected TNode grayNode;
    protected TNode stableNode;

    protected boolean isGrayConditionTriggered = false;
    protected boolean isStableConditionTriggered = false;

    protected StrategyProcessor strategyProcessor = new GrayStrategyProcessor();

    public GrayTopology() {
        super(ReleaseType.GRAY);
    }

    @Override
    public void initializeOperationBar() {
        JShrinkShortcut conditionShrinkShortcut = new JShrinkShortcut();
        conditionShrinkShortcut.setTitle(ConsoleLocaleFactory.getString(releaseType.toString() + "_condition"));
        conditionShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        conditionShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString(releaseType.toString() + "_condition"));

        String[] conditions = { "0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "65", "70", "75", "80", "85", "90", "95", "100" };
        grayConditionComboBox = new JBasicComboBox(conditions);
        grayConditionComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (grayConditionComboBox.getSelectedItem() != e.getItem()) {
                    isGrayConditionTriggered = true;
                    if (!isStableConditionTriggered) {
                        setConditionUI(grayConditionComboBox);
                    }
                    isGrayConditionTriggered = false;
                }
            }
        });
        stableConditionComboBox = new JBasicComboBox(conditions);
        stableConditionComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (stableConditionComboBox.getSelectedItem() != e.getItem()) {
                    isStableConditionTriggered = true;
                    if (!isGrayConditionTriggered) {
                        setConditionUI(stableConditionComboBox);
                    }
                    isStableConditionTriggered = false;
                }
            }
        });
        stableConditionComboBox.setSelectedItem(conditions[conditions.length - 1]);

        double[][] conditionSize = {
                { TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED },
                { TableLayout.PREFERRED, TableLayout.PREFERRED }
        };

        TableLayout conditionTableLayout = new TableLayout(conditionSize);
        conditionTableLayout.setHGap(0);
        conditionTableLayout.setVGap(5);

        conditionPanel = new JPanel();
        conditionPanel.setLayout(conditionTableLayout);
        conditionPanel.add(DimensionUtil.addWidth(new JBasicLabel(TypeLocale.getDescription(ElementType.GRAY)), 5), "0, 0");
        conditionPanel.add(grayConditionComboBox, "1, 0");
        conditionPanel.add(new JBasicLabel("%"), "2, 0");
        conditionPanel.add(DimensionUtil.addWidth(new JBasicLabel(TypeLocale.getDescription(ElementType.STABLE)), 5), "0, 1");
        conditionPanel.add(stableConditionComboBox, "1, 1");
        conditionPanel.add(new JBasicLabel("%"), "2, 1");

        conditionToolBar = new JPanel();
        conditionToolBar.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 0));
        conditionToolBar.add(new JClassicButton(createModifyConditionAction()));

        JShrinkShortcut orchestrationShrinkShortcut = new JShrinkShortcut();
        orchestrationShrinkShortcut.setTitle(ConsoleLocaleFactory.getString(releaseType.toString() + "_orchestration"));
        orchestrationShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        orchestrationShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString(releaseType.toString() + "_orchestration"));

        serviceIdComboBox = new JBasicComboBox();
        serviceIdComboBox.setEditable(true);
        serviceIdComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (serviceIdComboBox.getSelectedItem() != e.getItem()) {
                    setMetadataUI();
                }
            }
        });
        JClassicButton refreshServiceIdButton = new JClassicButton(createRefreshServiceIdAction());
        DimensionUtil.setWidth(refreshServiceIdButton, 30);

        grayMetadataComboBox = new JBasicComboBox();
        grayMetadataComboBox.setEditable(true);
        JClassicButton grayMetadataButton = new JClassicButton(createMetadataSelectorAction(grayMetadataComboBox));
        DimensionUtil.setWidth(grayMetadataButton, 30);

        stableMetadataComboBox = new JBasicComboBox();
        stableMetadataComboBox.setEditable(true);
        JClassicButton stableMetadataButton = new JClassicButton(createMetadataSelectorAction(stableMetadataComboBox));
        DimensionUtil.setWidth(stableMetadataButton, 30);

        double[][] orchestrationSize = {
                { TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
        };

        TableLayout orchestrationTableLayout = new TableLayout(orchestrationSize);
        orchestrationTableLayout.setHGap(0);
        orchestrationTableLayout.setVGap(5);

        orchestrationPanel = new JPanel();
        orchestrationPanel.setLayout(orchestrationTableLayout);
        orchestrationPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("service")), 5), "0, 0");
        orchestrationPanel.add(serviceIdComboBox, "1, 0");
        orchestrationPanel.add(refreshServiceIdButton, "2, 0");
        orchestrationPanel.add(DimensionUtil.addWidth(new JBasicLabel(TypeLocale.getDescription(ElementType.GRAY)), 5), "0, 1");
        orchestrationPanel.add(grayMetadataComboBox, "1, 1");
        orchestrationPanel.add(grayMetadataButton, "2, 1");
        orchestrationPanel.add(DimensionUtil.addWidth(new JBasicLabel(TypeLocale.getDescription(ElementType.STABLE)), 5), "0, 2");
        orchestrationPanel.add(stableMetadataComboBox, "1, 2");
        orchestrationPanel.add(stableMetadataButton, "2, 2");

        orchestrationToolBar = new JPanel();
        orchestrationToolBar.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 0));
        orchestrationToolBar.add(new JClassicButton(createAddServiceStrategyAction()));
        orchestrationToolBar.add(new JClassicButton(createModifyServiceStrategyAction()));

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
        operationBar.add(orchestrationShrinkShortcut, "0, 4");
        operationBar.add(orchestrationPanel, "0, 5");
        operationBar.add(orchestrationToolBar, "0, 6");
    }

    public void setConditionUI(JBasicComboBox comboBox) {
        int onePercent = Integer.parseInt(ComboBoxUtil.getSelectedValue(comboBox));
        int anotherPercent = 100 - onePercent;
        if (comboBox == grayConditionComboBox) {
            stableConditionComboBox.setSelectedItem(String.valueOf(anotherPercent));
        } else if (comboBox == stableConditionComboBox) {
            grayConditionComboBox.setSelectedItem(String.valueOf(anotherPercent));
        }
    }

    public void addNodes(String serviceId, String grayMetadata, String stableMetadata, String grayCondition, String stableCondition) {
        TNode newGrayNode = addNode(ButtonManager.getHtmlText(serviceId + "\n" + strategyType.toString() + "=" + grayMetadata), serviceGrayNodeUI);
        Instance newGrayInstance = new Instance();
        newGrayInstance.setServiceId(serviceId);
        Map<String, String> newGrayMetadataMap = new HashMap<String, String>();
        newGrayMetadataMap.put(strategyType.toString(), grayMetadata);
        newGrayInstance.setMetadata(newGrayMetadataMap);
        newGrayNode.setUserObject(newGrayInstance);
        newGrayNode.setBusinessObject(ElementType.GRAY);
        if (grayNode == null) {
            TLink grayLink = addLink(gatewayNode, newGrayNode, grayLinkUI);
            grayLink.setName(ButtonManager.getHtmlText(ConsoleLocaleFactory.getString("gray_route") + "\n" + grayCondition + "%"));
            grayLink.setToolTipText(grayCondition + "%");
            grayLink.setUserObject(grayCondition);
            grayLink.setBusinessObject(ElementType.GRAY);
        } else {
            TLink link = addLink(grayNode, newGrayNode, null);
            link.setBusinessObject(ElementType.UNDEFINED);
        }
        grayNode = newGrayNode;

        TNode newStableNode = addNode(ButtonManager.getHtmlText(serviceId + "\n" + strategyType.toString() + "=" + stableMetadata), serviceStableNodeUI);
        Instance newStableInstance = new Instance();
        newStableInstance.setServiceId(serviceId);
        Map<String, String> newStableMetadataMap = new HashMap<String, String>();
        newStableMetadataMap.put(strategyType.toString(), stableMetadata);
        newStableInstance.setMetadata(newStableMetadataMap);
        newStableNode.setUserObject(newStableInstance);
        newStableNode.setBusinessObject(ElementType.STABLE);
        if (stableNode == null) {
            TLink stableLink = addLink(gatewayNode, newStableNode, stableLinkUI);
            stableLink.setName(ButtonManager.getHtmlText(ConsoleLocaleFactory.getString("stable_route") + "\n" + stableCondition + "%"));
            stableLink.setToolTipText(stableCondition + "%");
            stableLink.setUserObject(stableCondition);
            stableLink.setBusinessObject(ElementType.STABLE);
        } else {
            TLink link = addLink(stableNode, newStableNode, null);
            link.setBusinessObject(ElementType.UNDEFINED);
        }
        stableNode = newStableNode;
    }

    @SuppressWarnings("unchecked")
    public void removeNodes() {
        if (grayNode != null) {
            List<Link> grayLinks = grayNode.getAllLinks();
            if (CollectionUtils.isNotEmpty(grayLinks)) {
                TNode currentGrayNode = (TNode) grayLinks.get(0).getFrom();
                dataBox.removeElement(grayNode);
                if (currentGrayNode != gatewayNode) {
                    grayNode = currentGrayNode;
                } else {
                    grayNode = null;
                }
            }
        }

        if (stableNode != null) {
            List<Link> stableLinks = stableNode.getAllLinks();
            if (CollectionUtils.isNotEmpty(stableLinks)) {
                TNode currentStableNode = (TNode) stableLinks.get(0).getFrom();
                dataBox.removeElement(stableNode);
                if (currentStableNode != gatewayNode) {
                    stableNode = currentStableNode;
                } else {
                    stableNode = null;
                }
            }
        }
    }

    public void resetNodes() {
        grayNode = null;
        stableNode = null;
    }

    @SuppressWarnings({ "unchecked", "incomplete-switch" })
    public void modifyNodes(String serviceId, String grayMetadata, String stableMetadata) {
        List<TNode> nodes = TElementManager.getNodes(dataBox);
        for (TNode node : nodes) {
            Instance instance = (Instance) node.getUserObject();
            if (StringUtils.equalsIgnoreCase(instance.getServiceId(), serviceId)) {
                ElementType nodeType = (ElementType) node.getBusinessObject();
                switch (nodeType) {
                    case GRAY:
                        node.setName(ButtonManager.getHtmlText(serviceId + "\n" + strategyType.toString() + "=" + grayMetadata));
                        instance.getMetadata().put(strategyType.toString(), grayMetadata);
                        break;
                    case STABLE:
                        node.setName(ButtonManager.getHtmlText(serviceId + "\n" + strategyType.toString() + "=" + stableMetadata));
                        instance.getMetadata().put(strategyType.toString(), stableMetadata);
                        break;
                }
            }
        }
    }

    @SuppressWarnings({ "unchecked", "incomplete-switch" })
    public void modifyLinks(String grayCondition, String stableCondition) {
        List<TLink> links = TElementManager.getLinks(dataBox);
        for (TLink link : links) {
            ElementType linkType = (ElementType) link.getBusinessObject();
            switch (linkType) {
                case GRAY:
                    link.setName(ButtonManager.getHtmlText(ConsoleLocaleFactory.getString("gray_route") + "\n" + grayCondition + "%"));
                    link.setToolTipText(grayCondition);
                    link.setUserObject(grayCondition);
                    break;
                case STABLE:
                    link.setName(ButtonManager.getHtmlText(ConsoleLocaleFactory.getString("stable_route") + "\n" + stableCondition + "%"));
                    link.setToolTipText(stableCondition);
                    link.setUserObject(stableCondition);
                    break;
            }
        }
    }

    @Override
    public CreatePanel getCreatePanel() {
        return new GrayCreatePanel();
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
        grayMetadataComboBox.setModel(new DefaultComboBoxModel<>(metadatas));
        stableMetadataComboBox.setModel(new DefaultComboBoxModel<>(metadatas));
    }

    @Override
    public void addServiceStrategy(String serviceId) {
        String grayMetadata = ComboBoxUtil.getSelectedValue(grayMetadataComboBox);
        String stableMetadata = ComboBoxUtil.getSelectedValue(stableMetadataComboBox);
        String grayCondition = ComboBoxUtil.getSelectedValue(grayConditionComboBox);
        String stableCondition = ComboBoxUtil.getSelectedValue(stableConditionComboBox);

        if (StringUtils.isBlank(grayMetadata) || StringUtils.isBlank(stableMetadata)) {
            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(GrayTopology.this), TypeLocale.getName(strategyType) + " " + ConsoleLocaleFactory.getString("not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

            return;
        }

        if (StringUtils.isBlank(grayCondition) || StringUtils.isBlank(stableCondition)) {
            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(GrayTopology.this), ConsoleLocaleFactory.getString("condition_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

            return;
        }

        addNodes(serviceId, grayMetadata, stableMetadata, grayCondition, stableCondition);

        executeLayout();
    }

    @Override
    public void modifyServiceStrategy(String serviceId) {
        String grayMetadata = ComboBoxUtil.getSelectedValue(grayMetadataComboBox);
        String stableMetadata = ComboBoxUtil.getSelectedValue(stableMetadataComboBox);

        if (StringUtils.isBlank(grayMetadata) || StringUtils.isBlank(stableMetadata)) {
            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(GrayTopology.this), TypeLocale.getName(strategyType) + " " + ConsoleLocaleFactory.getString("not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

            return;
        }

        modifyNodes(serviceId, grayMetadata, stableMetadata);
    }

    @Override
    public void modifyCondition() {
        String grayCondition = ComboBoxUtil.getSelectedValue(grayConditionComboBox);
        String stableCondition = ComboBoxUtil.getSelectedValue(stableConditionComboBox);

        if (StringUtils.isBlank(grayCondition) || StringUtils.isBlank(stableCondition)) {
            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(GrayTopology.this), ConsoleLocaleFactory.getString("condition_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

            return;
        }

        modifyLinks(grayCondition, stableCondition);
    }

    @Override
    public StrategyProcessor getStrategyProcessor() {
        return strategyProcessor;
    }
}