package de.tudresden.inf.lat.tabula.table;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * This is the default implementation of a table map.
 *
 */
public class TableMapImpl implements TableMap {

	private final TreeMap<String, Table> map = new TreeMap<String, Table>();

	/**
	 * Returns the identifiers of the stored tables.
	 * 
	 * @return the identifiers of the stored tables
	 */
	public List<String> getTableIds() {
		ArrayList<String> ret = new ArrayList<String>();
		ret.addAll(this.map.keySet());
		return ret;
	}

	/**
	 * Stores a table with the given identifier.
	 * 
	 * @param id
	 *            identifier
	 * @param table
	 *            table
	 */
	public void put(String id, Table table) {
		this.map.put(id, table);
	}

	/**
	 * Returns the table associated to the given identifier.
	 * 
	 * @param id
	 *            identifier
	 * @return the table associated to the given identifier
	 */
	public Table getTable(String id) {
		return this.map.get(id);
	}

	@Override
	public int hashCode() {
		return this.map.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof TableMap) {
			TableMap other = (TableMap) obj;
			boolean ret = getTableIds().equals(other.getTableIds());
			List<String> tableIds = getTableIds();
			for (String tableId : tableIds) {
				ret = ret && getTable(tableId).equals(other.getTable(tableId));
			}
			return ret;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		List<String> tableIds = getTableIds();
		for (String tableId : tableIds) {
			sbuf.append(tableId);
			sbuf.append("=");
			sbuf.append(getTable(tableId));
			sbuf.append("\n");
		}
		return sbuf.toString();
	}

}
