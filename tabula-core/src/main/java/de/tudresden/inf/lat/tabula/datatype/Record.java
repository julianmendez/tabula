package de.tudresden.inf.lat.tabula.datatype;

import java.util.List;

/**
 * This models a record.
 *
 */
public interface Record {

	/**
	 * Returns the value of a given property.
	 * 
	 * @param key
	 *            property name
	 * @return the value of a given property
	 */
	PrimitiveTypeValue get(String key);

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
