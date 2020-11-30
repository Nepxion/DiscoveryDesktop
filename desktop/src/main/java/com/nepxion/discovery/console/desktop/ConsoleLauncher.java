package com.nepxion.discovery.console.desktop;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import javax.swing.SwingUtilities;

import com.nepxion.discovery.console.desktop.common.context.ConsoleConstant;

public class ConsoleLauncher {
    public static void main(String[] args) {
        ConsoleInitializer.initialize();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Boolean loginEnabled = Boolean.valueOf(System.getProperty(ConsoleConstant.LOGIN_ENABLED, Boolean.TRUE.toString()));
                if (loginEnabled) {
                    ConsoleLogin consoleLogin = new ConsoleLogin();
                    consoleLogin.launch();
                }

                ConsoleFrame consoleFrame = new ConsoleFrame();
                consoleFrame.launch();
            }
        });
    }
}