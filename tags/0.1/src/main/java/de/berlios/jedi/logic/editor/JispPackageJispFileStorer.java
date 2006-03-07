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

package de.berlios.jedi.logic.editor;

import java.io.*;

import java.util.zip.*;

import org.apache.commons.logging.LogFactory;

import de.berlios.jedi.common.entity.jisp.JispFile;
import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.jisp.JispPackageStorer;
import de.berlios.jedi.common.jisp.exception.JispIncompletePackageException;

/**
 * Stores a JispPackage in a JispFile.<br>
 * icondef.xml and JispObjects equivalent files are created from the JispPackage
 * and zipped into a JispFile.
 */
public class JispPackageJispFileStorer extends JispPackageStorer {

	/**
	 * The ZipOutputStream to save the JispPackage to.
	 */
	private ZipOutputStream outZipped;

	/**
	 * Constructs a new converter from JispPackage to JispFile.
	 * 
	 * @param jispPackage
	 *            The JispPackage to get the JispFile from.
	 */
	public JispPackageJispFileStorer(JispPackage jispPackage) {
		super(jispPackage);
		outZipped = null;
	}

	/**
	 * Adds a new file to the ZipOutputStream, given the filename and its data.
	 * 
	 * @param filename
	 *            The name of the file to add.
	 * @param data
	 *            The data of the file.
	 */
	protected void addFileToStore(String filename, byte[] data) {
		ZipEntry zipEntry = new ZipEntry(filename);

		try {
			outZipped.putNextEntry(zipEntry);
			outZipped.write(data);
			outZipped.closeEntry();
		} catch (IOException e) {
			LogFactory.getLog(JispPackageJispFileStorer.class).warn(
					"A file couldn't be added to the ZipOutputStream", e);
		}
	}

	/**
	 * Gets the equivalent JispFile for the JispPackage.
	 * 
	 * @return The JispFile.
	 * @throws JispIncompletePackageException
	 *             If metadata or icons aren't complete.
	 * @throws UnsupportedOperationException
	 *             If server doesn't support UTF-8 encoding.
	 */
	public JispFile toJispFile() throws JispIncompletePackageException,
			UnsupportedOperationException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		outZipped = new ZipOutputStream(out);

		store();

		try {
			outZipped.close();
		} catch (IOException e) {
			LogFactory.getLog(JispPackageJispFileStorer.class).warn(
					"The ZipOutputStream couldn't be closed", e);
		}

		return new JispFile(out.toByteArray());
	}
}
