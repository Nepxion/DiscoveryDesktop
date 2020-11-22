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
import com.nepxion.discovery.console.desktop.workspace.AbstractTopology;
import com.nepxion.discovery.console.desktop.workspace.BlueGreenTopology;
import com.nepxion.discovery.console.desktop.workspace.GrayTopology;
import com.nepxion.discovery.console.desktop.workspace.InspectorTopology;
import com.nepxion.discovery.console.desktop.workspace.type.FeatureType;
import com.nepxion.discovery.console.desktop.workspace.type.ReleaseType;
import com.nepxion.discovery.console.desktop.workspace.type.TypeLocale;
import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.list.JBasicList;
import com.nepxion.swing.shrinkbar.JShrinkOutlook;

public class ConsoleHierarchy extends AbstractConsoleHierarchy {
    private static final long serialVersionUID = 1L;

    @Override
    public void initializeUI() {
        createServiceReleaseManageShrinkOutlook();
        createInstanceBlacklistManageShrinkOutlook();
        createMiddleWareReleaseManageShrinkOutlook();
    }

    private JShrinkOutlook createServiceReleaseManageShrinkOutlook() {
        List<ElementNode> elementNodes = new ArrayList<ElementNode>();
        elementNodes.add(new ElementNode(ReleaseType.BLUE_GREEN.toString(), TypeLocale.getDescription(ReleaseType.BLUE_GREEN), ConsoleIconFactory.getSwingIcon("component/ui_16.png"), TypeLocale.getDescription(ReleaseType.BLUE_GREEN), new BlueGreenTopology()));
        elementNodes.add(new ElementNode(ReleaseType.GRAY.toString(), TypeLocale.getDescription(ReleaseType.GRAY), ConsoleIconFactory.getSwingIcon("component/ui_16.png"), TypeLocale.getDescription(ReleaseType.GRAY), new GrayTopology()));
        elementNodes.add(new ElementNode(FeatureType.INSPECT.toString(), TypeLocale.getDescription(FeatureType.INSPECT), ConsoleIconFactory.getSwingIcon("component/ui_16.png"), TypeLocale.getDescription(FeatureType.INSPECT), new InspectorTopology()));

        JBasicList toggleList = createToggleList(elementNodes);
        toggleList.setSelectedIndex(0);

        JShrinkOutlook shrinkOutlook = shrinkOutlookBar.addShrinkOutlook(ConsoleLocaleFactory.getString("service_release_manage"), ConsoleIconFactory.getSwingIcon("stereo/favorite_16.png"), ConsoleIconFactory.getSwingIcon("stereo/favorite_add_16.png"), ConsoleLocaleFactory.getString("service_release_manage"), new Font(ConsoleUIContext.getFontName(), Font.BOLD, ConsoleUIContext.getMiddleFontSize()));
        shrinkOutlook.setContentPane(toggleList);
        shrinkOutlook.addPropertyChangeListener(new OutlookSelectionListener());

        return shrinkOutlook;
    }

    private JShrinkOutlook createInstanceBlacklistManageShrinkOutlook() {
        List<ElementNode> elementNodes = new ArrayList<ElementNode>();
        elementNodes.add(new ElementNode(ReleaseType.BLACKLIST.toString(), TypeLocale.getDescription(ReleaseType.BLACKLIST), ConsoleIconFactory.getSwingIcon("component/ui_16.png"), TypeLocale.getDescription(ReleaseType.BLACKLIST), null));

        JBasicList toggleList = createToggleList(elementNodes);

        JShrinkOutlook shrinkOutlook = shrinkOutlookBar.addShrinkOutlook(ConsoleLocaleFactory.getString("instance_blacklist_manage"), ConsoleIconFactory.getSwingIcon("stereo/favorite_16.png"), ConsoleIconFactory.getSwingIcon("stereo/favorite_add_16.png"), ConsoleLocaleFactory.getString("instance_blacklist_manage"), new Font(ConsoleUIContext.getFontName(), Font.BOLD, ConsoleUIContext.getMiddleFontSize()));
        shrinkOutlook.setContentPane(toggleList);
        shrinkOutlook.addPropertyChangeListener(new OutlookSelectionListener());

        return shrinkOutlook;
    }

    private JShrinkOutlook createMiddleWareReleaseManageShrinkOutlook() {
        List<ElementNode> elementNodes = new ArrayList<ElementNode>();
        elementNodes.add(new ElementNode(ReleaseType.MIDDLEWARE_BLUE_GREEN.toString(), TypeLocale.getDescription(ReleaseType.MIDDLEWARE_BLUE_GREEN), ConsoleIconFactory.getSwingIcon("component/ui_16.png"), TypeLocale.getDescription(ReleaseType.MIDDLEWARE_BLUE_GREEN), null));

        JBasicList toggleList = createToggleList(elementNodes);

        JShrinkOutlook shrinkOutlook = shrinkOutlookBar.addShrinkOutlook(ConsoleLocaleFactory.getString("middleware_release_manage"), ConsoleIconFactory.getSwingIcon("stereo/favorite_16.png"), ConsoleIconFactory.getSwingIcon("stereo/favorite_add_16.png"), ConsoleLocaleFactory.getString("middleware_release_manage"), new Font(ConsoleUIContext.getFontName(), Font.BOLD, ConsoleUIContext.getMiddleFontSize()));
        shrinkOutlook.setContentPane(toggleList);
        shrinkOutlook.addPropertyChangeListener(new OutlookSelectionListener());

        return shrinkOutlook;
    }

    @Override
    public void toggleListSelected(ElementNode elementNode) {
        Object userObject = elementNode.getUserObject();
        if (userObject == null) {
            return;
        }

        if (userObject instanceof AbstractTopology) {
            AbstractTopology topology = (AbstractTopology) userObject;

            shrinkContentBar.setContentPane(topology);
            shrinkOperationBar.setContentPane(topology.getOperationBar());
        }
    }

    @Override
    public int getOutlookBarWidth() {
        return 200;
    }

    @Override
    public int getOperationBar() {
        return 400;
    }
}