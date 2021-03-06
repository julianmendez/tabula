package de.tudresden.inf.lat.tabula.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
	private final PrefixMap prefixMap = new PrefixMapImpl();
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
			PrefixMap otherMap = otherTable.getPrefixMap();
			otherMap.getKeysAsStream().forEach(key -> this.prefixMap.put(key, otherMap.get(key).get()));
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
	public PrefixMap getPrefixMap() {
		return this.prefixMap;
	}

	@Override
	public void setPrefixMap(PrefixMap newPrefixMap) {
		this.prefixMap.clear();
		newPrefixMap.getKeysAsStream().forEach(key -> this.prefixMap.put(key, newPrefixMap.get(key).get()));
	}

	@Override
	public boolean add(Record record) {
		boolean result = false;
		if (Objects.isNull(record)) {
			result = false;
		} else {
			result = this.list.add(record);
		}
		return result;
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
		List<Record> result = new ArrayList<>();
		result.addAll(this.list);
		Collections.sort(result, new RecordComparator(this.sortingOrder, this.fieldsWithReverseOrder));
		return result;
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
		boolean result = false;
		if (this == obj) {
			result = true;
		} else if (obj instanceof Table) {
			Table other = (Table) obj;
			result = getType().equals(other.getType()) && getPrefixMap().equals(other.getPrefixMap())
					&& getSortingOrder().equals(other.getSortingOrder())
					&& getFieldsWithReverseOrder().equals(other.getFieldsWithReverseOrder())
					&& getRecords().equals(other.getRecords());
		} else {
			result = false;
		}
		return result;
	}

	@Override
	public String toString() {
		return this.tableType.toString() + " " + this.prefixMap.toString() + " " + this.sortingOrder.toString() + " "
				+ this.fieldsWithReverseOrder.toString() + " " + this.list.toString();
	}

}
