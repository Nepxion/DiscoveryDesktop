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
import com.nepxion.discovery.console.desktop.workspace.type.TypeLocale;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.radiobutton.JBasicRadioButton;
import com.nepxion.swing.shrinkbar.JShrinkShortcut;

public class CreatePanel extends SubscriptionPanel {
    private static final long serialVersionUID = 1L;

    protected JShrinkShortcut deployParameterShrinkShortcut;
    protected ButtonGroup deployButtonGroup;
    protected JPanel deployPanel;

    public CreatePanel() {
        deployParameterShrinkShortcut = new JShrinkShortcut();
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

        add(deployParameterShrinkShortcut, "0, 5, 1, 5");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("deploy_text")), "0, 6");
        add(deployPanel, "1, 6");
    }

    @Override
    public double[] getLayoutRow() {
        return new double[] { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, 0, TableLayout.PREFERRED, TableLayout.PREFERRED };
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

    public DeployType getDeployType() {
        String rationButtonName = ButtonUtil.getRationButtonName(deployButtonGroup);

        return DeployType.fromString(rationButtonName);
    }
}