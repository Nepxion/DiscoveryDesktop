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

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.util.DimensionUtil;
import com.nepxion.discovery.console.desktop.workspace.type.NodeType;
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
import com.nepxion.swing.shrinkbar.JShrinkShortcut;
import com.nepxion.swing.textfield.JBasicTextField;

public class BlueGreenConditionPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    protected ConditionBar blueConditionBar;
    protected ConditionBar greenConditionBar;

    public BlueGreenConditionPanel() {
        blueConditionBar = new ConditionBar(NodeType.BLUE);
        greenConditionBar = new ConditionBar(NodeType.GREEN);

        setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 10));
        add(blueConditionBar);
        add(greenConditionBar);
    }

    public void setGreenConditionBarEnabled(boolean enabled) {
        setComponentEnabled(greenConditionBar, enabled);
    }

    public void setComponentEnabled(JComponent component, boolean enabled) {
        if (component.getComponentCount() > 0) {
            for (int i = 0; i < component.getComponentCount(); i++) {
                if (component.getComponent(i) instanceof JComponent) {
                    JComponent childComponent = (JComponent) component.getComponent(i);
                    childComponent.setEnabled(enabled);

                    setComponentEnabled(childComponent, enabled);
                }
            }
        }
    }

    public String getBlueCondition() {
        return blueConditionBar.getResult();
    }

    public String getGreenCondition() {
        return greenConditionBar.getResult();
    }

    public class ConditionBar extends JPanel {
        private static final long serialVersionUID = 1L;

        protected int initialConditionItemCount = 1;
        protected List<ConditionItem> conditionItems = new ArrayList<ConditionItem>();

        protected JPanel conditionItemBar;
        protected JBasicTextField resultTextField;

        public ConditionBar(NodeType nodeType) {
            JShrinkShortcut shrinkShortcut = new JShrinkShortcut();
            shrinkShortcut.setTitle(nodeType.getDescription());
            shrinkShortcut.setIcon(nodeType == NodeType.BLUE ? ConsoleIconFactory.getSwingIcon("circle_blue.png") : ConsoleIconFactory.getSwingIcon("circle_green.png"));
            shrinkShortcut.setToolTipText(nodeType.getDescription());

            conditionItemBar = new JPanel();
            resultTextField = new JBasicTextField();

            JPanel resultButtonBar = new JPanel();
            resultButtonBar.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 0));
            resultButtonBar.add(DimensionUtil.setWidth(new JClassicButton(createAggregateConditionAction(conditionItems)), 30));
            resultButtonBar.add(DimensionUtil.setWidth(new JClassicButton(createValidateConditionAction()), 30));

            JPanel resultBar = new JPanel();
            resultBar.setLayout(new BorderLayout(0, 5));
            resultBar.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("result")), 5), BorderLayout.WEST);
            resultBar.add(resultTextField, BorderLayout.CENTER);
            resultBar.add(resultButtonBar, BorderLayout.EAST);

            setLayout(new BorderLayout());
            add(shrinkShortcut, BorderLayout.NORTH);
            add(conditionItemBar, BorderLayout.CENTER);
            add(resultBar, BorderLayout.SOUTH);

            for (int i = 0; i < initialConditionItemCount; i++) {
                conditionItems.add(new ConditionItem(UUID.randomUUID().toString()));
            }
            layoutConditionItems();
        }

        public void layoutConditionItems() {
            conditionItemBar.removeAll();

            double[] row = new double[conditionItems.size() + 1];
            for (int i = 0; i < row.length; i++) {
                row[i] = TableLayout.PREFERRED;
            }
            double[][] size = {
                    { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED },
                    row
            };

            TableLayout tableLayout = new TableLayout(size);
            tableLayout.setHGap(0);
            tableLayout.setVGap(5);

            int index = 0;

            conditionItemBar.setLayout(tableLayout);
            conditionItemBar.add(new JBasicLabel(ConsoleLocaleFactory.getString("parameter")), "0, " + index);
            conditionItemBar.add(new JBasicLabel(ConsoleLocaleFactory.getString("arithmetic")), "1, " + index);
            conditionItemBar.add(new JBasicLabel(ConsoleLocaleFactory.getString("value")), "2, " + index);
            conditionItemBar.add(new JBasicLabel(ConsoleLocaleFactory.getString("relational")), "3, " + index + ", 5, " + index);

            index++;

            for (ConditionItem conditionItem : conditionItems) {
                conditionItemBar.add(conditionItem.parameterTextField, "0, " + index);
                conditionItemBar.add(conditionItem.arithmeticComboBox, "1, " + index);
                conditionItemBar.add(conditionItem.valueTextField, "2, " + index);
                conditionItemBar.add(conditionItem.relationalComboBox, "3, " + index);
                conditionItemBar.add(conditionItem.addButton, "4, " + index);
                conditionItemBar.add(conditionItem.removeButton, "5, " + index);

                conditionItem.relationalComboBox.setVisible(index != conditionItems.size());

                index++;
            }

            ContainerManager.update(conditionItemBar);
        }

        public String getResult() {
            return resultTextField.getText().trim();
        }

        public ConditionItem getConditionItem(String uuid) {
            for (ConditionItem conditionItem : conditionItems) {
                if (StringUtils.equals(conditionItem.uuid, uuid)) {
                    return conditionItem;
                }
            }

            return null;
        }

        public class ConditionItem {
            protected JBasicTextField parameterTextField = new JBasicTextField();
            protected JBasicComboBox arithmeticComboBox = new JBasicComboBox(new String[] { "==", "!=", ">", ">=", "<", "<=", "matches" });
            protected JBasicTextField valueTextField = new JBasicTextField();
            protected JBasicComboBox relationalComboBox = new JBasicComboBox(new String[] { "&&", "||" });
            protected JClassicButton addButton = new JClassicButton(createAddConditionItemAction());
            protected JClassicButton removeButton = new JClassicButton(createRemoveConditionItemAction());

            protected String uuid;

            public ConditionItem(String uuid) {
                this.uuid = uuid;

                addButton.setName(uuid);
                removeButton.setName(uuid);

                DimensionUtil.setWidth(addButton, 30);
                DimensionUtil.setWidth(removeButton, 30);
            }
        }

        public JSecurityAction createAddConditionItemAction() {
            JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("add.png"), ConsoleLocaleFactory.getString("add_condition_item")) {
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
            JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("delete.png"), ConsoleLocaleFactory.getString("remove_condition_item")) {
                private static final long serialVersionUID = 1L;

                public void execute(ActionEvent e) {
                    if (conditionItems.size() < 2) {
                        JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenConditionPanel.this), ConsoleLocaleFactory.getString("condition_item_one_at_least"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

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

        public JSecurityAction createAggregateConditionAction(List<ConditionItem> conditionItems) {
            JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("netbean/action_16.png"), ConsoleLocaleFactory.getString("aggregate_condition_tooltip")) {
                private static final long serialVersionUID = 1L;

                public void execute(ActionEvent e) {
                    StringBuilder stringBuilder = new StringBuilder();

                    int index = 0;
                    for (ConditionItem conditionItem : conditionItems) {
                        String parameter = conditionItem.parameterTextField.getText().trim();
                        String arithmetic = conditionItem.arithmeticComboBox.getSelectedItem().toString();
                        String value = conditionItem.valueTextField.getText().trim();
                        String relational = conditionItem.relationalComboBox.getSelectedItem().toString();

                        if (StringUtils.isBlank(parameter)) {
                            JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenConditionPanel.this), ConsoleLocaleFactory.getString("condition_item_parameter_not_null"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                            return;
                        }

                        stringBuilder.append("#H['").append(parameter).append("'] ").append(arithmetic).append(" '").append(value).append("'");

                        if (index < conditionItems.size() - 1) {
                            stringBuilder.append(" ").append(relational).append(" ");
                        }

                        index++;
                    }

                    resultTextField.setText(stringBuilder.toString());
                }
            };

            return action;
        }

        public JSecurityAction createValidateConditionAction() {
            JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("netbean/linear_16.png"), ConsoleLocaleFactory.getString("validate_condition_tooltip")) {
                private static final long serialVersionUID = 1L;

                public void execute(ActionEvent e) {

                }
            };

            return action;
        }
    }
}