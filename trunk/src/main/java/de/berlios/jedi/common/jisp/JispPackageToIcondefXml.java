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

package de.berlios.jedi.common.jisp;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.jdom.*;

import de.berlios.jedi.common.entity.jisp.*;
import de.berlios.jedi.common.jisp.exception.JispIncompletePackageException;

/**
 * Gets the equivalent JDOM Document with icondef.xml format from a JispPackage.<br>
 * The Document represents the Icon Definition File. It's based on <a
 * href="http://www.jabber.org/jeps/jep-0038.html">JEP-038</a>, although isn't
 * equal to it. The used XML Schema format used can be seen on:<br>
 * <a href="http://jedi.berlios.de/schema/JISPSchema.xsd">
 * http://jedi.berlios.de/schema/JISPSchema.xsd</a>
 */
public class JispPackageToIcondefXml {

	/**
	 * Identifying code for the property to indicate an element child of
	 * another element.
	 */
	private static final int ELEMENT_CHILD_TYPE = 0;

	/**
	 * Identifying code for the property to indicate an attribute child of
	 * another element.
	 */
	private static final int ATTRIBUTE_CHILD_TYPE = 1;

	/**
	 * The JispPackage to get its representation.
	 */
	private JispPackage jispPackage;

	/**
	 * <p>
	 * Constructs a new JispPackageToIcondefXml with the JispPackage to use.
	 * </p>
	 * <p>
	 * jispPackage can not be null.
	 * </p>
	 * 
	 * 
	 * @param jispPackage
	 *            The JispPackage to use.
	 */
	public JispPackageToIcondefXml(JispPackage jispPackage) {
		this.jispPackage = jispPackage;
	}

	/**
	 * Returns the JispPackage representation as a JDOM Document with
	 * icondef.xml format.
	 * 
	 * @return The JDOM Document.
	 * @throws JispIncompletePackageException
	 *             If metadata or icons aren't complete (Metadata required
	 *             elements: name, version, description, at least one author,
	 *             creation. Authors must have a name.<br>
	 *             Icon required elements: at least one object, at least one
	 *             text. Objects must have a name and mime type. Texts must have
	 *             a text).
	 */
	public Document toIcondefXml() throws JispIncompletePackageException {
		Document icondef = new Document();
		icondef.setProperty("encoding", "UTF-8");
		Element icondefElement = new Element("icondef");
		icondef.setRootElement(icondefElement);

		addMeta(icondefElement);
		addIcons(icondefElement);

		return icondef;
	}

	/**
	 * Adds the metadata to the icondef element.
	 * 
	 * @param icondefElement
	 *            The icondef JDOM Element to put metadata in.
	 * @throws JispIncompletePackageException
	 *             If metadata isn't complete (Metadata required elements: name,
	 *             version, description, at least one author, creation. Authors
	 *             must have a name).
	 */
	private void addMeta(Element icondefElement)
			throws JispIncompletePackageException {
		Element meta = new Element("meta");

		addRequiredSimpleElement(meta, "name", jispPackage.getJispMetadata()
				.getName());
		addRequiredSimpleElement(meta, "version", jispPackage.getJispMetadata()
				.getVersion());
		addOptionalSimpleElement(meta, "description", jispPackage
				.getJispMetadata().getDescription());
		addMetaAuthors(meta);
		String creationString = null;
		if (jispPackage.getJispMetadata().getCreation() != null) {
			creationString = new SimpleDateFormat("yyyy-MM-dd")
					.format(jispPackage.getJispMetadata().getCreation());
		}
		addRequiredSimpleElement(meta, "creation", creationString);
		addOptionalSimpleElement(meta, "home", jispPackage.getJispMetadata()
				.getHome());

		icondefElement.addContent(meta);
	}

	/**
	 * Adds the author elements in the meta element.
	 * 
	 * @param metaElement
	 *            The meta element to add the authors in.
	 * @throws JispIncompletePackageException
	 *             If there are no authors, or an author has no name.
	 */
	private void addMetaAuthors(Element metaElement)
			throws JispIncompletePackageException {
		Iterator authors = jispPackage.getJispMetadata().jispAuthorsIterator();

		if (!authors.hasNext()) {
			throw new JispIncompletePackageException(
					"There are no authors in metadata");
		}

		while (authors.hasNext()) {
			JispAuthor jispAuthor = (JispAuthor) authors.next();
			Element author = addRequiredSimpleElement(metaElement, "author",
					jispAuthor.getName());
			addOptionalAttribute(author, "jid", jispAuthor.getJid());
		}
	}

	/**
	 * Adds the icons in the icondef element.
	 * 
	 * @param icondefElement
	 *            The element to add the icons in.
	 * @throws JispIncompletePackageException
	 *             If there are no icons in the package, or an icon isn't
	 *             complete (Icon required elements: at least one object, at
	 *             least one text. Objects must have a name and mime type. Texts
	 *             must have a text).
	 */
	private void addIcons(Element icondefElement)
			throws JispIncompletePackageException {
		Iterator icons = jispPackage.jispIconsIterator();

		if (!icons.hasNext())
			throw new JispIncompletePackageException(
					"There are no icons in JispPackage");

		while (icons.hasNext()) {
			JispIcon jispIcon = (JispIcon) icons.next();
			Element iconElement = new Element("icon");

			addIconTexts(iconElement, jispIcon);
			addIconObjects(iconElement, jispIcon);

			icondefElement.addContent(iconElement);
		}
	}

	/**
	 * Add the text from jispIcon in iconElement.
	 * 
	 * @param iconElement
	 *            The element to add the texts in.
	 * @param jispIcon
	 *            The JispIcon to get the texts from.
	 * @throws JispIncompletePackageException
	 *             If there are no texts in icon, or a text has no text.
	 */
	private void addIconTexts(Element iconElement, JispIcon jispIcon)
			throws JispIncompletePackageException {
		Iterator texts = jispIcon.jispTextsIterator();

		if (!texts.hasNext())
			throw new JispIncompletePackageException(
					"There are no texts in icon");

		while (texts.hasNext()) {
			JispText jispText = (JispText) texts.next();
			addRequiredSimpleElement(iconElement, "text", jispText.getText());

			// TODO Implement languages in JispText
			// addOptionalAttribute(iconElement, "xml:lang",
			// jispText.getLang());
		}
	}

	/**
	 * Add the objects from jispIcon in iconElement.
	 * 
	 * @param iconElement
	 *            The element to add the objects in.
	 * @param jispIcon
	 *            The JispIcon to get the objects from.
	 * @throws JispIncompletePackageException
	 *             If there are no objects in icon, or an object has no name or
	 *             mime type.
	 */
	private void addIconObjects(Element iconElement, JispIcon jispIcon)
			throws JispIncompletePackageException {
		Iterator objects = jispIcon.jispObjectsIterator();

		if (!objects.hasNext())
			throw new JispIncompletePackageException(
					"There are no objects in icon");

		while (objects.hasNext()) {
			JispObject jispObject = (JispObject) objects.next();
			Element object = addRequiredSimpleElement(iconElement, "object",
					jispObject.getName());
			addRequiredAttribute(object, "mime", jispObject.getMimeType());
		}

	}

	/**
	 * Adds a required simple element in a parent element with the specified
	 * name and text, provided text isn't null. If it's null, a
	 * JispIncompletePackageException is thrown. If parent is null nothing is
	 * added.
	 * 
	 * @param parent
	 *            The element where simple element should be added.
	 * @param name
	 *            The name of the simple element to add.
	 * @param text
	 *            The text of the simple element to add.
	 * @return If parent is null, null. Otherwise, the element added.
	 * @throws JispIncompletePackageException
	 *             If text is null.
	 */
	private Element addRequiredSimpleElement(Element parent, String name,
			String text) throws JispIncompletePackageException {
		return addRequiredSimpleChild(parent, name, text,
				ELEMENT_CHILD_TYPE);
	}

	/**
	 * Adds an optional element in a parent element with the specified name and
	 * text, provided text isn't null. If it's null, no element is added. If
	 * parent is null nothing is added.
	 * 
	 * @param parent
	 *            The element where simple element should be added.
	 * @param name
	 *            The name of the simple element to add.
	 * @param text
	 *            The text of the simple element to add.
	 * @return If parent is null, null. If no element was added, null.
	 *         Otherwise, the element added.
	 */
	private Element addOptionalSimpleElement(Element parent, String name,
			String text) {
		return addOptionalSimpleChild(parent, name, text,
				ELEMENT_CHILD_TYPE);
	}

	/**
	 * Adds a required attribute in a parent element with the specified name and
	 * value, provided value isn't null. If it's null, a
	 * JispIncompletePackageException is thrown. If parent is null nothing is
	 * added.
	 * 
	 * @param parent
	 *            The element where attribute should be added.
	 * @param name
	 *            The name of the attribute to add.
	 * @param value
	 *            The value of the attribute to add.
	 * @return If parent is null, null. Otherwise, the element where the
	 *         attribute was added.
	 * @throws JispIncompletePackageException
	 *             If value is null.
	 */
	private Element addRequiredAttribute(Element parent, String name,
			String value) throws JispIncompletePackageException {
		return addRequiredSimpleChild(parent, name, value,
				ATTRIBUTE_CHILD_TYPE);
	}

	/**
	 * Adds an optional attribute in a parent element with the specified name
	 * and value, provided value isn't null. If it's null, no attribute is
	 * added. If parent is null nothing is added.
	 * 
	 * @param parent
	 *            The element where attribute should be added.
	 * @param name
	 *            The name of the attribute to add.
	 * @param value
	 *            The value of the attribute to add.
	 * @return If parent is null, null. Otherwise, the element where the
	 *         attribute was added.
	 */
	private Element addOptionalAttribute(Element parent, String name,
			String value) {
		Element returnElement = addOptionalSimpleChild(parent, name,
				value, ATTRIBUTE_CHILD_TYPE);
		if (returnElement == null)
			return parent;
		return returnElement;
	}

	/**
	 * Adds a required simple element or attribute in a parent element with the
	 * specified name and text, provided text isn't null. If it's null, a
	 * JispIncompletePackageException is thrown. If parent is null nothing is
	 * added.
	 * 
	 * @param parent
	 *            The element where simple element or attribute should be added.
	 * @param name
	 *            The name of the simple element or attribute to add.
	 * @param text
	 *            The text/value of the simple element or attribute to add.
	 * @param childType
	 *            ELEMENT_CHILD_TYPE if the child is an element,
	 *            ATTRIBUTE_CHILD_TYPE if the child is an attribute.
	 * @return If parent is null, null. When adding an element, the element
	 *         added; when adding an attribute, the element where was added.
	 * @throws JispIncompletePackageException
	 *             If text is null.
	 */
	private Element addRequiredSimpleChild(Element parent, String name,
			String text, int childType)
			throws JispIncompletePackageException {
		if (text == null)
			throw new JispIncompletePackageException("Required "
					+ (childType == ELEMENT_CHILD_TYPE ? "element"
							: "attribute") + " named \"" + name
					+ "\" doesn't exist");
		return addSimpleChild(parent, name, text, childType);
	}

	/**
	 * Adds an optional element or attribute in a parent element with the
	 * specified name and text, provided text isn't null. If it's null, no
	 * element is added. If parent is null nothing is added.
	 * 
	 * @param parent
	 *            The element where simple element or attribute should be added.
	 * @param name
	 *            The name of the simple element or attribute to add.
	 * @param text
	 *            The text/value of the simple element or attribute to add.
	 * @param childType
	 *            ELEMENT_CHILD_TYPE if the child is an element,
	 *            ATTRIBUTE_CHILD_TYPE if the child is an attribute.
	 * @return If parent is null, null. If no element or attribute was added,
	 *         null. When adding an element, the element added; when adding an
	 *         attribute, the element where was added.
	 */
	private Element addOptionalSimpleChild(Element parent, String name,
			String text, int childType) {
		if (text == null)
			return null;
		return addSimpleChild(parent, name, text, childType);
	}

	/**
	 * Adds a new simple child (element or attribute) in a parent element
	 * with the specified name and text/value. If parent is null nothing is
	 * added.
	 * 
	 * @param parent
	 *            The element where simple element or attribute should be added.
	 * @param name
	 *            The name of the simple element or attribute to add.
	 * @param text
	 *            The text/value of the simple element or attribute to add.
	 * @param childType
	 *            ELEMENT_CHILD_TYPE if the child is an element,
	 *            ATTRIBUTE_CHILD_TYPE if the child is an attribute.
	 * @return If parent is null, null. When adding an element, the element
	 *         added; when adding an attribute, the element where was added.
	 */
	private Element addSimpleChild(Element parent, String name,
			String text, int childType) {
		if (parent == null)
			return null;
		switch (childType) {
		case ELEMENT_CHILD_TYPE:
			Element element = new Element(name);
			element.setText(text);
			parent.addContent(element);
			return element;

		case ATTRIBUTE_CHILD_TYPE:
			parent.setAttribute(name, text);
			return parent;

		default:
			// Bad child type code. Shouldn't happen...;
			return null;
		}
	}
}
