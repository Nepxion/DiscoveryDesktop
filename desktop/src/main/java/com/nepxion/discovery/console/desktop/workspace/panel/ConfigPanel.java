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
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.util.ListUtil;
import com.nepxion.discovery.console.desktop.workspace.type.ConfigType;
import com.nepxion.discovery.console.desktop.workspace.type.TypeLocale;
import com.nepxion.swing.container.ContainerManager;
import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.framework.dockable.JDockable;
import com.nepxion.swing.framework.dockable.JDockableView;
import com.nepxion.swing.list.JBasicList;
import com.nepxion.swing.query.JQueryHierarchy;
import com.nepxion.swing.scrollpane.JBasicScrollPane;

public class ConfigPanel extends JQueryHierarchy {
    private static final long serialVersionUID = 1L;

    protected static ConfigPanel configPanel;

    public static ConfigPanel getInstance() {
        if (configPanel == null) {
            configPanel = new ConfigPanel();
        }

        return configPanel;
    }

    private ConfigPanel() {
        CachePanel cachePanel = new CachePanel();

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        JBasicList configList = new JBasicList() {
            private static final long serialVersionUID = 1L;

            @Override
            public void executeSelection(int oldSelectedRow, int newSelectedRow) {
                if (newSelectedRow < 0) {
                    return;
                }

                ElementNode configElementNode = (ElementNode) getListData().get(newSelectedRow);
                ConfigItemPanel configItemPanel = (ConfigItemPanel) configElementNode.getUserObject();

                container.removeAll();
                container.add(configItemPanel, BorderLayout.CENTER);

                ContainerManager.update(container);
            }
        };
        configList.setSelectionMode(JBasicList.SINGLE_SELECTION);

        List<ElementNode> configElementNodeList = new ArrayList<ElementNode>();
        for (ConfigType configType : ConfigType.values()) {
            configElementNodeList.add(new ElementNode(TypeLocale.getDescription(configType), null, TypeLocale.getDescription(configType), cachePanel));
        }
        ListUtil.setModel(configList, configElementNodeList, ConsoleIconFactory.getSwingIcon("netbean/stack_16.png"));
        configList.setSelectedIndex(0);

        JPanel configListPanel = new JPanel();
        configListPanel.setLayout(new BorderLayout());
        configListPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        configListPanel.add(new JBasicScrollPane(configList), BorderLayout.CENTER);

        JDockable dockable = (JDockable) getDockableContainer().getContentPane();
        dockable.setDividerLocation(0, 200);

        JDockableView filterView = (JDockableView) dockable.getPaneAt(0);
        filterView.setTitle(ConsoleLocaleFactory.getString("config_list"));
        filterView.setIcon(ConsoleIconFactory.getSwingIcon("netbean/stack_16.png"));
        filterView.setToolTipText(ConsoleLocaleFactory.getString("config_list"));
        filterView.add(configListPanel);

        JDockableView ruleView = (JDockableView) dockable.getPaneAt(1);
        ruleView.setTitle(ConsoleLocaleFactory.getString("config_content"));
        ruleView.setIcon(ConsoleIconFactory.getSwingIcon("component/internal_frame_16.png"));
        ruleView.setToolTipText(ConsoleLocaleFactory.getString("config_content"));
        ruleView.add(container);

        setPreferredSize(new Dimension(800, 600));
    }
}