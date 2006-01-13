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

import java.util.Iterator;

import de.berlios.jedi.common.admin.exception.IncorrectPasswordException;
import de.berlios.jedi.common.entity.jisp.JispFile;
import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.exception.DataException;
import de.berlios.jedi.common.jisp.JispStoredToJispPackage;
import de.berlios.jedi.common.jisp.exception.JispInvalidIcondefException;
import de.berlios.jedi.common.jisp.exception.JispStoredException;
import de.berlios.jedi.logic.LogicService;
import de.berlios.jedi.testUtilities.DataUtil;
import de.berlios.jedi.testUtilities.DiskFilesRetriever;
import junit.framework.TestCase;

/**
 * Test file for AdminLogicService class.<br>
 * Although this class only delegates the methods in other classes, this test is
 * made to ensure that no changes are made in the service in a future that may
 * broke the system.
 */
public class AdminLogicServiceTest extends TestCase {

	/**
	 * The AdminLogicService to test.
	 */
	private AdminLogicService adminLogicService;

	/**
	 * A JispFile to use in testing.
	 */
	private JispFile jispFileOk;

	/**
	 * A JispFile to use in testing.
	 */
	private JispFile jispFileOk2;

	/**
	 * Sets up the test needed objects and cleans the database.
	 */
	protected void setUp() {
		DataUtil.cleanDatabase();

		// Preparing the JispPackages to save
		jispFileOk = DiskFilesRetriever
				.getJispFile("src/test/testFiles/jispFiles/Ok/Ok.jisp");
		jispFileOk2 = DiskFilesRetriever
				.getJispFile("src/test/testFiles/jispFiles/Ok2/Ok2.jisp");

		adminLogicService = new AdminLogicService();
	}

	/**
	 * <p>
	 * Saves a JispFile.<br>
	 * The JispPackages are get using the LogicService (which also has its own
	 * tests). It is tested that all the saved JispFiles (and only those) are
	 * returned (comparing them to their equivalent JispPackage).
	 * </p>
	 * 
	 * @throws JispStoredException
	 *             If a problem occurs when getting the equivalent JispPackages
	 *             to the JispFiles.
	 * @throws JispInvalidIcondefException
	 *             If the icondef of the JispFiles used in tests isn't valid.
	 */
	public void testSaveJispFile() throws JispInvalidIcondefException,
			JispStoredException {
		// Getting equivalent JispPackages to the JispFiles
		JispPackage jispPackageOkExpected = new JispStoredToJispPackage(
				new JispFileWrapper(jispFileOk)).toJispPackage();
		JispPackage jispPackageOk2Expected = new JispStoredToJispPackage(
				new JispFileWrapper(jispFileOk2)).toJispPackage();

		try {
			// Saving the JispFiles
			adminLogicService.saveJispFile(jispFileOk);
			adminLogicService.saveJispFile(jispFileOk2);

			// Verifying that JispPackages were saved
			Iterator iterator = new LogicService().getJispPackagesList()
					.iterator();
			assertEquals(jispPackageOkExpected, iterator.next());
			assertEquals(jispPackageOk2Expected, iterator.next());

			assertFalse(iterator.hasNext());
		} catch (DataException e) {
			fail("A DataException occured when saving the JispPackages (or when getting): "
					+ e.getMessage());
		} catch (JispInvalidIcondefException e) {
			fail("A JispInvalidIcondefException occured when saving the JispPackages");
		} catch (JispStoredException e) {
			fail("A JispStoredException occured when saving the JispPackages");
		}
	}

	/**
	 * Deletes a JispPackage.<br>
	 * The JispPackage to be deleted must have been previously saved. It is
	 * tested that a saved JispPackage and then deleted returns an empty list,
	 * and the same with more than one JispPackage.
	 * 
	 * @throws JispStoredException
	 *             If the JispFile used in tests isn't valid.
	 * @throws JispInvalidIcondefException
	 *             If the icondef of the JispFiles used in tests isn't valid.
	 */
	public void testDeleteJispPackage() throws JispInvalidIcondefException,
			JispStoredException {
		try {
			// Saving the JispFile
			adminLogicService.saveJispFile(jispFileOk);

			// Deleting the JispPackage
			Iterator iteratorToDelete = new LogicService()
					.getJispPackagesList().iterator();
			adminLogicService.deleteJispPackage((JispPackage) iteratorToDelete
					.next());

			// Verifying that the JispPackage was deleted
			Iterator iterator = new LogicService().getJispPackagesList()
					.iterator();
			assertFalse(iterator.hasNext());
		} catch (DataException e) {
			fail("A DataException occured when saving the JispPackages (or when getting): "
					+ e.getMessage());
		}

		// With more than one JispPackage
		try {
			// Saving the JispFiles
			adminLogicService.saveJispFile(jispFileOk);
			adminLogicService.saveJispFile(jispFileOk2);

			// Deleting the JispPackage
			Iterator iteratorToDelete = new LogicService()
					.getJispPackagesList().iterator();
			adminLogicService.deleteJispPackage((JispPackage) iteratorToDelete
					.next());

			// Verifying that the JispPackage was deleted
			JispPackage jispPackageOk2 = new JispStoredToJispPackage(
					new JispFileWrapper(jispFileOk2)).toJispPackage();
			Iterator iterator = new LogicService().getJispPackagesList()
					.iterator();
			assertEquals(jispPackageOk2, iterator.next());
			assertFalse(iterator.hasNext());

			// Deleting the other JispPackage
			adminLogicService.deleteJispPackage((JispPackage) iteratorToDelete
					.next());

			// Verifying that the JispPackage was deleted
			iterator = new LogicService().getJispPackagesList().iterator();
			assertFalse(iterator.hasNext());
		} catch (DataException e) {
			fail("A DataException occured when saving the JispPackages (or when getting): "
					+ e.getMessage());
		}
	}

	/**
	 * Checks if the password is equal to admin password.<br>
	 * The password is get from the config file. It is tested that the same
	 * password in the test file returns true, and that a different password
	 * (equal but with different case letters, null, empty and a string not
	 * related) throws an exception.
	 */
	public void testLogin() {
		// Test with a valid password
		try {
			assertTrue(new AdminLogicService().login("password"));
		} catch (IncorrectPasswordException e) {
			fail("An IncorrectPasswordException was thrown when testing"
					+ " with a valid password");
		}

		// Test that should fail
		// Test with an invalid password (equal to the password, but with
		// different case letters)
		try {
			assertTrue(new AdminLogicService().login("Password"));
			fail("An IncorrectPasswordException should have been thrown when testing"
					+ " with a password with different case letters than the valid one");
		} catch (IncorrectPasswordException e) {
		}

		// Test with an invalid password (null password)
		try {
			assertTrue(new AdminLogicService().login(null));
			fail("An IncorrectPasswordException should have been thrown when testing"
					+ " with a null password");
		} catch (IncorrectPasswordException e) {
		}

		// Test with an invalid password (empty password)
		try {
			assertTrue(new AdminLogicService().login(""));
			fail("An IncorrectPasswordException should have been thrown when testing"
					+ " with an empty password");
		} catch (IncorrectPasswordException e) {
		}

		// Test with an invalid password (not related password)
		try {
			assertTrue(new AdminLogicService().login("Master Yoda"));
			fail("An IncorrectPasswordException should have been thrown when testing"
					+ " with a not related password");
		} catch (IncorrectPasswordException e) {
		}
	}
}
