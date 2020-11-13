package com.nepxion.discovery.console.desktop;

import com.nepxion.discovery.console.desktop.common.component.AbstractConsoleLogin;

public class ConsoleLogin extends AbstractConsoleLogin {
    private static final long serialVersionUID = 1L;

    @Override
    public void initialize() {
        accountTextField.setText("admin");
        passwordField.setText("admin");
    }
}