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
import com.nepxion.discovery.console.desktop.workspace.type.SetType;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.checkbox.JBasicCheckBox;
import com.nepxion.swing.icon.IconFactory;
import com.nepxion.swing.list.JBasicList;
import com.nepxion.swing.scrollpane.JBasicScrollPane;
import com.nepxion.swing.tabbedpane.JBasicTabbedPane;

public class CacheSetPanel extends SetPanel {
    private static final long serialVersionUID = 1L;

    protected CacheListPanel groupCacheListPanel;
    protected CacheListPanel gatewayCacheListPanel;
    protected CacheListPanel serviceCacheListPanel;

    public CacheSetPanel() {
        super(SetType.CACHE);

        groupCacheListPanel = new CacheListPanel() {
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

        gatewayCacheListPanel = new CacheListPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<String> getInitialValue() {
                return ConsoleCache.getGateways();
            }

            @Override
            public List<String> getRefreshValue() {
                return ConsoleCache.refreshGateways();
            }
        };

        serviceCacheListPanel = new CacheListPanel() {
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

        JPanel cacheToolBar = new JPanel();
        cacheToolBar.setLayout(new BorderLayout());
        cacheToolBar.add(new JClassicButton(createRefreshCacheAction()), BorderLayout.WEST);
        cacheToolBar.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
        cacheToolBar.add(enableCacheCheckBox, BorderLayout.EAST);

        JBasicTabbedPane cacheTabbedPane = new JBasicTabbedPane();
        cacheTabbedPane.addTab(ConsoleLocaleFactory.getString("group_list_cache"), groupCacheListPanel, ConsoleLocaleFactory.getString("group_list_cache"));
        cacheTabbedPane.addTab(ConsoleLocaleFactory.getString("gateway_list_cache"), gatewayCacheListPanel, ConsoleLocaleFactory.getString("gateway_list_cache"));
        cacheTabbedPane.addTab(ConsoleLocaleFactory.getString("service_list_cache"), serviceCacheListPanel, ConsoleLocaleFactory.getString("service_list_cache"));

        setLayout(new BorderLayout(0, 5));
        setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        add(cacheTabbedPane, BorderLayout.CENTER);
        add(cacheToolBar, BorderLayout.SOUTH);
    }

    public abstract class CacheListPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        protected JBasicList cacheList;

        public CacheListPanel() {
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
                groupCacheListPanel.setRefreshModel();
                gatewayCacheListPanel.setRefreshModel();
                serviceCacheListPanel.setRefreshModel();
            }
        };

        return action;
    }
}