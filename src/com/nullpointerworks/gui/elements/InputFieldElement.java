package com.nullpointerworks.gui.elements;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.core.buffer.IntBuffer;
import com.nullpointerworks.core.input.Key;
import com.nullpointerworks.core.input.KeyboardInput;
import com.nullpointerworks.core.input.Mouse;
import com.nullpointerworks.core.input.MouseInput;
import com.nullpointerworks.gui.UIElement;
import com.nullpointerworks.gui.input.PassthroughFilter;
import com.nullpointerworks.gui.interfaces.UIFocusListener;
import com.nullpointerworks.gui.interfaces.UIKeystrokeFilter;
import com.nullpointerworks.gui.interfaces.UIKeystrokeListener;
import com.nullpointerworks.util.timing.Timer;

public abstract class InputFieldElement extends UIElement 
implements KeyListener, UIFocusListener
{
	private boolean isEditable = true;
	private boolean isEditing = false;
	private UIKeystrokeFilter ksfilter;
	private List<UIKeystrokeListener> listeners;
	private List<Byte> char_num; // contains the text
	
	private Runnable timed_call;
	private Timer timed;
	
	private boolean isVisible = true; // is the text censored or not
	private List<String> char_str; // the masked text as string
	
	private String grey_text; // tool-tip text when field is blank
	private String char_caret; // default caret marker
	private String caret;
	private boolean toggle = true; 
	
	public InputFieldElement()
	{
		ksfilter	= new PassthroughFilter();
		listeners	= new ArrayList<UIKeystrokeListener>();
		char_num 	= new ArrayList<Byte>();
		char_str 	= new ArrayList<String>();
		grey_text 	= "";
		char_caret 	= "_";
		caret		= char_caret;
		
		timed_call = new Runnable()
		{
			@Override
			public void run() 
			{
				toggle = !toggle;
				if (toggle) caret = char_caret;
				else caret = " ";
				onRefresh();
			}
		};
		
		timed = new Timer(timed_call, 0.333f);
	}
	
	/**
	 * Returns true if the input field is accepting changes
	 */
	public boolean isEditing()
	{
		return isEditing;
	}
	
	/**
	 * Enable or disable editing the field
	 */
	public void setEditable(boolean e)
	{
		isEditable = e;
	}
	
	/**
	 * Set a new keystroke filter to control the inputflow for this field
	 */
	public void setKeystrokeFilter(UIKeystrokeFilter ksf)
	{
		ksfilter = ksf;
	}
	
	/**
	 * Add a keystrokelistener to this element. Each key stroke will notify all listeners.
	 */
	public void addKeystrokeListener(UIKeystrokeListener ksl)
	{
		listeners.add(ksl);
	}
	
	/**
	 * Add a character to the field using an ASCII code, if the field is editable
	 */
	public void addFieldCharacter(byte code) 
	{
		if (!isEditable) return;
		if (!isEditing) return;
		char_num.add(code);
		copyToString();
		onRefresh();
	}
	
	/**
	 * Removes the last character in the field
	 */
	public void popFieldCharacter() 
	{
		if (!isEditable) return;
		if (!isEditing) return;
		if (char_num.size() < 1) return;
		char_num.remove( char_num.size()-1 );
		copyToString();
		onRefresh();
	}
	
	/**
	 * Returns a character in the field as a String object
	 */
	public String getFieldCharacter(int index, boolean override) 
	{
		if (!override)
		return char_str.get(index);
		else
		return ""+ (char)( (byte)char_num.get(index) );
	}
	
	/**
	 * Set the content of the inputfield to the given string. This also sets the 
	 * UI's label property to the same string.
	 */
	public void setFieldContent(String label)
	{
		super.setLabel(label);
		char_num.clear();
		char[] t = label.toCharArray();
		for (int i=0,l=t.length; i<l; i++)
		{
			byte code = (byte)(t[i]);
			char_num.add(code);
		}
		setVisible(isVisible);
	}
	
	/**
	 * Returns the content of the field as one string.
	 */
	public String getFieldContent() 
	{
		return getFieldContent(false);
	}
	
	/**
	 * Returns the content of the field as one string. Set override to true for
	 * uncensored output.
	 */
	public String getFieldContent(boolean override) 
	{
		String res = "";
		if (override)
		{
			for (int index=0, l=char_num.size(); index<l; index++)
			{
				res += (char)( (byte)char_num.get(index) );
			}
		}
		else
		{
			for (String s : char_str)
			{
				res += s;
			}
		}
		return res;
	}
	
	/**
	 * returns the amount of characters in the input field
	 */
	public int getFieldLength() 
	{
		return char_num.size();
	}
	
	/**
	 * set the tool-tip text in the box when empty
	 */
	public void setGreyText(String str)
	{
		this.grey_text = str;
		onRefresh();
	}
	
	/**
	 * returns the tool-tip text
	 */
	public String getGreyText()
	{
		return grey_text;
	}
	
	/**
	 * sets the caret character
	 */
	public void setCaret(String c)
	{
		this.caret = c;
	}
	
	/**
	 * returns the caret character
	 */
	public String getCaret()
	{
		return caret;
	}
	
	/**
	 * show or hide the text. when hidden, characters will show as * characters
	 */
	public void setVisible(boolean view)
	{
		isVisible = view;
		copyToString();
		onRefresh();
	}
	
	/*
	 * 
	 */
	private void copyToString()
	{
		char_str.clear();
		if (isVisible)
		{
			for (byte b : char_num)
			{
				char_str.add( ""+(char)b );
			}
		}
		else
		{
			for (int i=char_num.size(); i>0; i--)
				char_str.add("*");
		}
	}
	
	@Override
	public UIElement setLabel(String label)
	{
		setFieldContent(label);
		return this;
	}
	
	@Override
	public void onUpdate(MouseInput mi, float dt)
	{
		if (mi.isClickedUp( Mouse.LEFT ))
		{
			UIElement el = this.getParent().getSelected();
			if (el.hashCode() == this.hashCode())
			{
				this.getParent().setFocus(this);
			}
		}
		
		if (isEditing)
		{
			timed.update(dt);
		}
	}

	@Override
	public void onFocus()
	{
		isEditing = true;
		onRefresh();
	}

	@Override
	public void onDefocus()
	{
		isEditing = false;
		timed.reset();
		onRefresh();
	}
	
	@Override
	public void onUpdate(KeyboardInput ki, float dt) 
	{
		synchronized(pendUpdate)
		{
			if (pendUpdate)
			{
				// test stroke filter
				if (!ksfilter.onKeystroke(char_code)) return;
				
				// parse keycode
				switch(char_code)
				{
				case Key.ENTER:
					getParent().setFocus(getParent()); 
					break;
					
				case Key.BACKSPACE:
					popFieldCharacter();
					break;
					
				default:
					addFieldCharacter(char_code);
					break;
				}
				
				// notify listeners
				for (UIKeystrokeListener ksl: listeners)
				{
					ksl.onKeystroke( char_code );
				}
				pendUpdate = false;
			}
		}
	}
	
	@Override
	public void onRender(IntBuffer screen, float dx, float dy) { }
	
	// ==========================================
	
	/*
	 * the Java KeyListener runs on a different thread, make sure all accessed code is thread safe
	 */
	
	private Boolean pendUpdate = false;
	private byte char_code = 0;
	
	@Override
	public void keyPressed(KeyEvent e) { }
	
	@Override
	public void keyReleased(KeyEvent e) { }
	
	@Override
    public void keyTyped(KeyEvent e)
    {
		synchronized(pendUpdate)
		{
			if (!this.getParent().isFocused(this)) return;
			pendUpdate = true;
			char_code = (byte)e.getKeyChar();
		}
    }
}
