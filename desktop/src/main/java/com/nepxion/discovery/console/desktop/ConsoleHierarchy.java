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

import com.nepxion.discovery.console.desktop.common.component.AbstractConsoleHierarchy;
import com.nepxion.discovery.console.desktop.common.context.ConsoleUIContext;
import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.workspace.BlueGreenTopology;
import com.nepxion.discovery.console.desktop.workspace.GrayTopology;
import com.nepxion.discovery.console.desktop.workspace.type.ReleaseType;
import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.list.JBasicList;
import com.nepxion.swing.shrinkbar.JShrinkOutlook;

public class ConsoleHierarchy extends AbstractConsoleHierarchy {
    private static final long serialVersionUID = 1L;

    @Override
    public void initializeUI() {
        createReleaseManageShrinkOutlook();
        createBlacklistManageShrinkOutlook();
    }

    private JShrinkOutlook createReleaseManageShrinkOutlook() {
        List<ElementNode> elementNodes = new ArrayList<ElementNode>();
        elementNodes.add(new ElementNode(ReleaseType.BLUE_GREEN.toString(), ReleaseType.BLUE_GREEN.getDescription(), ConsoleIconFactory.getSwingIcon("component/ui_16.png"), ReleaseType.BLUE_GREEN.getDescription(), new BlueGreenTopology()));
        elementNodes.add(new ElementNode(ReleaseType.GRAY.toString(), ReleaseType.GRAY.getDescription(), ConsoleIconFactory.getSwingIcon("component/ui_16.png"), ReleaseType.GRAY.getDescription(), new GrayTopology()));

        JBasicList toggleList = createToggleList(elementNodes);
        toggleList.setSelectedIndex(0);

        JShrinkOutlook shrinkOutlook = shrinkOutlookBar.addShrinkOutlook(ConsoleLocaleFactory.getString("release_manage"), ConsoleIconFactory.getSwingIcon("stereo/favorite_16.png"), ConsoleIconFactory.getSwingIcon("stereo/favorite_add_16.png"), ConsoleLocaleFactory.getString("release_manage"), new Font(ConsoleUIContext.getFontName(), Font.BOLD, ConsoleUIContext.getMiddleFontSize()));
        shrinkOutlook.setContentPane(toggleList);
        shrinkOutlook.addPropertyChangeListener(new OutlookSelectionListener());

        return shrinkOutlook;
    }

    private JShrinkOutlook createBlacklistManageShrinkOutlook() {
        List<ElementNode> elementNodes = new ArrayList<ElementNode>();
        elementNodes.add(new ElementNode(ReleaseType.BLACKLIST.toString(), ReleaseType.BLACKLIST.getDescription(), ConsoleIconFactory.getSwingIcon("component/ui_16.png"), ReleaseType.BLACKLIST.getDescription()));

        JBasicList toggleList = createToggleList(elementNodes);

        JShrinkOutlook shrinkOutlook = shrinkOutlookBar.addShrinkOutlook(ConsoleLocaleFactory.getString("blacklist_manage"), ConsoleIconFactory.getSwingIcon("stereo/favorite_16.png"), ConsoleIconFactory.getSwingIcon("stereo/favorite_add_16.png"), ConsoleLocaleFactory.getString("blacklist_manage"), new Font(ConsoleUIContext.getFontName(), Font.BOLD, ConsoleUIContext.getMiddleFontSize()));
        shrinkOutlook.setContentPane(toggleList);
        shrinkOutlook.addPropertyChangeListener(new OutlookSelectionListener());

        return shrinkOutlook;
    }
}