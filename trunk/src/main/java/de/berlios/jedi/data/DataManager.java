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
 * Provides the common operations to manage the stored data.<br>
 * This includes retrieving all the stored JispPackages.
 */
public interface DataManager {

	/**
	 * Returns a JispPackagesList with all the previously saved JispPackages.
	 * 
	 * @return A JispPackagesList with all the previously saved JispPackages.
	 * @throws DataException
	 *             If there was a problem when getting the stored JispPackages.
	 */
	public JispPackagesList getJispPackagesList() throws DataException;
}
