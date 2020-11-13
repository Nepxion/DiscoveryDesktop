package com.nepxion.discovery.console.desktop;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import com.nepxion.discovery.console.desktop.component.BasicConsoleHierarchy;
import com.nepxion.discovery.console.desktop.context.UIContext;
import com.nepxion.discovery.console.desktop.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.workspace.type.ReleaseType;
import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.list.toggle.JToggleList;
import com.nepxion.swing.shrinkbar.JShrinkOutlook;
import com.nepxion.swing.style.texture.shrink.IHeaderTextureStyle;
import com.nepxion.swing.style.texture.shrink.IOutlookTextureStyle;

public class ConsoleHierarchy extends BasicConsoleHierarchy {
    private static final long serialVersionUID = 1L;

    public ConsoleHierarchy(IHeaderTextureStyle headerTextureStyle, IOutlookTextureStyle outlookTextureStyle) {
        super(headerTextureStyle, outlookTextureStyle);

        createReleaseManageShrinkOutlook();
        createBlacklistManageShrinkOutlook();

        shrinkOutlookBar.getShrinkOutlook(0).setSelected(true);
    }

    private JShrinkOutlook createReleaseManageShrinkOutlook() {
        List<ElementNode> elementNodes = new ArrayList<ElementNode>();
        elementNodes.add(new ElementNode(ReleaseType.BLUE_GREEN.toString(), ReleaseType.BLUE_GREEN.getDescription(), ConsoleIconFactory.getSwingIcon("component/ui_16.png"), ReleaseType.BLUE_GREEN.getDescription()));
        elementNodes.add(new ElementNode(ReleaseType.GRAY.toString(), ReleaseType.GRAY.getDescription(), ConsoleIconFactory.getSwingIcon("component/ui_16.png"), ReleaseType.GRAY.getDescription()));

        JToggleList list = createList(elementNodes);
        list.setSelectedIndex(0);

        JShrinkOutlook shrinkOutlook = shrinkOutlookBar.addShrinkOutlook(ConsoleLocaleFactory.getString("release_manage"), ConsoleIconFactory.getSwingIcon("stereo/favorite_16.png"), ConsoleIconFactory.getSwingIcon("stereo/favorite_add_16.png"), ConsoleLocaleFactory.getString("release_manage"), new Font(UIContext.getFontName(), Font.BOLD, UIContext.getMiddleFontSize()));
        shrinkOutlook.setContentPane(list);
        shrinkOutlook.addPropertyChangeListener(new OutlookSelectionListener());

        return shrinkOutlook;
    }

    private JShrinkOutlook createBlacklistManageShrinkOutlook() {
        List<ElementNode> elementNodes = new ArrayList<ElementNode>();
        elementNodes.add(new ElementNode(ReleaseType.BLACKLIST.toString(), ReleaseType.BLACKLIST.getDescription(), ConsoleIconFactory.getSwingIcon("component/ui_16.png"), ReleaseType.BLACKLIST.getDescription()));

        JToggleList list = createList(elementNodes);

        JShrinkOutlook shrinkOutlook = shrinkOutlookBar.addShrinkOutlook(ConsoleLocaleFactory.getString("blacklist_manage"), ConsoleIconFactory.getSwingIcon("stereo/favorite_16.png"), ConsoleIconFactory.getSwingIcon("stereo/favorite_add_16.png"), ConsoleLocaleFactory.getString("blacklist_manage"), new Font(UIContext.getFontName(), Font.BOLD, UIContext.getMiddleFontSize()));
        shrinkOutlook.setContentPane(list);
        shrinkOutlook.addPropertyChangeListener(new OutlookSelectionListener());

        return shrinkOutlook;
    }
}