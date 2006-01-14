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
import java.util.Arrays;

/**
 * Represents a jisp file (a zipped file with the icondef.xml and all the
 * objects).<br>
 * It stores the file as a byte array.
 */
public class JispFile implements Serializable {

	/**
	 * Generated <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = -263719624867306204L;
	
	/**
	 * The byte array containing the jisp file.
	 */
	private byte[] data;

	/**
	 * Creates a new JispFile with the file data (an array of bytes which
	 * contain the .jisp file).
	 * 
	 * @param data
	 *            The data of the file.
	 */
	public JispFile(byte[] data) {
		this.data = data;
	}

	/**
	 * Compares this JispFile to the specified object.<br>
	 * The result is true if and only if the argument is not null and is a
	 * JispFile object with the same values of fields.
	 * 
	 * @param object
	 *            The object to compare against.
	 * @return True if the JispFile are equal; false otherwise.
	 */
	public boolean equals(Object object) {
		if (object == null)
			return false;
		if (!(object instanceof JispFile))
			return false;
		JispFile jispFile = (JispFile) object;
		if (!Arrays.equals(data, jispFile.data))
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
		return 7;
	}

	/**
	 * Returns the representation of the JispFile as a String.
	 * 
	 * @return The representation of the JispFile as a String.
	 */
	public String toString() {
		return "data: " + data;
	}

	/**
	 * Returns the data of the file.
	 * 
	 * @return The data.
	 */
	public byte[] getData() {
		return data;
	}
}
