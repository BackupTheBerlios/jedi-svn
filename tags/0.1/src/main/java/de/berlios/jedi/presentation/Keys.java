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

package de.berlios.jedi.presentation;

/**
 * Keys used to store some elements in the request.
 */
public class Keys {

	/**
	 * The request attribute key under which the String with the forward name to
	 * be made in some Actions is stored.<br>
	 * For example, AddStatusAction forwards to the specified forward name in
	 * this key.
	 */
	public static final String NEXT_FORWARD_NAME = "nextForward";
	/**
	 * The request attribute key under which the String with the forward name to
	 * the AddStatusAction is made. 
	 */
	public static final String ADD_STATUS_FORWARD_NAME = "addStatus";
}
