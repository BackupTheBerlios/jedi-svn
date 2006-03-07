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

package de.berlios.jedi.common.config;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.LogFactory;

/**
 * Provides a static method to get a Configuration.<br>
 * This class takes care of initializing the Configuration using a
 * "jedi.config.xml" file in the classpath. This file contains the list of
 * configuration files to use.
 * 
 * TODO: add reference to configuration descriptor format
 */
public class ConfigurationFactory {

	/**
	 * The instance of this class.
	 */
	private static ConfigurationFactory instance = new ConfigurationFactory();

	/**
	 * The Apache ConfigurationFactory to get Configuration objects from.
	 */
	private org.apache.commons.configuration.ConfigurationFactory configurationFactory;

	/**
	 * <p>
	 * Creates a new ConfigurationFactory.<br>
	 * Initialites the Apache ConfigurationFactory to use with "jedi.config.xml"
	 * file.
	 * </p>
	 * <p>
	 * Objects from this class can only be created within the class.
	 * </p>
	 */
	private ConfigurationFactory() {
		configurationFactory = new org.apache.commons.configuration.ConfigurationFactory();
		configurationFactory.setConfigurationURL(getClass().getResource(
				"/jedi.cfg.xml"));
	}

	/**
	 * Returns a Configuration instance.<br>
	 * If an exception happens when creating the configuration, an empty
	 * Configuration object is returned and the exception is logged. It
	 * shouldn't happen, however...
	 * 
	 * @return A Configuration instance.
	 */
	public static Configuration getConfiguration() {
		try {
			return instance.getConfigurationFactory().getConfiguration();
		} catch (ConfigurationException e) {
			LogFactory.getLog(ConfigurationFactory.class).error(
					"Configuration can not be retrieved", e);
			return new BaseConfiguration();
		}
	}

	/**
	 * Returns the Apache ConfigurationFactory.
	 * 
	 * @return The Apache ConfigurationFactory.
	 */
	public org.apache.commons.configuration.ConfigurationFactory getConfigurationFactory() {
		return configurationFactory;
	}
}
