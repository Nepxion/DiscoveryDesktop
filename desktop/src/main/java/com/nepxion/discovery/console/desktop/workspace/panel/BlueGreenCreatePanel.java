package com.nepxion.discovery.console.desktop.workspace.panel;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.common.entity.BlueGreenRouteType;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.workspace.type.TypeLocale;
import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.table.TableLayout;

public class BlueGreenCreatePanel extends CreatePanel {
    private static final long serialVersionUID = 1L;

    protected JBasicComboBox blueGreenRouteComboBox;

    public BlueGreenCreatePanel() {
        BlueGreenRouteType[] blueGreenRouteTypes = BlueGreenRouteType.values();
        ElementNode[] blueGreenRouteElementNodes = new ElementNode[blueGreenRouteTypes.length];
        for (int i = 0; i < blueGreenRouteTypes.length; i++) {
            BlueGreenRouteType blueGreenRouteType = blueGreenRouteTypes[i];
            blueGreenRouteElementNodes[i] = new ElementNode();
            blueGreenRouteElementNodes[i].setName(blueGreenRouteType.toString());
            blueGreenRouteElementNodes[i].setText(TypeLocale.getDescription(blueGreenRouteType));
            blueGreenRouteElementNodes[i].setUserObject(blueGreenRouteType);
        }
        blueGreenRouteComboBox = new JBasicComboBox(blueGreenRouteElementNodes);

        add(new JBasicLabel(ConsoleLocaleFactory.getString("route_text")), "0, 5");
        add(blueGreenRouteComboBox, "1, 5");
    }

    public BlueGreenRouteType getBlueGreenRouteType() {
        ElementNode blueGreenRouteElementNode = (ElementNode) blueGreenRouteComboBox.getSelectedItem();
        BlueGreenRouteType blueGreenRouteType = (BlueGreenRouteType) blueGreenRouteElementNode.getUserObject();

        return blueGreenRouteType;
    }

    @Override
    public double[] getLayoutRow() {
        return new double[] { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED };
    }
}