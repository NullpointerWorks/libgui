/**
 * Creative Commons - Attribution, Share Alike 4.0<br>
 * Nullpointer Works (2020)<br>
 * Use of this library is subject to license terms.<br>
 * @version 0.0.1 alpha
 * @author Michiel Drost - Nullpointer Works
 */
module libnpw.gui 
{
	requires transitive java.desktop;
	requires transitive libnpw.core;
	requires libnpw.util;
	requires libnpw.math;

	
	exports com.nullpointerworks.gui;
	exports com.nullpointerworks.gui.awt;
	exports com.nullpointerworks.gui.elements;
	exports com.nullpointerworks.gui.input;
	exports com.nullpointerworks.gui.interfaces;
}