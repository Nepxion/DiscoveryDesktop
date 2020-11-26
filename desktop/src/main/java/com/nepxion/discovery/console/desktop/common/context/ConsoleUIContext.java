package com.nepxion.discovery.console.desktop.common.context;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.awt.Font;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.discovery.console.desktop.common.swing.lookandfeel.LookAndFeelManager;
import com.nepxion.swing.font.FontContext;
import com.nepxion.util.locale.LocaleContext;

public class ConsoleUIContext {
    private static final String FONT_NAME_ZH_CN = "\u5fae\u8f6f\u96c5\u9ed1"; // 微软雅黑
    private static final int FONT_SMALL_SIZE_ZH_CN = 12; // 11
    private static final int FONT_MIDDLE_SIZE_ZH_CN = FONT_SMALL_SIZE_ZH_CN + 1;
    private static final int FONT_LARGE_SIZE_ZH_CN = FONT_SMALL_SIZE_ZH_CN + 2;

    private static final String FONT_NAME_EN_US = "Calibri";
    private static final int FONT_SMALL_SIZE_EN_US = 12;
    private static final int FONT_MIDDLE_SIZE_EN_US = FONT_SMALL_SIZE_EN_US + 1;
    private static final int FONT_LARGE_SIZE_EN_US = FONT_SMALL_SIZE_EN_US + 2;

    public static void initialize() {
        String theme = ConsolePropertiesContext.getProperties().getTheme();
        if (StringUtils.equals(theme, ConsoleConstant.DEFAULT_THEME)) {
            setFont();
            setLookAndFeel(theme);
        } else {
            setLookAndFeel(theme);
            setFont();
        }
    }

    public static void setFont() {
        FontContext.registerFont(getFontName(), Font.PLAIN, getDefaultFontSize());
    }

    public static void setLookAndFeel(String theme) {
        // theme=NimbusLookAndFeel
        // theme=AcidLookAndFeel
        // theme=AlloyLookAndFeel
        // theme=BedouinLookAndFeel
        // theme=GlassLookAndFeel

        LookAndFeelManager.invokeLookAndFeel(theme);
    }

    public static String getFontName() {
        if (LocaleContext.getLocale() == LocaleContext.LOCALE_ZH_CN) {
            return FONT_NAME_ZH_CN;
        } else {
            return FONT_NAME_EN_US;
        }
    }

    public static int getDefaultFontSize() {
        String fontName = getFontName();
        if (fontName.equals(FONT_NAME_ZH_CN)) {
            return FONT_MIDDLE_SIZE_ZH_CN;
        } else {
            return FONT_MIDDLE_SIZE_EN_US;
        }
    }

    public static int getSmallFontSize() {
        String fontName = getFontName();
        if (fontName.equals(FONT_NAME_ZH_CN)) {
            return FONT_SMALL_SIZE_ZH_CN;
        } else {
            return FONT_SMALL_SIZE_EN_US;
        }
    }

    public static int getMiddleFontSize() {
        String fontName = getFontName();
        if (fontName.equals(FONT_NAME_ZH_CN)) {
            return FONT_MIDDLE_SIZE_ZH_CN;
        } else {
            return FONT_MIDDLE_SIZE_EN_US;
        }
    }

    public static int getLargeFontSize() {
        String fontName = getFontName();
        if (fontName.equals(FONT_NAME_ZH_CN)) {
            return FONT_LARGE_SIZE_ZH_CN;
        } else {
            return FONT_LARGE_SIZE_EN_US;
        }
    }
}