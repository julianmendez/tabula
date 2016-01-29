package de.tudresden.inf.lat.tabula.datatype;

import java.util.List;
import java.util.Optional;

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
	 * Returns an optional containing the type of the given field, if the field
	 * is present, or an empty optional otherwise.
	 * 
	 * @param field
	 *            field
	 * @return an optional containing the type of the given field, if the field
	 *         is present, or an empty optional otherwise
	 */
	Optional<String> getFieldType(String field);

}
