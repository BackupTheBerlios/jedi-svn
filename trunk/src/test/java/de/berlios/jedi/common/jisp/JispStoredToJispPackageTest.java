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

package de.berlios.jedi.common.jisp;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import de.berlios.jedi.common.entity.jisp.JispIcon;
import de.berlios.jedi.common.entity.jisp.JispObject;
import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.jisp.exception.JispInvalidIcondefException;
import de.berlios.jedi.common.jisp.exception.JispStoredException;
import junit.framework.TestCase;

/**
 * Test file for JispPackageStorer class.<br>
 * It uses a delegated class for algorithms, so a delegate class will be made
 * for test.
 */
public class JispStoredToJispPackageTest extends TestCase {

	/**
	 * Gets a JispPackage using the JispStoredWrapperInitializerForTests.<br>
	 * The JispPackage is created from the icondef data and the stored
	 * multimedia objects. It is tested if the generated JispPackage is equals
	 * to the expected JispPackage. The test is made with a complete
	 * JispPackage, lacking description in metadata, and lacking home in
	 * metadata.<br>
	 * When generating a JispPackage, all the objects in the icondef file must
	 * could be get from the JispStoredWrapper. If not, an exception is thrown,
	 * and a JispStoredWrapper not having all the objects in the original
	 * JispPackage is used to test this.
	 * 
	 * @throws ParseException
	 *             If a ParseException when setting the date occurs.
	 * @throws JispStoredException
	 *             If a problem occurs when creating the JispStoredWrapper.
	 */
	public void testToJispPackage() throws ParseException, JispStoredException {
		JispPackage jispPackageExpected = new JispPackageForTests().jispPackage;
		// Eliminate the duplicated object name from the JispPackage
		((JispObject) ((JispIcon) jispPackageExpected.jispIconsIterator()
				.next()).jispObjectsIterator().next())
				.setName("aDirectory/but now not duplicated");
		// Set a specific creation date
		jispPackageExpected.getJispMetadata().setCreation(
				new SimpleDateFormat("yyyy-MM-dd").parse("2005-11-27"));

		try {
			JispPackage jispPackageActual = new JispStoredToJispPackage(
					new JispStoredWrapper(
							new JispStoredWrapperInitializerForTests(
									jispPackageExpected))).toJispPackage();

			assertEquals(jispPackageExpected, jispPackageActual);
		} catch (JispInvalidIcondefException e) {
			fail("A JispInvalidIcondefException occured when getting the equivalent"
					+ " JispPackage");
		}

		// Test with metadata without description
		jispPackageExpected.getJispMetadata().setDescription(null);
		try {
			JispPackage jispPackageActual = new JispStoredToJispPackage(
					new JispStoredWrapper(
							new JispStoredWrapperInitializerForTests(
									jispPackageExpected))).toJispPackage();

			assertEquals(jispPackageExpected, jispPackageActual);
		} catch (JispInvalidIcondefException e) {
			fail("A JispInvalidIcondefException occured when getting the equivalent"
					+ " JispPackage with a metadata without description");
		}

		// Test with metadata without home
		jispPackageExpected.getJispMetadata().setHome(null);
		try {
			JispPackage jispPackageActual = new JispStoredToJispPackage(
					new JispStoredWrapper(
							new JispStoredWrapperInitializerForTests(
									jispPackageExpected))).toJispPackage();

			assertEquals(jispPackageExpected, jispPackageActual);
		} catch (JispInvalidIcondefException e) {
			fail("A JispInvalidIcondefException occured when getting the equivalent"
					+ " JispPackage with a metadata without home");
		}

		// Test with invalid contents
		try {
			new JispStoredToJispPackage(new JispStoredWrapper(
					new JispStoredWrapperInitializerForTests(
							jispPackageExpected, false))).toJispPackage();
			fail("A JispInvalidIcondefException should have been thrown");
		} catch (JispInvalidIcondefException e) {
			assertEquals("The icondef.xml file is not valid: aDirectory/but"
					+ " now not duplicated not found in stored Jisp", e
					.getMessage());
		}
	}
}
