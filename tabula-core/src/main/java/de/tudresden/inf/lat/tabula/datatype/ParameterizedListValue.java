package de.tudresden.inf.lat.tabula.datatype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This models a list of elements with a parameterized type.
 *
 */
public class ParameterizedListValue extends ArrayList<PrimitiveTypeValue> implements PrimitiveTypeValue {

	private static final long serialVersionUID = 6919072594014964617L;

	public static final String SEPARATOR = " ";

	private final PrimitiveType parameter;

	/**
	 * Constructs a new parameterized list value.
	 * 
	 * @param parameter0
	 *            primitive type
	 */
	public ParameterizedListValue(PrimitiveType parameter0) {
		super();
		Objects.requireNonNull(parameter0);
		this.parameter = parameter0;
	}

	/**
	 * Constructs a new parameterized list value using another parameterized
	 * list value.
	 * 
	 * @param other
	 *            parameterized list value
	 */
	public ParameterizedListValue(ParameterizedListValue other) {
		super();
		Objects.requireNonNull(other);
		this.parameter = other.getParameter();
	}

	@Override
	public PrimitiveType getType() {
		return new ParameterizedListType(this.parameter);
	}

	public void add(String str) {
		super.add(this.parameter.parse(str));
	}

	@Override
	public String render() {
		StringBuffer sbuf = new StringBuffer();
		List<String> list = renderAsList();
		boolean first = true;
		for (String str : list) {
			if (first) {
				first = false;
			} else {
				sbuf.append(SEPARATOR);
			}
			sbuf.append(str);
		}
		return sbuf.toString();
	}

	@Override
	public List<String> renderAsList() {
		List<String> ret = new ArrayList<>();
		this.forEach(elem -> ret.add(elem.render()));
		return Collections.unmodifiableList(ret);
	}

	@Override
	public int compareTo(PrimitiveTypeValue obj) {
		if (obj instanceof ParameterizedListValue) {
			ParameterizedListValue other = (ParameterizedListValue) obj;
			int ret = size() - other.size();
			if (ret == 0) {
				ret = toString().compareTo(other.toString());
			}
			return ret;
		} else {
			return toString().compareTo(obj.toString());
		}
	}

	public PrimitiveType getParameter() {
		return this.parameter;
	}

}
