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
import com.nepxion.discovery.console.controller.ServiceController;
import com.nepxion.discovery.console.desktop.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.topology.BasicTopology;
import com.nepxion.discovery.console.desktop.workspace.panel.PreviewPanel;
import com.nepxion.discovery.console.desktop.workspace.processor.StrategyProcessor;
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
        toolBar.add(new JClassicButton(createInspectorAction()));
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
            configCenterType = ServiceController.getConfigType();
        } catch (Exception ex) {
            JExceptionDialog.traceException(HandleManager.getFrame(AbstractTopology.this), ConsoleLocaleFactory.getString("query_data_failure"), ex);
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
        JSecurityAction action = new JSecurityAction("打开", ConsoleIconFactory.getSwingIcon("theme/tree/plastic/tree_open.png"), "打开或者新增策略") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                open();
            }
        };

        return action;
    }

    private JSecurityAction createSaveAction() {
        JSecurityAction action = new JSecurityAction("保存", ConsoleIconFactory.getSwingIcon("save.png"), "保存策略") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                String xml = getStrategyProcessor().toXml(strategyType, dataBox);
                if (StringUtils.isEmpty(xml)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(AbstractTopology.this), "策略为空", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                save();
            }
        };

        return action;
    }

    private JSecurityAction createClearAction() {
        JSecurityAction action = new JSecurityAction("清空", ConsoleIconFactory.getSwingIcon("paint.png"), "清空策略") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                clear();
            }
        };

        return action;
    }

    private JSecurityAction createPreviewAction() {
        JSecurityAction action = new JSecurityAction("预览", ConsoleIconFactory.getSwingIcon("ticket.png"), "预览策略") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                String xml = getStrategyProcessor().toXml(strategyType, dataBox);
                if (StringUtils.isEmpty(xml)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(AbstractTopology.this), "策略为空", SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                if (previewPanel == null) {
                    previewPanel = new PreviewPanel();
                    previewPanel.setPreferredSize(new Dimension(920, 420));
                }

                String key = null;
                if (StringUtils.equals(configCenterType, APOLLO)) {
                    key = group + "-" + getDataId();
                } else {
                    key = "Data ID=" + getDataId() + " | Group=" + group;
                }
                previewPanel.setKey(key);
                previewPanel.setConfig(xml);

                JBasicOptionPane.showOptionDialog(HandleManager.getFrame(AbstractTopology.this), previewPanel, "策略预览", JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/property.png"), new Object[] { SwingLocale.getString("close") }, null, true);
            }
        };

        return action;
    }

    private JSecurityAction createInspectorAction() {
        JSecurityAction action = new JSecurityAction("侦测", ConsoleIconFactory.getSwingIcon("relation.png"), "侦测链路") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {

            }
        };

        return action;
    }

    private JSecurityAction createLayoutAction() {
        JSecurityAction action = new JSecurityAction("布局", ConsoleIconFactory.getSwingIcon("layout.png"), "布局") {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                toggleLayoutBar();
            }
        };

        return action;
    }

    public abstract String getDataId();

    public abstract void clear();

    public abstract void open();

    public abstract void save();

    public abstract StrategyProcessor getStrategyProcessor();
}