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

import javax.swing.JPanel;

import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.util.DimensionUtil;
import com.nepxion.discovery.console.desktop.workspace.type.NodeType;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.layout.table.TableLayout;
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

    public String getBlueCondition() {
        return blueConditionBar.getResult();
    }

    public String getGreenCondition() {
        return greenConditionBar.getResult();
    }

    public class ConditionBar extends JPanel {
        private static final long serialVersionUID = 1L;

        private List<ConditionItem> conditionItems = new ArrayList<ConditionItem>();
        protected JBasicTextField resultTextField;

        public ConditionBar(NodeType nodeType) {
            for (int i = 0; i < 5; i++) {
                ConditionItem conditionItem = new ConditionItem();
                conditionItems.add(conditionItem);
            }

            JShrinkShortcut shrinkShortcut = new JShrinkShortcut();
            shrinkShortcut.setTitle(nodeType.getDescription());
            shrinkShortcut.setIcon(nodeType == NodeType.BLUE ? ConsoleIconFactory.getSwingIcon("circle_blue.png") : ConsoleIconFactory.getSwingIcon("circle_green.png"));
            shrinkShortcut.setToolTipText(nodeType.getDescription());

            resultTextField = new JBasicTextField();

            JPanel resultButtonBar = new JPanel();
            resultButtonBar.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 0));
            resultButtonBar.add(DimensionUtil.setWidth(new JClassicButton(createAggregateConditionAction()), 30));
            resultButtonBar.add(DimensionUtil.setWidth(new JClassicButton(createValidateConditionAction()), 30));

            JPanel resultBar = new JPanel();
            resultBar.setLayout(new BorderLayout(0, 5));
            resultBar.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("result")), 5), BorderLayout.WEST);
            resultBar.add(resultTextField, BorderLayout.CENTER);
            resultBar.add(resultButtonBar, BorderLayout.EAST);

            double[][] size = {
                    { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED },
                    { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
            };

            TableLayout tableLayout = new TableLayout(size);
            tableLayout.setHGap(0);
            tableLayout.setVGap(5);

            setLayout(tableLayout);
            add(shrinkShortcut, "0, 0, 3, 0");
            add(new JBasicLabel(ConsoleLocaleFactory.getString("parameter")), "0, 1");
            add(new JBasicLabel(ConsoleLocaleFactory.getString("arithmetic")), "1, 1");
            add(new JBasicLabel(ConsoleLocaleFactory.getString("value")), "2, 1");
            add(new JBasicLabel(ConsoleLocaleFactory.getString("logic")), "3, 1, 3, 1");

            int index = 2;
            for (ConditionItem conditionItem : conditionItems) {
                add(conditionItem.parameterTextField, "0, " + index);
                add(conditionItem.arithmeticComboBox, "1, " + index);
                add(conditionItem.valueTextField, "2, " + index);
                add(conditionItem.logicComboBox, "3, " + index);

                index++;
            }

            add(resultBar, "0, 7, 3, 7");
        }

        public String getResult() {
            return resultTextField.getText().trim();
        }
    }

    public class ConditionItem {
        protected JBasicTextField parameterTextField = new JBasicTextField();
        protected JBasicComboBox arithmeticComboBox = new JBasicComboBox(new String[] { "==", "!=", ">", ">=", "<", "<=", "matches" });
        protected JBasicTextField valueTextField = new JBasicTextField();
        protected JBasicComboBox logicComboBox = new JBasicComboBox(new String[] { "&&", "||" });
    }

    public JSecurityAction createAggregateConditionAction() {
        JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("theme/folder/deploy.png"), ConsoleLocaleFactory.getString("aggregate_condition_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {

            }
        };

        return action;
    }

    public JSecurityAction createValidateConditionAction() {
        JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("theme/folder/snapshot.png"), ConsoleLocaleFactory.getString("validate_condition_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {

            }
        };

        return action;
    }
}