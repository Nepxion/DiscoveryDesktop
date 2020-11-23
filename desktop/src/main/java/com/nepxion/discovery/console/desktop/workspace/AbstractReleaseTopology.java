package com.nepxion.discovery.console.desktop.workspace;

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

import javax.swing.JToolBar;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.common.entity.SubscriptionType;
import com.nepxion.discovery.console.cache.ConsoleCache;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.swing.dialog.JExceptionDialog;
import com.nepxion.discovery.console.desktop.common.util.ButtonUtil;
import com.nepxion.discovery.console.desktop.workspace.panel.ConfigPanel;
import com.nepxion.discovery.console.desktop.workspace.panel.PreviewPanel;
import com.nepxion.discovery.console.desktop.workspace.processor.StrategyProcessor;
import com.nepxion.discovery.console.desktop.workspace.type.ReleaseType;
import com.nepxion.discovery.console.desktop.workspace.type.StrategyType;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.ButtonManager;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.optionpane.JBasicOptionPane;
import com.nepxion.swing.scrollpane.JBasicScrollPane;
import com.nepxion.swing.textarea.JBasicTextArea;

public abstract class AbstractReleaseTopology extends AbstractTopology {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(AbstractReleaseTopology.class);

    public static final String APOLLO = "Apollo";

    protected ReleaseType releaseType;
    protected StrategyType strategyType;
    protected SubscriptionType subscriptionType;

    protected String group;
    protected String configType;
    protected RuleEntity ruleEntity;

    public AbstractReleaseTopology(ReleaseType releaseType) {
        this.releaseType = releaseType;

        initializeToolBar();
        initializeOperationBar();
        initializeData();
    }

    public void initializeToolBar() {
        JToolBar toolBar = getGraph().getToolbar();
        toolBar.addSeparator();
        toolBar.add(ButtonUtil.createButton(createCreateAction()));
        toolBar.add(ButtonUtil.createButton(createSaveAction()));
        toolBar.addSeparator();
        toolBar.add(ButtonUtil.createButton(createRemoveAction()));
        toolBar.add(ButtonUtil.createButton(createClearAction()));
        toolBar.addSeparator();
        toolBar.add(ButtonUtil.createButton(createPreviewAction()));
        toolBar.add(ButtonUtil.createButton(createConfigAction()));
        toolBar.addSeparator();
        toolBar.add(ButtonUtil.createButton(createLayoutAction()));

        ButtonManager.updateUI(toolBar);
    }

    public void initializeData() {
        try {
            configType = ConsoleCache.getConfigType();
        } catch (Exception ex) {
            JExceptionDialog.traceException(HandleManager.getFrame(AbstractReleaseTopology.this), ConsoleLocaleFactory.getString("operation_failure"), ex);
        }
    }

    public void initializeView() {
        try {
            getStrategyProcessor().fromConfig(ruleEntity, strategyType, dataBox);
        } catch (Exception e) {
            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(this), e.getMessage(), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);
        }

        executeLayout();
    }

    public void showResult(Object result) {
        JBasicTextArea resultTextArea = new JBasicTextArea();
        resultTextArea.setLineWrap(true);
        resultTextArea.setText(result.toString());

        JBasicScrollPane resultScrollPane = new JBasicScrollPane(resultTextArea);
        resultScrollPane.setPreferredSize(new Dimension(resultScrollPane.getPreferredSize().width, resultScrollPane.getPreferredSize().height + 20));
        resultScrollPane.setMaximumSize(new Dimension(800, 600));

        JBasicOptionPane.showOptionDialog(HandleManager.getFrame(this), resultScrollPane, ConsoleLocaleFactory.getString("execute_result"), JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/edit.png"), new Object[] { SwingLocale.getString("close") }, null, true);
    }

    public JSecurityAction createCreateAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("create_text"), ConsoleIconFactory.getSwingIcon("theme/folder/deploy.png"), ConsoleLocaleFactory.getString("create_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                create();
            }
        };

        return action;
    }

    public JSecurityAction createSaveAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("save_text"), ConsoleIconFactory.getSwingIcon("save.png"), ConsoleLocaleFactory.getString("save_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                String config = getStrategyProcessor().toConfig(ruleEntity, strategyType, dataBox);
                if (StringUtils.isEmpty(config)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(AbstractReleaseTopology.this), ConsoleLocaleFactory.getString("strategy_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                int selectedValue = JBasicOptionPane.showConfirmDialog(HandleManager.getFrame(AbstractReleaseTopology.this), ConsoleLocaleFactory.getString("save_confirm") + "\n" + getKey(), SwingLocale.getString("confirm"), JBasicOptionPane.YES_NO_OPTION);
                if (selectedValue != JBasicOptionPane.OK_OPTION) {
                    return;
                }

                save(config);
            }
        };

        return action;
    }

    public JSecurityAction createRemoveAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("remove_text"), ConsoleIconFactory.getSwingIcon("cut.png"), getRemoveTooltip()) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                remove();
            }
        };

        return action;
    }

    public JSecurityAction createClearAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("clear_text"), ConsoleIconFactory.getSwingIcon("paint.png"), ConsoleLocaleFactory.getString("clear_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                clear();
            }
        };

        return action;
    }

    public JSecurityAction createPreviewAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("preview_text"), ConsoleIconFactory.getSwingIcon("ticket.png"), ConsoleLocaleFactory.getString("preview_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                String config = getStrategyProcessor().toConfig(ruleEntity, strategyType, dataBox);
                if (StringUtils.isEmpty(config)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(AbstractReleaseTopology.this), ConsoleLocaleFactory.getString("strategy_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                PreviewPanel previewPanel = PreviewPanel.getInstance();

                String key = getKey();

                previewPanel.setKey(key);
                previewPanel.setConfig(config);

                int selectedOption = JBasicOptionPane.showOptionDialog(HandleManager.getFrame(AbstractReleaseTopology.this), previewPanel, ConsoleLocaleFactory.getString("preview_tooltip"), JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/property.png"), new Object[] { ConsoleLocaleFactory.getString("save_config_text"), ConsoleLocaleFactory.getString("close_preview_text") }, null, true);
                if (selectedOption != 0) {
                    return;
                }

                config = previewPanel.getConfig();
                if (StringUtils.isEmpty(config)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(AbstractReleaseTopology.this), ConsoleLocaleFactory.getString("strategy_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                save(config);
            }
        };

        return action;
    }

    public JSecurityAction createConfigAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("config_text"), ConsoleIconFactory.getSwingIcon("config.png"), ConsoleLocaleFactory.getString("config_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                JBasicOptionPane.showOptionDialog(HandleManager.getFrame(AbstractReleaseTopology.this), ConfigPanel.getInstance(), ConsoleLocaleFactory.getString("config_text"), JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/deploy.png"), new Object[] { SwingLocale.getString("close") }, null, true);
            }
        };

        return action;
    }

    public JSecurityAction createLayoutAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("layout_text"), ConsoleIconFactory.getSwingIcon("layout.png"), ConsoleLocaleFactory.getString("layout_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                executeLayout();
            }
        };

        return action;
    }

    public String getKey() {
        String key = null;
        if (StringUtils.equals(configType, APOLLO)) {
            key = group + "-" + getServiceId();
        } else {
            key = "Data ID=" + getServiceId() + " | Group=" + group;
        }

        return key;
    }

    public String getGroup() {
        return group;
    }

    public void save(String config) {
        String key = getKey();
        String group = getGroup();
        String serviceId = getServiceId();

        LOG.info("Save Config, key={}, config=\n{}", key, config);

        String result = getStrategyProcessor().saveConfig(group, serviceId, config);
        showResult(result);
    }

    public abstract ReleaseType getReleaseType();

    public abstract String getServiceId();

    public abstract String getRemoveTooltip();

    public abstract void create();

    public abstract void remove();

    public abstract void clear();

    public abstract StrategyProcessor getStrategyProcessor();
}