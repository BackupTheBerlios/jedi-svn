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

package de.berlios.jedi.data.admin;

import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.exception.DataException;
import de.berlios.jedi.data.HibernateDataManager;
import de.berlios.jedi.data.HibernateUtil;
import de.berlios.jedi.data.JispPackagesCache;

/**
 * <p>
 * Manages all the persistent admin data operations using Hibernate.<br>
 * It provides methods to save and delete JispPackages.
 * </p>
 * <p>
 * All the Hibernate related configuration is done in a declarative way using
 * the .xml files.
 * </p>
 * <p>
 * Due to the internal use of JispPackagesCache, it can not be used with various
 * JVM simultaneously.
 * </p>
 */
public class HibernateAdminDataManager implements AdminDataManager {

	/**
	 * Saves a JispPackage.<br>
	 * If the JispPackage was already stored, it will be updated. The
	 * JispPackage is identified by its name and version (stored in its
	 * JispMetadata object).
	 * 
	 * @param jispPackage
	 *            The JispPackage to save.
	 * @throws DataException
	 *             If there was a problem when saving a JispPackage.
	 */
	public void saveJispPackage(JispPackage jispPackage) throws DataException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			session.saveOrUpdate(jispPackage);

			tx.commit();
		} catch (HibernateException e) {
			LogFactory.getLog(HibernateDataManager.class).error(
					"Hibernate threw an exception when"
							+ " saving a JispPackage", e);
			throw new DataException(
					"A problem occured when saving a JispPackage", e);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	/**
	 * Deletes a stored JispPackage from the persistent store.<br>
	 * Also removes it from the JispPackagesCache.
	 * 
	 * @param jispPackage
	 *            The JispPackage to delete.
	 * @throws DataException
	 *             If there was a problem when deleting a stored JispPackage.
	 */
	public void deleteJispPackage(JispPackage jispPackage) throws DataException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			session.delete(jispPackage);

			JispPackagesCache.getJispPackagesCache().removeCachedJispPackage(
					jispPackage);

			tx.commit();
		} catch (HibernateException e) {
			LogFactory.getLog(HibernateDataManager.class).error(
					"Hibernate threw an exception when"
							+ " deleting a stored JispPackage", e);
			throw new DataException(
					"A problem occured when deleting a stored JispPackage", e);
		} finally {
			HibernateUtil.closeSession();
		}
	}
}
