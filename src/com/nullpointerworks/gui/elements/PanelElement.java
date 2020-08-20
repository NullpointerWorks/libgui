package com.nullpointerworks.gui.elements;

import com.nullpointerworks.core.input.MouseInput;
import com.nullpointerworks.gui.UIElement;
import com.nullpointerworks.gui.UserInterface;
import com.nullpointerworks.math.geometry.g2d.Rectangle;
import com.nullpointerworks.util.pattern.Iterator;

public abstract class PanelElement extends UserInterface
{
	private int space = 0;

	/**
	 * Add an element to the panel
	 */
	@Override
	public int addElement(UIElement el)
	{
		int count 		= getSize();
		int spacer 		= 0;
		Rectangle panel = getGeometry().getBoundingBox();
		Rectangle elmnt = el.getGeometry().getBoundingBox();
		
		if (count > 0)
		{
			UIElement uie = this.getElement(count-1);
			Rectangle box = uie.getGeometry().getBoundingBox();
			spacer = space + round( box.y - panel.y + box.h );
		}
		
		el.setGeometry( new Rectangle(elmnt.x + panel.x, 
									  elmnt.y + panel.y + spacer, 
									  elmnt.w, 
									  elmnt.h));
		
		el.setScrollEnabled(true);
		return super.addElement(el);
	}
	
	/**
	 * Set the number of pixels between the buttons
	 */
	public void setVerticalSpacing(int s) 
	{
		space = s;
	}
	
	@Override
	public void onUpdate(MouseInput mi, float f)
	{
		super.onUpdate(mi, f);
		
		if (!onGeometryTest(mi.getMouseX(), mi.getMouseY())) return;
		if (this.getSize() < 1) return;
		
		Iterator<UIElement> it = this.getIterator();
		
		if (mi.isScrolling())
		{
			int factor 	= -5 * mi.getScroll();
			float y_p 	= this.getGeometry().getBoundingBox().y;
			float yh_p 	= y_p + this.getGeometry().getBoundingBox().h;
			
			/*
			 * get first element
			 * clip the top
			 */
			if (factor > 0)
			{
				UIElement btne = it.getNext();
				float y_t = btne.getGeometry().getBoundingBox().y - 1f;
				int delta_y_t = round(y_t - y_p);
				if (delta_y_t < 0)
				{
					factor = ( (delta_y_t + factor) < 0)? factor: -delta_y_t;
				}
				else
				{
					factor = 0;
				}
			}
			/*
			 * get last element
			 * clip the bottom
			 */
			if (factor < 0)
			{
				UIElement btne = null;
				while(it.hasNext()) btne = it.getNext();
				float yh_b = btne.getGeometry().getBoundingBox().y + btne.getGeometry().getBoundingBox().h;
				int delta_yh_b = round(yh_b - yh_p);
				if (delta_yh_b > 0)
				{
					factor = (delta_yh_b + factor > 0)? factor: -delta_yh_b;
				}
				else
				{
					factor = 0;
				}
			}
			
			/*
			 * displace all elements
			 */
			if (factor != 0)
			{
				onScrolling(0, factor);
			}
		}
		else
		{
			int factor 	= 0;
			float y_p 	= this.getGeometry().getBoundingBox().y;
			float yh_p 	= y_p + this.getGeometry().getBoundingBox().h;
			
			/*
			 * check top out of bound
			 */
			UIElement btne = it.getNext();
			float y_t = btne.getGeometry().getBoundingBox().y - 1f;
			int delta_y_t = round(y_t - y_p);
			if (delta_y_t > 0)
			{
				factor = -delta_y_t;
			}
			
			/*
			 * check bottom out of bound
			 */
			while(it.hasNext()) btne = it.getNext();
			float yh_b = btne.getGeometry().getBoundingBox().y + btne.getGeometry().getBoundingBox().h;
			int delta_yh_b = round(yh_b - yh_p);
			if (delta_yh_b < 0)
			{
				factor = -delta_yh_b;
			}
			
			/*
			 * only correct scrolling pane if content overflows
			 */
			float delta = yh_b - y_t;
			if (delta > this.getGeometry().getBoundingBox().h)
			if (factor != 0)
			{
				onScrolling(0, factor >> 1);
			}
		}
	}
	
	/*
	 * simple rounding function
	 */
	private int round(float x)
	{
		return (int)(x+0.5f);
	}
}
