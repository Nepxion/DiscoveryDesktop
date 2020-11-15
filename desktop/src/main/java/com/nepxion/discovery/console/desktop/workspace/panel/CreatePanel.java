package com.nepxion.discovery.console.desktop.workspace.panel;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.discovery.common.entity.ServiceType;
import com.nepxion.discovery.console.controller.ConsoleController;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.workspace.type.ConfigType;
import com.nepxion.discovery.console.desktop.workspace.type.DeployType;
import com.nepxion.discovery.console.desktop.workspace.type.StrategyType;
import com.nepxion.discovery.console.desktop.workspace.util.ComboBoxUtil;
import com.nepxion.discovery.console.entity.Instance;
import com.nepxion.swing.checkbox.JBasicCheckBox;
import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.dialog.JExceptionDialog;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.radiobutton.JBasicRadioButton;
import com.nepxion.swing.textfield.JBasicTextField;

public class CreatePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    protected ButtonGroup strategyButtonGroup;
    protected ButtonGroup configButtonGroup;
    protected ButtonGroup deployButtonGroup;

    protected JBasicComboBox groupComboBox;
    protected JBasicComboBox gatewayIdComboBox;
    protected JBasicCheckBox showOnlyGatewayCheckBox;

    protected JBasicTextField layoutTextField = new JBasicTextField();

    public CreatePanel() {
        JPanel strategyPanel = new JPanel();
        strategyPanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 10));
        strategyButtonGroup = new ButtonGroup();
        StrategyType[] strategyTypes = StrategyType.values();
        for (int i = 0; i < strategyTypes.length; i++) {
            StrategyType strategyType = strategyTypes[i];
            if (strategyType.getCategory() == 0) {
                JBasicRadioButton strategyRadioButton = new JBasicRadioButton(strategyType.getDescription(), strategyType.getDescription());
                strategyRadioButton.setName(strategyType.toString());
                strategyPanel.add(strategyRadioButton);
                strategyButtonGroup.add(strategyRadioButton);

                if (i == 0) {
                    strategyRadioButton.setSelected(true);
                }
            }
        }

        JPanel configPanel = new JPanel();
        configPanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 10));
        configButtonGroup = new ButtonGroup();
        ConfigType[] configTypes = ConfigType.values();
        for (int i = 0; i < configTypes.length; i++) {
            ConfigType configType = configTypes[i];

            JBasicRadioButton configRadioButton = new JBasicRadioButton(configType.getDescription(), configType.getDescription());
            configRadioButton.setName(configType.toString());
            configRadioButton.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    JBasicRadioButton radioButton = (JBasicRadioButton) e.getItem();
                    if (deployButtonGroup != null && StringUtils.equals(radioButton.getName(), ConfigType.GLOBAL.toString())) {
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
            configPanel.add(configRadioButton);
            configButtonGroup.add(configRadioButton);

            if (i == 0) {
                configRadioButton.setSelected(true);
            }
        }

        JPanel deployPanel = new JPanel();
        deployPanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 10));
        deployButtonGroup = new ButtonGroup();
        DeployType[] deployTypes = DeployType.values();
        for (int i = 0; i < deployTypes.length; i++) {
            DeployType deployType = deployTypes[i];

            JBasicRadioButton deployRadioButton = new JBasicRadioButton(deployType.getDescription(), deployType.getDescription());
            deployRadioButton.setName(deployType.toString());
            deployPanel.add(deployRadioButton);
            deployButtonGroup.add(deployRadioButton);

            if (i == 0) {
                deployRadioButton.setSelected(true);
            }
        }

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

        showOnlyGatewayCheckBox = new JBasicCheckBox(ConsoleLocaleFactory.getString("show_only_gateway_text"), ConsoleLocaleFactory.getString("show_only_gateway_text"), true);
        showOnlyGatewayCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                setGatewayIds();
            }
        });

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
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
        };

        TableLayout tableLayout = new TableLayout(size);
        tableLayout.setHGap(20);
        tableLayout.setVGap(10);

        setLayout(tableLayout);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(new JBasicLabel(ConsoleLocaleFactory.getString("strategy_text")), "0, 0");
        add(strategyPanel, "1, 0");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("config_text")), "0, 1");
        add(configPanel, "1, 1");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("deploy_text")), "0, 2");
        add(deployPanel, "1, 2");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("belong_group_text")), "0, 3");
        add(groupComboBox, "1, 3");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("portal_service_text")), "0, 4");
        add(gatewayPanel, "1, 4");

        setGroups();
        setGatewayIds();
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

    public ConfigType getConfigType() {
        String rationButtonName = getRationButtonName(configButtonGroup);

        return ConfigType.fromString(rationButtonName);
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