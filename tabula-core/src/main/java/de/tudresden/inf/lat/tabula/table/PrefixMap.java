package de.tudresden.inf.lat.tabula.table;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This models a map of URI prefixes.
 */
public interface PrefixMap {

	/**
	 * Start of the prefix.
	 */
	String PREFIX_AMPERSAND = "&";

	/**
	 * End of the prefix.
	 */
	String PREFIX_SEMICOLON = ";";

	/**
	 * Returns the expansion for the given prefix.
	 *
	 * @param key
	 *            the prefix
	 * @return expansion for the given prefix
	 */
	Optional<URI> get(URI key);

	/**
	 * Assigns a prefix to an expansion.
	 *
	 * @param key
	 *            prefix
	 * @param value
	 *            expansion
	 * @return an optional containing the previous value of the given key, or
	 *         empty if there was no value for that key
	 */
	Optional<URI> put(URI key, URI value);

	/**
	 * Returns a URI with the prefix, i.e. a shortened URI.
	 *
	 * @param uri
	 *            URI
	 * @return a URI with the prefix
	 */
	URI getWithPrefix(URI uri);

	/**
	 * Returns a URI without the prefix, i.e. with the prefix already expanded.
	 *
	 * @param uri
	 *            URI
	 * @return a URI without the prefix
	 */
	URI getWithoutPrefix(URI uri);

	/**
	 * Returns an optional with the prefix for the given URI, or empty if there
	 * is none.
	 *
	 * @param uri
	 *            URI
	 * @return an optional with the prefix for the given URI, or empty if there
	 *         is none
	 */
	Optional<URI> getPrefixFor(URI uri);

	/**
	 * Returns a stream to iterate on the keys.
	 *
	 * @return a stream to iterate on the keys
	 */
	Stream<URI> getKeysAsStream();

	/**
	 * Clears the content.
	 */
	void clear();

	/**
	 * Returns the size of this prefix map.
	 *
	 * @return the size of this prefix map
	 */
	int size();

}
