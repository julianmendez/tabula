package de.tudresden.inf.lat.tabula.datatype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This models a string value.
 * 
 */
public class StringValue implements PrimitiveTypeValue {

	private String str = "";

	/**
	 * Constructs a new empty string value.
	 */
	public StringValue() {
	}

	/**
	 * Constructs a new string value using a string.
	 * 
	 * @param str
	 *            string
	 */
	public StringValue(String str) {
		this.str = Objects.isNull(str) ? "" : str;
	}

	/**
	 * Constructs a new string value using another string value.
	 * 
	 * @param other
	 *            a string value
	 */
	public StringValue(StringValue other) {
		this.str = other.str;
	}

	@Override
	public PrimitiveType getType() {
		return new StringType();
	}

	@Override
	public boolean isEmpty() {
		return str.trim().isEmpty();
	}

	@Override
	public String render() {
		return str;
	}

	@Override
	public List<String> renderAsList() {
		List<String> list = new ArrayList<>();
		list.add(render());
		List<String> result = Collections.unmodifiableList(list);
		return result;
	}

	@Override
	public int compareTo(PrimitiveTypeValue other) {
		return toString().compareTo(other.toString());
	}

	@Override
	public int hashCode() {
		return this.str.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (this == obj) {
			result = true;
		} else if (obj instanceof StringValue) {
			StringValue other = (StringValue) obj;
			result = this.str.equals(other.str);
		} else {
			result = false;
		}
		return result;
	}

	@Override
	public String toString() {
		return this.str;
	}

}
