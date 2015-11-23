package de.tudresden.inf.lat.tabula.datatype;

import java.util.List;

/**
 * This models a composite type value.
 *
 */
public interface CompositeTypeValue {

	/**
	 * Returns the type of this table.
	 * 
	 * @return the type of this table
	 */
	CompositeType getType();

	/**
	 * Returns all the records.
	 * 
	 * @return all the records
	 */
	List<Record> getRecords();

}
