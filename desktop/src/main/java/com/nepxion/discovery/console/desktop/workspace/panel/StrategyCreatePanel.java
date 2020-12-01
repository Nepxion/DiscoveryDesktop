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
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.discovery.common.entity.DeployType;
import com.nepxion.discovery.common.entity.SubscriptionType;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.util.ButtonUtil;
import com.nepxion.discovery.console.desktop.workspace.type.StrategyType;
import com.nepxion.discovery.console.desktop.workspace.type.TypeLocale;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.radiobutton.JBasicRadioButton;
import com.nepxion.swing.shrinkbar.JShrinkShortcut;

public class StrategyCreatePanel extends SubscriptionPanel {
    private static final long serialVersionUID = 1L;

    protected ButtonGroup deployButtonGroup;
    protected JPanel deployPanel;

    protected ButtonGroup strategyButtonGroup;
    protected JPanel strategyPanel;

    public StrategyCreatePanel() {
        JShrinkShortcut deployParameterShrinkShortcut = new JShrinkShortcut();
        deployParameterShrinkShortcut.setTitle(ConsoleLocaleFactory.getString("deploy_parameter_text"));
        deployParameterShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        deployParameterShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString("deploy_parameter_text"));

        deployPanel = new JPanel();
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

        JShrinkShortcut releaseShrinkShortcut = new JShrinkShortcut();
        releaseShrinkShortcut.setTitle(ConsoleLocaleFactory.getString("release_parameter_text"));
        releaseShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        releaseShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString("release_parameter_text"));

        strategyPanel = new JPanel();
        strategyPanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 10));
        strategyButtonGroup = new ButtonGroup();
        StrategyType[] strategyTypes = StrategyType.values();
        for (int i = 0; i < strategyTypes.length; i++) {
            StrategyType strategyType = strategyTypes[i];
            JBasicRadioButton strategyRadioButton = new JBasicRadioButton(TypeLocale.getDescription(strategyType), TypeLocale.getDescription(strategyType));
            strategyRadioButton.setName(strategyType.toString());
            strategyPanel.add(strategyRadioButton);
            strategyButtonGroup.add(strategyRadioButton);

            if (i == 0) {
                strategyRadioButton.setSelected(true);
            }
        }

        double[][] size = {
                { TableLayout.PREFERRED, TableLayout.FILL },
                getLayoutRow()
        };

        TableLayout tableLayout = new TableLayout(size);
        tableLayout.setHGap(20);
        tableLayout.setVGap(10);

        setLayout(tableLayout);
        removeAll();
        add(subscriptionParameterShrinkShortcut, "0, 0, 1, 0");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("subscription_text")), "0, 1");
        add(subscriptionRadioButtonPanel, "1, 1");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("group_name_text")), "0, 2");
        add(groupComboBox, "1, 2");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("service_name_text")), "0, 3");
        add(gatewayPanel, "1, 3");
        add(deployParameterShrinkShortcut, "0, 5, 1, 5");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("deploy_text")), "0, 6");
        add(deployPanel, "1, 6");
        add(releaseShrinkShortcut, "0, 8, 1, 8");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("strategy_text")), "0, 9");
        add(strategyPanel, "1, 9");
    }

    @Override
    public double[] getLayoutRow() {
        return new double[] { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, 0, TableLayout.PREFERRED, TableLayout.PREFERRED, 0, TableLayout.PREFERRED, TableLayout.PREFERRED };
    }

    @Override
    public void subscriptionRadioButtonItemStateChanged(ItemEvent e) {
        super.subscriptionRadioButtonItemStateChanged(e);

        JBasicRadioButton radioButton = (JBasicRadioButton) e.getItem();
        if (StringUtils.equals(radioButton.getName(), SubscriptionType.GLOBAL.toString())) {
            if (deployButtonGroup != null) {
                for (Enumeration<AbstractButton> enumeration = deployButtonGroup.getElements(); enumeration.hasMoreElements();) {
                    AbstractButton button = enumeration.nextElement();
                    button.setEnabled(!radioButton.isSelected());

                    // 全局配置下，处理为非域网关模式
                    if (StringUtils.equals(button.getName(), DeployType.NON_DOMAIN_GATEWAY.toString())) {
                        button.setSelected(true);
                    }
                }
            }
        }
    }

    public StrategyType getStrategyType() {
        String rationButtonName = ButtonUtil.getRationButtonName(strategyButtonGroup);

        return StrategyType.fromString(rationButtonName);
    }

    public DeployType getDeployType() {
        String rationButtonName = ButtonUtil.getRationButtonName(deployButtonGroup);

        return DeployType.fromString(rationButtonName);
    }
}