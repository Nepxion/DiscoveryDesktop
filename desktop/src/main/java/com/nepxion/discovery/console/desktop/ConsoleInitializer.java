package com.nepxion.discovery.console.desktop;

import com.nepxion.discovery.console.desktop.common.context.DataContext;
import com.nepxion.discovery.console.desktop.common.context.PropertiesContext;
import com.nepxion.discovery.console.desktop.common.context.UIContext;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;

public class ConsoleInitializer {
    private static final String PROPERTIES_PATH = "config/console.properties";
    private static final String ICON_PATH = "com/nepxion/discovery/console/desktop/common/icon/";
    private static final String LOCALE_PATH = "com/nepxion/discovery/console/desktop/common/locale/";

    public static void initialize() {
        PropertiesContext.initialize(PROPERTIES_PATH);
        DataContext.initialize();
        UIContext.initialize();

        ConsoleIconFactory.initialize(ICON_PATH);
        ConsoleLocaleFactory.initialize(LOCALE_PATH);
    }
}