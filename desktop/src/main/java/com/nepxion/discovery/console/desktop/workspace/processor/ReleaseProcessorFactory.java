package com.nepxion.discovery.console.desktop.workspace.processor;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.plugin.framework.parser.xml.XmlConfigDeparser;
import com.nepxion.discovery.plugin.framework.parser.xml.XmlConfigParser;

public class ReleaseProcessorFactory {
    private static XmlConfigParser xmlConfigParser = new XmlConfigParser();
    private static XmlConfigDeparser xmlConfigDeparser = new XmlConfigDeparser();

    public static XmlConfigParser getXmlConfigParser() {
        return xmlConfigParser;
    }

    public static XmlConfigDeparser getXmlConfigDeparser() {
        return xmlConfigDeparser;
    }
}