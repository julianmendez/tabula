package de.tudresden.inf.lat.tabula.datatype;

import java.util.List;

/**
 * This models a simplified composite type where the fields have the same type.
 *
 */
public class SimplifiedCompositeType implements CompositeType {

	public static final String DefaultFieldType = "String";

	private CompositeTypeImpl dataType = new CompositeTypeImpl();

	public SimplifiedCompositeType(String[] knownFields) {
		for (String field : knownFields) {
			this.dataType.declareField(field, DefaultFieldType);
		}
	}

	@Override
	public List<String> getFields() {
		return this.dataType.getFields();
	}

	@Override
	public String getFieldType(String field) {
		return this.dataType.getFieldType(field);
	}

	@Override
	public int hashCode() {
		return this.dataType.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof SimplifiedCompositeType) {
			SimplifiedCompositeType other = (SimplifiedCompositeType) obj;
			return this.dataType.equals(other.dataType);
		}
		return false;
	}

	@Override
	public String toString() {
		return this.dataType.toString();
	}

}
