package de.tudresden.inf.lat.tabula.datatype;

import java.util.Objects;
import java.util.StringTokenizer;

/**
 * This models the type of a list of elements with a parameterized type.
 *
 */
public class ParameterizedListType implements PrimitiveType {

	public static final String TYPE_PREFIX = "List_";

	private final PrimitiveType parameter;

	public ParameterizedListType(PrimitiveType parameter) {
		Objects.requireNonNull(parameter);
		this.parameter = parameter;
	}

	@Override
	public String getTypeName() {
		return TYPE_PREFIX + this.parameter.getTypeName();
	}

	@Override
	public boolean isList() {
		return true;
	}

	@Override
	public ParameterizedListValue parse(String str) {
		ParameterizedListValue ret = new ParameterizedListValue(this.parameter);
		StringTokenizer stok = new StringTokenizer(str);
		while (stok.hasMoreTokens()) {
			ret.add(this.parameter.parse(stok.nextToken()));
		}
		return ret;
	}

	public PrimitiveType getParameter() {
		return this.parameter;
	}

	public ParameterizedListValue castInstance(PrimitiveTypeValue value) {
		return parse(value.render());
	}

	@Override
	public int hashCode() {
		return this.parameter.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (Objects.isNull(obj)) {
			return false;
		} else if (obj instanceof ParameterizedListType) {
			ParameterizedListType other = (ParameterizedListType) obj;
			return this.parameter.equals(other.parameter);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return getTypeName();
	}

}
