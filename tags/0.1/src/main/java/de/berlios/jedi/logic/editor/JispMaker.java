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

import java.util.Iterator;

import de.berlios.jedi.common.entity.jisp.*;

/**
 * Provides all the needed functionality to edit jisp entities.<br>
 * As jisp entities are beans, they have no logic to control how authors are
 * added in metadata, for example. This class takes care of all the checkings
 * and operations that need to be made.
 */
public class JispMaker {

	/**
	 * <p>
	 * Adds all the JispIcon from a JispPackage to another JispPackage.<br>
	 * If any of the JispIcons was already added (or one equals to it), that
	 * JispIcon isn't added again.
	 * </p>
	 * <p>
	 * jispPackageToAdd and jispPackage can not be null.
	 * </p>
	 * 
	 * @param jispPackageToAdd
	 *            The JispPackage which contains the JispIcons to add.
	 * @param jispPackage
	 *            The JispPackage to add to.
	 */
	public void addJispPackageToJispPackage(JispPackage jispPackageToAdd, JispPackage jispPackage) {
		Iterator jispIconsIterator = jispPackageToAdd.jispIconsIterator();
		while (jispIconsIterator.hasNext()) {
			addJispIconToJispPackage((JispIcon)jispIconsIterator.next(), jispPackage);
		}
	}

	/**
	 * <p>
	 * Adds a JispIcon to a JispPackage.<br>
	 * If the JispIcon was already added (or one equals to it), nothing is
	 * added.
	 * </p>
	 * <p>
	 * jispIcon and jispPackage can not be null.
	 * </p>
	 * 
	 * @param jispIcon
	 *            The JispIcon to be added.
	 * @param jispPackage
	 *            The JispPackage to add to.
	 */
	public void addJispIconToJispPackage(JispIcon jispIcon,
			JispPackage jispPackage) {
		if (!jispPackage.containsJispIcon(jispIcon))
			jispPackage.addJispIcon(jispIcon);
	}

	/**
	 * <p>
	 * Adds a JispObject to a JispIcon.<br>
	 * If the JispObject was already added (or one equals to it), nothing is
	 * added.
	 * </p>
	 * <p>
	 * jispObject and jispIcon can not be null.
	 * </p>
	 * 
	 * @param jispObject
	 *            The JispObject to be added.
	 * @param jispIcon
	 *            The JispIcon to add to.
	 */
	public void addJispObjectToJispIcon(JispObject jispObject, JispIcon jispIcon) {
		if (!jispIcon.containsJispObject(jispObject))
			jispIcon.addJispObject(jispObject);
	}

	/**
	 * <p>
	 * Adds a JispText to a JispIcon.<br>
	 * If the JispText was already added (or one equals to it), nothing is
	 * added.
	 * </p>
	 * <p>
	 * jispText and jispIcon can not be null.
	 * </p>
	 * 
	 * @param jispText
	 *            The JispText to be added.
	 * @param jispIcon
	 *            The JispIcon to add to.
	 */
	public void addJispTextToJispIcon(JispText jispText, JispIcon jispIcon) {
		if (!jispIcon.containsJispText(jispText))
			jispIcon.addJispText(jispText);
	}

	/**
	 * <p>
	 * Removes a JispIcon from a JispPackage.
	 * </p>
	 * <p>
	 * jispIcon and jispPackage can not be null.
	 * </p>
	 * 
	 * @param jispIcon
	 *            The JispIcon to be removed.
	 * @param jispPackage
	 *            The JispPackage to remove from.
	 * @return True if the JispPackage contained the specified JispIcon.
	 */
	public boolean removeJispIconFromJispPackage(JispIcon jispIcon,
			JispPackage jispPackage) {
		return jispPackage.removeJispIcon(jispIcon);
	}

	/**
	 * <p>
	 * Removes a JispObject from a JispIcon.
	 * </p>
	 * <p>
	 * jispObject and jispIcon can not be null.
	 * </p>
	 * 
	 * @param jispObject
	 *            The JispObject to be removed.
	 * @param jispIcon
	 *            The JispIcon to remove from.
	 * @return True if the JispIcon contained the specified JispObject.
	 */
	public boolean removeJispObjectFromJispIcon(JispObject jispObject,
			JispIcon jispIcon) {
		return jispIcon.removeJispObject(jispObject);
	}

	/**
	 * <p>
	 * Removes a JispText from a JispIcon.
	 * </p>
	 * <p>
	 * jispText and jispIcon can not be null.
	 * </p>
	 * 
	 * @param jispText
	 *            The JispText to be removed.
	 * @param jispIcon
	 *            The JispIcon to remove from.
	 * @return True if the JispIcon contained the specified JispText.
	 */
	public boolean removeJispTextFromJispIcon(JispText jispText,
			JispIcon jispIcon) {
		return jispIcon.removeJispText(jispText);
	}
}
