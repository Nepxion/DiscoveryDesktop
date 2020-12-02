package com.nepxion.discovery.console.desktop.workspace;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import twaver.DataBoxEvent;
import twaver.DataBoxListener;
import twaver.Generator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
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
import com.nepxion.discovery.console.cache.ConsoleCache;
import com.nepxion.discovery.console.controller.ConsoleController;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.swing.dialog.JExceptionDialog;
import com.nepxion.discovery.console.desktop.common.util.DimensionUtil;
import com.nepxion.discovery.console.desktop.workspace.panel.CacheSetPanel;
import com.nepxion.discovery.console.desktop.workspace.panel.LayouterSetPanel;
import com.nepxion.discovery.console.desktop.workspace.panel.SetManagePanel;
import com.nepxion.discovery.console.desktop.workspace.topology.BasicTopology;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeLocation;
import com.nepxion.discovery.console.desktop.workspace.topology.NodeUI;
import com.nepxion.discovery.console.entity.Instance;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.listener.DisplayAbilityListener;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.optionpane.JBasicOptionPane;
import com.nepxion.swing.scrollpane.JBasicScrollPane;
import com.nepxion.swing.textarea.JBasicTextArea;

public abstract class AbstractTopology extends BasicTopology {
    private static final long serialVersionUID = 1L;

    protected NodeLocation nodeLocation = new NodeLocation(415, 100, 250, 60);

    protected TGraphBackground background;

    protected JPanel operationBar = new JPanel();

    public AbstractTopology() {
        initializeTopology();
        // initializeListener();

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    public void initializeTopology() {
        background = graph.getGraphBackground();

        graph.setElementStateOutlineColorGenerator(new Generator() {
            public Object generate(Object object) {
                return null;
            }
        });

        dataBox.addDataBoxListener(new DataBoxListener() {
            @Override
            public void elementAdded(DataBoxEvent arg0) {
                executeLayout();
            }

            @Override
            public void elementRemoved(DataBoxEvent arg0) {
                executeLayout();
            }

            @Override
            public void elementsCleared(DataBoxEvent arg0) {
                executeLayout();
            }
        });
    }

    public void initializeListener() {
        addHierarchyListener(new DisplayAbilityListener() {
            public void displayAbilityChanged(HierarchyEvent e) {
                SetManagePanel setManagePanel = SetManagePanel.getInstance();
                LayouterSetPanel layouterSetPanel = setManagePanel.getLayouterSetPanel();
                int xOffset = layouterSetPanel.getXOffset();
                int yOffset = layouterSetPanel.getYOffset();
                int xGap = layouterSetPanel.getXGap();
                int yGap = layouterSetPanel.getYGap();

                showLayoutBar(xOffset, yOffset, xGap, yGap);
                toggleLayoutBar();

                removeHierarchyListener(this);
            }
        });
    }

    public abstract void initializeOperationBar();

    public JPanel getOperationBar() {
        return operationBar;
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
                return link;
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

    public void showResult(Object result) {
        JBasicTextArea resultTextArea = new JBasicTextArea();
        resultTextArea.setLineWrap(true);
        resultTextArea.setText(result.toString());

        JBasicScrollPane resultScrollPane = new JBasicScrollPane(resultTextArea);
        resultScrollPane.setMaximumSize(new Dimension(800, 600));
        DimensionUtil.addHeight(resultScrollPane, 20);

        JBasicOptionPane.showOptionDialog(HandleManager.getFrame(this), resultScrollPane, ConsoleLocaleFactory.getString("execute_result"), JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/edit.png"), new Object[] { SwingLocale.getString("close") }, null, true);
    }

    public void executeLayout() {
        SetManagePanel setManagePanel = SetManagePanel.getInstance();
        LayouterSetPanel layouterSetPanel = setManagePanel.getLayouterSetPanel();
        int xOffset = layouterSetPanel.getXOffset();
        int yOffset = layouterSetPanel.getYOffset();
        int xGap = layouterSetPanel.getXGap();
        int yGap = layouterSetPanel.getYGap();

        layouter.doLayout(TLayoutType.HIERARCHIC_LAYOUT_TYPE, xOffset, yOffset, xGap, yGap);
    }

    public JSecurityAction createSetAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("set_text"), ConsoleIconFactory.getSwingIcon("config.png"), ConsoleLocaleFactory.getString("set_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                SetManagePanel setManagePanel = SetManagePanel.getInstance();
                CacheSetPanel cacheSetPanel = setManagePanel.getCacheSetPanel();
                cacheSetPanel.setInitialModel();

                JBasicOptionPane.showOptionDialog(HandleManager.getFrame(AbstractTopology.this), setManagePanel, ConsoleLocaleFactory.getString("set_text"), JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/deploy.png"), new Object[] { SwingLocale.getString("close") }, null, true);

                executeLayout();
            }
        };

        return action;
    }

    public JSecurityAction createLayoutAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("layout_text"), ConsoleIconFactory.getSwingIcon("layout.png"), ConsoleLocaleFactory.getString("layout_tooltip")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                executeLayout();
            }
        };

        return action;
    }

    public List<String> getServiceIds(boolean isGateway) {
        try {
            if (isGateway) {
                return ConsoleCache.getGateways();
            } else {
                return ConsoleCache.getRealServices();
            }
        } catch (Exception e) {
            JExceptionDialog.traceException(HandleManager.getFrame(this), ConsoleLocaleFactory.getString("operation_failure"), e);
        }

        return null;
    }

    public List<Instance> getInstances(String serviceId) {
        if (StringUtils.isBlank(serviceId)) {
            return null;
        }

        try {
            return ConsoleController.getInstanceList(serviceId);
        } catch (Exception e) {
            JExceptionDialog.traceException(HandleManager.getFrame(this), ConsoleLocaleFactory.getString("operation_failure"), e);
        }

        return null;
    }
}