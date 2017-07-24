package de.tudresden.inf.lat.tabula.datatype;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * This models a simplified composite type where the fields have the same type.
 *
 */
public class SimplifiedCompositeType implements CompositeType {

	public static final String DEFAULT_FIELD_TYPE = "String";

	private CompositeTypeImpl dataType = new CompositeTypeImpl();

	public SimplifiedCompositeType(String[] knownFields) {
		Arrays.stream(knownFields).forEach(field -> this.dataType.declareField(field, DEFAULT_FIELD_TYPE));
	}

	@Override
	public List<String> getFields() {
		return this.dataType.getFields();
	}

	@Override
	public Optional<String> getFieldType(String field) {
		return this.dataType.getFieldType(field);
	}

	@Override
	public int hashCode() {
		return this.dataType.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (this == obj) {
			result = true;
		} else if (Objects.isNull(obj)) {
			result = false;
		} else if (obj instanceof SimplifiedCompositeType) {
			SimplifiedCompositeType other = (SimplifiedCompositeType) obj;
			result = this.dataType.equals(other.dataType);
		}
		return result;
	}

	@Override
	public String toString() {
		return this.dataType.toString();
	}

}
