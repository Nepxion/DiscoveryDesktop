package com.nepxion.discovery.console.desktop.workspace.util;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.awt.Component;
import java.awt.Dimension;

import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.label.JBasicLabel;

public class ComponentUtil {
    public static Component addWidth(JBasicLabel Component, int width) {
        Component.setPreferredSize(new Dimension(Component.getPreferredSize().width + width, Component.getPreferredSize().height));

        return Component;
    }

    public static Component addHeight(JBasicLabel Component, int height) {
        Component.setPreferredSize(new Dimension(Component.getPreferredSize().width, Component.getPreferredSize().height + height));

        return Component;
    }

    public static Component setWidth(Component component, int width) {
        component.setPreferredSize(new Dimension(width, component.getPreferredSize().height));

        return component;
    }

    public static Component setHeight(Component component, int height) {
        component.setPreferredSize(new Dimension(component.getPreferredSize().width, height));

        return component;
    }

    public static String getSelectedValue(JBasicComboBox comoboBox) {
        return comoboBox.getSelectedItem() != null ? comoboBox.getSelectedItem().toString().trim() : null;
    }
}