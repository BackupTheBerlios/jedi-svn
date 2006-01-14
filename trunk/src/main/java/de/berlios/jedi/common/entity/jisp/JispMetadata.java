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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Represents the metadata information in a Jisp package.<br>
 * The metadata includes the name, version, description, author, creation date
 * and webpage for the package.<br>
 * Objects from this class will be persisted.
 */
public class JispMetadata implements Cloneable, Serializable {

	/**
	 * Generated <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = -8141671710888244895L;

	/**
	 * Persistent identifier.
	 */
	public Long id;

	/**
	 * The package's name.
	 */
	private String name;

	/**
	 * The package's version.
	 */
	private String version;

	/**
	 * The package's description.
	 */
	private String description;

	/**
	 * The list with the package's authors.
	 */
	private List jispAuthors;

	/**
	 * The creation date.
	 */
	private Date creation;

	/**
	 * The package's webpage.
	 */
	private String home;

	/**
	 * Creates a new JispMetadata, with its Strings and Dates as null and
	 * without authors.
	 */
	public JispMetadata() {
		name = null;
		version = null;
		description = null;
		jispAuthors = new ArrayList();
		creation = null;
		home = null;
	}

	/**
	 * Compares this JispMetadata to the specified object.<br>
	 * The result is true if and only if the argument is not null and is a
	 * JispMetadata object with the same values of fields.
	 * 
	 * @param object
	 *            The object to compare against.
	 * @return True if the JispMetadatas are equal; false otherwise.
	 */
	public boolean equals(Object object) {
		if (object == null)
			return false;
		if (!(object instanceof JispMetadata))
			return false;
		JispMetadata jispMetadata = (JispMetadata) object;
		if (!(name == null ? jispMetadata.name == null : name
				.equals(jispMetadata.name)))
			return false;
		if (!(version == null ? jispMetadata.version == null : version
				.equals(jispMetadata.version)))
			return false;
		if (!(description == null ? jispMetadata.description == null
				: description.equals(jispMetadata.description)))
			return false;
		if (!jispAuthors.equals(jispMetadata.jispAuthors))
			return false;
		if (!(creation == null ? jispMetadata.creation == null : creation
				.equals(jispMetadata.creation)))
			return false;
		if (!(home == null ? jispMetadata.home == null : home
				.equals(jispMetadata.home)))
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
		return 1;
	}

	/**
	 * Creates and returns a copy of this object.
	 * 
	 * @return A clone of this instance.
	 */
	public Object clone() {
		JispMetadata clone = new JispMetadata();

		if (id != null) {
			clone.id = new Long(id.longValue());
		}
		if (name != null) {
			clone.name = new String(name);
		}
		if (version != null) {
			clone.version = new String(version);
		}
		if (description != null) {
			clone.description = new String(description);
		}
		if (creation != null) {
			clone.creation = new Date(creation.getTime());
		}
		if (home != null) {
			clone.home = new String(home);
		}
		Iterator jispAuthorsIterator = jispAuthors.iterator();
		while (jispAuthorsIterator.hasNext()) {
			clone.jispAuthors.add(((JispAuthor) jispAuthorsIterator.next())
					.clone());
		}

		return clone;
	}

	/**
	 * Returns the representation of the JispMetadata as a String.
	 * 
	 * @return The representation of the JispMetadata as a String.
	 */
	public String toString() {
		return "name: " + name + "\nversion: " + version + "\ndescription: "
				+ description + "\nauthors: " + jispAuthors.toString()
				+ "\ncreation: "
				+ (creation != null ? creation.toString() : null) + "\nhome: "
				+ home;
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
	 * Returns the version.
	 * 
	 * @return The version.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Returns the description.
	 * 
	 * @return The description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the creation.
	 * 
	 * @return The creation.
	 */
	public Date getCreation() {
		return creation;
	}

	/**
	 * Returns the home.
	 * 
	 * @return The home.
	 */
	public String getHome() {
		return home;
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
	 * Sets the version.
	 * 
	 * @param version
	 *            The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the creation.
	 * 
	 * @param creation
	 *            The creation to set.
	 */
	public void setCreation(Date creation) {
		this.creation = creation;
	}

	/**
	 * Sets the home.
	 * 
	 * @param home
	 *            The home to set.
	 */
	public void setHome(String home) {
		this.home = home;
	}

	/**
	 * <p>
	 * Adds an author to the metadata.
	 * </p>
	 * <p>
	 * jispAuthor can not be null.
	 * </p>
	 * 
	 * @param jispAuthor
	 *            The author to add.
	 */
	public void addJispAuthor(JispAuthor jispAuthor) {
		jispAuthors.add(jispAuthor);
	}

	/**
	 * Test if the JispMetadata contains the specified JispAuthor. <br>
	 * Returns true if and only if this JispMetadata contains at least one
	 * JispAuthor containedJispAuthor such that (jispAuthor==null ?
	 * containedJispAuthor==null : jispAuthor.equals(containedJispAuthor)).
	 * 
	 * @param jispAuthor
	 *            The JispAuthor whose presence is going to be tested.
	 * @return True if the JispMetadata contains the specified JispAuthor.
	 */
	public boolean containsJispAuthor(JispAuthor jispAuthor) {
		return jispAuthors.contains(jispAuthor);
	}

	/**
	 * Returns an iterator for the JispAuthors.<br>
	 * The iterator allows erasing elements.
	 * 
	 * @return An iterator for the JispAuthors.
	 */
	public Iterator jispAuthorsIterator() {
		return jispAuthors.iterator();
	}

	/**
	 * <p>
	 * Removes the first ocurrence of the JispAuthor in JispMetadata.
	 * </p>
	 * <p>
	 * jispAuthor can not be null.
	 * </p>
	 * 
	 * @param jispAuthor
	 *            The JispAuthor to remove.
	 * @return True if the JispMetadata contained the specified JispAuthor.
	 */
	public boolean removeJispAuthor(JispAuthor jispAuthor) {
		return jispAuthors.remove(jispAuthor);
	}
}
