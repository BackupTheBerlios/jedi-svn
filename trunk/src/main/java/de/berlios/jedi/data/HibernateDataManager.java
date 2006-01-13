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

import java.util.Iterator;

import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.entity.jisp.JispPackagesList;
import de.berlios.jedi.common.exception.DataException;

/**
 * <p>
 * Manages all the persistent common data operations using Hibernate.<br>
 * It provides methods to retrieve all the stored JispPackages.
 * </p>
 * <p>
 * All the Hibernate related configuration is done in a declarative way using
 * the .xml files.
 * </p>
 * <p>
 * Due to the internal use of JispPackagesCache, if the cache is enabled this
 * class can not be used with various JVM simultaneously.
 * </p>
 */
public class HibernateDataManager implements DataManager {

	/**
	 * Returns a JispPackagesList with all the previously saved JispPackages.<br>
	 * The returned JispPackages are get from the JispPackagesCache.
	 * 
	 * @return A JispPackagesList with all the previously saved JispPackages.
	 * @throws DataException
	 *             If there was a problem when getting the stored JispPackages.
	 */
	public JispPackagesList getJispPackagesList() throws DataException {
		JispPackagesList jispPackagesList = new JispPackagesList();

		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Iterator queryResultIterator = session.createQuery(
					"FROM JispPackage").iterate();

			JispPackagesCache jispPackagesCache = JispPackagesCache
					.getJispPackagesCache();
			while (queryResultIterator.hasNext()) {
				jispPackagesList.addJispPackage(jispPackagesCache
						.getJispPackage((JispPackage) queryResultIterator
								.next()));
			}

			tx.commit();
		} catch (HibernateException e) {
			LogFactory.getLog(HibernateDataManager.class).error(
					"Hibernate threw an exception when getting"
							+ " the stored JispPackages", e);
			throw new DataException(
					"A problem occured when getting the stored JispPackages", e);
		} finally {
			HibernateUtil.closeSession();
		}

		return jispPackagesList;
	}
}
