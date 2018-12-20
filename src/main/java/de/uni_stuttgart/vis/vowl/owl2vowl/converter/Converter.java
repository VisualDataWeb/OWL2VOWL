/*
 * Converter.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.converter;

import de.uni_stuttgart.vis.vowl.owl2vowl.export.types.Exporter;

/**
 *
 */
public interface Converter {
	String getLoadingInfoString();
	void setOntologyHasMissingImports(boolean val);
	boolean ontologyHasMissingImports();
	void addLoadingInfo(String msg);
	boolean getCurrentlyLoadingFlag();
	void setCurrentlyLoadingFlag(boolean val);
	void setCurrentlyLoadingFlag(String parentLine,boolean val);
	void addLoadingInfoToParentLine(String msg);
	void clearLoadingMsg();
	void convert();
	void releaseMemory();

	void export(Exporter exporter) throws Exception;
}
