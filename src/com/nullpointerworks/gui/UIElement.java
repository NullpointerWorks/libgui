package com.nullpointerworks.gui;

import com.nullpointerworks.core.input.KeyboardInput;
import com.nullpointerworks.core.input.MouseInput;
import com.nullpointerworks.gui.interfaces.UIApplicationListener;
import com.nullpointerworks.gui.interfaces.UIGeometryListener;
import com.nullpointerworks.gui.interfaces.UIRefreshListener;
import com.nullpointerworks.math.geometry.g2d.Geometry2D;
import com.nullpointerworks.math.geometry.g2d.Rectangle;

public abstract class UIElement extends UISettings 
implements UIGeometryListener, UIApplicationListener, UIRefreshListener
{
	private UserInterface _parent = null;
	public final void setParent(UserInterface p) {_parent = p;}
	public UserInterface getParent() {return _parent;}
	public UISettings getSettings() {return (UISettings)this;}
	
	public final void setElementSettings(UISettings es)
	{
		this.copySettings(es);
	}
	
	/**
	 * Returns the geometry offset by the positioning of all its parent.
	 */
	public Geometry2D getAbsoluteGeometry()
	{
		float ox = 0f;
		float oy = 0f;
		if (getParent()!=null)
		{
			Rectangle pgeom = getParent().getGeometry().getBoundingBox();
			ox += pgeom.x;
			oy += pgeom.y;
		}
		Geometry2D geom = super.getGeometry().copy();
		geom.translate(ox, oy);
		return geom;
	}
	
	@Override
	public abstract void onRefresh();
	
	@Override
	public final void onUpdate(MouseInput mi, KeyboardInput ki, float dt)
	{
		if (mi!=null) onUpdate(mi,dt);
		if (ki!=null) onUpdate(ki,dt);
	}
	
	// =============================================
	
	@Override
	public boolean onGeometryTest(float px, float py) 
	{
		return this.getGeometry().isInside(px, py);
	}
}
