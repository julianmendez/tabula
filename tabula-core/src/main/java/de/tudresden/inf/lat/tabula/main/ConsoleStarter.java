package de.tudresden.inf.lat.tabula.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import de.tudresden.inf.lat.tabula.extension.Extension;
import de.tudresden.inf.lat.tabula.extension.ExtensionException;
import de.tudresden.inf.lat.tabula.extension.ExtensionManager;
import de.tudresden.inf.lat.tabula.extension.NormalizationExtension;

/**
 * An object of this class runs the application with the given arguments.
 */
public class ConsoleStarter {

	public static final String USAGE = "\nusage: java -jar (jarname) (extension) (input) (output)\n" //
			+ "\nIf the extension is ommitted, the '" + NormalizationExtension.NAME + "' extension is executed." //
			+ "\n\nThe available extensions are:" + "\n";

	public static final String ERROR_PREFIX = "ERROR: ";

	/**
	 * Constructs a new console starter.
	 */
	public ConsoleStarter() {
	}

	/**
	 * Executes the application
	 * 
	 * @param extensions
	 *            extensions
	 * 
	 * @param args
	 *            console arguments
	 */
	public void run(List<Extension> extensions, String[] args) {
		Objects.requireNonNull(extensions);
		Objects.requireNonNull(args);

		List<String> arguments = new ArrayList<>();
		if (args.length == 1) {
			arguments.add(NormalizationExtension.NAME);
		}
		arguments.addAll(Arrays.asList(args));

		ExtensionManager manager = new ExtensionManager(extensions);
		try {
			manager.process(arguments);
		} catch (ExtensionException e) {
			System.out.println(ERROR_PREFIX + e.getMessage());
			System.out.println(USAGE + manager.getHelp());
		}
	}

}
