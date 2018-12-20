package de.uni_stuttgart.vis.vowl.owl2vowl.converter;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.util.Collection;
import java.util.Collections;
import org.semanticweb.owlapi.apibinding.OWLManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class InputStreamConverter extends AbstractConverter {
	protected InputStream mainOntology;
	protected Collection<InputStream> depdencyOntologies;
	private static final Logger logger = LogManager.getLogger(InputStreamConverter.class);
	public boolean ontologyHasMissingImports() {
		return missingImports;
	}
	public void setOntologyHasMissingImports(boolean val) {
		missingImports=val;
	}
	
	public InputStreamConverter(InputStream ontology) {
		this(ontology, Collections.emptyList());
	}

	public InputStreamConverter(InputStream ontology, Collection<InputStream> necessaryExternals) {
		mainOntology = ontology;
		depdencyOntologies = Collections.unmodifiableCollection(necessaryExternals);
	}

	
	@Override
	protected void loadOntology() throws OWLOntologyCreationException {
		logger.info("Converting input streams...");
		this.setOntologyHasMissingImports(false);
		manager = OWLManager.createOWLOntologyManager();

		for (InputStream depdencyOntology : depdencyOntologies) {
			manager.loadOntologyFromOntologyDocument(depdencyOntology);
		}
		String CharEncoding="UTF-8";
		String copiedOntologyContent="";
		try {
			copiedOntologyContent = IOUtils.toString(mainOntology, CharEncoding);
		} catch (IOException e1) {
			e1.printStackTrace();
			
		}
		// need a workaround with copied string since the input stream is flushed on reading ... 
		try {
			this.addLoadingInfo("* Parsing ontology with OWL API ");
			this.setCurrentlyLoadingFlag("* Parsing ontology with OWL API ",true);
			InputStream copyOfInputBuffer = new ByteArrayInputStream(copiedOntologyContent.getBytes(CharEncoding));
			missingListener=new OWLAPI_MissingImportsListener(this);
			manager.addMissingImportListener(missingListener);
			manager.getOntologyConfigurator().setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
			ontology = manager.loadOntologyFromOntologyDocument(copyOfInputBuffer);
			this.addLoadingInfoToParentLine("... done " );
			this.setCurrentlyLoadingFlag(false);
			
			if (this.ontologyHasMissingImports()==true) {
				this.addLoadingInfoToLine("* Parsing ontology with OWL API ","<span style='color:yellow;'>(with warnings)</span>" );
			}
			copyOfInputBuffer=null;
		} catch (Exception e ) {
			this.setCurrentlyLoadingFlag(false);
			this.addLoadingInfoToLine("* Parsing ontology with OWL API ","... <span style='color:red;'>failed</span>" );
			this.addLoadingInfo("\n <span style='color:red;'>Loading process failed:</span> \n"+msgForWebVOWL(e.getMessage()));
		}
		
		loadedOntologyPath = "file upload";
		copiedOntologyContent="";
		String logOntoName;
		if (!ontology.isAnonymous()) {
			logOntoName = ontology.getOntologyID().getOntologyIRI().get().toString();
		} else {
			logOntoName = "Anonymous";
			logger.info("Ontology IRI is anonymous. Use loaded URI/IRI instead.");
		}
		logger.info("Ontologies loaded! Main Ontology: " + logOntoName);
	}
}
