package com.nepxion.discovery.console.desktop.common.locale;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Locale;

import com.nepxion.util.locale.LocaleManager;

public class ConsoleLocaleFactory {
    public static final Class<?> BUNDLE_CLASS = ConsoleLocaleFactory.class;

    private static String localePath;

    public static void initialize(String localePath) {
        ConsoleLocaleFactory.localePath = localePath;
    }

    public static String getString(String key) {
        return LocaleManager.getString(BUNDLE_CLASS, key);
    }

    public static String getString(String key, Locale locale) {
        return LocaleManager.getString(BUNDLE_CLASS, key, locale);
    }

    public static String getLocalePath() {
        return localePath;
    }
}