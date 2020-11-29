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
import twaver.Element;
import twaver.Generator;
import twaver.TWaverConst;
import twaver.network.ui.ElementUI;

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
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.cots.twaver.element.TElementManager;
import com.nepxion.cots.twaver.element.TLink;
import com.nepxion.cots.twaver.element.TNode;
import com.nepxion.cots.twaver.icon.TIconFactory;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
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
import com.nepxion.discovery.console.desktop.common.util.TextFieldUtil;
import com.nepxion.discovery.console.desktop.workspace.panel.InspectorConditionPanel;
import com.nepxion.discovery.console.desktop.workspace.panel.StrategyOpenPanel;
import com.nepxion.discovery.console.desktop.workspace.topology.LinkUI;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeImageType;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeSizeType;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeUI;
import com.nepxion.discovery.console.desktop.workspace.type.DimensionType;
import com.nepxion.discovery.console.desktop.workspace.type.FeatureType;
import com.nepxion.discovery.console.desktop.workspace.type.PortalType;
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
import com.nepxion.swing.swingworker.JSwingWorker;
import com.nepxion.swing.textfield.JBasicTextField;

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

    protected JBasicComboBox dimensionComboBox;
    protected JBasicComboBox timesComboBox;
    protected JProgressBar progressBar;
    protected JBasicTextField spentTextField;

    protected long currentTime;

    protected Pattern pattern = Pattern.compile("\\[\\S+\\]");

    public InspectorTopology() {
        initializeToolBar();
        initializeOperationBar();
    }

    @Override
    public void initializeTopology() {
        super.initializeTopology();

        graph.setAlarmLabelGenerator(new Generator() {
            public Object generate(Object ui) {
                ElementUI elementUI = (ElementUI) ui;
                Element element = elementUI.getElement();
                if (element instanceof TLink) {
                    int alarmCount = element.getAlarmState().getAlarmCount();

                    return "<html>" + ConsoleLocaleFactory.getString("times") + "=<font color=red>" + alarmCount + "</font></html>";
                }

                return null;
            }
        });

        setTitle(null);
    }

    public void initializeToolBar() {
        JToolBar toolBar = getGraph().getToolbar();
        toolBar.addSeparator();
        // toolBar.add(ButtonUtil.createButton(createOpenAction()));
        toolBar.add(ButtonUtil.createButton(createLaunchAction()));
        toolBar.addSeparator();
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

        parameterTextField = new JBasicTextField();

        setServiceIds();
        setInstances();

        double[][] portalSize = {
                { TableLayout.PREFERRED, TableLayout.FILL },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
        };

        TableLayout portalTableLayout = new TableLayout(portalSize);
        portalTableLayout.setHGap(0);
        portalTableLayout.setVGap(5);

        JPanel portalPanel = new JPanel();
        portalPanel.setLayout(portalTableLayout);
        portalPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("type")), 5), "0, 0");
        portalPanel.add(portalComboBox, "1, 0");
        portalPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("name")), 5), "0, 1");
        portalPanel.add(serviceIdComboBox, "1, 1");
        portalPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("address")), 5), "0, 2");
        portalPanel.add(instanceComboBox, "1, 2");
        portalPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("parameter")), 5), "0, 3");
        portalPanel.add(TextFieldUtil.setTip(parameterTextField, ConsoleLocaleFactory.getString("inspector_parameter_example")), "1, 3");

        JShrinkShortcut conditionShrinkShortcut = new JShrinkShortcut();
        conditionShrinkShortcut.setTitle(ConsoleLocaleFactory.getString("inspector_link"));
        conditionShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        conditionShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString("inspector_link"));

        conditionPanel = new InspectorConditionPanel();

        JShrinkShortcut parameterShrinkShortcut = new JShrinkShortcut();
        parameterShrinkShortcut.setTitle(ConsoleLocaleFactory.getString("inspector_parameter"));
        parameterShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        parameterShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString("inspector_parameter"));

        List<ElementNode> dimensionElementNodes = new ArrayList<ElementNode>();
        DimensionType[] dimensionTypes = DimensionType.values();
        for (int i = 0; i < dimensionTypes.length; i++) {
            DimensionType dimensionType = dimensionTypes[i];
            dimensionElementNodes.add(new ElementNode(dimensionType.toString(), TypeLocale.getDescription(dimensionType), null, TypeLocale.getDescription(dimensionType), dimensionType));
        }

        dimensionComboBox = new JBasicComboBox(dimensionElementNodes.toArray());

        Integer[] times = new Integer[] { 5, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 20000 };
        timesComboBox = new JBasicComboBox(times);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);

        spentTextField = new JBasicTextField("0");

        double[][] parameterSize = {
                { TableLayout.PREFERRED, TableLayout.FILL },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
        };

        TableLayout parameterTableLayout = new TableLayout(parameterSize);
        parameterTableLayout.setHGap(0);
        parameterTableLayout.setVGap(5);

        JPanel parameterPanel = new JPanel();
        parameterPanel.setLayout(parameterTableLayout);
        parameterPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("dimension")), 5), "0, 0");
        parameterPanel.add(dimensionComboBox, "1, 0");
        parameterPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("times")), 5), "0, 1");
        parameterPanel.add(timesComboBox, "1, 1");
        parameterPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("progress")), 5), "0, 2");
        parameterPanel.add(DimensionUtil.addHeight(progressBar, 6), "1, 2");
        parameterPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("spent")), 5), "0, 3");
        parameterPanel.add(spentTextField, "1, 3");

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

    public void setTitle(DimensionType dimensionType) {
        background.setTitle(TypeLocale.getDescription(FeatureType.INSPECTOR) + (dimensionType != null ? " | " + TypeLocale.getDescription(dimensionType) : ""));
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

    public TNode addNode(TNode previousNode, Map<String, String> metadataMap, NodeUI nodeUI, int times) {
        ElementNode dimensionElementNode = (ElementNode) dimensionComboBox.getSelectedItem();
        DimensionType dimensionType = (DimensionType) dimensionElementNode.getUserObject();
        String dimensionKey = dimensionType.getKey();
        String dimensionValue = dimensionType.getValue();

        String serviceId = metadataMap.get("ID");
        String metadata = metadataMap.get(dimensionKey);
        String nodeName = ButtonManager.getHtmlText(serviceId + "\n" + dimensionValue + "=" + metadata);

        TNode node = TElementManager.getNode(dataBox, nodeName);
        if (node == null) {
            node = addNode(nodeName, nodeUI);
            setNodeLabelPosition(node, TWaverConst.POSITION_RIGHT);

            node.setToolTipText(nodeName);
        }

        if (previousNode != null) {
            TLink link = addLink(previousNode, node, linkUI);
            link.putLabelPosition(TWaverConst.POSITION_HOTSPOT);
            link.getAlarmState().addNewAlarm(AlarmSeverity.WARNING);
            link.putAlarmBalloonOutlineColor(LinkUI.GRAY);

            int counts = 1;
            Object userObject = link.getUserObject();
            if (userObject != null) {
                counts = (int) userObject + 1;
            }

            DecimalFormat format = new DecimalFormat("0.0000");
            double percent = Double.valueOf(format.format((double) counts * 100 / times));

            String text = ConsoleLocaleFactory.getString("percent") + "=" + percent + "%";
            link.setName(text);
            link.setToolTipText(text);
            link.setUserObject(counts);
        }

        return node;
    }

    public String getParameter() {
        String parameter = parameterTextField.getText().trim();
        if (StringUtils.equals(parameter, ConsoleLocaleFactory.getString("inspector_parameter_example"))) {
            parameter = "";
        }

        try {
            StringUtil.splitToMap(parameter);
        } catch (Exception ex) {
            showParameterInvalidFormatTip();

            return null;
        }

        return parameter;
    }

    public void showParameterInvalidFormatTip() {
        parameterTextField.showTip(ConsoleLocaleFactory.getString("parameter_invalid_format"), ConsoleIconFactory.getSwingIcon("error_message.png"), 1, 12);
    }

    public void launch() {
        String address = ComboBoxUtil.getSelectedValue(instanceComboBox);
        if (StringUtils.isBlank(address)) {
            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(this), ConsoleLocaleFactory.getString("address_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

            return;
        }

        String parameter = getParameter();
        if (parameter == null) {
            return;
        }

        if (conditionPanel.isServiceIdInvalid()) {
            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(this), ConsoleLocaleFactory.getString("service_id_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

            return;
        }

        dataBox.clear();

        ElementNode dimensionElementNode = (ElementNode) dimensionComboBox.getSelectedItem();
        DimensionType dimensionType = (DimensionType) dimensionElementNode.getUserObject();

        setTitle(dimensionType);

        ElementNode portalElementNode = (ElementNode) portalComboBox.getSelectedItem();
        PortalType portalType = (PortalType) portalElementNode.getUserObject();

        List<String> allServiceIds = null;
        List<String> serviceIds = null;

        if (portalType == PortalType.GATEWAY) {
            String firstServiceId = conditionPanel.getFirstServiceId();

            address += "/" + firstServiceId + DiscoveryConstant.INSPECTOR_ENDPOINT_URL + "?" + parameter;

            allServiceIds = conditionPanel.getServiceIds(true);
            serviceIds = conditionPanel.getServiceIds(false);
        } else {
            address += DiscoveryConstant.INSPECTOR_ENDPOINT_URL + "?" + parameter;

            allServiceIds = conditionPanel.getServiceIds(true);
            serviceIds = allServiceIds;
        }

        LOG.info("Inspection URL={}", address);

        LOG.info("Inspection Services={}", allServiceIds);

        InspectorEntity inspectorEntity = new InspectorEntity();
        inspectorEntity.setServiceIdList(serviceIds);

        int times = Integer.valueOf(timesComboBox.getSelectedItem().toString());

        DefaultBoundedRangeModel boundedRangeModel = new DefaultBoundedRangeModel(0, 1, 0, times);
        progressBar.setModel(boundedRangeModel);

        currentTime = System.currentTimeMillis();

        try {
            for (int i = 0; i < times; i++) {
                InspectorSwingWorker inspectorSwingWorker = new InspectorSwingWorker();
                inspectorSwingWorker.setAddress(address);
                inspectorSwingWorker.setInspectorEntity(inspectorEntity);
                inspectorSwingWorker.setTimes(times);
                inspectorSwingWorker.execute();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class InspectorSwingWorker extends JSwingWorker {
        protected String address;
        protected InspectorEntity inspectorEntity;
        protected int times;

        @Override
        protected Object loadBackground() throws Exception {
            InspectorEntity resultInspectorEntity = ConsoleController.inspect(address, inspectorEntity);
            List<Map<String, String>> metadatas = convertToMetadatas(resultInspectorEntity);

            return metadatas;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void loadForeground(Object object) throws Exception {
            List<Map<String, String>> metadatas = (List<Map<String, String>>) object;

            TNode node = null;
            int index = 0;
            for (Map<String, String> metadataMap : metadatas) {
                NodeUI nodeUI = index == 0 ? gatewayNodeUI : serviceNodeUI;
                node = addNode(node, metadataMap, nodeUI, times);

                index++;
            }

            setProgress();

            executeLayout();
        }

        public synchronized void setProgress() {
            int progress = progressBar.getModel().getValue() + 1;
            progressBar.getModel().setValue(progress);

            spentTextField.setText(String.valueOf(System.currentTimeMillis() - currentTime));
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public InspectorEntity getInspectorEntity() {
            return inspectorEntity;
        }

        public void setInspectorEntity(InspectorEntity inspectorEntity) {
            this.inspectorEntity = inspectorEntity;
        }

        public int getTimes() {
            return times;
        }

        public void setTimes(int times) {
            this.times = times;
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

    public JSecurityAction createLaunchAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("launch_text"), TIconFactory.getSwingIcon("theme/folder/deploy.png"), ConsoleLocaleFactory.getString("launch_inspector_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                launch();
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