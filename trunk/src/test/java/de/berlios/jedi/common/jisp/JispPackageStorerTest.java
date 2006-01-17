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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.jisp.exception.JispIncompletePackageException;
import junit.framework.TestCase;

/**
 * Test file for JispPackageStorer class.<br>
 * It's an abstract class, so a subclass is made to test the algorithms.
 */
public class JispPackageStorerTest extends TestCase {

	/**
	 * <p>
	 * Stores a package using the dummy subclass created to test.<br>
	 * Store method using the dummy class stores the JispPackage in a list. The
	 * first element stored is icondef, and then all the objects in the order
	 * JispPackage.getJispIconIterator() returns them.
	 * </p>
	 * <p>
	 * An iterator for the list is get, and it's tested that all the returned
	 * elements are the ones they should be based on the criteria above.
	 * </p>
	 * 
	 * @throws JDOMException
	 *             If a JDOMException when parsing the stored icondef file
	 *             occurs.
	 * @throws IOException
	 *             If a IOException when parsing the stored icondef file occurs.
	 * @throws JispIncompletePackageException
	 *             If the expected icondef.xml used for tests isn't valid.
	 */
	public void testStore() throws JDOMException, IOException,
			JispIncompletePackageException {
		JispPackageForTests jispPackageForTests = new JispPackageForTests();

		Document icondefExpected = new JispPackageToIcondefXml(
				jispPackageForTests.jispPackage).toIcondefXml();

		try {
			List list = new ArrayList();
			new JispPackageStorerDummy(jispPackageForTests.jispPackage, list)
					.store();

			Iterator listIterator = list.iterator();

			assertEquals("JispPackageForTests/icondef.xml", listIterator.next());

			Document icondefActual = new SAXBuilder()
					.build(new ByteArrayInputStream((byte[]) listIterator
							.next()));
			assertEquals(icondefExpected.toString(), icondefActual.toString());

			assertEquals("JispPackageForTests/"
					+ jispPackageForTests.jispObject1_1.getName(), listIterator
					.next());
			assertEquals(jispPackageForTests.jispObject1_1.getData(),
					listIterator.next());

			assertEquals("JispPackageForTests/"
					+ jispPackageForTests.jispObject2_1.getName(), listIterator
					.next());
			assertEquals(jispPackageForTests.jispObject2_1.getData(),
					listIterator.next());

			assertEquals("JispPackageForTests/"
					+ jispPackageForTests.jispObject3_1.getName(), listIterator
					.next());
			assertEquals(jispPackageForTests.jispObject3_1.getData(),
					listIterator.next());

			assertEquals("JispPackageForTests/"
					+ jispPackageForTests.jispObject4_1.getName(), listIterator
					.next());
			assertEquals(jispPackageForTests.jispObject4_1.getData(),
					listIterator.next());

			assertEquals("JispPackageForTests/"
					+ jispPackageForTests.jispObject4_2.getName(), listIterator
					.next());
			assertEquals(jispPackageForTests.jispObject4_2.getData(),
					listIterator.next());

			assertFalse(listIterator.hasNext());
		} catch (JispIncompletePackageException e) {
			fail("A JispIncompletePackageException occured when storing the JispPackage: "
					+ e.getMessage());
		}
	}

	/**
	 * <p>
	 * Stores a package specifying the root directory name using the dummy
	 * subclass created to test.<br>
	 * Store method using the dummy class stores the JispPackage in a list. The
	 * first element stored is icondef, and then all the objects in the order
	 * JispPackage.getJispIconIterator() returns them.
	 * </p>
	 * <p>
	 * An iterator for the list is get, and it's tested that all the returned
	 * elements are the ones they should be based on the criteria above.
	 * </p>
	 * 
	 * @throws JDOMException
	 *             If a JDOMException when parsing the stored icondef file
	 *             occurs.
	 * @throws IOException
	 *             If a IOException when parsing the stored icondef file occurs.
	 * @throws JispIncompletePackageException
	 *             If the expected icondef.xml used for tests isn't valid.
	 */
	public void testStoreString() throws JDOMException, IOException,
			JispIncompletePackageException {
		JispPackageForTests jispPackageForTests = new JispPackageForTests();

		Document icondefExpected = new JispPackageToIcondefXml(
				jispPackageForTests.jispPackage).toIcondefXml();

		try {
			List list = new ArrayList();
			new JispPackageStorerDummy(jispPackageForTests.jispPackage, list)
					.store("palpatine/");

			Iterator listIterator = list.iterator();

			assertEquals("palpatine/icondef.xml", listIterator.next());

			Document icondefActual = new SAXBuilder()
					.build(new ByteArrayInputStream((byte[]) listIterator
							.next()));
			assertEquals(icondefExpected.toString(), icondefActual.toString());

			assertEquals("palpatine/"
					+ jispPackageForTests.jispObject1_1.getName(), listIterator
					.next());
			assertEquals(jispPackageForTests.jispObject1_1.getData(),
					listIterator.next());

			assertEquals("palpatine/"
					+ jispPackageForTests.jispObject2_1.getName(), listIterator
					.next());
			assertEquals(jispPackageForTests.jispObject2_1.getData(),
					listIterator.next());

			assertEquals("palpatine/"
					+ jispPackageForTests.jispObject3_1.getName(), listIterator
					.next());
			assertEquals(jispPackageForTests.jispObject3_1.getData(),
					listIterator.next());

			assertEquals("palpatine/"
					+ jispPackageForTests.jispObject4_1.getName(), listIterator
					.next());
			assertEquals(jispPackageForTests.jispObject4_1.getData(),
					listIterator.next());

			assertEquals("palpatine/"
					+ jispPackageForTests.jispObject4_2.getName(), listIterator
					.next());
			assertEquals(jispPackageForTests.jispObject4_2.getData(),
					listIterator.next());

			assertFalse(listIterator.hasNext());
		} catch (JispIncompletePackageException e) {
			fail("A JispIncompletePackageException occured when storing the JispPackage: "
					+ e.getMessage());
		}
	}

	/**
	 * Stores the JispPackage in a List.<br>
	 * For every file, it stores two elements in the list: first the filename
	 * and then the data.
	 */
	public class JispPackageStorerDummy extends JispPackageStorer {

		/**
		 * The list to store files in.
		 */
		private List storingList;

		/**
		 * Creates the JispPackageStorerDummy with the list to use.
		 * 
		 * @param jispPackage
		 *            The JispPackage to store.
		 * @param storingList
		 *            The list to store the package in.
		 */
		public JispPackageStorerDummy(JispPackage jispPackage, List storingList) {
			super(jispPackage);
			this.storingList = storingList;
		}

		/**
		 * Adds the filename and the data to the list.
		 */
		protected void addFileToStore(String filename, byte[] data) {
			storingList.add(filename);
			storingList.add(data);
		}
	}
}
