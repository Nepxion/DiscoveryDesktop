package com.nepxion.discovery.console.cache;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.List;

import com.nepxion.discovery.console.controller.ConsoleController;

public class ConsoleCache {
    private static String discoveryType;
    private static String configType;

    private static List<String> groups;
    private static List<String> gateways;
    private static List<String> services;

    private static boolean cacheEnabled = true;

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

    public static List<String> getGroups() {
        if (groups == null || !cacheEnabled) {
            groups = ConsoleController.getGroups();
        }

        return groups;
    }

    public static List<String> refreshGroups() {
        groups = ConsoleController.getGroups();

        return groups;
    }

    public static List<String> getGateways() {
        if (gateways == null || !cacheEnabled) {
            gateways = ConsoleController.getGateways();
        }

        return gateways;
    }

    public static List<String> refreshGateways() {
        gateways = ConsoleController.getGateways();

        return gateways;
    }

    public static List<String> getServices() {
        if (services == null || !cacheEnabled) {
            services = ConsoleController.getServices();
        }

        return services;
    }

    public static List<String> refreshServices() {
        services = ConsoleController.getServices();

        return services;
    }

    public static boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public static void setCacheEnabled(boolean cacheEnabled) {
        ConsoleCache.cacheEnabled = cacheEnabled;
    }
}