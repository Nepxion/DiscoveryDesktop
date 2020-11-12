package com.nepxion.discovery.console.desktop.workspace.panel;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JPanel;

import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.scrollpane.JBasicScrollPane;
import com.nepxion.swing.textarea.JBasicTextArea;

public class PreviewPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JBasicLabel keyLabel;
    private JBasicTextArea configTextArea;

    public PreviewPanel() {
        keyLabel = new JBasicLabel();
        JPanel keyPanel = new JPanel();
        keyPanel.setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 0));
        keyPanel.add(keyLabel);
        keyPanel.add(Box.createVerticalStrut(5));

        configTextArea = new JBasicTextArea();
        JPanel configPanel = new JPanel();
        configPanel.setLayout(new BorderLayout());
        configPanel.add(new JBasicLabel("配置内容 : "), BorderLayout.NORTH);
        configPanel.add(new JBasicScrollPane(configTextArea), BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(keyPanel, BorderLayout.NORTH);
        add(configPanel, BorderLayout.CENTER);
    }

    public void setKey(String key) {
        keyLabel.setText("配置主键 : " + key);
    }

    public void setConfig(String config) {
        configTextArea.setText(config);
    }
}