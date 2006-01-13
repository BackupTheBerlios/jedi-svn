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

package de.berlios.jedi.presentation.admin;

/**
 * Keys used to store some elements in the request.
 */
public class AdminKeys {

	/**
	 * The session attributes key under which the Boolean signaling if the admin
	 * is logged in is stored.
	 */
	public static final String IS_LOGGED_IN = "adminLoggedIn";

	/**
	 * The session attributes key under which the list of saved JispPackages is
	 * stored.
	 */
	public static final String JISP_PACKAGES_LIST_KEY = "adminJispPackagesList";

	/**
	 * The session attributes key under which the JispIdManager is stored.
	 */
	public static final String JISP_ID_MANAGER_KEY = "adminJispIdManager";
}
