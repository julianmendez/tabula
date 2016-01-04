package de.tudresden.inf.lat.tabula.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
	private final Set<String> identifiers = new TreeSet<>();
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
	}

	@Override
	public CompositeType getType() {
		return this.tableType;
	}

	public void setType(CompositeType newType) {
		this.tableType = newType;
	}

	public boolean add(Record record) {
		if (record == null) {
			return false;
		} else {
			return this.list.add(record);
		}
	}

	public boolean addId(String id) {
		return this.identifiers.add(id);
	}

	public List<String> getSortingOrder() {
		return this.sortingOrder;
	}

	public void setSortingOrder(List<String> sortingOrder0) {
		this.sortingOrder.clear();
		if (sortingOrder0 != null) {
			this.sortingOrder.addAll(sortingOrder0);
		}
	}

	public Set<String> getFieldsWithReverseOrder() {
		return this.fieldsWithReverseOrder;
	}

	public void setFieldsWithReverseOrder(Set<String> fieldsWithReverseOrder0) {
		this.fieldsWithReverseOrder.clear();
		if (fieldsWithReverseOrder0 != null) {
			this.fieldsWithReverseOrder.addAll(fieldsWithReverseOrder0);
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
	public Set<String> getIdentifiers() {
		return this.identifiers;
	}

	@Override
	public int hashCode() {
		return this.sortingOrder.hashCode() + 0x1F * (this.fieldsWithReverseOrder.hashCode()
				+ 0x1F * (this.list.hashCode() + 0x1F * this.tableType.hashCode()));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof Table) {
			Table other = (Table) obj;
			return getSortingOrder().equals(other.getSortingOrder())
					&& getFieldsWithReverseOrder().equals(other.getFieldsWithReverseOrder())
					&& getType().equals(other.getType()) && getRecords().equals(other.getRecords())
					&& getIdentifiers().equals(other.getIdentifiers());
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return this.tableType.toString() + " " + this.sortingOrder + " " + this.fieldsWithReverseOrder.toString() + " "
				+ this.list.toString();
	}

}
