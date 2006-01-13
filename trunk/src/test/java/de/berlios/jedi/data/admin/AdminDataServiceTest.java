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

package de.berlios.jedi.data.admin;

import java.util.Iterator;


import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.exception.DataException;
import de.berlios.jedi.common.jisp.JispStoredToJispPackage;
import de.berlios.jedi.common.jisp.exception.JispInvalidIcondefException;
import de.berlios.jedi.common.jisp.exception.JispStoredException;
import de.berlios.jedi.data.DataService;
import de.berlios.jedi.logic.admin.JispFileWrapper;
import de.berlios.jedi.testUtilities.DataUtil;
import de.berlios.jedi.testUtilities.DiskFilesRetriever;

import junit.framework.TestCase;

/**
 * Test file for AdminDataService class.<br>
 * Although this class only delegates the methods in other classes, this test is
 * made to ensure that no changes are made in the service in a future that may
 * broke the system.
 */
public class AdminDataServiceTest extends TestCase {

	/**
	 * The AdminDataService to test.
	 */
	private AdminDataService adminDataService;

	/**
	 * A JispPackage to use in testing.
	 */
	private JispPackage jispPackageOk;

	/**
	 * A JispPackage to use in testing.
	 */
	private JispPackage jispPackageOk2;

	/**
	 * A JispPackage to use in testing.
	 */
	private JispPackage jispPackageOkDuplicated;

	/**
	 * Sets up the test needed objects and cleans the database.
	 * 
	 * @throws JispStoredException
	 *             If a problem occurs when retrieving the JispPackages to test
	 *             from the JispFile.
	 * @throws JispInvalidIcondefException
	 *             If the icondef of the JispFiles used in tests isn't valid.
	 */
	protected void setUp() throws JispInvalidIcondefException,
			JispStoredException {
		DataUtil.cleanDatabase();

		// Preparing the JispPackages to save
		jispPackageOk = new JispStoredToJispPackage(new JispFileWrapper(
				DiskFilesRetriever
						.getJispFile("src/test/testFiles/jispFiles/Ok/Ok.jisp")))
				.toJispPackage();
		jispPackageOk2 = new JispStoredToJispPackage(new JispFileWrapper(
				DiskFilesRetriever
						.getJispFile("src/test/testFiles/jispFiles/Ok2/Ok2.jisp")))
				.toJispPackage();
		jispPackageOkDuplicated = new JispStoredToJispPackage(
				new JispFileWrapper(DiskFilesRetriever
						.getJispFile("src/test/testFiles/jispFiles/Ok/Ok.jisp")))
				.toJispPackage();

		adminDataService = new AdminDataService();
	}

	/**
	 * Saves a JispPackage.<br>
	 * The JispPackages are get using the DataService (which also has its own
	 * tests). It is tested that all the saved JispPackages (and only those) are
	 * returned.
	 */
	public void testSaveJispPackage() {
		try {
			// Saving the JispPackages
			adminDataService.saveJispPackage(jispPackageOk);
			adminDataService.saveJispPackage(jispPackageOk2);

			// Verifying that JispPackages were saved
			Iterator iterator = new DataService().getJispPackagesList()
					.iterator();
			assertEquals(jispPackageOk, iterator.next());
			assertEquals(jispPackageOk2, iterator.next());

			assertFalse(iterator.hasNext());
		} catch (DataException e) {
			fail("A DataException occured when saving the JispPackages (or when getting): "
					+ e.getMessage());
		}
	}

	/**
	 * Deletes a JispPackage.<br>
	 * The JispPackage to be deleted must have been previously saved. It is
	 * tested that a saved JispPackage and then deleted returns an empty list,
	 * and the same with more than one JispPackage.
	 */
	public void testDeleteJispPackage() {
		try {
			// Saving the JispPackage
			adminDataService.saveJispPackage(jispPackageOk);

			// Deleting the JispPackage
			adminDataService.deleteJispPackage(jispPackageOk);

			// Verifying that the JispPackage was deleted
			Iterator iterator = new DataService().getJispPackagesList()
					.iterator();
			assertFalse(iterator.hasNext());
		} catch (DataException e) {
			fail("A DataException occured when saving the JispPackages (or when getting): "
					+ e.getMessage());
		}

		//With more than one JispPackage
		try {
			// Saving the JispPackage
			// The duplicated package is used because a deleted package can't be
			// saved again. It is not relevant to the test that it's equal to
			// the other package
			adminDataService.saveJispPackage(jispPackageOkDuplicated);
			adminDataService.saveJispPackage(jispPackageOk2);

			// Deleting the JispPackage
			adminDataService.deleteJispPackage(jispPackageOkDuplicated);

			// Verifying that the JispPackage was deleted
			Iterator iterator = new DataService().getJispPackagesList()
					.iterator();
			assertEquals(jispPackageOk2, iterator.next());
			assertFalse(iterator.hasNext());

			// Deleting the other JispPackage
			adminDataService.deleteJispPackage(jispPackageOk2);

			// Verifying that the JispPackage was deleted
			iterator = new DataService().getJispPackagesList().iterator();
			assertFalse(iterator.hasNext());
		} catch (DataException e) {
			fail("A DataException occured when saving the JispPackages (or when getting): "
					+ e.getMessage());
		}
	}
}
