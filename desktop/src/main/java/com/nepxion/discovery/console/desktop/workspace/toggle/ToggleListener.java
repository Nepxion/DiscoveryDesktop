package com.nepxion.discovery.console.desktop.workspace.toggle;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.swing.element.IElementNode;
import com.nepxion.swing.list.toggle.AbstractToggleAdapter;
import com.nepxion.swing.list.toggle.JToggleList;
import com.nepxion.swing.toggle.ITogglePanel;

public class ToggleListener extends AbstractToggleAdapter {
    public ToggleListener(JToggleList toggleList) {
        super(toggleList);
    }

    public ITogglePanel getTogglePanel(IElementNode elementNode) {
        Object userObject = elementNode.getUserObject();
        if (userObject == null) {
            userObject = new TogglePanel(elementNode);
            elementNode.setUserObject(userObject);
        }

        return (ITogglePanel) userObject;
    }
}