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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.berlios.jedi.common.config.ConfigurationFactory;
import de.berlios.jedi.common.entity.jisp.JispPackage;

/**
 * <p>
 * Cache for persisted JispPackages.<br>
 * Implemented as a Singleton, it caches the JispPackages so when a JispPackage
 * equals to another previously saved JispPackage, the previously saved is
 * returned.<br>
 * The JispPackages can also be removed from the cache.
 * </p>
 * <p>
 * The JispPackagesCache can be enabled or disabled using the configuration
 * files.
 * </p>
 * <p>
 * The getJispPackage and removeCachedJispPackage methods are synchronized, so
 * no external synchronization is needed.
 * </p>
 * <p>
 * Note: the jispPackagesCache returned is unique for a JVM. But if its deployed
 * along various JVM, every JVM will have its own jispPackagesCache. That is, if
 * the cache is enabled, the application can not be used with various JVM
 * simultaneously.
 * </p>
 */
public class JispPackagesCache {

	/**
	 * The jispPackagesCache of the class.
	 */
	public static JispPackagesCache jispPackagesCache = new JispPackagesCache();

	/**
	 * The set containing the cached JispPackages.
	 */
	private Set cachedJispPackages;

	/**
	 * Private constructor ensures that no instances will be created from out
	 * the class.
	 */
	private JispPackagesCache() {
		cachedJispPackages = new HashSet();
	}

	/**
	 * Returns the jispPackagesCache of this class.<br>
	 * Note: the jispPackagesCache returned is unique for a JVM. But if its
	 * deployed along various JVM, every JVM will have its own
	 * jispPackagesCache. That is, it can not be used with various JVM
	 * simultaneously.
	 * 
	 * @return The jispPackagesCache of this class.
	 */
	public static JispPackagesCache getJispPackagesCache() {
		return jispPackagesCache;
	}

	/**
	 * Returns a cached JispPackage equals to the one specified.<br>
	 * If no JispPackage equals to it is cached, a clone is made, cached and
	 * returned (if the cache is enabled).<br>
	 * If the cache isn't enabled, a clone of the specified jispPackage is
	 * returned.
	 * 
	 * @param jispPackage
	 *            The JispPackage to get a cached one equals to it.
	 * @return The cached JispPackage equals to the one specified, or a clone if
	 *         the cache isn't enabled.
	 */
	public synchronized JispPackage getJispPackage(JispPackage jispPackage) {
		if (ConfigurationFactory.getConfiguration().getBoolean(
				"data.jispPackagesCacheEnabled")) {

			JispPackage cachedJispPackage = null;

			Iterator iterator = cachedJispPackages.iterator();
			while (iterator.hasNext()) {
				cachedJispPackage = (JispPackage) iterator.next();
				if (jispPackage.equals(cachedJispPackage)) {
					return cachedJispPackage;
				}
			}

			// If the JispPackage isn't already cached, do it now
			cachedJispPackage = (JispPackage) jispPackage.clone();
			cachedJispPackages.add(cachedJispPackage);

			return cachedJispPackage;
		}

		return (JispPackage) jispPackage.clone();
	}

	/**
	 * Removes a cached JispPackage from the cache.<br>
	 * The JispPackage to be removed must be equal, but doesn't need to be the
	 * same, to the one specified.
	 * 
	 * @param jispPackage
	 *            The JispPackage to be removed from the cache.
	 * @return True if the JispPackage was removed; false otherwise.
	 */
	public synchronized boolean removeCachedJispPackage(JispPackage jispPackage) {
		return cachedJispPackages.remove(jispPackage);
	}
}
