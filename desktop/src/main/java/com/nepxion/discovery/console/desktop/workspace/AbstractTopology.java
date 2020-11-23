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

import java.awt.Color;
import java.awt.event.HierarchyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.cots.twaver.element.TElementManager;
import com.nepxion.cots.twaver.element.TLink;
import com.nepxion.cots.twaver.element.TNode;
import com.nepxion.cots.twaver.graph.TGraphBackground;
import com.nepxion.cots.twaver.graph.TLayoutType;
import com.nepxion.discovery.console.desktop.workspace.topology.BasicTopology;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeImageType;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeLocation;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeSizeType;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeUI;
import com.nepxion.discovery.console.entity.Instance;
import com.nepxion.swing.listener.DisplayAbilityListener;

public abstract class AbstractTopology extends BasicTopology {
    private static final long serialVersionUID = 1L;

    protected NodeLocation nodeLocation = new NodeLocation(415, 100, 200, 0);
    protected NodeUI gatewayBlackNodeUI = new NodeUI(NodeImageType.GATEWAY_BLACK, NodeSizeType.LARGE, true);

    protected TGraphBackground background;

    protected JPanel operationBar = new JPanel();

    public AbstractTopology() {
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        initializeTopology();
        // initializeListener();
    }

    public void initializeTopology() {
        background = graph.getGraphBackground();
        graph.setElementStateOutlineColorGenerator(new Generator() {
            public Object generate(Object object) {
                return null;
            }
        });
    }

    public void initializeListener() {
        addHierarchyListener(new DisplayAbilityListener() {
            public void displayAbilityChanged(HierarchyEvent e) {
                showLayoutBar(125, 100, 200, 60);
                toggleLayoutBar();

                removeHierarchyListener(this);
            }
        });
    }

    public abstract void initializeOperationBar();

    public JPanel getOperationBar() {
        return operationBar;
    }

    @SuppressWarnings("unchecked")
    public boolean hasNodes(String serviceId) {
        List<TNode> nodes = TElementManager.getNodes(dataBox);
        for (TNode node : nodes) {
            Instance instance = (Instance) node.getUserObject();
            if (StringUtils.equalsIgnoreCase(instance.getServiceId(), serviceId)) {
                return true;
            }
        }

        return false;
    }

    public TNode addNode(String name, NodeUI nodeUI) {
        TNode node = createNode(name, nodeUI, nodeLocation, 0);

        dataBox.addElement(node);

        return node;
    }

    @SuppressWarnings("unchecked")
    public TLink addLink(TNode fromNode, TNode toNode, Color linkFlowingColor) {
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

    public void executeLayout() {
        layouter.doLayout(TLayoutType.HIERARCHIC_LAYOUT_TYPE, 125, 100, 200, 60);
    }
}