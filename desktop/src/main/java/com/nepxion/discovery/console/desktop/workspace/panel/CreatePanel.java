package com.nepxion.discovery.console.desktop.workspace.panel;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.discovery.common.entity.DeployType;
import com.nepxion.discovery.common.entity.ServiceType;
import com.nepxion.discovery.common.entity.SubscriptionType;
import com.nepxion.discovery.console.controller.ConsoleController;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.util.ComboBoxUtil;
import com.nepxion.discovery.console.desktop.workspace.type.StrategyType;
import com.nepxion.discovery.console.desktop.workspace.type.TypeLocale;
import com.nepxion.discovery.console.entity.Instance;
import com.nepxion.swing.checkbox.JBasicCheckBox;
import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.dialog.JExceptionDialog;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.radiobutton.JBasicRadioButton;
import com.nepxion.swing.shrinkbar.JShrinkShortcut;

public class CreatePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    protected JBasicRadioButton newRadioButton;
    protected JBasicRadioButton openRadioButton;

    protected ButtonGroup subscriptionButtonGroup;
    protected JBasicComboBox groupComboBox;
    protected JBasicComboBox gatewayIdComboBox;
    protected JBasicCheckBox showOnlyGatewayCheckBox;

    protected ButtonGroup strategyButtonGroup;
    protected ButtonGroup deployButtonGroup;

    public CreatePanel() {
        newRadioButton = new JBasicRadioButton(ConsoleLocaleFactory.getString("create_tooltip"), ConsoleLocaleFactory.getString("create_tooltip"));
        newRadioButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (newRadioButton.isSelected()) {

                }
            }
        });
        newRadioButton.setSelected(true);
        openRadioButton = new JBasicRadioButton(ConsoleLocaleFactory.getString("open_tooltip"), ConsoleLocaleFactory.getString("open_tooltip"));
        ButtonGroup createModeButtonGroup = new ButtonGroup();
        createModeButtonGroup.add(newRadioButton);
        createModeButtonGroup.add(openRadioButton);

        JPanel createModePanel = new JPanel();
        createModePanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 10));
        createModePanel.add(newRadioButton);
        createModePanel.add(openRadioButton);

        JShrinkShortcut subscriptionShrinkShortcut = new JShrinkShortcut();
        subscriptionShrinkShortcut.setTitle(ConsoleLocaleFactory.getString("subscription_parameter_text"));
        subscriptionShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        subscriptionShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString("subscription_parameter_text"));

        JPanel subscriptionPanel = new JPanel();
        subscriptionPanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 10));
        subscriptionButtonGroup = new ButtonGroup();
        SubscriptionType[] subscriptionTypes = SubscriptionType.values();
        for (int i = 0; i < subscriptionTypes.length; i++) {
            SubscriptionType subscriptionType = subscriptionTypes[i];

            JBasicRadioButton subscriptionRadioButton = new JBasicRadioButton(TypeLocale.getDescription(subscriptionType), TypeLocale.getDescription(subscriptionType));
            subscriptionRadioButton.setName(subscriptionType.toString());
            subscriptionRadioButton.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    JBasicRadioButton radioButton = (JBasicRadioButton) e.getItem();
                    if (deployButtonGroup != null && StringUtils.equals(radioButton.getName(), SubscriptionType.GLOBAL.toString())) {
                        for (Enumeration<AbstractButton> enumeration = deployButtonGroup.getElements(); enumeration.hasMoreElements();) {
                            AbstractButton button = enumeration.nextElement();
                            button.setEnabled(!radioButton.isSelected());

                            // 全局配置下，处理为非域网关模式
                            if (StringUtils.equals(button.getName(), DeployType.NON_DOMAIN_GATEWAY.toString())) {
                                button.setSelected(true);
                            }
                        }
                        gatewayIdComboBox.setEnabled(!radioButton.isSelected());
                        showOnlyGatewayCheckBox.setEnabled(!radioButton.isSelected());
                    }
                }
            });
            subscriptionPanel.add(subscriptionRadioButton);
            subscriptionButtonGroup.add(subscriptionRadioButton);

            if (i == 0) {
                subscriptionRadioButton.setSelected(true);
            }
        }

        groupComboBox = new JBasicComboBox();
        groupComboBox.setEditable(true);
        groupComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (groupComboBox.getSelectedItem() != e.getItem()) {
                    setGatewayIds();
                }
            }
        });
        // JSearchableFactory.installSearchable(groupComboBox);

        gatewayIdComboBox = new JBasicComboBox();
        gatewayIdComboBox.setEditable(true);
        // JSearchableFactory.installSearchable(gatewayIdComboBox);

        showOnlyGatewayCheckBox = new JBasicCheckBox(ConsoleLocaleFactory.getString("show_only_gateway_text"), ConsoleLocaleFactory.getString("show_only_gateway_text"), true);
        showOnlyGatewayCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                setGatewayIds();
            }
        });

        JShrinkShortcut newShrinkShortcut = new JShrinkShortcut();
        newShrinkShortcut.setTitle(ConsoleLocaleFactory.getString("new_parameter_text"));
        newShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        newShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString("new_parameter_text"));

        JPanel strategyPanel = new JPanel();
        strategyPanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 10));
        strategyButtonGroup = new ButtonGroup();
        StrategyType[] strategyTypes = StrategyType.values();
        for (int i = 0; i < strategyTypes.length; i++) {
            StrategyType strategyType = strategyTypes[i];
            if (strategyType.getCategory() == 0) {
                JBasicRadioButton strategyRadioButton = new JBasicRadioButton(TypeLocale.getDescription(strategyType), TypeLocale.getDescription(strategyType));
                strategyRadioButton.setName(strategyType.toString());
                strategyPanel.add(strategyRadioButton);
                strategyButtonGroup.add(strategyRadioButton);

                if (i == 0) {
                    strategyRadioButton.setSelected(true);
                }
            }
        }

        JPanel deployPanel = new JPanel();
        deployPanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 10));
        deployButtonGroup = new ButtonGroup();
        DeployType[] deployTypes = DeployType.values();
        for (int i = 0; i < deployTypes.length; i++) {
            DeployType deployType = deployTypes[i];

            JBasicRadioButton deployRadioButton = new JBasicRadioButton(TypeLocale.getDescription(deployType), TypeLocale.getDescription(deployType));
            deployRadioButton.setName(deployType.toString());
            deployPanel.add(deployRadioButton);
            deployButtonGroup.add(deployRadioButton);

            if (i == 0) {
                deployRadioButton.setSelected(true);
            }
        }

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

        double[][] size = {
                { TableLayout.PREFERRED, TableLayout.FILL },
                getLayoutRow()
        };

        TableLayout tableLayout = new TableLayout(size);
        tableLayout.setHGap(20);
        tableLayout.setVGap(10);

        setLayout(tableLayout);
        add(new JBasicLabel("创建方式"), "0, 0");
        add(createModePanel, "1, 0");
        add(subscriptionShrinkShortcut, "0, 2, 1, 2");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("subscription_text")), "0, 3");
        add(subscriptionPanel, "1, 3");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("group_name_text")), "0, 4");
        add(groupComboBox, "1, 4");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("service_name_text")), "0, 5");
        add(gatewayPanel, "1, 5");
        add(newShrinkShortcut, "0, 7, 1, 7");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("strategy_text")), "0, 8");
        add(strategyPanel, "1, 8");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("deploy_text")), "0, 9");
        add(deployPanel, "1, 9");

        setGroups();
        setGatewayIds();
    }

    public double[] getLayoutRow() {
        return new double[] { TableLayout.PREFERRED, 0, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, 0, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED };
    }

    @SuppressWarnings("unchecked")
    public void setGroups() {
        Object[] groups = getGroups();
        if (groups != null) {
            groupComboBox.setModel(new DefaultComboBoxModel<>(groups));
        }
    }

    @SuppressWarnings("unchecked")
    public void setGatewayIds() {
        String group = ComboBoxUtil.getSelectedValue(groupComboBox);
        if (StringUtils.isNotBlank(group)) {
            Object[] gatewayIds = geServiceIds(group, showOnlyGatewayCheckBox.isSelected());
            if (gatewayIds != null) {
                gatewayIdComboBox.setModel(new DefaultComboBoxModel<>(gatewayIds));
            }
        }
    }

    public String getRationButtonName(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> enumeration = buttonGroup.getElements(); enumeration.hasMoreElements();) {
            AbstractButton button = enumeration.nextElement();
            if (button.isSelected()) {
                return button.getName();
            }
        }

        return null;
    }

    public StrategyType getStrategyType() {
        String rationButtonName = getRationButtonName(strategyButtonGroup);

        return StrategyType.fromString(rationButtonName);
    }

    public SubscriptionType getSubscriptionType() {
        String rationButtonName = getRationButtonName(subscriptionButtonGroup);

        return SubscriptionType.fromString(rationButtonName);
    }

    public DeployType getDeployType() {
        String rationButtonName = getRationButtonName(deployButtonGroup);

        return DeployType.fromString(rationButtonName);
    }

    public String getGroup() {
        return ComboBoxUtil.getSelectedValue(groupComboBox);
    }

    public String getGatewayId() {
        return ComboBoxUtil.getSelectedValue(gatewayIdComboBox);
    }

    public Object[] getGroups() {
        try {
            return ConsoleController.getGroups().toArray();
        } catch (Exception e) {
            JExceptionDialog.traceException(HandleManager.getFrame(this), ConsoleLocaleFactory.getString("operation_failure"), e);
        }

        return null;
    }

    public Object[] geServiceIds(String group, boolean onlyGateway) {
        try {
            Map<String, List<Instance>> instanceMap = ConsoleController.getInstanceMap(Arrays.asList(group));
            if (onlyGateway) {
                List<String> gatewayIds = new ArrayList<String>();
                for (Map.Entry<String, List<Instance>> entry : instanceMap.entrySet()) {
                    String gatewayId = entry.getKey();
                    List<Instance> gateways = entry.getValue();
                    if (hasGateway(gateways)) {
                        gatewayIds.add(gatewayId);
                    }
                }

                return gatewayIds.toArray();
            } else {
                return instanceMap.keySet().toArray();
            }
        } catch (Exception e) {
            JExceptionDialog.traceException(HandleManager.getFrame(this), ConsoleLocaleFactory.getString("operation_failure"), e);
        }

        return null;
    }

    public boolean hasGateway(List<Instance> gateways) {
        for (Instance gateway : gateways) {
            if (StringUtils.equals(gateway.getServiceType(), ServiceType.GATEWAY.toString())) {
                return true;
            }
        }

        return false;
    }
}