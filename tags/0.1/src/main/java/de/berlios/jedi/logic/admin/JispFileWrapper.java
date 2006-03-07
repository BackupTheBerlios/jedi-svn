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

import java.io.*;
import java.util.*;
import java.util.zip.*;

import org.apache.commons.logging.LogFactory;

import de.berlios.jedi.common.entity.jisp.JispFile;
import de.berlios.jedi.common.jisp.JispStoredWrapper;
import de.berlios.jedi.common.jisp.exception.JispStoredException;

/**
 * <p>
 * This wrapper makes easy accessing Jisp file entries, encapsulating
 * decompression and file look-up tasks.
 * </p>
 * <p>
 * In case that the compressed files have a base directory, this won't be used
 * when registering the contents. That is, if a "aBaseDirectory/icondef.xml"
 * stored file must be get with the name "icondef.xml".<br>
 * However, this only affects to the base directory. That is, to get a
 * "aBaseDirectory/anotherDirectory/aFile.jpg" the name
 * "anotherDirectory/aFile.jpg" must be used.<br>
 * JispFiles can only have a base directory.
 * "aBaseDirectory/anotherBaseDirectory/" structure (being icondef.xml and the
 * rest of files children of anotherBaseDirectory) isn't valid.
 * </p>
 * <p>
 * Although other files can be in subdirectories, icondef.xml can only be in the
 * root directory or in the base directory, if it exists.
 * </p>
 * <p>
 * Extends JispStoredWrapper, adapting it to be used with JispFiles.<br>
 * Initialization is encapsulated and only a JispFile is needed to create the
 * JispFileWrapper. Constructor takes care internally to initialize the contents
 * using a JispFileWrapperInitializar.
 * </p>
 */
public class JispFileWrapper extends JispStoredWrapper {

	/**
	 * <p>
	 * Initializes JispFileWrapper adding contents from JispFile to the internal
	 * map, used to get those contents.
	 * </p>
	 * <p>
	 * In case that the compressed files have a base directory, this won't be
	 * used when registering the contents. That is, if a
	 * "aBaseDirectory/icondef.xml" stored file will be added with the name
	 * "icondef.xml".<br>
	 * However, this only affects to the base directory. That is, a
	 * "aBaseDirectory/anotherDirectory/aFile.jpg" stored file will be added
	 * with the name "anotherDirectory/aFile.jpg".<br>
	 * JispFiles can only have a base directory.
	 * "aBaseDirectory/anotherBaseDirectory/" structure (being icondef.xml and
	 * the rest of files children of anotherBaseDirectory) isn't valid.
	 * </p>
	 */
	private static class JispFileWrapperInitializer extends
			JispStoredWrapperInitializer {

		/**
		 * The JispFile to get the contents from.
		 */
		private JispFile jispFile;

		/**
		 * Constructs a new JispFileWrapperInitializer with a JispFile.
		 * 
		 * @param jispFile
		 *            The JispFile to get the contents from.
		 */
		public JispFileWrapperInitializer(JispFile jispFile) {
			super();
			this.jispFile = jispFile;
		}

		/**
		 * Reads the data from JispFile and puts files' data in the map using
		 * the filename as key.
		 * 
		 * @throws JispStoredException
		 *             If the file isn't a valid Zip file, is empty or doesn't
		 *             have a valid directory structure (icondef.xml file isn't
		 *             in root or base directory, if any).
		 */
		protected void init() throws JispStoredException {
			byte[] data = jispFile.getData();
			Map contentsSize = getContentsSize(data);

			if (contentsSize.size() == 0) {
				throw new JispStoredException(
						"The file isn't a valid Zip file or is empty");
			}

			getContents(data, contentsSize);
		}

		/**
		 * Gets the size of files in Zip and returns them in a Map, using the
		 * filename as key.<br>
		 * Size of contents is need by getContents(byte[], Map) method.
		 * 
		 * @param data
		 *            The zip file data to read the stored files from.
		 * @return The Map with files size stored using filenames as keys.
		 * @throws JispStoredException
		 *             If the file isn't a valid Zip file (an IOException
		 *             happened)
		 */
		private Map getContentsSize(byte[] data) throws JispStoredException {
			Map contentsSize = new Hashtable();
			ZipInputStream input = new ZipInputStream(new BufferedInputStream(
					new ByteArrayInputStream(data)));

			try {
				ZipEntry entry;
				while ((entry = input.getNextEntry()) != null) {
					if (entry.isDirectory()) {
						continue;
					}
					input.closeEntry();
					contentsSize.put(entry.getName(), new Integer((int) entry
							.getSize()));
				}
			} catch (IOException e) {
				LogFactory.getLog(JispFileWrapperInitializer.class).error(
						"The file isn't a valid Zip file: IOException", e);
				throw new JispStoredException(
						"The file isn't a valid Zip file: IOException: "
								+ e.getMessage());
			} finally {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
			return contentsSize;
		}

		/**
		 * Gets the data from the stored files in the zip, and adds it to the
		 * contents Map.<br>
		 * Size of contents are needed to get them right.
		 * 
		 * @param data
		 *            The zip file data to get stored files from.
		 * @param contentsSize
		 *            The Map with size of contents, stored with filename as
		 *            key.
		 * @throws JispStoredException
		 *             If the file isn't a valid Zip file, or doesn't have a
		 *             valid directory structure (icondef.xml file isn't in root
		 *             or base directory, if any).
		 */
		private void getContents(byte[] data, Map contentsSize)
				throws JispStoredException {
			String baseDirectoryName = getBaseDirectoryName(contentsSize
					.keySet().iterator());

			ZipInputStream input = new ZipInputStream(new BufferedInputStream(
					new ByteArrayInputStream(data)));
			try {
				ZipEntry entry;
				while ((entry = input.getNextEntry()) != null) {
					if (entry.isDirectory()) {
						continue;
					}

					byte[] entryData;
					int entrySize = ((Integer) contentsSize
							.get(entry.getName())).intValue();
					entryData = new byte[entrySize];
					int dataReaded = 0;
					while (entrySize - dataReaded > 0) {
						dataReaded += input.read(entryData, dataReaded,
								entrySize - dataReaded);
					}

					addFileToInitializedContentsMap(entry.getName(), entryData,
							baseDirectoryName);
					input.closeEntry();
				}
			} catch (NegativeArraySizeException e) {
				// Shouldn't be thrown...
				LogFactory.getLog(JispFileWrapperInitializer.class).error(
						"The file isn't a valid Zip file:"
								+ " unknown size of entry", e);
				throw new JispStoredException(
						"The file isn't a valid Zip file: unknown size of entry");
			} catch (ZipException e) {
				LogFactory.getLog(JispFileWrapperInitializer.class).error(
						"The file isn't a valid Zip file", e);
				throw new JispStoredException(
						"The file isn't a valid Zip file: " + e.getMessage());
			} catch (IOException e) {
				LogFactory.getLog(JispFileWrapperInitializer.class).error(
						"The file isn't a valid Zip file: IOException", e);
				throw new JispStoredException(
						"The file isn't a valid Zip file: IOException: "
								+ e.getMessage());
			} finally {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
		}

		/**
		 * Returns the base directory name of the fileNames, if any.<br>
		 * The base directory includes final "/".
		 * 
		 * @param fileNamesIterator
		 *            An iterator through all the stored files names.
		 * @return The base directory name, or null if it doesn't exists.
		 * 
		 * @throws JispStoredException
		 *             If there is not a valid directory structure (icondef.xml
		 *             file isn't in root or base directory, if any).
		 */
		private String getBaseDirectoryName(Iterator fileNamesIterator)
				throws JispStoredException {
			while (fileNamesIterator.hasNext()) {
				String fileName = (String) fileNamesIterator.next();
				if (fileName.endsWith("icondef.xml")) {
					if (fileName.startsWith("icondef.xml")) {
						return null;
					}
					if (fileName.indexOf("/") != fileName.lastIndexOf("/")) {
						throw new JispStoredException(
								"Invalid directory structure:"
										+ " icondef.xml file path is: "
										+ fileName);
					}
					return fileName.substring(0, fileName.indexOf("/") + 1);
				}
			}
			return null;
		}

		/**
		 * Adds a file to the initializedContents Map.<br>
		 * If the base directory doesn't exist (is null), the key used to store
		 * the file's data is the file's name.<br>
		 * If the base directory exists, the file's name must begin with the
		 * base directory's name. The key used to store the file's data is the
		 * file's name removing the base directory name from it.
		 * 
		 * @param fileName
		 *            The name of the file to add.
		 * @param fileData
		 *            The data of the file to add.
		 * @param baseDirectoryName
		 *            The name of the base directory, if any.
		 */
		private void addFileToInitializedContentsMap(String fileName,
				byte[] fileData, String baseDirectoryName) {
			if (baseDirectoryName != null) {
				fileName = fileName.substring(baseDirectoryName.length());
			}

			initializedContents.put(fileName, fileData);
		}
	}

	/**
	 * Creates a new wrapper for a jisp file.
	 * 
	 * @param jispFile
	 *            The jisp file to be wrapped.
	 * @throws JispStoredException
	 *             If the file isn't a valid Zip file, is empty, doesn't have a
	 *             valid directory structure (icondef.xml file isn't in root or
	 *             base directory, if any) or doesn't contain an icondef.xml
	 *             file.
	 */
	public JispFileWrapper(JispFile jispFile) throws JispStoredException {
		super(new JispFileWrapperInitializer(jispFile));
	}
}
