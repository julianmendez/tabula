package de.tudresden.inf.lat.tabula.datatype;

/**
 * Parse exception.
 */
public class ParseException extends RuntimeException {

	private static final long serialVersionUID = 6474793300996737190L;

	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParseException(String message) {
		this(message, null);
	}

	public ParseException(Throwable cause) {
		this(null, cause);
	}

}
