package com.nepxion.discovery.console.desktop;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.console.desktop.context.DataContext;
import com.nepxion.discovery.console.desktop.context.PropertiesContext;
import com.nepxion.discovery.console.desktop.context.UIContext;
import com.nepxion.discovery.console.desktop.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.locale.ConsoleLocaleFactory;

public class ConsoleInitializer {
    private static final String PROPERTIES_PATH = "config/console.properties";
    private static final String ICON_PATH = "com/nepxion/discovery/console/desktop/icon/";
    private static final String LOCALE_PATH = "com/nepxion/discovery/console/desktop/locale/";

    public static void initialize() {
        PropertiesContext.initialize(PROPERTIES_PATH);
        DataContext.initialize();
        UIContext.initialize();

        ConsoleIconFactory.initialize(ICON_PATH);
        ConsoleLocaleFactory.initialize(LOCALE_PATH);
    }
}