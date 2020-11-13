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
import javax.swing.JToolBar;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.cots.twaver.graph.TGraphBackground;
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
import com.nepxion.swing.textfield.JBasicTextField;

public abstract class AbstractTopology extends BasicTopology {
    private static final long serialVersionUID = 1L;

    public static final String APOLLO = "Apollo";

    protected TGraphBackground background;

    protected PreviewPanel previewPanel;

    protected JBasicTextField layoutTextField = new JBasicTextField();

    protected String group;
    protected ReleaseType releaseType;
    protected StrategyType strategyType;
    protected ConfigType configType;

    protected String configCenterType;

    public AbstractTopology() {
        initializeToolBar();
        initializeTopology();
        initializeListener();
        initializeData();

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void initializeToolBar() {
        JToolBar toolBar = getGraph().getToolbar();
        toolBar.addSeparator();
        toolBar.add(new JClassicButton(createOpenAction()));
        toolBar.add(new JClassicButton(createSaveAction()));
        toolBar.add(new JClassicButton(createClearAction()));
        toolBar.addSeparator();
        toolBar.add(new JClassicButton(createPreviewAction()));
        toolBar.add(new JClassicButton(createInspectAction()));
        toolBar.addSeparator();
        toolBar.add(new JClassicButton(createLayoutAction()));

        ButtonManager.updateUI(toolBar);
    }

    private void initializeTopology() {
        background = graph.getGraphBackground();
        graph.setElementStateOutlineColorGenerator(new Generator() {
            public Object generate(Object object) {
                return null;
            }
        });
    }

    private void initializeData() {
        try {
            configCenterType = ConsoleController.getConfigType();
        } catch (Exception ex) {
            JExceptionDialog.traceException(HandleManager.getFrame(AbstractTopology.this), ConsoleLocaleFactory.getString("operation_failure"), ex);
        }
    }

    private void initializeListener() {
        addHierarchyListener(new DisplayAbilityListener() {
            public void displayAbilityChanged(HierarchyEvent e) {
                showLayoutBar(150, 100, 200, 60);
                toggleLayoutBar();

                removeHierarchyListener(this);
            }
        });
    }

    private JSecurityAction createOpenAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("open_text"), ConsoleIconFactory.getSwingIcon("theme/tree/plastic/tree_open.png"), ConsoleLocaleFactory.getString("open_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                open();
            }
        };

        return action;
    }

    private JSecurityAction createSaveAction() {
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

    private JSecurityAction createClearAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("clear_text"), ConsoleIconFactory.getSwingIcon("paint.png"), ConsoleLocaleFactory.getString("clear_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                clear();
            }
        };

        return action;
    }

    private JSecurityAction createPreviewAction() {
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

                int selectedOption = JBasicOptionPane.showOptionDialog(HandleManager.getFrame(AbstractTopology.this), previewPanel, ConsoleLocaleFactory.getString("preview_tooltip"), JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/property.png"), new Object[] { ConsoleLocaleFactory.getString("save_config"), ConsoleLocaleFactory.getString("close_preview") }, null, true);
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

    private JSecurityAction createInspectAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("inspect_text"), ConsoleIconFactory.getSwingIcon("relation.png"), ConsoleLocaleFactory.getString("inspect_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {

            }
        };

        return action;
    }

    private JSecurityAction createLayoutAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("layout_text"), ConsoleIconFactory.getSwingIcon("layout.png"), ConsoleLocaleFactory.getString("layout_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                toggleLayoutBar();
            }
        };

        return action;
    }

    public String getGroup() {
        return group;
    }

    public abstract String getServiceId();

    public abstract void clear();

    public abstract void open();

    public abstract void save(String config);

    public abstract StrategyProcessor getStrategyProcessor();
}