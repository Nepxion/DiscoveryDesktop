package com.nepxion.discovery.console.cache;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.console.controller.ConsoleController;

public class ConsoleCache {
    private static String discoveryType;
    private static String configType;

    private static Object[] groups;
    private static Object[] services;

    public static String getDiscoveryType() {
        if (discoveryType == null) {
            discoveryType = ConsoleController.getDiscoveryType();
        }

        return discoveryType;
    }

    public static String getConfigType() {
        if (configType == null) {
            configType = ConsoleController.getConfigType();
        }

        return configType;
    }

    public static Object[] getGroups() {
        if (groups == null) {
            groups = ConsoleController.getGroups().toArray();
        }

        return groups;
    }

    public static Object[] refreshGroups() {
        groups = null;

        return getGroups();
    }
    
    public static Object[] getServices() {
        if (services == null) {
            services = ConsoleController.getServices().toArray();
        }

        return services;
    }

    public static Object[] refreshServices() {
        services = null;

        return getServices();
    }
}