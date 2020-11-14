package com.nepxion.discovery.console.desktop.workspace.util;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.swing.combobox.JBasicComboBox;

public class ComboBoxUtil {
    public static String getSelectedValue(JBasicComboBox comoboBox) {
        return comoboBox.getSelectedItem() != null ? comoboBox.getSelectedItem().toString().trim() : null;
    }
}