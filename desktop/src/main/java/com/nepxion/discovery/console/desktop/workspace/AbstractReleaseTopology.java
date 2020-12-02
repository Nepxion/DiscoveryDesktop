package com.nepxion.discovery.console.desktop.workspace;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.awt.event.ActionEvent;

import javax.swing.JToolBar;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.common.entity.SubscriptionType;
import com.nepxion.discovery.console.cache.ConsoleCache;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.swing.dialog.JExceptionDialog;
import com.nepxion.discovery.console.desktop.common.util.ButtonUtil;
import com.nepxion.discovery.console.desktop.common.util.DimensionUtil;
import com.nepxion.discovery.console.desktop.workspace.panel.PreviewPanel;
import com.nepxion.discovery.console.desktop.workspace.panel.ResetPanel;
import com.nepxion.discovery.console.desktop.workspace.processor.ReleaseProcessor;
import com.nepxion.discovery.console.desktop.workspace.type.ReleaseType;
import com.nepxion.discovery.console.desktop.workspace.type.TypeLocale;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.ButtonManager;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.optionpane.JBasicOptionPane;

public abstract class AbstractReleaseTopology extends AbstractTopology {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(AbstractReleaseTopology.class);

    public static final String APOLLO = "Apollo";

    protected ResetPanel resetPanel;

    protected ReleaseType releaseType;
    protected SubscriptionType subscriptionType;

    protected String configType;
    protected String group;
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
        toolBar.add(ButtonUtil.createButton(createOpenAction()));
        toolBar.add(ButtonUtil.createButton(createSaveAction()));
        toolBar.add(ButtonUtil.createButton(createResetAction()));
        toolBar.addSeparator();
        toolBar.add(ButtonUtil.createButton(createRemoveAction()));
        toolBar.add(ButtonUtil.createButton(createClearAction()));
        toolBar.addSeparator();
        toolBar.add(ButtonUtil.createButton(createPreviewAction()));
        toolBar.add(ButtonUtil.createButton(createSetAction()));
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
            getReleaseProcessor().fromConfig(ruleEntity, dataBox);
        } catch (Exception e) {
            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(this), e.getMessage(), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);
        }
    }

    public JSecurityAction createCreateAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("create_text"), ConsoleIconFactory.getSwingIcon("theme/tree/plastic/tree_leaf.png"), ConsoleLocaleFactory.getString("create_config_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                create();
            }
        };

        return action;
    }

    public JSecurityAction createOpenAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("open_text"), ConsoleIconFactory.getSwingIcon("theme/tree/plastic/tree_open.png"), ConsoleLocaleFactory.getString("open_config_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                open();
            }
        };

        return action;
    }

    public JSecurityAction createSaveAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("save_text"), ConsoleIconFactory.getSwingIcon("save.png"), ConsoleLocaleFactory.getString("save_config_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                if (!saveValidate()) {
                    return;
                }

                String config = getReleaseProcessor().toConfig(ruleEntity, dataBox);
                if (StringUtils.isEmpty(config)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(AbstractReleaseTopology.this), ConsoleLocaleFactory.getString("config_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

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

    public JSecurityAction createResetAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("reset_text"), ConsoleIconFactory.getSwingIcon("modify.png"), ConsoleLocaleFactory.getString("reset_config_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                if (resetPanel == null) {
                    resetPanel = new ResetPanel();
                    DimensionUtil.addSize(resetPanel, 100, 10);
                }

                int selectedOption = JBasicOptionPane.showOptionDialog(HandleManager.getFrame(AbstractReleaseTopology.this), resetPanel, ConsoleLocaleFactory.getString("reset_config_tooltip") + "【" + TypeLocale.getDescription(releaseType) + "】", JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/net.png"), new Object[] { SwingLocale.getString("confirm"), SwingLocale.getString("cancel") }, null, true);
                if (selectedOption != 0) {
                    return;
                }

                String group = resetPanel.getValidGroup();
                if (StringUtils.isEmpty(group)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(AbstractReleaseTopology.this), ConsoleLocaleFactory.getString("group_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                String gatewayId = resetPanel.getValidGatewayId();
                if (StringUtils.isEmpty(gatewayId)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(AbstractReleaseTopology.this), ConsoleLocaleFactory.getString("service_id_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                save(group, gatewayId, DiscoveryConstant.DEFAULT_XML_RULE);
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
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("clear_text"), ConsoleIconFactory.getSwingIcon("paint.png"), getClearTooltip()) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                clear();
            }
        };

        return action;
    }

    public JSecurityAction createPreviewAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("preview_text"), ConsoleIconFactory.getSwingIcon("ticket.png"), ConsoleLocaleFactory.getString("preview_config_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                if (!saveValidate()) {
                    return;
                }

                String config = getReleaseProcessor().toConfig(ruleEntity, dataBox);
                if (StringUtils.isEmpty(config)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(AbstractReleaseTopology.this), ConsoleLocaleFactory.getString("config_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                PreviewPanel previewPanel = PreviewPanel.getInstance();

                String key = getKey();

                previewPanel.setKey(key);
                previewPanel.setConfig(config);

                int selectedOption = JBasicOptionPane.showOptionDialog(HandleManager.getFrame(AbstractReleaseTopology.this), previewPanel, ConsoleLocaleFactory.getString("preview_config_tooltip"), JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/property.png"), new Object[] { ConsoleLocaleFactory.getString("save_config_text"), ConsoleLocaleFactory.getString("close_preview_text") }, null, true);
                if (selectedOption != 0) {
                    return;
                }

                config = previewPanel.getConfig();
                if (StringUtils.isEmpty(config)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(AbstractReleaseTopology.this), ConsoleLocaleFactory.getString("config_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                save(config);
            }
        };

        return action;
    }

    public String getGroup() {
        return group;
    }

    public void save(String config) {
        String group = getGroup();
        String serviceId = getServiceId();

        save(group, serviceId, config);
    }

    public void save(String group, String serviceId, String config) {
        String key = getKey(group, serviceId);

        LOG.info("Save Config, key={}, config=\n{}", key, config);

        String result = getReleaseProcessor().saveConfig(group, serviceId, config);
        showResult(result);
    }

    public String getKey() {
        String group = getGroup();
        String serviceId = getServiceId();

        return getKey(group, serviceId);
    }

    public String getKey(String group, String serviceId) {
        String key = null;
        if (StringUtils.equals(configType, APOLLO)) {
            key = group + "-" + serviceId;
        } else {
            key = "Data ID=" + serviceId + " | Group=" + group;
        }

        return key;
    }

    public abstract ReleaseType getReleaseType();

    public abstract String getServiceId();

    public abstract String getRemoveTooltip();

    public abstract String getClearTooltip();

    public abstract void create();

    public abstract void open();

    public abstract void remove();

    public abstract void clear();

    public abstract boolean saveValidate();

    public abstract ReleaseProcessor getReleaseProcessor();
}