package com.nepxion.discovery.console.cache;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.nepxion.discovery.common.entity.ConfigType;
import com.nepxion.discovery.common.entity.DiscoveryType;
import com.nepxion.discovery.console.controller.ConsoleController;

public class ConsoleCache {
    private static DiscoveryType discoveryType;
    private static ConfigType configType;

    private static List<String> groups;
    private static List<String> gateways;
    private static List<String> services;

    private static boolean cacheEnabled = true;

    public static DiscoveryType getDiscoveryType() {
        if (discoveryType == null) {
            discoveryType = DiscoveryType.fromString(ConsoleController.getDiscoveryType());
        }

        return discoveryType;
    }

    public static ConfigType getConfigType() {
        if (configType == null) {
            configType = ConfigType.fromString(ConsoleController.getConfigType());
        }

        return configType;
    }

    public static List<String> getGroups() {
        if (groups == null || !cacheEnabled) {
            groups = ConsoleController.getGroups();
        }

        return groups;
    }

    public static List<String> getCachedGroups() {
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

    public static List<String> getCachedGateways() {
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

    public static List<String> getCachedServices() {
        return services;
    }

    public static List<String> refreshServices() {
        services = ConsoleController.getServices();

        return services;
    }

    public static List<String> getRealServices() {
        List<String> services = getServices();
        if (CollectionUtils.isEmpty(services)) {
            return null;
        }

        List<String> gateways = getGateways();

        List<String> realServices = new ArrayList<String>();
        for (String service : services) {
            if (CollectionUtils.isNotEmpty(gateways)) {
                if (!gateways.contains(service)) {
                    realServices.add(service);
                }
            } else {
                realServices.add(service);
            }
        }

        return realServices;
    }

    public static String getKey(String group, String serviceId) {
        ConfigType configType = getConfigType();
        String key = null;
        if (ConfigType.isSingleKey(configType)) {
            key = group + "-" + serviceId;
        } else {
            key = "Data ID=" + serviceId + " | Group=" + group;
        }

        return key;
    }

    public static boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public static void setCacheEnabled(boolean cacheEnabled) {
        ConsoleCache.cacheEnabled = cacheEnabled;
    }
}