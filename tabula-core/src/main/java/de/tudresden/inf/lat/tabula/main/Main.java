package de.tudresden.inf.lat.tabula.main;

import java.util.ArrayList;
import java.util.List;

import de.tudresden.inf.lat.tabula.extension.DefaultExtension;
import de.tudresden.inf.lat.tabula.extension.Extension;
import de.tudresden.inf.lat.tabula.extension.NormalizationExtension;

/**
 * This is the main class.
 */
public class Main {

	/**
	 * Constructs a new main class.
	 */
	public Main() {
	}

	/**
	 * Entry point for the console.
	 * 
	 * @param args
	 *            console arguments
	 */
	public static void main(String args[]) {
		List<Extension> extensions = new ArrayList<>();
		extensions.add(new DefaultExtension());
		extensions.add(new NormalizationExtension());

		ConsoleStarter instance = new ConsoleStarter();
		instance.run(extensions, args);
	}

}
