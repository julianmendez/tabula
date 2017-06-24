package de.tudresden.inf.lat.tabula.datatype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

import de.tudresden.inf.lat.util.map.OptMap;
import de.tudresden.inf.lat.util.map.OptMapImpl;

/**
 * Default implementation of a composite type.
 *
 */
public class CompositeTypeImpl implements CompositeType {

	private final List<String> fields = new ArrayList<>();
	private final OptMap<String, String> fieldType = new OptMapImpl<>(new TreeMap<>());

	/**
	 * Constructs a new composite type.
	 */
	public CompositeTypeImpl() {
	}

	/**
	 * Constructs a new composite type using another one.
	 * 
	 * @param otherType
	 *            other type
	 */
	public CompositeTypeImpl(CompositeType otherType) {
		Objects.requireNonNull(otherType);
		otherType.getFields().forEach(field -> declareField(field, otherType.getFieldType(field).get()));
	}

	@Override
	public List<String> getFields() {
		return Collections.unmodifiableList(this.fields);
	}

	@Override
	public Optional<String> getFieldType(String field) {
		Objects.requireNonNull(field);
		return this.fieldType.get(field);
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
		this.fields.forEach(field -> sbuf.append(field + ":" + this.fieldType.get(field).get() + " "));
		return sbuf.toString();
	}

}
