package de.uni_stuttgart.vis.vowl.owl2vowl.model.annotation;

import java.util.*;

/**
 * @author Eduard
 */
public class Annotations {
	private List<Annotation> labels = new ArrayList<>();
	private Map<String, List<Annotation>> identifierToAnnotation = new HashMap<>();

	public List<Annotation> getLabels() {
		return Collections.unmodifiableList(labels);
	}

	public void addLabel(Annotation annotation) {
		labels.add(annotation);
	}

	public Map<String, List<Annotation>> getIdentifierToAnnotation() {
		return Collections.unmodifiableMap(identifierToAnnotation);
	}

	public void addAnnotation(Annotation annotation) {
		String identifier = annotation.getIdentifier();

		if (!identifierToAnnotation.containsKey(identifier)) {
			identifierToAnnotation.put(identifier, new ArrayList<>());
		}

		identifierToAnnotation.get(identifier).add(annotation);
	}
}
