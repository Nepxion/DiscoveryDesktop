package com.nepxion.discovery.console.desktop.common.util;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import com.nepxion.swing.list.BasicListModel;
import com.nepxion.swing.list.JBasicList;
import com.nepxion.swing.renderer.list.ElementListCellRenderer;
import com.nepxion.swing.searchable.JSearchableFactory;

public class ListUtil {
    public static void installSearchable(JBasicList list) {
        JSearchableFactory.installSearchable(list);
    }

    @SuppressWarnings("unchecked")
    public static void setSortableModel(JBasicList list, List<String> value, ImageIcon imageIcon) {
        Collections.sort(value);

        Vector<String> vector = new Vector<String>(value);

        list.setListData(value.toArray());
        list.setModel(new BasicListModel(vector));
        list.setCellRenderer(new ElementListCellRenderer(list, BorderFactory.createEmptyBorder(0, 5, 0, 0), imageIcon, 22));
    }
}