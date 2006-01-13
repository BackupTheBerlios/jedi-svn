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

package de.berlios.jedi.testUtilities;

import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.Transaction;

import de.berlios.jedi.data.HibernateUtil;

/**
 * Utility class for data related operations.<br>
 * Provides a method to clean the database used in tests.
 */
public class DataUtil {

	/**
	 * Cleans the database used for testing.<br>
	 * It deletes all the JispPackages and its children in the database.
	 */
	public static void cleanDatabase() {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();

		try {
			Iterator jispPackagesIterator = session.createQuery(
					"FROM JispPackage").list().iterator();
			while (jispPackagesIterator.hasNext()) {
				session.delete(jispPackagesIterator.next());
			}

			tx.commit();
		} finally {
			HibernateUtil.closeSession();
		}
	}
}
