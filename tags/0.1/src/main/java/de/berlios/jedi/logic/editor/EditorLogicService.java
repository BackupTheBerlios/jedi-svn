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

import de.berlios.jedi.common.entity.jisp.*;
import de.berlios.jedi.common.jisp.exception.JispIncompletePackageException;

/**
 * <p>
 * Façade for jisp composing and downloading related classes in logic layer.<br>
 * It offers all the jisp edition actions that presentation layer requires from
 * logic layer while abstracts its internal behaviour.
 * </p>
 * <p>
 * It provides methods to make a JispFile from a JispPackage, and JispPackage
 * editing actions.
 * </p>
 */
public class EditorLogicService {

	/**
	 * <p>
	 * Converts a JispPackage in a JispFile.
	 * </p>
	 * <p>
	 * jispPackage can not be null.
	 * </p>
	 * 
	 * @param jispPackage
	 *            The JispPackage to get the JispFile from.
	 * 
	 * @return The JispFile.
	 * @throws JispIncompletePackageException
	 *             If metadata or icons aren't complete.
	 * @throws UnsupportedOperationException
	 *             If server doesn't support UTF-8 encoding.
	 */
	public JispFile getJispFileFromJispPackage(JispPackage jispPackage)
			throws JispIncompletePackageException,
			UnsupportedOperationException {
		return new JispPackageJispFileStorer(jispPackage).toJispFile();
	}

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
	public void addJispPackageToJispPackage(JispPackage jispPackageToAdd,
			JispPackage jispPackage) {
		new JispMaker().addJispPackageToJispPackage(jispPackageToAdd,
				jispPackage);
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
		new JispMaker().addJispIconToJispPackage(jispIcon, jispPackage);
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
		new JispMaker().addJispObjectToJispIcon(jispObject, jispIcon);
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
		new JispMaker().addJispTextToJispIcon(jispText, jispIcon);
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
		return new JispMaker().removeJispIconFromJispPackage(jispIcon,
				jispPackage);
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
		return new JispMaker().removeJispObjectFromJispIcon(jispObject,
				jispIcon);
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
		return new JispMaker().removeJispTextFromJispIcon(jispText, jispIcon);
	}
}
