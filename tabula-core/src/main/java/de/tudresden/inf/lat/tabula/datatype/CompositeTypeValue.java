package de.tudresden.inf.lat.tabula.datatype;

import java.util.List;

/**
 * This models a composite type value.
 *
 */
public interface CompositeTypeValue {

	/**
	 * Returns the type of this composite type value.
	 * 
	 * @return the type of this composite type value
	 */
	CompositeType getType();

	/**
	 * Sets the type of this composite type value.
	 * 
	 * @param type
	 *            type
	 */
	void setType(CompositeType type);

	/**
	 * Returns all the records.
	 * 
	 * @return all the records
	 */
	List<Record> getRecords();

	/**
	 * Adds a record. Returns <code>true</code> if and only if this composite
	 * type value changed as a result of the call.
	 * 
	 * @param record
	 *            record
	 * @return <code>true</code> if and only if this composite type value
	 *         changed as a result of the call
	 */
	boolean add(Record record);

}
