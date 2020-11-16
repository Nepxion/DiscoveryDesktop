package com.nepxion.discovery.console.desktop;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.console.desktop.common.context.ConsoleDataContext;
import com.nepxion.discovery.console.desktop.common.context.ConsolePropertiesContext;
import com.nepxion.discovery.console.desktop.common.context.ConsoleFontContext;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;

public class ConsoleInitializer {
    private static final String PROPERTIES_PATH = "config/console.properties";
    private static final String ICON_PATH = "com/nepxion/discovery/console/desktop/common/icon/";
    private static final String LOCALE_PATH = "com/nepxion/discovery/console/desktop/common/locale/";

    public static void initialize() {
        ConsolePropertiesContext.initialize(PROPERTIES_PATH);
        ConsoleDataContext.initialize();
        ConsoleFontContext.initialize();

        ConsoleIconFactory.initialize(ICON_PATH);
        ConsoleLocaleFactory.initialize(LOCALE_PATH);
    }
}