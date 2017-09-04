package de.uni_stuttgart.vis.vowl.owl2vowl.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 */
public class ProjectInformations {

	private static final String VERSION_PROPERTIES_FILE = "/version.properties";
	private static String version;

	static {

		try (InputStream inputStream =
						 ProjectInformations.class.getResourceAsStream(VERSION_PROPERTIES_FILE)) {
			Properties properties = new Properties();
			properties.load(inputStream);
			version = properties.getProperty("version", "");
		} catch (IOException e) {
			System.err.println(VERSION_PROPERTIES_FILE + " not found.");
		}

	}

	public static String getVersion() {
		return version;
	}
}
