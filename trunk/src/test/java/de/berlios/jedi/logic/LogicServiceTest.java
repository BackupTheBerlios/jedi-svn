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

package de.berlios.jedi.logic;

import java.util.Iterator;

import junit.framework.TestCase;

import de.berlios.jedi.common.entity.jisp.JispFile;
import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.exception.DataException;
import de.berlios.jedi.common.jisp.JispStoredToJispPackage;
import de.berlios.jedi.common.jisp.exception.JispInvalidIcondefException;
import de.berlios.jedi.common.jisp.exception.JispStoredException;
import de.berlios.jedi.logic.admin.AdminLogicService;
import de.berlios.jedi.logic.admin.JispFileWrapper;
import de.berlios.jedi.testUtilities.DataUtil;
import de.berlios.jedi.testUtilities.DiskFilesRetriever;

/**
 * Test file for LogicService class.<br>
 * Although this class only delegates the methods in other classes, this test is
 * made to ensure that no changes are made in the service in a future that may
 * broke the system.
 */
public class LogicServiceTest extends TestCase {

	/**
	 * Cleans the database.
	 */
	protected void setUp() {
		DataUtil.cleanDatabase();
	}

	/**
	 * Returns the JispPackagesList with all the saved packages.<br>
	 * The packages are saved as JispFiles using the AdminLogicService (which
	 * also has its own tests). It is tested that all the saved packages (and
	 * only those) are returned, usgin the equivalent JispPackage to the saved
	 * JispFiles to compare.
	 * 
	 * @throws JispStoredException
	 *             If a problem occurs when getting the equivalent JispPackages
	 *             to the JispFiles.
	 * @throws JispInvalidIcondefException
	 *             If the icondef of the JispFiles used in tests isn't valid.
	 */
	public void testGetJispPackages() throws JispInvalidIcondefException,
			JispStoredException {
		// Preparing the JispFiles to save
		JispFile jispFileOk = DiskFilesRetriever
				.getJispFile("src/test/testFiles/jispFiles/Ok/Ok.jisp");
		JispFile jispFileOk2 = DiskFilesRetriever
				.getJispFile("src/test/testFiles/jispFiles/Ok2/Ok2.jisp");
		JispFile jispFileOkEdited = DiskFilesRetriever
				.getJispFile("src/test/testFiles/jispFiles/Ok edited/Ok edited.jisp");

		// Getting the equivalent JispPackages
		JispPackage jispPackageOk = new JispStoredToJispPackage(
				new JispFileWrapper(jispFileOk)).toJispPackage();
		JispPackage jispPackageOk2 = new JispStoredToJispPackage(
				new JispFileWrapper(jispFileOk2)).toJispPackage();
		JispPackage jispPackageOkEdited = new JispStoredToJispPackage(
				new JispFileWrapper(jispFileOkEdited)).toJispPackage();

		try {
			// Saving the JispFiles
			AdminLogicService adminLogicService = new AdminLogicService();
			adminLogicService.saveJispFile(jispFileOk);
			adminLogicService.saveJispFile(jispFileOk2);
			adminLogicService.saveJispFile(jispFileOkEdited);

			// Testing the getJispPackagesList method
			Iterator iterator = new LogicService().getJispPackagesList()
					.iterator();
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
