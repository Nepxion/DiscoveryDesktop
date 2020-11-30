package com.nepxion.discovery.console.desktop.common.context;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.io.File;
import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConsolePropertiesContext extends PropertiesConfiguration {
    private static ConsolePropertiesContext propertiesContext;

    public static void initialize(String propertiesPath) {
        try {
            propertiesContext = new ConsolePropertiesContext(propertiesPath);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static ConsolePropertiesContext getProperties() {
        return propertiesContext;
    }

    public ConsolePropertiesContext(String path) throws ConfigurationException {
        super(path);
    }

    public ConsolePropertiesContext(File file) throws ConfigurationException {
        super(file);
    }

    public ConsolePropertiesContext(URL url) throws ConfigurationException {
        super(url);
    }
}