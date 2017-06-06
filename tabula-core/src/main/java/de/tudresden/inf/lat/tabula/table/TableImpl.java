package de.tudresden.inf.lat.tabula.table;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import de.tudresden.inf.lat.tabula.datatype.CompositeType;
import de.tudresden.inf.lat.tabula.datatype.CompositeTypeImpl;
import de.tudresden.inf.lat.tabula.datatype.CompositeTypeValue;
import de.tudresden.inf.lat.tabula.datatype.Record;

/**
 * This is the default implementation of a sorted table.
 * 
 */
public class TableImpl implements Table {

	private CompositeType tableType = new CompositeTypeImpl();
	private final List<Record> list = new ArrayList<>();
	private final Map<URI, URI> prefixMap = new TreeMap<URI, URI>();
	private final List<String> sortingOrder = new ArrayList<>();
	private final Set<String> fieldsWithReverseOrder = new TreeSet<>();

	public TableImpl() {
	}

	public TableImpl(CompositeType newType) {
		this.tableType = newType;
	}

	public TableImpl(CompositeTypeValue other) {
		this.tableType = other.getType();
		this.list.addAll(other.getRecords());
		if (other instanceof Table) {
			Table otherTable = (Table) other;
			Map<URI, URI> otherMap = otherTable.getPrefixMap();
			otherMap.keySet().forEach(key -> this.prefixMap.put(key, otherMap.get(key)));
			this.sortingOrder.addAll(otherTable.getSortingOrder());
			this.fieldsWithReverseOrder.addAll(otherTable.getFieldsWithReverseOrder());
		}
	}

	@Override
	public CompositeType getType() {
		return this.tableType;
	}

	@Override
	public void setType(CompositeType newType) {
		this.tableType = newType;
	}

	@Override
	public Map<URI, URI> getPrefixMap() {
		return this.prefixMap;
	}

	@Override
	public void setPrefixMap(Map<URI, URI> newPrefixMap) {
		this.prefixMap.clear();
		newPrefixMap.keySet().forEach(key -> this.prefixMap.put(key, newPrefixMap.get(key)));
	}

	@Override
	public boolean add(Record record) {
		if (Objects.isNull(record)) {
			return false;
		} else {
			return this.list.add(record);
		}
	}

	@Override
	public List<String> getSortingOrder() {
		return this.sortingOrder;
	}

	@Override
	public void setSortingOrder(List<String> sortingOrder) {
		this.sortingOrder.clear();
		if (Objects.nonNull(sortingOrder)) {
			this.sortingOrder.addAll(sortingOrder);
		}
	}

	@Override
	public Set<String> getFieldsWithReverseOrder() {
		return this.fieldsWithReverseOrder;
	}

	@Override
	public void setFieldsWithReverseOrder(Set<String> fieldsWithReverseOrder) {
		this.fieldsWithReverseOrder.clear();
		if (Objects.nonNull(fieldsWithReverseOrder)) {
			this.fieldsWithReverseOrder.addAll(fieldsWithReverseOrder);
		}
	}

	@Override
	public List<Record> getRecords() {
		List<Record> ret = new ArrayList<>();
		ret.addAll(this.list);
		Collections.sort(ret, new RecordComparator(this.sortingOrder, this.fieldsWithReverseOrder));
		return ret;
	}

	@Override
	public void clear() {
		this.list.clear();
	}

	@Override
	public int hashCode() {
		return this.tableType.hashCode() + 0x1F * (this.prefixMap.hashCode() + 0x1F * (this.sortingOrder.hashCode()
				+ 0x1F * (this.fieldsWithReverseOrder.hashCode() + 0x1F * this.list.hashCode())));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof Table) {
			Table other = (Table) obj;
			return getType().equals(other.getType()) && getPrefixMap().equals(other.getPrefixMap())
					&& getSortingOrder().equals(other.getSortingOrder())
					&& getFieldsWithReverseOrder().equals(other.getFieldsWithReverseOrder())
					&& getRecords().equals(other.getRecords());
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return this.tableType.toString() + " " + this.prefixMap.toString() + " " + this.sortingOrder.toString() + " "
				+ this.fieldsWithReverseOrder.toString() + " " + this.list.toString();
	}

}
