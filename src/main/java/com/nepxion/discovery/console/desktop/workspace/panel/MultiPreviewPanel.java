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

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.swing.tabbedpane.JBasicTabbedPane;

public class MultiPreviewPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    protected PreviewPanel partialPreviewPanel;
    protected PreviewPanel globalPreviewPanel;

    public MultiPreviewPanel() {
        partialPreviewPanel = new PreviewPanel();
        partialPreviewPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        globalPreviewPanel = new PreviewPanel();
        globalPreviewPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JBasicTabbedPane previewTabbedPane = new JBasicTabbedPane();
        previewTabbedPane.addTab(ConsoleLocaleFactory.getString("partial_config"), partialPreviewPanel, ConsoleLocaleFactory.getString("partial_config"));
        previewTabbedPane.addTab(ConsoleLocaleFactory.getString("global_config"), globalPreviewPanel, ConsoleLocaleFactory.getString("global_config"));

        setLayout(new BorderLayout());
        add(previewTabbedPane, BorderLayout.CENTER);
    }

    public PreviewPanel getPartialPreviewPanel() {
        return partialPreviewPanel;
    }

    public PreviewPanel getGlobalPreviewPanel() {
        return globalPreviewPanel;
    }
}