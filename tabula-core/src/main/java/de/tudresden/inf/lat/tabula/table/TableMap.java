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
	 * @return an optional containing the previous value associated to the given
	 *         key, or an empty optional if there was no association before
	 */
	Optional<Table> put(String id, Table table);

	/**
	 * Returns an optional containing the value associated to the given key, or
	 * an empty optional if there is no association.
	 * 
	 * @param id
	 *            identifier
	 * @return an optional containing the value associated to the given key, or
	 *         an empty optional if there is no association
	 */
	Optional<Table> getTable(String id);

}
