/*
 * FileWriter.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.export.types;

import org.apache.commons.io.FilenameUtils;

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
	public Object write(String text) throws IOException {
		int i = 0;

		while (destinationFile.exists()) {
			String path = destinationFile.getPath();
			File newFile = new File(FilenameUtils.getFullPath(path) + FilenameUtils.getBaseName(path) + i + "." + FilenameUtils.getExtension(destinationFile.getName()));
			destinationFile.renameTo(newFile);
			i++;
		}

		FileWriter writer = new FileWriter(destinationFile);
		writer.write(text);
		writer.close();

		return true;
	}

}
