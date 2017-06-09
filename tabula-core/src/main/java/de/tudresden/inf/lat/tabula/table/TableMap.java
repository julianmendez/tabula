package de.tudresden.inf.lat.tabula.table;

import java.util.List;
import java.util.Optional;

/**
 * This models a collection of tables with identifiers.
 *
 */
public interface TableMap {

	/**
	 * Returns the identifiers of the stored tables.
	 * 
	 * @return the identifiers of the stored tables
	 */
	List<String> getTableIds();

	/**
	 * Stores a table with the given identifier.
	 * 
	 * @param id
	 *            identifier
	 * @param table
	 *            table
	 */
	void put(String id, Table table);

	/**
	 * Returns the table associated to the given identifier.
	 * 
	 * @param id
	 *            identifier
	 * @return the table associated to the given identifier
	 */
	Optional<Table> getTable(String id);

}
