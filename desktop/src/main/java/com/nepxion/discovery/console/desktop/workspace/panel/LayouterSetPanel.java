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

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.util.DimensionUtil;
import com.nepxion.discovery.console.desktop.workspace.type.SetType;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.tabbedpane.JBasicTabbedPane;
import com.nepxion.swing.textfield.number.JNumberTextField;

public class LayouterSetPanel extends SetPanel {
    private static final long serialVersionUID = 1L;

    private static final String X_OFFSET = "125";
    private static final String Y_OFFSET = "100";
    private static final String X_GAP = "200";
    private static final String Y_GAP = "60";

    private JNumberTextField xOffsetTextField;
    private JNumberTextField yOffsetTextField;
    private JNumberTextField xGapTextField;
    private JNumberTextField yGapTextField;

    public LayouterSetPanel() {
        super(SetType.LAYOUTER);

        xOffsetTextField = new JNumberTextField(4, 0, 0, 10000);
        yOffsetTextField = new JNumberTextField(4, 0, 0, 10000);
        xGapTextField = new JNumberTextField(4, 0, 0, 10000);
        yGapTextField = new JNumberTextField(4, 0, 0, 10000);

        xOffsetTextField.setText(X_OFFSET);
        yOffsetTextField.setText(Y_OFFSET);
        xGapTextField.setText(X_GAP);
        yGapTextField.setText(Y_GAP);

        double[][] size = {
                { TableLayout.PREFERRED, TableLayout.FILL },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED }
        };

        TableLayout tableLayout = new TableLayout(size);
        tableLayout.setHGap(5);
        tableLayout.setVGap(5);

        JPanel layouterPanel = new JPanel();
        layouterPanel.setLayout(tableLayout);
        layouterPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        layouterPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("x_offset")), 5), "0, 0");
        layouterPanel.add(xOffsetTextField, "1, 0");
        layouterPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("y_offset")), 5), "0, 1");
        layouterPanel.add(yOffsetTextField, "1, 1");
        layouterPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("x_gap")), 5), "0, 2");
        layouterPanel.add(xGapTextField, "1, 2");
        layouterPanel.add(DimensionUtil.addWidth(new JBasicLabel(ConsoleLocaleFactory.getString("y_gap")), 5), "0, 3");
        layouterPanel.add(yGapTextField, "1, 3");

        JBasicTabbedPane layouterTabbedPane = new JBasicTabbedPane();
        layouterTabbedPane.addTab(ConsoleLocaleFactory.getString("layouter_parameter"), layouterPanel, ConsoleLocaleFactory.getString("layouter_parameter"));

        setLayout(new BorderLayout(0, 5));
        setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        add(layouterTabbedPane, BorderLayout.CENTER);
    }

    public int getXOffset() {
        return Integer.parseInt(xOffsetTextField.getText());
    }

    public int getYOffset() {
        return Integer.parseInt(yOffsetTextField.getText());
    }

    public int getXGap() {
        return Integer.parseInt(xGapTextField.getText());
    }

    public int getYGap() {
        return Integer.parseInt(yGapTextField.getText());
    }

    public void setXOffset(int xOffset) {
        xOffsetTextField.setText(String.valueOf(xOffset));
    }

    public void setYOffset(int yOffset) {
        yOffsetTextField.setText(String.valueOf(yOffset));
    }

    public void setXGap(int xGap) {
        xGapTextField.setText(String.valueOf(xGap));
    }

    public void setYGap(int yGap) {
        yGapTextField.setText(String.valueOf(yGap));
    }
}