package de.tudresden.inf.lat.tabula.table;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

import de.tudresden.inf.lat.util.map.OptMap;
import de.tudresden.inf.lat.util.map.OptMapImpl;

/**
 * An object of this class is a map of URI prefixes. This implementation
 * iterates on the keys keeping the order in which they were added for the first
 * time.
 *
 */
public class PrefixMapImpl implements PrefixMap {

	private OptMap<URI, URI> prefixMap = new OptMapImpl<URI, URI>(new TreeMap<>());
	private List<URI> keyList = new ArrayList<URI>();

	@Override
	public boolean isEmpty() {
		return this.prefixMap.isEmpty();
	}

	@Override
	public int size() {
		return this.prefixMap.size();
	}

	@Override
	public Optional<URI> get(URI key) {
		return this.prefixMap.get(key);
	}

	@Override
	public Optional<URI> put(URI key, URI value) {
		if (!this.prefixMap.containsKey(key)) {
			this.keyList.add(key);
		}
		return this.prefixMap.put(key, value);
	}

	@Override
	public Optional<URI> getPrefixFor(URI uri) {
		String uriStr = uri.toASCIIString();
		Optional<URI> key = prefixMap.keySet().stream()
				.filter((URI e) -> uriStr.startsWith(prefixMap.get(e).get().toASCIIString())).findFirst();
		return key;
	}

	@Override
	public URI getWithoutPrefix(URI uri) {
		URI ret = uri;
		String uriStr = uri.toASCIIString();
		if (uriStr.startsWith(PREFIX_AMPERSAND)) {
			int pos = uriStr.indexOf(PREFIX_SEMICOLON, PREFIX_AMPERSAND.length());
			if (pos != -1) {
				URI prefix = URI.create(uriStr.substring(PREFIX_AMPERSAND.length(), pos));
				Optional<URI> optExpansion = this.prefixMap.get(prefix);
				if (optExpansion.isPresent()) {
					ret = URI.create(
							optExpansion.get().toASCIIString() + uriStr.substring(pos + PREFIX_SEMICOLON.length()));
				}
			}
		}
		return ret;
	}

	@Override
	public URI getWithPrefix(URI uri) {
		URI ret = uri;
		Optional<URI> optPrefix = getPrefixFor(uri);
		if (optPrefix.isPresent()) {
			String uriStr = uri.toASCIIString();
			URI key = optPrefix.get();
			String keyStr = key.toASCIIString();
			Optional<URI> optExpansion = get(key);
			String expansionStr = optExpansion.get().toASCIIString();
			if (keyStr.isEmpty()) {
				ret = URI.create(uriStr.substring(expansionStr.length()));
			} else {
				ret = URI
						.create(PREFIX_AMPERSAND + keyStr + PREFIX_SEMICOLON + uriStr.substring(expansionStr.length()));
			}
		}
		return ret;
	}

	@Override
	public Stream<URI> getKeysAsStream() {
		return this.keyList.stream();
	}

	@Override
	public void clear() {
		this.prefixMap.clear();
		this.keyList.clear();
	}

}
