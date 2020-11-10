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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.apache.commons.collections4.CollectionUtils;

import com.nepxion.cots.twaver.element.TElementManager;
import com.nepxion.cots.twaver.element.TLink;
import com.nepxion.cots.twaver.element.TNode;
import com.nepxion.cots.twaver.graph.TGraphBackground;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.console.desktop.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.workspace.topology.AbstractTopology;
import com.nepxion.discovery.console.desktop.workspace.topology.LocationEntity;
import com.nepxion.discovery.console.desktop.workspace.topology.TopologyEntity;
import com.nepxion.discovery.console.desktop.workspace.topology.TopologyEntityType;
import com.nepxion.discovery.console.desktop.workspace.topology.TopologyStyleType;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.ButtonManager;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.icon.IconFactory;
import com.nepxion.swing.label.JBasicLabel;
import com.nepxion.swing.layout.filed.FiledLayout;
import com.nepxion.swing.selector.checkbox.JCheckBoxSelector;
import com.nepxion.swing.textfield.JBasicTextField;

public class BlueGreenTopology extends AbstractTopology {
    private static final long serialVersionUID = 1L;

    private LocationEntity locationEntity = new LocationEntity(100, 200, 200, 0);
    private TopologyEntity gatewayBlackNodeEntity = new TopologyEntity(TopologyEntityType.GATEWAY_BLACK, TopologyStyleType.LARGE, true);
    private TopologyEntity serviceBlackNodeEntity = new TopologyEntity(TopologyEntityType.SERVICE_BLACK, TopologyStyleType.MIDDLE, true);
    private TopologyEntity serviceBlueNodeEntity = new TopologyEntity(TopologyEntityType.SERVICE_BLUE, TopologyStyleType.MIDDLE, true);
    private TopologyEntity serviceGreenNodeEntity = new TopologyEntity(TopologyEntityType.SERVICE_GREEN, TopologyStyleType.MIDDLE, true);

    private TGraphBackground background;
    private JBasicComboBox serviceComboBox;
    private JBasicTextField blueMetadataTextField;
    private JBasicTextField greenMetadataTextField;
    private JBasicTextField defaultMetadataTextField;
    private JBasicTextField blueConditionTextField;
    private JBasicTextField greenConditionTextField;

    public BlueGreenTopology() {
        initializeTopToolBar();
        initializeBottomToolBar();
        initializeTopology();

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void initializeTopToolBar() {
        String[] services = new String[] { "discovery-guide-serivice-a" };
        serviceComboBox = new JBasicComboBox(services);
        serviceComboBox.setPreferredSize(new Dimension(250, serviceComboBox.getPreferredSize().height));

        blueMetadataTextField = new JBasicTextField();
        blueMetadataTextField.setPreferredSize(new Dimension(150, blueMetadataTextField.getPreferredSize().height));
        JClassicButton blueMetadataButton = new JClassicButton(createMetadataSelectorAction(blueMetadataTextField));
        blueMetadataButton.setPreferredSize(new Dimension(30, blueMetadataButton.getPreferredSize().height));

        greenMetadataTextField = new JBasicTextField();
        greenMetadataTextField.setPreferredSize(new Dimension(150, greenMetadataTextField.getPreferredSize().height));
        JClassicButton greenMetadataButton = new JClassicButton(createMetadataSelectorAction(greenMetadataTextField));
        greenMetadataButton.setPreferredSize(new Dimension(30, greenMetadataButton.getPreferredSize().height));

        defaultMetadataTextField = new JBasicTextField();
        defaultMetadataTextField.setPreferredSize(new Dimension(150, defaultMetadataTextField.getPreferredSize().height));
        JClassicButton defaultMetadataButton = new JClassicButton(createMetadataSelectorAction(defaultMetadataTextField));
        defaultMetadataButton.setPreferredSize(new Dimension(30, defaultMetadataButton.getPreferredSize().height));

        JPanel servicePanel = new JPanel();
        servicePanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 0));
        servicePanel.add(new JBasicLabel("蓝绿服务"));
        servicePanel.add(Box.createHorizontalStrut(5));
        servicePanel.add(new JBasicLabel(ConsoleIconFactory.getSwingIcon("information_message.png")));
        servicePanel.add(Box.createHorizontalStrut(10));
        servicePanel.add(serviceComboBox);
        servicePanel.add(Box.createHorizontalStrut(10));
        servicePanel.add(new JBasicLabel("蓝版本"));
        servicePanel.add(Box.createHorizontalStrut(10));
        servicePanel.add(blueMetadataTextField);
        servicePanel.add(blueMetadataButton);
        servicePanel.add(Box.createHorizontalStrut(10));
        servicePanel.add(new JBasicLabel("绿版本"));
        servicePanel.add(Box.createHorizontalStrut(10));
        servicePanel.add(greenMetadataTextField);
        servicePanel.add(greenMetadataButton);
        servicePanel.add(Box.createHorizontalStrut(10));
        servicePanel.add(new JBasicLabel("兜底版本"));
        servicePanel.add(Box.createHorizontalStrut(10));
        servicePanel.add(defaultMetadataTextField);
        servicePanel.add(defaultMetadataButton);
        servicePanel.add(Box.createHorizontalStrut(10));
        servicePanel.add(new JClassicButton("添加", ConsoleIconFactory.getSwingIcon("add.png")));
        servicePanel.add(new JClassicButton("删除", ConsoleIconFactory.getSwingIcon("delete.png")));
        servicePanel.add(new JClassicButton("修改", ConsoleIconFactory.getSwingIcon("paste.png")));

        blueConditionTextField = new JBasicTextField("#H['a'] == '1' && #H['b'] <= '2'");
        blueConditionTextField.setPreferredSize(new Dimension(436, blueConditionTextField.getPreferredSize().height));

        greenConditionTextField = new JBasicTextField("#H['a'] == '3'");
        greenConditionTextField.setPreferredSize(new Dimension(436, greenConditionTextField.getPreferredSize().height));

        JPanel conditionPanel = new JPanel();
        conditionPanel.setLayout(new FiledLayout(FiledLayout.ROW, FiledLayout.FULL, 0));
        conditionPanel.add(new JBasicLabel("蓝绿条件"));
        conditionPanel.add(Box.createHorizontalStrut(5));
        conditionPanel.add(new JBasicLabel(ConsoleIconFactory.getSwingIcon("information_message.png")));
        conditionPanel.add(Box.createHorizontalStrut(10));
        conditionPanel.add(new JBasicLabel("蓝条件"));
        conditionPanel.add(Box.createHorizontalStrut(10));
        conditionPanel.add(blueConditionTextField);
        conditionPanel.add(Box.createHorizontalStrut(10));
        conditionPanel.add(new JBasicLabel("绿条件"));
        conditionPanel.add(Box.createHorizontalStrut(10));
        conditionPanel.add(greenConditionTextField);
        conditionPanel.add(Box.createHorizontalStrut(10));
        conditionPanel.add(new JClassicButton("校验", ConsoleIconFactory.getSwingIcon("config.png")));
        conditionPanel.add(new JClassicButton("保存", ConsoleIconFactory.getSwingIcon("save.png")));

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
        background.setTitle("My Blue Green | 蓝绿部署 | 版本策略 | 局部订阅模式");
        graph.setElementStateOutlineColorGenerator(new Generator() {
            public Object generate(Object object) {
                return null;
            }
        });

        TNode portalNode = addNode("discovery-guide-gateway", gatewayBlackNodeEntity);

        TNode a1Node = addNode(ButtonManager.getHtmlText("discovery-guide-serivice-a\n[Version=3.0]"), serviceBlueNodeEntity);
        TNode a2Node = addNode(ButtonManager.getHtmlText("discovery-guide-serivice-a\n[Version=2.0]"), serviceGreenNodeEntity);
        TNode a3Node = addNode(ButtonManager.getHtmlText("discovery-guide-serivice-a\n[Version=1.0]"), serviceBlackNodeEntity);
        TLink blueLink = addLink(portalNode, a1Node, TopologyEntity.BLUE);
        blueLink.setDisplayName("蓝路由");
        blueLink.setToolTipText("#H['a'] == '1' && #H['b'] <= '2'");
        TLink greenLink = addLink(portalNode, a2Node, TopologyEntity.GREEN);
        greenLink.setDisplayName("绿路由");
        greenLink.setToolTipText("#H['a'] == '3'");
        TLink defaultLink = addLink(portalNode, a3Node, TopologyEntity.BLACK);
        defaultLink.setDisplayName("兜底路由");

        TNode b1Node = addNode(ButtonManager.getHtmlText("discovery-guide-serivice-b\n[Version=3.0]"), serviceBlueNodeEntity);
        TNode b2Node = addNode(ButtonManager.getHtmlText("discovery-guide-serivice-b\n[Version=2.0]"), serviceGreenNodeEntity);
        TNode b3Node = addNode(ButtonManager.getHtmlText("discovery-guide-serivice-b\n[Version=1.0]"), serviceBlackNodeEntity);
        addLink(a1Node, b1Node, null);
        addLink(a2Node, b2Node, null);
        addLink(a3Node, b3Node, null);

        TNode c1Node = addNode(ButtonManager.getHtmlText("discovery-guide-serivice-c\n[Version=3.0]"), serviceBlueNodeEntity);
        TNode c2Node = addNode(ButtonManager.getHtmlText("discovery-guide-serivice-c\n[Version=2.0]"), serviceGreenNodeEntity);
        TNode c3Node = addNode(ButtonManager.getHtmlText("discovery-guide-serivice-c\n[Version=1.0]"), serviceBlackNodeEntity);
        addLink(b1Node, c1Node, null);
        addLink(b2Node, c2Node, null);
        addLink(b3Node, c3Node, null);
    }

    private TNode addNode(String name, TopologyEntity topologyEntity) {
        TNode node = createNode(name, topologyEntity, locationEntity, 0);
        node.setUserObject(name);

        dataBox.addElement(node);

        return node;
    }

    @SuppressWarnings("unchecked")
    private TLink addLink(TNode fromNode, TNode toNode, Color linkFlowingColor) {
        List<TLink> links = TElementManager.getLinks(dataBox);
        for (TLink link : links) {
            if (link.getFrom() == fromNode && link.getTo() == toNode) {
                return null;
            }
        }

        TLink link = createLink(fromNode, toNode, linkFlowingColor != null);
        if (linkFlowingColor != null) {
            link.putLinkToArrowColor(Color.yellow);
            link.putLinkFlowing(true);
            link.putLinkFlowingColor(linkFlowingColor);
            link.putLinkFlowingWidth(3);
        }

        dataBox.addElement(link);

        return link;
    }

    private JSecurityAction createMetadataSelectorAction(JBasicTextField metadataTextField) {
        JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("direction_south.png"), "版本选取") {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            public void execute(ActionEvent e) {
                String[] metadatas = new String[] { "20201110-001", "20201110-002", "20201110-003", "default" };

                List<ElementNode> metadataElementNodes = new ArrayList<ElementNode>();
                for (String metadata : metadatas) {
                    metadataElementNodes.add(new ElementNode(metadata, IconFactory.getSwingIcon("component/file_chooser_16.png"), metadata, metadata));
                }

                JCheckBoxSelector checkBoxSelector = new JCheckBoxSelector(HandleManager.getFrame(BlueGreenTopology.this), "版本选取", new Dimension(400, 350), metadataElementNodes);
                checkBoxSelector.setVisible(true);
                checkBoxSelector.dispose();

                if (!checkBoxSelector.isConfirmed()) {
                    return;
                }

                List<String> selectedMetadatas = checkBoxSelector.getSelectedUserObjects();
                if (CollectionUtils.isEmpty(selectedMetadatas)) {
                    return;
                }

                StringBuilder StringBuilder = new StringBuilder();
                int index = 0;
                for (String selectedMetadata : selectedMetadatas) {
                    StringBuilder.append(selectedMetadata);
                    if (index < selectedMetadatas.size() - 1) {
                        StringBuilder.append(DiscoveryConstant.SEPARATE);
                    }

                    index++;
                }

                metadataTextField.setText(StringBuilder.toString());
            }
        };

        return action;
    }
}