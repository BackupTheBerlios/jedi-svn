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

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite containing all the JUnit test for the package and subpackages.
 */
public class CommonJispTestSuite {

	/**
	 * Returns a suite, with all the JUnit tests for this package, to be
	 * executed or added to a bigger suite.
	 * 
	 * @return A suite of JUnit test.
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite("de.berlios.jedi.common.jisp");
		suite.addTest(new TestSuite(JispPackageToIcondefXmlTest.class));
		suite.addTest(new TestSuite(JispStoredWrapperTest.class));
		suite.addTest(new TestSuite(JispStoredToJispPackageTest.class));
		suite.addTest(new TestSuite(JispPackageStorerTest.class));
		suite.addTest(new TestSuite(JispUtilTest.class));
		return suite;
	}

	/**
	 * Executes all the test for this package with JUnit's graphic interface.
	 * 
	 * @param args
	 *            Not used.
	 */
	public static void main(String[] args) {
		junit.swingui.TestRunner.run(CommonJispTestSuite.class);
	}
}
