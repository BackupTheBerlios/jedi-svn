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


import de.berlios.jedi.common.entity.jisp.JispFile;
import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.jisp.JispStoredToJispPackage;
import de.berlios.jedi.common.jisp.exception.JispIncompletePackageException;
import de.berlios.jedi.common.jisp.exception.JispInvalidIcondefException;
import de.berlios.jedi.common.jisp.exception.JispStoredException;
import de.berlios.jedi.logic.admin.JispFileWrapper;
import de.berlios.jedi.logic.editor.JispPackageJispFileStorer;
import de.berlios.jedi.testUtilities.DiskFilesRetriever;

import junit.framework.TestCase;

/**
 * Test file for JispPackageJispFileStorer class.<br>
 * Uses JispStoredToJispPackage and JispFileWrapper to get the JispPackage to
 * use in the tests.
 */
public class JispPackageJispFileStorerTest extends TestCase {

	/**
	 * The JispPackage to use.
	 */
	private JispPackage jispPackageOkExpected;

	/**
	 * The JispPackageJispFileStorer to test.
	 */
	private JispPackageJispFileStorer jispPackageJispFileStorer;

	/**
	 * Sets up the test needed objects.
	 * 
	 * @throws JispStoredException
	 *             If a problem occurs when getting the equivalent JispPackage
	 *             to the JispFile.
	 * @throws JispInvalidIcondefException
	 *             If the icondef of the JispFile used in tests isn't valid.
	 */
	protected void setUp() throws JispInvalidIcondefException,
			JispStoredException {
		jispPackageOkExpected = new JispStoredToJispPackage(
				new JispFileWrapper(DiskFilesRetriever
						.getJispFile("src/test/testFiles/jispFiles/Ok/Ok.jisp")))
				.toJispPackage();
		jispPackageJispFileStorer = new JispPackageJispFileStorer(
				jispPackageOkExpected);
	}

	/**
	 * Gets the equivalent JispFile to a JispPackage.<br>
	 * It is tested that the JispPackage generated from the JispFile returned by
	 * toJispFile method its equals to the one used to get the JispFile. The
	 * disk stored JispFile can't be used to compare, because the zip file was
	 * generated by ark instead of a Java outpustream.
	 * 
	 * @throws JispStoredException
	 *             If a problem occurs when getting the equivalent JispPackage
	 *             to the generated JispFile.
	 * @throws JispInvalidIcondefException
	 *             If the icondef of the JispFile generated isn't valid.
	 */
	public void testToJispFile() throws JispInvalidIcondefException,
			JispStoredException {
		JispFile jispFileOkActual;
		try {
			jispFileOkActual = jispPackageJispFileStorer.toJispFile();
			JispPackage jispPackageOkActual = new JispStoredToJispPackage(
					new JispFileWrapper(jispFileOkActual)).toJispPackage();

			assertEquals(jispPackageOkExpected, jispPackageOkActual);
		} catch (UnsupportedOperationException e) {
			fail("UnsupportedOperationException: " + e.getMessage());
		} catch (JispIncompletePackageException e) {
			fail("toJispFile threw a JispIncompletePackageException with a valid JispPackage");
		}
	}
}
