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

import javax.swing.JPanel;

import com.nepxion.discovery.console.desktop.workspace.type.NodeType;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.icon.IconFactory;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.shrinkbar.JShrinkShortcut;
import com.nepxion.swing.textfield.JBasicTextField;

public class BlueGreenConditionPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public BlueGreenConditionPanel() {
        setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 10));
        add(new ConditionBar(NodeType.BLUE));
        add(new ConditionBar(NodeType.GREEN));
    }

    public class ConditionBar extends JPanel {
        private static final long serialVersionUID = 1L;

        protected JBasicTextField resultTextField;

        public ConditionBar(NodeType nodeType) {
            JShrinkShortcut shrinkShortcut = new JShrinkShortcut();
            shrinkShortcut.setTitle(nodeType.getDescription());
            shrinkShortcut.setIcon(nodeType == NodeType.BLUE ? IconFactory.getSwingIcon("circle_blue.png") : IconFactory.getSwingIcon("circle_green.png"));
            shrinkShortcut.setToolTipText(nodeType.getDescription());

            JBasicLabel resultLabel = new JBasicLabel("结果");
            resultLabel.setPreferredSize(new Dimension(resultLabel.getPreferredSize().width + 5, resultLabel.getPreferredSize().height));
            resultTextField = new JBasicTextField("#H['a'] == '1' && #H['b'] <= '2'");
            JClassicButton validateButton = new JClassicButton(IconFactory.getSwingIcon("edit.png"));
            validateButton.setPreferredSize(new Dimension(30, validateButton.getPreferredSize().height));

            JPanel resultBar = new JPanel();
            resultBar.setLayout(new BorderLayout(0, 5));
            resultBar.add(resultLabel, BorderLayout.WEST);
            resultBar.add(resultTextField, BorderLayout.CENTER);
            resultBar.add(validateButton, BorderLayout.EAST);

            double[][] size = {
                    { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED },
                    { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
            };

            TableLayout tableLayout = new TableLayout(size);
            tableLayout.setHGap(0);
            tableLayout.setVGap(5);

            setLayout(tableLayout);
            add(shrinkShortcut, "0, 0, 5, 0");
            add(new JBasicLabel("参数"), "0, 1");
            add(new JBasicLabel("运算"), "1, 1");
            add(new JBasicLabel("赋值"), "2, 1");
            add(new JBasicLabel("逻辑"), "3, 1, 5, 1");

            ConditionItem conditionItem1 = new ConditionItem();
            add(conditionItem1.parameterTextField, "0, 2");
            add(conditionItem1.arithmeticComboBox, "1, 2");
            add(conditionItem1.valueTextField, "2, 2");
            add(conditionItem1.logicComboBox, "3, 2");
            add(conditionItem1.addButton, "4, 2");
            add(conditionItem1.deleteButton, "5, 2");

            add(resultBar, "0, 3, 5, 3");
        }

        public String getResult() {
            return resultTextField.getText().trim();
        }
    }

    public class ConditionItem {
        protected JBasicTextField parameterTextField = new JBasicTextField("abcd");
        protected JBasicComboBox arithmeticComboBox = new JBasicComboBox(new String[] { "==", "!=", ">", ">=", "<", "<=", "匹配" });
        protected JBasicTextField valueTextField = new JBasicTextField("1234");
        protected JBasicComboBox logicComboBox = new JBasicComboBox(new String[] { "&&", "||" });
        protected JClassicButton addButton = new JClassicButton(IconFactory.getSwingIcon("add.png"));
        protected JClassicButton deleteButton = new JClassicButton(IconFactory.getSwingIcon("delete.png"));

        public ConditionItem() {
            addButton.setPreferredSize(new Dimension(30, addButton.getPreferredSize().height));
            deleteButton.setPreferredSize(new Dimension(30, deleteButton.getPreferredSize().height));
        }
    }
}