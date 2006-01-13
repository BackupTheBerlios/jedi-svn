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

import java.util.Arrays;

/**
 * Represents a multimedia object of a Jisp Icon.<br>
 * Objects from this class will be persisted.
 */
public class JispObject implements Cloneable {

	/**
	 * Persistent identifier.
	 */
	private Long id;

	/**
	 * The JispObject's filename.
	 */
	private String name;

	/**
	 * The JispObject's mime type as a String.
	 */
	private String mimeType;

	/**
	 * The byte array containing the JispObject's data.
	 */
	private byte[] data;

	/**
	 * Creates a new JispObject with all its attributes as null.
	 */
	public JispObject() {
		name = null;
		mimeType = null;
		data = null;
	}

	/**
	 * Compares this JispObject to the specified object.<br>
	 * The result is true if and only if the argument is not null and is a
	 * JispObject object with the same values of fields.
	 * 
	 * @param object
	 *            The object to compare against.
	 * @return True if the JispObjects are equal; false otherwise.
	 */
	public boolean equals(Object object) {
		if (object == null)
			return false;
		if (!(object instanceof JispObject))
			return false;
		JispObject jispObject = (JispObject) object;
		if (!name.equals(jispObject.name))
			return false;
		if (!mimeType.equals(jispObject.mimeType))
			return false;
		if (!Arrays.equals(data, jispObject.data))
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
		return 5;
	}

	/**
	 * Creates and returns a copy of this object.
	 * 
	 * @return A clone of this instance.
	 */
	public Object clone() {
		JispObject clone = new JispObject();

		if (id != null) {
			clone.id = new Long(id.longValue());
		}
		if (data != null) {
			clone.data = (byte[]) data.clone();
		}
		if (mimeType != null) {
			clone.mimeType = new String(mimeType);
		}
		if (name != null) {
			clone.name = new String(name);
		}

		return clone;
	}

	/**
	 * Returns the representation of the JispObject as a String.
	 * 
	 * @return The representation of the JispObject as a String.
	 */
	public String toString() {
		return "name: " + name + " MIME type: " + mimeType;
	}

	/**
	 * Returns the data.
	 * 
	 * @return The data.
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Returns the mimeType.
	 * 
	 * @return The mimeType.
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Returns the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the data.
	 * 
	 * @param data
	 *            The data to set.
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	/**
	 * Sets the mimeType.
	 * 
	 * @param mimeType
	 *            The mimeType to set.
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
}
