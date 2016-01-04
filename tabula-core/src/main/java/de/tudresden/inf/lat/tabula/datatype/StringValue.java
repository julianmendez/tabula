package de.tudresden.inf.lat.tabula.datatype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringValue implements PrimitiveTypeValue {

	private String str = "";

	public StringValue() {
	}

	public StringValue(String str0) {
		this.str = (str0 == null) ? "" : str0.trim();
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
		ArrayList<String> ret = new ArrayList<>();
		ret.add(render());
		return Collections.unmodifiableList(ret);
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
		if (this == obj) {
			return true;
		} else if (obj instanceof StringValue) {
			StringValue other = (StringValue) obj;
			return this.str.equals(other.str);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return this.str;
	}
}
