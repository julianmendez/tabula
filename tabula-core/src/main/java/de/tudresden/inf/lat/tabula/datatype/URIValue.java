package de.tudresden.inf.lat.tabula.datatype;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This models a URI.
 *
 */
public class URIValue implements PrimitiveTypeValue {

	public static final String SPECIAL_SYMBOL = "#";

	private URI uri = null;

	/**
	 * Constructs a new URI value using a string.
	 * 
	 * @param uriStr
	 *            URI
	 */
	public URIValue(String uriStr) {
		Objects.requireNonNull(uriStr);
		this.uri = createURI(uriStr);
	}

	/**
	 * Constructs a new URI value using a URI.
	 * 
	 * @param uri
	 *            URI
	 */
	public URIValue(URI uri) {
		Objects.requireNonNull(uri);
		this.uri = uri;
	}

	/**
	 * Constructs a new URI value using another URI value.
	 * 
	 * @param other
	 *            URI value
	 */
	public URIValue(URIValue other) {
		this.uri = other.getUri();
	}

	@Override
	public PrimitiveType getType() {
		return new URIType();
	}

	public URI createURI(String uriStr) {
		URI result = null;
		try {
			result = new URI(uriStr);
		} catch (URISyntaxException e) {
			throw new ParseException("Invalid URI '" + uriStr + "'.", e);
		}
		return result;
	}

	public URI getUri() {
		return this.uri;
	}

	public URI getUriNoLabel() {
		URI result = null;
		String uriStr = this.uri.toASCIIString();
		int pos = uriStr.lastIndexOf(SPECIAL_SYMBOL);
		if (pos == -1) {
			result = this.uri;
		} else {
			result = createURI(uriStr.substring(0, pos));
		}
		return result;
	}

	public String getLabel() {
		String result = "";
		String uriStr = this.uri.toASCIIString();
		int pos = uriStr.lastIndexOf(SPECIAL_SYMBOL);
		if (pos == -1) {
			result = "";
		} else {
			result = uriStr.substring(pos + SPECIAL_SYMBOL.length());
		}
		return result;
	}

	@Override
	public boolean isEmpty() {
		return getUri().toASCIIString().trim().isEmpty();
	}

	@Override
	public String render() {
		return this.uri.toASCIIString();
	}

	@Override
	public List<String> renderAsList() {
		List<String> list = new ArrayList<>();
		list.add(render());
		List<String> result = Collections.unmodifiableList(list);
		return result;
	}

	@Override
	public int compareTo(PrimitiveTypeValue other) {
		return toString().compareTo(other.toString());
	}

	@Override
	public int hashCode() {
		return this.uri.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (this == obj) {
			result = true;
		} else if (!(obj instanceof URIValue)) {
			result = false;
		} else {
			URIValue other = (URIValue) obj;
			result = getUri().equals(other.getUri());
		}
		return result;
	}

	@Override
	public String toString() {
		return render();
	}

}
