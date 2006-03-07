package de.berlios.jedi;
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

import de.berlios.jedi.common.CommonTestSuite;
import de.berlios.jedi.data.DataTestSuite;
import de.berlios.jedi.logic.LogicTestSuite;

import junit.framework.*;

/**
 * Test suite containing all the JUnit test.
 */
public class AllTests {

	/**
	 * Returns a suite with all the JUnit tests.
	 * 
	 * @return A suite with all the JUnit tests.
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(CommonTestSuite.suite());
		suite.addTest(DataTestSuite.suite());
		suite.addTest(LogicTestSuite.suite());
		return suite;
	}

	/**
	 * Executes all the tests with JUnit's graphic interface.
	 * 
	 * @param args
	 *            Not used.
	 */
	public static void main(String[] args) {
		junit.swingui.TestRunner.run(AllTests.class);
	}
}
