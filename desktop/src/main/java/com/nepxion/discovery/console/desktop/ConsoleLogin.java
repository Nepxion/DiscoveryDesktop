package com.nepxion.discovery.console.desktop;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.console.desktop.component.AbstractConsoleLogin;

public class ConsoleLogin extends AbstractConsoleLogin {
    private static final long serialVersionUID = 1L;

    @Override
    public void initialize() {
        accountTextField.setText("admin");
        passwordField.setText("admin");
    }
}