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
 * storage type.
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
 * Subclasses should call store() to begin the process of storing the Jisp
 * package.
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
	 * them.<br>
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
		addIcondef();
		addObjects();
	}

	/**
	 * Adds icondef.xml file to the store, using an icondef JDOM Document
	 * created from the Jisp package.
	 * 
	 * @throws JispIncompletePackageException
	 *             If metadata or icons aren't complete.
	 * @throws UnsupportedOperationException
	 *             If server doesn't support UTF-8 encoding.
	 */
	private void addIcondef() throws JispIncompletePackageException,
			UnsupportedOperationException {
		Document icondef = new JispPackageToIcondefXml(jispPackage)
				.toIcondefXml();
		String icondefString = new XMLOutputter(Format.getPrettyFormat())
				.outputString(icondef);

		try {
			addFileToStore("icondef.xml", icondefString.getBytes("UTF-8"));
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
	 * name, included directories), provided it's not duplicated.
	 */
	private void addObjects() {
		Iterator icons = jispPackage.jispIconsIterator();

		List addedObjects = new ArrayList();
		while (icons.hasNext()) {
			JispIcon jispIcon = (JispIcon) icons.next();

			addObjectsFromIcon(jispIcon, addedObjects);
		}
	}

	/**
	 * Adds all the objects in the icon to the store.<br>
	 * No duplicated objects (based only on the name) will be added.
	 * 
	 * @param jispIcon
	 *            The JispIcon to get the objects from.
	 * @param addedObjects
	 *            An ArrayList where names of already added files are stored.
	 */
	private void addObjectsFromIcon(JispIcon jispIcon, List addedObjects) {
		Iterator objects = jispIcon.jispObjectsIterator();

		while (objects.hasNext()) {
			JispObject jispObject = (JispObject) objects.next();

			if (!addedObjects.contains(jispObject.getName())) {
				addFileToStore(jispObject.getName(), jispObject.getData());
				addedObjects.add(jispObject.getName());
			}
		}
	}
}
