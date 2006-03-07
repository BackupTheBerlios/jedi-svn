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

import org.jdom.Document;

import de.berlios.jedi.common.jisp.exception.JispInvalidIcondefException;
import de.berlios.jedi.common.jisp.exception.JispStoredException;
import de.berlios.jedi.testUtilities.DiskFilesRetriever;

import junit.framework.TestCase;

/**
 * Test file for JispStoredWrapper class.<br>
 * JispStoredWrapperInitializerForTests is used.
 */
public class JispStoredWrapperTest extends TestCase {

	/**
	 * A JispStoredWrapper object to test.
	 */
	private JispStoredWrapper jispStoredWrapper;

	/**
	 * The JispPackageForTest to use in tests.
	 */
	private JispPackageForTests jispPackageForTests;

	/**
	 * Creates jispPackageForTests and jispStoredWrapper using the JispPackage
	 * provided by the first.
	 * 
	 * @throws JispStoredException
	 *             If a problem occurs when creating the JispStoredWrapper.
	 */
	protected void setUp() throws JispStoredException {
		jispPackageForTests = new JispPackageForTests();
		jispStoredWrapper = new JispStoredWrapper(
				new JispStoredWrapperInitializerForTests(
						jispPackageForTests.jispPackage));
	}

	/**
	 * <p>
	 * Gets the icondef in the JispStoredWrapper.<br>
	 * The icondef is stored as JDOM Document. It is tested if the document
	 * returned is equals to the expected, but using their String representation
	 * (Document equals method doesn't works as it should be expected).
	 * </p>
	 * <p>
	 * It is also tested that a not xml file, not encoded in UTF-8 xml file,
	 * without an icondef root element, without a meta section and with an
	 * invalid creation date. A subclass of JispStoredWrapperInitializer,
	 * internal to this test method, is made to test the invalid icondef.xml
	 * files.
	 * </p>
	 * 
	 * @throws JispStoredException
	 *             If a problem occurs when creating the
	 *             JispStoredWrapperInitializers for test.
	 * @throws UnsupportedOperationException
	 *             If UTF-8 encoding isn't supported.
	 */
	public void testGetIcondef() throws UnsupportedOperationException,
			JispStoredException {
		Document icondefExpectedDocument = jispPackageForTests.getIcondef();
		try {
			Document icondefActualDocument = jispStoredWrapper.getIcondef();

			assertEquals(icondefExpectedDocument.toString(),
					icondefActualDocument.toString());
		} catch (JispInvalidIcondefException e) {
			fail("A JispInvalidIcondefException occured when getting the icondef: "
					+ e.getMessage());
		}

		/**
		 * A initializer for JispStoredWrapper used for testing purposes.<br>
		 * It simply adds an icondef.xml file to the initializedContents Map.
		 * Its used to test getIcondef with bad icondef.xml files.
		 */
		class JispStoredWrapperInitializerWithIcondef extends
				JispStoredWrapper.JispStoredWrapperInitializer {

			/**
			 * The data of the icondef.xml file.
			 */
			private byte[] icondefData;

			/**
			 * Creates a new JispStoredWrapperInitializerWithIcondef using the
			 * path to the icondef.xml file to store.
			 * 
			 * @param path
			 *            The path to the icondef.xml file to store.
			 */
			public JispStoredWrapperInitializerWithIcondef(String path) {
				icondefData = DiskFilesRetriever.getDataFromDiskFile(path);
			}

			/**
			 * Adds the data of the icondef.xml file in the initializaedContents
			 * Map using "icondef.xml" as key.
			 */
			protected void init() {
				initializedContents.put("icondef.xml", icondefData);
			}
		}

		// Tests that should fail
		// Test an icondef.xml file not in xml format (a renamed png)
		try {
			new JispStoredWrapper(new JispStoredWrapperInitializerWithIcondef(
					"src/test/testFiles/jispFiles/IcondefNoXml/icondef.xml"))
					.getIcondef();

			fail("getIcondef should have thrown a JispInvalidIcondefException;"
					+ " The icondef.xml file is not valid: Not encoded in UTF-8");
		} catch (JispInvalidIcondefException e) {
			assertEquals("The icondef.xml file is not valid:"
					+ " UTF-8 encoding error", e.getMessage());
		}

		// Test an icondef.xml file valid but not encoded in UTF-8
		try {
			new JispStoredWrapper(new JispStoredWrapperInitializerWithIcondef(
					"src/test/testFiles/jispFiles/IcondefNoUtf8Encoded/icondef.xml"))
					.getIcondef();

			fail("getIcondef should have thrown a JispInvalidIcondefException;"
					+ " The icondef.xml file is not valid: Not encoded in UTF-8");
		} catch (JispInvalidIcondefException e) {
			assertEquals("The icondef.xml file is not valid:"
					+ " UTF-8 encoding error", e.getMessage());
		}

		// Test an icondef.xml with no root icondef element
		try {
			new JispStoredWrapper(new JispStoredWrapperInitializerWithIcondef(
					"src/test/testFiles/jispFiles/IcondefNoIcondefXml/icondef.xml"))
					.getIcondef();

			fail("getIcondef should have thrown a JispInvalidIcondefException;"
					+ " The icondef.xml file is not valid: Error on line 2: cvc-elt.1:"
					+ " Cannot find the declaration of element 'test'.");
		} catch (JispInvalidIcondefException e) {
			assertEquals("The icondef.xml file is not valid: Error on line 2:"
					+ " cvc-elt.1: Cannot find the declaration of "
					+ "element 'test'.", e.getMessage());
		}

		// Test an icondef.xml file with no meta section
		try {
			new JispStoredWrapper(new JispStoredWrapperInitializerWithIcondef(
					"src/test/testFiles/jispFiles/IcondefNoMetadata/icondef.xml"))
					.getIcondef();

			fail("getIcondef should have thrown a JispInvalidIcondefException;"
					+ " The icondef.xml file is not valid: Error on line 3:"
					+ " cvc-complex-type.2.4.a: Invalid content was found starting"
					+ " with element 'icon'. One of '{\"\":meta}' is expected.");
		} catch (JispInvalidIcondefException e) {
			assertEquals("The icondef.xml file is not valid: Error on line 3:"
					+ " cvc-complex-type.2.4.a: Invalid content was found"
					+ " starting with element 'icon'. One of "
					+ "'{\"\":meta}' is expected.", e.getMessage());
		}

		// Test an icondef.xml file with an invalid creation date
		try {
			new JispStoredWrapper(new JispStoredWrapperInitializerWithIcondef(
					"src/test/testFiles/jispFiles/IcondefNoISO8601Creation/icondef.xml"))
					.getIcondef();

			fail("getIcondef should have thrown a JispInvalidIcondefException;"
					+ " The icondef.xml file is not valid: Error on line 9:"
					+ " cvc-datatype-valid.1.2.2: '2005-' is not a valid value"
					+ " of list type 'iso8601Date'");
		} catch (JispInvalidIcondefException e) {
			assertEquals("The icondef.xml file is not valid: Error on line 9: "
					+ "cvc-datatype-valid.1.2.2: '2005-' is not a valid value"
					+ " of list type 'iso8601Date'.", e.getMessage());
		}
	}

	/**
	 * Gets the objects in the JispStoredWrapper.<br>
	 * The objects stored can be get using their names. It is tested if every
	 * jispObject get uses the right key, and that a not existing user returns
	 * null.
	 */
	public void testGetObject() {
		assertEquals(jispPackageForTests.jispObject2_1.getData(),
				jispStoredWrapper.getObject(jispPackageForTests.jispObject2_1
						.getName()));
		assertEquals(jispPackageForTests.jispObject2_2.getData(),
				jispStoredWrapper.getObject(jispPackageForTests.jispObject2_2
						.getName()));
		assertEquals(jispPackageForTests.jispObject3_1.getData(),
				jispStoredWrapper.getObject(jispPackageForTests.jispObject3_1
						.getName()));
		assertEquals(jispPackageForTests.jispObject4_1.getData(),
				jispStoredWrapper.getObject(jispPackageForTests.jispObject4_1
						.getName()));
		assertEquals(jispPackageForTests.jispObject4_2.getData(),
				jispStoredWrapper.getObject(jispPackageForTests.jispObject4_2
						.getName()));

		// Getting a not existing object
		assertNull(jispStoredWrapper.getObject("not existing"));
	}

	/**
	 * Gets the filenames in the jispStoredWrapper.<br>
	 * The filenames in the array returned don't have a concrete order, so the
	 * objects can't be get from fixed index. It should be test if the array
	 * contains all the filenames (and only those filenames), but without
	 * specifying an order.
	 */
	public void testGetFileNames() {
		String[] fileNames = jispStoredWrapper.getFilesNames();

		assertTrue(contains(fileNames, "icondef.xml"));
		assertTrue(contains(fileNames, jispPackageForTests.jispObject2_1
				.getName()));
		assertTrue(contains(fileNames, jispPackageForTests.jispObject2_2
				.getName()));
		assertTrue(contains(fileNames, jispPackageForTests.jispObject3_1
				.getName()));
		assertTrue(contains(fileNames, jispPackageForTests.jispObject4_1
				.getName()));
		assertTrue(contains(fileNames, jispPackageForTests.jispObject4_2
				.getName()));

		assertEquals(6, fileNames.length);
	}

	/**
	 * Returns true if the array contains a string equals to the specified,
	 * false otherwise.
	 * 
	 * @param array
	 *            The array to look in for the string.
	 * @param string
	 *            The String to look for in the array.
	 * @return True if the array contains a string equals to the specified,
	 *         false otherwise.
	 */
	private boolean contains(String[] array, String string) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(string)) {
				return true;
			}
		}
		return false;
	}
}
