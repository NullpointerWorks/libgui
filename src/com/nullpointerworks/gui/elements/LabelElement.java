package com.nullpointerworks.gui.elements;

import com.nullpointerworks.core.input.KeyboardInput;
import com.nullpointerworks.core.input.MouseInput;
import com.nullpointerworks.gui.UIElement;

public abstract class LabelElement extends UIElement 
{
	@Override
	public void onUpdate(MouseInput mi, float dt) { }

	@Override
	public void onUpdate(KeyboardInput ki, float dt) { }

	@Override
	public void onDispose() { }
}
