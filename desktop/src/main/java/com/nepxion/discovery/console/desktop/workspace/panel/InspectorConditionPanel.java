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
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.discovery.console.cache.ConsoleCache;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.swing.dialog.JExceptionDialog;
import com.nepxion.discovery.console.desktop.common.util.ComboBoxUtil;
import com.nepxion.discovery.console.desktop.common.util.DimensionUtil;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.container.ContainerManager;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.optionpane.JBasicOptionPane;

public class InspectorConditionPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    protected ConditionBar conditionBar;

    protected List<String> serviceIds;

    public InspectorConditionPanel() {
        conditionBar = new ConditionBar();

        setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 10));
        add(conditionBar);

        setServiceIds();
    }

    public void setServiceIds() {
        serviceIds = getServiceIds();

        conditionBar.setServiceIds();
    }

    public List<String> getServiceIds() {
        try {
            return ConsoleCache.getRealServices();
        } catch (Exception e) {
            JExceptionDialog.traceException(HandleManager.getFrame(this), ConsoleLocaleFactory.getString("operation_failure"), e);
        }

        return null;
    }

    public String getCondition() {
        //return conditionBar.getCondition();

        return "";
    }

    public class ConditionBar extends JPanel {
        private static final long serialVersionUID = 1L;

        protected int initialConditionItemCount = 1;
        protected List<ConditionItem> conditionItems = new ArrayList<ConditionItem>();

        protected JPanel conditionItemBar;

        public ConditionBar() {
            conditionItemBar = new JPanel();

            double[][] size = {
                    { TableLayout.FILL },
                    { TableLayout.PREFERRED }
            };

            TableLayout tableLayout = new TableLayout(size);
            tableLayout.setHGap(0);
            tableLayout.setVGap(5);

            JPanel conditionBar = new JPanel();
            conditionBar.setLayout(tableLayout);

            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            add(conditionItemBar, BorderLayout.CENTER);
            add(conditionBar, BorderLayout.SOUTH);

            for (int i = 0; i < initialConditionItemCount; i++) {
                conditionItems.add(new ConditionItem(UUID.randomUUID().toString()));
            }
            layoutConditionItems();
        }

        public void layoutConditionItems() {
            conditionItemBar.removeAll();

            double[] row = new double[conditionItems.size()];
            for (int i = 0; i < row.length; i++) {
                row[i] = TableLayout.PREFERRED;
            }
            double[][] size = {
                    { TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.PREFERRED },
                    row
            };

            TableLayout tableLayout = new TableLayout(size);
            tableLayout.setHGap(0);
            tableLayout.setVGap(5);

            int index = 0;

            conditionItemBar.setLayout(tableLayout);
            for (ConditionItem conditionItem : conditionItems) {
                conditionItemBar.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("service")), 5), "0, " + index);
                conditionItemBar.add(conditionItem.serviceIdComboBox, "1, " + index);
                conditionItemBar.add(conditionItem.addButton, "2, " + index);
                conditionItemBar.add(conditionItem.removeButton, "3, " + index);

                index++;
            }

            ContainerManager.update(conditionItemBar);
        }

        public ConditionItem getConditionItem(String uuid) {
            for (ConditionItem conditionItem : conditionItems) {
                if (StringUtils.equals(conditionItem.uuid, uuid)) {
                    return conditionItem;
                }
            }

            return null;
        }

        public void setServiceIds() {
            if (serviceIds == null) {
                return;
            }

            for (ConditionItem conditionItem : conditionItems) {
                conditionItem.setServiceIdComboBoxModel();
            }
        }

        public class ConditionItem {
            protected JBasicComboBox serviceIdComboBox = new JBasicComboBox();
            protected JClassicButton addButton = new JClassicButton(createAddConditionItemAction());
            protected JClassicButton removeButton = new JClassicButton(createRemoveConditionItemAction());

            protected String uuid;

            public ConditionItem(String uuid) {
                this.uuid = uuid;

                serviceIdComboBox.setEditable(true);
                ComboBoxUtil.installlAutoCompletion(serviceIdComboBox);

                setServiceIdComboBoxModel();

                addButton.setName(uuid);
                removeButton.setName(uuid);

                DimensionUtil.setWidth(addButton, 30);
                DimensionUtil.setWidth(removeButton, 30);
            }

            public void setServiceIdComboBoxModel() {
                if (serviceIds != null) {
                    ComboBoxUtil.setSortableModel(serviceIdComboBox, serviceIds);
                }
            }
        }

        public JSecurityAction createAddConditionItemAction() {
            JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("add.png"), ConsoleLocaleFactory.getString("add_service_item")) {
                private static final long serialVersionUID = 1L;

                public void execute(ActionEvent e) {
                    JClassicButton addButton = (JClassicButton) e.getSource();
                    String uuid = addButton.getName();

                    ConditionItem conditionItem = getConditionItem(uuid);
                    if (conditionItem == null) {
                        return;
                    }

                    conditionItems.add(conditionItems.indexOf(conditionItem) + 1, new ConditionItem(UUID.randomUUID().toString()));
                    layoutConditionItems();
                }
            };

            return action;
        }

        public JSecurityAction createRemoveConditionItemAction() {
            JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("delete.png"), ConsoleLocaleFactory.getString("remove_service_item")) {
                private static final long serialVersionUID = 1L;

                public void execute(ActionEvent e) {
                    if (conditionItems.size() < 2) {
                        JBasicOptionPane.showMessageDialog(HandleManager.getFrame(InspectorConditionPanel.this), ConsoleLocaleFactory.getString("service_one_at_least"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                        return;
                    }

                    JClassicButton removeButton = (JClassicButton) e.getSource();
                    String uuid = removeButton.getName();

                    ConditionItem conditionItem = getConditionItem(uuid);
                    if (conditionItem == null) {
                        return;
                    }

                    conditionItems.remove(conditionItem);
                    layoutConditionItems();
                }
            };

            return action;
        }
    }
}