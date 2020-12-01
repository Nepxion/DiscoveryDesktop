package com.nepxion.discovery.console.desktop.common.util;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import com.nepxion.swing.button.JBasicButton;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.lookandfeel.LookAndFeelManager;

public class ButtonUtil {
    public static AbstractButton createButton(AbstractAction action) {
        return LookAndFeelManager.isNimbusLookAndFeel() ? new JClassicButton(action) : new JBasicButton(action);
    }

    public static String getRationButtonName(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> enumeration = buttonGroup.getElements(); enumeration.hasMoreElements();) {
            AbstractButton button = enumeration.nextElement();
            if (button.isSelected()) {
                return button.getName();
            }
        }

        return null;
    }
}