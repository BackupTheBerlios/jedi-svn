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
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import de.berlios.jedi.common.entity.jisp.JispIcon;
import de.berlios.jedi.common.entity.jisp.JispObject;
import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.jisp.exception.JispIncompletePackageException;

/**
 * A initializer for JispStoredWrapper used for testing purposes.<br>
 * It's created using a JispPackage, and gets the icondef and multimedia objects
 * to add them in the initializedContents Map.<br>
 * It can also stored all the objects, or all the objects except one. This is
 * used in JispStoredToJispPackage.toJispPackage() testing.
 */
public class JispStoredWrapperInitializerForTests extends
		JispStoredWrapper.JispStoredWrapperInitializer {

	/**
	 * The JispPackage to use.
	 */
	private JispPackage jispPackage;

	/**
	 * If all the objects should be added to the map.
	 */
	private boolean complete;

	/**
	 * Creates a new initializer adding all the objects to the map.
	 * 
	 * @param jispPackage
	 *            The JispPackage to use.
	 */
	public JispStoredWrapperInitializerForTests(JispPackage jispPackage) {
		this(jispPackage, true);
	}

	/**
	 * Creates a new initializer specifying if all the objects should be added
	 * to the map.<br>
	 * It is used by JispStoredToJispPackageTest to test toJispPackage() if not
	 * all the icondef specified objects exist.
	 * 
	 * @param jispPackage
	 *            The JispPackage to use.
	 * @param complete
	 *            If all the objects should be added.
	 */
	public JispStoredWrapperInitializerForTests(JispPackage jispPackage,
			boolean complete) {
		this.jispPackage = jispPackage;
		this.complete = complete;
	}

	/**
	 * Stores the contents in the initializedContents Map.
	 */
	protected void init() {
		addIcondef();
		addContents();
	}

	/**
	 * Gets the icondef and stores it as a UTF-8 encoded file.
	 */
	private void addIcondef() {
		Document icondefDocument = null;
		try {
			icondefDocument = new JispPackageToIcondefXml(jispPackage)
					.toIcondefXml();
		} catch (JispIncompletePackageException e1) {
			e1.printStackTrace();
		}

		String icondefString = new XMLOutputter(Format.getPrettyFormat())
				.outputString(icondefDocument);

		try {
			initializedContents.put("icondef.xml", icondefString
					.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the JispObjects and stores them using their name as a key in the
	 * initializedContents Map.<br>
	 * If an object has the same name as another previously added, overwrites
	 * it. Depending on "complete" value, all the objects are stored, or all
	 * except one (the first).
	 */
	private void addContents() {
		Iterator jispIconsIterator = jispPackage.jispIconsIterator();
		if (!complete) {
			jispIconsIterator.next();
		}
		while (jispIconsIterator.hasNext()) {
			Iterator jispObjectsIterator = ((JispIcon) jispIconsIterator.next())
					.jispObjectsIterator();
			while (jispObjectsIterator.hasNext()) {
				JispObject jispObject = (JispObject) jispObjectsIterator.next();
				initializedContents.put(jispObject.getName(), jispObject
						.getData());
			}
		}
	}
}
