package de.tudresden.inf.lat.tabula.table;

import java.util.List;
import java.util.Set;

import de.tudresden.inf.lat.tabula.datatype.CompositeTypeValue;

/**
 * This models a sorted table.
 *
 */
public interface Table extends CompositeTypeValue {

	/**
	 * Returns the sorting order of the fields.
	 * 
	 * @return the sorting order of the fields
	 */
	List<String> getSortingOrder();

	/**
	 * Returns the fields that are supposed to be sorted in reverse order.
	 * 
	 * @return the fields that are supposed to be sorted in reverse order
	 */
	Set<String> getFieldsWithReverseOrder();

	/**
	 * Returns the identifiers of all added records.
	 * 
	 * @return the identifiers of all added records
	 */
	Set<String> getIdentifiers();

}
