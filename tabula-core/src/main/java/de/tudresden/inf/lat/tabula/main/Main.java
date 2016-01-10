package de.tudresden.inf.lat.tabula.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import de.tudresden.inf.lat.tabula.extension.DefaultExtension;
import de.tudresden.inf.lat.tabula.extension.Extension;
import de.tudresden.inf.lat.tabula.extension.ExtensionManager;
import de.tudresden.inf.lat.tabula.extension.NormalizationExtension;

/**
 * This is the main class.
 */
public class Main {

	private static final String HEADER = "Use: java -jar (jarname) (command) (input) (output)\n\n";

	private final ExtensionManager manager;

	/**
	 * Constructs a new main class.
	 */
	public Main() {
		List<Extension> extensions = new ArrayList<>();
		extensions.add(new DefaultExtension());
		extensions.add(new NormalizationExtension());

		this.manager = new ExtensionManager(extensions);
	}

	public void run(String args[]) {
		if ((args != null) && ((args.length == 2) || (args.length == 3))) {
			List<String> arguments = new ArrayList<>();
			IntStream.range(0, args.length).forEach(index -> arguments.add(args[index]));
			this.manager.process(arguments);
		} else {
			System.out.println(HEADER + this.manager.getHelp());
		}
	}

	public static void main(String args[]) throws IOException {
		Main instance = new Main();
		instance.run(args);
	}

}
