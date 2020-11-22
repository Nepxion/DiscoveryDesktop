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

import java.awt.event.HierarchyEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.nepxion.cots.twaver.graph.TGraphBackground;
import com.nepxion.cots.twaver.graph.TLayoutType;
import com.nepxion.discovery.console.desktop.workspace.topology.BasicTopology;
import com.nepxion.swing.listener.DisplayAbilityListener;

public abstract class AbstractTopology extends BasicTopology {
    private static final long serialVersionUID = 1L;

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
                showLayoutBar(150, 100, 200, 60);
                toggleLayoutBar();

                removeHierarchyListener(this);
            }
        });
    }

    public void executeLayout() {
        layouter.doLayout(TLayoutType.HIERARCHIC_LAYOUT_TYPE, 150, 100, 200, 60);
    }

    public JPanel getOperationBar() {
        return operationBar;
    }

    public abstract void initializeOperationBar();
}