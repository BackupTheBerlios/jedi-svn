/*
 * Copyright 2006 Daniel Calviño Sánchez
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

import java.util.Hashtable;
import java.util.Map;

import de.berlios.jedi.common.entity.jisp.JispMetadata;
import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.entity.jisp.JispPackagesList;
import junit.framework.TestCase;

/**
 * Test file for JispUtil class.
 */
public class JispUtilTest extends TestCase {

	/**
	 * Gets the default root directory name for a JispPackage.<br>
	 * The default root directory name is got from the package's name removing
	 * the white spaces and capitalizing the first letter of the words.<br>
	 * It is tested that an all lower case string, a mixed lower with upper case
	 * string, and a mixed case string beggining with upper case get capitalized
	 * as they should. The string are used as the name of a JispMetadata in a
	 * JispPackage.
	 */
	public void testGetDefaultRootDirectoryName() {
		JispPackage jispPackage = new JispPackage();
		JispMetadata jispMetadata = new JispMetadata();
		jispPackage.setJispMetadata(jispMetadata);

		// Test with an all lower case string
		jispMetadata.setName("credits will do fine");

		assertEquals("CreditsWillDoFine", JispUtil
				.getDefaultRootDirectoryName(jispPackage));

		// Test with an mixed lower and upper case string
		jispMetadata.setName("may the Force be with you");

		assertEquals("MayTheForceBeWithYou", JispUtil
				.getDefaultRootDirectoryName(jispPackage));

		// Test with an mixed lower and upper case string beggining with upper
		// case
		jispMetadata.setName("I am the Senate");

		assertEquals("IAmTheSenate", JispUtil
				.getDefaultRootDirectoryName(jispPackage));
	}

	/**
	 * Rearranges the JispObjects in the JispPackage.<br>
	 * The JispObjects are moved (keeping also the directories they were in, if
	 * any) to a directory called like the default root directory name.<br>
	 * It is tested that rearranging a JispPackage moves all its JispObjects
	 * where they should be.
	 */
	public void testRearrangeJispObjects() {
		JispPackageForTests jispPackageForTests = new JispPackageForTests();

		JispUtil.rearrangeJispObjects(jispPackageForTests.jispPackage);

		String defaultRootDirectoryName = JispUtil
				.getDefaultRootDirectoryName(jispPackageForTests.jispPackage);

		assertEquals(defaultRootDirectoryName + "/aDirectory/more 0 array",
				jispPackageForTests.jispObject1_1.getName());

		assertEquals(defaultRootDirectoryName + "/0 array",
				jispPackageForTests.jispObject2_1.getName());

		assertEquals(defaultRootDirectoryName + "/aDirectory/more 0 array",
				jispPackageForTests.jispObject2_2.getName());

		assertEquals(defaultRootDirectoryName + "/another 0 array",
				jispPackageForTests.jispObject3_1.getName());

		assertEquals(defaultRootDirectoryName
				+ "/living in a 0 array world (8)",
				jispPackageForTests.jispObject4_1.getName());

		assertEquals(defaultRootDirectoryName
				+ "/anotherDirectory/andAnother/and finally a 0 array",
				jispPackageForTests.jispObject4_2.getName());
	}

	/**
	 * Adds the JispPackages from the JispPackagesList in the given Map using
	 * the default root directory name as index.<br>
	 * It is tested that all the JispPackages from a JispPackagesList are added
	 * using as key the default root directory name.
	 */
	public void testIndexJispPackagesByDefaultRootDirectoryName() {
		Map jispPackagesMap = new Hashtable();
		JispPackagesList jispPackagesList = new JispPackagesList();

		// Creating the JispPackages to use for tests
		JispPackage jispPackage1 = new JispPackage();
		JispMetadata jispMetadata1 = new JispMetadata();
		jispMetadata1.setName("jisp package 1");
		jispPackage1.setJispMetadata(jispMetadata1);
		jispPackagesList.addJispPackage(jispPackage1);

		JispPackage jispPackage2 = new JispPackage();
		JispMetadata jispMetadata2 = new JispMetadata();
		jispMetadata2.setName("jisp package 2");
		jispPackage2.setJispMetadata(jispMetadata2);
		jispPackagesList.addJispPackage(jispPackage2);

		JispPackage jispPackage3 = new JispPackage();
		JispMetadata jispMetadata3 = new JispMetadata();
		jispMetadata3.setName("jisp package 3");
		jispPackage3.setJispMetadata(jispMetadata3);
		jispPackagesList.addJispPackage(jispPackage3);

		// Indexing the jispPackages
		JispUtil.indexJispPackagesByDefaultRootDirectoryName(jispPackagesMap,
				jispPackagesList);

		// Testing that they were indexed as they should
		assertSame(jispPackage1, jispPackagesMap.get(JispUtil
				.getDefaultRootDirectoryName(jispPackage1)));
		assertSame(jispPackage2, jispPackagesMap.get(JispUtil
				.getDefaultRootDirectoryName(jispPackage2)));
		assertSame(jispPackage3, jispPackagesMap.get(JispUtil
				.getDefaultRootDirectoryName(jispPackage3)));
		assertEquals(3, jispPackagesMap.size());
	}
}
