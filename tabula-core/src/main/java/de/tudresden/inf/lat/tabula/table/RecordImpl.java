package de.tudresden.inf.lat.tabula.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

import de.tudresden.inf.lat.tabula.datatype.PrimitiveTypeValue;
import de.tudresden.inf.lat.tabula.datatype.Record;
import de.tudresden.inf.lat.util.map.OptMap;
import de.tudresden.inf.lat.util.map.OptMapImpl;

/**
 * This is the default implementation of a record.
 *
 */
public class RecordImpl implements Record {

	private final OptMap<String, PrimitiveTypeValue> map = new OptMapImpl<>(new TreeMap<>());

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
		Optional<PrimitiveTypeValue> result = Optional.empty();
		if (Objects.isNull(key)) {
			result = Optional.empty();
		} else {
			result = this.map.get(key);
		}
		return result;
	}

	@Override
	public void set(String key, PrimitiveTypeValue value) {
		if (Objects.nonNull(key)) {
			this.map.put(key, value);
		}
	}

	@Override
	public List<String> getProperties() {
		List<String> result = new ArrayList<>();
		result.addAll(map.keySet());
		return result;
	}

	@Override
	public boolean equals(Object o) {
		boolean result = false;
		if (this == o) {
			result = true;
		} else if (o instanceof Record) {
			Record other = (Record) o;
			result = getProperties().equals(other.getProperties());
			result = result && getProperties().stream().allMatch(property -> get(property).equals(other.get(property)));
		} else {
			result = false;
		}
		return result;
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
