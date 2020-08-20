package com.nullpointerworks.gui.interfaces;

import com.nullpointerworks.core.buffer.IntBuffer;
import com.nullpointerworks.core.input.KeyboardInput;
import com.nullpointerworks.core.input.MouseInput;

public interface UIApplicationListener 
{
	public void onUpdate(MouseInput mi, KeyboardInput ki, float dt);
	public void onUpdate(MouseInput mi, float dt);
	public void onUpdate(KeyboardInput ki, float dt);
	public void onRender(IntBuffer screen, float dx, float dy);
	public void onDispose();
}
