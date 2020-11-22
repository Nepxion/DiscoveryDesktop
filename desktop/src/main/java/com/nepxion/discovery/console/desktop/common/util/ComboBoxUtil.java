package com.nepxion.discovery.console.desktop.common.util;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.completion.JAutoCompletion;
import com.nepxion.swing.searchable.JSearchableFactory;

public class ComboBoxUtil {
    public static void installSearchable(JBasicComboBox comoboBox) {
        JSearchableFactory.installSearchable(comoboBox);
    }

    public static JAutoCompletion installlAutoCompletion(JBasicComboBox comoboBox) {
        JAutoCompletion autoCompletion = new JAutoCompletion(comoboBox);
        autoCompletion.setStrict(false);

        return autoCompletion;
    }

    public static String getSelectedValue(JBasicComboBox comoboBox) {
        return comoboBox.getSelectedItem() != null ? comoboBox.getSelectedItem().toString().trim() : null;
    }
}