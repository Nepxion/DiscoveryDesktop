package com.nepxion.discovery.console.desktop.common.util;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;

import com.nepxion.swing.button.JBasicButton;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.lookandfeel.LookAndFeelManager;

public class ButtonUtil {
    public static AbstractButton createButton(AbstractAction action) {
        return LookAndFeelManager.isNimbusLookAndFeel() ? new JClassicButton(action) : new JBasicButton(action);
    }
}