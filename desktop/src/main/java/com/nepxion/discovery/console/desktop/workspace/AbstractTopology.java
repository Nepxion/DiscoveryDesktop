package com.nepxion.discovery.console.desktop.workspace;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import twaver.Generator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.cots.twaver.graph.TGraphBackground;
import com.nepxion.cots.twaver.graph.TLayoutType;
import com.nepxion.discovery.console.controller.ConsoleController;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.workspace.panel.PreviewPanel;
import com.nepxion.discovery.console.desktop.workspace.processor.StrategyProcessor;
import com.nepxion.discovery.console.desktop.workspace.topology.BasicTopology;
import com.nepxion.discovery.console.desktop.workspace.type.ConfigType;
import com.nepxion.discovery.console.desktop.workspace.type.ReleaseType;
import com.nepxion.discovery.console.desktop.workspace.type.StrategyType;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.ButtonManager;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.dialog.JExceptionDialog;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.listener.DisplayAbilityListener;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.optionpane.JBasicOptionPane;

public abstract class AbstractTopology extends BasicTopology {
    private static final long serialVersionUID = 1L;

    public static final String APOLLO = "Apollo";

    protected TGraphBackground background;

    protected PreviewPanel previewPanel;

    protected JPanel operationBar = new JPanel();

    protected ReleaseType releaseType;
    protected StrategyType strategyType;
    protected ConfigType configType;

    protected String group;

    protected String configCenterType;

    public AbstractTopology(ReleaseType releaseType) {
        this.releaseType = releaseType;

        initializeToolBar();
        initializeTopology();
        initializeOperationBar();
        // initializeListener();
        initializeData();

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    public void initializeToolBar() {
        JToolBar toolBar = getGraph().getToolbar();
        toolBar.addSeparator();
        toolBar.add(new JClassicButton(createCreateAction()));
        toolBar.add(new JClassicButton(createSaveAction()));
        toolBar.addSeparator();
        toolBar.add(new JClassicButton(createRemoveAction()));
        toolBar.add(new JClassicButton(createClearAction()));
        toolBar.addSeparator();
        toolBar.add(new JClassicButton(createPreviewAction()));
        toolBar.add(new JClassicButton(createInspectAction()));
        toolBar.addSeparator();
        toolBar.add(new JClassicButton(createLayoutAction()));

        ButtonManager.updateUI(toolBar);
    }

    public void initializeTopology() {
        background = graph.getGraphBackground();
        graph.setElementStateOutlineColorGenerator(new Generator() {
            public Object generate(Object object) {
                return null;
            }
        });
    }

    public void initializeData() {
        try {
            configCenterType = ConsoleController.getConfigType();
        } catch (Exception ex) {
            JExceptionDialog.traceException(HandleManager.getFrame(AbstractTopology.this), ConsoleLocaleFactory.getString("operation_failure"), ex);
        }
    }

    public void initializeListener() {
        addHierarchyListener(new DisplayAbilityListener() {
            public void displayAbilityChanged(HierarchyEvent e) {
                showLayoutBar(150, 100, 200, 60);
                toggleLayoutBar();

                removeHierarchyListener(this);
            }
        });
    }

    public abstract void initializeOperationBar();

    public JPanel getOperationBar() {
        return operationBar;
    }

    public void executeLayout() {
        layouter.doLayout(TLayoutType.HIERARCHIC_LAYOUT_TYPE, 150, 100, 200, 60);
    }

    public JSecurityAction createCreateAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("create_text"), ConsoleIconFactory.getSwingIcon("theme/tree/plastic/tree_leaf.png"), ConsoleLocaleFactory.getString("create_tooltip")) {
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
                String config = getStrategyProcessor().toConfig(strategyType, dataBox);
                if (StringUtils.isEmpty(config)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(AbstractTopology.this), ConsoleLocaleFactory.getString("strategy_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

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
                String config = getStrategyProcessor().toConfig(strategyType, dataBox);
                if (StringUtils.isEmpty(config)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(AbstractTopology.this), ConsoleLocaleFactory.getString("strategy_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                if (previewPanel == null) {
                    previewPanel = new PreviewPanel();
                    previewPanel.setPreferredSize(new Dimension(920, 420));
                }

                String key = null;
                if (StringUtils.equals(configCenterType, APOLLO)) {
                    key = group + "-" + getServiceId();
                } else {
                    key = "Data ID=" + getServiceId() + " | Group=" + group;
                }
                previewPanel.setKey(key);
                previewPanel.setConfig(config);

                int selectedOption = JBasicOptionPane.showOptionDialog(HandleManager.getFrame(AbstractTopology.this), previewPanel, ConsoleLocaleFactory.getString("preview_tooltip"), JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/property.png"), new Object[] { ConsoleLocaleFactory.getString("save_config_text"), ConsoleLocaleFactory.getString("close_preview_text") }, null, true);
                if (selectedOption != 0) {
                    return;
                }

                config = previewPanel.getConfig();
                if (StringUtils.isEmpty(config)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(AbstractTopology.this), ConsoleLocaleFactory.getString("strategy_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                save(config);
            }
        };

        return action;
    }

    public JSecurityAction createInspectAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("inspect_text"), ConsoleIconFactory.getSwingIcon("relation.png"), ConsoleLocaleFactory.getString("inspect_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {

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

    public String getGroup() {
        return group;
    }

    public abstract String getServiceId();

    public abstract String getRemoveTooltip();

    public abstract void create();

    public abstract void save(String config);

    public abstract void remove();

    public abstract void clear();

    public abstract StrategyProcessor getStrategyProcessor();
}