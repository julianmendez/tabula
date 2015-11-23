package de.tudresden.inf.lat.tabula.datatype;

import java.util.List;

/**
 * This models a composite type.
 *
 */
public interface CompositeType extends DataType {

	/**
	 * Returns all the fields.
	 * 
	 * @return all the fields
	 */
	List<String> getFields();

	/**
	 * Returns the type of the given field.
	 * 
	 * @param field
	 *            field
	 * @return the type of the given field
	 */
	String getFieldType(String field);

}
