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

package de.berlios.jedi.common.jisp;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import de.berlios.jedi.common.entity.jisp.JispIcon;
import de.berlios.jedi.common.entity.jisp.JispObject;
import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.jisp.exception.JispIncompletePackageException;

/**
 * <p>
 * Abstract class to store a JispPackage.<br>
 * Subclasses store icondef.xml file and all multimedia objects in a specific
 * storage type, such as a jisp file.
 * </p>
 * 
 * <p>
 * This class uses Template design pattern. Subclasses should implement abstract
 * method addFileToStore. It will store a file (icondef.xml or a multimedia
 * object) with the specified name and data.<br>
 * The process of storing the elements is entirely responsability of the
 * subclasses. This class is made to obtain the icondef.xml file and all the
 * multimedia objects.
 * </p>
 * 
 * <p>
 * Subclasses should call store() or store(String) to begin the process of
 * storing the Jisp package. All the files will be added into a root directory,
 * with a name asigned automatically or specified, depending on the store method
 * called.
 * </p>
 */
public abstract class JispPackageStorer {

	/**
	 * The JispPackage to store.
	 */
	protected JispPackage jispPackage;

	/**
	 * <p>
	 * Creates a new JispPackageStorer specifying the jispPackage to store.
	 * </p>
	 * <p>
	 * jispPackage can not be null.
	 * </p>
	 * 
	 * @param jispPackage
	 *            The JispPackage to store.
	 */
	public JispPackageStorer(JispPackage jispPackage) {
		this.jispPackage = jispPackage;
	}

	/**
	 * <p>
	 * Subclasses should implement the method so it adds a new file to the
	 * store, given the filename and its data.
	 * </p>
	 * <p>
	 * filename and data won't be null.
	 * </p>
	 * 
	 * @param filename
	 *            The name of the file to add.
	 * @param data
	 *            The data of the file.
	 */
	protected abstract void addFileToStore(String filename, byte[] data);

	/**
	 * Obtains the icondef.xml file and all the multimedia objects and stores
	 * them using the default root directory name.<br>
	 * The default root directory name is got from the package's name, removing
	 * the white spaces and capitalizing the first letter of the words.<br>
	 * Subclasses should call this method when they need to store the
	 * JispPackage.
	 * 
	 * @throws JispIncompletePackageException
	 *             If metadata or icons aren't complete.
	 * @throws UnsupportedOperationException
	 *             If server doesn't support UTF-8 encoding.
	 */
	protected void store() throws JispIncompletePackageException,
			UnsupportedOperationException {
		store(getDefaultRootDirectoryName());
	}

	/**
	 * Obtains the icondef.xml file and all the multimedia objects and stores
	 * them using a specific root directory name.<br>
	 * If the root directory name doesn't ends in "/", it is added
	 * automatically.<br>
	 * Subclasses should call this method when they need to store the
	 * JispPackage using a specific root directory name.
	 * 
	 * @param rootDirectoryName
	 *            The name of the root directory where the icondef.xml and the
	 *            objects will be saved.
	 * @throws JispIncompletePackageException
	 *             If metadata or icons aren't complete.
	 * @throws UnsupportedOperationException
	 *             If server doesn't support UTF-8 encoding.
	 */
	protected void store(String rootDirectoryName)
			throws JispIncompletePackageException,
			UnsupportedOperationException {
		if (!rootDirectoryName.endsWith("/")) {
			rootDirectoryName = rootDirectoryName + "/";
		}
		addIcondef(rootDirectoryName);
		addObjects(rootDirectoryName);
	}

	/**
	 * Returns the default root directory name got from the package name.<br>
	 * The default root directory name is got from the package's name, removing
	 * the white spaces and capitalizing the first letter of the words.
	 * 
	 * @return The default root directory name.
	 */
	private String getDefaultRootDirectoryName() {
		String defaultRootDirectoryName = "";

		String packageName = jispPackage.getJispMetadata().getName();
		packageName = packageName.trim();

		int i = 0;
		while (i != -1) {
			packageName = Character.toUpperCase(packageName.charAt(i))
					+ packageName.substring(i + 1);
			i = packageName.indexOf(" ");
			if (i != -1) {
				defaultRootDirectoryName = defaultRootDirectoryName
						+ packageName.substring(0, i);
				i++;
			} else {
				defaultRootDirectoryName = defaultRootDirectoryName
						+ packageName.substring(0);
			}
		}

		return defaultRootDirectoryName;
	}

	/**
	 * Adds icondef.xml file to the store, using an icondef JDOM Document
	 * created from the Jisp package.<br>
	 * The icondef.xml will be added in the specified root directory.
	 * 
	 * @param rootDirectoryName
	 *            The name of the root directory to save the icondef.xml file
	 *            in.
	 * @throws JispIncompletePackageException
	 *             If metadata or icons aren't complete.
	 * @throws UnsupportedOperationException
	 *             If server doesn't support UTF-8 encoding.
	 */
	private void addIcondef(String rootDirectoryName)
			throws JispIncompletePackageException,
			UnsupportedOperationException {
		Document icondef = new JispPackageToIcondefXml(jispPackage)
				.toIcondefXml();
		String icondefString = new XMLOutputter(Format.getPrettyFormat())
				.outputString(icondef);

		try {
			addFileToStore(rootDirectoryName + "icondef.xml", icondefString
					.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			LogFactory.getLog(JispPackageStorer.class).fatal(
					"The server doesn't support UTF-8 encoding!!"
							+ " No icondef.xml files can be created!!", e);
			throw new UnsupportedOperationException(
					"The server doesn't support UTF-8 encoding!!!"
							+ " No icondef.xml files can be created!!");
		}
	}

	/**
	 * Adds all the data from the objects in the package to the store.<br>
	 * Those objects will be stored with the same name used in icondef.xml (full
	 * name, included directories), provided they are not duplicated, in the
	 * root directory specified.
	 * 
	 * @param rootDirectoryName
	 *            The name of the root directory to save the objects in.
	 */
	private void addObjects(String rootDirectoryName) {
		Iterator icons = jispPackage.jispIconsIterator();

		List addedObjects = new ArrayList();
		while (icons.hasNext()) {
			JispIcon jispIcon = (JispIcon) icons.next();

			addObjectsFromIcon(jispIcon, addedObjects, rootDirectoryName);
		}
	}

	/**
	 * Adds all the objects in the icon to the store.<br>
	 * No duplicated objects (based only on the name) will be added.<br>
	 * The object name to be added will be composed with rootDirectoryName and
	 * the object's name.
	 * 
	 * @param jispIcon
	 *            The JispIcon to get the objects from.
	 * @param addedObjects
	 *            An ArrayList where names of already added files are stored.
	 * @param rootDirectoryName
	 *            The name of the root directory to save the object in.
	 */
	private void addObjectsFromIcon(JispIcon jispIcon, List addedObjects,
			String rootDirectoryName) {
		Iterator objects = jispIcon.jispObjectsIterator();

		while (objects.hasNext()) {
			JispObject jispObject = (JispObject) objects.next();

			if (!addedObjects.contains(jispObject.getName())) {
				addFileToStore(rootDirectoryName + jispObject.getName(),
						jispObject.getData());
				addedObjects.add(jispObject.getName());
			}
		}
	}
}
