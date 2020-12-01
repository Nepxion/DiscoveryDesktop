package com.nepxion.discovery.console.desktop.workspace.panel;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

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

    protected ButtonGroup strategyButtonGroup;
    protected JPanel strategyPanel;

    public StrategyCreatePanel() {
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

        add(releaseShrinkShortcut, "0, 8, 1, 8");
        add(new JBasicLabel(ConsoleLocaleFactory.getString("strategy_text")), "0, 9");
        add(strategyPanel, "1, 9");
    }

    @Override
    public double[] getLayoutRow() {
        return new double[] { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, 0, TableLayout.PREFERRED, TableLayout.PREFERRED, 0, TableLayout.PREFERRED, TableLayout.PREFERRED };
    }

    public StrategyType getStrategyType() {
        String rationButtonName = ButtonUtil.getRationButtonName(strategyButtonGroup);

        return StrategyType.fromString(rationButtonName);
    }
}