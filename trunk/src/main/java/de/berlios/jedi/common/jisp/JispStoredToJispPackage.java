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

import java.util.Date;
import java.util.Iterator;

import org.apache.commons.logging.LogFactory;
import org.jdom.*;
import org.joda.time.format.ISODateTimeFormat;

import de.berlios.jedi.common.entity.jisp.*;
import de.berlios.jedi.common.jisp.exception.JispInvalidIcondefException;

/**
 * Creator for JispPackage objects from a stored jisp package.<br>
 * This class provides a method to create a JispPackage from the data in a
 * JispStoredWrapper.
 * 
 * @see de.berlios.jedi.common.jisp.JispStoredWrapper
 */
public class JispStoredToJispPackage {

	/**
	 * The JispStoredWrapper to use.
	 */
	private JispStoredWrapper jispStoredWrapper;

	/**
	 * <p>
	 * Constructs a new JispStoredToJispPackage with a JispStoredWrapper.
	 * </p>
	 * <p>
	 * jispStoredWrapper can not be null.
	 * </p>
	 * 
	 * @param jispStoredWrapper
	 *            The JispStoredWrapper for the concrete stored jisp.
	 */
	public JispStoredToJispPackage(JispStoredWrapper jispStoredWrapper) {
		this.jispStoredWrapper = jispStoredWrapper;
	}

	/**
	 * Converts a stored Jisp in a JispPackage.
	 * 
	 * @return The JispPackage.
	 * @throws JispInvalidIcondefException
	 *             If icondef.xml in stored Jisp is not valid, not well-formed
	 *             or an object in icondef.xml doesn't exist in stored Jisp.
	 */
	public JispPackage toJispPackage() throws JispInvalidIcondefException {
		JispPackage jispPackage = new JispPackage();
		Document icondefDocument = jispStoredWrapper.getIcondef();
		Element icondefElement = icondefDocument.getRootElement();

		setMetadata(jispPackage, icondefElement);
		setIcons(jispPackage, icondefElement);

		return jispPackage;
	}

	/**
	 * Sets the metadata in a JispPackage from the icondef element in the
	 * icondef.xml file.<br>
	 * If creation date in icondef.xml can't be parsed as ISO 8601, a new date
	 * representing the time at which it was allocated is used.
	 * 
	 * @param jispPackage
	 *            The JispPackage to set metadata in.
	 * @param icondefElement
	 *            The icondef JDOM Element to get the data from.
	 */
	private void setMetadata(JispPackage jispPackage, Element icondefElement) {
		JispMetadata jispMetadata = new JispMetadata();
		Element metaElement = icondefElement.getChild("meta");

		jispMetadata.setName(metaElement.getChildText("name"));
		jispMetadata.setVersion(metaElement.getChildText("version"));
		jispMetadata.setDescription(metaElement.getChildText("description"));
		setMetadataAuthors(jispMetadata, metaElement);
		try {
			jispMetadata.setCreation(new Date(ISODateTimeFormat
					.dateParser().parseMillis(
							metaElement.getChildText("creation"))));
		} catch (IllegalArgumentException e) {
			LogFactory.getLog(JispStoredToJispPackage.class).error(
					"Icondef.xml creation date can not be parsed as ISO 8601",
					e);
			jispMetadata.setCreation(new Date());
		}
		if (metaElement.getChildText("home") != null)
			jispMetadata.setHome(metaElement.getChildText("home"));

		jispPackage.setJispMetadata(jispMetadata);
	}

	/**
	 * Sets the authors in a JispMetadata from the meta element in the
	 * icondef.xml file.
	 * 
	 * @param jispMetadata
	 *            The JispMetadata to set authors in.
	 * @param metaElement
	 *            The meta JDOM Element to get the data from.
	 */
	private void setMetadataAuthors(JispMetadata jispMetadata,
			Element metaElement) {
		Iterator authors = metaElement.getChildren("author").iterator();

		while (authors.hasNext()) {
			JispAuthor jispAuthor = new JispAuthor();
			Element authorElement = (Element) authors.next();

			jispAuthor.setName(authorElement.getText());
			if (authorElement.getAttributeValue("jid") != null)
				jispAuthor.setJid(authorElement.getAttributeValue("jid"));

			jispMetadata.addJispAuthor(jispAuthor);
		}
	}

	/**
	 * Sets the icons in a JispPackage from the icondef Element in the
	 * icondef.xml file.
	 * 
	 * @param jispPackage
	 *            The JispPackage to set icons in.
	 * @param icondefElement
	 *            The icondef JDOM Element to get the icons related information
	 *            from.
	 * @throws JispInvalidIcondefException
	 *             If an object in icondef.xml doesn't exists in stored Jisp.
	 */
	private void setIcons(JispPackage jispPackage, Element icondefElement)
			throws JispInvalidIcondefException {
		Iterator icons = icondefElement.getChildren("icon").iterator();

		while (icons.hasNext()) {
			JispIcon jispIcon = new JispIcon();
			Element iconElement = (Element) icons.next();

			setIconTexts(jispIcon, iconElement);
			setIconObjects(jispIcon, iconElement);

			jispPackage.addJispIcon(jispIcon);
		}
	}

	/**
	 * Sets the texts in a JispIcon from an icon Element in the icondef.xml
	 * file.
	 * 
	 * @param jispIcon
	 *            The JispIcon to set texts in.
	 * @param iconElement
	 *            An icon JDOM Element to get the texts from.
	 */
	private void setIconTexts(JispIcon jispIcon, Element iconElement) {
		Iterator texts = iconElement.getChildren("text").iterator();

		while (texts.hasNext()) {
			JispText jispText = new JispText();
			Element textElement = (Element) texts.next();

			jispText.setText(textElement.getText());

			// TODO Implement setLang in JispText
			// jispText.setLang(textElement.getAttributeValue("xml:lang"));

			jispIcon.addJispText(jispText);
		}
	}

	/**
	 * Sets the objects in a JispIcon from an icon Element in the icondef.xml
	 * file.
	 * 
	 * @param jispIcon
	 *            The JispIcon to set objects in.
	 * @param iconElement
	 *            An icon JDOM Element to get the objects from.
	 * @throws JispInvalidIcondefException
	 *             If an object in icondef.xml doesn't exists in stored Jisp.
	 */
	private void setIconObjects(JispIcon jispIcon, Element iconElement)
			throws JispInvalidIcondefException {
		Iterator objects = iconElement.getChildren("object").iterator();

		while (objects.hasNext()) {
			JispObject jispObject = new JispObject();
			Element objectElement = (Element) objects.next();

			jispObject.setName(objectElement.getText());
			jispObject.setMimeType(objectElement.getAttributeValue("mime"));
			byte[] data = jispStoredWrapper.getObject(objectElement.getText());
			if (data == null) {
				throw new JispInvalidIcondefException(
						"The icondef.xml file is not valid: "
								+ objectElement.getText()
								+ " not found in stored Jisp");
			}
			jispObject.setData(data);

			jispIcon.addJispObject(jispObject);
		}
	}
}
