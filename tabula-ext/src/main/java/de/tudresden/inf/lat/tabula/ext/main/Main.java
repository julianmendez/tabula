package de.tudresden.inf.lat.tabula.ext.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.IntStream;

import de.tudresden.inf.lat.tabula.ext.parser.CalendarParserExtension;
import de.tudresden.inf.lat.tabula.ext.parser.CsvParserExtension;
import de.tudresden.inf.lat.tabula.ext.renderer.CsvExtension;
import de.tudresden.inf.lat.tabula.ext.renderer.HtmlExtension;
import de.tudresden.inf.lat.tabula.ext.renderer.SqlExtension;
import de.tudresden.inf.lat.tabula.ext.renderer.WikitextExtension;
import de.tudresden.inf.lat.tabula.extension.DefaultExtension;
import de.tudresden.inf.lat.tabula.extension.Extension;
import de.tudresden.inf.lat.tabula.extension.ExtensionManager;
import de.tudresden.inf.lat.tabula.extension.NormalizationExtension;

/**
 * This is the main class.
 */
public class Main {

	private static final String Header = "Use: java -jar (jarname) (command) [(field)] (input) (output)\n\n";

	private final ExtensionManager manager;

	/**
	 * Constructs a new main class.
	 */
	public Main() {
		ArrayList<Extension> extensions = new ArrayList<>();
		extensions.add(new DefaultExtension());
		extensions.add(new CsvParserExtension());
		extensions.add(new CalendarParserExtension());
		extensions.add(new WikitextExtension());
		extensions.add(new SqlExtension());
		extensions.add(new CsvExtension());
		extensions.add(new HtmlExtension());
		extensions.add(new NormalizationExtension());

		this.manager = new ExtensionManager(extensions);
	}

	public void run(String args[]) {
		if ((args != null) && ((args.length == 2) || (args.length == 3) || (args.length == 4))) {
			ArrayList<String> arguments = new ArrayList<>();
			IntStream.range(0, args.length).forEach(index -> arguments.add(args[index]));
			this.manager.process(arguments);
		} else {
			System.out.println(Header + this.manager.getHelp());
		}
	}

	public static void main(String args[]) throws IOException {
		Main instance = new Main();
		instance.run(args);
	}

}
