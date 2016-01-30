package de.tudresden.inf.lat.tabula.table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import de.tudresden.inf.lat.tabula.datatype.PrimitiveTypeValue;
import de.tudresden.inf.lat.tabula.datatype.Record;

/**
 * Comparator for records.
 * 
 */
public class RecordComparator implements Comparator<Record> {

	private final List<String> sortingOrder = new ArrayList<>();
	private final Set<String> fieldsWithReverseOrder = new TreeSet<>();

	public RecordComparator(List<String> sortingOrder) {
		this.sortingOrder.addAll(sortingOrder);
	}

	public RecordComparator(List<String> sortingOrder, Set<String> reverseOrder) {
		this.sortingOrder.addAll(sortingOrder);
		this.fieldsWithReverseOrder.addAll(reverseOrder);
	}

	public List<String> getSortingOrder() {
		return this.sortingOrder;
	}

	public Set<String> getFieldsWithReverseOrder() {
		return this.fieldsWithReverseOrder;
	}

	@Override
	public int compare(Record record0, Record record1) {
		if (record0 == null) {
			if (record1 == null) {
				return 0;
			} else {
				return (-1);
			}
		} else {
			if (record1 == null) {
				return 1;
			} else {
				int ret = 0;
				Iterator<String> it = this.sortingOrder.iterator();
				while (it.hasNext() && (ret == 0)) {
					String token = it.next();
					ret = compareValues(record0.get(token).get(), record1.get(token).get(),
							this.fieldsWithReverseOrder.contains(token));
				}
				return ret;
			}
		}
	}

	public int compareValues(PrimitiveTypeValue value0, PrimitiveTypeValue value1, boolean hasReverseOrder) {
		if (hasReverseOrder) {
			return compareValues(value1, value0, false);
		} else {
			if (value0 == null) {
				if (value1 == null) {
					return 0;
				} else {
					return (-1);
				}
			} else {
				if (value1 == null) {
					return 1;
				} else {
					return value0.compareTo(value1);
				}
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (o instanceof RecordComparator) {
			RecordComparator other = (RecordComparator) o;
			return this.sortingOrder.equals(other.sortingOrder);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.sortingOrder.hashCode();
	}

	@Override
	public String toString() {
		return this.sortingOrder.toString();
	}

}
