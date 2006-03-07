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

import java.util.Iterator;
import java.util.Map;

import de.berlios.jedi.common.entity.jisp.JispIcon;
import de.berlios.jedi.common.entity.jisp.JispObject;
import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.entity.jisp.JispPackagesList;

/**
 * <p>
 * Utility class to work with JispPackages.<br>
 * All the methods are utility methods and can be accessed statically.
 * </p>
 * <p>
 * Provides a method to get the default root directory name for a package,
 * rearrange JispObjects in a JispPackage...
 * </p>
 */
public class JispUtil {

	/**
	 * Returns the default root directory name got from the package's name.<br>
	 * The default root directory name is got from the package's name removing
	 * the white spaces and capitalizing the first letter of the words.
	 * 
	 * @param jispPackage
	 *            The JispPackage to get the default root directory name from.
	 * 
	 * @return The default root directory name.
	 */
	public static String getDefaultRootDirectoryName(JispPackage jispPackage) {
		String defaultRootDirectoryName = "";

		String packageName = jispPackage.getJispMetadata().getName();
		packageName = packageName.trim();

		int i = 0;
		while (i != -1) {
			packageName = Character.toUpperCase(packageName.charAt(i))
					+ packageName.substring(i + 1);
			i = packageName.indexOf(" ");
			if (i != -1) {
				defaultRootDirectoryName = defaultRootDirectoryName
						+ packageName.substring(0, i);
				i++;
			} else {
				defaultRootDirectoryName = defaultRootDirectoryName
						+ packageName.substring(0);
			}
		}

		return defaultRootDirectoryName;
	}

	/**
	 * Rearranges the JispObjects in the JispPackage.<br>
	 * The JispObjects are moved (keeping also the directories they were in, if
	 * any) to a directory called like the package's name, removing the white
	 * spaces and capitalizing the first letter of the words. Moving a
	 * JispObject consists simply in renaming it.
	 * 
	 * @param jispPackage
	 *            The JispPackage to rearrange its JispObjects.
	 */
	public static void rearrangeJispObjects(JispPackage jispPackage) {
		Iterator jispIconsIterator = jispPackage.jispIconsIterator();
		while (jispIconsIterator.hasNext()) {
			JispIcon jispIcon = (JispIcon) jispIconsIterator.next();

			Iterator jispObjectsIterator = jispIcon.jispObjectsIterator();
			while (jispObjectsIterator.hasNext()) {
				JispObject jispObject = (JispObject) jispObjectsIterator.next();
				String name = JispUtil.getDefaultRootDirectoryName(jispPackage)
						+ "/" + jispObject.getName();
				jispObject.setName(name);
			}
		}
	}

	/**
	 * Adds the JispPackages from the JispPackagesList in the given Map,
	 * indexing them by the default name of the root directory.<br>
	 * If two packages have the same default root directory name, the second
	 * saved overwrites the first.
	 * 
	 * @param map
	 *            The Map to add the JispPackages to.
	 * @param jispPackagesList
	 *            The list of the JispPackages to be added.
	 */
	public static void indexJispPackagesByDefaultRootDirectoryName(Map map,
			JispPackagesList jispPackagesList) {
		Iterator jispPackagesIterator = jispPackagesList.iterator();
		while (jispPackagesIterator.hasNext()) {
			JispPackage jispPackage = (JispPackage) jispPackagesIterator.next();
			map.put(getDefaultRootDirectoryName(jispPackage), jispPackage);
		}
	}
}
