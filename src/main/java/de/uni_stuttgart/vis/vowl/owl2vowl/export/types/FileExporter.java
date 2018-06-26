/*
 * FileWriter.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.export.types;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Exports the passed text into a file.
 */
public class FileExporter implements Exporter {

	private File destinationFile;

	public FileExporter(File destinationFile) {
		this.destinationFile = destinationFile;
	}

	@Override
	public void write(String text) throws IOException {
		while (destinationFile.exists()) {
			destinationFile.delete();
		}
		FileWriter writer = new FileWriter(destinationFile);
		writer.write(text);
		writer.close();
	}

}
