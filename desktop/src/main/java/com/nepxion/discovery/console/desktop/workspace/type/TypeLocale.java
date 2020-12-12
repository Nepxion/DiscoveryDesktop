package com.nepxion.discovery.console.desktop.workspace.type;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.common.entity.BlueGreenRouteType;
import com.nepxion.discovery.common.entity.DeployType;
import com.nepxion.discovery.common.entity.ElementType;
import com.nepxion.discovery.common.entity.SubscriptionType;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;

public class TypeLocale {
    public static String getDescription(ReleaseType releaseType) {
        return getDescription(releaseType, "_release");
    }

    public static String getDescription(AuthorityType authorityType) {
        return getDescription(authorityType, "_authority");
    }

    public static String getDescription(StrategyType strategyType) {
        return getDescription(strategyType, "_strategy");
    }

    public static String getDescription(SubscriptionType subscriptionType) {
        return getDescription(subscriptionType, "_subscription");
    }

    public static String getDescription(DeployType deployType) {
        return getDescription(deployType, "_deploy");
    }

    public static String getDescription(BlueGreenRouteType blueGreenRouteType) {
        return getDescription(blueGreenRouteType, "_route");
    }

    public static String getDescription(ElementType elementType) {
        return getDescription(elementType, "_element");
    }

    public static String getDescription(FeatureType featureType) {
        return getDescription(featureType, "_feature");
    }

    public static String getDescription(SetType setType) {
        return getDescription(setType, "_set");
    }

    public static String getDescription(PortalType portalType) {
        return getDescription(portalType, "_portal");
    }

    public static String getDescription(DimensionType dimensionType) {
        return getDescription(dimensionType, "_dimension");
    }

    public static String getName(Object type) {
        return ConsoleLocaleFactory.getString(type.toString());
    }

    public static String getDescription(Object type, String suffix) {
        return ConsoleLocaleFactory.getString(type + suffix);
    }
}