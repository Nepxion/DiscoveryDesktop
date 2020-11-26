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
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.discovery.common.entity.ServiceType;
import com.nepxion.discovery.common.entity.SubscriptionType;
import com.nepxion.discovery.console.cache.ConsoleCache;
import com.nepxion.discovery.console.controller.ConsoleController;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.swing.dialog.JExceptionDialog;
import com.nepxion.discovery.console.desktop.common.util.ComboBoxUtil;
import com.nepxion.discovery.console.desktop.workspace.type.TypeLocale;
import com.nepxion.discovery.console.entity.Instance;
import com.nepxion.swing.checkbox.JBasicCheckBox;
import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.radiobutton.JBasicRadioButton;
import com.nepxion.swing.shrinkbar.JShrinkShortcut;

public class StrategyOpenPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    protected ButtonGroup subscriptionButtonGroup;
    protected JPanel subscriptionPanel;

    protected JBasicComboBox groupComboBox;
    protected JBasicComboBox gatewayIdComboBox;
    protected JBasicCheckBox showOnlyGatewayCheckBox;
    protected JPanel gatewayPanel;

    public StrategyOpenPanel() {
        JShrinkShortcut subscriptionParameterShrinkShortcut = new JShrinkShortcut();
        subscriptionParameterShrinkShortcut.setTitle(ConsoleLocaleFactory.getString("subscription_parameter_text"));
        subscriptionParameterShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        subscriptionParameterShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString("subscription_parameter_text"));

        subscriptionPanel = new JPanel();
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
                    if (StringUtils.equals(radioButton.getName(), SubscriptionType.GLOBAL.toString())) {
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
        ComboBoxUtil.installlAutoCompletion(groupComboBox);

        gatewayIdComboBox = new JBasicComboBox();
        gatewayIdComboBox.setEditable(true);
        ComboBoxUtil.installlAutoCompletion(gatewayIdComboBox);

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
        add(subscriptionPanel, "1, 1");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("group_name_text")), "0, 2");
        add(groupComboBox, "1, 2");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("service_name_text")), "0, 3");
        add(gatewayPanel, "1, 3");

        setGroups();
        setGatewayIds();
    }

    public double[] getLayoutRow() {
        return new double[] { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED };
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

    public SubscriptionType getSubscriptionType() {
        String rationButtonName = getRationButtonName(subscriptionButtonGroup);

        return SubscriptionType.fromString(rationButtonName);
    }

    public String getGroup() {
        return ComboBoxUtil.getSelectedValue(groupComboBox);
    }

    public String getGatewayId() {
        return ComboBoxUtil.getSelectedValue(gatewayIdComboBox);
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
}