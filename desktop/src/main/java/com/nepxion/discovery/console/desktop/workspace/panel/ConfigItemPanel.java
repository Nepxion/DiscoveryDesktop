package com.nepxion.discovery.console.desktop.workspace.panel;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import javax.swing.JPanel;

import com.nepxion.discovery.console.desktop.workspace.type.ConfigType;

public class ConfigItemPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    protected ConfigType configType;

    public ConfigItemPanel(ConfigType configType) {
        this.configType = configType;
    }

    public ConfigType getConfigType() {
        return configType;
    }

    public void setConfigType(ConfigType configType) {
        this.configType = configType;
    }
}