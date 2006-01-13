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

package de.berlios.jedi.logic.admin;

import de.berlios.jedi.common.admin.exception.IncorrectPasswordException;
import de.berlios.jedi.common.entity.jisp.JispFile;
import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.exception.DataException;
import de.berlios.jedi.common.jisp.JispStoredToJispPackage;
import de.berlios.jedi.common.jisp.exception.JispInvalidIcondefException;
import de.berlios.jedi.common.jisp.exception.JispStoredException;
import de.berlios.jedi.data.admin.AdminDataService;

/**
 * <p>
 * Façade for admin related classes in logic layer.<br>
 * It offers all the actions about admin that presentation layer requires from
 * logic layer while abstracts its internal behaviour.
 * </p>
 * <p>
 * It provides methods to save JispFiles and remove JispPackages.
 * </p>
 */
public class AdminLogicService {

	/**
	 * Saves a JispFile, so the equivalent JispPackage can be got later.<br>
	 * If the JispPackage contained in the JispFile was already stored, it will
	 * be updated. The JispPackage is identified by its name and version (stored
	 * in its JispMetadata object).
	 * 
	 * @param jispFile
	 *            The JispFile to save.
	 * @throws JispInvalidIcondefException
	 *             If icondef.xml in stored Jisp is not valid, not well-formed
	 *             or an object in icondef.xml doesn't exist in stored Jisp.
	 * @throws JispStoredException
	 *             If the file isn't a valid Zip file, is empty, doesn't have a
	 *             valid directory structure (icondef.xml file isn't in root or
	 *             base directory, if any) or doesn't contain an icondef.xml
	 *             file.
	 * @throws DataException
	 *             If there was a problem when saving a JispPackage.
	 */
	public void saveJispFile(JispFile jispFile)
			throws JispInvalidIcondefException, JispStoredException,
			DataException {
		new AdminDataService().saveJispPackage(new JispStoredToJispPackage(
				new JispFileWrapper(jispFile)).toJispPackage());
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
		new AdminDataService().deleteJispPackage(jispPackage);
	}

	/**
	 * Checks if the password is equal to the admin password.
	 * 
	 * @param password
	 *            The password to check.
	 * @return True if the password is correct.
	 * @throws IncorrectPasswordException
	 *             If the password is not correct.
	 */
	public boolean login(String password) throws IncorrectPasswordException {
		return new LoginManager().login(password);
	}
}
