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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.util.DimensionUtil;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.container.ContainerManager;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.optionpane.JBasicOptionPane;
import com.nepxion.swing.textfield.JBasicTextField;

public class InspectorKVPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    protected KVBar kvBar;

    public InspectorKVPanel() {
        kvBar = new KVBar();

        setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 10));
        add(kvBar);
    }

    public Map<String, String> getMap() {
        return kvBar.getMap();
    }

    public class KVBar extends JPanel {
        private static final long serialVersionUID = 1L;

        protected int initialKVItemCount = 1;
        protected List<KVItem> kvItems = new ArrayList<KVItem>();

        protected JPanel kvItemBar;

        public KVBar() {
            kvItemBar = new JPanel();

            double[][] size = {
                    { TableLayout.FILL },
                    { TableLayout.PREFERRED }
            };

            TableLayout tableLayout = new TableLayout(size);
            tableLayout.setHGap(0);
            tableLayout.setVGap(5);

            JPanel kvBar = new JPanel();
            kvBar.setLayout(tableLayout);

            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            add(kvItemBar, BorderLayout.CENTER);
            add(kvBar, BorderLayout.SOUTH);

            for (int i = 0; i < initialKVItemCount; i++) {
                kvItems.add(new KVItem(UUID.randomUUID().toString()));
            }
            layoutKVItems();
        }

        public void layoutKVItems() {
            kvItemBar.removeAll();

            double[] row = new double[kvItems.size()];
            for (int i = 0; i < row.length; i++) {
                row[i] = TableLayout.PREFERRED;
            }
            double[][] size = {
                    { TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.PREFERRED },
                    row
            };

            TableLayout tableLayout = new TableLayout(size);
            tableLayout.setHGap(0);
            tableLayout.setVGap(5);

            int index = 0;

            kvItemBar.setLayout(tableLayout);
            for (KVItem kvItem : kvItems) {
                kvItemBar.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("kv")), 5), "0, " + index);
                kvItemBar.add(kvItem.keyTextField, "1, " + index);
                kvItemBar.add(new JBasicLabel("="), "2, " + index);
                kvItemBar.add(kvItem.valueTextField, "3, " + index);
                kvItemBar.add(kvItem.addButton, "4, " + index);
                kvItemBar.add(kvItem.removeButton, "5, " + index);

                index++;
            }

            ContainerManager.update(kvItemBar);
        }

        public Map<String, String> getMap() {
            Map<String, String> map = new LinkedHashMap<String, String>();

            for (KVItem kvItem : kvItems) {
                String key = kvItem.keyTextField.getText().trim();
                String value = kvItem.valueTextField.getText().trim();
                if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
                    map.put(key, value);
                }
            }

            return map;
        }

        public KVItem getKVItem(String uuid) {
            for (KVItem kvItem : kvItems) {
                if (StringUtils.equals(kvItem.uuid, uuid)) {
                    return kvItem;
                }
            }

            return null;
        }

        public class KVItem {
            protected JBasicTextField keyTextField = new JBasicTextField();
            protected JBasicTextField valueTextField = new JBasicTextField();
            protected JClassicButton addButton = new JClassicButton(createAddKVItemAction());
            protected JClassicButton removeButton = new JClassicButton(createRemoveKVItemAction());

            protected String uuid;

            public KVItem(String uuid) {
                this.uuid = uuid;

                // DimensionUtil.setWidth(keyTextField, 136);

                addButton.setName(uuid);
                removeButton.setName(uuid);

                DimensionUtil.setWidth(addButton, 30);
                DimensionUtil.setWidth(removeButton, 30);
            }
        }

        public JSecurityAction createAddKVItemAction() {
            JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("add.png"), ConsoleLocaleFactory.getString("add_kv_item")) {
                private static final long serialVersionUID = 1L;

                public void execute(ActionEvent e) {
                    JClassicButton addButton = (JClassicButton) e.getSource();
                    String uuid = addButton.getName();

                    KVItem kvItem = getKVItem(uuid);
                    if (kvItem == null) {
                        return;
                    }

                    kvItems.add(kvItems.indexOf(kvItem) + 1, new KVItem(UUID.randomUUID().toString()));
                    layoutKVItems();
                }
            };

            return action;
        }

        public JSecurityAction createRemoveKVItemAction() {
            JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("delete.png"), ConsoleLocaleFactory.getString("remove_kv_item")) {
                private static final long serialVersionUID = 1L;

                public void execute(ActionEvent e) {
                    if (kvItems.size() < 2) {
                        JBasicOptionPane.showMessageDialog(HandleManager.getFrame(InspectorKVPanel.this), ConsoleLocaleFactory.getString("kv_one_at_least"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                        return;
                    }

                    JClassicButton removeButton = (JClassicButton) e.getSource();
                    String uuid = removeButton.getName();

                    KVItem kvItem = getKVItem(uuid);
                    if (kvItem == null) {
                        return;
                    }

                    kvItems.remove(kvItem);
                    layoutKVItems();
                }
            };

            return action;
        }
    }
}