package de.tudresden.inf.lat.tabula.datatype;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	 * @param link
	 *            URI
	 */
	public URIValue(String link) {
		this.uri = createURI(link);
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

	public URI createURI(String uri0) {
		try {
			return new URI(uri0);
		} catch (URISyntaxException e) {
			throw new ParseException("Invalid URI '" + uri0 + "'.", e);
		}
	}

	public URI getUri() {
		return this.uri;
	}

	public URI getUriNoLabel() {
		String uriStr = this.uri.toASCIIString();
		int pos = uriStr.lastIndexOf(SPECIAL_SYMBOL);
		if (pos == -1) {
			return this.uri;
		} else {
			return createURI(uriStr.substring(0, pos));
		}
	}

	public String getLabel() {
		String uriStr = this.uri.toASCIIString();
		int pos = uriStr.lastIndexOf(SPECIAL_SYMBOL);
		if (pos == -1) {
			return "";
		} else {
			return uriStr.substring(pos + SPECIAL_SYMBOL.length());
		}
	}

	@Override
	public boolean isEmpty() {
		return (getUri() == null) || getUri().toASCIIString().trim().isEmpty();
	}

	@Override
	public String render() {
		return this.uri.toASCIIString();
	}

	@Override
	public List<String> renderAsList() {
		List<String> ret = new ArrayList<>();
		ret.add(render());
		return Collections.unmodifiableList(ret);
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
		if (this == obj) {
			return true;
		} else if (!(obj instanceof URIValue)) {
			return false;
		} else {
			URIValue other = (URIValue) obj;
			return getUri().equals(other.getUri());
		}
	}

	@Override
	public String toString() {
		return render();
	}

}
