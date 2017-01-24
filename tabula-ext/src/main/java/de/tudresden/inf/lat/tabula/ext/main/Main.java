package de.tudresden.inf.lat.tabula.ext.main;

import java.util.ArrayList;
import java.util.List;

import de.tudresden.inf.lat.tabula.ext.parser.CalendarParserExtension;
import de.tudresden.inf.lat.tabula.ext.parser.CsvParserExtension;
import de.tudresden.inf.lat.tabula.ext.renderer.CsvExtension;
import de.tudresden.inf.lat.tabula.ext.renderer.HtmlExtension;
import de.tudresden.inf.lat.tabula.ext.renderer.SqlExtension;
import de.tudresden.inf.lat.tabula.ext.renderer.WikitextExtension;
import de.tudresden.inf.lat.tabula.extension.DefaultExtension;
import de.tudresden.inf.lat.tabula.extension.Extension;
import de.tudresden.inf.lat.tabula.extension.NormalizationExtension;
import de.tudresden.inf.lat.tabula.main.CommandLineStarter;

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
	 * Entry point for the command line.
	 * 
	 * @param args
	 *            command-line arguments
	 */
	public static void main(String args[]) {
		List<Extension> extensions = new ArrayList<>();
		extensions.add(new DefaultExtension());
		extensions.add(new CsvParserExtension());
		extensions.add(new CalendarParserExtension());
		extensions.add(new WikitextExtension());
		extensions.add(new SqlExtension());
		extensions.add(new CsvExtension());
		extensions.add(new HtmlExtension());
		extensions.add(new NormalizationExtension());

		CommandLineStarter instance = new CommandLineStarter();
		instance.run(extensions, args);
	}

}
