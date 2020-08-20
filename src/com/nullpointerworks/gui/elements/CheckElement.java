package com.nullpointerworks.gui.elements;

import com.nullpointerworks.core.input.Mouse;
import com.nullpointerworks.core.input.MouseInput;
import com.nullpointerworks.util.concurrency.Lock;

public abstract class CheckElement extends ButtonElement 
{
	private Boolean state = false;
	private Lock _presslock = new Lock();
	
	@Override
	public void onUpdate(MouseInput mi, float dt) 
	{
		if (!isEnabled()) return;
		
		float ox = getParent().getGeometry().getBoundingBox().x;
		float oy = getParent().getGeometry().getBoundingBox().y;
		
		float mouse_x = mi.getMouseX() - ox;
		float mouse_y = mi.getMouseY() - oy;
		
		if (!onGeometryTest(mouse_x, mouse_y))
		{
			_presslock.unlock();
			return;
		}
		onHover();
		
		if (!_presslock.isLocked())
		if (mi.isClicked( Mouse.LEFT )) 
		{
			_presslock.lock();
			onPressed();
		}
		
		if (_presslock.isLocked())
		{
			if(mi.isClicked( Mouse.LEFT )) 
			{
				onPressing();
			}
			else
			{
				setChecked(!state);
				onRelease();
				_presslock.unlock();
			}
		}
	}
	
	/**
	 * Returns the state of the checkbox
	 */
	public boolean isChecked()
	{
		synchronized(state)
		{
			return state;
		}
	}
	
	/**
	 * Set the state of the box
	 */
	public void setChecked(boolean c)
	{
		synchronized(state)
		{
			if (state != c)
			{
				state = c;
				onRefresh();
			}
		}
	}
}
