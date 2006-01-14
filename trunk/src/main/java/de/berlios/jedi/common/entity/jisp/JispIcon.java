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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a Jisp Icon.<br>
 * Contains JispObjects and JispTexts.<br>
 * Objects from this class will be persisted.
 */
public class JispIcon implements Cloneable, Serializable {

	/**
	 * Generated <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = -4624136439245050109L;

	/**
	 * Persistent identifier.
	 */
	private Long id;

	/**
	 * The list containing the JispObjects.
	 */
	private List jispObjects;

	/**
	 * The list containing the JispTexts.
	 */
	private List jispTexts;

	/**
	 * Creates a new JispIcon with no JispObject nor JispText.
	 */
	public JispIcon() {
		jispObjects = new ArrayList();
		jispTexts = new ArrayList();
	}

	/**
	 * Compares this JispIcon to the specified object.<br>
	 * The result is true if and only if the argument is not null and is a
	 * JispIcon object with the same values of fields.
	 * 
	 * @param object
	 *            The object to compare against.
	 * @return True if the JispIcons are equal; false otherwise.
	 */
	public boolean equals(Object object) {
		if (object == null)
			return false;
		if (!(object instanceof JispIcon))
			return false;
		JispIcon jispIcon = (JispIcon) object;
		if (!jispObjects.equals(jispIcon.jispObjects))
			return false;
		if (!jispTexts.equals(jispIcon.jispTexts))
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
		return 3;
	}

	/**
	 * Creates and returns a copy of this object.
	 * 
	 * @return A clone of this instance.
	 */
	public Object clone() {
		JispIcon clone = new JispIcon();

		if (id != null) {
			clone.id = new Long(id.longValue());
		}

		Iterator jispObjectsIterator = jispObjects.iterator();
		while (jispObjectsIterator.hasNext()) {
			clone.jispObjects.add(((JispObject) jispObjectsIterator.next())
					.clone());
		}

		Iterator jispTextsIterator = jispTexts.iterator();
		while (jispTextsIterator.hasNext()) {
			clone.jispTexts.add(((JispText) jispTextsIterator.next()).clone());
		}

		return clone;
	}

	/**
	 * Returns the representation of the JispIcon as a String.
	 * 
	 * @return The representation of the JispIcon as a String.
	 */
	public String toString() {
		return "texts: " + jispTexts.toString() + "\nobjects: "
				+ jispObjects.toString();
	}

	/**
	 * <p>
	 * Adds a JispObject to the icon.
	 * </p>
	 * <p>
	 * jispObject can not be null
	 * </p>
	 * 
	 * @param jispObject
	 *            The JispObject to add.
	 */
	public void addJispObject(JispObject jispObject) {
		jispObjects.add(jispObject);
	}

	/**
	 * <p>
	 * Adds a JispText to the icon.
	 * </p>
	 * <p>
	 * jispText can not be null
	 * </p>
	 * 
	 * @param jispText
	 *            The JispText to add.
	 */
	public void addJispText(JispText jispText) {
		jispTexts.add(jispText);
	}

	/**
	 * Test if the JispIcon contains the specified JispObject. <br>
	 * Returns true if and only if this JispIcon contains at least one
	 * JispObject containedJispObject such that (jispObject==null ?
	 * containedJispObject==null : jispObject.equals(containedJispObject)).
	 * 
	 * @param jispObject
	 *            The JispObject whose presence is going to be tested.
	 * @return True if the JispIcon contains the specified JispObject.
	 */
	public boolean containsJispObject(JispObject jispObject) {
		return jispObjects.contains(jispObject);
	}

	/**
	 * Test if the JispIcon contains the specified JispText. <br>
	 * Returns true if and only if this JispIcon contains at least one JispText
	 * containedJispText such that (jispText==null ? containedJispText==null :
	 * jispText.equals(containedJispText)).
	 * 
	 * @param jispText
	 *            The JispText whose presence is going to be tested.
	 * @return True if the JispIcon contains the specified JispText.
	 */
	public boolean containsJispText(JispText jispText) {
		return jispTexts.contains(jispText);
	}

	/**
	 * Returns an iterator for the JispObjects.<br>
	 * The iterator allows erasing elements.
	 * 
	 * @return An iterator for the JispObjects.
	 */
	public Iterator jispObjectsIterator() {
		return jispObjects.iterator();
	}

	/**
	 * Returns an iterator for the JispTexts.<br>
	 * The iterator allows erasing elements.
	 * 
	 * @return An iterator for the JispTexts.
	 */
	public Iterator jispTextsIterator() {
		return jispTexts.iterator();
	}

	/**
	 * <p>
	 * Removes the first ocurrence of the JispObject in JispIcon.
	 * </p>
	 * <p>
	 * jispObject can not be null
	 * </p>
	 * 
	 * @param jispObject
	 *            The JispObject to remove.
	 * @return True if the JispIcon contained the specified JispObject.
	 */
	public boolean removeJispObject(JispObject jispObject) {
		return jispObjects.remove(jispObject);
	}

	/**
	 * <p>
	 * Removes the first ocurrence of the JispText in JispIcon.
	 * </p>
	 * <p>
	 * jispText can not be null
	 * </p>
	 * 
	 * @param jispText
	 *            The JispText to remove.
	 * @return True if the JispIcon contained the specified JispText.
	 */
	public boolean removeJispText(JispText jispText) {
		return jispTexts.remove(jispText);
	}
}
