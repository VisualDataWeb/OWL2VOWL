/*
 * BaseIriCollector.java
 *
 */

package de.uni_stuttgart.vis.vowl.owl2vowl.parser.vowl;

import de.uni_stuttgart.vis.vowl.owl2vowl.model.data.VowlData;

/**
 * Collects all base iris from all all available entities in vowl data and stores it directly in it.
 */
public class BaseIriCollector {
	private final VowlData vowlData;

	public BaseIriCollector(VowlData vowlData) {
		this.vowlData = vowlData;
	}

	public void execute() {
		vowlData.getEntityMap().values().stream().forEach(entity -> vowlData.addBaseIri(entity.getBaseIri()));
	}
}
