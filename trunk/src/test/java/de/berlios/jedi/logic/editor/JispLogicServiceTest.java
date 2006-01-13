/*
 * Copyright 2005-2006 Daniel Calvi�o S�nchez
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
import de.berlios.jedi.common.jisp.JispStoredToJispPackage;
import de.berlios.jedi.common.jisp.exception.JispIncompletePackageException;
import de.berlios.jedi.common.jisp.exception.JispInvalidIcondefException;
import de.berlios.jedi.common.jisp.exception.JispStoredException;
import de.berlios.jedi.logic.admin.JispFileWrapper;
import de.berlios.jedi.logic.editor.EditorLogicService;
import de.berlios.jedi.testUtilities.DataUtil;
import de.berlios.jedi.testUtilities.DiskFilesRetriever;

import junit.framework.TestCase;

/**
 * Test file for EditorLogicService class.<br>
 * Although this class only delegates the methods in other classes, this test is
 * made to ensure that no changes are made in the service in a future that may
 * broke the system.
 */
public class JispLogicServiceTest extends TestCase {

	/**
	 * The EditorLogicService to test.
	 */
	private EditorLogicService jispLogicService;

	/**
	 * A JispIcon to use in testing.
	 */
	private JispIcon jispIcon;

	/**
	 * A JispText to use in testing.
	 */
	private JispText jispText;

	/**
	 * A JispObject to use in testing.
	 */
	private JispObject jispObject;

	/**
	 * A JispIcon to use in testing.
	 */
	private JispIcon jispIcon2;

	/**
	 * A JispText to use in testing.
	 */
	private JispText jispText2;

	/**
	 * A JispObject to use in testing.
	 */
	private JispObject jispObject2;

	/**
	 * Sets up the test needed objects and cleans the database.
	 */
	protected void setUp() {
		DataUtil.cleanDatabase();

		jispLogicService = new EditorLogicService();

		jispIcon = new JispIcon();

		jispObject = new JispObject();
		jispObject.setName("0 array");
		jispObject.setMimeType("none");
		jispObject.setData(new byte[10]);

		jispText = new JispText();
		jispText.setText("nothing");

		jispIcon2 = new JispIcon();

		jispObject2 = new JispObject();
		jispObject2.setName("another 0 array");
		jispObject2.setMimeType("none");
		jispObject2.setData(new byte[15]);

		jispText2 = new JispText();
		jispText.setText("nothing nothing");
	}

	/**
	 * Gets the equivalent JispFile to a JispPackage.<br>
	 * It is tested that the JispPackage generated from the JispFile returned by
	 * toJispFile method its equals to the one used to get the JispFile. The
	 * disk stored JispFile can't be used to compare, because the zip file was
	 * generated by ark instead of a Java outpustream.<br>
	 * Uses JispStoredToJispPackage and JispFileWrapper to get the JispPackage
	 * to use in the tests.
	 * 
	 * @throws JispStoredException
	 *             If a problem occurs when getting the equivalent JispPackage
	 *             to the JispFile.
	 * @throws JispInvalidIcondefException
	 *             If the icondef of the JispFile used in tests isn't valid.
	 */
	public void testGetJispFileFromJispPackage()
			throws JispInvalidIcondefException, JispStoredException {
		JispPackage jispPackageOkExpected = new JispStoredToJispPackage(
				new JispFileWrapper(DiskFilesRetriever
						.getJispFile("src/test/testFiles/jispFiles/Ok/Ok.jisp")))
				.toJispPackage();
		try {
			JispFile jispFileOkActual = jispLogicService
					.getJispFileFromJispPackage(jispPackageOkExpected);
			JispPackage jispPackageOkActual = new JispStoredToJispPackage(
					new JispFileWrapper(jispFileOkActual)).toJispPackage();

			assertEquals(jispPackageOkExpected, jispPackageOkActual);
		} catch (UnsupportedOperationException e) {
			fail("UnsupportedOperationException: " + e.getMessage());
		} catch (JispIncompletePackageException e) {
			fail("getJispFileFromJispPackage threw a JispIncompletePackageException"
					+ " with a valid JispPackage");
		}
	}

	/**
	 * Adds all the JispIcons from a JispPackage to another JispPackage.<br>
	 * It is tested that all the JispIcons from the first JispPackage are added
	 * to the second. It is also tested that if a JispIcon was already added, it
	 * is not added again.
	 */
	public void testAddJispPackageToJispPackage() {
		// Preparing the JispIcons and JispPackage to be added
		jispIcon.addJispObject(jispObject);
		jispIcon.addJispText(jispText);
		jispIcon2.addJispObject(jispObject2);
		jispIcon2.addJispText(jispText2);
		JispPackage jispPackageToAdd = new JispPackage();
		jispPackageToAdd.addJispIcon(jispIcon);
		jispPackageToAdd.addJispIcon(jispIcon2);

		JispPackage jispPackage = new JispPackage();
		jispLogicService.addJispPackageToJispPackage(jispPackageToAdd,
				jispPackage);
		Iterator iterator = jispPackage.jispIconsIterator();
		assertEquals(jispIcon, iterator.next());
		assertEquals(jispIcon2, iterator.next());
		assertFalse(iterator.hasNext());

		// Try to add an icon added already
		JispPackage jispPackageToAdd2 = new JispPackage();
		jispPackageToAdd2.addJispIcon(jispIcon2);
		jispLogicService.addJispPackageToJispPackage(jispPackageToAdd2,
				jispPackage);
		iterator = jispPackage.jispIconsIterator();
		assertEquals(jispIcon, iterator.next());
		assertEquals(jispIcon2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 * Adds a JispIcon to a JispPackage.<br>
	 * It is tested that the JispPackage contains the added JispIcon. It is also
	 * tested that if a JispIcon was already added, it is not added again.
	 */
	public void testAddJispIconToJispPackage() {
		// Preparing the JispIcons
		jispIcon.addJispObject(jispObject);
		jispIcon.addJispText(jispText);
		jispIcon2.addJispObject(jispObject2);
		jispIcon2.addJispText(jispText2);

		JispPackage jispPackage = new JispPackage();

		jispLogicService.addJispIconToJispPackage(jispIcon, jispPackage);
		assertTrue(jispPackage.containsJispIcon(jispIcon));

		jispLogicService.addJispIconToJispPackage(jispIcon2, jispPackage);
		assertTrue(jispPackage.containsJispIcon(jispIcon2));
		assertTrue(jispPackage.containsJispIcon(jispIcon));

		// Try to add an icon added already
		jispLogicService.addJispIconToJispPackage(jispIcon, jispPackage);
		Iterator iterator = jispPackage.jispIconsIterator();
		assertEquals(jispIcon, iterator.next());
		assertEquals(jispIcon2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 * Adds a JispText to a JispIcon.<br>
	 * It is tested that the JispIcon contains the added JispText. It is also
	 * tested that if a JispText was already added, it is not added again.
	 */
	public void testAddJispTextToJispIcon() {
		jispLogicService.addJispTextToJispIcon(jispText, jispIcon);
		assertTrue(jispIcon.containsJispText(jispText));

		jispLogicService.addJispTextToJispIcon(jispText2, jispIcon);
		assertTrue(jispIcon.containsJispText(jispText2));
		assertTrue(jispIcon.containsJispText(jispText));

		// Try to add a text added already
		jispLogicService.addJispTextToJispIcon(jispText, jispIcon);
		Iterator iterator = jispIcon.jispTextsIterator();
		assertEquals(jispText, iterator.next());
		assertEquals(jispText2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 * Adds a JispObject to a JispIcon.<br>
	 * It is tested that the JispIcon contains the added JispObject. It is also
	 * tested that if a JispObject was already added, it is not added again.
	 */
	public void testAddJispObjectToJispIcon() {
		jispLogicService.addJispObjectToJispIcon(jispObject, jispIcon);
		assertTrue(jispIcon.containsJispObject(jispObject));

		jispLogicService.addJispObjectToJispIcon(jispObject2, jispIcon);
		assertTrue(jispIcon.containsJispObject(jispObject2));
		assertTrue(jispIcon.containsJispObject(jispObject));

		// Try to add an object added already
		assertTrue(jispIcon.containsJispObject(jispObject));
		Iterator iterator = jispIcon.jispObjectsIterator();
		assertEquals(jispObject, iterator.next());
		assertEquals(jispObject2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 * Removes a JispIcon from a JispPackage.<br>
	 * It is tested that the JispPackage doesn't contain the JispIcon. It is
	 * also tested that if a JispIcon was already removed, removing it again
	 * returns false.
	 */
	public void testRemoveJispIconFromJispPackage() {
		// Preparing the JispIcons
		jispIcon.addJispObject(jispObject);
		jispIcon.addJispText(jispText);
		jispIcon2.addJispObject(jispObject2);
		jispIcon2.addJispText(jispText2);

		JispPackage jispPackage = new JispPackage();

		jispLogicService.addJispIconToJispPackage(jispIcon, jispPackage);
		assertTrue(jispLogicService.removeJispIconFromJispPackage(jispIcon,
				jispPackage));
		Iterator iterator = jispPackage.jispIconsIterator();
		assertFalse(iterator.hasNext());

		jispLogicService.addJispIconToJispPackage(jispIcon2, jispPackage);
		jispLogicService.addJispIconToJispPackage(jispIcon, jispPackage);

		// Try to remove an icon removed already
		assertTrue(jispLogicService.removeJispIconFromJispPackage(jispIcon2,
				jispPackage));
		assertFalse(jispLogicService.removeJispIconFromJispPackage(jispIcon2,
				jispPackage));
		iterator = jispPackage.jispIconsIterator();
		assertEquals(jispIcon, iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 * Removes a JispText from a JispIcon.<br>
	 * It is tested that the JispIcon doesn't contain the JispText. It is also
	 * tested that if a JispText was already removed, removing it again returns
	 * false.
	 */
	public void testRemoveJispTextFromJispIcon() {
		jispLogicService.addJispTextToJispIcon(jispText, jispIcon);
		assertTrue(jispLogicService.removeJispTextFromJispIcon(jispText,
				jispIcon));
		Iterator iterator = jispIcon.jispTextsIterator();
		assertFalse(iterator.hasNext());

		jispLogicService.addJispTextToJispIcon(jispText2, jispIcon);
		jispLogicService.addJispTextToJispIcon(jispText, jispIcon);

		// Try to remove a text removed already
		assertTrue(jispLogicService.removeJispTextFromJispIcon(jispText2,
				jispIcon));
		assertFalse(jispLogicService.removeJispTextFromJispIcon(jispText2,
				jispIcon));
		iterator = jispIcon.jispTextsIterator();
		assertEquals(jispText, iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 * Removes a JispObject from a JispIcon.<br>
	 * It is tested that the JispIcon doesn't contain the JispObject. It is also
	 * tested that if a JispObject was already removed, removing it again
	 * returns false.
	 */
	public void testRemoveJispObjectFromJispIcon() {
		jispLogicService.addJispObjectToJispIcon(jispObject, jispIcon);
		assertTrue(jispLogicService.removeJispObjectFromJispIcon(jispObject,
				jispIcon));
		Iterator iterator = jispIcon.jispObjectsIterator();
		assertFalse(iterator.hasNext());

		jispLogicService.addJispObjectToJispIcon(jispObject2, jispIcon);
		jispLogicService.addJispObjectToJispIcon(jispObject, jispIcon);

		// Try to remove a object removed already
		assertTrue(jispLogicService.removeJispObjectFromJispIcon(jispObject2,
				jispIcon));
		assertFalse(jispLogicService.removeJispObjectFromJispIcon(jispObject2,
				jispIcon));
		iterator = jispIcon.jispObjectsIterator();
		assertEquals(jispObject, iterator.next());
		assertFalse(iterator.hasNext());
	}
}
