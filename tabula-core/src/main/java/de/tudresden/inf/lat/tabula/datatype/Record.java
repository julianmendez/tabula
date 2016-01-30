package de.tudresden.inf.lat.tabula.datatype;

import java.util.List;
import java.util.Optional;

/**
 * This models a record.
 *
 */
public interface Record {

	/**
	 * Returns an optional containing the value of a given property, if this
	 * value is present, or an empty optional otherwise.
	 * 
	 * @param key
	 *            property name
	 * @return an optional containing the value of a given property, if this
	 *         value is present, or an empty optional otherwise
	 */
	Optional<PrimitiveTypeValue> get(String key);

	/**
	 * Sets the value of a given property.
	 * 
	 * @param key
	 *            property name
	 * @param value
	 *            value
	 */
	void set(String key, PrimitiveTypeValue value);

	/**
	 * Returns the property names.
	 * 
	 * @return the property names
	 */
	List<String> getProperties();

}
