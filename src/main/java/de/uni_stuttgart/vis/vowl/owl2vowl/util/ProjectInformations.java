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
		Properties properties = new Properties();
		InputStream inputStream = ProjectInformations.class.getResourceAsStream(VERSION_PROPERTIES_FILE);

		try {
			properties.load(inputStream);
		} catch (IOException e) {
			System.err.println(VERSION_PROPERTIES_FILE + " not found.");
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				// Do nothing
			}
		}

		version = properties.getProperty("version", "");
	}

	public static String getVersion() {
		return version;
	}
}
