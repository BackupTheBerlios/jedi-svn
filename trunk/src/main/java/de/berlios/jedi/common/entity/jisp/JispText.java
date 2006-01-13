/*
 * Copyright 2005-2006 Daniel Calviño Sánchez
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY.
 *
 * See the COPYING file for more details.
 */

package de.berlios.jedi.common.entity.jisp;

/**
 * Represents a text element of a Jisp Icon.
 */
public class JispText implements Cloneable {

	/**
	 * Persistent identifier.
	 */
	private Long id;

	/**
	 * The text.
	 */
	private String text;

	/**
	 * Creates a new JispText, with its text as null.
	 */
	public JispText() {
		text = null;
	}

	/**
	 * Compares this JispText to the specified object.<br>
	 * The result is true if and only if the argument is not null and is a
	 * JispText object with the same values of fields.
	 * 
	 * @param object
	 *            The object to compare against.
	 * @return True if the JispText are equal; false otherwise.
	 */
	public boolean equals(Object object) {
		if (object == null)
			return false;
		if (!(object instanceof JispText))
			return false;
		JispText jispText = (JispText) object;
		if (!(text == null ? jispText.text == null : text.equals(jispText.text)))
			return false;
		return true;
	}

	/**
	 * Returns a hash code value for the object.<br>
	 * The objects are mutable, so the hashCode is the same for all objects of
	 * this class.
	 * 
	 * @return The hashCode value for the object.
	 */
	public int hashCode() {
		return 4;
	}

	/**
	 * Creates and returns a copy of this object.
	 * 
	 * @return A clone of this instance.
	 */
	public Object clone() {
		JispText clone = new JispText();

		if (id != null) {
			clone.id = new Long(id.longValue());
		}
		if (text != null) {
			clone.text = new String(text);
		}

		return clone;
	}

	/**
	 * Returns the representation of the JispText as a String.
	 * 
	 * @return The representation of the JispText as a String.
	 */
	public String toString() {
		return "text: " + text;
	}

	/**
	 * Returns the text.
	 * 
	 * @return The text.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 * 
	 * @param text
	 *            The text to set.
	 */
	public void setText(String text) {
		this.text = text;
	}
}
