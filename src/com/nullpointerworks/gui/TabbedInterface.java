package com.nullpointerworks.gui;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.core.buffer.IntBuffer;
import com.nullpointerworks.core.input.KeyboardInput;
import com.nullpointerworks.core.input.MouseInput;
import com.nullpointerworks.gui.elements.ButtonElement;
import com.nullpointerworks.gui.interfaces.UICallback;
import com.nullpointerworks.math.IntMath;
import com.nullpointerworks.math.geometry.g2d.Geometry2D;
import com.nullpointerworks.math.geometry.g2d.Rectangle;

public abstract class TabbedInterface extends UserInterface implements UICallback
{
	private List<ButtonElement> tabs;
	private List<UserInterface> uis;
	private int ACTIVE_TAB 	= 0;
	private int TAB_HEIGHT 	= 20;
	private int TAB_SPACE 	= 2;
	
	public TabbedInterface()
	{
		tabs = new ArrayList<ButtonElement>();
		uis = new ArrayList<UserInterface>();
	}
	
	/**
	 * Add an interface to be displayed in the tabbing window
	 */
	public int addInterface(UserInterface ui)
	{
		// make and add button
		ButtonElement btn = onCreateTabButton(ui.getLabel());
		btn.setIdentifier( uis.size() );
		btn.setIndex(0);
		addElement( btn );
		tabs.add( btn );
		
		// adjust and add ui
		Rectangle tbb = this.getGeometry().getBoundingBox();		
		Rectangle ubb = ui.getGeometry().getBoundingBox();
		ui.getGeometry().translate( tbb.x + ubb.x, tbb.y + TAB_HEIGHT + ubb.y );
		uis.add( ui );
		
		onRefresh();
		return getTabCount();
	}
	
	/**
	 * Activate a tab by index
	 */
	public void setActive(int index)
	{
		ACTIVE_TAB = IntMath.clamp(0, index, tabs.size()-1);
		
		for (ButtonElement e : tabs)
		{
			e.setIndex(0);
		}
		
		if (tabs.size() > 0)
		{
			tabs.get(ACTIVE_TAB).setIndex(2);
		}
		
		onRefresh();
	}
	
	/**
	 * Event trigger when a new tab is added. Returns a button to fit in the tabbing bar
	 */
	protected abstract ButtonElement onCreateTabButton(String label);
	
	/**
	 * Returns the amount of tabs currently active
	 */
	public int getTabCount()
	{
		return tabs.size();
	}
	
	/**
	 * Returns the space between each tab button
	 */
	public int getTabHeight()
	{
		return TAB_HEIGHT;
	}
	
	/**
	 * Returns the space between each tab button
	 */
	public int getTabSpace()
	{
		return TAB_SPACE;
	}
	
	@Override
	public void onCall(UIElement uie)
	{
		setActive(uie.getIdentifier());
	}
	
	@Override
	public UISettings setGeometry(Geometry2D g2d)
	{
		Rectangle oldbb = this.getGeometry().getBoundingBox();	
		Rectangle newbb = g2d.getBoundingBox();	

		float dx = newbb.x - oldbb.x;
		float dy = newbb.y - oldbb.y;
		
		for (UserInterface ui : uis)
		{
			ui.getGeometry().translate(dx, dy);
		}
		
		UISettings sett = super.setGeometry(g2d);
		onRefresh();
		return sett;
	}
	
	@Override
	public int addElement(UIElement uie)
	{
		if (uie instanceof UserInterface)
			return addInterface( (UserInterface)uie );
		return super.addElement( uie );
	}
	
	@Override
	public void onRefresh()
	{
		super.onRefresh();
		if (uis.size() > 0) uis.get(ACTIVE_TAB).onRefresh();
	}

	@Override
	public void onUpdate(MouseInput mi, float dt)
	{
		super.onUpdate(mi,dt);
		if (uis.size() > 0)
			uis.get(ACTIVE_TAB).onUpdate(mi,dt);
	}

	@Override
	public void onUpdate(KeyboardInput ki, float dt)
	{
		super.onUpdate(ki,dt);
		if (uis.size() > 0)
			uis.get(ACTIVE_TAB).onUpdate(ki,dt);
	}
	
	/**
	 * Render the tab buttons and the content of the selected tab
	 */
	@Override
	public void onRender(IntBuffer screen)
	{
		super.onRender(screen);
		if (uis.size() > 0)
			uis.get(ACTIVE_TAB).onRender(screen);
	}
}
