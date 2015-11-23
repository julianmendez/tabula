package de.tudresden.inf.lat.tabula.extension;

import java.util.List;

/**
 * This models an extension.
 *
 */
public interface Extension {

	/**
	 * Executes an extension.
	 * 
	 * @param arguments
	 *            arguments
	 * 
	 * @return <code>true</code> if the extension was successfully executed
	 */
	boolean process(List<String> arguments);

	/**
	 * Returns a name for this extension.
	 * 
	 * @return a name for this extension
	 */
	String getExtensionName();

	/**
	 * Returns a human-readable help of what this extension does.
	 * 
	 * @return a human-readable help of what this extension does
	 */
	String getHelp();

	/**
	 * Returns the number of required arguments.
	 * 
	 * @return the number of required arguments
	 */
	int getRequiredArguments();

}
