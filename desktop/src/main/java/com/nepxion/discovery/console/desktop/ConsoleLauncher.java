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

public class ConsoleLauncher {
    public static void main(String[] args) {
        ConsoleInitializer.initialize();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ConsoleLogin consoleLogin = new ConsoleLogin();
                consoleLogin.launch();

                ConsoleFrame consoleFrame = new ConsoleFrame();
                consoleFrame.launch();
            }
        });
    }
}