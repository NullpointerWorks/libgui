package com.nullpointerworks.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.nullpointerworks.core.buffer.IntBuffer;
import com.nullpointerworks.core.input.*;
import com.nullpointerworks.gui.elements.*;
import com.nullpointerworks.gui.interfaces.*;
import com.nullpointerworks.math.geometry.g2d.Rectangle;
import com.nullpointerworks.util.pattern.Iterator;

public class UserInterface extends ButtonElement 
implements UIFocusListener, UIScrollListener
{
	private List<UIHoverListener> _hoverlisteners;
	private List<UIEnableListener> _enablelisteners;
	private List<UIFocusListener> _focuslisteners;
	private List<UIActivityListener> _activitylisteners;
	private List<UIRefreshListener> _refreshlisteners;
	private List<UIScrollListener> _scrolllisteners;
	
	private List<UIElement> _elements;
	private UIElement _selected;
	private UIElement _focussed;
	private boolean _entered;
	private Comparator<UIElement> comp = new Comparator<UIElement>()
	{
		@Override
		public int compare(UIElement a, UIElement b)
		{
			if (a.getIndex() > b.getIndex()) return -1;
			if (a.getIndex() < b.getIndex()) return 1;
			return 0;
		}
	};
	
	public UserInterface()
	{
		_hoverlisteners = new ArrayList<UIHoverListener>();
		_enablelisteners = new ArrayList<UIEnableListener>();
		_focuslisteners = new ArrayList<UIFocusListener>();
		_activitylisteners = new ArrayList<UIActivityListener>();
		_refreshlisteners = new ArrayList<UIRefreshListener>();
		_scrolllisteners = new ArrayList<UIScrollListener>();
		
		_elements = new ArrayList<UIElement>();
		_selected = null;
		_focussed = this;
		_entered = false;
	}
	
	/**
	 * Provide an external hover event listener
	 */
	public void addHoverListener(UIHoverListener hl)
	{
		_hoverlisteners.add(hl);
	}
	
	/**
	 * Provide an external enabler event listener
	 */
	public void addEnableListener(UIEnableListener el)
	{
		_enablelisteners.add(el);
	}
	
	/**
	 * Provide an external focus event listener
	 */
	public void addFocusListener(UIFocusListener fl)
	{
		_focuslisteners.add(fl);
	}
	
	/**
	 * Provide an external button activity event listener
	 */
	public void addActivityListener(UIActivityListener al)
	{
		_activitylisteners.add(al);
	}
	
	/**
	 * Provide an external refreshing event listener
	 */
	public void addRefreshListener(UIRefreshListener rl)
	{
		_refreshlisteners.add(rl);
	}
	
	/**
	 * Adds an element to the UI
	 */
	public int addElement(UIElement uie)
	{
		uie.setParent(this);
		uie.onRefresh();
		_elements.add(uie);
		_elements.sort(comp);
		return _elements.size() - 1;
	}
	
	/**
	 * Fetch an element form the interface on its identifier
	 */
	public UIElement getElement(int identifier)
	{
		for (int l=_elements.size()-1; l>=0; l--)
		{
			UIElement e = _elements.get(l);
			if (e.getIdentifier() == identifier)
			{
				return e;
			}
		}
		return null;
	}
	
	/**
	 * Returns the amount of elements that are registered to this UI
	 */
	public int getSize()
	{
		return _elements.size();
	}
	
	/**
	 * Removes the given element in this UI if it's present
	 */
	public void remElement(UIElement uie)
	{
		if (uie == null) return;
		for (int l=_elements.size()-1; l>=0; l--)
		{
			UIElement e = _elements.get(l);
			if (e.hashCode() == uie.hashCode())
			{
				_elements.remove(l);
				e.setParent(null);
				return;
			}
		}
	}
	
	/**
	 * Removes all elements in the UI
	 */
	public void remAllElements()
	{
		_elements.clear();
	}
	
	/**
	 * Returns an element within the bounds of the UI. 
	 * May return a NullElement object if no element was found
	 */
	public UIElement findElement(float x, float y)
	{
		UIElement uie = new NullElement();
		
		int l = _elements.size() - 1;
		for(; l>=0; l--)
		{
			UIElement u = _elements.get(l);
			if (u.getIndex() > uie.getIndex())
			if (u.getGeometry().isInside(x, y))
			{
				uie = u;
			}
		}
		
		return uie;
	}
	
	/**
	 * Returns the element in the selection buffer
	 */
	public UIElement getSelected()
	{
		return _selected;
	}
	
	/**
	 * Returns the element in the focus buffer
	 */
	public UIElement getFocused()
	{
		return _focussed;
	}
	
	/**
	 * Returns true if the given element is selected in the UI
	 */
	public boolean isSelected(UIElement el)
	{
		boolean el_n = el==null;
		boolean se_n = _selected==null;
		if (el_n ^ se_n) return false;
		if (el_n && se_n) return true;
		return _selected.hashCode() == el.hashCode();
	}
	
	/**
	 * Returns true if the given element is in focus in the UI
	 */
	public boolean isFocused(UIElement el)
	{
		boolean el_n = el==null;
		boolean se_n = _focussed==null;
		if (el_n ^ se_n) return false;
		if (el_n && se_n) return true;
		return _focussed.hashCode() == el.hashCode();
	}
	
	/**
	 * Set an element as selected in the UI. 
	 * The given element has to belong to this UI
	 */
	public void setSelected(UIElement el)
	{
		if (el != null)
		if (el.getParent() != null)
		if (el.getParent().hashCode() != this.hashCode()) return;
		
		if (_selected != null)
		{
			if (_selected instanceof UIHoverListener)
			{
				UIHoverListener s = (UIHoverListener)_selected;
				s.onLeave();
			}
		}
		
		_selected = el;
		
		if (_selected != null)
		{
			if (_selected instanceof UIHoverListener)
			{
				UIHoverListener s = (UIHoverListener)_selected;
				s.onEnter();
			}
		}
	}
	
	/**
	 * Focus on an element and trigger respective events. 
	 * The given element has to belong to this UI
	 */
	public void setFocus(UIElement el)
	{
		if (el != null)
		if (el.getParent() != null)
		if (el.getParent().hashCode() != this.hashCode()) return;
		
		if (_focussed != null)
		{
			if (el != null)
			if (_focussed.hashCode() == el.hashCode()) 
			{
				return;
			}
			
			if (_focussed instanceof UIFocusListener)
			{
				UIFocusListener f = (UIFocusListener)_focussed;
				f.onDefocus();
			}
		}
		
		_focussed = el;
		
		if (_focussed != null)
		{
			if (_focussed instanceof UIFocusListener)
			{
				UIFocusListener f = (UIFocusListener)_focussed;
				f.onFocus();
			}
		}
	}
	
	/**
	 * Returns all elements in an iterator
	 */
	public Iterator<UIElement> getIterator()
	{
		return new Iterator<UIElement>(_elements);
	}
	
	// ============================================================
	
	@Override
	public void onScrolling(int dx, int dy)
	{
		int l = _elements.size() - 1;
		for(; l>=0; l--)
		{
			UIElement uie = _elements.get(l);
			if (uie.getScrollEnabled())
			if (uie instanceof UIScrollListener)
			{
				UIScrollListener sl = (UIScrollListener)uie;
				sl.onScrolling(dx, dy);
			}
		}
		
		if (this.getScrollEnabled())
			for (UIScrollListener al : _scrolllisteners) {al.onScrolling(dx,dy);}
	}
	
	// ============================================================
	
	@Override
	public void onRefresh() 
	{
		_elements.sort(comp);
		int l = _elements.size() - 1;
		for(; l>=0; l--)
		{
			UIElement u = _elements.get(l);
			u.onRefresh();
		}
		
		for (UIRefreshListener al : _refreshlisteners) {al.onRefresh();}
	}
	
	// ============================================================
	
	@Override
	public void onPressed() 
	{
		for (UIActivityListener al : _activitylisteners) {al.onPressed();}
		
		if (_selected == null) 
		{
			setFocus(this);
			return;
		}
		
		/*
		 * selected may be a NullElement object
		 * it has no parent, thus null
		 */
		if (_selected.getParent() == null)
		{
			setFocus(this);
			return;
		}
	}
	
	@Override
	public void onPressing()
	{
		for (UIActivityListener al : _activitylisteners) {al.onPressing();}
	}
	
	@Override
	public void onRelease()
	{
		for (UIActivityListener al : _activitylisteners) {al.onRelease();}
	}
	
	// ============================================================
	
	@Override
	public void onEnter()
	{
		for (UIHoverListener hl : _hoverlisteners) {hl.onEnter();}
	}
	
	@Override
	public void onHover()
	{
		for (UIHoverListener hl : _hoverlisteners) {hl.onHover();}
	}
	
	@Override
	public void onLeave()
	{
		for (UIHoverListener hl : _hoverlisteners) {hl.onLeave();}
	}
	
	// ============================================================
	
	@Override
	public void onEnable()
	{
		for (UIEnableListener el : _enablelisteners) {el.onEnable();}
	}
	
	@Override
	public void onDisable()
	{
		for (UIEnableListener el : _enablelisteners) {el.onDisable();}
	}
	
	// ============================================================
	
	@Override
	public void onFocus() 
	{
		for (UIEnableListener el : _enablelisteners) {el.onEnable();}
	}
	
	@Override
	public void onDefocus()
	{
		for (UIEnableListener el : _enablelisteners) {el.onDisable();}
	}
	
	// ============================================================
	
	/**
	 * Update all UI elements and selection with core mouse input
	 */
	@Override
	public void onUpdate(MouseInput mi, float dt)
	{
		if (!this.isEnabled()) return;
		
		float mx = mi.getMouseX();
		float my = mi.getMouseY();
		
		UIElement uie;
		
		if (!this.getGeometry().isInside(mx, my))
		{
			if (_entered) 
			{
				_entered = false;
				onLeave();
			}
			
			uie = new NullElement();
		}
		else
		{
			if (!_entered)
			{
				_entered = true;
				onEnter();
			}
			else
			{
				onHover();
			}
			
			mx -= getGeometry().getBoundingBox().x;
			my -= getGeometry().getBoundingBox().y;
			uie = findElement(mx, my);
		}
		
		if (uie != _selected)
		{
			setSelected(uie);
		}
		
		for(int l=_elements.size()-1; l>=0; l--)
		{
			UIElement u = _elements.get(l);
			u.onUpdate(mi, dt);
		}
		super.onUpdate(mi,dt);
	}
	
	/**
	 * Update the UI elements with core keyboard input
	 */
	@Override
	public void onUpdate(KeyboardInput ki, float dt) 
	{
		if (!this.isEnabled()) return;
		
		for(int l=_elements.size()-1; l>=0; l--)
		{
			UIElement u = _elements.get(l);
			u.onUpdate(ki, dt);
		}
		super.onUpdate(ki,dt);
	}
	
	/**
	 * Render the interface with no offsets
	 */
	public void onRender(IntBuffer s) 
	{
		onRender(s,0f,0f);
	}
	
	/**
	 * Call for a render on each UI elements
	 */
	@Override
	public void onRender(IntBuffer s, float dx, float dy) 
	{
		if (!this.isEnabled()) return;
		
		Rectangle g = this.getGeometry().getBoundingBox();
		dx += g.x;
		dy += g.y;
		int l = _elements.size() - 1;
		for(; l>=0; l--)
		{
			UIElement u = _elements.get(l);
			u.onRender(s, dx, dy);
		}
	}
	
	/**
	 * Dispose of this interface and all its elements
	 */
	@Override
	public void onDispose()
	{
		int l = _elements.size() - 1;
		for(; l>=0; l--)
		{
			UIElement u = _elements.get(l);
			u.onDispose();
		}
		_elements.clear();
		_elements = null;
		_selected = null;
		_focussed = null;
	}
}
