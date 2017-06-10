package de.tudresden.inf.lat.tabula.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * This is the default implementation of {{@link OptMap}.
 * 
 * @author Julian Mendez
 * 
 * @param <K>
 *            type of keys in this map
 * @param <V>
 *            type of mapped values
 */
public class OptMapImpl<K, V> implements OptMap<K, V> {

	private final Map<K, V> map;

	public OptMapImpl() {
		this.map = new HashMap<K, V>();
	}

	public OptMapImpl(Map<K, V> map) {
		Objects.requireNonNull(map);
		this.map = map;
	}

	@Override
	public int size() {
		return this.map.size();
	}

	@Override
	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	@Override
	public boolean isKeyContained(K key) {
		Objects.requireNonNull(key);
		return this.map.containsKey(key);
	}

	@Override
	@Deprecated
	public boolean containsKey(Object key) {
		Objects.requireNonNull(key);
		return this.map.containsKey(key);
	}

	@Override
	public boolean isValueContained(V value) {
		Objects.requireNonNull(value);
		return this.map.containsValue(value);
	}

	@Override
	@Deprecated
	public boolean containsValue(Object value) {
		Objects.requireNonNull(value);
		return this.map.containsValue(value);
	}

	@Override
	public Optional<V> getOpt(K key) {
		Objects.requireNonNull(key);
		return Optional.ofNullable(this.map.get(key));
	}

	@Override
	@Deprecated
	public V get(Object key) {
		Objects.requireNonNull(key);
		return this.map.get(key);
	}

	@Override
	public Optional<V> putOpt(K key, V value) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(value);
		return Optional.ofNullable(this.map.put(key, value));
	}

	@Override
	@Deprecated
	public V put(K key, V value) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(value);
		return this.map.put(key, value);
	}

	@Override
	public Optional<V> removeOpt(K key) {
		Objects.requireNonNull(key);
		return Optional.ofNullable(this.map.remove(key));
	}

	@Override
	@Deprecated
	public V remove(Object key) {
		Objects.requireNonNull(key);
		return this.map.remove(key);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		Objects.requireNonNull(m);
		this.map.putAll(m);
	}

	@Override
	public void clear() {
		this.map.clear();
	}

	@Override
	public Set<K> keySet() {
		return this.map.keySet();
	}

	@Override
	public Collection<V> values() {
		return this.map.values();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return this.map.entrySet();
	}

	@Override
	public int hashCode() {
		return this.map.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return this.map.equals(obj);
	}

	@Override
	public String toString() {
		return this.map.toString();
	}

}
