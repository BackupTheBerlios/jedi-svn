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

package de.berlios.jedi.common.jisp.exception;

/**
 * Signals that an exception in stored Jisp processing has happened.
 */
public class JispStoredException extends Exception {

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = 382917793693924394L;

	/**
	 * Constructs a JispStoredException with null as its error detail message.
	 */
	public JispStoredException() {
		super();
	}

	/**
	 * Constructs a JispStoredException with the specified detail message.
	 * 
	 * @param message
	 *            The detail message.
	 */
	public JispStoredException(String message) {
		super(message);
	}
}
