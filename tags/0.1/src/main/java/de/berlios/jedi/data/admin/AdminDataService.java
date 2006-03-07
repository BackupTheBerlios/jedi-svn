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
 * <p>
 * Façade for admin related classes in data layer.<br>
 * It offers all the actions about admin that logic layer requires from data
 * layer while abstracts its internal behaviour.
 * </p>
 * <p>
 * It provides methods to save and delete JispPackages.
 * </p>
 * <p>
 * Due to the internal use of HibernateAdminDataManager, it can not be used with
 * various JVM simultaneously.
 * </p>
 */
public class AdminDataService {

	/**
	 * The data manager which implements the delegated methods.
	 */
	private AdminDataManager adminDataManager;

	/**
	 * Constructs a new AdminDataService.
	 */
	public AdminDataService() {
		adminDataManager = new HibernateAdminDataManager();
	}

	/**
	 * Saves the JispPackage in a persistent way.<br>
	 * 
	 * If the JispPackage was already stored, it will be updated. The
	 * JispPackage is identified by its name and version (stored in its
	 * JispMetadata object).
	 * 
	 * @param jispPackage
	 *            The JispPackage to save.
	 * @throws DataException
	 *             If there was a problem when saving a JispPackage.
	 */
	public void saveJispPackage(JispPackage jispPackage) throws DataException {
		adminDataManager.saveJispPackage(jispPackage);
	}

	/**
	 * Deletes a stored JispPackage from the persistent store.
	 * 
	 * @param jispPackage
	 *            The JispPackage to delete.
	 * @throws DataException
	 *             If there was a problem when deleting a stored JispPackage.
	 */
	public void deleteJispPackage(JispPackage jispPackage) throws DataException {
		adminDataManager.deleteJispPackage(jispPackage);
	}
}
