package de.tudresden.inf.lat.tabula.datatype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This models a list of elements with a parameterized type.
 *
 */
public class ParameterizedListValue extends ArrayList<PrimitiveTypeValue> implements PrimitiveTypeValue {

	private static final long serialVersionUID = 6919072594014964617L;

	public static final String Separator = " ";

	private final PrimitiveType parameter;

	public ParameterizedListValue(PrimitiveType parameter0) {
		super();
		if (parameter0 == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		this.parameter = parameter0;
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
				sbuf.append(Separator);
			}
			sbuf.append(str);
		}
		return sbuf.toString();
	}

	@Override
	public List<String> renderAsList() {
		ArrayList<String> ret = new ArrayList<>();
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
