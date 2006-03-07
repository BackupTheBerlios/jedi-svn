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

import org.apache.commons.logging.LogFactory;
import org.hibernate.*;
import org.hibernate.cfg.*;

import de.berlios.jedi.common.config.ConfigurationFactory;

/**
 * <p>
 * Helper class for using Hibernate.<br>
 * Allows getting a session instance for Thread (and closing it when it is not
 * longer needed).
 * </p>
 * 
 * <p>
 * This class was got from Hibernate Reference Documentation,
 * http://www.hibernate.org/hib_docs/v3/reference/en/html/index.html<br>
 * Only minor changes were made.
 * </p>
 */
public class HibernateUtil {

	/**
	 * A thread local variable for Hibernate sessions.
	 */
	public static final ThreadLocal session = new ThreadLocal();

	/**
	 * Factory for session creating.
	 */
	private static final SessionFactory sessionFactory;

	static {
		try {
			// Create the SessionFactory
			sessionFactory = new Configuration().configure(
					ConfigurationFactory.getConfiguration().getString(
							"path.hibernateConfigFile")).buildSessionFactory();
		} catch (Throwable ex) {
			LogFactory.getLog(HibernateUtil.class).error(
					"Initial SessionFactory creation failed.", ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * Returns a new session if this Thread has none yet, or the same session
	 * requested before.
	 * 
	 * @return An Hibernate session.
	 * 
	 * @throws HibernateException
	 *             If there was a problem opening the Hibernate session.
	 */
	public static Session currentSession() throws HibernateException {
		Session s = (Session) session.get();
		// Open a new Session, if this Thread has none yet
		if (s == null) {
			s = sessionFactory.openSession();
			session.set(s);
		}
		return s;
	}

	/**
	 * Closes the active session.<br>
	 * If there was no active session for this Thread, nothing is done.
	 */
	public static void closeSession() {
		Session s = (Session) session.get();
		if (s != null) {
			try {
				s.close();
			} catch (HibernateException e) {
				LogFactory.getLog(HibernateUtil.class).error(
						"An error occured when closing the Hibernate session",
						e);
			}
		}
		session.set(null);
	}
}