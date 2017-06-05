package de.tudresden.inf.lat.tabula.table;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.tudresden.inf.lat.tabula.datatype.CompositeTypeValue;

/**
 * This models a sorted table with a map of URI prefixes.
 *
 */
public interface Table extends CompositeTypeValue {

	/**
	 * Returns the map of URI prefixes.
	 *
	 * @return the map of URI prefixes
	 */
	Map<URI, URI> getPrefixMap();

	/**
	 * Sets the map of URI prefixes
	 *
	 * @param prefixMap
	 *            map of URI prefixes
	 */
	void setPrefixMap(Map<URI, URI> prefixMap);

	/**
	 * Returns the sorting order for the fields.
	 * 
	 * @return the sorting order for the fields
	 */
	List<String> getSortingOrder();

	/**
	 * Sets the sorting order for the fields.
	 * 
	 * @param sortingOrder
	 *            sorting order
	 */
	void setSortingOrder(List<String> sortingOrder);

	/**
	 * Returns the fields that are supposed to be sorted in reverse order.
	 * 
	 * @return the fields that are supposed to be sorted in reverse order
	 */
	Set<String> getFieldsWithReverseOrder();

	/**
	 * Sets the fields that are supposed to be sorted in reverse order.
	 * 
	 * @param fieldsWithReverseOrder
	 *            fields with reverse order
	 *
	 */
	void setFieldsWithReverseOrder(Set<String> fieldsWithReverseOrder);

}
