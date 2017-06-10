package de.tudresden.inf.lat.tabula.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import de.tudresden.inf.lat.tabula.common.OptMap;
import de.tudresden.inf.lat.tabula.common.OptMapImpl;

/**
 * This is the default implementation of a table map.
 *
 */
public class TableMapImpl implements TableMap {

	private final OptMap<String, Table> map = new OptMapImpl<>(new TreeMap<>());

	/**
	 * Constructs a new table map.
	 */
	public TableMapImpl() {
	}

	/**
	 * Constructs a new table map using another one.
	 * 
	 * @param otherTableMap
	 *            other table map
	 */
	public TableMapImpl(TableMap otherTableMap) {
		otherTableMap.getTableIds().forEach(tableId -> put(tableId, otherTableMap.getTable(tableId).get()));
	}

	@Override
	public List<String> getTableIds() {
		List<String> ret = new ArrayList<>();
		ret.addAll(this.map.keySet());
		return ret;
	}

	@Override
	public Optional<Table> put(String id, Table table) {
		return this.map.put(id, table);
	}

	@Override
	public Optional<Table> getTable(String id) {
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
			ret = ret && tableIds.stream().allMatch(tableId -> getTable(tableId).equals(other.getTable(tableId)));
			return ret;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		List<String> tableIds = getTableIds();
		tableIds.forEach(tableId -> {
			sbuf.append(tableId);
			sbuf.append("=");
			sbuf.append(getTable(tableId));
			sbuf.append("\n");
		});
		return sbuf.toString();
	}

}
