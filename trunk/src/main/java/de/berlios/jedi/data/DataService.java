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

package de.berlios.jedi.data;

import de.berlios.jedi.common.entity.jisp.JispPackagesList;
import de.berlios.jedi.common.exception.DataException;

/**
 * <p>
 * Façade for common operations in data layer.<br>
 * It offers all the common operations from data layer while abstracts its
 * internal behaviour.
 * </p>
 * <p>
 * This service provides a method to retrieve all the stored JispPackages.
 * </p>
 * <p>
 * Due to the internal use of HibernateDataManager, if the cache is enabled this
 * class can not be used with various JVM simultaneously.
 * </p>
 */
public class DataService {

	/**
	 * The data manager which implements the delegated methods.
	 */
	private DataManager dataManager;

	/**
	 * Constructs a new DataService.
	 */
	public DataService() {
		dataManager = new HibernateDataManager();
	}

	/**
	 * Returns a list with all the stored JispPackages.
	 * 
	 * @return The list with all the stored JispPackages.
	 * @throws DataException
	 *             If there was a problem when getting the stored JispPackages.
	 */
	public JispPackagesList getJispPackagesList() throws DataException {
		return dataManager.getJispPackagesList();
	}
}
