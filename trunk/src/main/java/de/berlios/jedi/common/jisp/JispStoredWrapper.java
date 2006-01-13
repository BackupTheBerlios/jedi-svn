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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import de.berlios.jedi.common.config.ConfigurationFactory;
import de.berlios.jedi.common.jisp.exception.JispInvalidIcondefException;
import de.berlios.jedi.common.jisp.exception.JispStoredException;

/**
 * <p>
 * Class which encapsulates getting contents (icondef.xml and jisp objects) from
 * a stored jisp package.<br>
 * This class gives the needed methods to get those files, but the contents
 * should be initialized with a JispStoredWrapperInitializer. Initialization of
 * contents is the only thing that varies depending on the source of stored
 * jisp.
 * </p>
 * 
 * <p>
 * JispStoredWrapperInitializer is a internal static abstract class that must be
 * subclassed to be used.
 * </p>
 * 
 * <p>
 * A JispStoredWrapper can be used with a JispStoredToJispPackage to get the
 * equivalent JispPackage.
 * </p>
 * 
 * @see de.berlios.jedi.common.jisp.JispStoredToJispPackage
 */
public class JispStoredWrapper {

	/**
	 * <p>
	 * Initializer for JispStoredWrapper.<br>
	 * Subclasses of this class are used in JispStoreWrapper constructor.
	 * </p>
	 * 
	 * <p>
	 * This class retrieves all the files (icondef.xml and multimedia objects,
	 * but no directories if they exist) and add their data as byte[] to
	 * initializedContents, using their full name (including path, if they have)
	 * as key.
	 * </p>
	 * 
	 * <p>
	 * Subclass of this class should implement init method. Init method is
	 * automatically called when needed. However, it can also be called
	 * explicitly in subclass constructor (altough isn't needed).
	 * </p>
	 * 
	 * @see JispStoredWrapperInitializer#init()
	 */
	protected static abstract class JispStoredWrapperInitializer {

		/**
		 * The initialized contents for the JispStoredWrapper.
		 */
		protected Map initializedContents;

		/**
		 * Creates the initializedContents Map.
		 */
		protected JispStoredWrapperInitializer() {
			initializedContents = new Hashtable();
		}

		/**
		 * Subclasses should implement this method so it retrieves all the files
		 * (icondef.xml and multimedia objects, but no directories if they
		 * exist) and add their data as byte[] to initializedContents Map, using
		 * their full name (including path, if they have) as key.
		 * 
		 * @throws JispStoredException
		 *             If a problem occured when initializing the
		 *             JispStoredWrapper.
		 */
		protected abstract void init() throws JispStoredException;

		/**
		 * Returns the initialized contents Map.
		 * 
		 * @return The initialized contents Map.
		 * 
		 * @throws JispStoredException
		 *             If a problem occured when initializing the
		 *             JispStoredWrapper.
		 */
		private Map getInitializedContents() throws JispStoredException {
			if (initializedContents.isEmpty()) {
				init();
			}
			return initializedContents;
		}
	}

	/**
	 * The contents (icondef.xml and jisp objects) of the stored jisp.
	 */
	private Map contents;

	/**
	 * Initializes the contents and validates the stored jisp.<br>
	 * At this level, a stored Jisp is valid if it has an icondef.xml file.
	 * 
	 * @param init
	 *            The concrete JispStoredWrapperInitializer to use.
	 * @throws JispStoredException
	 *             If there is no icondef.xml
	 */
	public JispStoredWrapper(JispStoredWrapperInitializer init)
			throws JispStoredException {
		contents = init.getInitializedContents();
		validate();
	}

	/**
	 * Returns the icondef.xml file in the stored Jisp as a JDOM Document.
	 * 
	 * @return The icondef.xml file as a JDOM Document.
	 * @throws JispInvalidIcondefException
	 *             If the icondef.xml file is not valid or not well-formed.
	 * @throws UnsupportedOperationException
	 *             If there's no support for the icondef.xml file encoding.
	 */
	public Document getIcondef() throws JispInvalidIcondefException,
			UnsupportedOperationException {
		Document icondef = null;

		SAXBuilder builder = getSAXBuilder();

		ByteArrayInputStream in = null;
		try {
			icondef = builder.build(in = new ByteArrayInputStream(
					(byte[]) contents.get("icondef.xml")));
		} catch (JDOMException e) {
			LogFactory.getLog(JispPackageStorer.class).error(
					"The icondef.xml file is not valid: " + e.getMessage(), e);
			throw new JispInvalidIcondefException(
					"The icondef.xml file is not valid: " + e.getMessage());
		} catch (UTFDataFormatException e) {
			LogFactory.getLog(JispPackageStorer.class).error(
					"The icondef.xml file is not valid: "
							+ "UTF-8 encoding error", e);
			throw new JispInvalidIcondefException(
					"The icondef.xml file is not valid: "
							+ "UTF-8 encoding error");
		} catch (UnsupportedEncodingException e) {
			LogFactory.getLog(JispPackageStorer.class).error(
					"The server doesn't support icondef.xml file encoding!!!"
							+ " No jisp processing can be made!!", e);
			throw new UnsupportedOperationException(
					"The server doesn't support icondef.xml file encoding!!!"
							+ " No jisp processing can be made!!");
		} catch (IOException e) {
			LogFactory.getLog(JispPackageStorer.class).error(
					"The icondef.xml file is not valid: IOException: "
							+ e.getMessage(), e);
			throw new JispInvalidIcondefException(
					"The icondef.xml file is not valid: IOException: "
							+ e.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
		}
		return icondef;
	}

	/**
	 * <p>
	 * Returns the object's data in the stored Jisp with the specified name, or
	 * null if it doesn't exist.
	 * <p>
	 * <p>
	 * name can not be null
	 * </p>
	 * 
	 * @param name
	 *            The name of the object to get.
	 * @return The object's data in the stored Jisp with the specified name,
	 *         null if it doesn't exist.
	 */
	public byte[] getObject(String name) {
		return (byte[]) contents.get(name);
	}

	/**
	 * Returns the names of all the files inside the stored Jisp.
	 * 
	 * @return The names of all contained files in the stored Jisp.
	 */
	public String[] getFilesNames() {
		String[] filesNames = new String[contents.size()];
		Iterator keys = contents.keySet().iterator();

		int i = 0;
		while (keys.hasNext()) {
			filesNames[i] = (String) keys.next();
			i++;
		}

		return filesNames;
	}

	/**
	 * Checks if it's a valid stored Jisp, throwing an exception if it doesn't
	 * validate.<br>
	 * At this level, a stored Jisp is valid if it has an icondef.xml file.
	 * 
	 * @throws JispStoredException
	 *             If there is no icondef.xml
	 */
	protected void validate() throws JispStoredException {
		if (contents.get("icondef.xml") == null) {
			throw new JispStoredException("There is no icondef.xml in the "
					+ "stored Jisp");
		}
	}

	/**
	 * Returns a SAXBuilder to build the icondef.xml file as a JDOM Document.<br>
	 * Options for the SAXBuilder are set with the configuration system.
	 * 
	 * @return A SAXBuilder.
	 */
	private SAXBuilder getSAXBuilder() {
		SAXBuilder builder;
		if (ConfigurationFactory.getConfiguration().getBoolean(
				"admin.enableValidation")) {
			builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser",
					true);
			builder.setFeature(
					"http://apache.org/xml/features/validation/schema", true);
			builder.setProperty("http://apache.org/xml/properties/schema/"
					+ "external-noNamespaceSchemaLocation",
					ConfigurationFactory.getConfiguration().getString(
							"path.JISPSchema"));

			LogFactory.getLog(JispStoredWrapper.class).info(
					"Using XML validation, Schema location: "
							+ ConfigurationFactory.getConfiguration()
									.getString("path.JISPSchema"));
		} else {
			builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser");

			LogFactory.getLog(JispStoredWrapper.class).info(
					"No XML validation used");
		}
		return builder;
	}
}
