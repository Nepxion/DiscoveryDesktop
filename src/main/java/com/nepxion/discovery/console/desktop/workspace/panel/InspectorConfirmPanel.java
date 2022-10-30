package com.nepxion.discovery.console.desktop.workspace.panel;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;

import com.nepxion.discovery.console.desktop.common.icon.ConsoleIconFactory;
import com.nepxion.discovery.console.desktop.common.locale.ConsoleLocaleFactory;
import com.nepxion.discovery.console.desktop.common.util.ListUtil;
import com.nepxion.swing.icon.IconFactory;
import com.nepxion.swing.layout.table.TableLayout;
import com.nepxion.swing.list.JBasicList;
import com.nepxion.swing.scrollpane.JBasicScrollPane;
import com.nepxion.swing.shrinkbar.JShrinkShortcut;
import com.nepxion.swing.textfield.JBasicTextField;

public class InspectorConfirmPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    protected JBasicTextField urlTextField;
    protected JBasicList serviceIdList;

    public InspectorConfirmPanel() {
        JShrinkShortcut urlShrinkShortcut = new JShrinkShortcut();
        urlShrinkShortcut.setTitle(ConsoleLocaleFactory.getString("inspector_url"));
        urlShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        urlShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString("inspector_url"));

        urlTextField = new JBasicTextField();

        JShrinkShortcut serviceIdShrinkShortcut = new JShrinkShortcut();
        serviceIdShrinkShortcut.setTitle(ConsoleLocaleFactory.getString("inspector_services"));
        serviceIdShrinkShortcut.setIcon(ConsoleIconFactory.getSwingIcon("stereo/paste_16.png"));
        serviceIdShrinkShortcut.setToolTipText(ConsoleLocaleFactory.getString("inspector_services"));

        serviceIdList = new JBasicList();
        JBasicScrollPane serviceIdScrollPane = new JBasicScrollPane(serviceIdList);
        serviceIdScrollPane.setPreferredSize(new Dimension(580, 340));

        double[][] size = {
                { TableLayout.FILL },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, 10, TableLayout.PREFERRED, TableLayout.FILL }
        };

        TableLayout tableLayout = new TableLayout(size);
        tableLayout.setHGap(0);
        tableLayout.setVGap(5);

        setLayout(tableLayout);
        add(urlShrinkShortcut, "0, 0");
        add(urlTextField, "0, 1");
        add(serviceIdShrinkShortcut, "0, 3");
        add(serviceIdScrollPane, "0, 4");
    }

    public String getUrl() {
        return urlTextField.getText().trim();
    }

    public void setUrl(String url) {
        urlTextField.setText(url);
    }

    public void setServiceIds(List<String> serviceIds) {
        ListUtil.setModel(serviceIdList, serviceIds, IconFactory.getSwingIcon("component/view.png"));
    }
}