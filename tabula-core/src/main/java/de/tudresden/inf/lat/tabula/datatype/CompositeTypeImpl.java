package de.tudresden.inf.lat.tabula.datatype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * Default implementation of a composite type.
 *
 */
public class CompositeTypeImpl implements CompositeType {

	private final ArrayList<String> fields = new ArrayList<String>();
	private final TreeMap<String, String> fieldType = new TreeMap<String, String>();

	/**
	 * Constructs a new default implementation of type.
	 */
	public CompositeTypeImpl() {
	}

	@Override
	public List<String> getFields() {
		return Collections.unmodifiableList(this.fields);
	}

	@Override
	public String getFieldType(String field) {
		if (field == null) {
			return null;
		} else {
			return this.fieldType.get(field);
		}
	}

	/**
	 * Declares a field.
	 * 
	 * @param field
	 *            field name
	 * @param typeStr
	 *            type of the field
	 */
	public void declareField(String field, String typeStr) {
		if (this.fields.contains(field)) {
			throw new ParseException("Field '" + field + "' has been already defined.");
		} else {
			this.fields.add(field);
			this.fieldType.put(field, typeStr);
		}
	}

	@Override
	public int hashCode() {
		return this.fields.hashCode() + (0x1F * this.fieldType.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof CompositeType) {
			CompositeType other = (CompositeType) obj;
			boolean ret = getFields().equals(other.getFields());
			if (ret) {
				List<String> fields = getFields();
				ret = ret && fields.stream().allMatch(field -> getFieldType(field).equals(other.getFieldType(field)));
			}
			return ret;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		for (String field : this.fields) {
			sbuf.append(field + ":" + this.fieldType.get(field) + " ");
		}
		return sbuf.toString();
	}

}
