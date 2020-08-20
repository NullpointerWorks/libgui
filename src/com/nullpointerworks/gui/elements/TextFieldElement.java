package com.nullpointerworks.gui.elements;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.core.input.KeyboardInput;
import com.nullpointerworks.core.input.MouseInput;
import com.nullpointerworks.graphics.font.Font;
import com.nullpointerworks.gui.UIElement;
import com.nullpointerworks.math.geometry.g2d.Geometry2D;

public abstract class TextFieldElement extends UIElement 
{
	private List<String> characters; 	// individual characters
	private List<String> lines; 		// lines after wrapping and returns
	
	private int LINE_LENGTH = Integer.MAX_VALUE;
	private int CHAR_WIDTH = 10;
	
	public TextFieldElement()
	{
		characters = new ArrayList<String>();
		lines = new ArrayList<String>();
	}
	
	/**
	 * The text field assumes a monospace font. Set the width of each character
	 */
	public void setFontMetaData(Font font) 
	{
		CHAR_WIDTH = font.getCharWidth();
	}
	
	/**
	 * Returns the compiled lines 
	 */
	public List<String> getLines() 
	{
		return lines;
	}
	
	/**
	 * Refresh this UI element
	 */
	@Override
	public void onRefresh()
	{
		// clear all
		lines.clear();
		
		// recompile lines
		wordWrap(this.getGeometry());
		
		// call compile method
		onCompile(lines);
	}
	
	/*
	 * Detect words and wrap each line to fit the field dimensions
	 */
	protected String RETURN = "\n";
	protected String SPACE 	= " ";
	private void wordWrap(Geometry2D g) 
	{
		// get linewidth based on geometry
		int width 		= (int)g.getBoundingBox().w;
		LINE_LENGTH 	= (width / CHAR_WIDTH); // integer division
		
		String word = "";
		int word_l = 0;
		String line = "";
		int line_l = 0;
		
		int i = 0;
		int l = characters.size();
		while (i < l)
		{
			String character = characters.get(i);
			i++;

			boolean isSPACE 	= character.equalsIgnoreCase(SPACE);
			boolean isRETURN 	= character.equalsIgnoreCase(RETURN);
			
			// handling spaces
			if (isSPACE)
			{
				// if the word does not fit on the line
				// commit the line and reset
				if (line_l + word_l >= LINE_LENGTH)
				{
					lines.add(line);
					line = "";
					line_l = 0;
				}
				
				line 	+= ((line_l!=0)?SPACE:"") + word;
				line_l 	+= ((line_l!=0)?1:0) + word_l;
				
				word 	= "";
				word_l 	= 0;
				continue;
			}
			
			// if we reached a carriage return
			// place last word and commit
			if (isRETURN)
			{
				// if the word does not fit on the line
				// commit the line and reset
				if (line_l + word_l >= LINE_LENGTH)
				{
					lines.add(line);
					line = "";
					line_l = 0;
				}
				
				// if its the first and only word
				line 	+= ((line_l!=0)?SPACE:"") + word;
				line_l 	+= ((line_l!=0)?1:0) + word_l;
				
				word = "";
				word_l = 0;
				
				lines.add(line);
				line = "";
				line_l = 0;
				continue;
			}
			
			word += character;
			word_l++;
			
			// end of the text
			if (i>=l)
			{
				if (line_l + word_l >= LINE_LENGTH)
				{
					lines.add(line);
					line = "";
					line_l = 0;
				}
				line += ((line_l!=0)?SPACE:"") + word;
				lines.add(line);
			}
		}
	}

	/**
	 * Set strings as the content of the textfield
	 */
	public void setFieldContent(String... txt) 
	{
		clear();
		addFieldContent(txt);
	}

	/**
	 * Add strings as the content of the textfield
	 */
	public void addFieldContent(String... txt) 
	{
		for (String s : txt) print(s);
		onRefresh();
	}
	
	/**
	 * Returns the content of the textfield as a list of strings
	 */
	public List<String> getFieldContent() 
	{
		return lines;
	}
	
	/**
	 * Clear the textfield
	 */
	public void clear()
	{
		lines.clear();
		characters.clear();
		onRefresh();
	}
	
	/**
	 * Add string characters to the textfield
	 */
	private void print(String txt) 
	{
		// place an end line space
		if (characters.size() > 0)
		{
			String chr = characters.get( characters.size()-1 );
			if (!chr.equalsIgnoreCase(SPACE))
			if (!chr.equalsIgnoreCase(RETURN))
				characters.add(" "); 
		}
		
		String[] tokens = txt.split("");
		for (int i=0,l=tokens.length; i<l;i++)
		{
			characters.add(tokens[i]);
		}
		onRefresh();
	}
	
	public abstract void onCompile(List<String> lines);

	@Override
	public void onUpdate(MouseInput mi, float dt) {}

	@Override
	public void onUpdate(KeyboardInput ki, float dt) {}

	@Override
	public void onDispose() { }
}
