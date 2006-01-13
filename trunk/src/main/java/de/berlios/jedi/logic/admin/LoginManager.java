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


import org.apache.commons.logging.LogFactory;

import de.berlios.jedi.common.admin.exception.IncorrectPasswordException;
import de.berlios.jedi.common.config.ConfigurationFactory;

/**
 * Manager for admin login.<br>
 * Takes care of password validity checking. 
 */
public class LoginManager {

	/**
	 * Checks if the password is equal to the admin password.<br>
	 * The admin password is set in the configuration file.
	 * 
	 * @param password
	 *            The password to check.
	 * @return True if the password is correct.
	 * @throws IncorrectPasswordException
	 *             If the password is not correct.
	 */
	public boolean login(String password) throws IncorrectPasswordException {
		if (password != null
				&& password.equals(ConfigurationFactory.getConfiguration().getString(
						"admin.password"))) {
			return true;
		}

		LogFactory.getLog(LoginManager.class).error("Incorrect password when login: " + password);
		
		throw new IncorrectPasswordException("Incorrect password");
	}
}
