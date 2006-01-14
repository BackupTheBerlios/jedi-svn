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
 * Represents a Jisp Package, with the metadata and the list of icons.<br>
 * Objects from this class will be persisted.
 */
public class JispPackage implements Cloneable, Serializable {

	/**
	 * Generated <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = -8655489890155831314L;

	/**
	 * Persistent identifier.
	 */
	private Long id;

	/**
	 * The package's metadata.
	 */
	private JispMetadata jispMetadata;

	/**
	 * The list with package's icons.
	 */
	private List jispIcons;

	/**
	 * Creates a new JispPackage, with an empty metadata and without icons.
	 */
	public JispPackage() {
		jispMetadata = new JispMetadata();
		jispIcons = new ArrayList();
	}

	/**
	 * Compares this JispPackage to the specified object.<br>
	 * The result is true if and only if the argument is not null and is a
	 * JispPackage object with the same values of fields.
	 * 
	 * @param object
	 *            The object to compare against.
	 * @return True if the JispPackages are equal; false otherwise.
	 */
	public boolean equals(Object object) {
		if (object == null)
			return false;
		if (!(object instanceof JispPackage))
			return false;
		JispPackage jispPackage = (JispPackage) object;

		if (!(jispMetadata == null ? jispPackage.jispMetadata == null
				: jispMetadata.equals(jispPackage.jispMetadata)))
			return false;
		if (!jispIcons.equals(jispPackage.jispIcons))
			return false;
		return true;
	}

	/**
	 * Returns a hash code value for the object. The objects are mutable, so the
	 * hashCode is the same for all objects of this class.
	 * 
	 * @return The hashCode value for the object.
	 */
	public int hashCode() {
		return 0;
	}

	/**
	 * Creates and returns a copy of this object.
	 * 
	 * @return A clone of this instance.
	 */
	public Object clone() {
		JispPackage clone = new JispPackage();

		if (id != null) {
			clone.id = new Long(id.longValue());
		}
		clone.jispMetadata = (JispMetadata) jispMetadata.clone();
		Iterator jispIconsIterator = jispIcons.iterator();
		while (jispIconsIterator.hasNext()) {
			clone.jispIcons.add(((JispIcon) jispIconsIterator.next()).clone());
		}

		return clone;
	}

	/**
	 * Returns the representation of the JispPackage as a String.
	 * 
	 * @return The representation of the JispPackage as a String.
	 */
	public String toString() {
		return "metadata: "
				+ (jispMetadata != null ? jispMetadata.toString() : null)
				+ "\nicons: "
				+ (jispIcons != null ? jispIcons.toString() : null);
	}

	/**
	 * Returns the jispMetadata.
	 * 
	 * @return The jispMetadata.
	 */
	public JispMetadata getJispMetadata() {
		return jispMetadata;
	}

	/**
	 * <p>
	 * Sets the jispMetadata.
	 * </p>
	 * <p>
	 * jispMetadata can not be null.
	 * </p>
	 * 
	 * @param jispMetadata
	 *            The jispMetadata to set.
	 */
	public void setJispMetadata(JispMetadata jispMetadata) {
		this.jispMetadata = jispMetadata;
	}

	/**
	 * <p>
	 * Adds a JispIcon to the package.
	 * </p>
	 * <p>
	 * jispIcon can not be null.
	 * </p>
	 * 
	 * @param jispIcon
	 *            The JispIcon to add.
	 */
	public void addJispIcon(JispIcon jispIcon) {
		jispIcons.add(jispIcon);
	}

	/**
	 * Test if the JispPackage contains the specified JispIcon. <br>
	 * Returns true if and only if this JispPackage contains at least one
	 * JispIcon containedJispIcon such that (jispIcon==null ?
	 * containedJispIcon==null : jispIcon.equals(containedJispIcon)).
	 * 
	 * @param jispIcon
	 *            The JispIcon whose presence is going to be tested.
	 * @return True if the JispPackage contains the specified JispIcon.
	 */
	public boolean containsJispIcon(JispIcon jispIcon) {
		return jispIcons.contains(jispIcon);
	}

	/**
	 * Returns an iterator for the JispIcons.<br>
	 * The iterator allows erasing elements.
	 * 
	 * @return An iterator for the JispIcons.
	 */
	public Iterator jispIconsIterator() {
		return jispIcons.iterator();
	}

	/**
	 * <p>
	 * Removes the first ocurrence of the JispIcon in JispPackage.
	 * </p>
	 * <p>
	 * jispIcon can not be null.
	 * </p>
	 * 
	 * @param jispIcon
	 *            The JispIcon to remove.
	 * @return True if the JispPackage contained the specified JispIcon.
	 */
	public boolean removeJispIcon(JispIcon jispIcon) {
		return jispIcons.remove(jispIcon);
	}
}
