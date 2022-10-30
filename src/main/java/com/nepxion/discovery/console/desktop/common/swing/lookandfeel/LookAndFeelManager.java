package com.nepxion.discovery.console.desktop.common.swing.lookandfeel;

/**
 * <p>Title: Nepxion Swing</p>
 * <p>Description: Nepxion Swing Repository</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.awt.Font;
import java.lang.reflect.InvocationTargetException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.l2fprod.common.swing.plaf.LookAndFeelAddons;
import com.l2fprod.common.swing.plaf.aqua.AquaLookAndFeelAddons;
import com.l2fprod.common.swing.plaf.metal.MetalLookAndFeelAddons;
import com.l2fprod.common.swing.plaf.windows.WindowsClassicLookAndFeelAddons;
import com.l2fprod.common.swing.plaf.windows.WindowsLookAndFeelAddons;
import com.nepxion.swing.button.ButtonContext;
import com.nepxion.swing.button.ButtonManager;
import com.nepxion.swing.font.FontContext;
import com.nepxion.swing.font.FontManager;
import com.nepxion.swing.style.button.flat.FlatButtonUI;
import com.nepxion.swing.style.button.flat.FlatMenuButtonUI;
import com.nepxion.swing.style.button.flat.FlatSplitButtonUI;
import com.nepxion.swing.style.button.flat.JFlatButtonStyle;
import com.nepxion.swing.style.button.lite.JAcidLiteButtonStyle;
import com.nepxion.swing.style.button.lite.JAlloyLiteButtonStyle;
import com.nepxion.swing.style.button.lite.JBedouinLiteButtonStyle;
import com.nepxion.swing.style.button.lite.JBorlandLiteButtonStyle;
import com.nepxion.swing.style.button.lite.JEclipseLiteButtonStyle;
import com.nepxion.swing.style.button.lite.JGlassLiteButtonStyle;
import com.nepxion.swing.style.button.lite.JNimbusLiteButtonStyle;
import com.nepxion.swing.style.button.lite.LiteButtonUI;
import com.nepxion.swing.style.button.lite.LiteMenuButtonUI;
import com.nepxion.swing.style.button.lite.LiteSplitButtonUI;
import com.nepxion.swing.style.framework.JLiteStyle;
import com.nepxion.swing.style.framework.JNimbusStyle;
import com.nepxion.swing.style.framework.JWindowsStyle;
import com.nepxion.swing.style.framework.StyleContext;
import com.nepxion.swing.style.framework.StyleManager;

public class LookAndFeelManager
{
	/**
	 * Sets the system look and feel. It will be adapted to OS facade.
	 * The framework style is Lite.
	 * The button ui is Flat.
	 * The button style is Flat.
	 */
	public static void setSystemLookAndFeel()
	{
		setLookAndFeel(UIManager.getSystemLookAndFeelClassName(), JLiteStyle.ID, FlatButtonUI.ID, FlatMenuButtonUI.ID, FlatSplitButtonUI.ID, JFlatButtonStyle.ID);
	}
	
	/**
	 * Sets the default metal look and feel. It is the JDK 1.4 style.
	 * The framework style is Lite.
	 * The button ui is Flat.
	 * The button style is Flat.
	 */
	public static void setDefaultMetalLookAndFeel()
	{
		try
		{
			MetalLookAndFeel lookAndFeel = new MetalLookAndFeel();
			lookAndFeel.setCurrentTheme(new DefaultMetalTheme()
			{
				private FontUIResource uiRes = new FontUIResource("Dialog", Font.PLAIN, 12);
				
				public FontUIResource getControlTextFont()
				{
					return uiRes;
				}
				
				public FontUIResource getSystemTextFont()
				{
					return uiRes;
				}
				
				public FontUIResource getUserTextFont()
				{
					return uiRes;
				}
				
				public FontUIResource getMenuTextFont()
				{
					return uiRes;
				}
				
				public FontUIResource getWindowTitleFont()
				{
					return uiRes;
				}
				
				public FontUIResource getSubTextFont()
				{
					return uiRes;
				}
			}
			);
			UIManager.setLookAndFeel(lookAndFeel);
		}
		catch (UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
		
		setStyle(JLiteStyle.ID);
		setFont();
		setButtonUI(FlatButtonUI.ID, FlatMenuButtonUI.ID, FlatSplitButtonUI.ID, JFlatButtonStyle.ID);
	}
	
	/**
	 * Sets the metal look and feel. It is the JDK 1.5 style above.
	 * The framework style is Lite.
	 * The button ui is Flat.
	 * The button style is Flat.
	 */
	public static void setMetalLookAndFeel()
	{
		setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel", JLiteStyle.ID, FlatButtonUI.ID, FlatMenuButtonUI.ID, FlatSplitButtonUI.ID, JFlatButtonStyle.ID);
	}
	
	/**
	 * Sets the windows look and feel.
	 * The framework style is Windows.
	 * The button ui is Flat.
	 * The button style is Flat.
	 */
	public static void setWindowsLookAndFeel()
	{
		setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel", JWindowsStyle.ID, FlatButtonUI.ID, FlatMenuButtonUI.ID, FlatSplitButtonUI.ID, JFlatButtonStyle.ID);
	}
	
	/**
	 * Sets the motif look and feel.
	 * The framework style is Lite.
	 * The button ui is Flat.
	 * The button style is Flat.
	 */
	public static void setMotifLookAndFeel()
	{
		setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel", JLiteStyle.ID, FlatButtonUI.ID, FlatMenuButtonUI.ID, FlatSplitButtonUI.ID, JFlatButtonStyle.ID);
	}
	
	/**
	 * Sets the gtk look and feel.
	 * The framework style is Lite.
	 * The button ui is Flat.
	 * The button style is Flat.
	 */
	public static void setGTKLookAndFeel()
	{
		setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel", JLiteStyle.ID, FlatButtonUI.ID, FlatMenuButtonUI.ID, FlatSplitButtonUI.ID, JFlatButtonStyle.ID);
	}
	
	/**
	 * Sets the nimbus look and feel. It is only used in JDK 1.6 above.
	 * The framework style is Nimbus.
	 * The button ui is Lite.
	 * The button style is Nimbus.
	 */
	public static void setNimbusLookAndFeel()
	{
		setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel", JNimbusStyle.ID, LiteButtonUI.ID, LiteMenuButtonUI.ID, LiteSplitButtonUI.ID, JNimbusLiteButtonStyle.ID);
	}
	
	/**
	 * Sets the alloy look and feel.
	 * The framework style is Lite.
	 * The button ui is Lite.
	 * The button style is Alloy.
	 */
	public static void setAlloyLookAndFeel()
	{
		setLookAndFeel("com.incors.plaf.alloy.DefaultThemeAlloyLookAndFeel", JLiteStyle.ID, LiteButtonUI.ID, LiteMenuButtonUI.ID, LiteSplitButtonUI.ID, JAlloyLiteButtonStyle.ID);
	}
	
	/**
	 * Sets the acid look and feel.
	 * The framework style is Lite.
	 * The button ui is Lite.
	 * The button style is Acid.
	 */
	public static void setAcidLookAndFeel()
	{
		setLookAndFeel("com.incors.plaf.alloy.AcidThemeAlloyLookAndFeel", JLiteStyle.ID, LiteButtonUI.ID, LiteMenuButtonUI.ID, LiteSplitButtonUI.ID, JAcidLiteButtonStyle.ID);
	}
	
	/**
	 * Sets the bedouin look and feel.
	 * The framework style is Lite.
	 * The button ui is Lite.
	 * The button style is Bedouin.
	 */
	public static void setBedouinLookAndFeel()
	{
		setLookAndFeel("com.incors.plaf.alloy.BedouinThemeAlloyLookAndFeel", JLiteStyle.ID, LiteButtonUI.ID, LiteMenuButtonUI.ID, LiteSplitButtonUI.ID, JBedouinLiteButtonStyle.ID);
	}
	
	/**
	 * Sets the glass look and feel.
	 * The framework style is Lite.
	 * The button ui is Lite.
	 * The button style is Glass.
	 */
	public static void setGlassLookAndFeel()
	{
		setLookAndFeel("com.incors.plaf.alloy.GlassThemeAlloyLookAndFeel", JLiteStyle.ID, LiteButtonUI.ID, LiteMenuButtonUI.ID, LiteSplitButtonUI.ID, JGlassLiteButtonStyle.ID);
	}
	
	/**
	 * Sets the borland look and feel. It is the JDK 1.5 style above.
	 * The framework style is Windows.
	 * The button ui is Lite.
	 * The button style is Borland.
	 */
	public static void setBorlandLookAndFeel()
	{
		setLookAndFeel("com.borland.plaf.borland.BorlandLookAndFeel", JWindowsStyle.ID, LiteButtonUI.ID, LiteMenuButtonUI.ID, LiteSplitButtonUI.ID, JBorlandLiteButtonStyle.ID);
	}
	
	/**
	 * Sets the jgoodies windows look and feel.
	 * The framework style is Windows.
	 * The button ui is Lite.
	 * The button style is Eclipse.
	 */
	public static void setJGoodiesWindowsLookAndFeel()
	{
		setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel", JWindowsStyle.ID, LiteButtonUI.ID, LiteMenuButtonUI.ID, LiteSplitButtonUI.ID, JEclipseLiteButtonStyle.ID);
	}
	
	/**
	 * Sets the plastic look and feel.
	 * The framework style is Windows.
	 * The button ui is Lite.
	 * The button style is Eclipse.
	 */
	public static void setPlasticLookAndFeel()
	{
		setLookAndFeel("com.jgoodies.looks.plastic.PlasticLookAndFeel", JWindowsStyle.ID, LiteButtonUI.ID, LiteMenuButtonUI.ID, LiteSplitButtonUI.ID, JEclipseLiteButtonStyle.ID);
	}
	
	/**
	 * Sets the plastic xp look and feel.
	 * The framework style is Windows.
	 * The button ui is Lite.
	 * The button style is Eclipse.
	 */
	public static void setPlasticXPLookAndFeel()
	{
		setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel", JWindowsStyle.ID, LiteButtonUI.ID, LiteMenuButtonUI.ID, LiteSplitButtonUI.ID, JEclipseLiteButtonStyle.ID);
	}
	
	/**
	 * Sets the plastic 3D look and feel.
	 * The framework style is Windows.
	 * The button ui is Lite.
	 * The button style is Eclipse.
	 */
	public static void setPlastic3DLookAndFeel()
	{
		setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel", JWindowsStyle.ID, LiteButtonUI.ID, LiteMenuButtonUI.ID, LiteSplitButtonUI.ID, JEclipseLiteButtonStyle.ID);
	}
	
	/**
	 * Sets the xp look and feel.
	 * The framework style is Windows.
	 * The button ui is Flat.
	 * The button style is Flat.
	 */
	public static void setXPLookAndFeel()
	{
		setLookAndFeel("com.stefankrause.xplookandfeel.XPLookAndFeel", JWindowsStyle.ID, FlatButtonUI.ID, FlatMenuButtonUI.ID, FlatSplitButtonUI.ID, JFlatButtonStyle.ID);
	}
	
	/**
	 * Sets the tiny look and feel.
	 * The framework style is Windows.
	 * The button ui is Flat.
	 * The button style is Flat.
	 */
	public static void setTinyLookAndFeel()
	{
		setLookAndFeel("de.muntjak.tinylookandfeel.TinyLookAndFeel", JWindowsStyle.ID, FlatButtonUI.ID, FlatMenuButtonUI.ID, FlatSplitButtonUI.ID, JFlatButtonStyle.ID);
	}
	
	/**
	 * Sets look and feel by a look and feel string.
	 * @param lookAndFeel the look and feel string
	 */
	public static void setLookAndFeel(String lookAndFeel)
	{
		setLookAndFeel(lookAndFeel, null, null, null, null, null);
	}
	
	/**
	 * Sets look and feel by a look and feel string, framework style, button ui and button style.
	 * @param lookAndFeel the look and feel string
	 * @param style the framework style string
	 * @param buttonUI the button ui string
	 * @param menuButtonUI the menu button ui string
	 * @param splitButtonUI the split button ui string
	 * @param buttonStyle the button style string
	 */
	public static void setLookAndFeel(String lookAndFeel, String style, String buttonUI, String menuButtonUI, String splitButtonUI, String buttonStyle)
	{
		try
		{
			UIManager.setLookAndFeel(lookAndFeel);
		}
		catch (Exception e)
		{
			setSystemLookAndFeel();
		}
		
		setStyle(style);
		setFont();
		setButtonUI(buttonUI, menuButtonUI, splitButtonUI, buttonStyle);
	}
	
	/**
	 * Sets look and feel by a look and feel string in a reflected way. 
	 * @param lookAndFeel the look and feel string
	 */
	public static void invokeLookAndFeel(String lookAndFeel)
	{
		try
		{
			// Optimized by Neptune		
			Class.forName("com.nepxion.discovery.console.desktop.common.swing.lookandfeel.LookAndFeelManager").getMethod("set" + lookAndFeel, null).invoke(null, null);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns true if the look and feel is Nimbus.
	 * @return true if the look and feel is Nimbus
	 */
	public static boolean isNimbusLookAndFeel()
	{
		return UIManager.getLookAndFeel().getID().equals("Nimbus");
	}
	
	/**
	 * Adds one the metal look and feel.
	 */
	public static void addonMetalLookAndFeel()
	{
		try
		{
			LookAndFeelAddons.setAddon(MetalLookAndFeelAddons.class);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds one the windows look and feel.
	 */
	public static void addonWindowsLookAndFeel()
	{
		try
		{
			LookAndFeelAddons.setAddon(WindowsClassicLookAndFeelAddons.class);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds one the luna look and feel.
	 */
	public static void addonLunaLookAndFeel()
	{
		try
		{
			UIManager.put("win.xpstyle.name", "luna");
			LookAndFeelAddons.setAddon(WindowsLookAndFeelAddons.class);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds one the homestead look and feel.
	 */
	public static void addonHomesteadLookAndFeel()
	{
		try
		{
			UIManager.put("win.xpstyle.name", "homestead");
			LookAndFeelAddons.setAddon(WindowsLookAndFeelAddons.class);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds one the metallic look and feel.
	 */
	public static void addonMetallicLookAndFeel()
	{
		try
		{
			UIManager.put("win.xpstyle.name", "metallic");
			LookAndFeelAddons.setAddon(WindowsLookAndFeelAddons.class);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds one the glossy look and feel.
	 */
	public static void addonGlossyLookAndFeel()
	{
		try
		{
			LookAndFeelAddons.setAddon(AquaLookAndFeelAddons.class);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the frame work style by a style class.
	 * @param styleClass the style class string
	 */
	public static void setStyle(String styleClass)
	{
		if (!StyleContext.setPriorityContext())
		{
			StyleManager.setStyle(styleClass);
		}
		
		// Optimized by Neptune
		// StyleManager.setThemeDecorated();
		
		if (StyleContext.isFrameDecorated())
		{
			StyleManager.setFrameDecorated(StyleContext.isFrameDecorated());
		}
		
		if (StyleContext.isDialogDecorated())
		{
			StyleManager.setDialogDecorated(StyleContext.isDialogDecorated());
		}
	}
	
	/**
	 * Sets the button ui by a button ui class, menu button ui class, split button ui class and button style class.
	 * @param buttonUIClass the button ui class string
	 * @param menuButtonUIClass the menu button ui class string
	 * @param splitButtonUIClass the split button ui class string
	 * @param buttonStyleClass the button style class string
	 */
	public static void setButtonUI(String buttonUIClass, String menuButtonUIClass, String splitButtonUIClass, String buttonStyleClass)
	{
		if (!ButtonContext.setPriorityContext())
		{
			ButtonManager.setUI(buttonUIClass, menuButtonUIClass, splitButtonUIClass, buttonStyleClass);
		}
	}
	
	/**
	 * Sets the font.
	 */
	public static void setFont()
	{
		FontManager.setFont(FontContext.getFont());
	}
}