package com.nepxion.discovery.console.desktop.component;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.nepxion.discovery.console.desktop.context.UIContext;
import com.nepxion.discovery.console.desktop.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.toggle.ConsoleToggleListener;
import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.list.toggle.JToggleList;
import com.nepxion.swing.shrinkbar.JShrinkBar;
import com.nepxion.swing.shrinkbar.JShrinkOutlook;
import com.nepxion.swing.shrinkbar.JShrinkOutlookBar;
import com.nepxion.swing.shrinkbar.ShrinkListCellRenderer;
import com.nepxion.swing.shrinkbar.ShrinkOutlookSelectionListener;
import com.nepxion.swing.style.texture.shrink.IHeaderTextureStyle;
import com.nepxion.swing.style.texture.shrink.IOutlookTextureStyle;
import com.nepxion.util.data.CollectionUtil;

public class BasicConsoleHierarchy extends JPanel {
    private static final long serialVersionUID = 1L;

    protected JShrinkBar shrinkContentBar;
    protected JShrinkOutlookBar shrinkOutlookBar;

    public BasicConsoleHierarchy(IHeaderTextureStyle headerTextureStyle, IOutlookTextureStyle outlookTextureStyle) {
        shrinkContentBar = new JShrinkBar(JShrinkBar.PLACEMENT_EAST, JShrinkBar.CONTENT_PANE_TYPE_LABEL, headerTextureStyle);
        shrinkContentBar.setShrinkable(false);
        shrinkContentBar.setTitle(ConsoleLocaleFactory.getString("content_bar"));
        shrinkContentBar.setToolTipText(ConsoleLocaleFactory.getString("content_bar"));
        shrinkContentBar.setIcon(ConsoleIconFactory.getSwingIcon("paste.png"));
        shrinkContentBar.setTitleFont(new Font(UIContext.getFontName(), Font.BOLD, UIContext.getLargeFontSize()));
        shrinkContentBar.getShrinkHeader().getLabel().addMouseListener(new ShrinkContentBarMouseListener());

        shrinkOutlookBar = new JShrinkOutlookBar(JShrinkBar.PLACEMENT_WEST, JShrinkBar.CONTENT_PANE_TYPE_LABEL, headerTextureStyle, outlookTextureStyle);
        shrinkOutlookBar.setTitle(ConsoleLocaleFactory.getString("navigator_bar"));
        shrinkOutlookBar.setToolTipText(ConsoleLocaleFactory.getString("navigator_bar"));
        shrinkOutlookBar.setIcon(ConsoleIconFactory.getSwingIcon("hierarchy.png"));
        shrinkOutlookBar.setTitleFont(new Font(UIContext.getFontName(), Font.BOLD, UIContext.getLargeFontSize()));
        shrinkOutlookBar.setPreferredSize(new Dimension(210, shrinkOutlookBar.getPreferredSize().height));

        setLayout(new BorderLayout(5, 5));
        add(shrinkContentBar, BorderLayout.CENTER);
        add(shrinkOutlookBar, BorderLayout.WEST);
    }

    @SuppressWarnings("unchecked")
    public JToggleList createList(List<ElementNode> elementNodes) {
        JToggleList list = new JToggleList(CollectionUtil.parseVector(elementNodes));
        list.setSelectionMode(JToggleList.SINGLE_SELECTION);
        list.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        list.setCellRenderer(new ShrinkListCellRenderer(list, BorderFactory.createEmptyBorder(0, 10, 0, 0), 22));
        list.setToggleContentPanel(shrinkContentBar);
        list.setToggleAdapter(new ConsoleToggleListener(list));

        return list;
    }

    public class ShrinkContentBarMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() > 1) {
                boolean isShrinked = !shrinkOutlookBar.isShrinked();
                shrinkOutlookBar.setShrinked(isShrinked);
            }
        }
    }

    public class OutlookSelectionListener extends ShrinkOutlookSelectionListener {
        public void selectionStateChanged(JShrinkOutlook shrinkOutlook) {
            JToggleList toggleList = (JToggleList) shrinkOutlook.getContentPane();
            toggleList.executeSelection(-1, toggleList.getSelectedIndex());
        }
    }
}
