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

import java.util.Arrays;


import de.berlios.jedi.common.jisp.exception.JispStoredException;
import de.berlios.jedi.logic.admin.JispFileWrapper;
import de.berlios.jedi.testUtilities.DiskFilesRetriever;

import junit.framework.TestCase;

/**
 * Test file for JispFileWrapper class.
 */
public class JispFileWrapperTest extends TestCase {

	/**
	 * The JispFileWrapper to test.
	 */
	private JispFileWrapper jispFileWrapperOk;

	/**
	 * Sets up the test needed objects.
	 * 
	 * @throws JispStoredException
	 *             If the JispFile used for tests isn't valid.
	 */
	protected void setUp() throws JispStoredException {
		jispFileWrapperOk = new JispFileWrapper(DiskFilesRetriever
				.getJispFile("src/test/testFiles/jispFiles/Ok/Ok.jisp"));
	}

	/**
	 * Creates a new JispFileWrapper using a JispFile.<br>
	 * The JispFile to use should be a valid one (be a zip file and have an
	 * icondef.xml in it). It is tested that a JispFile with a base directory
	 * (icondef.xml being child of a directory) can be used.<br>
	 * It is also tested that invalid JispFile throw a JispStoredException.
	 */
	public void testConstructor() {
		// Test with a file with a base directory
		try {
			new JispFileWrapper(DiskFilesRetriever
					.getJispFile("src/test/testFiles/jispFiles/OkWithBaseDirectory/"
							+ "OkWithBaseDirectory.jisp"));
		} catch (JispStoredException e) {
			fail("A JispStoredException was thrown when using a file with a base directory: "
					+ e.getMessage());
		}

		// Test that should fail
		// Test a file without an icondef.xml
		try {
			new JispFileWrapper(DiskFilesRetriever
					.getJispFile("src/test/testFiles/jispFiles/WithoutIcondef/"
							+ "WithoutIcondef.jisp"));
			fail("JispFileWrapper constructor should have thrown a "
					+ "JispStoredException; There is no icondef.xml in "
					+ "the stored Jisp");
		} catch (JispStoredException e) {
			assertEquals("There is no icondef.xml in the stored Jisp", e
					.getMessage());
		}

		// Test a no zipped file (a plain text file)
		try {
			new JispFileWrapper(DiskFilesRetriever
					.getJispFile("src/test/testFiles/jispFiles/NoZippedFile/"
							+ "NoZippedFile.jisp"));
			fail("JispFileWrapper constructor should have thrown a "
					+ "JispStoredException; The file isn't a valid Zip "
					+ "file or is empty");
		} catch (JispStoredException e) {
			assertEquals("The file isn't a valid Zip file or is empty", e
					.getMessage());
		}

		// Test an empty zip file
		try {
			new JispFileWrapper(DiskFilesRetriever
					.getJispFile("src/test/testFiles/jispFiles/EmptyFile/"
							+ "EmptyFile.jisp"));
			fail("JispFileWrapper constructor should have thrown a "
					+ "JispStoredException; The file isn't a valid Zip "
					+ "file or is empty");
		} catch (JispStoredException e) {
			assertEquals("The file isn't a valid Zip file or is empty", e
					.getMessage());
		}

	}

	/**
	 * Gets the names of the stored files in the JispFile.<br>
	 * The filenames are returned in a String[] and includes all the path of the
	 * stored file, excepting the base directory if it exists. It is tested that
	 * the filenames returned are as expected with a JispFile without a base
	 * directory, and with a JispFile with a base directory.
	 * 
	 * @throws JispStoredException
	 *             If the JispFile used for tests isn't valid.
	 */
	public void testGetFilesNames() throws JispStoredException {
		String[] jispFileOkFilesNames = jispFileWrapperOk.getFilesNames();

		assertEquals(5, jispFileOkFilesNames.length);

		Arrays.sort(jispFileOkFilesNames);
		assertEquals("icondef.xml", jispFileOkFilesNames[0]);
		assertEquals("images/bigSmile.png", jispFileOkFilesNames[1]);
		assertEquals("images/smile.png", jispFileOkFilesNames[2]);
		assertEquals("images/svg/bigSmile.svg", jispFileOkFilesNames[3]);
		assertEquals("images/svg/smile.svg", jispFileOkFilesNames[4]);

		// Test with a file with a base directory
		jispFileOkFilesNames = new JispFileWrapper(DiskFilesRetriever
				.getJispFile("src/test/testFiles/jispFiles/OkWithBaseDirectory/"
						+ "OkWithBaseDirectory.jisp")).getFilesNames();

		assertEquals(5, jispFileOkFilesNames.length);

		Arrays.sort(jispFileOkFilesNames);
		assertEquals("icondef.xml", jispFileOkFilesNames[0]);
		assertEquals("images/bigSmile.png", jispFileOkFilesNames[1]);
		assertEquals("images/smile.png", jispFileOkFilesNames[2]);
		assertEquals("images/svg/bigSmile.svg", jispFileOkFilesNames[3]);
		assertEquals("images/svg/smile.svg", jispFileOkFilesNames[4]);
	}

	/**
	 * Gets a stored file in the JispFile.<br>
	 * The file is get by its full path in the JispFile excepting the base
	 * directory if it exists. It is tested that all the stored files in the
	 * JispFile are returned.<br>
	 * This is a method from JispStoredWrapper, so the test made here ensures
	 * that the objects where added to contents Map as they should. It also
	 * ensures that icondef.xml was added as it should, and thus there is no
	 * need to test getIcondef.
	 */
	public void testGetObject() {
		assertTrue(Arrays.equals(jispFileWrapperOk.getObject("icondef.xml"),
				DiskFilesRetriever
						.getDataFromDiskFile("src/test/testFiles/jispFiles/Ok/"
								+ "icondef.xml")));
		assertTrue(Arrays
				.equals(
						jispFileWrapperOk.getObject("images/bigSmile.png"),
						DiskFilesRetriever
								.getDataFromDiskFile("src/test/testFiles/jispFiles/Ok/images/bigSmile.png")));
		assertTrue(Arrays
				.equals(
						jispFileWrapperOk.getObject("images/smile.png"),
						DiskFilesRetriever
								.getDataFromDiskFile("src/test/testFiles/jispFiles/Ok/images/smile.png")));
		assertTrue(Arrays
				.equals(
						jispFileWrapperOk.getObject("images/svg/bigSmile.svg"),
						DiskFilesRetriever
								.getDataFromDiskFile("src/test/testFiles/jispFiles/Ok/images/svg/bigSmile.svg")));
		assertTrue(Arrays
				.equals(
						jispFileWrapperOk.getObject("images/svg/smile.svg"),
						DiskFilesRetriever
								.getDataFromDiskFile("src/test/testFiles/jispFiles/Ok/images/svg/smile.svg")));

		assertNull(jispFileWrapperOk.getObject("noExistentFile"));
	}
}
