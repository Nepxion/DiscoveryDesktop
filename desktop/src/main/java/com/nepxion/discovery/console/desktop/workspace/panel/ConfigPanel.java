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
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;

import com.nepxion.discovery.console.cache.ConsoleCache;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.util.ListUtil;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.checkbox.JBasicCheckBox;
import com.nepxion.swing.icon.IconFactory;
import com.nepxion.swing.list.JBasicList;
import com.nepxion.swing.scrollpane.JBasicScrollPane;
import com.nepxion.swing.tabbedpane.JBasicTabbedPane;

public class ConfigPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    protected static ConfigPanel configPanel;

    protected CachePanel groupCachePanel;
    protected CachePanel serviceCachePanel;

    public static ConfigPanel getInstance() {
        if (configPanel == null) {
            configPanel = new ConfigPanel();
        }

        return configPanel;
    }

    private ConfigPanel() {
        groupCachePanel = new CachePanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<String> getInitialValue() {
                return ConsoleCache.getGroups();
            }

            @Override
            public List<String> getRefreshValue() {
                return ConsoleCache.refreshGroups();
            }
        };

        serviceCachePanel = new CachePanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<String> getInitialValue() {
                return ConsoleCache.getServices();
            }

            @Override
            public List<String> getRefreshValue() {
                return ConsoleCache.refreshServices();
            }
        };

        JBasicCheckBox enableCacheCheckBox = new JBasicCheckBox(ConsoleLocaleFactory.getString("cache_enable"), ConsoleCache.isCacheEnabled());
        enableCacheCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                ConsoleCache.setCacheEnabled(enableCacheCheckBox.isSelected());
            }
        });

        JPanel cacheBottonBar = new JPanel();
        cacheBottonBar.setLayout(new BorderLayout());
        cacheBottonBar.add(new JClassicButton(createRefreshCacheAction()), BorderLayout.WEST);
        cacheBottonBar.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
        cacheBottonBar.add(enableCacheCheckBox, BorderLayout.EAST);

        JBasicTabbedPane configTabbedPane = new JBasicTabbedPane();
        configTabbedPane.addTab(ConsoleLocaleFactory.getString("group_list_cache"), groupCachePanel, ConsoleLocaleFactory.getString("group_list_cache"));
        configTabbedPane.addTab(ConsoleLocaleFactory.getString("service_list_cache"), serviceCachePanel, ConsoleLocaleFactory.getString("service_list_cache"));

        setLayout(new BorderLayout(0, 5));
        setPreferredSize(new Dimension(500, 500));
        add(configTabbedPane, BorderLayout.CENTER);
        add(cacheBottonBar, BorderLayout.SOUTH);
    }

    public abstract class CachePanel extends JPanel {
        private static final long serialVersionUID = 1L;

        protected JBasicList cacheList;

        public CachePanel() {
            cacheList = new JBasicList();

            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
            add(new JBasicScrollPane(cacheList), BorderLayout.CENTER);

            setInitialModel();
        }

        public JBasicList getCacheList() {
            return cacheList;
        }

        public void setInitialModel() {
            List<String> initialValue = getInitialValue();

            setModel(initialValue);
        }

        public void setRefreshModel() {
            List<String> refreshValue = getRefreshValue();

            setModel(refreshValue);
        }

        public void setModel(List<String> value) {
            ListUtil.setSortableModel(cacheList, value, IconFactory.getSwingIcon("component/view.png"));
        }

        public abstract List<String> getInitialValue();

        public abstract List<String> getRefreshValue();
    }

    public JSecurityAction createRefreshCacheAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("refresh_cache"), ConsoleIconFactory.getSwingIcon("netbean/rotate_16.png"), ConsoleLocaleFactory.getString("refresh_cache")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                groupCachePanel.setRefreshModel();
                serviceCachePanel.setRefreshModel();
            }
        };

        return action;
    }
}