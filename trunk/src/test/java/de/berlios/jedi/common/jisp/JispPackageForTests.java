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

import org.jdom.Document;

import de.berlios.jedi.common.entity.jisp.JispAuthor;
import de.berlios.jedi.common.entity.jisp.JispIcon;
import de.berlios.jedi.common.entity.jisp.JispMetadata;
import de.berlios.jedi.common.entity.jisp.JispObject;
import de.berlios.jedi.common.entity.jisp.JispPackage;
import de.berlios.jedi.common.entity.jisp.JispText;
import de.berlios.jedi.testUtilities.DiskFilesRetriever;

/**
 * Creates a JispPackage, a JispIcon with a JispObject and a JispText, a
 * JispIcon with two JispObjects and a JispText, a JispIcon with a JispObject
 * and two JispTexts, and a JispIcon with two JispObjects, and two JispTexts.<br>
 * jispObject1_1 and jispObject2_2 have the same name.
 */
public class JispPackageForTests {

	/**
	 * The JispPackage which contains the JispIcons.
	 */
	public JispPackage jispPackage;

	/**
	 * The JispMetadata for the JispPackage.
	 */
	public JispMetadata jispMetadata;

	/**
	 * The JispAuthor for the JispMetadata.
	 */
	public JispAuthor jispAuthor1;

	/**
	 * The first JispIcon.<br>
	 * It has a JispObject and a JispText.
	 */
	public JispIcon jispIcon1;

	/**
	 * The JispText for the first JispIcon.
	 */
	public JispText jispText1_1;

	/**
	 * The JispObject for the first JispIcon.<br>
	 * It has the same name as jispObject2_2.
	 */
	public JispObject jispObject1_1;

	/**
	 * The second JispIcon.<br>
	 * It has a JispText and two JispObjects.
	 */
	public JispIcon jispIcon2;

	/**
	 * The JispText for the second JispObject.
	 */
	public JispText jispText2_1;

	/**
	 * The first JispObject for the second JispIcon.
	 */
	public JispObject jispObject2_1;

	/**
	 * The second JispObject for the second JispIcon.<br>
	 * It has the same name as jispObject1_1;
	 */
	public JispObject jispObject2_2;

	/**
	 * The third JispIcon.<br>
	 * It has two JispTexts and a JispObject.
	 */
	public JispIcon jispIcon3;

	/**
	 * The first JispText for the third JispObject.
	 */
	public JispText jispText3_1;

	/**
	 * The second JispText for the third JispObject.
	 */
	public JispText jispText3_2;

	/**
	 * The JispObject for the third JispIcon.
	 */
	public JispObject jispObject3_1;

	/**
	 * The fourth JispIcon.<br>
	 * It has two JispTexts and two JispObjects.
	 */
	public JispIcon jispIcon4;

	/**
	 * The first JispText for the fourth JispObject.
	 */
	public JispText jispText4_1;

	/**
	 * The second JispText for the fourth JispObject.
	 */
	public JispText jispText4_2;

	/**
	 * The first JispObject for the fourth JispIcon.
	 */
	public JispObject jispObject4_1;

	/**
	 * The second JispObject for the fourth JispIcon.
	 */
	public JispObject jispObject4_2;

	/**
	 * Creates all the objects and adds them to their parents.
	 */
	public JispPackageForTests() {
		jispPackage = new JispPackage();

		jispMetadata = new JispMetadata();
		jispMetadata.setName("test");
		jispMetadata.setVersion("1.0");
		jispMetadata.setDescription("none");
		jispMetadata.setCreation(new Date());
		jispMetadata.setHome("http://jedi.berlios.de");

		jispAuthor1 = new JispAuthor();
		jispAuthor1.setName("Name");
		jispMetadata.addJispAuthor(jispAuthor1);
		jispPackage.setJispMetadata(jispMetadata);

		jispIcon1 = new JispIcon();

		jispObject1_1 = new JispObject();
		jispObject1_1.setName("aDirectory/more 0 array");
		jispObject1_1.setMimeType("none");
		jispObject1_1.setData(new byte[10]);
		jispIcon1.addJispObject(jispObject1_1);

		jispText1_1 = new JispText();
		jispText1_1.setText("nothing");
		jispIcon1.addJispText(jispText1_1);
		jispPackage.addJispIcon(jispIcon1);

		jispIcon2 = new JispIcon();

		jispObject2_1 = new JispObject();
		jispObject2_1.setName("0 array");
		jispObject2_1.setMimeType("none");
		jispObject2_1.setData(new byte[15]);
		jispIcon2.addJispObject(jispObject2_1);

		jispObject2_2 = new JispObject();
		jispObject2_2.setName("aDirectory/more 0 array");
		jispObject2_2.setMimeType("none");
		jispObject2_2.setData(new byte[20]);
		jispIcon2.addJispObject(jispObject2_2);

		jispText2_1 = new JispText();
		jispText2_1.setText("nothing nothing");
		jispIcon2.addJispText(jispText2_1);
		jispPackage.addJispIcon(jispIcon2);

		jispIcon3 = new JispIcon();

		jispObject3_1 = new JispObject();
		jispObject3_1.setName("another 0 array");
		jispObject3_1.setMimeType("none");
		jispObject3_1.setData(new byte[25]);
		jispIcon3.addJispObject(jispObject3_1);

		jispText3_1 = new JispText();
		jispText3_1.setText("nothing nothing nothing");
		jispIcon3.addJispText(jispText3_1);

		jispText3_2 = new JispText();
		jispText3_2.setText("have I said nothing?");
		jispIcon3.addJispText(jispText3_2);
		jispPackage.addJispIcon(jispIcon3);

		jispIcon4 = new JispIcon();

		jispObject4_1 = new JispObject();
		jispObject4_1.setName("living in a 0 array world (8)");
		jispObject4_1.setMimeType("none");
		jispObject4_1.setData(new byte[25]);
		jispIcon4.addJispObject(jispObject4_1);

		jispObject4_2 = new JispObject();
		jispObject4_2
				.setName("anotherDirectory/andAnother/and finally a 0 array");
		jispObject4_2.setMimeType("none");
		jispObject4_2.setData(new byte[30]);
		jispIcon4.addJispObject(jispObject4_2);

		jispText4_1 = new JispText();
		jispText4_1.setText("still nothing");
		jispIcon4.addJispText(jispText4_1);

		jispText4_2 = new JispText();
		jispText4_2.setText("guess what? nothing!");
		jispIcon4.addJispText(jispText4_2);
		jispPackage.addJispIcon(jispIcon4);
	}

	/**
	 * Returns the icondef JDOM Document which describes the JispPackage.<br>
	 * The icondef is retrieved from the filesystem.
	 * 
	 * @return The icondef JDOM Document which describes the JispPackage.
	 */
	public Document getIcondef() {
		return DiskFilesRetriever
				.getDocument("src/test/testFiles/JispPackageForTestsIcondef.xml");
	}
}
