package com.nullpointerworks.gui;

import com.nullpointerworks.color.ColorFormat;
import com.nullpointerworks.color.Colorizer;
import com.nullpointerworks.math.geometry.g2d.Geometry2D;
import com.nullpointerworks.math.geometry.g2d.Point;

public class UISettings 
{
	public static UISettings New()
	{
		return new UISettings();
	}
	
	// ========================================
	
	private final Colorizer ColorRGB = Colorizer.getColorizer(ColorFormat.RGB);
	
	private String name 		= "";
	private String label 		= "";
	private int fgColor 		= ColorRGB.toInt(255, 255, 255);
	private int bgColor 		= ColorRGB.toInt(0, 0, 0);
	private int identifier		= 0;
	private int index			= 0;
	private Geometry2D geom		= new Point(0f, 0f);
	private boolean bScrolling 	= false;
	
	public UISettings() {}
	
	public UISettings copy()
	{
		UISettings es = new UISettings();
		es.setName(name);
		es.setLabel(label);
		es.setForegroundColor(fgColor);
		es.setBackgroundColor(bgColor);
		es.setIndex(index);
		es.setIdentifier(identifier);
		es.setGeometry(geom);
		es.setScrollEnabled(bScrolling);
		return es;
	}
	
	protected void copySettings(UISettings es)
	{
		name 		= es.getName();
		label 		= es.getLabel();
		fgColor 	= es.getForegroundColor();
		bgColor 	= es.getBackgroundColor();
		index		= es.getIndex();
		identifier	= es.getIdentifier();
		geom 		= es.getGeometry();
		bScrolling 	= es.getScrollEnabled();
	}
	
	// ========================================
	
	public String getName() {return name;}
	public String getLabel() {return label;}
	public int getForegroundColor() {return fgColor;}
	public int getBackgroundColor() {return bgColor;}
	public int getIndex() {return index;}
	public int getIdentifier() {return identifier;}
	public Geometry2D getGeometry() {return geom;}
	public boolean getScrollEnabled() {return bScrolling;}
	
	// ========================================
	
	public UISettings setName(String name)
	{
		this.name = name;
		return this;
	}
	
	public UISettings setLabel(String label)
	{
		this.label = label;
		return this;
	}
	
	public UISettings setForegroundColor(int c)
	{
		fgColor = c;
		return this;
	}

	public UISettings setBackgroundColor(int c)
	{
		bgColor = c;
		return this;
	}

	public UISettings setIndex(int i)
	{
		index = i;
		return this;
	}

	public UISettings setIdentifier(int i)
	{
		identifier = i;
		return this;
	}
	
	public UISettings setGeometry(Geometry2D g)
	{
		geom = g;
		return this;
	}
	
	public UISettings setScrollEnabled(boolean enable)
	{
		bScrolling = enable;
		return this;
	}
}
