package de.uni_stuttgart.vis.vowl.owl2vowl.model.annotation;

/**
 * @author Eduard
 */
public enum AnnotationIdentifierEnum {
	DEPRECATED("deprecated"),
	DESCRIPTION("description"),
	COMMENT("comment"),
	LABEL("label");

	private final String value;

	AnnotationIdentifierEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return getValue();
	}
}
