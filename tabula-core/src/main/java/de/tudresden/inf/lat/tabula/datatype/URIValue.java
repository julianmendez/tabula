package de.tudresden.inf.lat.tabula.datatype;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This models a link.
 *
 */
public class URIValue implements PrimitiveTypeValue {

	public static final String SpecialSymbol = "#";

	private URI uri = null;

	public URIValue(String link) {
		this.uri = createURI(link);
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
		int pos = uriStr.lastIndexOf(SpecialSymbol);
		if (pos == -1) {
			return this.uri;
		} else {
			return createURI(uriStr.substring(0, pos));
		}
	}

	public String getLabel() {
		String uriStr = this.uri.toASCIIString();
		int pos = uriStr.lastIndexOf(SpecialSymbol);
		if (pos == -1) {
			return "";
		} else {
			return uriStr.substring(pos + SpecialSymbol.length());
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
