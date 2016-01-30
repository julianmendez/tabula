package de.tudresden.inf.lat.tabula.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

import de.tudresden.inf.lat.tabula.datatype.PrimitiveTypeValue;
import de.tudresden.inf.lat.tabula.datatype.Record;

/**
 * This is the default implementation of a record.
 *
 */
public class RecordImpl implements Record {

	private final Map<String, PrimitiveTypeValue> map = new TreeMap<>();

	/**
	 * Constructs a new record.
	 */
	public RecordImpl() {
	}

	/**
	 * Constructs a new record using another one.
	 * 
	 * @param otherRecord
	 *            other record
	 */
	public RecordImpl(Record otherRecord) {
		otherRecord.getProperties().forEach(property -> set(property, otherRecord.get(property).get()));
	}

	@Override
	public Optional<PrimitiveTypeValue> get(String key) {
		if (Objects.isNull(key)) {
			return Optional.empty();
		} else {
			PrimitiveTypeValue value = this.map.get(key);
			if (Objects.isNull(value)) {
				return Optional.empty();
			} else {
				return Optional.of(value);
			}
		}
	}

	@Override
	public void set(String key, PrimitiveTypeValue value) {
		if (Objects.nonNull(key)) {
			this.map.put(key, value);
		}
	}

	@Override
	public List<String> getProperties() {
		List<String> ret = new ArrayList<>();
		ret.addAll(map.keySet());
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (o instanceof Record) {
			Record other = (Record) o;
			boolean ret = getProperties().equals(other.getProperties());
			ret = ret && getProperties().stream().allMatch(property -> get(property).equals(other.get(property)));
			return ret;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.map.hashCode();
	}

	@Override
	public String toString() {
		return this.map.toString();
	}

}
