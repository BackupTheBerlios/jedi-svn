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

package de.berlios.jedi.data;

import java.util.Iterator;


import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.exception.DataException;
import de.berlios.jedi.common.jisp.JispStoredToJispPackage;
import de.berlios.jedi.common.jisp.exception.JispInvalidIcondefException;
import de.berlios.jedi.common.jisp.exception.JispStoredException;
import de.berlios.jedi.data.DataService;
import de.berlios.jedi.data.admin.AdminDataService;
import de.berlios.jedi.logic.admin.JispFileWrapper;
import de.berlios.jedi.testUtilities.DataUtil;
import de.berlios.jedi.testUtilities.DiskFilesRetriever;

import junit.framework.TestCase;

/**
 * Test file for DataService class.<br>
 * Although this class only delegates the methods in other classes, this test is
 * made to ensure that no changes are made in the service in a future that may
 * broke the system.
 */
public class DataServiceTest extends TestCase {

	/**
	 * Cleans the database.
	 */
	protected void setUp() {
		DataUtil.cleanDatabase();
	}

	/**
	 * Returns the JispPackagesList with all the saved packages.<br>
	 * The JispPackages are saved using the AdminDataService (which also has its
	 * own tests). It is tested that all the saved JispPackages (and only those)
	 * are returned.
	 * 
	 * @throws JispStoredException
	 *             If a problem occurs when retrieving the JispPackages to test
	 *             from the JispFile.
	 * @throws JispInvalidIcondefException
	 *             If the icondef of the JispFiles used in tests isn't valid.
	 */
	public void testGetJispPackagesList() throws JispInvalidIcondefException,
			JispStoredException {
		DataService dataService = new DataService();

		// Preparing the JispPackages to save
		JispPackage jispPackageOk = new JispStoredToJispPackage(
				new JispFileWrapper(DiskFilesRetriever
						.getJispFile("src/test/testFiles/jispFiles/Ok/Ok.jisp")))
				.toJispPackage();
		JispPackage jispPackageOk2 = new JispStoredToJispPackage(
				new JispFileWrapper(DiskFilesRetriever
						.getJispFile("src/test/testFiles/jispFiles/Ok2/Ok2.jisp")))
				.toJispPackage();
		JispPackage jispPackageOkEdited = new JispStoredToJispPackage(
				new JispFileWrapper(
						DiskFilesRetriever
								.getJispFile("src/test/testFiles/jispFiles/Ok edited/Ok edited.jisp")))
				.toJispPackage();

		try {
			// Saving the JispPackages
			AdminDataService adminDataService = new AdminDataService();
			adminDataService.saveJispPackage(jispPackageOk);
			adminDataService.saveJispPackage(jispPackageOk2);
			adminDataService.saveJispPackage(jispPackageOkEdited);

			// Testing the getPackagesList method
			Iterator iterator = dataService.getJispPackagesList().iterator();
			assertEquals(jispPackageOk, iterator.next());
			assertEquals(jispPackageOk2, iterator.next());
			assertEquals(jispPackageOkEdited, iterator.next());

			assertFalse(iterator.hasNext());
		} catch (DataException e) {
			fail("A DataException occured when saving the JispPackages (or when getting): "
					+ e.getMessage());
		}
	}
}
