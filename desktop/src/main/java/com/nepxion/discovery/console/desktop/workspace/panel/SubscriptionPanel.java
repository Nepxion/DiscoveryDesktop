package com.nepxion.discovery.console.desktop.workspace.panel;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.discovery.common.entity.ServiceType;
import com.nepxion.discovery.common.entity.SubscriptionType;
import com.nepxion.discovery.console.cache.ConsoleCache;
import com.nepxion.discovery.console.controller.ConsoleController;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.swing.dialog.JExceptionDialog;
import com.nepxion.discovery.console.desktop.common.util.ButtonUtil;
import com.nepxion.discovery.console.desktop.common.util.ComboBoxUtil;
import com.nepxion.discovery.console.desktop.common.util.DimensionUtil;
import com.nepxion.discovery.console.desktop.workspace.type.TypeLocale;
import com.nepxion.discovery.console.entity.Instance;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.checkbox.JBasicCheckBox;
import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.radiobutton.JBasicRadioButton;
import com.nepxion.swing.shrinkbar.JShrinkShortcut;

public class SubscriptionPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    protected JShrinkShortcut subscriptionParameterShrinkShortcut;
    protected ButtonGroup subscriptionButtonGroup;
    protected JPanel subscriptionRadioButtonPanel;
    protected JBasicComboBox groupComboBox;
    protected JClassicButton refreshGroupButton;
    protected JPanel groupPanel;
    protected JBasicComboBox gatewayIdComboBox;
    protected JBasicCheckBox showOnlyGatewayCheckBox;
    protected JPanel gatewayPanel;

    public SubscriptionPanel() {
        subscriptionParameterShrinkShortcut = new JShrinkShortcut();
        subscriptionParameterShrinkShortcut.setTitle(ConsoleLocaleFactory.getString("subscription_parameter_text"));
        subscriptionParameterShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        subscriptionParameterShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString("subscription_parameter_text"));

        subscriptionRadioButtonPanel = new JPanel();
        subscriptionRadioButtonPanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 10));
        subscriptionButtonGroup = new ButtonGroup();
        SubscriptionType[] subscriptionTypes = SubscriptionType.values();
        for (int i = 0; i < subscriptionTypes.length; i++) {
            SubscriptionType subscriptionType = subscriptionTypes[i];

            JBasicRadioButton subscriptionRadioButton = new JBasicRadioButton(TypeLocale.getDescription(subscriptionType), TypeLocale.getDescription(subscriptionType));
            subscriptionRadioButton.setName(subscriptionType.toString());
            subscriptionRadioButton.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    subscriptionRadioButtonItemStateChanged(e);
                }
            });
            subscriptionRadioButtonPanel.add(subscriptionRadioButton);
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
                    groupComboBoxItemStateChanged(e);
                }
            }
        });
        ComboBoxUtil.installlAutoCompletion(groupComboBox);
        refreshGroupButton = new JClassicButton(createRefreshGroupAction());
        DimensionUtil.setWidth(refreshGroupButton, 30);

        double[][] groupSize = {
                { TableLayout.FILL, TableLayout.PREFERRED },
                { TableLayout.PREFERRED }
        };

        TableLayout groupTableLayout = new TableLayout(groupSize);
        groupTableLayout.setHGap(5);
        groupTableLayout.setVGap(5);

        groupPanel = new JPanel();
        groupPanel.setLayout(groupTableLayout);
        groupPanel.add(groupComboBox, "0, 0");
        groupPanel.add(refreshGroupButton, "1, 0");

        gatewayIdComboBox = new JBasicComboBox();
        gatewayIdComboBox.setEditable(true);
        ComboBoxUtil.installlAutoCompletion(gatewayIdComboBox);

        showOnlyGatewayCheckBox = new JBasicCheckBox(ConsoleLocaleFactory.getString("show_only_gateway_text"), ConsoleLocaleFactory.getString("show_only_gateway_text"), false);
        showOnlyGatewayCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                showOnlyGatewayCheckBoxItemStateChanged(e);
            }
        });

        double[][] gatewaySize = {
                { TableLayout.FILL, TableLayout.PREFERRED },
                { TableLayout.PREFERRED }
        };

        TableLayout gatewayTableLayout = new TableLayout(gatewaySize);
        gatewayTableLayout.setHGap(5);
        gatewayTableLayout.setVGap(5);

        gatewayPanel = new JPanel();
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
        add(subscriptionParameterShrinkShortcut, "0, 0, 1, 0");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("subscription_text")), "0, 1");
        add(subscriptionRadioButtonPanel, "1, 1");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("group_name_text")), "0, 2");
        add(groupPanel, "1, 2");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("service_name_text")), "0, 3");
        add(gatewayPanel, "1, 3");

        setGroups();
        setGatewayIds();
    }

    public double[] getLayoutRow() {
        return new double[] { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED };
    }

    public void subscriptionRadioButtonItemStateChanged(ItemEvent e) {
        JBasicRadioButton radioButton = (JBasicRadioButton) e.getItem();

        if (StringUtils.equals(radioButton.getName(), SubscriptionType.GLOBAL.toString())) {
            if (gatewayIdComboBox != null) {
                gatewayIdComboBox.setEnabled(!radioButton.isSelected());
            }

            if (showOnlyGatewayCheckBox != null) {
                showOnlyGatewayCheckBox.setEnabled(!radioButton.isSelected());
            }
        }
    }

    public void groupComboBoxItemStateChanged(ItemEvent e) {
        setGatewayIds();
    }

    public void showOnlyGatewayCheckBoxItemStateChanged(ItemEvent e) {
        setGatewayIds();
    }

    public void setGroups() {
        List<String> groups = getGroups();
        if (groups != null) {
            ComboBoxUtil.setSortableModel(groupComboBox, groups);
        }
    }

    public void setGatewayIds() {
        String group = ComboBoxUtil.getSelectedValue(groupComboBox);
        if (StringUtils.isNotBlank(group)) {
            List<String> gatewayIds = getServiceIds(group, showOnlyGatewayCheckBox.isSelected());
            if (gatewayIds != null) {
                ComboBoxUtil.setSortableModel(gatewayIdComboBox, gatewayIds);
            }
        } else {
            ComboBoxUtil.setSortableModel(gatewayIdComboBox, new ArrayList<String>());
        }
    }

    public SubscriptionType getSubscriptionType() {
        String rationButtonName = ButtonUtil.getRationButtonName(subscriptionButtonGroup);

        return SubscriptionType.fromString(rationButtonName);
    }

    public String getGroup() {
        return ComboBoxUtil.getSelectedValue(groupComboBox);
    }

    public String getValidGroup() {
        String group = getGroup();

        if (StringUtils.isBlank(group)) {
            return null;
        }

        return group;
    }

    public String getGatewayId() {
        return ComboBoxUtil.getSelectedValue(gatewayIdComboBox);
    }

    public String getValidGatewayId() {
        String gatewayId = null;

        SubscriptionType subscriptionType = getSubscriptionType();
        if (subscriptionType == SubscriptionType.PARTIAL) {
            gatewayId = getGatewayId();
            if (StringUtils.isBlank(gatewayId)) {
                return null;
            }
        } else {
            gatewayId = getGroup();
        }

        return gatewayId;
    }

    public List<String> getGroups() {
        try {
            return ConsoleCache.getGroups();
        } catch (Exception e) {
            JExceptionDialog.traceException(HandleManager.getFrame(this), ConsoleLocaleFactory.getString("operation_failure"), e);
        }

        return null;
    }

    public List<String> getServiceIds(String group, boolean onlyGateway) {
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

                return gatewayIds;
            } else {
                return new ArrayList<String>(instanceMap.keySet());
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

    public JSecurityAction createRefreshGroupAction() {
        JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("netbean/rotate_16.png"), ConsoleLocaleFactory.getString("refresh_group_list_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                setGroups();
                setGatewayIds();
            }
        };

        return action;
    }
}