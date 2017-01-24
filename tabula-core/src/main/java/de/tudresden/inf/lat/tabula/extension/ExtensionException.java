package de.tudresden.inf.lat.tabula.extension;

/**
 * Extension exception.
 */
public class ExtensionException extends RuntimeException {

	private static final long serialVersionUID = 5305514536326848806L;

	public ExtensionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExtensionException(String message) {
		super(message);
	}

	public ExtensionException(Throwable cause) {
		super(cause);
	}

}
