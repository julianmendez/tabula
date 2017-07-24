package de.tudresden.inf.lat.tabula.datatype;

import java.util.Collections;
import java.util.List;

/**
 * This models an empty value.
 * 
 */
public class EmptyValue implements PrimitiveTypeValue {

	private static final String EMPTY = "";

	/**
	 * Constructs a new empty value.
	 */
	public EmptyValue() {
	}

	@Override
	public PrimitiveType getType() {
		return new EmptyType();
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public String render() {
		return EMPTY;
	}

	@Override
	public List<String> renderAsList() {
		return Collections.emptyList();
	}

	@Override
	public int compareTo(PrimitiveTypeValue other) {
		return toString().compareTo(other.toString());
	}

	@Override
	public int hashCode() {
		return EMPTY.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (this == obj) {
			result = true;
		} else if (obj instanceof EmptyValue) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	@Override
	public String toString() {
		return EMPTY;
	}

}
