package com.nepxion.discovery.console.desktop.workspace;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import twaver.AlarmSeverity;
import twaver.TWaverConst;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.cots.twaver.element.TElementManager;
import com.nepxion.cots.twaver.element.TLink;
import com.nepxion.cots.twaver.element.TNode;
import com.nepxion.cots.twaver.icon.TIconFactory;
import com.nepxion.discovery.common.entity.InspectorEntity;
import com.nepxion.discovery.common.exception.DiscoveryException;
import com.nepxion.discovery.common.util.StringUtil;
import com.nepxion.discovery.console.cache.ConsoleCache;
import com.nepxion.discovery.console.controller.ConsoleController;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.swing.dialog.JExceptionDialog;
import com.nepxion.discovery.console.desktop.common.util.ButtonUtil;
import com.nepxion.discovery.console.desktop.common.util.ComboBoxUtil;
import com.nepxion.discovery.console.desktop.common.util.DimensionUtil;
import com.nepxion.discovery.console.desktop.workspace.panel.InspectorConditionPanel;
import com.nepxion.discovery.console.desktop.workspace.panel.StrategyOpenPanel;
import com.nepxion.discovery.console.desktop.workspace.topology.LinkUI;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeImageType;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeSizeType;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeUI;
import com.nepxion.discovery.console.desktop.workspace.type.FeatureType;
import com.nepxion.discovery.console.desktop.workspace.type.PortalType;
import com.nepxion.discovery.console.desktop.workspace.type.StrategyType;
import com.nepxion.discovery.console.desktop.workspace.type.TypeLocale;
import com.nepxion.discovery.console.entity.Instance;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.ButtonManager;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.optionpane.JBasicOptionPane;
import com.nepxion.swing.shrinkbar.JShrinkShortcut;
import com.nepxion.swing.textfield.JBasicTextField;
import com.nepxion.swing.timer.JTimerProgressBar;

public class InspectorTopology extends AbstractTopology {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(InspectorTopology.class);

    protected NodeUI gatewayNodeUI = new NodeUI(NodeImageType.GATEWAY_BLUE, NodeSizeType.LARGE, true);
    protected NodeUI serviceNodeUI = new NodeUI(NodeImageType.SERVICE_BLUE, NodeSizeType.MIDDLE, true);
    protected Color linkUI = LinkUI.BLUE;

    protected JBasicComboBox portalComboBox;
    protected JBasicComboBox serviceIdComboBox;
    protected JBasicComboBox instanceComboBox;
    protected JBasicTextField parameterTextField;

    protected InspectorConditionPanel conditionPanel;

    protected JBasicComboBox strategyComboBox;
    protected JBasicComboBox timesComboBox;
    protected JTimerProgressBar progressBar;

    protected Pattern pattern = Pattern.compile("\\[\\S+\\]");

    public InspectorTopology() {
        initializeToolBar();
        initializeOperationBar();
    }

    @Override
    public void initializeTopology() {
        super.initializeTopology();

        background.setTitle(TypeLocale.getDescription(FeatureType.INSPECTOR));
    }

    public void initializeToolBar() {
        JToolBar toolBar = getGraph().getToolbar();
        toolBar.addSeparator();
        // toolBar.add(ButtonUtil.createButton(createOpenAction()));
        toolBar.add(ButtonUtil.createButton(createStartAction()));
        toolBar.add(ButtonUtil.createButton(createStopAction()));
        toolBar.addSeparator();
        toolBar.add(ButtonUtil.createButton(createViewAction()));
        toolBar.add(ButtonUtil.createButton(createSetAction()));
        toolBar.addSeparator();
        toolBar.add(ButtonUtil.createButton(createLayoutAction()));

        ButtonManager.updateUI(toolBar);
    }

    @Override
    public void initializeOperationBar() {
        JShrinkShortcut portalShrinkShortcut = new JShrinkShortcut();
        portalShrinkShortcut.setTitle(ConsoleLocaleFactory.getString("inspector_portal"));
        portalShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        portalShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString("inspector_portal"));

        double[][] portalSize = {
                { TableLayout.PREFERRED, TableLayout.FILL },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
        };

        TableLayout portalTableLayout = new TableLayout(portalSize);
        portalTableLayout.setHGap(0);
        portalTableLayout.setVGap(5);

        List<ElementNode> portalElementNodes = new ArrayList<ElementNode>();
        PortalType[] portalTypes = PortalType.values();
        for (int i = 0; i < portalTypes.length; i++) {
            PortalType portalType = portalTypes[i];
            portalElementNodes.add(new ElementNode(portalType.toString(), TypeLocale.getDescription(portalType), null, TypeLocale.getDescription(portalType), portalType));
        }

        portalComboBox = new JBasicComboBox(portalElementNodes.toArray());
        portalComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (portalComboBox.getSelectedItem() != e.getItem()) {
                    setServiceIds();
                    setInstances();
                }
            }
        });

        serviceIdComboBox = new JBasicComboBox();
        serviceIdComboBox.setEditable(true);
        serviceIdComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (serviceIdComboBox.getSelectedItem() != e.getItem()) {
                    setInstances();
                }
            }
        });
        ComboBoxUtil.installlAutoCompletion(serviceIdComboBox);

        instanceComboBox = new JBasicComboBox();
        instanceComboBox.setEditable(true);
        ComboBoxUtil.installlAutoCompletion(instanceComboBox);

        parameterTextField = new JBasicTextField("a=1&b=1");

        setServiceIds();
        setInstances();

        JPanel portalPanel = new JPanel();
        portalPanel.setLayout(portalTableLayout);
        portalPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("type")), 5), "0, 0");
        portalPanel.add(portalComboBox, "1, 0");
        portalPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("name")), 5), "0, 1");
        portalPanel.add(serviceIdComboBox, "1, 1");
        portalPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("address")), 5), "0, 2");
        portalPanel.add(instanceComboBox, "1, 2");
        portalPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("parameter")), 5), "0, 3");
        portalPanel.add(parameterTextField, "1, 3");

        JShrinkShortcut conditionShrinkShortcut = new JShrinkShortcut();
        conditionShrinkShortcut.setTitle(ConsoleLocaleFactory.getString("inspector_link"));
        conditionShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        conditionShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString("inspector_link"));

        conditionPanel = new InspectorConditionPanel();

        JShrinkShortcut parameterShrinkShortcut = new JShrinkShortcut();
        parameterShrinkShortcut.setTitle(ConsoleLocaleFactory.getString("inspector_parameter"));
        parameterShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        parameterShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString("inspector_parameter"));

        double[][] parameterSize = {
                { TableLayout.PREFERRED, TableLayout.FILL },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
        };

        TableLayout parameterTableLayout = new TableLayout(parameterSize);
        parameterTableLayout.setHGap(0);
        parameterTableLayout.setVGap(5);

        List<ElementNode> strategyElementNodes = new ArrayList<ElementNode>();
        StrategyType[] strategyTypes = StrategyType.values();
        for (int i = 0; i < strategyTypes.length; i++) {
            StrategyType strategyType = strategyTypes[i];
            if (strategyType.getCategory() == 0) {
                strategyElementNodes.add(new ElementNode(strategyType.toString(), TypeLocale.getDescription(strategyType), null, TypeLocale.getDescription(strategyType), strategyType));
            }
        }

        strategyComboBox = new JBasicComboBox(strategyElementNodes.toArray());

        Integer[] times = new Integer[] { 10, 20, 50, 100, 200, 500, 1000, 2000 };
        timesComboBox = new JBasicComboBox(times);

        progressBar = new JTimerProgressBar();

        JPanel parameterPanel = new JPanel();
        parameterPanel.setLayout(parameterTableLayout);
        parameterPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("strategy")), 5), "0, 0");
        parameterPanel.add(strategyComboBox, "1, 0");
        parameterPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("times")), 5), "0, 1");
        parameterPanel.add(timesComboBox, "1, 1");
        parameterPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("progress")), 5), "0, 2");
        parameterPanel.add(DimensionUtil.addHeight(progressBar, 3), "1, 2");

        JPanel toolBar = new JPanel();
        toolBar.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 0));
        toolBar.add(new JClassicButton(createRefreshServiceIdAction()));

        double[][] size = {
                { TableLayout.FILL },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, 10, TableLayout.PREFERRED, TableLayout.PREFERRED, 10, TableLayout.PREFERRED, TableLayout.PREFERRED, 10, TableLayout.PREFERRED }
        };

        TableLayout tableLayout = new TableLayout(size);
        tableLayout.setHGap(0);
        tableLayout.setVGap(5);

        operationBar.setLayout(tableLayout);
        operationBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        operationBar.add(portalShrinkShortcut, "0, 0");
        operationBar.add(portalPanel, "0, 1");
        operationBar.add(conditionShrinkShortcut, "0, 3");
        operationBar.add(conditionPanel, "0, 4");
        operationBar.add(parameterShrinkShortcut, "0, 6");
        operationBar.add(parameterPanel, "0, 7");
        operationBar.add(toolBar, "0, 9");
    }

    public void setServiceIds() {
        List<String> serviceIds = null;

        ElementNode portalElementNode = (ElementNode) portalComboBox.getSelectedItem();
        PortalType portalType = (PortalType) portalElementNode.getUserObject();
        switch (portalType) {
            case GATEWAY:
                serviceIds = getServiceIds(true);
                break;
            case SERVICE:
                serviceIds = getServiceIds(false);
                break;
        }

        if (serviceIds != null) {
            ComboBoxUtil.setSortableModel(serviceIdComboBox, serviceIds);
        }
    }

    public void setInstances() {
        String serviceId = ComboBoxUtil.getSelectedValue(serviceIdComboBox);

        List<Instance> instances = getInstances(serviceId);
        if (instances != null) {
            List<String> addresses = new ArrayList<String>();
            for (Instance instance : instances) {
                addresses.add("http://" + instance.getHost() + ":" + instance.getPort());
            }
            ComboBoxUtil.setSortableModel(instanceComboBox, addresses);
        }
    }

    public List<String> getServiceIds(boolean isGateway) {
        try {
            if (isGateway) {
                return ConsoleCache.getGateways();
            } else {
                return ConsoleCache.getRealServices();
            }
        } catch (Exception e) {
            JExceptionDialog.traceException(HandleManager.getFrame(this), ConsoleLocaleFactory.getString("operation_failure"), e);
        }

        return null;
    }

    public List<Instance> getInstances(String serviceId) {
        try {
            return ConsoleController.getInstanceList(serviceId);
        } catch (Exception e) {
            JExceptionDialog.traceException(HandleManager.getFrame(this), ConsoleLocaleFactory.getString("operation_failure"), e);
        }

        return null;
    }

    public List<Map<String, String>> convertToMetadatas(InspectorEntity inspectorEntity) {
        if (inspectorEntity == null) {
            throw new DiscoveryException("Inspector Entity is null");
        }

        String result = inspectorEntity.getResult();
        if (StringUtils.isEmpty(result)) {
            throw new DiscoveryException("Inspector Result is null or empty");
        }

        List<Map<String, String>> metadataList = new ArrayList<Map<String, String>>();

        List<String> expressionList = StringUtil.splitToList(result, " -> ");
        for (String expression : expressionList) {
            expression = StringUtils.replace(expression, "][", "] [");
            Matcher matcher = pattern.matcher(expression);

            Map<String, String> metadataMap = new LinkedHashMap<String, String>();
            while (matcher.find()) {
                String group = matcher.group();
                String value = StringUtils.substringBetween(group, "[", "]");
                String[] expressionArray = StringUtils.split(value, "=");
                metadataMap.put(expressionArray[0], expressionArray[1]);
            }

            metadataList.add(metadataMap);
        }

        return metadataList;
    }

    public TNode addNode(TNode previousNode, StrategyType strategyType, Map<String, String> metadataMap, NodeUI nodeUI, int times) {
        String serviceId = metadataMap.get("ID");
        String version = metadataMap.get("V");
        String nodeName = ButtonManager.getHtmlText(serviceId + "\n" + strategyType + "=" + version);

        TNode node = TElementManager.getNode(dataBox, nodeName);
        if (node == null) {
            node = addNode(nodeName, nodeUI);
            setNodeLabelPosition(node, TWaverConst.POSITION_RIGHT);

            Instance instance = new Instance();
            instance.setServiceId(serviceId);
            instance.setMetadata(metadataMap);
            node.setToolTipText(nodeName);
            node.setUserObject(instance);
        }

        if (previousNode != null) {
            TLink link = addLink(previousNode, node, linkUI);
            link.getAlarmState().addNewAlarm(AlarmSeverity.WARNING);
            link.putAlarmBalloonOutlineColor(LinkUI.GRAY);
            link.putLabelPosition(TWaverConst.POSITION_HOTSPOT);

            int count = 1;
            Object userObject = link.getUserObject();
            if (userObject != null) {
                count = (int) userObject + 1;
            }

            DecimalFormat format = new DecimalFormat("0.0000");
            double percent = Double.valueOf(format.format((double) count * 100 / times));

            String text = ConsoleLocaleFactory.getString("weight") + "=" + percent + "%";
            link.setName(text);
            link.setToolTipText(text);
            link.setUserObject(count);
        }

        return node;
    }

    public void start() {
        dataBox.clear();

        String address = ComboBoxUtil.getSelectedValue(instanceComboBox);
        String parameter = parameterTextField.getText().trim();

        ElementNode portalElementNode = (ElementNode) portalComboBox.getSelectedItem();
        PortalType portalType = (PortalType) portalElementNode.getUserObject();

        if (portalType == PortalType.GATEWAY) {
            String firstServiceId = conditionPanel.getFirstServiceId();

            address += "/" + firstServiceId + "/inspector/inspect?" + parameter;
        } else {
            address += "/inspector/inspect?" + parameter;
        }

        LOG.info("Inspection URL={}", address);

        List<String> allServiceIds = conditionPanel.getServiceIds(true);
        List<String> serviceIds = conditionPanel.getServiceIds(false);

        LOG.info("Inspection Services={}", allServiceIds);

        InspectorEntity inspectorEntity = new InspectorEntity();
        inspectorEntity.setServiceIdList(serviceIds);

        int times = Integer.valueOf(timesComboBox.getSelectedItem().toString());
        try {
            for (int i = 0; i < times; i++) {
                InspectorEntity resultInspectorEntity = ConsoleController.inspect(address, inspectorEntity);
                List<Map<String, String>> metadataList = convertToMetadatas(resultInspectorEntity);

                TNode node = null;
                int index = 0;
                for (Map<String, String> metadataMap : metadataList) {
                    NodeUI nodeUI = index == 0 ? gatewayNodeUI : serviceNodeUI;
                    node = addNode(node, StrategyType.VERSION, metadataMap, nodeUI, times);

                    index++;
                }
            }

            executeLayout();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JSecurityAction createOpenAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("open_text"), ConsoleIconFactory.getSwingIcon("theme/tree/plastic/tree_open.png"), ConsoleLocaleFactory.getString("open_strategy_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                StrategyOpenPanel openPanel = new StrategyOpenPanel();
                openPanel.setPreferredSize(new Dimension(openPanel.getPreferredSize().width + 100, openPanel.getPreferredSize().height + 10));

                int selectedOption = JBasicOptionPane.showOptionDialog(HandleManager.getFrame(InspectorTopology.this), openPanel, ConsoleLocaleFactory.getString("open_strategy_tooltip"), JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/net.png"), new Object[] { SwingLocale.getString("confirm"), SwingLocale.getString("cancel") }, null, true);
                if (selectedOption != 0) {
                    return;
                }
            }
        };

        return action;
    }

    public JSecurityAction createStartAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("start_text"), TIconFactory.getContextIcon("run.png"), ConsoleLocaleFactory.getString("start_inspector_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                start();
            }
        };

        return action;
    }

    public JSecurityAction createStopAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("stop_text"), TIconFactory.getContextIcon("stop.png"), ConsoleLocaleFactory.getString("stop_inspector_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {

            }
        };

        return action;
    }

    public JSecurityAction createViewAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("view_text"), ConsoleIconFactory.getSwingIcon("ticket.png"), ConsoleLocaleFactory.getString("view_metadata_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {

            }
        };

        return action;
    }

    public JSecurityAction createRefreshServiceIdAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("refresh_service_list_tooltip"), ConsoleIconFactory.getSwingIcon("netbean/rotate_16.png"), ConsoleLocaleFactory.getString("refresh_service_list_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                setServiceIds();
                setInstances();

                conditionPanel.setServiceIds();
            }
        };

        return action;
    }
}