package de.tudresden.inf.lat.tabula.table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
		if (Objects.isNull(record0)) {
			if (Objects.isNull(record1)) {
				return 0;
			} else {
				return (-1);
			}
		} else {
			if (Objects.isNull(record1)) {
				return 1;
			} else {
				int ret = 0;
				Iterator<String> it = this.sortingOrder.iterator();
				while (it.hasNext() && (ret == 0)) {
					String token = it.next();
					ret = compareValues(record0.get(token), record1.get(token),
							this.fieldsWithReverseOrder.contains(token));
				}
				return ret;
			}
		}
	}

	public int compareValues(Optional<PrimitiveTypeValue> optValue0, Optional<PrimitiveTypeValue> optValue1,
			boolean hasReverseOrder) {
		if (hasReverseOrder) {
			return compareValues(optValue1, optValue0, false);
		} else {
			if (optValue0.isPresent()) {
				if (optValue1.isPresent()) {
					return optValue0.get().compareTo(optValue1.get());
				} else {
					return 1;
				}
			} else {
				if (optValue1.isPresent()) {
					return (-1);
				} else {
					return 0;
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
