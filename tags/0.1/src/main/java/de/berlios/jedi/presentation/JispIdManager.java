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

package de.berlios.jedi.presentation;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.berlios.jedi.common.entity.jisp.*;

/**
 * <p>
 * Associates Jisp Elements with an id, and makes possible retrieve the Jisp
 * Elements by its id.<br>
 * When an element is registered, all its sub-elements are also registered (i.e.
 * registering a package registers all its icons).<br>
 * Registered elements can be get by its id. Or an id can also be get by the
 * element it identifies (must be the same element, not one equals to it). When
 * an association between a Jisp Element and its id is no longer needed, the
 * Jisp Element should be deregistered.
 * </p>
 * <p>
 * Elements ids are Strings. Ids are assigned using a simple mechanism:
 * <ul>
 * <li>-Jisp Packages id is "'JispPackageNumber'p"</li>
 * <li>-Jisp Icons id is "'JispIconNumber'i"</li>
 * <li>-Jisp Objects id is "'JispObjectNumber'o"</li>
 * <li>-Jisp Text id is "'JispTextNumber't"</li>
 * </ul>
 * All the numbers are integers which always increment. The next integer to the
 * last used is assigned, even when previous elements are deregistered.
 * </p>
 */
public class JispIdManager {
	// TODO: should common packages share the same id between different
	// instances of JispIdManager?

	/**
	 * The Map where Jisp Elements are registered with their id.
	 */
	private Map elements;

	/**
	 * Jisp Package last used number.
	 */
	private int jispPackageNumber;

	/**
	 * Jisp Icon last used number.
	 */
	private int jispIconNumber;

	/**
	 * Jisp Object last used number.
	 */
	private int jispObjectNumber;

	/**
	 * Jisp Text last used number.
	 */
	private int jispTextNumber;

	/**
	 * Creates a new JispIdManager.<br>
	 * All id numbers are initialized to 0.
	 */
	public JispIdManager() {
		elements = new Hashtable();

		jispPackageNumber = 0;
		jispIconNumber = 0;
		jispObjectNumber = 0;
		jispTextNumber = 0;
	}

	/**
	 * Registers all the JispPackages in the JispPackagesList and returns a list
	 * with the assigned ids.<br>
	 * If any JispPackage is already registered, it simply adds the already
	 * assigned id to the list.
	 * 
	 * @param jispPackagesList
	 *            The JispPackagesList to register the contained JispPackages in
	 *            it.
	 * @return The list with the assigned ids to the JispPackages.
	 */
	public List registerJispPackagesList(JispPackagesList jispPackagesList) {
		List jispPackageIdsList = new ArrayList();

		Iterator jispPackagesListIterator = jispPackagesList.iterator();
		while (jispPackagesListIterator.hasNext()) {
			jispPackageIdsList
					.add(registerJispPackage((JispPackage) jispPackagesListIterator
							.next()));
		}

		return jispPackageIdsList;
	}

	/**
	 * Registers a JispPackage and returns the assigned id.<br>
	 * All JispIcons in the JispPackage are also registered.<br>
	 * If JispPackage is already registered, it simply returns the already
	 * assigned id.
	 * 
	 * @param jispPackage
	 *            The JispPackage to register.
	 * @return The assigned id to the JispPackage.
	 */
	public String registerJispPackage(JispPackage jispPackage) {
		String jispPackageId = getId(jispPackage);

		if (jispPackageId != null) {
			return jispPackageId;
		}

		jispPackageId = jispPackageNumber + "p";

		elements.put(jispPackageId, jispPackage);
		jispPackageNumber++;

		Iterator iterator = jispPackage.jispIconsIterator();
		while (iterator.hasNext()) {
			registerJispIcon((JispIcon) iterator.next());
		}

		return jispPackageId;
	}

	/**
	 * Registers a JispIcon and returns the assigned id.<br>
	 * All JispObjects and JispTexts in the JispIcon are also registered.<br>
	 * If JispIcon is already registered, it simply returns the already assigned
	 * id.
	 * 
	 * @param jispIcon
	 *            The JispIcon to register.
	 * @return The assigned id to the JispIcon.
	 */
	public String registerJispIcon(JispIcon jispIcon) {
		String jispIconId = getId(jispIcon);

		if (jispIconId != null) {
			return jispIconId;
		}

		jispIconId = jispIconNumber + "i";

		elements.put(jispIconId, jispIcon);
		jispIconNumber++;

		Iterator objectsIterator = jispIcon.jispObjectsIterator();
		while (objectsIterator.hasNext()) {
			registerJispObject((JispObject) objectsIterator.next());
		}

		Iterator textsIterator = jispIcon.jispTextsIterator();
		while (textsIterator.hasNext()) {
			registerJispText((JispText) textsIterator.next());
		}

		return jispIconId;
	}

	/**
	 * Registers a JispObject and returns the assigned id.<br>
	 * If JispObject is already registered, it simply returns the already
	 * assigned id.
	 * 
	 * @param jispObject
	 *            The JispObject to register.
	 * @return The assigned id to the JispObject.
	 */
	public String registerJispObject(JispObject jispObject) {
		String jispObjectId = getId(jispObject);

		if (jispObjectId != null) {
			return jispObjectId;
		}

		jispObjectId = jispObjectNumber + "o";

		elements.put(jispObjectId, jispObject);
		jispObjectNumber++;

		return jispObjectId;
	}

	/**
	 * Registers a JispText and returns the assigned id.<br>
	 * If JispText is already registered, it simply returns the already assigned
	 * id.
	 * 
	 * @param jispText
	 *            The JispText to register.
	 * @return The assigned id to the JispText.
	 */
	public String registerJispText(JispText jispText) {
		String jispTextId = getId(jispText);

		if (jispTextId != null) {
			return jispTextId;
		}

		jispTextId = jispTextNumber + "t";

		elements.put(jispTextId, jispText);
		jispTextNumber++;

		return jispTextId;
	}

	/**
	 * Returns the id for the specified JispPackage, or null if it isn't
	 * registered.
	 * 
	 * @param jispPackage
	 *            The JispPackage to get its id.
	 * @return The id for the specified JispPackage, or null if it isn't
	 *         registered.
	 */
	public String getId(JispPackage jispPackage) {
		return getId((Object) jispPackage);
	}

	/**
	 * Returns the id for the specified JispIcon, or null if it isn't
	 * registered.
	 * 
	 * @param jispIcon
	 *            The JispIcon to get its id.
	 * @return The id for the specified JispIcon, or null if it isn't
	 *         registered.
	 */
	public String getId(JispIcon jispIcon) {
		return getId((Object) jispIcon);
	}

	/**
	 * Returns the id for the specified JispObject, or null if it isn't
	 * registered.
	 * 
	 * @param jispObject
	 *            The JispObject to get its id.
	 * @return The id for the specified JispObject, or null if it isn't
	 *         registered.
	 */
	public String getId(JispObject jispObject) {
		return getId((Object) jispObject);
	}

	/**
	 * Returns the id for the specified JispText, or null if it isn't
	 * registered.
	 * 
	 * @param jispText
	 *            The JispText to get its id.
	 * @return The id for the specified JispText, or null if it isn't
	 *         registered.
	 */
	public String getId(JispText jispText) {
		return getId((Object) jispText);
	}

	/**
	 * Returns the JispPackage registered with the specified id, or null if
	 * there is no JispPackage with this id.
	 * 
	 * @param id
	 *            The id of the JispPackage to get.
	 * @return The JispPackage registered with the specified id, or null if
	 *         there is no JispPackage with this id.
	 */
	public JispPackage getJispPackage(String id) {
		try {
			return (JispPackage) elements.get(id);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Returns the JispIcon registered with the specified id, or null if there
	 * is no JispIcon with this id.
	 * 
	 * @param id
	 *            The id of the JispIcon to get.
	 * @return The JispIcon registered with the specified id, or null if there
	 *         is no JispIcon with this id.
	 */
	public JispIcon getJispIcon(String id) {
		try {
			return (JispIcon) elements.get(id);
		} catch (ClassCastException e) {
			return null;
		}

	}

	/**
	 * Returns the JispObject registered with the specified id, or null if there
	 * is no JispObject with this id.
	 * 
	 * @param id
	 *            The id of the JispObject to get.
	 * @return The JispObject registered with the specified id, or null if there
	 *         is no JispObject with this id.
	 */
	public JispObject getJispObject(String id) {
		try {
			return (JispObject) elements.get(id);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Returns the JispText registered with the specified id, or null if there
	 * is no JispText with this id.
	 * 
	 * @param id
	 *            The id of the JispText to get.
	 * @return The JispText registered with the specified id, or null if there
	 *         is no JispText with this id.
	 */
	public JispText getJispText(String id) {
		try {
			return (JispText) elements.get(id);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Deregisters a JispPackage, indicating if it was already registered or
	 * not.<br>
	 * All JispIcons in the JispPackage are also deregistered.<br>
	 * If the JispPackage wasn't registered returns false.
	 * 
	 * @param jispPackage
	 *            The JispPackage to deregister.
	 * @return True if the JispPackage was deregistered, false if it wasn't
	 *         registered.
	 */
	public boolean deregisterJispPackage(JispPackage jispPackage) {
		boolean deleted = elements.remove(getId(jispPackage)) != null ? true
				: false;

		if (deleted) {
			Iterator iconsIterator = jispPackage.jispIconsIterator();
			while (iconsIterator.hasNext()) {
				deregisterJispIcon((JispIcon) iconsIterator.next());
			}
		}

		return deleted;
	}

	/**
	 * Deregisters a JispIcon, indicating if it was already registered or not.<br>
	 * All JispObjects and JispTexts in the JispIcon are also deregistered.<br>
	 * If the JispIcon wasn't registered returns false.
	 * 
	 * @param jispIcon
	 *            The JispIcon to deregister.
	 * @return True if the JispIcon was deregistered, false if it wasn't
	 *         registered.
	 */
	public boolean deregisterJispIcon(JispIcon jispIcon) {
		boolean deleted = elements.remove(getId(jispIcon)) != null ? true
				: false;

		if (deleted) {
			Iterator objectsIterator = jispIcon.jispObjectsIterator();
			while (objectsIterator.hasNext()) {
				deregisterJispObject((JispObject) objectsIterator.next());
			}

			Iterator textsIterator = jispIcon.jispTextsIterator();
			while (textsIterator.hasNext()) {
				deregisterJispText((JispText) textsIterator.next());
			}
		}

		return deleted;
	}

	/**
	 * Deregisters a JispObject, indicating if it was already registered or not.<br>
	 * If the JispObject wasn't registered returns false.
	 * 
	 * @param jispObject
	 *            The JispObject to deregister.
	 * @return True if the JispObject was deregistered, false if it wasn't
	 *         registered.
	 */
	public boolean deregisterJispObject(JispObject jispObject) {
		return elements.remove(getId(jispObject)) != null ? true : false;
	}

	/**
	 * Deregisters a JispText, indicating if it was already registered or not.<br>
	 * If the JispText wasn't registered returns false.
	 * 
	 * @param jispText
	 *            The JispText to deregister.
	 * @return True if the JispText was deregistered, false if it wasn't
	 *         registered.
	 */
	public boolean deregisterJispText(JispText jispText) {
		return elements.remove(getId(jispText)) != null ? true : false;
	}

	/**
	 * Returns the id used to register the object in the elements map, or null
	 * if it isn't registered.<br>
	 * The object must be the same registered, another object equals to it (and
	 * not registered) will return a null key.
	 * 
	 * @param object
	 *            The object to get its id.
	 * @return The id used to register the object in the elements map, or null
	 *         if it isn't registered.
	 */
	private String getId(Object object) {
		String id = null;
		Iterator iterator = elements.entrySet().iterator();

		while (id == null && iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();

			if (entry.getValue() == object) {
				id = (String) entry.getKey();
			}
		}

		return id;
	}
}
