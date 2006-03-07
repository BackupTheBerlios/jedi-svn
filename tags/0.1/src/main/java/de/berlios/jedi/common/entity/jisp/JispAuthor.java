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

package de.berlios.jedi.common.entity.jisp;

import java.io.Serializable;

/**
 * Represents an author in the metadata information of a Jisp package.<br>
 * Objects from this class will be persisted.
 */
public class JispAuthor implements Cloneable, Serializable {

	/**
	 * Generated <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = 1193678141353620650L;

	/**
	 * Persistent identifier.
	 */
	private Long id;

	/**
	 * The author's name.
	 */
	private String name;

	/**
	 * The author's Jabber identifier.
	 */
	private String jid;

	/**
	 * The author's email address.
	 */
	private String email;

	/**
	 * The author's webpage.
	 */
	private String www;

	/**
	 * Creates a new author, with its name, jid, email and webpage as null.
	 */
	public JispAuthor() {
		name = null;
		jid = null;
		email = null;
		www = null;
	}

	/**
	 * Compares this JispAuthor to the specified object.<br>
	 * The result is true if and only if the argument is not null and is a
	 * JispAuthor object with the same values of fields.
	 * 
	 * @param object
	 *            The object to compare against.
	 * @return True if the JispAuthors are equal; false otherwise.
	 */
	public boolean equals(Object object) {
		if (object == null)
			return false;
		if (!(object instanceof JispAuthor))
			return false;
		JispAuthor jispAuthor = (JispAuthor) object;
		if (!(name == null ? jispAuthor.name == null : name
				.equals(jispAuthor.name)))
			return false;
		if (!(jid == null ? jispAuthor.jid == null : jid.equals(jispAuthor.jid)))
			return false;
		if (!(email == null ? jispAuthor.email == null : email
				.equals(jispAuthor.email)))
			return false;
		if (!(www == null ? jispAuthor.www == null : www.equals(jispAuthor.www)))
			return false;
		return true;
	}

	/**
	 * Returns a hash code value for the object.<br>
	 * The objects are mutable, so the hashCode is the same for all objects of
	 * this class.
	 * 
	 * @return The hashCode value for the object.
	 */
	public int hashCode() {
		return 2;
	}

	/**
	 * Creates and returns a copy of this object.
	 * 
	 * @return A clone of this instance.
	 */
	public Object clone() {
		JispAuthor clone = new JispAuthor();

		if (id != null) {
			clone.id = new Long(id.longValue());
		}
		if (jid != null) {
			clone.jid = new String(jid);
		}
		if (name != null) {
			clone.name = new String(name);
		}
		if (email != null) {
			clone.email = new String(email);
		}
		if (www != null) {
			clone.www = new String(www);
		}

		return clone;
	}

	/**
	 * Returns the representation of the JispAuthor as a String.
	 * 
	 * @return The representation of the JispAuthor as a String.
	 */
	public String toString() {
		return "name: " + name + " jid: " + jid;
	}

	/**
	 * Returns the email.
	 * 
	 * @return The email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Returns the jid.
	 * 
	 * @return The jid.
	 */
	public String getJid() {
		return jid;
	}

	/**
	 * Returns the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the webpage.
	 * 
	 * @return The webpage.
	 */
	public String getWww() {
		return www;
	}

	/**
	 * Sets the email.
	 * 
	 * @param email
	 *            The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Sets the jid.
	 * 
	 * @param jid
	 *            The jid to set.
	 */
	public void setJid(String jid) {
		this.jid = jid;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the webpage.
	 * 
	 * @param webpage
	 *            The webpage to set.
	 */
	public void setWww(String webpage) {
		this.www = webpage;
	}
}
