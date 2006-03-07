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

package de.berlios.jedi.testUtilities;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import de.berlios.jedi.common.entity.jisp.JispFile;

/**
 * Common functions for JispFile tests and others tests needing retrieve data
 * from disk, such as getting a JispFile stored in disk for testing.
 */
public class DiskFilesRetriever {

	/**
	 * Creates a Jisp file from the name of a file in the disk.
	 * 
	 * @param filePath
	 *            The name of the file stored in disk.
	 * @return The Jisp file created from the disk file.
	 */
	public static JispFile getJispFile(String filePath) {
		return new JispFile(getDataFromDiskFile(filePath));
	}

	/**
	 * Gets a Jisp icondef as a JDOM Document from the name of a icondef.xml
	 * file in the disk.
	 * 
	 * @param filePath
	 *            The name of the file stored in disk.
	 * @return The Jisp icondef JDOM Document created from the disk file.
	 */
	public static Document getDocument(String filePath) {
		ByteArrayInputStream in = null;
		try {
			return new SAXBuilder().build(in = new ByteArrayInputStream(
					getDataFromDiskFile(filePath)));
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Opens a file in disk and returns its data as a bytes array.
	 * 
	 * @param filePath
	 *            The name of the file stored in disk.
	 * @return The data of the file as a bytes array.
	 */
	public static byte[] getDataFromDiskFile(String filePath) {
		File file = new File(filePath);
		byte[] fileData = new byte[(int) file.length()];

		FileInputStream fileStream = null;
		try {
			fileStream = new FileInputStream(file);

			int dataLength = fileStream.available();
			fileData = new byte[dataLength];
			int dataReaded = 0;

			while ((dataLength - dataReaded) > 0) {
				dataReaded += fileStream.read(fileData, dataReaded, dataLength
						- dataReaded);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileData;
	}

}
