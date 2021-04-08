/**
 * This is free and unencumbered software released into the public domain.
 * (http://unlicense.org/)
 * Nullpointer Works (2021)
 *
 * @version 0.0.1 alpha
 * @author Michiel Drost - Nullpointer Works
 */
module libnpw.gui 
{
	requires transitive java.desktop;
	requires transitive libnpw.core;
	requires transitive libnpw.math;
	requires libnpw.util;

	
	exports com.nullpointerworks.gui;
	exports com.nullpointerworks.gui.awt;
	exports com.nullpointerworks.gui.elements;
	exports com.nullpointerworks.gui.input;
	exports com.nullpointerworks.gui.interfaces;
}