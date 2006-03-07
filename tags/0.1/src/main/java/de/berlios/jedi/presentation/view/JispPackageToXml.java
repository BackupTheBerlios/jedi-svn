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

package de.berlios.jedi.presentation.view;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.jdom.Element;

import de.berlios.jedi.common.entity.jisp.JispAuthor;
import de.berlios.jedi.common.entity.jisp.JispIcon;
import de.berlios.jedi.common.entity.jisp.JispMetadata;
import de.berlios.jedi.common.entity.jisp.JispObject;
import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.entity.jisp.JispPackagesList;
import de.berlios.jedi.common.entity.jisp.JispText;
import de.berlios.jedi.presentation.JispIdManager;

/**
 * <p>
 * Utility class to create the XML representation for a Jisp Element.<br>
 * Jisp Elements are JispPackage, JispIcon, JispObject and JispText. Each
 * element has its own method to get the XML representation as a JDOM element.
 * </p>
 * <p>
 * Every Jisp Element used must be registered in the JispIdManager.
 * </p>
 */
public class JispPackageToXml {

	/**
	 * JispIdManager to get from the Jisp Elements ids.
	 */
	private JispIdManager jispIdManager;

	/**
	 * <p>
	 * Constructs a new JispPackageToXml with a JispIdManager.
	 * </p>
	 * <p>
	 * jispIdManager can't be null.
	 * </p>
	 * 
	 * @param jispIdManager
	 *            The JispIdManager to use.
	 */
	public JispPackageToXml(JispIdManager jispIdManager) {
		this.jispIdManager = jispIdManager;
	}

	/**
	 * <p>
	 * Creates a new JDOM element containing the XML representation of a
	 * JispPackagesList.<br>
	 * All the Jisp Packages in the list must be registered with the
	 * JispIdManager.
	 * </p>
	 * <p>
	 * An example of the XML representation:<br>
	 * &lt;jispPackagesList&gt;<br>
	 * &nbsp;&nbsp;&lt;jispPackage id="0"&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...<br>
	 * &nbsp;&nbsp;&lt;/jispPackage&gt;<br>
	 * &nbsp;&nbsp;...<br>
	 * &lt;/jispPackagesList&gt;
	 * </p>
	 * 
	 * @param jispPackagesList
	 *            The JispPackagesList which XML representation get.
	 * @param withJispMetadata
	 *            True if the JispMetadata of the JispPackages must be
	 *            represented, false otherwise.
	 * @return A JDOM Element containing the XML representation of the
	 *         JispPackagesList.
	 * @see JispPackageToXml#createJispPackageElement(JispPackage, boolean)
	 */
	public Element createJispPackagesListElement(
			JispPackagesList jispPackagesList, boolean withJispMetadata) {
		Element jispPackagesListElement = new Element("jispPackagesList");

		Iterator jispPackages = jispPackagesList.iterator();
		while (jispPackages.hasNext()) {
			JispPackage jispPackage = (JispPackage) jispPackages.next();
			jispPackagesListElement.addContent(createJispPackageElement(
					jispPackage, withJispMetadata));
		}

		return jispPackagesListElement;
	}

	/**
	 * <p>
	 * Creates a new JDOM element containing the XML representation of a
	 * JispPackage.<br>
	 * The Jisp Package must be registered with the JispIdManager.
	 * </p>
	 * <p>
	 * An example of the XML representation:<br>
	 * &lt;jispPackage id="0"&gt;<br>
	 * &nbsp;&nbsp;&lt;jispMetadata&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;...<br>
	 * &nbsp;&nbsp;&lt;/jispMetadata&gt;<br>
	 * &nbsp;&nbsp;&lt;jispIcon id="0-0"&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;...<br>
	 * &nbsp;&nbsp;&lt;/jispIcon&gt;<br>
	 * &nbsp;&nbsp;...<br>
	 * &lt;/jispPackage&gt;
	 * </p>
	 * 
	 * @param jispPackage
	 *            The JispPackage which XML representation get.
	 * @param withJispMetadata
	 *            True if the JispMetadata must be represented, false otherwise.
	 * @return A JDOM Element containing the XML representation of the
	 *         JispPackage.
	 * @see JispPackageToXml#createJispMetadataElement(JispMetadata)
	 * @see JispPackageToXml#createJispIconElement(JispIcon)
	 */
	public Element createJispPackageElement(JispPackage jispPackage,
			boolean withJispMetadata) {
		Element jispPackageElement = new Element("jispPackage");
		jispPackageElement.setAttribute("id", jispIdManager.getId(jispPackage));

		if (withJispMetadata) {
			jispPackageElement.addContent(createJispMetadataElement(jispPackage
					.getJispMetadata()));
		}

		Iterator jispIcons = jispPackage.jispIconsIterator();
		while (jispIcons.hasNext()) {
			jispPackageElement
					.addContent(createJispIconElement((JispIcon) jispIcons
							.next()));
		}

		return jispPackageElement;
	}

	/**
	 * <p>
	 * Creates a new JDOM element containing the XML representation of a
	 * JispMetadata.<br>
	 * </p>
	 * <p>
	 * An example of the XML representation:<br>
	 * &lt;jispMetadata&gt;<br>
	 * &nbsp;&nbsp;&lt;name&gt;theName&lt;/name&gt;<br>
	 * &nbsp;&nbsp;&lt;version&gt;theVersion&lt;/version&gt;<br>
	 * &nbsp;&nbsp;&lt;description&gt;theDescription&lt;/description&gt;<br>
	 * &nbsp;&nbsp;&lt;jispAuthor&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;...<br>
	 * &nbsp;&nbsp;&lt;/jispAuthor&gt;<br>
	 * &nbsp;&nbsp;...<br>
	 * &nbsp;&nbsp;&lt;creation&gt;theCreationDate&lt;/creation&gt;<br>
	 * &nbsp;&nbsp;&lt;home&gt;theHome&lt;/home&gt;<br>
	 * &lt;/jispMetadata&gt;
	 * </p>
	 * 
	 * @param jispMetadata
	 *            The JispMetadata which XML representation get.
	 * @return A JDOM Element containing the XML representation of the
	 *         JispMetadata.
	 * @see JispPackageToXml#createJispAuthorElement(JispAuthor)
	 */
	public Element createJispMetadataElement(JispMetadata jispMetadata) {
		Element jispMetadataElement = new Element("jispMetadata");

		Element name = new Element("name");
		name.setText(jispMetadata.getName());

		jispMetadataElement.addContent(name);

		Element version = new Element("version");
		version.setText(jispMetadata.getVersion());

		jispMetadataElement.addContent(version);

		Element description = new Element("description");
		description.setText(jispMetadata.getDescription());

		jispMetadataElement.addContent(description);

		Iterator jispAuthors = jispMetadata.jispAuthorsIterator();
		while (jispAuthors.hasNext()) {
			jispMetadataElement
					.addContent(createJispAuthorElement((JispAuthor) jispAuthors
							.next()));
		}

		Element creation = new Element("creation");
		creation.setText(new SimpleDateFormat("yyyy-MM-dd").format(jispMetadata
				.getCreation()));

		jispMetadataElement.addContent(creation);

		Element home = new Element("home");
		home.setText(jispMetadata.getHome());

		jispMetadataElement.addContent(home);

		return jispMetadataElement;
	}

	/**
	 * <p>
	 * Creates a new JDOM element containing the XML representation of a
	 * JispAuthor.<br>
	 * </p>
	 * <p>
	 * An example of the XML representation:<br>
	 * &lt;jispAuthor&gt;<br>
	 * &nbsp;&nbsp;&lt;name&gt;theName&lt;/name&gt;<br>
	 * &nbsp;&nbsp;&lt;jid&gt;theJID&lt;/jid&gt;<br>
	 * &nbsp;&nbsp;&lt;email&gt;theEmail&lt;/email&gt;<br>
	 * &nbsp;&nbsp;&lt;www&gt;theWebpage&lt;/www&gt;<br>
	 * &lt;/jispAuthor&gt;
	 * </p>
	 * 
	 * @param jispAuthor
	 *            The JispAuthor which XML representation get.
	 * @return A JDOM Element containing the XML representation of the
	 *         JispAuthor.
	 */
	public Element createJispAuthorElement(JispAuthor jispAuthor) {
		Element jispAuthorElement = new Element("jispAuthor");

		Element name = new Element("name");
		name.setText(jispAuthor.getName());

		jispAuthorElement.addContent(name);

		Element jid = new Element("jid");
		jid.setText(jispAuthor.getJid());

		jispAuthorElement.addContent(jid);

		Element email = new Element("email");
		email.setText(jispAuthor.getEmail());

		jispAuthorElement.addContent(email);

		Element www = new Element("www");
		www.setText(jispAuthor.getWww());

		jispAuthorElement.addContent(www);

		return jispAuthorElement;
	}

	/**
	 * <p>
	 * Creates a new JDOM element containing the XML representation of a
	 * JispIcon.<br>
	 * The Jisp Icon must be registered with the JispIdManager.
	 * </p>
	 * <p>
	 * An example of the XML representation:<br>
	 * &lt;jispIcon id="0-0"&gt;<br>
	 * &nbsp;&nbsp;&lt;jispObject id="0-0-0o"&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;...<br>
	 * &nbsp;&nbsp;&lt;/jispObject&gt;<br>
	 * &nbsp;&nbsp;...<br>
	 * &nbsp;&nbsp;&lt;jispText id="0-0-0t"&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;...<br>
	 * &nbsp;&nbsp;&lt;/jispText&gt;<br>
	 * &nbsp;&nbsp;...<br>
	 * &lt;/jispIcon&gt;
	 * </p>
	 * 
	 * @param jispIcon
	 *            The JispIcon which XML representation get.
	 * @return A JDOM Element containing the XML representation of the JispIcon.
	 * @see JispPackageToXml#createJispObjectElement(JispObject)
	 * @see JispPackageToXml#createJispTextElement(JispText)
	 */
	public Element createJispIconElement(JispIcon jispIcon) {
		Element jispIconElement = new Element("jispIcon");
		jispIconElement.setAttribute("id", jispIdManager.getId(jispIcon));

		Iterator jispObjects = jispIcon.jispObjectsIterator();
		while (jispObjects.hasNext()) {
			jispIconElement
					.addContent(createJispObjectElement((JispObject) jispObjects
							.next()));
		}

		Iterator jispTexts = jispIcon.jispTextsIterator();
		while (jispTexts.hasNext()) {
			jispIconElement
					.addContent(createJispTextElement((JispText) jispTexts
							.next()));
		}

		return jispIconElement;
	}

	/**
	 * <p>
	 * Creates a new JDOM element containing the XML representation of a
	 * JispObject.<br>
	 * The Jisp Object must be registered with the JispIdManager.
	 * </p>
	 * <p>
	 * An example of the XML representation:<br>
	 * &lt;jispObject id="0-0-0o"&gt;<br>
	 * &nbsp;&nbsp;&lt;contentType&gt;image/png&lt;contentType&gt;<br>
	 * &nbsp;&nbsp;&lt;src&gt;GetJispObject.do?jispObjectId=0-0-0o&lt;/src&gt;<br>
	 * &lt;/jispObject&gt;
	 * </p>
	 * 
	 * @param jispObject
	 *            The JispObject which XML representation get.
	 * @return A JDOM Element containing the XML representation of the
	 *         JispObject.
	 */
	public Element createJispObjectElement(JispObject jispObject) {
		Element jispObjectElement = new Element("jispObject");
		String jispObjectId = jispIdManager.getId(jispObject);
		jispObjectElement.setAttribute("id", jispObjectId);

		Element contentType = new Element("contentType");
		contentType.setText(jispObject.getMimeType());

		jispObjectElement.addContent(contentType);

		Element src = new Element("src");
		src.setText("GetJispObject.do?jispObjectId=" + jispObjectId);

		jispObjectElement.addContent(src);

		return jispObjectElement;
	}

	/**
	 * <p>
	 * Creates a new JDOM element containing the XML representation of a
	 * JispText.<br>
	 * The Jisp Text must be registered with the JispIdManager.
	 * </p>
	 * <p>
	 * An example of the XML representation:<br>
	 * &lt;jispText id="0-0-0t"&gt;<br>
	 * &nbsp;&nbsp;&lt;text&gt;:)&lt;/text&gt;<br>
	 * &lt;/jispText&gt;
	 * </p>
	 * 
	 * @param jispText
	 *            The JispText which XML representation get.
	 * @return A JDOM Element containing the XML representation of the JispText.
	 */
	public Element createJispTextElement(JispText jispText) {
		Element jispTextElement = new Element("jispText");
		jispTextElement.setAttribute("id", jispIdManager.getId(jispText));

		Element text = new Element("text");
		text.setText(jispText.getText());

		jispTextElement.addContent(text);

		return jispTextElement;
	}
}
