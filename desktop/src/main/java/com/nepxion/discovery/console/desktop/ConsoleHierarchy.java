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
import com.nepxion.discovery.console.desktop.workspace.type.AuthorityType;
import com.nepxion.discovery.console.desktop.workspace.type.FeatureType;
import com.nepxion.discovery.console.desktop.workspace.type.ReleaseType;
import com.nepxion.discovery.console.desktop.workspace.type.TypeLocale;
import com.nepxion.swing.element.ElementNode;
import com.nepxion.swing.list.JBasicList;
import com.nepxion.swing.shrinkbar.JShrinkOutlook;

public class ConsoleHierarchy extends AbstractConsoleHierarchy {
    private static final long serialVersionUID = 1L;

    public static final String OUTLOOK_STYLE = "outlook/stereo/";
    public static final String NAVIGATOR_STYLE = "navigator/stereo/";

    @Override
    public void initializeUI() {
        createServiceReleaseManageShrinkOutlook();
        createServiceAuthorityManageShrinkOutlook();
        createMiddlewareReleaseManageShrinkOutlook();
    }

    private JShrinkOutlook createServiceReleaseManageShrinkOutlook() {
        List<ElementNode> elementNodes = new ArrayList<ElementNode>();
        elementNodes.add(new ElementNode(ReleaseType.BLUE_GREEN.toString(), TypeLocale.getDescription(ReleaseType.BLUE_GREEN), ConsoleIconFactory.getContextIcon(OUTLOOK_STYLE + "blue_green.png"), TypeLocale.getDescription(ReleaseType.BLUE_GREEN), new BlueGreenTopology()));
        elementNodes.add(new ElementNode(ReleaseType.GRAY.toString(), TypeLocale.getDescription(ReleaseType.GRAY), ConsoleIconFactory.getContextIcon(OUTLOOK_STYLE + "gray.png"), TypeLocale.getDescription(ReleaseType.GRAY), new GrayTopology()));
        elementNodes.add(new ElementNode(FeatureType.INSPECTOR.toString(), TypeLocale.getDescription(FeatureType.INSPECTOR), ConsoleIconFactory.getContextIcon(OUTLOOK_STYLE + "inspector.png"), TypeLocale.getDescription(FeatureType.INSPECTOR), new InspectorTopology()));

        JBasicList toggleList = createToggleList(elementNodes);
        toggleList.setSelectedIndex(0);

        JShrinkOutlook shrinkOutlook = shrinkOutlookBar.addShrinkOutlook(ConsoleLocaleFactory.getString("service_release_manage"), ConsoleIconFactory.getContextIcon(NAVIGATOR_STYLE + "close_16.png"), ConsoleIconFactory.getContextIcon(NAVIGATOR_STYLE + "open_favorite_16.png"), ConsoleLocaleFactory.getString("service_release_manage"), new Font(ConsoleUIContext.getFontName(), Font.BOLD, ConsoleUIContext.getMiddleFontSize()));
        shrinkOutlook.setContentPane(toggleList);
        shrinkOutlook.addPropertyChangeListener(new OutlookSelectionListener());

        return shrinkOutlook;
    }

    private JShrinkOutlook createServiceAuthorityManageShrinkOutlook() {
        List<ElementNode> elementNodes = new ArrayList<ElementNode>();
        elementNodes.add(new ElementNode(AuthorityType.STRATEGY.toString(), TypeLocale.getDescription(AuthorityType.STRATEGY), ConsoleIconFactory.getContextIcon(OUTLOOK_STYLE + "authority_strategy.png"), TypeLocale.getDescription(AuthorityType.STRATEGY), null));
        elementNodes.add(new ElementNode(AuthorityType.DISCOVERY.toString(), TypeLocale.getDescription(AuthorityType.DISCOVERY), ConsoleIconFactory.getContextIcon(OUTLOOK_STYLE + "authority_discovery.png"), TypeLocale.getDescription(AuthorityType.DISCOVERY), null));
        elementNodes.add(new ElementNode(AuthorityType.REGISTER.toString(), TypeLocale.getDescription(AuthorityType.REGISTER), ConsoleIconFactory.getContextIcon(OUTLOOK_STYLE + "authority_register.png"), TypeLocale.getDescription(AuthorityType.REGISTER), null));

        JBasicList toggleList = createToggleList(elementNodes);

        JShrinkOutlook shrinkOutlook = shrinkOutlookBar.addShrinkOutlook(ConsoleLocaleFactory.getString("service_authority_manage"), ConsoleIconFactory.getContextIcon(NAVIGATOR_STYLE + "close_16.png"), ConsoleIconFactory.getContextIcon(NAVIGATOR_STYLE + "open_favorite_16.png"), ConsoleLocaleFactory.getString("service_authority_manage"), new Font(ConsoleUIContext.getFontName(), Font.BOLD, ConsoleUIContext.getMiddleFontSize()));
        shrinkOutlook.setContentPane(toggleList);
        shrinkOutlook.addPropertyChangeListener(new OutlookSelectionListener());

        return shrinkOutlook;
    }

    private JShrinkOutlook createMiddlewareReleaseManageShrinkOutlook() {
        List<ElementNode> elementNodes = new ArrayList<ElementNode>();
        elementNodes.add(new ElementNode(ReleaseType.DATABASE_BLUE_GREEN.toString(), TypeLocale.getDescription(ReleaseType.DATABASE_BLUE_GREEN), ConsoleIconFactory.getContextIcon(OUTLOOK_STYLE + "database_blue_green.png"), TypeLocale.getDescription(ReleaseType.DATABASE_BLUE_GREEN), null));
        elementNodes.add(new ElementNode(ReleaseType.MESSAGE_QUEUE_BLUE_GREEN.toString(), TypeLocale.getDescription(ReleaseType.MESSAGE_QUEUE_BLUE_GREEN), ConsoleIconFactory.getContextIcon(OUTLOOK_STYLE + "message_queue_blue_green.png"), TypeLocale.getDescription(ReleaseType.MESSAGE_QUEUE_BLUE_GREEN), null));

        JBasicList toggleList = createToggleList(elementNodes);

        JShrinkOutlook shrinkOutlook = shrinkOutlookBar.addShrinkOutlook(ConsoleLocaleFactory.getString("middleware_release_manage"), ConsoleIconFactory.getContextIcon(NAVIGATOR_STYLE + "close_16.png"), ConsoleIconFactory.getContextIcon(NAVIGATOR_STYLE + "open_favorite_16.png"), ConsoleLocaleFactory.getString("middleware_release_manage"), new Font(ConsoleUIContext.getFontName(), Font.BOLD, ConsoleUIContext.getMiddleFontSize()));
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
        return 250;
    }

    @Override
    public int getOperationBar() {
        return 400;
    }
}