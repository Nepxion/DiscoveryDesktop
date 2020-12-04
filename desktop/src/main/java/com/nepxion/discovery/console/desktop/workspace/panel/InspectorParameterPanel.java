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

public class InspectorParameterPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    protected ParameterBar parameterBar;

    public InspectorParameterPanel() {
        parameterBar = new ParameterBar();

        setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 10));
        add(parameterBar);
    }

    public Map<String, String> getParameterMap() {
        return parameterBar.getParameterMap();
    }

    public class ParameterBar extends JPanel {
        private static final long serialVersionUID = 1L;

        protected int initialParameterItemCount = 1;
        protected List<ParameterItem> parameterItems = new ArrayList<ParameterItem>();

        protected JPanel parameterItemBar;

        public ParameterBar() {
            parameterItemBar = new JPanel();

            double[][] size = {
                    { TableLayout.FILL },
                    { TableLayout.PREFERRED }
            };

            TableLayout tableLayout = new TableLayout(size);
            tableLayout.setHGap(0);
            tableLayout.setVGap(5);

            JPanel parameterBar = new JPanel();
            parameterBar.setLayout(tableLayout);

            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            add(parameterItemBar, BorderLayout.CENTER);
            add(parameterBar, BorderLayout.SOUTH);

            for (int i = 0; i < initialParameterItemCount; i++) {
                parameterItems.add(new ParameterItem(UUID.randomUUID().toString()));
            }
            layoutParameterItems();
        }

        public void layoutParameterItems() {
            parameterItemBar.removeAll();

            double[] row = new double[parameterItems.size()];
            for (int i = 0; i < row.length; i++) {
                row[i] = TableLayout.PREFERRED;
            }
            double[][] size = {
                    { TableLayout.PREFERRED, 136, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.PREFERRED },
                    row
            };

            TableLayout tableLayout = new TableLayout(size);
            tableLayout.setHGap(0);
            tableLayout.setVGap(5);

            int index = 0;

            parameterItemBar.setLayout(tableLayout);
            for (ParameterItem parameterItem : parameterItems) {
                parameterItemBar.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("key-value")), 5), "0, " + index);
                parameterItemBar.add(parameterItem.keyTextField, "1, " + index);
                parameterItemBar.add(new JBasicLabel("="), "2, " + index);
                parameterItemBar.add(parameterItem.valueTextField, "3, " + index);
                parameterItemBar.add(parameterItem.addButton, "4, " + index);
                parameterItemBar.add(parameterItem.removeButton, "5, " + index);

                index++;
            }

            ContainerManager.update(parameterItemBar);
        }

        public Map<String, String> getParameterMap() {
            Map<String, String> parameterMap = new LinkedHashMap<String, String>();

            for (ParameterItem parameterItem : parameterItems) {
                String key = parameterItem.keyTextField.getText().trim();
                String value = parameterItem.valueTextField.getText().trim();
                if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
                    parameterMap.put(key, value);
                }
            }

            return parameterMap;
        }

        public ParameterItem getParameterItem(String uuid) {
            for (ParameterItem parameterItem : parameterItems) {
                if (StringUtils.equals(parameterItem.uuid, uuid)) {
                    return parameterItem;
                }
            }

            return null;
        }

        public class ParameterItem {
            protected JBasicTextField keyTextField = new JBasicTextField();
            protected JBasicTextField valueTextField = new JBasicTextField();
            protected JClassicButton addButton = new JClassicButton(createAddParameterItemAction());
            protected JClassicButton removeButton = new JClassicButton(createRemoveParameterItemAction());

            protected String uuid;

            public ParameterItem(String uuid) {
                this.uuid = uuid;

                addButton.setName(uuid);
                removeButton.setName(uuid);

                DimensionUtil.setWidth(addButton, 30);
                DimensionUtil.setWidth(removeButton, 30);
            }
        }

        public JSecurityAction createAddParameterItemAction() {
            JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("add.png"), ConsoleLocaleFactory.getString("add_parameter_item")) {
                private static final long serialVersionUID = 1L;

                public void execute(ActionEvent e) {
                    JClassicButton addButton = (JClassicButton) e.getSource();
                    String uuid = addButton.getName();

                    ParameterItem parameterItem = getParameterItem(uuid);
                    if (parameterItem == null) {
                        return;
                    }

                    parameterItems.add(parameterItems.indexOf(parameterItem) + 1, new ParameterItem(UUID.randomUUID().toString()));
                    layoutParameterItems();
                }
            };

            return action;
        }

        public JSecurityAction createRemoveParameterItemAction() {
            JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("delete.png"), ConsoleLocaleFactory.getString("remove_parameter_item")) {
                private static final long serialVersionUID = 1L;

                public void execute(ActionEvent e) {
                    if (parameterItems.size() < 2) {
                        JBasicOptionPane.showMessageDialog(HandleManager.getFrame(InspectorParameterPanel.this), ConsoleLocaleFactory.getString("parameter_one_at_least"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                        return;
                    }

                    JClassicButton removeButton = (JClassicButton) e.getSource();
                    String uuid = removeButton.getName();

                    ParameterItem parameterItem = getParameterItem(uuid);
                    if (parameterItem == null) {
                        return;
                    }

                    parameterItems.remove(parameterItem);
                    layoutParameterItems();
                }
            };

            return action;
        }
    }
}