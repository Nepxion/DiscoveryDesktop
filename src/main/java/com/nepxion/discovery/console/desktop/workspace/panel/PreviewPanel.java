package com.nepxion.discovery.console.desktop.workspace.panel;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.awt.Dimension;

import javax.swing.JPanel;

import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.scrollpane.JBasicScrollPane;
import com.nepxion.swing.shrinkbar.JShrinkShortcut;
import com.nepxion.swing.textarea.JBasicTextArea;
import com.nepxion.swing.textfield.JBasicTextField;

public class PreviewPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    protected JBasicTextField keyTextField;
    protected JBasicTextArea configTextArea;

    public PreviewPanel() {
        JShrinkShortcut keyShrinkShortcut = new JShrinkShortcut();
        keyShrinkShortcut.setTitle(ConsoleLocaleFactory.getString("config_key_text"));
        keyShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        keyShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString("config_key_text"));

        keyTextField = new JBasicTextField();

        JShrinkShortcut contentShrinkShortcut = new JShrinkShortcut();
        contentShrinkShortcut.setTitle(ConsoleLocaleFactory.getString("config_content_text"));
        contentShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        contentShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString("config_content_text"));

        configTextArea = new JBasicTextArea();
        JBasicScrollPane configTextAreaScrollPane = new JBasicScrollPane(configTextArea);
        configTextAreaScrollPane.setPreferredSize(new Dimension(660, 340));

        double[][] size = {
                { TableLayout.FILL },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, 10, TableLayout.PREFERRED, TableLayout.FILL }
        };

        TableLayout tableLayout = new TableLayout(size);
        tableLayout.setHGap(0);
        tableLayout.setVGap(5);

        setLayout(tableLayout);
        add(keyShrinkShortcut, "0, 0");
        add(keyTextField, "0, 1");
        add(contentShrinkShortcut, "0, 3");
        add(configTextAreaScrollPane, "0, 4");
    }

    public void setKey(String key) {
        keyTextField.setText(key);
    }

    public void setConfig(String config) {
        configTextArea.setText(config);
        configTextArea.setCaretPosition(0);
    }

    public String getConfig() {
        return configTextArea.getText().trim();
    }
}