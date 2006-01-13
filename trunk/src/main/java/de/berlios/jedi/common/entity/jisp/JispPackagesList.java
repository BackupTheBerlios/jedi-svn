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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A list for JispPackages.<br>
 * This class simply encapsulates a list, limiting storable objects to
 * JispPackages.
 */
public class JispPackagesList {

	/**
	 * The list where JispPackages will be stored.
	 */
	private List jispPackages;

	/**
	 * Constructs a new list to contain JispPackages.
	 */
	public JispPackagesList() {
		jispPackages = new ArrayList();
	}

	/**
	 * Compares this JispPackagesList to the specified object.<br>
	 * The result is true if and only if the argument is not null and is a
	 * JispPackagesList object with the same values of fields.
	 * 
	 * @param object
	 *            The object to compare against.
	 * @return True if the JispPackagesLists are equal; false otherwise.
	 */
	public boolean equals(Object object) {
		if (object == null)
			return false;
		if (!(object instanceof JispPackagesList))
			return false;
		JispPackagesList jispPackagesList = (JispPackagesList) object;
		if (!jispPackages.equals(jispPackagesList.jispPackages))
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
		return 6;
	}

	/**
	 * Returns the representation of the JispPackagesList as a String.
	 * 
	 * @return The representation of the JispPackagesList as a String.
	 */
	public String toString() {
		return "packages: " + jispPackages.toString();
	}

	/**
	 * <p>
	 * Adds a new JispPackage to the list.
	 * </p>
	 * <p>
	 * jispPackage can not be null.
	 * </p>
	 * 
	 * @param jispPackage
	 *            The JispPackage to add.
	 */
	public void addJispPackage(JispPackage jispPackage) {
		jispPackages.add(jispPackage);
	}

	/**
	 * Returns an iterator for the list of JispPackages.<br>
	 * All elements will be JispPackages, and no null elements will exist.
	 * 
	 * Remove operation is implemented.
	 * 
	 * @return The iterator for the list.
	 */
	public Iterator iterator() {
		return jispPackages.iterator();
	}

	/**
	 * <p>
	 * Removes a JispPackage from the list, returning true if the JispPackage
	 * was contained.
	 * </p>
	 * <p>
	 * jispPackage can't be null.
	 * </p>
	 * 
	 * @param jispPackage
	 *            The JispPackage to remove.
	 * @return True if the list contained the JispPackage.
	 */
	public boolean removeJispPackage(JispPackage jispPackage) {
		return jispPackages.remove(jispPackage);
	}
}
