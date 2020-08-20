package com.nullpointerworks.gui.elements;

import com.nullpointerworks.core.buffer.IntBuffer;
import com.nullpointerworks.core.input.KeyboardInput;
import com.nullpointerworks.core.input.MouseInput;
import com.nullpointerworks.gui.UIElement;
import com.nullpointerworks.gui.interfaces.UIActivityListener;
import com.nullpointerworks.gui.interfaces.UIEnableListener;
import com.nullpointerworks.gui.interfaces.UIFocusListener;
import com.nullpointerworks.gui.interfaces.UIHoverListener;
import com.nullpointerworks.gui.interfaces.UIScrollListener;

public class NullElement extends UIElement
implements UIEnableListener, UIActivityListener, UIHoverListener, 
UIFocusListener, UIScrollListener
{
	public NullElement()
	{
		setIndex(-1);
	}

	@Override
	public void onUpdate(MouseInput mi, float dt) {}
	
	@Override
	public void onUpdate(KeyboardInput ki, float dt) {}
	
	@Override
	public void onRender(IntBuffer s, float dx, float dy) {}

	@Override
	public void onDispose() { }
	
	@Override
	public void onRefresh() { }

	@Override
	public void onEnter() { }

	@Override
	public void onHover() { }

	@Override
	public void onLeave() { }

	@Override
	public void onPressed() { }

	@Override
	public void onPressing() { }

	@Override
	public void onRelease() { }

	@Override
	public void onEnable() { }

	@Override
	public void onDisable() { }

	@Override
	public void onFocus() { }

	@Override
	public void onDefocus() { }

	@Override
	public void onScrolling(int dx, int dy) { }
};