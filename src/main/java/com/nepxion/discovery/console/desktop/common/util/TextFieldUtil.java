package com.nepxion.discovery.console.desktop.common.util;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.swing.textfield.JBasicTextField;

public class TextFieldUtil {
    public static JBasicTextField setTip(JBasicTextField textField, String tip) {
        textField.setText(tip);
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                String text = textField.getText().trim();
                if (StringUtils.equals(text, tip)) {
                    textField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String text = textField.getText().trim();
                if (StringUtils.isBlank(text)) {
                    textField.setText(tip);
                }
            }
        });

        return textField;
    }
}