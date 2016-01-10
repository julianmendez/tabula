package de.tudresden.inf.lat.tabula.datatype;

/**
 * This models a URI.
 *
 */
public class URIType implements PrimitiveType {

	public static final String TYPE_NAME = "URI";

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	@Override
	public boolean isList() {
		return false;
	}

	@Override
	public URIValue parse(String str) {
		return new URIValue(str);
	}

	public URIValue castInstance(PrimitiveTypeValue value) {
		return parse(value.render());
	}

	@Override
	public int hashCode() {
		return getTypeName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else {
			return (obj instanceof URIType);
		}
	}

	@Override
	public String toString() {
		return getTypeName();
	}

}
