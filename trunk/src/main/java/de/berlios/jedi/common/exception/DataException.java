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

package de.berlios.jedi.common.exception;

/**
 * Signals that an exception when accesing or modifying the persistent data
 * happened.
 */
public class DataException extends Exception {

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = -4295823542249006714L;

	/**
	 * Constructs a DataException with null as its error detail message.
	 */
	public DataException() {
		super();
	}

	/**
	 * Constructs a DataException with the specified detail message.
	 * 
	 * @param message
	 *            The detail message.
	 */
	public DataException(String message) {
		super(message);
	}

	/**
	 * Constructs a DataException with the specified detail message and cause.
	 * 
	 * @param message
	 *            The detail message.
	 * @param cause
	 *            The cause.
	 */
	public DataException(String message, Throwable cause) {
		super(message, cause);
	}
}
