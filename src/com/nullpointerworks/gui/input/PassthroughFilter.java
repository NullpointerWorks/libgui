package com.nullpointerworks.gui.input;

import com.nullpointerworks.gui.interfaces.UIKeystrokeFilter;

public class PassthroughFilter implements UIKeystrokeFilter
{
	/**
	 * Returns true for every keystroke. 
	 */
	@Override
	public boolean onKeystroke(int keycode)
	{
		return true;
	}
}
