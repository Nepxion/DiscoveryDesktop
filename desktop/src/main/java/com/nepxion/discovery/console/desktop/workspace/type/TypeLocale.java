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
import com.nepxion.discovery.common.entity.ConfigType;
import com.nepxion.discovery.common.entity.DeployType;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;

public class TypeLocale {
    public static String getName(StrategyType strategyType) {
        return ConsoleLocaleFactory.getString(strategyType.toString());
    }

    public static String getDescription(ReleaseType releaseType) {
        return ConsoleLocaleFactory.getString(releaseType.toString() + "_release");
    }

    public static String getDescription(StrategyType strategyType) {
        return ConsoleLocaleFactory.getString(strategyType.toString() + "_strategy");
    }

    public static String getDescription(ConfigType configType) {
        return ConsoleLocaleFactory.getString(configType.toString() + "_config");
    }

    public static String getDescription(DeployType deployType) {
        return ConsoleLocaleFactory.getString(deployType.toString() + "_deploy");
    }

    public static String getDescription(BlueGreenRouteType blueGreenRouteType) {
        return ConsoleLocaleFactory.getString(blueGreenRouteType.toString() + "_route");
    }

    public static String getDescription(ElementType elementType) {
        return ConsoleLocaleFactory.getString(elementType.toString() + "_type");
    }
}