package com.nepxion.discovery.console.desktop.workspace.panel;

import java.awt.Dimension;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

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
        setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 0));
        add(new ConditionBar(NodeType.BLUE));
        add(new ConditionBar(NodeType.GREEN));
    }

    public class ConditionBar extends JPanel {
        private static final long serialVersionUID = 1L;

        public JBasicLabel x = new JBasicLabel("结果");
        public JBasicTextField y = new JBasicTextField("#H['a'] == '1' && #H['b'] <= '2'");
        public JClassicButton z = new JClassicButton(IconFactory.getSwingIcon("config.png"));

        public ConditionBar(NodeType nodeType) {
            JShrinkShortcut conditionShrinkShortcut = new JShrinkShortcut();
            conditionShrinkShortcut.setTitle(nodeType.getDescription());
            conditionShrinkShortcut.setIcon(IconFactory.getSwingIcon("stereo/paste_16.png"));
            conditionShrinkShortcut.setToolTipText(nodeType.getDescription());
            
            z.setPreferredSize(new Dimension(30, z.getPreferredSize().height));

            double[][] size = {
                    { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED },
                    { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
            };

            TableLayout tableLayout = new TableLayout(size);
            tableLayout.setHGap(0);
            tableLayout.setVGap(5);

            setLayout(tableLayout);
            add(conditionShrinkShortcut, "0, 0, 5, 0");
            add(new JBasicLabel("参数"), "0, 1");
            add(new JBasicLabel("运算"), "1, 1");
            add(new JBasicLabel("赋值"), "2, 1");
            add(new JBasicLabel("逻辑"), "3, 1, 5, 1");

            ConditionItem c1 = new ConditionItem();
            add(c1.a, "0, 2");
            add(c1.b, "1, 2");
            add(c1.c, "2, 2");
            add(c1.d, "3, 2");
            add(c1.e, "4, 2");
            add(c1.f, "5, 2");

            add(x, "0, 3");
            add(y, "1, 3, 4, 3");
            add(z, "5, 3");
        }
    }

    public class ConditionItem {
        public JBasicTextField a = new JBasicTextField("abcd");
        public JBasicComboBox b = new JBasicComboBox(new String[] { "==", "!=", ">", ">=", "<", "<=", "匹配" });
        public JBasicTextField c = new JBasicTextField("1234");
        public JBasicComboBox d = new JBasicComboBox(new String[] { "&&", "||" });
        public JClassicButton e = new JClassicButton(IconFactory.getSwingIcon("add.png"));
        public JClassicButton f = new JClassicButton(IconFactory.getSwingIcon("delete.png"));

        public ConditionItem() {
            e.setPreferredSize(new Dimension(30, e.getPreferredSize().height));
            f.setPreferredSize(new Dimension(30, f.getPreferredSize().height));
        }
    }
}