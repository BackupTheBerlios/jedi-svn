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

import junit.framework.TestCase;
import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.jisp.JispStoredToJispPackage;
import de.berlios.jedi.common.jisp.exception.JispInvalidIcondefException;
import de.berlios.jedi.common.jisp.exception.JispStoredException;
import de.berlios.jedi.logic.admin.JispFileWrapper;
import de.berlios.jedi.testUtilities.DiskFilesRetriever;

/**
 * Test file for JispPackagesCache class.<br>
 * The test are supossed to be executed with the cache enabled.
 */
public class JispPackagesCacheTest extends TestCase {

	/**
	 * The JispPackagesCache to test.
	 */
	private JispPackagesCache jispPackagesCache;

	/**
	 * Sets up the test needed objects.
	 */
	protected void setUp() {
		jispPackagesCache = JispPackagesCache.getJispPackagesCache();
	}

	/**
	 * Returns a cached JispPackage equals to the one specified.<br>
	 * It is tested that a JispPackage equals to the one specified is returned,
	 * and calling again the method returns the same JispPackage. The same is
	 * tested adding more JispPackages.
	 * 
	 * @throws JispStoredException
	 *             If a problem occurs when retrieving the JispPackages to test
	 *             from the JispFile.
	 * @throws JispInvalidIcondefException
	 *             If the icondef of the JispFiles used in tests isn't valid.
	 */
	public void testGetJispPackage() throws JispInvalidIcondefException,
			JispStoredException {
		// Preparing the JispPackages to be cached
		JispPackage jispPackageOk = new JispStoredToJispPackage(
				new JispFileWrapper(DiskFilesRetriever
						.getJispFile("src/test/testFiles/jispFiles/Ok/Ok.jisp")))
				.toJispPackage();
		JispPackage jispPackageOk2 = new JispStoredToJispPackage(
				new JispFileWrapper(DiskFilesRetriever
						.getJispFile("src/test/testFiles/jispFiles/Ok2/Ok2.jisp")))
				.toJispPackage();

		// Caching the JispPackageOk
		JispPackage jispPackageOkCached = jispPackagesCache
				.getJispPackage(jispPackageOk);

		assertEquals(jispPackageOk, jispPackageOkCached);
		assertNotSame(jispPackageOk, jispPackageOkCached);

		// Getting again the cached JispPackageOk
		JispPackage jispPackageOkCachedAgain = jispPackagesCache
				.getJispPackage(jispPackageOk);

		assertSame(jispPackageOkCached, jispPackageOkCachedAgain);

		// Testing with another JispPackage
		// Caching the JispPackageOk2
		JispPackage jispPackageOk2Cached = jispPackagesCache
				.getJispPackage(jispPackageOk2);
		jispPackageOkCached = jispPackagesCache.getJispPackage(jispPackageOk);

		assertEquals(jispPackageOk, jispPackageOkCached);
		assertNotSame(jispPackageOk, jispPackageOkCached);
		assertEquals(jispPackageOk2, jispPackageOk2Cached);
		assertNotSame(jispPackageOk2, jispPackageOk2Cached);

		// Getting again the cached JispPackageOk2
		JispPackage jispPackageOk2CachedAgain = jispPackagesCache
				.getJispPackage(jispPackageOk2);

		assertSame(jispPackageOk2Cached, jispPackageOk2CachedAgain);
	}

	/**
	 * Removes a cached JispPackage.<br>
	 * It is tested that a cached JispPackage is removed and this is ensured
	 * getting again a cached JispPackage which must not be the same that the
	 * previously cached.<br>
	 * It is also tested that trying to remove a not cached JispPackage returns
	 * false.
	 * 
	 * @throws JispStoredException
	 *             If a problem occurs when retrieving the JispPackages to test
	 *             from the JispFile.
	 * @throws JispInvalidIcondefException
	 *             If the icondef of the JispFiles used in tests isn't valid.
	 */
	public void testRemoveJispPackage() throws JispInvalidIcondefException,
			JispStoredException {
		// Preparing the JispPackages to be cached
		JispPackage jispPackageOk = new JispStoredToJispPackage(
				new JispFileWrapper(DiskFilesRetriever
						.getJispFile("src/test/testFiles/jispFiles/Ok/Ok.jisp")))
				.toJispPackage();
		JispPackage jispPackageOkEdited = new JispStoredToJispPackage(
				new JispFileWrapper(DiskFilesRetriever
						.getJispFile("src/test/testFiles/jispFiles/Ok edited/Ok edited.jisp")))
				.toJispPackage();

		// Caching the JispPackageOk
		JispPackage jispPackageOkCached = jispPackagesCache
				.getJispPackage(jispPackageOk);
		
		assertTrue(jispPackagesCache.removeCachedJispPackage(jispPackageOk));

		// Getting again the cached JispPackageOk
		JispPackage jispPackageOkCachedAgain = jispPackagesCache
				.getJispPackage(jispPackageOk);

		assertNotSame(jispPackageOkCached, jispPackageOkCachedAgain);
		
		//Trying to remove a JispPackage not added
		assertFalse(jispPackagesCache.removeCachedJispPackage(jispPackageOkEdited));
	}
}
