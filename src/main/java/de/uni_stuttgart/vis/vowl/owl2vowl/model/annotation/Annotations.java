package de.uni_stuttgart.vis.vowl.owl2vowl.model.annotation;

import java.util.*;

/**
 * @author Eduard
 */
public class Annotations {
	private List<Annotation> labels = new ArrayList<>();
	private List<Annotation> comments = new ArrayList<>();
	private List<Annotation> description = new ArrayList<>();
	private boolean deprecated = false;
	private Map<String, List<Annotation>> identifierToAnnotation = new HashMap<>();

	public List<Annotation> getComments() {
		return Collections.unmodifiableList(comments);
	}

	public List<Annotation> getDescription() {
		return Collections.unmodifiableList(description);
	}

	public boolean isDeprecated() {
		return deprecated;
	}

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

	public void fillAnnotations(Set<Annotation> annotations) {
		for (Annotation annotation : annotations) {
			if (annotation.getIdentifier().equals(AnnotationIdentifierEnum.DEPRECATED.getValue())) {
				deprecated = true;
				continue;
			}

			if (annotation.getIdentifier().equals(AnnotationIdentifierEnum.LABEL.getValue())) {
				labels.add(annotation);
				continue;
			}

			if (annotation.getIdentifier().equals(AnnotationIdentifierEnum.DESCRIPTION.getValue())) {
				description.add(annotation);
				continue;
			}

			if (annotation.getIdentifier().equals(AnnotationIdentifierEnum.COMMENT.getValue())) {
				comments.add(annotation);
				continue;
			}

			if (!identifierToAnnotation.containsKey(annotation.getIdentifier())) {
				identifierToAnnotation.put(annotation.getIdentifier(), new ArrayList<>());
			}

			identifierToAnnotation.get(annotation.getIdentifier()).add(annotation);
		}
	}

	@Override
	public String toString() {
		return "Annotations{" +
				"labels=" + labels +
				", description=" + description +
				", deprecated=" + deprecated +
				", identifierToAnnotation=" + identifierToAnnotation +
				'}';
	}
}
