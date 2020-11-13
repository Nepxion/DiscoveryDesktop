package com.nepxion.discovery.console.desktop.common.icon;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import javax.swing.ImageIcon;

import com.nepxion.swing.icon.IconFactory;

public class ConsoleIconFactory extends IconFactory {
    private static String iconPath;

    public static void initialize(String iconPath) {
        ConsoleIconFactory.iconPath = iconPath;
    }

    public static ImageIcon getContextIcon(String iconName) {
        return getIcon(iconPath + iconName);
    }

    public static String getIconPath() {
        return iconPath;
    }
}