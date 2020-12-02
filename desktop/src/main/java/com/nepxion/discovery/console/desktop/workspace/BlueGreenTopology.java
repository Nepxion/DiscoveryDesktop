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
import javax.swing.JPanel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.nepxion.cots.twaver.element.TElementManager;
import com.nepxion.cots.twaver.element.TLink;
import com.nepxion.cots.twaver.element.TNode;
import com.nepxion.discovery.common.entity.BlueGreenRouteType;
import com.nepxion.discovery.common.entity.ElementType;
import com.nepxion.discovery.common.entity.StrategyConditionBlueGreenEntity;
import com.nepxion.discovery.common.entity.StrategyHeaderEntity;
import com.nepxion.discovery.common.util.StringUtil;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.util.ComboBoxUtil;
import com.nepxion.discovery.console.desktop.common.util.DimensionUtil;
import com.nepxion.discovery.console.desktop.common.util.TextFieldUtil;
import com.nepxion.discovery.console.desktop.workspace.panel.BlueGreenConditionPanel;
import com.nepxion.discovery.console.desktop.workspace.panel.BlueGreenCreatePanel;
import com.nepxion.discovery.console.desktop.workspace.panel.StrategyCreatePanel;
import com.nepxion.discovery.console.desktop.workspace.processor.ReleaseProcessor;
import com.nepxion.discovery.console.desktop.workspace.processor.ReleaseProcessorUtil;
import com.nepxion.discovery.console.desktop.workspace.processor.strategy.BlueGreenStrategyReleaseProcessor;
import com.nepxion.discovery.console.desktop.workspace.topology.LinkUI;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeImageType;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeSizeType;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeUI;
import com.nepxion.discovery.console.desktop.workspace.type.ReleaseType;
import com.nepxion.discovery.console.desktop.workspace.type.TypeLocale;
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
import com.nepxion.swing.textfield.JBasicTextField;

public class BlueGreenTopology extends AbstractStrategyTopology {
    private static final long serialVersionUID = 1L;

    protected NodeUI serviceBlueNodeUI = new NodeUI(NodeImageType.SERVICE_BLUE, NodeSizeType.MIDDLE, true);
    protected NodeUI serviceGreenNodeUI = new NodeUI(NodeImageType.SERVICE_GREEN, NodeSizeType.MIDDLE, true);
    protected NodeUI serviceBasicNodeUI = new NodeUI(NodeImageType.SERVICE_YELLOW, NodeSizeType.MIDDLE, true);
    protected Color blueLinkUI = LinkUI.BLUE;
    protected Color greenLinkUI = LinkUI.GREEN;
    protected Color basicLinkUI = LinkUI.YELLOW;

    protected BlueGreenConditionPanel conditionPanel;
    protected JPanel conditionToolBar;
    protected JPanel orchestrationPanel;
    protected JPanel orchestrationToolBar;
    protected JPanel parameterPanel;
    protected JPanel parameterToolBar;

    protected JBasicComboBox blueMetadataComboBox;
    protected JBasicComboBox greenMetadataComboBox;
    protected JBasicComboBox basicMetadataComboBox;
    protected JBasicTextField parameterTextField;

    protected BlueGreenCreatePanel createPanel;

    protected TNode blueNode;
    protected TNode greenNode;
    protected TNode basicNode;

    protected BlueGreenRouteType blueGreenRouteType;

    protected BlueGreenStrategyReleaseProcessor releaseProcessor = new BlueGreenStrategyReleaseProcessor();

    public BlueGreenTopology() {
        super(ReleaseType.BLUE_GREEN);

        releaseProcessor.setBlueGreenTopology(this);
    }

    @Override
    public void initializeTopology() {
        super.initializeTopology();

        background.setTitle(TypeLocale.getDescription(ReleaseType.BLUE_GREEN));
    }

    public void initializeBlueGreenRouteType(BlueGreenRouteType blueGreenRouteType) {
        boolean enabled = blueGreenRouteType == BlueGreenRouteType.BLUE_GREEN_BASIC;
        orchestrationPanel.getComponent(6).setEnabled(enabled);
        orchestrationPanel.getComponent(7).setEnabled(enabled);
        orchestrationPanel.getComponent(8).setEnabled(enabled);
        conditionPanel.setGreenConditionBarEnabled(enabled);
    }

    @Override
    public void initializeUI(StrategyCreatePanel createPanel) {
        if (createPanel != null) {
            BlueGreenCreatePanel blueGreenCreatePanel = (BlueGreenCreatePanel) createPanel;
            blueGreenRouteType = blueGreenCreatePanel.getBlueGreenRouteType();
        } else {
            blueGreenRouteType = ReleaseProcessorUtil.getBlueGreenRouteType(ruleEntity, strategyType);
        }

        initializeBlueGreenRouteType(blueGreenRouteType);

        super.initializeUI(createPanel);
    }

    @Override
    public void initializeView() {
        blueGreenRouteType = ReleaseProcessorUtil.getBlueGreenRouteType(ruleEntity, strategyType);

        initializeBlueGreenRouteType(blueGreenRouteType);

        super.initializeView();

        String blueConditionId = ReleaseProcessorUtil.getStrategyBlueConditionId();
        String greenConditionId = ReleaseProcessorUtil.getStrategyGreenConditionId();
        StrategyConditionBlueGreenEntity strategyConditionBlueEntity = ReleaseProcessorUtil.getStrategyConditionBlueGreenEntity(ruleEntity, blueConditionId);
        StrategyConditionBlueGreenEntity strategyConditionGreenEntity = ReleaseProcessorUtil.getStrategyConditionBlueGreenEntity(ruleEntity, greenConditionId);

        StrategyHeaderEntity strategyHeaderEntity = ruleEntity.getStrategyCustomizationEntity().getStrategyHeaderEntity();

        if (strategyConditionBlueEntity != null) {
            String blueCondition = strategyConditionBlueEntity.getConditionHeader();
            if (blueCondition != null) {
                conditionPanel.setBlueCondition(blueCondition);
            }
        }

        if (strategyConditionGreenEntity != null) {
            String greenCondition = strategyConditionGreenEntity.getConditionHeader();
            if (greenCondition != null) {
                conditionPanel.setGreenCondition(greenCondition);
            }
        }

        if (strategyHeaderEntity != null) {
            Map<String, String> parameterMap = strategyHeaderEntity.getHeaderMap();
            String parameter = StringUtil.convertToString(parameterMap);
            parameterTextField.setText(parameter);
        }
    }

    @Override
    public void initializeOperationBar() {
        JShrinkShortcut conditionShrinkShortcut = new JShrinkShortcut();
        conditionShrinkShortcut.setTitle(TypeLocale.getDescription(releaseType, "_condition"));
        conditionShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        conditionShrinkShortcut.setToolTipText(TypeLocale.getDescription(releaseType, "_condition"));

        conditionPanel = new BlueGreenConditionPanel();

        conditionToolBar = new JPanel();
        conditionToolBar.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 0));
        conditionToolBar.add(new JClassicButton(createModifyConditionAction()));

        JShrinkShortcut orchestrationShrinkShortcut = new JShrinkShortcut();
        orchestrationShrinkShortcut.setTitle(TypeLocale.getDescription(releaseType, "_orchestration"));
        orchestrationShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        orchestrationShrinkShortcut.setToolTipText(TypeLocale.getDescription(releaseType, "_orchestration"));

        serviceIdComboBox = new JBasicComboBox();
        serviceIdComboBox.setEditable(true);
        serviceIdComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (serviceIdComboBox.getSelectedItem() != e.getItem()) {
                    setMetadataUI();
                }
            }
        });
        ComboBoxUtil.installlAutoCompletion(serviceIdComboBox);
        JClassicButton refreshServiceIdButton = new JClassicButton(createRefreshServiceIdAction());
        DimensionUtil.setWidth(refreshServiceIdButton, 30);

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
        orchestrationPanel.add(DimensionUtil.addWidth(new JBasicLabel(TypeLocale.getDescription(ElementType.BLUE)), 5), "0, 1");
        orchestrationPanel.add(blueMetadataComboBox, "1, 1");
        orchestrationPanel.add(blueMetadataButton, "2, 1");
        orchestrationPanel.add(DimensionUtil.addWidth(new JBasicLabel(TypeLocale.getDescription(ElementType.GREEN)), 5), "0, 2");
        orchestrationPanel.add(greenMetadataComboBox, "1, 2");
        orchestrationPanel.add(greenMetadataButton, "2, 2");
        orchestrationPanel.add(DimensionUtil.addWidth(new JBasicLabel(TypeLocale.getDescription(ElementType.BASIC)), 5), "0, 3");
        orchestrationPanel.add(basicMetadataComboBox, "1, 3");
        orchestrationPanel.add(basicMetadataButton, "2, 3");

        orchestrationToolBar = new JPanel();
        orchestrationToolBar.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 0));
        orchestrationToolBar.add(new JClassicButton(createAddServiceStrategyAction()));
        orchestrationToolBar.add(new JClassicButton(createModifyServiceStrategyAction()));

        JShrinkShortcut parameterShrinkShortcut = new JShrinkShortcut();
        parameterShrinkShortcut.setTitle(ConsoleLocaleFactory.getString("blue-green_parameter"));
        parameterShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        parameterShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString("blue-green_parameter"));

        double[][] parameterSize = {
                { TableLayout.PREFERRED, TableLayout.FILL },
                { TableLayout.PREFERRED }
        };

        TableLayout parameterTableLayout = new TableLayout(parameterSize);
        parameterTableLayout.setHGap(0);
        parameterTableLayout.setVGap(5);

        parameterTextField = new JBasicTextField();
        parameterPanel = new JPanel();
        parameterPanel.setLayout(parameterTableLayout);
        parameterPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("parameter")), 5), "0, 0");
        parameterPanel.add(TextFieldUtil.setTip(parameterTextField, ConsoleLocaleFactory.getString("blue-green_parameter_example")), "1, 0");

        parameterToolBar = new JPanel();
        parameterToolBar.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 0));
        parameterToolBar.add(new JClassicButton(createModifyParameterAction()));

        double[][] size = {
                { TableLayout.FILL },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, 10, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, 10, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
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
        operationBar.add(parameterShrinkShortcut, "0, 8");
        operationBar.add(parameterPanel, "0, 9");
        operationBar.add(parameterToolBar, "0, 10");
    }

    public void addNodes(String serviceId, String blueMetadata, String greenMetadata, String basicMetadata, String blueCondition, String greenCondition) {
        TNode newBlueNode = addNode(ButtonManager.getHtmlText(serviceId + "\n" + strategyType + "=" + blueMetadata), serviceBlueNodeUI);
        Instance newBlueInstance = new Instance();
        newBlueInstance.setServiceId(serviceId);
        Map<String, String> newBlueMetadataMap = new HashMap<String, String>();
        newBlueMetadataMap.put(strategyType.toString(), blueMetadata);
        newBlueInstance.setMetadata(newBlueMetadataMap);
        newBlueNode.setUserObject(newBlueInstance);
        newBlueNode.setBusinessObject(ElementType.BLUE);
        if (blueNode == null) {
            TLink blueLink = addLink(gatewayNode, newBlueNode, blueLinkUI);
            blueLink.setName(ConsoleLocaleFactory.getString("blue_route"));
            blueLink.setToolTipText(blueCondition);
            blueLink.setUserObject(blueCondition);
            blueLink.setBusinessObject(ElementType.BLUE);
        } else {
            TLink link = addLink(blueNode, newBlueNode, null);
            link.setBusinessObject(ElementType.UNDEFINED);
        }
        blueNode = newBlueNode;

        if (blueGreenRouteType == BlueGreenRouteType.BLUE_GREEN_BASIC) {
            TNode newGreenNode = addNode(ButtonManager.getHtmlText(serviceId + "\n" + strategyType + "=" + greenMetadata), serviceGreenNodeUI);
            Instance newGreenInstance = new Instance();
            newGreenInstance.setServiceId(serviceId);
            Map<String, String> newGreenMetadataMap = new HashMap<String, String>();
            newGreenMetadataMap.put(strategyType.toString(), greenMetadata);
            newGreenInstance.setMetadata(newGreenMetadataMap);
            newGreenNode.setUserObject(newGreenInstance);
            newGreenNode.setBusinessObject(ElementType.GREEN);
            if (greenNode == null) {
                TLink greenLink = addLink(gatewayNode, newGreenNode, greenLinkUI);
                greenLink.setName(ConsoleLocaleFactory.getString("green_route"));
                greenLink.setToolTipText(greenCondition);
                greenLink.setUserObject(greenCondition);
                greenLink.setBusinessObject(ElementType.GREEN);
            } else {
                TLink link = addLink(greenNode, newGreenNode, null);
                link.setBusinessObject(ElementType.UNDEFINED);
            }
            greenNode = newGreenNode;
        }

        TNode newBasicNode = addNode(ButtonManager.getHtmlText(serviceId + "\n" + strategyType + "=" + basicMetadata), serviceBasicNodeUI);
        Instance newBasicInstance = new Instance();
        newBasicInstance.setServiceId(serviceId);
        Map<String, String> newBasicMetadataMap = new HashMap<String, String>();
        newBasicMetadataMap.put(strategyType.toString(), basicMetadata);
        newBasicInstance.setMetadata(newBasicMetadataMap);
        newBasicNode.setUserObject(newBasicInstance);
        newBasicNode.setBusinessObject(ElementType.BASIC);
        if (basicNode == null) {
            TLink basicLink = addLink(gatewayNode, newBasicNode, basicLinkUI);
            basicLink.setName(ConsoleLocaleFactory.getString("basic_route"));
            basicLink.setBusinessObject(ElementType.BASIC);
        } else {
            TLink link = addLink(basicNode, newBasicNode, null);
            link.setBusinessObject(ElementType.UNDEFINED);
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
                ElementType nodeType = (ElementType) node.getBusinessObject();
                switch (nodeType) {
                    case BLUE:
                        node.setName(ButtonManager.getHtmlText(serviceId + "\n" + strategyType + "=" + blueMetadata));
                        instance.getMetadata().put(strategyType.toString(), blueMetadata);
                        break;
                    case GREEN:
                        node.setName(ButtonManager.getHtmlText(serviceId + "\n" + strategyType + "=" + greenMetadata));
                        instance.getMetadata().put(strategyType.toString(), greenMetadata);
                        break;
                    case BASIC:
                        node.setName(ButtonManager.getHtmlText(serviceId + "\n" + strategyType + "=" + basicMetadata));
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
            ElementType linkType = (ElementType) link.getBusinessObject();
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

    public BlueGreenRouteType getBlueGreenRouteType() {
        return blueGreenRouteType;
    }

    @Override
    public ReleaseType getReleaseType() {
        return ReleaseType.BLUE_GREEN;
    }

    @Override
    public StrategyCreatePanel getCreatePanel() {
        if (createPanel == null) {
            createPanel = new BlueGreenCreatePanel();
            DimensionUtil.addSize(createPanel, 100, 10);
        }

        return createPanel;
    }

    @Override
    public void remove() {
        removeNodes();
    }

    @Override
    public void reset() {
        resetNodes();
    }

    @Override
    public void setMetadataUI(List<String> metadatas) {
        ComboBoxUtil.setSortableModel(blueMetadataComboBox, metadatas);
        ComboBoxUtil.setSortableModel(greenMetadataComboBox, metadatas);
        ComboBoxUtil.setSortableModel(basicMetadataComboBox, metadatas);
    }

    @Override
    public boolean addServiceStrategy(String serviceId) {
        String blueMetadata = ComboBoxUtil.getSelectedValue(blueMetadataComboBox);
        String greenMetadata = ComboBoxUtil.getSelectedValue(greenMetadataComboBox);
        String basicMetadata = ComboBoxUtil.getSelectedValue(basicMetadataComboBox);
        String blueCondition = conditionPanel.getBlueCondition();
        String greenCondition = conditionPanel.getGreenCondition();

        if (StringUtils.isBlank(blueMetadata) || (blueGreenRouteType == BlueGreenRouteType.BLUE_GREEN_BASIC && StringUtils.isBlank(greenMetadata)) || StringUtils.isBlank(basicMetadata)) {
            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), TypeLocale.getName(strategyType) + " " + ConsoleLocaleFactory.getString("not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

            return false;
        }

        addNodes(serviceId, blueMetadata, greenMetadata, basicMetadata, blueCondition, greenCondition);

        return true;
    }

    public JSecurityAction createModifyParameterAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("modify_text"), ConsoleIconFactory.getSwingIcon("netbean/canvas_16.png"), ConsoleLocaleFactory.getString("modify_parameter_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                modifyParameter();
            }
        };

        return action;
    }

    @Override
    public boolean modifyServiceStrategy(String serviceId) {
        String blueMetadata = ComboBoxUtil.getSelectedValue(blueMetadataComboBox);
        String greenMetadata = ComboBoxUtil.getSelectedValue(greenMetadataComboBox);
        String basicMetadata = ComboBoxUtil.getSelectedValue(basicMetadataComboBox);

        if (StringUtils.isBlank(blueMetadata) || (blueGreenRouteType == BlueGreenRouteType.BLUE_GREEN_BASIC && StringUtils.isBlank(greenMetadata)) || StringUtils.isBlank(basicMetadata)) {
            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenTopology.this), TypeLocale.getName(strategyType) + " " + ConsoleLocaleFactory.getString("not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

            return false;
        }

        modifyNodes(serviceId, blueMetadata, greenMetadata, basicMetadata);

        return true;
    }

    @Override
    public boolean modifyCondition() {
        String blueCondition = conditionPanel.getBlueCondition();
        String greenCondition = conditionPanel.getGreenCondition();

        if (StringUtils.isBlank(blueCondition)) {
            conditionPanel.showBlueConditionNotNullTip();

            return false;
        }

        if (blueGreenRouteType == BlueGreenRouteType.BLUE_GREEN_BASIC && StringUtils.isBlank(greenCondition)) {
            conditionPanel.showGreenConditionNotNullTip();

            return false;
        }

        if (blueCondition.contains("#H['']")) {
            conditionPanel.showBlueConditionInvalidFormatTip();

            return false;
        }

        if (blueGreenRouteType == BlueGreenRouteType.BLUE_GREEN_BASIC && greenCondition.contains("#H['']")) {
            conditionPanel.showGreenConditionInvalidFormatTip();

            return false;
        }

        modifyLinks(blueCondition, greenCondition);

        return true;
    }

    @Override
    public boolean modifyParameter() {
        String parameter = parameterTextField.getText().trim();
        if (StringUtils.equals(parameter, ConsoleLocaleFactory.getString("blue-green_parameter_example"))) {
            parameter = "";
        }

        Map<String, String> map = null;
        try {
            map = StringUtil.splitToMap(parameter);
        } catch (Exception ex) {
            showParameterInvalidFormatTip();

            return false;
        }

        dataBox.setID(map);

        return true;
    }

    public void showParameterInvalidFormatTip() {
        parameterTextField.showTip(ConsoleLocaleFactory.getString("parameter_invalid_format"), ConsoleIconFactory.getSwingIcon("error_message.png"), 1, 12);
    }

    @Override
    public ReleaseProcessor getReleaseProcessor() {
        return releaseProcessor;
    }
}