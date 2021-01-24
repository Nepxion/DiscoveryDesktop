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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.TypeComparator;

import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.entity.ElementType;
import com.nepxion.discovery.common.expression.DiscoveryExpressionResolver;
import com.nepxion.discovery.common.expression.DiscoveryTypeComparor;
import com.nepxion.discovery.common.util.StringUtil;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.util.DimensionUtil;
import com.nepxion.discovery.console.desktop.common.util.TextFieldUtil;
import com.nepxion.discovery.console.desktop.workspace.type.TypeLocale;
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
import com.nepxion.swing.tabbedpane.JBasicTabbedPane;
import com.nepxion.swing.textfield.JBasicTextField;

public class BlueGreenConditionPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    protected ConditionBar blueConditionBar;
    protected ConditionBar greenConditionBar;

    protected JBasicTabbedPane conditionTabbedPane;

    protected TypeComparator typeComparator = new DiscoveryTypeComparor();

    public BlueGreenConditionPanel() {
        blueConditionBar = new ConditionBar(ElementType.BLUE);
        greenConditionBar = new ConditionBar(ElementType.GREEN);

        conditionTabbedPane = new JBasicTabbedPane();
        conditionTabbedPane.addTab(" " + TypeLocale.getDescription(ElementType.BLUE) + " ", blueConditionBar, TypeLocale.getDescription(ElementType.BLUE));
        conditionTabbedPane.addTab(" " + TypeLocale.getDescription(ElementType.GREEN) + " ", greenConditionBar, TypeLocale.getDescription(ElementType.GREEN));

        setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 10));
        add(conditionTabbedPane);
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

    public String getBlueExpression() {
        return blueConditionBar.getCondition();
    }

    public void setBlueExpression(String blueExpression) {
        blueConditionBar.setCondition(blueExpression);
    }

    public String getGreenExpression() {
        return greenConditionBar.getCondition();
    }

    public void setGreenExpression(String greenExpression) {
        greenConditionBar.setCondition(greenExpression);
    }

    public void showBlueConditionNotNullTip() {
        conditionTabbedPane.setSelectedIndex(0);
        blueConditionBar.showConditionNotNullTip();
    }

    public void showGreenConditionNotNullTip() {
        conditionTabbedPane.setSelectedIndex(1);
        greenConditionBar.showConditionNotNullTip();
    }

    public void showBlueConditionInvalidFormatTip() {
        conditionTabbedPane.setSelectedIndex(0);
        blueConditionBar.showConditionInvalidFormatTip();
    }

    public void showGreenConditionInvalidFormatTip() {
        conditionTabbedPane.setSelectedIndex(1);
        greenConditionBar.showConditionInvalidFormatTip();
    }

    public class ConditionBar extends JPanel {
        private static final long serialVersionUID = 1L;

        protected int initialConditionItemCount = 1;
        protected List<ConditionItem> conditionItems = new ArrayList<ConditionItem>();

        protected JPanel conditionItemBar;
        protected JBasicTextField conditionTextField;
        protected JClassicButton aggregateButton;
        protected JBasicTextField validateTextField;
        protected JClassicButton validateButton;

        public ConditionBar(ElementType nodeType) {
            JShrinkShortcut shrinkShortcut = new JShrinkShortcut();
            shrinkShortcut.setTitle(TypeLocale.getDescription(nodeType));
            shrinkShortcut.setIcon(nodeType == ElementType.BLUE ? ConsoleIconFactory.getSwingIcon("circle_blue.png") : ConsoleIconFactory.getSwingIcon("circle_green.png"));
            shrinkShortcut.setToolTipText(TypeLocale.getDescription(nodeType));

            conditionItemBar = new JPanel();
            conditionTextField = new JBasicTextField();
            validateTextField = new JBasicTextField();

            aggregateButton = new JClassicButton(createAggregateConditionAction());
            validateButton = new JClassicButton(createValidateConditionAction());

            double[][] size = {
                    { TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED },
                    { TableLayout.PREFERRED, TableLayout.PREFERRED }
            };

            TableLayout tableLayout = new TableLayout(size);
            tableLayout.setHGap(0);
            tableLayout.setVGap(5);

            JPanel conditionBar = new JPanel();
            conditionBar.setLayout(tableLayout);
            conditionBar.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("aggregate_text")), 5), "0, 0");
            conditionBar.add(conditionTextField, "1, 0");
            conditionBar.add(DimensionUtil.setWidth(aggregateButton, 30), "2, 0");
            conditionBar.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("validate_text")), 5), "0, 1");
            conditionBar.add(TextFieldUtil.setTip(validateTextField, ConsoleLocaleFactory.getString("validate_condition_example")), "1, 1");
            conditionBar.add(DimensionUtil.setWidth(validateButton, 30), "2, 1");

            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
            add(conditionItemBar, BorderLayout.CENTER);
            add(conditionBar, BorderLayout.SOUTH);

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

        public ConditionItem getConditionItem(String uuid) {
            for (ConditionItem conditionItem : conditionItems) {
                if (StringUtils.equals(conditionItem.uuid, uuid)) {
                    return conditionItem;
                }
            }

            return null;
        }

        public String getCondition() {
            return conditionTextField.getText().trim();
        }

        public void setCondition(String condition) {
            conditionTextField.setText(condition);
        }

        public void showConditionNotNullTip() {
            conditionTextField.showTip(ConsoleLocaleFactory.getString("condition_not_null"), ConsoleIconFactory.getSwingIcon("error_message.png"), 1, 12);
        }

        public void showConditionInvalidFormatTip() {
            conditionTextField.showTip(ConsoleLocaleFactory.getString("condition_invalid_format"), ConsoleIconFactory.getSwingIcon("error_message.png"), 1, 16);
        }

        public void showValidationInvalidFormatTip() {
            validateTextField.showTip(ConsoleLocaleFactory.getString("validate_condition_invalid_format"), ConsoleIconFactory.getSwingIcon("error_message.png"), 1, 12);
        }

        public void showValidationResultTip(boolean validated) {
            validateTextField.showTip(ConsoleLocaleFactory.getString("validate_condition_result") + " : " + validated, ConsoleIconFactory.getSwingIcon(validated ? "question_message.png" : "error_message.png"), 1, 12);
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

                parameterTextField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        aggregateButton.getAction().actionPerformed(null);
                    }
                });
                arithmeticComboBox.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        if (arithmeticComboBox.getSelectedItem() != e.getItem()) {
                            aggregateButton.getAction().actionPerformed(null);
                        }
                    }
                });
                valueTextField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        aggregateButton.getAction().actionPerformed(null);
                    }
                });
                relationalComboBox.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        if (arithmeticComboBox.getSelectedItem() != e.getItem()) {
                            aggregateButton.getAction().actionPerformed(null);
                        }
                    }
                });
            }

            public void showParameterNotNullTip() {
                parameterTextField.showTip(ConsoleLocaleFactory.getString("expression_item_parameter_not_null"), ConsoleIconFactory.getSwingIcon("error_message.png"), 1, 12);
            }

            public void showValueNotNullTip() {
                valueTextField.showTip(ConsoleLocaleFactory.getString("expression_item_value_not_null"), ConsoleIconFactory.getSwingIcon("error_message.png"), 1, 12);
            }
        }

        public JSecurityAction createAddConditionItemAction() {
            JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("add.png"), ConsoleLocaleFactory.getString("add_expression_item")) {
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

                    aggregateButton.getAction().actionPerformed(null);
                }
            };

            return action;
        }

        public JSecurityAction createRemoveConditionItemAction() {
            JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("delete.png"), ConsoleLocaleFactory.getString("remove_expression_item")) {
                private static final long serialVersionUID = 1L;

                public void execute(ActionEvent e) {
                    if (conditionItems.size() < 2) {
                        JBasicOptionPane.showMessageDialog(HandleManager.getFrame(BlueGreenConditionPanel.this), ConsoleLocaleFactory.getString("expression_item_one_at_least"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

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

                    aggregateButton.getAction().actionPerformed(null);
                }
            };

            return action;
        }

        public JSecurityAction createAggregateConditionAction() {
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

                        // if (StringUtils.isBlank(parameter)) {
                        //    conditionItem.showParameterNotNullTip();

                        //    return;
                        // }

                        // if (StringUtils.isBlank(value)) {
                        //    conditionItem.showValueNotNullTip();

                        //    return;
                        // }

                        stringBuilder.append("#H['").append(parameter).append("'] ").append(arithmetic).append(" '").append(value).append("'");

                        if (index < conditionItems.size() - 1) {
                            stringBuilder.append(" ").append(relational).append(" ");
                        }

                        index++;
                    }

                    conditionTextField.setText(stringBuilder.toString());
                }
            };

            return action;
        }

        public JSecurityAction createValidateConditionAction() {
            JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("netbean/linear_16.png"), ConsoleLocaleFactory.getString("validate_condition_tooltip")) {
                private static final long serialVersionUID = 1L;

                public void execute(ActionEvent e) {
                    String condition = conditionTextField.getText().trim();
                    String validation = validateTextField.getText().trim();

                    if (StringUtils.isBlank(condition)) {
                        showConditionNotNullTip();

                        return;
                    }

                    if (condition.contains("#H['']")) {
                        showBlueConditionInvalidFormatTip();

                        return;
                    }

                    Map<String, String> map = null;
                    try {
                        map = StringUtil.splitToMap(validation);
                    } catch (Exception ex) {
                        showValidationInvalidFormatTip();

                        return;
                    }

                    boolean validated = DiscoveryExpressionResolver.eval(condition, DiscoveryConstant.EXPRESSION_PREFIX, map, typeComparator);

                    showValidationResultTip(validated);
                }
            };

            return action;
        }
    }
}