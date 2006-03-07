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

package de.berlios.jedi.common.admin.exception;

/**
 * Signals that an incorrect password was used when trying to log in.
 */
public class IncorrectPasswordException extends Exception {

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = 4382648777159785012L;

	/**
	 * Constructs a IncorrectPasswordException with null as its error detail
	 * message.
	 */
	public IncorrectPasswordException() {
		super();
	}

	/**
	 * Constructs a IncorrectPasswordException with the specified detail
	 * message.
	 * 
	 * @param message
	 *            The detail message.
	 */
	public IncorrectPasswordException(String message) {
		super(message);
	}
}
