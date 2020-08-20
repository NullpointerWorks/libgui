package com.nullpointerworks.gui.awt;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class PixelLayout implements LayoutManager 
{
	public PixelLayout() { }
	
	@Override
	public void layoutContainer(Container parent) 
	{
		int cNum = parent.getComponentCount();
		for (int i = 0 ; i < cNum ; i++)
        {
            Component c = parent.getComponent(i);
            if (c.isVisible()) 
            {
        		c.setBounds(c.getX(), c.getY(), c.getWidth(), c.getHeight());
            }
        }
	}
	
	public Dimension minimumLayoutSize(Container parent) 			{return null;}
	public Dimension preferredLayoutSize(Container parent) 			{return null;}
	public void removeLayoutComponent(Component comp) 				{ }
	public void addLayoutComponent(String name, Component comp) 	{ }
}