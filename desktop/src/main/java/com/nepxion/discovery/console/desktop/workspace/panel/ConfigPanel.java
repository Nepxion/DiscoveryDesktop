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
import javax.swing.JPanel;

import com.nepxion.discovery.console.cache.ConsoleCache;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.util.ListUtil;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.checkbox.JBasicCheckBox;
import com.nepxion.swing.icon.IconFactory;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.list.JBasicList;
import com.nepxion.swing.scrollpane.JBasicScrollPane;
import com.nepxion.swing.shrinkbar.JShrinkShortcut;
import com.nepxion.swing.tabbedpane.JBasicTabbedPane;

public class ConfigPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    protected static ConfigPanel configPanel;

    public static ConfigPanel getInstance() {
        if (configPanel == null) {
            configPanel = new ConfigPanel();
        }

        return configPanel;
    }

    private ConfigPanel() {
        JBasicTabbedPane configTabbedPane = new JBasicTabbedPane();
        configTabbedPane.addTab(ConsoleLocaleFactory.getString("cache_config"), new CachePanel(), ConsoleLocaleFactory.getString("cache_config"));

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(650, 550));
        add(configTabbedPane, BorderLayout.CENTER);
    }

    public class CachePanel extends JPanel {
        private static final long serialVersionUID = 1L;

        protected JBasicList groupListCacheList;
        protected JBasicList serviceListCacheList;

        public CachePanel() {
            JBasicCheckBox enableCacheCheckBox = new JBasicCheckBox(ConsoleLocaleFactory.getString("cache_enable"), ConsoleCache.isCacheEnabled());
            enableCacheCheckBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    ConsoleCache.setCacheEnabled(enableCacheCheckBox.isSelected());
                }
            });

            JShrinkShortcut groupListCacheShrinkShortcut = new JShrinkShortcut();
            groupListCacheShrinkShortcut.setTitle(ConsoleLocaleFactory.getString("group_list_cache"));
            groupListCacheShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
            groupListCacheShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString("group_list_cache"));

            JShrinkShortcut serviceListCacheShrinkShortcut = new JShrinkShortcut();
            serviceListCacheShrinkShortcut.setTitle(ConsoleLocaleFactory.getString("service_list_cache"));
            serviceListCacheShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
            serviceListCacheShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString("service_list_cache"));

            groupListCacheList = new JBasicList();
            serviceListCacheList = new JBasicList();

            JPanel groupListCacheBottonBar = new JPanel();
            groupListCacheBottonBar.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.LEFT, 0));
            groupListCacheBottonBar.add(new JClassicButton(createRefreshGroupListCacheAction()));

            JPanel serviceListCacheButtonBar = new JPanel();
            serviceListCacheButtonBar.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.LEFT, 0));
            serviceListCacheButtonBar.add(new JClassicButton(createRefreshServiceListCacheAction()));

            double[][] size = {
                    { TableLayout.FILL, TableLayout.FILL },
                    { TableLayout.PREFERRED, 5, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.PREFERRED }
            };

            TableLayout tableLayout = new TableLayout(size);
            tableLayout.setHGap(5);
            tableLayout.setVGap(5);

            setLayout(tableLayout);
            setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            add(enableCacheCheckBox, "0, 0, 1, 0");
            add(groupListCacheShrinkShortcut, "0, 2");
            add(serviceListCacheShrinkShortcut, "1, 2");
            add(new JBasicScrollPane(groupListCacheList), "0, 3");
            add(new JBasicScrollPane(serviceListCacheList), "1, 3");
            add(groupListCacheBottonBar, "0, 4");
            add(serviceListCacheButtonBar, "1, 4");

            List<String> groups = ConsoleCache.getGroups();
            ListUtil.setSortableModel(groupListCacheList, groups, IconFactory.getSwingIcon("component/label_multi_16.png"));

            List<String> services = ConsoleCache.getServices();
            ListUtil.setSortableModel(serviceListCacheList, services, IconFactory.getSwingIcon("component/label_16.png"));
        }

        public JSecurityAction createRefreshGroupListCacheAction() {
            JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("refresh_cache"), ConsoleIconFactory.getSwingIcon("netbean/rotate_16.png"), ConsoleLocaleFactory.getString("refresh_cache")) {
                private static final long serialVersionUID = 1L;

                public void execute(ActionEvent e) {
                    List<String> groups = ConsoleCache.refreshGroups();
                    ListUtil.setSortableModel(groupListCacheList, groups, IconFactory.getSwingIcon("component/label_multi_16.png"));
                }
            };

            return action;
        }

        public JSecurityAction createRefreshServiceListCacheAction() {
            JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("refresh_cache"), ConsoleIconFactory.getSwingIcon("netbean/rotate_16.png"), ConsoleLocaleFactory.getString("refresh_cache")) {
                private static final long serialVersionUID = 1L;

                public void execute(ActionEvent e) {
                    List<String> services = ConsoleCache.refreshServices();
                    ListUtil.setSortableModel(serviceListCacheList, services, IconFactory.getSwingIcon("component/label_16.png"));
                }
            };

            return action;
        }
    }
}