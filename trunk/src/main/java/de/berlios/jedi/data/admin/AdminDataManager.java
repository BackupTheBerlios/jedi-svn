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
package de.berlios.jedi.data.admin;

import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.exception.DataException;

/**
 * Provides operations to manage the stored data from an admin point of view.<br>
 * This includes saving and deleting JispPackages.
 */
public interface AdminDataManager {

	/**
	 * Saves a JispPackage.<br>
	 * If the JispPackage was already stored, it will be updated. The
	 * JispPackage is identified by its name and version (stored in its
	 * JispMetadata object).
	 * 
	 * @param jispPackage
	 *            The JispPackage to save.
	 * @throws DataException
	 *             If there was a problem when saving a JispPackage.
	 */
	public void saveJispPackage(JispPackage jispPackage) throws DataException;

	/**
	 * Deletes a stored JispPackage from the persistent store.
	 * 
	 * @param jispPackage
	 *            The JispPackage to delete.
	 * @throws DataException
	 *             If there was a problem when deleting a stored JispPackage.
	 */
	public void deleteJispPackage(JispPackage jispPackage) throws DataException;
}
