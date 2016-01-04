package de.tudresden.inf.lat.tabula.ext.renderer;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import de.tudresden.inf.lat.tabula.extension.Extension;
import de.tudresden.inf.lat.tabula.extension.ExtensionException;
import de.tudresden.inf.lat.tabula.parser.SimpleFormatParser;
import de.tudresden.inf.lat.tabula.table.TableMap;

/**
 * This models an extension that writes the output in comma-separated values.
 *
 */
public class CsvExtension implements Extension {

	public static final String Name = "csv";
	public static final String Help = "(input) (output) : create output as a comma-separated values (CSV) file";
	public static final int RequiredArguments = 2;

	@Override
	public boolean process(List<String> arguments) {
		if (arguments == null || arguments.size() != RequiredArguments) {
			return false;
		} else {
			try {
				String inputFileName = arguments.get(0);
				String outputFileName = arguments.get(1);
				TableMap tableMap = new SimpleFormatParser(new FileReader(inputFileName)).parse();
				BufferedWriter output = new BufferedWriter(new FileWriter(outputFileName));
				CsvRenderer renderer = new CsvRenderer(output);
				renderer.render(tableMap);
				return true;
			} catch (IOException e) {
				throw new ExtensionException(e);
			}
		}
	}

	@Override
	public String getExtensionName() {
		return Name;
	}

	@Override
	public String getHelp() {
		return Help;
	}

	@Override
	public int getRequiredArguments() {
		return RequiredArguments;
	}

}
