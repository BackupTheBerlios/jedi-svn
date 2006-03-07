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

import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;

/**
 * Action form for JispFile uploading.<br>
 * Contains the uploaded file in a FormFile.
 */
public class UploadJispFileForm extends ActionForm {

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = -9183339993814853022L;

	/**
	 * The file that the user has uploaded
	 */
	protected FormFile file;

	/**
	 * Returns the file.
	 * 
	 * @return The file.
	 */
	public FormFile getFile() {
		return file;
	}

	/**
	 * Sets the file.
	 * 
	 * @param file
	 *            The file to set.
	 */
	public void setFile(FormFile file) {
		this.file = file;
	}
}
