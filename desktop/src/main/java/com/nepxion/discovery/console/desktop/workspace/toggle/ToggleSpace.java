package com.nepxion.discovery.console.desktop.workspace.toggle;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.nepxion.discovery.console.desktop.workspace.BlueGreenTopology;
import com.nepxion.discovery.console.desktop.workspace.GrayTopology;
import com.nepxion.discovery.console.desktop.workspace.type.ReleaseType;
import com.nepxion.swing.element.IElementNode;

public class ToggleSpace extends JPanel {
    private static final long serialVersionUID = 1L;

    private IElementNode listElementNode;
    private JPanel blankPane = new JPanel();

    public ToggleSpace(IElementNode listElementNode) {
        this.listElementNode = listElementNode;

        setLayout(new BorderLayout());
        add(createContentPane(), BorderLayout.CENTER);
    }

    private JComponent createContentPane() {
        JComponent contentPane = null;

        String name = listElementNode.getName();
        ReleaseType releaseType = ReleaseType.fromString(name);
        switch (releaseType) {
            case BLUE_GREEN:
                contentPane = new BlueGreenTopology();
                break;
            case GRAY:
                contentPane = new GrayTopology();
                break;
            case BLACKLIST:
                contentPane = blankPane;
                break;
        }

        return contentPane;
    }
}