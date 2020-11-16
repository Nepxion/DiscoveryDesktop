package com.nepxion.discovery.console.desktop.common.component;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.nepxion.discovery.console.desktop.common.context.ConsoleUIContext;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.util.DimensionUtil;
import com.nepxion.swing.container.ContainerManager;
import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.framework.reflection.JReflectionHierarchy;
import com.nepxion.swing.list.JBasicList;
import com.nepxion.swing.shrinkbar.JShrinkBar;
import com.nepxion.swing.shrinkbar.JShrinkOutlook;
import com.nepxion.swing.shrinkbar.JShrinkOutlookBar;
import com.nepxion.swing.shrinkbar.ShrinkListCellRenderer;
import com.nepxion.swing.shrinkbar.ShrinkOutlookSelectionListener;
import com.nepxion.swing.style.texture.shrink.IHeaderTextureStyle;
import com.nepxion.swing.style.texture.shrink.IOutlookTextureStyle;
import com.nepxion.swing.style.texture.shrink.JBlackHeaderTextureStyle;
import com.nepxion.swing.style.texture.shrink.JGreenOutlookTextureStyle;
import com.nepxion.util.data.CollectionUtil;

public abstract class AbstractConsoleHierarchy extends JReflectionHierarchy {
    private static final long serialVersionUID = 1L;

    protected JShrinkBar shrinkOperationBar;
    protected JShrinkBar shrinkContentBar;
    protected JShrinkOutlookBar shrinkOutlookBar;

    public AbstractConsoleHierarchy() {
        super(20, 20);

        IHeaderTextureStyle headerTextureStyle = new JBlackHeaderTextureStyle();
        IOutlookTextureStyle outlookTextureStyle = new JGreenOutlookTextureStyle();

        shrinkOperationBar = new JShrinkBar(JShrinkBar.PLACEMENT_EAST, JShrinkBar.CONTENT_PANE_TYPE_LABEL, headerTextureStyle) {
            private static final long serialVersionUID = 1L;

            private JPanel container;
            private Component contentPane;

            @Override
            public Component getContentPane() {
                return contentPane;
            }

            @Override
            public void setContentPane(Component contentPane) {
                this.contentPane = contentPane;

                if (container == null) {
                    container = new JPanel();
                    container.setLayout(new BorderLayout());
                    // container.setBorder(BorderFactory.createLineBorder(outlookTextureStyle.getBorderColor()));

                    shrinkContentPane.add(container, BorderLayout.CENTER);
                }

                container.removeAll();
                container.add(contentPane, BorderLayout.CENTER);

                ContainerManager.update(container);
            }
        };
        shrinkOperationBar.setTitle(ConsoleLocaleFactory.getString("operation_bar"));
        shrinkOperationBar.setToolTipText(ConsoleLocaleFactory.getString("operation_bar"));
        shrinkOperationBar.setIcon(ConsoleIconFactory.getSwingIcon("property.png"));
        shrinkOperationBar.setTitleFont(new Font(ConsoleUIContext.getFontName(), Font.BOLD, ConsoleUIContext.getLargeFontSize()));
        DimensionUtil.setWidth(shrinkOperationBar, getOperationBar());

        shrinkContentBar = new JShrinkBar(JShrinkBar.PLACEMENT_EAST, JShrinkBar.CONTENT_PANE_TYPE_LABEL, headerTextureStyle);
        shrinkContentBar.setShrinkable(false);
        shrinkContentBar.setTitle(ConsoleLocaleFactory.getString("content_bar"));
        shrinkContentBar.setToolTipText(ConsoleLocaleFactory.getString("content_bar"));
        shrinkContentBar.setIcon(ConsoleIconFactory.getSwingIcon("paste.png"));
        shrinkContentBar.setTitleFont(new Font(ConsoleUIContext.getFontName(), Font.BOLD, ConsoleUIContext.getLargeFontSize()));
        shrinkContentBar.getShrinkHeader().getLabel().addMouseListener(new ShrinkContentBarMouseListener());

        shrinkOutlookBar = new JShrinkOutlookBar(JShrinkBar.PLACEMENT_WEST, JShrinkBar.CONTENT_PANE_TYPE_LABEL, headerTextureStyle, outlookTextureStyle);
        shrinkOutlookBar.setTitle(ConsoleLocaleFactory.getString("navigator_bar"));
        shrinkOutlookBar.setToolTipText(ConsoleLocaleFactory.getString("navigator_bar"));
        shrinkOutlookBar.setIcon(ConsoleIconFactory.getSwingIcon("hierarchy.png"));
        shrinkOutlookBar.setTitleFont(new Font(ConsoleUIContext.getFontName(), Font.BOLD, ConsoleUIContext.getLargeFontSize()));
        DimensionUtil.setWidth(shrinkOutlookBar, getOutlookBarWidth());

        initializeUI();

        shrinkOutlookBar.getShrinkOutlook(0).setSelected(true);

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout(5, 5));
        container.add(shrinkOperationBar, BorderLayout.EAST);
        container.add(shrinkContentBar, BorderLayout.CENTER);
        container.add(shrinkOutlookBar, BorderLayout.WEST);

        setContentPane(container);
    }

    @SuppressWarnings("unchecked")
    public JBasicList createToggleList(List<ElementNode> elementNodes) {
        JBasicList toggleList = new JBasicList(CollectionUtil.parseVector(elementNodes)) {
            private static final long serialVersionUID = 1L;

            @Override
            public void executeSelection(int oldSelectedRow, int newSelectedRow) {
                if (newSelectedRow == -1) {
                    return;
                }

                ElementNode elementNode = (ElementNode) getModel().getElementAt(newSelectedRow);
                toggleListSelected(elementNode);
            }
        };
        toggleList.setSelectionMode(JBasicList.SINGLE_SELECTION);
        toggleList.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        toggleList.setCellRenderer(new ShrinkListCellRenderer(toggleList, BorderFactory.createEmptyBorder(0, 10, 0, 0), 22));

        return toggleList;
    }

    public class ShrinkContentBarMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() > 1) {
                boolean isShrinked = !shrinkOutlookBar.isShrinked();
                shrinkOutlookBar.setShrinked(isShrinked);
                shrinkOperationBar.setShrinked(isShrinked);
            }
        }
    }

    public class OutlookSelectionListener extends ShrinkOutlookSelectionListener {
        public void selectionStateChanged(JShrinkOutlook shrinkOutlook) {
            JBasicList toggleList = (JBasicList) shrinkOutlook.getContentPane();
            toggleList.executeSelection(-1, toggleList.getSelectedIndex());
        }
    }

    public abstract void initializeUI();

    public abstract void toggleListSelected(ElementNode elementNode);

    public abstract int getOutlookBarWidth();

    public abstract int getOperationBar();
}