package de.tudresden.inf.lat.tabula.datatype;

import java.util.Objects;

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
		Objects.requireNonNull(value);
		return parse(value.render());
	}

	@Override
	public int hashCode() {
		return getTypeName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (this == obj) {
			result = true;
		} else if (Objects.isNull(obj)) {
			result = false;
		} else {
			result = (obj instanceof URIType);
		}
		return result;
	}

	@Override
	public String toString() {
		return getTypeName();
	}

}
