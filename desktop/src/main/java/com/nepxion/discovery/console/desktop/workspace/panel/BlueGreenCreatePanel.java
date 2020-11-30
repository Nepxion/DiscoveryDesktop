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

import com.nepxion.discovery.common.entity.BlueGreenRouteType;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.workspace.type.TypeLocale;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.radiobutton.JBasicRadioButton;

public class BlueGreenCreatePanel extends StrategyCreatePanel {
    private static final long serialVersionUID = 1L;

    protected ButtonGroup blueGreenRouteButtonGroup;
    protected JPanel blueGreenRoutePanel;

    public BlueGreenCreatePanel() {
        blueGreenRoutePanel = new JPanel();
        blueGreenRoutePanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 10));
        blueGreenRouteButtonGroup = new ButtonGroup();
        BlueGreenRouteType[] blueGreenRouteTypes = BlueGreenRouteType.values();
        for (int i = 0; i < blueGreenRouteTypes.length; i++) {
            BlueGreenRouteType blueGreenRouteType = blueGreenRouteTypes[i];

            JBasicRadioButton blueGreenRouteRadioButton = new JBasicRadioButton(TypeLocale.getDescription(blueGreenRouteType), TypeLocale.getDescription(blueGreenRouteType));
            blueGreenRouteRadioButton.setName(blueGreenRouteType.toString());
            blueGreenRoutePanel.add(blueGreenRouteRadioButton);
            blueGreenRouteButtonGroup.add(blueGreenRouteRadioButton);

            if (i == 0) {
                blueGreenRouteRadioButton.setSelected(true);
            }
        }

        add(new JBasicLabel(ConsoleLocaleFactory.getString("route_text")), "0, 12");
        add(blueGreenRoutePanel, "1, 12");
    }

    public BlueGreenRouteType getBlueGreenRouteType() {
        String rationButtonName = getRationButtonName(blueGreenRouteButtonGroup);

        return BlueGreenRouteType.fromString(rationButtonName);
    }

    @Override
    public double[] getLayoutRow() {
        double[] layoutRow = super.getLayoutRow();

        double[] newLayoutRow = new double[layoutRow.length + 1];
        for (int i = 0; i < layoutRow.length; i++) {
            newLayoutRow[i] = layoutRow[i];
        }

        newLayoutRow[layoutRow.length] = TableLayout.PREFERRED;

        return newLayoutRow;
    }

    @Override
    public void setNewMode(boolean isNewMode) {
        super.setNewMode(isNewMode);

        if (blueGreenRoutePanel != null) {
            for (int i = 0; i < blueGreenRoutePanel.getComponentCount(); i++) {
                blueGreenRoutePanel.getComponent(i).setEnabled(isNewMode);
            }
        }
    }
}