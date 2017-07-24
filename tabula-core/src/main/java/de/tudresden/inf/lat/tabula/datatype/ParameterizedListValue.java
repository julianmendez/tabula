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
	 * @param parameter
	 *            primitive type
	 */
	public ParameterizedListValue(PrimitiveType parameter) {
		super();
		Objects.requireNonNull(parameter);
		this.parameter = parameter;
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
		String result = sbuf.toString();
		return result;
	}

	@Override
	public List<String> renderAsList() {
		List<String> list = new ArrayList<>();
		this.forEach(elem -> list.add(elem.render()));
		List<String> result = Collections.unmodifiableList(list);
		return result;
	}

	@Override
	public int compareTo(PrimitiveTypeValue obj) {
		int result = 0;
		if (obj instanceof ParameterizedListValue) {
			ParameterizedListValue other = (ParameterizedListValue) obj;
			result = size() - other.size();
			if (result == 0) {
				result = toString().compareTo(other.toString());
			}
		} else {
			result = toString().compareTo(obj.toString());
		}
		return result;
	}

	public PrimitiveType getParameter() {
		return this.parameter;
	}

}
