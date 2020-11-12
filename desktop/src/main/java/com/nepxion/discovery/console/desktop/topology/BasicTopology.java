package com.nepxion.discovery.console.desktop.topology;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import twaver.GeoCoordinate;
import twaver.Node;
import twaver.TDataBox;
import twaver.TView;
import twaver.TWaverConst;
import twaver.gis.GeographyMap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;

import com.nepxion.cots.twaver.element.TElementManager;
import com.nepxion.cots.twaver.element.TGroup;
import com.nepxion.cots.twaver.element.TLink;
import com.nepxion.cots.twaver.element.TNode;
import com.nepxion.cots.twaver.gis.TGisGraph;
import com.nepxion.cots.twaver.graph.TGraph;
import com.nepxion.cots.twaver.graph.TGraphBackground;
import com.nepxion.cots.twaver.graph.TGraphControlBar;
import com.nepxion.cots.twaver.graph.TGraphController;
import com.nepxion.cots.twaver.graph.TGraphManager;
import com.nepxion.cots.twaver.graph.TGraphPointBackground;
import com.nepxion.cots.twaver.graph.TGraphPopupMenuGenerator;
import com.nepxion.cots.twaver.graph.TLayoutPanel;
import com.nepxion.cots.twaver.graph.TLayouterBar;
import com.nepxion.cots.twaver.locale.TLocale;
import com.nepxion.discovery.console.desktop.context.UIContext;
import com.nepxion.discovery.console.desktop.icon.ConsoleIconFactory;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.JBasicButton;
import com.nepxion.swing.button.JBasicToggleButton;
import com.nepxion.swing.button.JClassicMenuButton;
import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.menuitem.JBasicMenuItem;
import com.nepxion.swing.menuitem.JBasicRadioButtonMenuItem;
import com.nepxion.swing.popupmenu.JBasicPopupMenu;

public class BasicTopology extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String IMAGE_FOLDER = "/" + ConsoleIconFactory.getIconPath();

    protected TGraph graph;
    protected TDataBox dataBox = new TDataBox();

    protected JBasicPopupMenu popupMenu;
    protected JBasicMenuItem pinSelectedGroupMenuItem;
    protected JBasicMenuItem pinSelectedNodeMenuItem;

    protected JBasicRadioButtonMenuItem groupAutoExpandRadioButtonMenuItem;
    protected JBasicRadioButtonMenuItem linkAutoHideRadioButtonMenuItem;

    protected ActionListener layoutActionListener;

    public BasicTopology() {
        setLayout(new BorderLayout());
        add(createGraph(), BorderLayout.CENTER);
        // add(createGisGraph(), BorderLayout.CENTER);

        initializePopupMenu();
    }

    protected TGraph createGraph() {
        if (graph == null) {
            graph = new TGraph();
            graph.setDataBox(dataBox);
            graph.setGraphBackground(createBackground());
            graph.getControlBarInternalFrame().setVisible(false);
        }

        return graph;
    }

    protected TGraph createGisGraph() {
        if (graph == null) {
            graph = new TGisGraph();
            graph.setDataBox(dataBox);
            graph.getControlBarInternalFrame().setVisible(false);

            TGisGraph gisGraph = (TGisGraph) graph;
            GeographyMap map = gisGraph.getMap();
            map.setZoom(16);
            map.setCenterPoint(new GeoCoordinate(118.795896, 32.088683));
        }

        return graph;
    }

    protected TGraph getGraph() {
        return graph;
    }

    protected TGraphBackground createBackground() {
        return createBackground(null);
    }

    protected TGraphBackground createBackground(String title) {
        TGraphBackground background = new TGraphBackground(Color.white, TGraphBackground.BLUE_STYLE_COLOR);
        // TGraphBackground background = new TGraphBackground(Color.cyan, Color.black);
        // background.setGradientFactory(TWaverConst.GRADIENT_LINE_S);
        background.setTitleAlignment(TGraphPointBackground.TOP);
        background.setTitle(title);

        return background;
    }

    protected void initializePopupMenu() {
        popupMenu = new JBasicPopupMenu();

        pinSelectedGroupMenuItem = new JBasicMenuItem(TGraphController.getPinSelectedGroupAction(dataBox));
        popupMenu.add(pinSelectedGroupMenuItem);

        pinSelectedNodeMenuItem = new JBasicMenuItem(TGraphController.getPinSelectedNodeAction(dataBox));
        popupMenu.add(pinSelectedNodeMenuItem);

        new TGraphPopupMenuGenerator(graph) {
            @Override
            public JPopupMenu generate(TView view, MouseEvent event) {
                return popupMenuGenerate();
            }
        };
    }

    protected JBasicPopupMenu popupMenuGenerate() {
        TGroup group = TElementManager.getSelectedGroup(dataBox);
        pinSelectedGroupMenuItem.setVisible(group != null);

        TNode node = TElementManager.getSelectedNode(dataBox);
        pinSelectedNodeMenuItem.setVisible(node != null);

        if (group != null || node != null) {
            return popupMenu;
        }

        return null;
    }

    protected JClassicMenuButton createConfigButton(boolean linkConfig) {
        JBasicPopupMenu popupMenu = new JBasicPopupMenu();

        JSecurityAction showLayoutAction = createShowLayoutAction();
        popupMenu.add(new JBasicMenuItem(showLayoutAction));

        popupMenu.addSeparator();

        JSecurityAction groupExpandAction = TGraphController.getGroupExpandAction(graph);
        groupExpandAction.setIcon(ConsoleIconFactory.getSwingIcon("rectangle_multi.png"));
        popupMenu.add(new JBasicMenuItem(groupExpandAction));

        JSecurityAction groupCollapseAction = TGraphController.getGroupCollapseAction(graph);
        groupCollapseAction.setIcon(ConsoleIconFactory.getSwingIcon("rectangle_single.png"));
        popupMenu.add(new JBasicMenuItem(groupCollapseAction));

        groupAutoExpandRadioButtonMenuItem = new JBasicRadioButtonMenuItem(TLocale.getString("group_expand_auto"), TLocale.getString("group_expand_auto"));
        popupMenu.add(groupAutoExpandRadioButtonMenuItem);

        if (linkConfig) {
            popupMenu.addSeparator();

            JSecurityAction linkShowAction = TGraphController.getLinkShowAction(graph);
            linkShowAction.setIcon(ConsoleIconFactory.getSwingIcon("netbean/arc_16.png"));
            popupMenu.add(new JBasicMenuItem(linkShowAction));

            JSecurityAction linkHideAction = TGraphController.getLinkHideAction(graph);
            linkHideAction.setIcon(ConsoleIconFactory.getSwingIcon("netbean/arc_to_16.png"));
            popupMenu.add(new JBasicMenuItem(linkHideAction));

            linkAutoHideRadioButtonMenuItem = new JBasicRadioButtonMenuItem(TLocale.getString("link_hide_auto"), TLocale.getString("link_hide_auto"));
            popupMenu.add(linkAutoHideRadioButtonMenuItem);
        }

        JClassicMenuButton menuButton = new JClassicMenuButton(SwingLocale.getString("setting"), ConsoleIconFactory.getSwingIcon("property.png"), SwingLocale.getString("setting"));
        menuButton.setPopupMenu(popupMenu);

        return menuButton;
    }

    private JSecurityAction createShowLayoutAction() {
        JSecurityAction action = new JSecurityAction(SwingLocale.getString("layout"), ConsoleIconFactory.getSwingIcon("netbean/clip_view_16.png"), SwingLocale.getString("layout")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                showLayout();
            }
        };

        return action;
    }

    protected void showLayout() {

    }

    protected boolean isGroupAutoExpand() {
        if (groupAutoExpandRadioButtonMenuItem == null) {
            return false;
        }

        return groupAutoExpandRadioButtonMenuItem.isSelected();
    }

    protected void setGroupAutoExpand(boolean expand) {
        if (groupAutoExpandRadioButtonMenuItem == null) {
            return;
        }

        groupAutoExpandRadioButtonMenuItem.setSelected(expand);
    }

    protected boolean isLinkAutoHide() {
        if (linkAutoHideRadioButtonMenuItem == null) {
            return false;
        }

        return linkAutoHideRadioButtonMenuItem.isSelected();
    }

    protected void setLinkAutoHide(boolean hide) {
        if (linkAutoHideRadioButtonMenuItem == null) {
            return;
        }

        linkAutoHideRadioButtonMenuItem.setSelected(hide);
    }

    protected TGroup createGroup(String name, NodeUI nodeUI, NodeLocation nodeLocation, int index) {
        String image = nodeUI.getImage();

        int startX = nodeLocation.getStartX();
        int startY = nodeLocation.getStartY();
        int horizontalGap = nodeLocation.getHorizontalGap();
        int verticalGap = nodeLocation.getVerticalGap();
        Point location = new Point(nodeUI.isHorizontalPile() ? startX + index * horizontalGap : startX, nodeUI.isHorizontalPile() ? startY : startY + index * verticalGap);

        return createGroup(name, image, location);
    }

    protected TGroup createGroup(String name, NodeUI nodeUI) {
        String image = nodeUI.getImage();
        Point location = nodeUI.getLocation();

        return createGroup(name, image, location);
    }

    protected TGroup createGroup(String name, String image, Point location) {
        TGroup group = new TGroup();
        group.setName(name);
        group.setImage(IMAGE_FOLDER + image);
        group.putLabelFont(new Font(UIContext.getFontName(), Font.PLAIN, UIContext.getDefaultFontSize()));
        group.setLocation(location);

        return group;
    }

    protected TNode createNode(String name, NodeUI nodeUI, NodeLocation nodeLocation, int index) {
        String image = nodeUI.getImage();

        int startX = nodeLocation.getStartX();
        int startY = nodeLocation.getStartY();
        int horizontalGap = nodeLocation.getHorizontalGap();
        int verticalGap = nodeLocation.getVerticalGap();
        Point location = new Point(nodeUI.isHorizontalPile() ? startX + index * horizontalGap : startX, nodeUI.isHorizontalPile() ? startY : startY + index * verticalGap);

        return createNode(name, image, location);
    }

    protected TNode createNode(String name, NodeUI nodeUI) {
        String image = nodeUI.getImage();
        Point location = nodeUI.getLocation();

        return createNode(name, image, location);
    }

    protected TNode createNode(String name, String image, Point location) {
        TNode node = new TNode();
        node.setName(name);
        node.setImage(IMAGE_FOLDER + image);
        node.putLabelFont(new Font(UIContext.getFontName(), Font.PLAIN, UIContext.getSmallFontSize()));
        node.setLocation(location);

        setNodeTopBottom(node, true);

        return node;
    }

    protected void setNodeTopBottom(TNode node, boolean bottom) {
        if (bottom) {
            node.putLabelPosition(TWaverConst.POSITION_BOTTOM);
            node.putLabelYOffset(6);
        } else {
            node.putLabelPosition(TWaverConst.POSITION_TOP);
            node.putLabelYOffset(-5);
        }
    }

    protected TLink createLink(Node fromNode, Node toNode) {
        return createLink(fromNode, toNode, false);
    }

    protected TLink createLink(Node fromNode, Node toNode, boolean toArrow) {
        TLink link = new TLink(fromNode, toNode);
        link.putLinkToArrow(toArrow);
        link.putLabelYOffset(-2);
        link.putLabelFont(new Font(UIContext.getFontName(), Font.PLAIN, UIContext.getSmallFontSize()));

        return link;
    }

    protected void showLayoutBar(int xOffset, int yOffset, int xGap, int yGap) {
        // Ugly code
        TGraphControlBar graphControlBar = (TGraphControlBar) graph.getControlBarInternalFrame().getContent();
        JBasicToggleButton toggleButton = (JBasicToggleButton) graphControlBar.getViewToolBar().getViewOutlook().getComponent(10);
        toggleButton.setSelected(true);

        TGraphManager.layout(graph);

        TLayouterBar layouterBar = (TLayouterBar) graph.getLayoutInternalFrame().getContent();
        JScrollPane scrollPane = (JScrollPane) layouterBar.getTabAt(layouterBar.getSelectedTitle());
        JPanel panel = (JPanel) scrollPane.getViewport().getView();
        TLayoutPanel layoutPanel = (TLayoutPanel) panel.getComponent(0);

        JPanel childPanel1 = (JPanel) layoutPanel.getComponent(0);
        JBasicComboBox typeComboBox = (JBasicComboBox) childPanel1.getComponent(1);
        typeComboBox.setSelectedIndex(2);

        JPanel childPanel2 = (JPanel) layoutPanel.getComponent(1);
        JSlider xOffsetSlider = (JSlider) childPanel2.getComponent(9);
        xOffsetSlider.setValue(xOffset);
        JSlider yOffsetSlider = (JSlider) childPanel2.getComponent(11);
        yOffsetSlider.setValue(yOffset);
        JSlider xGapSlider = (JSlider) childPanel2.getComponent(13);
        xGapSlider.setValue(xGap);
        JSlider yGapSlider = (JSlider) childPanel2.getComponent(15);
        yGapSlider.setValue(yGap);

        JPanel childPanel3 = (JPanel) layoutPanel.getComponent(2);
        JBasicButton runButton = (JBasicButton) childPanel3.getComponent(1);
        layoutActionListener = runButton.getActionListeners()[0];

        graph.getLayoutInternalFrame().setLocation(3000, 3000);
        // graph.adjustComponentPosition(graph.getLayoutInternalFrame());
    }

    protected void toggleLayoutBar() {
        // Ugly code
        TGraphControlBar graphControlBar = (TGraphControlBar) graph.getControlBarInternalFrame().getContent();
        JBasicToggleButton toggleButton = (JBasicToggleButton) graphControlBar.getViewToolBar().getViewOutlook().getComponent(10);
        toggleButton.doClick();
    }
}