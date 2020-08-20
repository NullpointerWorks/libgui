package com.nullpointerworks.gui;

import com.nullpointerworks.core.input.MouseInput;
import com.nullpointerworks.gui.UIElement;
import com.nullpointerworks.gui.UISettings;
import com.nullpointerworks.gui.UserInterface;
import com.nullpointerworks.gui.interfaces.UIScrollListener;
import com.nullpointerworks.math.FloatMath;
import com.nullpointerworks.math.geometry.g2d.Rectangle;
import com.nullpointerworks.util.pattern.Iterator;

public class ScrollInterface extends UserInterface
{
	private Rectangle content_bb;
	private final float TOLERANCE 	= 0.00001f;
	private float[] scroll_vector 	= {0f,1f};
	private float scroll_factor 	= 5f;
	private float bounce_factor 	= 0.25f;

	/**
	 * Set the scrolling factor for the panel. Each mousewheel scrollstep will 
	 * be scaled by the given value. This factor of 5 is default.
	 */
	public void setScrollFactor(float s)
	{
		scroll_factor = s * 0.3333f;
	}
	
	/**
	 * Set the bounce-back factor for the panel. This factor of 0.25 is default.
	 */
	public void setBounceFactor(float s)
	{
		bounce_factor = s;
	}
	
	/**
	 * Set the direction of the scrolling vector. The given values are normalized 
	 * into a unit vector.
	 */
	public void setScrollVector(float sx, float sy)
	{
		float pyth = FloatMath.pythagoras(sx,sy);
		if (pyth < TOLERANCE) pyth = 1f;
		pyth = 1f / pyth;
		scroll_vector = new float[] {sx*pyth, sy*pyth};
	}
	
	/**
	 * Set the direction of the scrolling vector. The given vector is normalized
	 * into a unit vector.
	 */
	public void setScrollVector(float[] vec)
	{
		setScrollVector(vec[0], vec[1]);
	}
	
	/**
	 * Scroll the content of the panel an amount of pixels with the given factor.
	 * The direction is set by the scrolling vector.
	 */
	public void onScrolling(float[] v, float f)
	{
		int x = round(v[0] * f);
		int y = round(v[1] * f);
		super.onScrolling(x, y);
	}
	
	/**
	 * Add an element to the scrolling interface
	 */
	@Override
	public int addElement(UIElement el)
	{
		if (el.getScrollEnabled())
		if (!(el instanceof UIScrollListener) ) return -1;
		
		int index = super.addElement(el);
		content_bb = construct_bb();
		
		return index;
	}
	
	@Override
	public UISettings setScrollEnabled(boolean b)
	{
		Iterator<UIElement> it = this.getIterator();
		while(it.hasNext()) 
		{
			UIElement e = it.getNext();
			e.setScrollEnabled(b);
		}
		return super.setScrollEnabled(b);
	}
	
	@Override
	public void onUpdate(MouseInput mi, float f)
	{
		if (!this.isEnabled()) return;
		/*
		 * update its own mouse state and scrolling pane
		 */
		super.onUpdate(mi, f);
		
		float ox=0f, oy=0f;
		if (getParent() != null) 
		{
			ox = getParent().getGeometry().getBoundingBox().x;
			oy = getParent().getGeometry().getBoundingBox().y;
		}

		/*
		 * bounce back elements if out of bounds
		 */
		doScrollBounce(mi);
		
		/*
		 * if mouse is not in the UI, skip the rest
		 */
		float mouse_x = mi.getMouseX() - ox;
		float mouse_y = mi.getMouseY() - oy;
		if (!onGeometryTest(mouse_x, mouse_y)) return;
		
		/*
		 * scroll the panel if need be
		 */
		doScrolling(mi);
	}
	
	// ====================================================================
	
	/*
	 * Returns the boundary margin in a rectangle. 
	 */
	private Rectangle marginbox()
	{
		Rectangle margin = new Rectangle();
		Rectangle this_bb = this.getGeometry().getBoundingBox();
		margin.x = -content_bb.x;
		margin.y = -content_bb.y;
		margin.w = content_bb.w + content_bb.x - this_bb.w;
		margin.h = content_bb.h + content_bb.y - this_bb.h;
		return margin;
	}
	
	/*
	 * Contructs the area boundbox of the interface's content
	 */
	private Rectangle construct_bb()
	{
		float x1=0,y1=0,x2=0,y2=0;
		Iterator<UIElement> children = this.getIterator();
		
		if (children.hasNext())
		{
			UIElement uie = children.getNext();
			Rectangle bb = uie.getGeometry().getBoundingBox();
			x1 = bb.x;
			y1 = bb.y;
			x2 = x1 + bb.w;
			y2 = y1 + bb.h;
		}
		
		while(children.hasNext())
		{
			UIElement uie = children.getNext();
			Rectangle bb = uie.getGeometry().getBoundingBox();

			x1 = (bb.x < x1)? bb.x: x1;
			y1 = (bb.y < y1)? bb.y: y1;
			
			x2 = ( (bb.x + bb.w) > x2)? (bb.x + bb.w): x2;
			y2 = ( (bb.y + bb.h) > y2)? (bb.y + bb.h): y2;
		}
		
		return new Rectangle( x1,y1, x2-x1, y2-y1);
	}
	
	/*
	 * Scroll the panel with the mouse scrollwheel input
	 */
	private void doScrolling(MouseInput mi)
	{
		if (!mi.isScrolling()) return;
		float factor = -scroll_factor * mi.getScroll();
		doScrolling(scroll_vector, factor);
	}
	
	/*
	 * Bounce the elements back to fit the UI boundary
	 */
	private void doScrollBounce(MouseInput mi)
	{
		doScrollBounce(scroll_vector);
	}
	
	/*
	 * Preform a scroll on a vector and step factor
	 */
	private void doScrolling(float[] v, float f)
	{
		if (this.getSize()<1) return;
		if (!this.getScrollEnabled()) return;
		
		float fscrx 	= v[0];
		float fscry 	= v[1];
		boolean xscroll	= true;
		boolean yscroll	= true;
		int scrx = 0;
		int scry = 0;
		
		// if view box is inside total bound area, we can scroll
		content_bb = construct_bb();
		Rectangle mb = marginbox();
		
		// if we may scroll horizontally
		if (fscrx != 0f)
		{
			if (!sign(mb.x)) xscroll = false;
			if (!sign(mb.w)) xscroll = false;
		}
		
		// if we may scroll vertically
		if (fscry != 0f)
		{
			if (!sign(mb.y)) yscroll = false;
			if (!sign(mb.h)) yscroll = false;
		}
		
		if (xscroll)
		{
			scrx = round( fscrx * f );
		}
		
		if (yscroll)
		{
			scry = round( fscry * f );
		}
		
		onScrolling(scrx, scry);
	}
	
	/*
	 * Preform a bounceback with the given vector
	 */
	private void doScrollBounce(float[] v)
	{
		if (this.getSize()<1) return;
		if (!this.getScrollEnabled()) return;
		
		content_bb = construct_bb();
		Rectangle mb = marginbox();
		
		float fscrx = v[0];
		float fscry = v[1];
		int scrx = 0;
		int scry = 0;
		
		if (fscrx != 0f)
		{
			if (!sign(mb.x))
			{
				if (sign(mb.w))
				{
					scrx = round( fscrx * mb.x * bounce_factor ) - 1;
				}
			}
			else
			{
				if (!sign(mb.w))
				{
					scrx = round( fscrx * -mb.w * bounce_factor ) + 1;
				}
			}
		}
		
		if (fscry != 0f)
		{
			if (!sign(mb.y))
			{
				if (sign(mb.h))
				{
					scry = round( fscry * mb.y * bounce_factor ) - 1;
				}
			}
			else
			{
				if (!sign(mb.h))
				{
					scry = round( fscry * -mb.h * bounce_factor ) + 1;
				}
			}
		}
		
		onScrolling(scrx, scry);
	}
	
	/*
	 * Simple rounding function
	 */
	private int round(float x)
	{
		return (int)(x+0.5f);
	}
	
	/*
	 * Returns true if the given value is positive, including zero
	 */
	private boolean sign(float x)
	{
		return x > 0f;
	}
}
