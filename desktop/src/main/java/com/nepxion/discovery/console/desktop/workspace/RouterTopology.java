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
import java.awt.event.HierarchyEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.nepxion.cots.twaver.element.TElementManager;
import com.nepxion.cots.twaver.element.TLink;
import com.nepxion.cots.twaver.element.TNode;
import com.nepxion.cots.twaver.graph.TGraphBackground;
import com.nepxion.discovery.common.entity.RouterEntity;
import com.nepxion.discovery.console.controller.ServiceController;
import com.nepxion.discovery.console.desktop.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.topology.AbstractTopology;
import com.nepxion.discovery.console.desktop.topology.NodeImageType;
import com.nepxion.discovery.console.desktop.topology.NodeLocation;
import com.nepxion.discovery.console.desktop.topology.NodeSizeType;
import com.nepxion.discovery.console.desktop.topology.NodeUI;
import com.nepxion.discovery.console.entity.Instance;
import com.nepxion.swing.action.JSecurityAction;
import com.nepxion.swing.button.ButtonManager;
import com.nepxion.swing.button.JClassicButton;
import com.nepxion.swing.combobox.JBasicComboBox;
import com.nepxion.swing.dialog.JExceptionDialog;
import com.nepxion.swing.handle.HandleManager;
import com.nepxion.swing.listener.DisplayAbilityListener;
import com.nepxion.swing.locale.SwingLocale;
import com.nepxion.swing.menuitem.JBasicMenuItem;
import com.nepxion.swing.optionpane.JBasicOptionPane;
import com.nepxion.swing.popupmenu.JBasicPopupMenu;
import com.nepxion.swing.scrollpane.JBasicScrollPane;
import com.nepxion.swing.tabbedpane.JBasicTabbedPane;
import com.nepxion.swing.textarea.JBasicTextArea;
import com.nepxion.swing.textfield.JBasicTextField;

public class RouterTopology extends AbstractTopology {
    private static final long serialVersionUID = 1L;

    private NodeLocation nodeLocation = new NodeLocation(440, 50, 200, 0);
    private NodeUI nodeUI = new NodeUI(NodeImageType.SERVICE, NodeSizeType.MIDDLE, true);

    private TGraphBackground background;
    private JBasicMenuItem showRuleMenuItem;
    private JBasicComboBox comboBox;
    private JBasicTextField textField;
    private RulePanel rulePanel;

    private Instance instance;

    public RouterTopology() {
        initializeToolBar();
        initializeTopology();
        initializeListener();
    }

    @Override
    protected void initializePopupMenu() {
        super.initializePopupMenu();

        showRuleMenuItem = new JBasicMenuItem(createShowRuleAction());
        popupMenu.add(showRuleMenuItem, 0);
    }

    @Override
    protected JBasicPopupMenu popupMenuGenerate() {
        super.popupMenuGenerate();

        TNode node = TElementManager.getSelectedNode(dataBox);
        showRuleMenuItem.setVisible(node != null);

        if (node != null) {
            return popupMenu;
        }

        return null;
    }

    private void initializeToolBar() {
        JSecurityAction addServiceAction = createAddServiceAction();

        comboBox = new JBasicComboBox();
        comboBox.setPreferredSize(new Dimension(300, comboBox.getPreferredSize().height));
        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (comboBox.getSelectedItem() != e.getItem()) {
                    addServiceAction.execute(null);
                }
            }
        });

        textField = new JBasicTextField();
        textField.setPreferredSize(new Dimension(650, textField.getPreferredSize().height));

        JToolBar toolBar = getGraph().getToolbar();
        toolBar.addSeparator();
        toolBar.add(new JLabel(ConsoleLocaleFactory.getString("service_list")));
        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(comboBox);
        toolBar.add(new JClassicButton(addServiceAction));
        toolBar.add(new JClassicButton(createDeleteServiceAction()));
        toolBar.add(textField);
        toolBar.add(new JClassicButton(createExecuteRouterAction()));
        toolBar.add(new JClassicButton(createClearRouterAction()));
        toolBar.add(new JClassicButton(createShowRuleAction()));

        ButtonManager.updateUI(toolBar);
    }

    private void initializeTopology() {
        background = graph.getGraphBackground();
        background.setTitle(ConsoleLocaleFactory.getString("title_service_gray_router"));
        graph.setElementStateOutlineColorGenerator(new Generator() {
            public Object generate(Object object) {
                return null;
            }
        });
    }

    private void initializeListener() {
        addHierarchyListener(new DisplayAbilityListener() {
            public void displayAbilityChanged(HierarchyEvent e) {
                showLayoutBar(150, 20, 200, 150);

                RouterTopology.this.setPreferredSize(new Dimension(RouterTopology.this.getPreferredSize().width - 100, 900));

                removeHierarchyListener(this);
            }
        });
    }

    private void route(RouterEntity routerEntity) {
        dataBox.clear();

        int index = 0;

        TNode node = addNode(routerEntity, index);

        index++;

        route(routerEntity, node, index);
    }

    private void route(RouterEntity routerEntity, TNode node, int index) {
        List<RouterEntity> nexts = routerEntity.getNexts();
        if (CollectionUtils.isNotEmpty(nexts)) {
            for (RouterEntity next : nexts) {
                String nodeName = getNodeName(next);
                TNode nextNode = TElementManager.getNode(dataBox, nodeName);
                if (nextNode == null) {
                    nextNode = addNode(next, index);

                    index++;
                }
                addLink(node, nextNode, next);

                route(next, nextNode, index);
            }
        }

    }

    private String getNodeName(Instance instance) {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isNotEmpty(instance.getServiceType())) {
            stringBuilder.append(ConsoleLocaleFactory.getString("type_" + instance.getServiceType())).append(" - ");
        }
        stringBuilder.append(instance.getServiceId()).append("\n");
        stringBuilder.append(instance.getHost()).append(":").append(instance.getPort());
        if (StringUtils.isNotEmpty(instance.getVersion())) {
            stringBuilder.append("\n[Version=").append(instance.getVersion()).append("]");
        }
        if (StringUtils.isNotEmpty(instance.getRegion())) {
            stringBuilder.append("\n[Region=").append(instance.getRegion()).append("]");
        }
        if (StringUtils.isNotEmpty(instance.getEnvironment())) {
            stringBuilder.append("\n[Env=").append(instance.getEnvironment()).append("]");
        }
        if (StringUtils.isNotEmpty(instance.getZone())) {
            stringBuilder.append("\n[Zone=").append(instance.getZone()).append("]");
        }

        return ButtonManager.getHtmlText(stringBuilder.toString());
    }

    private String getNodeName(RouterEntity routerEntity) {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isNotEmpty(routerEntity.getServiceType())) {
            stringBuilder.append(ConsoleLocaleFactory.getString("type_" + routerEntity.getServiceType())).append(" - ");
        }
        stringBuilder.append(routerEntity.getServiceId()).append("\n");
        stringBuilder.append(routerEntity.getHost()).append(":").append(routerEntity.getPort());
        if (StringUtils.isNotEmpty(routerEntity.getVersion())) {
            stringBuilder.append("\n[Version=").append(routerEntity.getVersion()).append("]");
        }
        if (StringUtils.isNotEmpty(routerEntity.getRegion())) {
            stringBuilder.append("\n[Region=").append(routerEntity.getRegion()).append("]");
        }
        if (StringUtils.isNotEmpty(routerEntity.getEnvironment())) {
            stringBuilder.append("\n[Env=").append(routerEntity.getEnvironment()).append("]");
        }
        if (StringUtils.isNotEmpty(routerEntity.getZone())) {
            stringBuilder.append("\n[Zone=").append(routerEntity.getZone()).append("]");
        }

        return ButtonManager.getHtmlText(stringBuilder.toString());
    }

    private TNode addNode(Instance instance) {
        String nodeName = getNodeName(instance);

        TNode node = createNode(nodeName, nodeUI, nodeLocation, 0);
        node.setUserObject(instance);

        dataBox.addElement(node);

        return node;
    }

    private TNode addNode(RouterEntity routerEntity, int index) {
        String nodeName = getNodeName(routerEntity);

        TNode node = createNode(nodeName, nodeUI, nodeLocation, index);
        node.setUserObject(routerEntity);

        dataBox.addElement(node);

        return node;
    }

    @SuppressWarnings("unchecked")
    private void addLink(TNode fromNode, TNode toNode, RouterEntity routerEntity) {
        List<TLink> links = TElementManager.getLinks(dataBox);
        for (TLink link : links) {
            if (link.getFrom() == fromNode && link.getTo() == toNode) {
                return;
            }
        }

        int weight = routerEntity.getWeight();

        TLink link = createLink(fromNode, toNode, true);
        link.putLinkToArrowColor(Color.yellow);
        if (weight > -1) {
            link.setName("weight=" + weight);
            link.putLinkFlowing(true);
            link.putLinkFlowingColor(new Color(255, 155, 85));
            link.putLinkFlowingWidth(3);
        }

        dataBox.addElement(link);
    }

    @SuppressWarnings({ "unchecked" })
    public void setServices(Object[] services) {
        comboBox.setModel(new DefaultComboBoxModel<>(services));
    }

    public void setInstance(Instance instance) {
        if (this.instance != instance) {
            this.instance = instance;

            textField.setText(StringUtils.EMPTY);
            dataBox.clear();

            addNode(instance);
        }
    }

    private JSecurityAction createAddServiceAction() {
        JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("add.png"), ConsoleLocaleFactory.getString("add_service")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                Object selectedItem = comboBox.getSelectedItem();
                if (selectedItem == null) {
                    return;
                }

                String routerPath = textField.getText();
                String serviceId = selectedItem.toString();
                if (StringUtils.isNotEmpty(routerPath)) {
                    routerPath = routerPath + ";" + serviceId;
                } else {
                    routerPath = serviceId;
                }
                textField.setText(routerPath);
            }
        };

        return action;
    }

    private JSecurityAction createDeleteServiceAction() {
        JSecurityAction action = new JSecurityAction(ConsoleIconFactory.getSwingIcon("delete.png"), ConsoleLocaleFactory.getString("delete_service")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                String routerPath = textField.getText();
                if (StringUtils.isEmpty(routerPath)) {
                    return;
                }

                if (routerPath.contains(";")) {
                    routerPath = routerPath.substring(0, routerPath.lastIndexOf(";"));
                } else {
                    routerPath = StringUtils.EMPTY;
                }

                textField.setText(routerPath);
            }
        };

        return action;
    }

    private JSecurityAction createExecuteRouterAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("execute_router"), ConsoleIconFactory.getSwingIcon("netbean/action_16.png"), ConsoleLocaleFactory.getString("execute_router")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                String routerPath = textField.getText();
                if (StringUtils.isEmpty(routerPath)) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(RouterTopology.this), ConsoleLocaleFactory.getString("router_path_invalid"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                RouterEntity routerEntity = null;
                try {
                    routerEntity = ServiceController.routes(instance, routerPath);
                } catch (Exception ex) {
                    JExceptionDialog.traceException(HandleManager.getFrame(RouterTopology.this), ConsoleLocaleFactory.getString("query_data_failure"), ex);

                    return;
                }

                route(routerEntity);

                layoutActionListener.actionPerformed(null);
            }
        };

        return action;
    }

    private JSecurityAction createClearRouterAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("clear_router"), ConsoleIconFactory.getSwingIcon("paint.png"), ConsoleLocaleFactory.getString("clear_router")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                textField.setText(StringUtils.EMPTY);
                dataBox.clear();
            }
        };

        return action;
    }

    private JSecurityAction createShowRuleAction() {
        JSecurityAction action = new JSecurityAction(ConsoleLocaleFactory.getString("view_rule"), ConsoleIconFactory.getSwingIcon("component/file_chooser_16.png"), ConsoleLocaleFactory.getString("view_rule")) {
            private static final long serialVersionUID = 1L;

            public void execute(ActionEvent e) {
                TNode node = TElementManager.getSelectedNode(dataBox);
                if (node == null) {
                    JBasicOptionPane.showMessageDialog(HandleManager.getFrame(RouterTopology.this), ConsoleLocaleFactory.getString("select_a_node"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);

                    return;
                }

                Object userObject = node.getUserObject();
                List<String> rules = null;
                if (userObject instanceof Instance) {
                    Instance instance = (Instance) userObject;
                    rules = ServiceController.getRules(instance);
                } else if (userObject instanceof RouterEntity) {
                    RouterEntity routerEntity = (RouterEntity) userObject;
                    rules = ServiceController.getRules(routerEntity);
                }
                String dynamicPartialRule = rules.get(2);
                String dynamicGlobalRule = rules.get(1);
                String localRule = rules.get(0);

                if (rulePanel == null) {
                    rulePanel = new RulePanel();
                    rulePanel.setPreferredSize(new Dimension(800, 600));
                }

                rulePanel.setDynamicPartialRule(dynamicPartialRule);
                rulePanel.setDynamicGlobalRule(dynamicGlobalRule);
                rulePanel.setLocalRule(localRule);

                JBasicOptionPane.showOptionDialog(HandleManager.getFrame(RouterTopology.this), rulePanel, ConsoleLocaleFactory.getString("view_rule"), JBasicOptionPane.DEFAULT_OPTION, JBasicOptionPane.PLAIN_MESSAGE, ConsoleIconFactory.getSwingIcon("banner/property.png"), new Object[] { SwingLocale.getString("close") }, null, true);
            }
        };

        return action;
    }

    private class RulePanel extends JPanel {
        private static final long serialVersionUID = 1L;

        private JBasicTextArea dynamicPartialRuleTextArea;
        private JPanel dynamicPartialRulePanel;
        private JBasicTextArea dynamicGlobalRuleTextArea;
        private JPanel dynamicGlobalRulePanel;
        private JBasicTextArea localRuleTextArea;
        private JPanel localRulePanel;
        private JBasicTabbedPane ruleTabbedPane;

        public RulePanel() {
            setLayout(new BorderLayout());
            add(createRuleTabbedPane(), BorderLayout.CENTER);
        }

        private JBasicTabbedPane createRuleTabbedPane() {
            dynamicPartialRuleTextArea = new JBasicTextArea();
            dynamicPartialRulePanel = new JPanel();
            dynamicPartialRulePanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            dynamicPartialRulePanel.setLayout(new BorderLayout());
            dynamicPartialRulePanel.add(new JBasicScrollPane(dynamicPartialRuleTextArea), BorderLayout.CENTER);

            dynamicGlobalRuleTextArea = new JBasicTextArea();
            dynamicGlobalRulePanel = new JPanel();
            dynamicGlobalRulePanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            dynamicGlobalRulePanel.setLayout(new BorderLayout());
            dynamicGlobalRulePanel.add(new JBasicScrollPane(dynamicGlobalRuleTextArea), BorderLayout.CENTER);

            localRuleTextArea = new JBasicTextArea();
            localRulePanel = new JPanel();
            localRulePanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            localRulePanel.setLayout(new BorderLayout());
            localRulePanel.add(new JBasicScrollPane(localRuleTextArea), BorderLayout.CENTER);

            ruleTabbedPane = new JBasicTabbedPane();
            ruleTabbedPane.addTab(ConsoleLocaleFactory.getString("label_dynamic_partial_rule"), dynamicPartialRulePanel, ConsoleLocaleFactory.getString("label_dynamic_partial_rule"));
            ruleTabbedPane.addTab(ConsoleLocaleFactory.getString("label_dynamic_global_rule"), dynamicGlobalRulePanel, ConsoleLocaleFactory.getString("label_dynamic_global_rule"));
            ruleTabbedPane.addTab(ConsoleLocaleFactory.getString("label_local_rule"), localRulePanel, ConsoleLocaleFactory.getString("label_local_rule"));

            return ruleTabbedPane;
        }

        public void setDynamicPartialRule(String dynamicPartialRule) {
            dynamicPartialRuleTextArea.setText(dynamicPartialRule);
        }

        public void setDynamicGlobalRule(String dynamicGlobalRule) {
            dynamicGlobalRuleTextArea.setText(dynamicGlobalRule);
        }

        public void setLocalRule(String localRule) {
            localRuleTextArea.setText(localRule);
        }
    }
}