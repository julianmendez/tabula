package de.tudresden.inf.lat.tabula.common;

import java.util.Map;
import java.util.Optional;

/**
 * An object implementing this interface wraps a map with some conditions:
 * <ul>
 * <li>no <code>null</code> value is accepted nor returned</li>
 * <li>the methods that verify containment accept only objects of type K or
 * V</li>
 * </ul>
 * 
 * @author Julian Mendez
 * @param <K>
 *            type of keys in this map
 * @param <V>
 *            type of mapped values
 */
public interface OptMap<K, V> extends Map<K, V> {

	/**
	 * Returns <code>true</code> if and only if this map contains a mapping for
	 * the given key. This method replaces {@link #containsKey(Object)}.
	 * 
	 * @param key
	 *            key
	 * @return <code>true</code> if and only if this map contains a mapping for
	 *         the given key
	 * @throws NullPointerException
	 *             if a <code>null</code> value is given
	 */
	public boolean isKeyContained(K key);

	/**
	 * @deprecated Replaced by {@link #isKeyContained isKeyContained(K)}
	 */
	@Deprecated
	public boolean containsKey(Object key);

	/**
	 * Returns <code>true</code> if and only if this map associates one or more
	 * keys to the given value. This method replaces
	 * {@link #containsValue(Object)}.
	 * 
	 * @param value
	 *            value
	 * @return <code>true</code> if and only if this map associates one or more
	 *         keys to the given value
	 * @throws NullPointerException
	 *             if a <code>null</code> value is given
	 */
	public boolean isValueContained(V value);

	/**
	 * @deprecated Replaced by {@link #isValueContained isValueContained(V)}
	 */
	@Deprecated
	public boolean containsValue(Object value);

	/**
	 * Returns an optional containing the value associated to the given key, is
	 * this association exists, or an empty optional otherwise. This method
	 * replaces {@link #get(Object)}.
	 * 
	 * @param key
	 *            key
	 * @return an optional containing the value associated to the given key, is
	 *         this association exists, or an empty optional otherwise
	 */
	public Optional<V> getOpt(K key);

	/**
	 * @deprecated Replaced by {@link #getOpt getOpt(K)}
	 */
	@Deprecated
	public V get(Object key);

	/**
	 * Associates the given value with the given key. This method replaces
	 * {@link #put put(K, V)}.
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * @return an optional containing the previous associated value for the
	 *         given key, or an empty optional if there was no mapping for that
	 *         key
	 * @throws NullPointerException
	 *             if a <code>null</code> value is given
	 */
	public Optional<V> putOpt(K key, V value);

	/**
	 * @deprecated Replaced by {@link #putOpt putOpt(K, V)}
	 */
	@Deprecated
	public V put(K key, V value);

	/**
	 * Removes the mapping for the given key. This method replaces
	 * {@link #remove(Object)}.
	 * 
	 * @param key
	 *            key
	 * @return an optional containing the previous associated value for the
	 *         given key, or an empty optional if there was no mapping for that
	 *         key
	 * @throws NullPointerException
	 *             if a <code>null</code> value is given
	 */
	public Optional<V> removeOpt(K key);

	/**
	 * @deprecated Replaced by {@link #removeOpt removeOpt(K)}
	 */
	@Deprecated
	public V remove(Object key);

}
