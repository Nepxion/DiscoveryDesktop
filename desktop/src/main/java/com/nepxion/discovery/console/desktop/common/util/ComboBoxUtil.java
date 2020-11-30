package com.nepxion.discovery.console.desktop.common.util;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Collections;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.apache.commons.lang3.SystemUtils;

import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.completion.JAutoCompletion;
import com.nepxion.swing.searchable.JSearchableFactory;

public class ComboBoxUtil {
    public static void installSearchable(JBasicComboBox comboBox) {
        if (SystemUtils.IS_OS_WINDOWS) {
            JSearchableFactory.installSearchable(comboBox);
        }
    }

    public static void installlAutoCompletion(JBasicComboBox comboBox) {
        if (SystemUtils.IS_OS_WINDOWS) {
            JAutoCompletion autoCompletion = new JAutoCompletion(comboBox);
            autoCompletion.setStrict(false);
        }
    }

    @SuppressWarnings("unchecked")
    public static void setSortableModel(JBasicComboBox comboBox, List<String> value) {
        Collections.sort(value);

        comboBox.setModel(new DefaultComboBoxModel<>(value.toArray()));
    }

    public static String getSelectedValue(JBasicComboBox comboBox) {
        return comboBox.getSelectedItem() != null ? comboBox.getSelectedItem().toString().trim() : null;
    }
}