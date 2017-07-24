package de.tudresden.inf.lat.tabula.datatype;

import java.util.Objects;

/**
 * This models the primitive data type Empty.
 *
 */
public class EmptyType implements PrimitiveType {

	public static final String TYPE_NAME = "Empty";

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	@Override
	public boolean isList() {
		return false;
	}

	@Override
	public EmptyValue parse(String str) {
		return new EmptyValue();
	}

	public EmptyValue castInstance(PrimitiveTypeValue value) {
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
			result =  false;
		} else {
			result = (obj instanceof EmptyType);
		}
		return result;
	}

	@Override
	public String toString() {
		return getTypeName();
	}

}
