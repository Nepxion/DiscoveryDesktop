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
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.swing.dialog.JExceptionDialog;
import com.nepxion.discovery.console.desktop.workspace.processor.ReleaseProcessor;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.optionpane.JBasicOptionPane;
import com.nepxion.swing.scrollpane.JBasicScrollPane;
import com.nepxion.swing.selector.file.JFileFilter;
import com.nepxion.swing.selector.file.JFileSelector;
import com.nepxion.swing.shrinkbar.JShrinkShortcut;
import com.nepxion.swing.textarea.JBasicTextArea;

public class OpenPanel extends SubscriptionPanel {
    private static final long serialVersionUID = -4561221242634636904L;

    protected ReleaseProcessor releaseProcessor;

    protected JBasicTextArea configTextArea;

    public OpenPanel(ReleaseProcessor releaseProcessor) {
        this.releaseProcessor = releaseProcessor;

        JPanel buttonBar = new JPanel();
        buttonBar.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 10));
        buttonBar.add(new JClassicButton(createOpenRemoteAction()));
        buttonBar.add(new JClassicButton(createOpenLocalAction()));

        JShrinkShortcut previewShrinkShortcut = new JShrinkShortcut();
        previewShrinkShortcut.setTitle(ConsoleLocaleFactory.getString("preview_config_tooltip"));
        previewShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        previewShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString("preview_config_tooltip"));

        configTextArea = new JBasicTextArea();
        JBasicScrollPane configTextAreaScrollPane = new JBasicScrollPane(configTextArea);
        configTextAreaScrollPane.setPreferredSize(new Dimension(660, 330));

        add(buttonBar, "0, 4, 1, 4");
        add(previewShrinkShortcut, "0, 6, 1, 6");
        add(configTextAreaScrollPane, "0, 7, 1, 7");
    }

    @Override
    public double[] getLayoutRow() {
        return new double[] { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, 0, TableLayout.PREFERRED, TableLayout.FILL };
    }

    public JSecurityAction createOpenRemoteAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("open_config_remote_text"), ConsoleIconFactory.getSwingIcon("theme/folder/query.png"), ConsoleLocaleFactory.getString("open_config_remote_text")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                String group = getValidGroup();
                if (StringUtils.isEmpty(group)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(OpenPanel.this), ConsoleLocaleFactory.getString("group_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                String gatewayId = getValidGatewayId();
                if (StringUtils.isEmpty(gatewayId)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(OpenPanel.this), ConsoleLocaleFactory.getString("service_id_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                String config = releaseProcessor.getConfig(group, gatewayId);
                configTextArea.setText(config);
            }
        };

        return action;
    }

    public JSecurityAction createOpenLocalAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("open_config_local_text"), ConsoleIconFactory.getSwingIcon("theme/folder/folder_import.png"), ConsoleLocaleFactory.getString("open_config_local_text")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                List<String> filterWords = Arrays.asList(new String[] { "xml" });
                String filterDescription = ConsoleLocaleFactory.getString("config_file_text") + "(*.xml)";
                JFileFilter fileFilter = new JFileFilter(filterWords, filterDescription);

                JFileSelector fileSelector = new JFileSelector(HandleManager.getFrame(OpenPanel.this), ConsoleLocaleFactory.getString("open_config_local_text"));
                fileSelector.setFileFilter(fileFilter);
                File file = fileSelector.openFile();
                if (file == null) {
                    return;
                }

                String config = null;
                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(file);
                    config = IOUtils.toString(inputStream, DiscoveryConstant.ENCODING_UTF_8);
                } catch (Exception ex) {
                    JExceptionDialog.traceException(HandleManager.getFrame(OpenPanel.this), ConsoleLocaleFactory.getString("operation_failure"), ex);
                } finally {
                    if (inputStream != null) {
                        IOUtils.closeQuietly(inputStream);
                    }
                }

                configTextArea.setText(config);
                configTextArea.setCaretPosition(0);
            }
        };

        return action;
    }
}