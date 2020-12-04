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
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.cots.twaver.element.TElementManager;
import com.nepxion.cots.twaver.element.TLink;
import com.nepxion.cots.twaver.element.TNode;
import com.nepxion.cots.twaver.icon.TIconFactory;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.entity.InspectorEntity;
import com.nepxion.discovery.common.entity.ServiceType;
import com.nepxion.discovery.common.exception.DiscoveryException;
import com.nepxion.discovery.common.util.StringUtil;
import com.nepxion.discovery.console.cache.ConsoleCache;
import com.nepxion.discovery.console.controller.ConsoleController;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.util.ButtonUtil;
import com.nepxion.discovery.console.desktop.common.util.ComboBoxUtil;
import com.nepxion.discovery.console.desktop.common.util.DimensionUtil;
import com.nepxion.discovery.console.desktop.common.util.TextFieldUtil;
import com.nepxion.discovery.console.desktop.workspace.panel.InspectorConditionPanel;
import com.nepxion.discovery.console.desktop.workspace.panel.MultiPreviewPanel;
import com.nepxion.discovery.console.desktop.workspace.topology.LinkUI;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeImageType;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeSizeType;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeUI;
import com.nepxion.discovery.console.desktop.workspace.type.DimensionType;
import com.nepxion.discovery.console.desktop.workspace.type.FeatureType;
import com.nepxion.discovery.console.desktop.workspace.type.ParameterType;
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
import com.nepxion.swing.textfield.JBasicTextField;

public class InspectorTopology extends AbstractTopology {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(InspectorTopology.class);

    public static final String APOLLO = "Apollo";

    protected NodeUI gatewayNodeUI = new NodeUI(NodeImageType.GATEWAY_BLUE, NodeSizeType.LARGE, true);
    protected NodeUI serviceNodeUI = new NodeUI(NodeImageType.SERVICE_BLUE, NodeSizeType.MIDDLE, true);
    protected Color linkUI = LinkUI.BLUE;

    protected JBasicComboBox portalComboBox;
    protected JBasicComboBox serviceIdComboBox;
    protected JBasicComboBox instanceComboBox;
    protected JBasicComboBox parameterComboBox;
    protected JBasicTextField parameterTextField;

    protected InspectorConditionPanel conditionPanel;
    protected MultiPreviewPanel multiPreviewPanel;

    protected JBasicComboBox dimensionComboBox;
    protected JBasicComboBox timesComboBox;
    protected JBasicComboBox concurrencyComboBox;
    protected JProgressBar successfulProgressBar;
    protected JProgressBar failureProgressBar;
    protected JBasicTextField spentTextField;

    protected ExecutorService executorService;
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

        background.setTitle(TypeLocale.getDescription(FeatureType.INSPECTOR));
    }

    public void initializeToolBar() {
        JToolBar toolBar = getGraph().getToolbar();
        toolBar.addSeparator();
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

        List<ElementNode> parameterElementNodes = new ArrayList<ElementNode>();
        ParameterType[] parameterTypes = ParameterType.values();
        for (int i = 0; i < parameterTypes.length; i++) {
            ParameterType parameterType = parameterTypes[i];
            parameterElementNodes.add(new ElementNode(parameterType.toString(), parameterType.getCapitalizeValue(), null, parameterType.getCapitalizeValue(), parameterType));
        }

        parameterComboBox = new JBasicComboBox(parameterElementNodes.toArray());

        parameterTextField = new JBasicTextField();

        setServiceIds();
        setInstances();

        double[][] portalSize = {
                { TableLayout.PREFERRED, TableLayout.FILL },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
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
        portalPanel.add(parameterComboBox, "1, 3");
        portalPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("value")), 5), "0, 4");
        portalPanel.add(TextFieldUtil.setTip(parameterTextField, ConsoleLocaleFactory.getString("inspector_parameter_example")), "1, 4");

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

        Integer[] times = new Integer[] { 1, 5, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 20000 };
        timesComboBox = new JBasicComboBox(times);
        timesComboBox.setSelectedIndex(2);

        Integer[] concurrency = new Integer[] { 10, 20, 30, 50, 80, 100, 120, 150, 180, 200, 300, 500 };
        concurrencyComboBox = new JBasicComboBox(concurrency);

        successfulProgressBar = new JProgressBar();
        successfulProgressBar.setStringPainted(true);

        failureProgressBar = new JProgressBar();
        failureProgressBar.setStringPainted(true);

        spentTextField = new JBasicTextField("0");
        spentTextField.setEditable(false);

        double[][] parameterSize = {
                { TableLayout.PREFERRED, TableLayout.FILL, 5, TableLayout.PREFERRED, TableLayout.FILL },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
        };

        TableLayout parameterTableLayout = new TableLayout(parameterSize);
        parameterTableLayout.setHGap(0);
        parameterTableLayout.setVGap(5);

        JPanel parameterPanel = new JPanel();
        parameterPanel.setLayout(parameterTableLayout);
        parameterPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("dimension")), 5), "0, 0");
        parameterPanel.add(dimensionComboBox, "1, 0, 4, 0");
        parameterPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("times")), 5), "0, 1");
        parameterPanel.add(timesComboBox, "1, 1");
        parameterPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("concurrency")), 5), "3, 1");
        parameterPanel.add(concurrencyComboBox, "4, 1");
        parameterPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("successful")), 5), "0, 2");
        parameterPanel.add(DimensionUtil.addHeight(successfulProgressBar, 6), "1, 2");
        parameterPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("failure")), 5), "3, 2");
        parameterPanel.add(DimensionUtil.addHeight(failureProgressBar, 6), "4, 2");
        parameterPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("spent")), 5), "0, 3");
        parameterPanel.add(spentTextField, "1, 3, 4, 3");

        JPanel toolBar = new JPanel();
        toolBar.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 0));
        toolBar.add(new JClassicButton(createRefreshServiceListAction()));
        toolBar.add(new JClassicButton(createViewFailureListAction()));

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

    public TNode addNode(TNode previousNode, DimensionType dimensionType, Map<String, String> metadataMap, NodeUI nodeUI, int times) {
        String dimensionKey = dimensionType.getKey();
        String dimensionValue = dimensionType.getValue();

        String serviceId = metadataMap.get("ID");
        String metadata = metadataMap.get(dimensionKey);
        String nodeName = ButtonManager.getHtmlText(serviceId + "\n" + dimensionValue + "=" + metadata);

        TNode node = TElementManager.getNode(dataBox, nodeName);
        if (node == null) {
            node = addNode(nodeName, nodeUI);
            setNodeLabelPosition(node, TWaverConst.POSITION_RIGHT);

            node.setUserObject(metadataMap);
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

    public class InspectorResult {
        protected List<Map<String, String>> metadatas;
        protected Exception exception;

        public List<Map<String, String>> getMetadatas() {
            return metadatas;
        }

        public void setMetadatas(List<Map<String, String>> metadatas) {
            this.metadatas = metadatas;
        }

        public Exception getException() {
            return exception;
        }

        public void setException(Exception exception) {
            this.exception = exception;
        }
    }

    public class InspectorSwingWorker extends SwingWorker<InspectorResult, Void> {
        protected DimensionType dimensionType;
        protected String address;
        protected InspectorEntity inspectorEntity;
        protected int times;

        @Override
        protected InspectorResult doInBackground() throws Exception {
            InspectorResult inspectorResult = new InspectorResult();

            try {
                InspectorEntity resultInspectorEntity = ConsoleController.inspect(address, inspectorEntity);
                List<Map<String, String>> metadatas = convertToMetadatas(resultInspectorEntity);

                inspectorResult.setMetadatas(metadatas);
            } catch (Exception e) {
                inspectorResult.setException(e);
            }

            return inspectorResult;
        }

        @Override
        protected void done() {
            InspectorResult inspectorResult = null;
            try {
                inspectorResult = get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (inspectorResult.getException() != null) {
                updateFailureProgress();
            } else {
                List<Map<String, String>> metadatas = inspectorResult.getMetadatas();

                TNode node = null;
                for (Map<String, String> metadataMap : metadatas) {
                    String serviceType = metadataMap.get("T");

                    NodeUI nodeUI = ServiceType.fromString(serviceType) == ServiceType.GATEWAY ? gatewayNodeUI : serviceNodeUI;
                    node = addNode(node, dimensionType, metadataMap, nodeUI, times);
                }

                updateSuccessfulProgress();
            }

            updateSpent();
        }

        public synchronized void updateSuccessfulProgress() {
            int progress = successfulProgressBar.getModel().getValue() + 1;
            successfulProgressBar.getModel().setValue(progress);
        }

        public synchronized void updateFailureProgress() {
            int progress = failureProgressBar.getModel().getValue() + 1;
            failureProgressBar.getModel().setValue(progress);
        }

        public synchronized void updateSpent() {
            long spentTime = System.currentTimeMillis() - currentTime;
            spentTextField.setText(String.valueOf(spentTime));
        }

        public DimensionType getDimensionType() {
            return dimensionType;
        }

        public void setDimensionType(DimensionType dimensionType) {
            this.dimensionType = dimensionType;
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

    public void start() {
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

        ElementNode dimensionElementNode = (ElementNode) dimensionComboBox.getSelectedItem();
        DimensionType dimensionType = (DimensionType) dimensionElementNode.getUserObject();

        ElementNode portalElementNode = (ElementNode) portalComboBox.getSelectedItem();
        PortalType portalType = (PortalType) portalElementNode.getUserObject();

        parameter = StringUtils.isNotBlank(parameter) ? "?" + parameter : "";
        List<String> allServiceIds = conditionPanel.getServiceIds(true);
        List<String> serviceIds = null;

        if (portalType == PortalType.GATEWAY) {
            String firstServiceId = conditionPanel.getFirstServiceId();

            address += "/" + firstServiceId + "/" + DiscoveryConstant.INSPECTOR_ENDPOINT_URL + parameter;

            serviceIds = conditionPanel.getServiceIds(false);
        } else {
            address += "/" + DiscoveryConstant.INSPECTOR_ENDPOINT_URL + parameter;

            serviceIds = allServiceIds;
        }

        StringBuilder informationStringBuilder = new StringBuilder();
        informationStringBuilder.append(ConsoleLocaleFactory.getString("inspector_url") + " : \n" + address + "\n" + ConsoleLocaleFactory.getString("inspector_services") + " : \n");
        for (int i = 0; i < allServiceIds.size(); i++) {
            String serviceId = allServiceIds.get(i);

            informationStringBuilder.append(serviceId);
            if (i < allServiceIds.size() - 1) {
                informationStringBuilder.append("\n");
            }
        }

        int selectedValue = JBasicOptionPane.showConfirmDialog(HandleManager.getFrame(this), ConsoleLocaleFactory.getString("start_confirm") + "\n" + informationStringBuilder.toString(), SwingLocale.getString("confirm"), JBasicOptionPane.YES_NO_OPTION);
        if (selectedValue != JBasicOptionPane.OK_OPTION) {
            return;
        }

        LOG.info("Inspection URL={}", address);
        LOG.info("Inspection Services={}", allServiceIds);

        setTitle(dimensionType);
        dataBox.clear();

        InspectorEntity inspectorEntity = new InspectorEntity();
        inspectorEntity.setServiceIdList(serviceIds);

        int times = (Integer) timesComboBox.getSelectedItem();
        int concurrency = (Integer) concurrencyComboBox.getSelectedItem();

        DefaultBoundedRangeModel successfulBoundedRangeModel = new DefaultBoundedRangeModel(0, 1, 0, times);
        successfulProgressBar.setModel(successfulBoundedRangeModel);

        DefaultBoundedRangeModel failureBoundedRangeModel = new DefaultBoundedRangeModel(0, 1, 0, times);
        failureProgressBar.setModel(failureBoundedRangeModel);

        spentTextField.setText("0");

        currentTime = System.currentTimeMillis();

        executorService = Executors.newFixedThreadPool(concurrency);
        for (int i = 0; i < times; i++) {
            InspectorSwingWorker inspectorSwingWorker = new InspectorSwingWorker();
            inspectorSwingWorker.setDimensionType(dimensionType);
            inspectorSwingWorker.setAddress(address);
            inspectorSwingWorker.setInspectorEntity(inspectorEntity);
            inspectorSwingWorker.setTimes(times);

            executorService.execute(inspectorSwingWorker);
        }
    }

    public String getKey(String group, String serviceId) {
        String configType = ConsoleCache.getConfigType();
        String key = null;
        if (StringUtils.equals(configType, APOLLO)) {
            key = group + "-" + serviceId;
        } else {
            key = "Data ID=" + serviceId + " | Group=" + group;
        }

        return key;
    }

    public void stop() {
        if (executorService == null) {
            return;
        }

        int selectedValue = JBasicOptionPane.showConfirmDialog(HandleManager.getFrame(this), ConsoleLocaleFactory.getString("stop_confirm"), SwingLocale.getString("confirm"), JBasicOptionPane.YES_NO_OPTION);
        if (selectedValue != JBasicOptionPane.OK_OPTION) {
            return;
        }

        executorService.shutdownNow();
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
                stop();
            }
        };

        return action;
    }

    public JSecurityAction createViewAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("view_text"), ConsoleIconFactory.getSwingIcon("ticket.png"), ConsoleLocaleFactory.getString("view_config_tooltip")) {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            public void execute(ActionEvent e) {
                if (multiPreviewPanel == null) {
                    multiPreviewPanel = new MultiPreviewPanel();
                }

                TNode node = TElementManager.getSelectedNode(dataBox);
                if (node == null) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(InspectorTopology.this), ConsoleLocaleFactory.getString("service_or_gateway_selected"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                Map<String, String> metadataMap = (Map<String, String>) node.getUserObject();
                String group = metadataMap.get("G");
                String serviceId = metadataMap.get("ID");

                String partialConfig = ConsoleController.remoteConfigView(group, serviceId);
                multiPreviewPanel.getPartialPreviewPanel().setKey(getKey(group, serviceId));
                multiPreviewPanel.getPartialPreviewPanel().setConfig(partialConfig);

                String globalConfig = ConsoleController.remoteConfigView(group, group);
                multiPreviewPanel.getGlobalPreviewPanel().setKey(getKey(group, group));
                multiPreviewPanel.getGlobalPreviewPanel().setConfig(globalConfig);

                JBasicOptionPane.showOptionDialog(HandleManager.getFrame(InspectorTopology.this), multiPreviewPanel, ConsoleLocaleFactory.getString("view_config_tooltip"), JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/property.png"), new Object[] { SwingLocale.getString("close") }, null, true);
            }
        };

        return action;
    }

    public JSecurityAction createRefreshServiceListAction() {
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

    public JSecurityAction createViewFailureListAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("view_failure_list_tooltip"), ConsoleIconFactory.getSwingIcon("netbean/rotate_16.png"), ConsoleLocaleFactory.getString("view_failure_list_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {

            }
        };

        return action;
    }
}