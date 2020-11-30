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

import com.nepxion.discovery.console.desktop.workspace.type.SetType;

public class SetPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    protected SetType setType;

    public SetPanel(SetType setType) {
        this.setType = setType;
    }

    public SetType getSetType() {
        return setType;
    }

    public void setSetType(SetType setType) {
        this.setType = setType;
    }
}