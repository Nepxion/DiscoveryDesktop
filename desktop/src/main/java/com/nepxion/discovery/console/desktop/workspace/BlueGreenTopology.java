package com.nepxion.discovery.console.desktop.workspace;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import twaver.Generator;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.nepxion.cots.twaver.graph.TGraphBackground;
import com.nepxion.discovery.console.desktop.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.workspace.topology.AbstractTopology;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.textfield.JBasicTextField;

public class BlueGreenTopology extends AbstractTopology {
    private static final long serialVersionUID = 1L;

    private TGraphBackground background;

    private JBasicComboBox serviceComboBox;
    private JBasicComboBox blueMetadataComboBox;
    private JBasicComboBox greenMetadataComboBox;
    private JBasicComboBox basicMetadataComboBox;

    private JBasicTextField blueConditionTextField;
    private JBasicTextField greenConditionTextField;

    public BlueGreenTopology() {
        initializeTopToolBar();
        initializeBottomToolBar();
        initializeTopology();

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void initializeTopToolBar() {
        serviceComboBox = new JBasicComboBox();
        serviceComboBox.setPreferredSize(new Dimension(250, serviceComboBox.getPreferredSize().height));

        blueMetadataComboBox = new JBasicComboBox();
        blueMetadataComboBox.setPreferredSize(new Dimension(150, blueMetadataComboBox.getPreferredSize().height));

        greenMetadataComboBox = new JBasicComboBox();
        greenMetadataComboBox.setPreferredSize(new Dimension(150, greenMetadataComboBox.getPreferredSize().height));

        basicMetadataComboBox = new JBasicComboBox();
        basicMetadataComboBox.setPreferredSize(new Dimension(150, basicMetadataComboBox.getPreferredSize().height));

        JPanel servicePanel = new JPanel();
        servicePanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 5));
        servicePanel.add(new JBasicLabel("蓝绿服务"));
        servicePanel.add(Box.createHorizontalStrut(5));
        servicePanel.add(serviceComboBox);
        servicePanel.add(Box.createHorizontalStrut(5));
        servicePanel.add(new JBasicLabel("蓝版本"));
        servicePanel.add(Box.createHorizontalStrut(5));
        servicePanel.add(blueMetadataComboBox);
        servicePanel.add(Box.createHorizontalStrut(5));
        servicePanel.add(new JBasicLabel("绿版本"));
        servicePanel.add(Box.createHorizontalStrut(5));
        servicePanel.add(greenMetadataComboBox);
        servicePanel.add(Box.createHorizontalStrut(5));
        servicePanel.add(new JBasicLabel("兜底版本"));
        servicePanel.add(Box.createHorizontalStrut(5));
        servicePanel.add(basicMetadataComboBox);
        servicePanel.add(Box.createHorizontalStrut(5));
        servicePanel.add(new JClassicButton("添加", ConsoleIconFactory.getSwingIcon("add.png")));
        servicePanel.add(new JClassicButton("删除", ConsoleIconFactory.getSwingIcon("delete.png")));
        servicePanel.add(new JClassicButton("修改", ConsoleIconFactory.getSwingIcon("paste.png")));

        blueConditionTextField = new JBasicTextField();
        blueConditionTextField.setPreferredSize(new Dimension(398, blueConditionTextField.getPreferredSize().height));

        greenConditionTextField = new JBasicTextField();
        greenConditionTextField.setPreferredSize(new Dimension(399, greenConditionTextField.getPreferredSize().height));

        JPanel conditionPanel = new JPanel();
        conditionPanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 5));
        conditionPanel.add(new JBasicLabel("蓝绿条件"));
        conditionPanel.add(Box.createHorizontalStrut(5));
        conditionPanel.add(new JBasicLabel("蓝条件"));
        conditionPanel.add(Box.createHorizontalStrut(5));
        conditionPanel.add(blueConditionTextField);
        conditionPanel.add(Box.createHorizontalStrut(5));
        conditionPanel.add(new JBasicLabel("绿条件"));
        conditionPanel.add(Box.createHorizontalStrut(5));
        conditionPanel.add(greenConditionTextField);
        conditionPanel.add(Box.createHorizontalStrut(5));
        conditionPanel.add(new JClassicButton("校验", ConsoleIconFactory.getSwingIcon("config.png")));
        conditionPanel.add(new JClassicButton("应用", ConsoleIconFactory.getSwingIcon("save.png")));

        JPanel toolBar = new JPanel();
        toolBar.setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 5));
        toolBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        toolBar.add(servicePanel);
        toolBar.add(conditionPanel);

        add(toolBar, BorderLayout.NORTH);
    }

    private void initializeBottomToolBar() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(new JClassicButton("文本预览", ConsoleIconFactory.getSwingIcon("ticket.png")));
        buttonPanel.add(new JClassicButton("链路侦测", ConsoleIconFactory.getSwingIcon("relation.png")));
        buttonPanel.add(new JClassicButton("路由拓扑", ConsoleIconFactory.getSwingIcon("rotate.png")));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(new JClassicButton("布局", ConsoleIconFactory.getSwingIcon("layout.png")));
        buttonPanel.add(new JClassicButton("保存", ConsoleIconFactory.getSwingIcon("save.png")));
        buttonPanel.add(new JClassicButton("清空", ConsoleIconFactory.getSwingIcon("paint.png")));

        JPanel toolBar = new JPanel();
        toolBar.setLayout(new FiledLayout(FiledLayout.COLUMN, FiledLayout.FULL, 5));
        toolBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        toolBar.add(buttonPanel);

        add(toolBar, BorderLayout.SOUTH);
    }

    private void initializeTopology() {
        background = graph.getGraphBackground();
        background.setTitle("蓝绿部署 | 版本策略 | 局部订阅模式");
        graph.setElementStateOutlineColorGenerator(new Generator() {
            public Object generate(Object object) {
                return null;
            }
        });
    }
}