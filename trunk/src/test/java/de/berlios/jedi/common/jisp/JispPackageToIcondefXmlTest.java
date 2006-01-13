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

import de.berlios.jedi.common.jisp.exception.JispIncompletePackageException;

import junit.framework.TestCase;

/**
 * Test file for JispPackageToIcondefXml class.
 */
public class JispPackageToIcondefXmlTest extends TestCase {

	/**
	 * Gets the icondef JDOM Document from a JispPackage.<br>
	 * It is tested if the document returned is equals to the expected, but
	 * using their String representation (Document equals method doesn't works
	 * as it should be expected). The test is made with a complete JispPackage,
	 * one lacking description in metadata, and one lacking home in metadata.
	 */
	public void testToIcondefXml() {
		try {
			JispPackageForTests jispPackageForTests = new JispPackageForTests();

			JispPackageToIcondefXml jispPackageToIcondefXmlOk = new JispPackageToIcondefXml(
					jispPackageForTests.jispPackage);

			String icondefOkStringExpected = jispPackageForTests.getIcondef()
					.toString();
			String icondefOkStringActual = jispPackageToIcondefXmlOk
					.toIcondefXml().toString();

			assertEquals(icondefOkStringExpected, icondefOkStringActual);
		} catch (JispIncompletePackageException e) {
			fail("A JispIncompletePackageException occured when getting the equivalent"
					+ " icondef xml file for the JispPackage: "
					+ e.getMessage());
		}

		// With a metadata without description
		try {
			JispPackageForTests jispPackageForTests = new JispPackageForTests();
			jispPackageForTests = new JispPackageForTests();
			jispPackageForTests.jispMetadata.setDescription(null);

			JispPackageToIcondefXml jispPackageToIcondefXmlOk = new JispPackageToIcondefXml(
					jispPackageForTests.jispPackage);

			String icondefOkStringExpected = jispPackageForTests.getIcondef()
					.toString();
			String icondefOkStringActual = jispPackageToIcondefXmlOk
					.toIcondefXml().toString();

			assertEquals(icondefOkStringExpected, icondefOkStringActual);
		} catch (JispIncompletePackageException e) {
			fail("A JispIncompletePackageException occured when getting the equivalent"
					+ " icondef xml file for the JispPackage without description element: "
					+ e.getMessage());
		}

		// With a metadata without home
		try {
			JispPackageForTests jispPackageForTests = new JispPackageForTests();
			jispPackageForTests = new JispPackageForTests();
			jispPackageForTests.jispMetadata.setHome(null);

			JispPackageToIcondefXml jispPackageToIcondefXmlOk = new JispPackageToIcondefXml(
					jispPackageForTests.jispPackage);

			String icondefOkStringExpected = jispPackageForTests.getIcondef()
					.toString();
			String icondefOkStringActual = jispPackageToIcondefXmlOk
					.toIcondefXml().toString();

			assertEquals(icondefOkStringExpected, icondefOkStringActual);
		} catch (JispIncompletePackageException e) {
			fail("A JispIncompletePackageException occured when getting the equivalent"
					+ " icondef xml file for the JispPackage without home element: "
					+ e.getMessage());
		}
	}

	/**
	 * Getting the icondef JDOM Document is tested with invalid JispPackages.<br>
	 * If the equivalent element in the JispMetadata of a required element in
	 * the icondef has no value, toIcondefXml() throws a
	 * JispIncompletePackageException.<br>
	 * Metadata required elements: name, version, description, at least one
	 * author, creation. Authors must have a name.
	 */
	public void testToIcondefXmlWithInvalidMetadata() {
		JispPackageForTests jispPackageForTests;

		// With a metadata without name
		try {
			jispPackageForTests = new JispPackageForTests();
			jispPackageForTests.jispMetadata.setName(null);

			new JispPackageToIcondefXml(jispPackageForTests.jispPackage)
					.toIcondefXml();
			fail("A JispIncompletePackageException should have been thrown");
		} catch (JispIncompletePackageException e) {
			assertEquals("Required element named \"name\" doesn't exist", e
					.getMessage());
		}

		// With a metadata without version
		try {
			jispPackageForTests = new JispPackageForTests();
			jispPackageForTests.jispMetadata.setVersion(null);

			new JispPackageToIcondefXml(jispPackageForTests.jispPackage)
					.toIcondefXml();
			fail("A JispIncompletePackageException should have been thrown");
		} catch (JispIncompletePackageException e) {
			assertEquals("Required element named \"version\" doesn't exist", e
					.getMessage());
		}

		// With a metadata without authors
		try {
			jispPackageForTests = new JispPackageForTests();
			jispPackageForTests.jispMetadata
					.removeJispAuthor(jispPackageForTests.jispAuthor1);

			new JispPackageToIcondefXml(jispPackageForTests.jispPackage)
					.toIcondefXml();
			fail("A JispIncompletePackageException should have been thrown");
		} catch (JispIncompletePackageException e) {
			assertEquals("There are no authors in metadata", e.getMessage());
		}

		// With an author without name
		try {
			jispPackageForTests = new JispPackageForTests();
			jispPackageForTests.jispAuthor1.setName(null);

			new JispPackageToIcondefXml(jispPackageForTests.jispPackage)
					.toIcondefXml();
			fail("A JispIncompletePackageException should have been thrown");
		} catch (JispIncompletePackageException e) {
			assertEquals("Required element named \"author\" doesn't exist", e
					.getMessage());
		}

		// With a metadata without creation date
		try {
			jispPackageForTests = new JispPackageForTests();
			jispPackageForTests.jispMetadata.setCreation(null);

			new JispPackageToIcondefXml(jispPackageForTests.jispPackage)
					.toIcondefXml();
			fail("A JispIncompletePackageException should have been thrown");
		} catch (JispIncompletePackageException e) {
			assertEquals("Required element named \"creation\" doesn't exist", e
					.getMessage());
		}
	}

	/**
	 * Getting the icondef JDOM Document is tested with invalid JispPackages.<br>
	 * If the equivalent element in a JispIcon of a required element in the
	 * icondef has no value, toIcondefXml() throws a
	 * JispIncompletePackageException. Also, if there are no JispIcons.<br>
	 * Icon required elements: at least one object, at least one text. Objects
	 * must have a name and mime type. Texts must have a text.
	 */
	public void testToIcondefXmlWithInvalidIcons() {
		JispPackageForTests jispPackageForTests;

		// With a JispPackage without icons
		try {
			jispPackageForTests = new JispPackageForTests();
			jispPackageForTests.jispPackage
					.removeJispIcon(jispPackageForTests.jispIcon1);
			jispPackageForTests.jispPackage
					.removeJispIcon(jispPackageForTests.jispIcon2);
			jispPackageForTests.jispPackage
					.removeJispIcon(jispPackageForTests.jispIcon3);
			jispPackageForTests.jispPackage
					.removeJispIcon(jispPackageForTests.jispIcon4);

			new JispPackageToIcondefXml(jispPackageForTests.jispPackage)
					.toIcondefXml();
			fail("A JispIncompletePackageException should have been thrown");
		} catch (JispIncompletePackageException e) {
			assertEquals("There are no icons in JispPackage", e.getMessage());
		}

		// With a JispIcon without objects
		try {
			jispPackageForTests = new JispPackageForTests();
			jispPackageForTests.jispIcon1
					.removeJispObject(jispPackageForTests.jispObject1_1);

			new JispPackageToIcondefXml(jispPackageForTests.jispPackage)
					.toIcondefXml();
			fail("A JispIncompletePackageException should have been thrown");
		} catch (JispIncompletePackageException e) {
			assertEquals("There are no objects in icon", e.getMessage());
		}

		// With a JispIcon without texts
		try {
			jispPackageForTests = new JispPackageForTests();
			jispPackageForTests.jispIcon1
					.removeJispText(jispPackageForTests.jispText1_1);

			new JispPackageToIcondefXml(jispPackageForTests.jispPackage)
					.toIcondefXml();
			fail("A JispIncompletePackageException should have been thrown");
		} catch (JispIncompletePackageException e) {
			assertEquals("There are no texts in icon", e.getMessage());
		}

		// With an object without name
		try {
			jispPackageForTests = new JispPackageForTests();
			jispPackageForTests.jispObject1_1.setName(null);

			new JispPackageToIcondefXml(jispPackageForTests.jispPackage)
					.toIcondefXml();
			fail("A JispIncompletePackageException should have been thrown");
		} catch (JispIncompletePackageException e) {
			assertEquals("Required element named \"object\" doesn't exist", e
					.getMessage());
		}

		// With an object without mime type
		try {
			jispPackageForTests = new JispPackageForTests();
			jispPackageForTests.jispObject1_1.setMimeType(null);

			new JispPackageToIcondefXml(jispPackageForTests.jispPackage)
					.toIcondefXml();
			fail("A JispIncompletePackageException should have been thrown");
		} catch (JispIncompletePackageException e) {
			assertEquals("Required attribute named \"mime\" doesn't exist", e
					.getMessage());
		}

		// With an text without text
		try {
			jispPackageForTests = new JispPackageForTests();
			jispPackageForTests.jispText1_1.setText(null);

			new JispPackageToIcondefXml(jispPackageForTests.jispPackage)
					.toIcondefXml();
			fail("A JispIncompletePackageException should have been thrown");
		} catch (JispIncompletePackageException e) {
			assertEquals("Required element named \"text\" doesn't exist", e
					.getMessage());
		}
	}
}
