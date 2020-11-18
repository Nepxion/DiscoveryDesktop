package com.nepxion.discovery.console.desktop;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.nepxion.banner.NepxionBanner;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.console.desktop.common.context.ConsoleBannerContext;
import com.nepxion.discovery.console.desktop.common.context.ConsoleDataContext;
import com.nepxion.discovery.console.desktop.common.context.ConsolePropertiesContext;
import com.nepxion.discovery.console.desktop.common.context.ConsoleUIContext;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.taobao.text.Color;

public class ConsoleInitializer {
    private static final String PROPERTIES_PATH = "config/console.properties";
    private static final String ICON_PATH = "com/nepxion/discovery/console/desktop/common/icon/";
    private static final String LOCALE_PATH = "com/nepxion/discovery/console/desktop/common/locale/";

    public static void initialize() {
        ConsoleBannerContext.initialize();
        ConsolePropertiesContext.initialize(PROPERTIES_PATH);
        ConsoleDataContext.initialize();
        ConsoleUIContext.initialize();

        ConsoleIconFactory.initialize(ICON_PATH);
        ConsoleLocaleFactory.initialize(LOCALE_PATH);
    }
}