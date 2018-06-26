package de.uni_stuttgart.vis.vowl.owl2vowl.converter;

import java.util.HashMap;
import java.util.Map;
import org.semanticweb.owlapi.model.MissingImportEvent;
import org.semanticweb.owlapi.model.MissingImportListener;
@SuppressWarnings("serial")
public class OWLAPI_MissingImportsListener implements MissingImportListener {
	Map<String, Boolean> missingImportsMap;
	AbstractConverter currentConverter;
	public OWLAPI_MissingImportsListener(AbstractConverter c) {
		 currentConverter=c;
		 missingImportsMap=new HashMap<String, Boolean>();
	};

	@Override
	public void importMissing(MissingImportEvent event) {
		String missingName=event.getImportedOntologyURI().toString();
		currentConverter.addLoadingInfo("\n >> <span style='color:yellow;'>Failed to import ontology :</span> <a target=\"_blank\" href="+missingName+">"+missingName+"</a>");
		missingImportsMap.put(event.getImportedOntologyURI().toString(),false);
		currentConverter.setOntologyHasMissingImports(true);
	}
	
	public Map<String, Boolean> getMissingImportsMap(){
		return missingImportsMap;
	}
}

