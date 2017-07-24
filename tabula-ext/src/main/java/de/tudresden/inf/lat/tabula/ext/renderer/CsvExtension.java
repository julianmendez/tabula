package de.tudresden.inf.lat.tabula.ext.renderer;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import de.tudresden.inf.lat.tabula.extension.Extension;
import de.tudresden.inf.lat.tabula.extension.ExtensionException;
import de.tudresden.inf.lat.tabula.parser.SimpleFormatParser;
import de.tudresden.inf.lat.tabula.table.TableMap;

/**
 * This models an extension that writes the output in comma-separated values.
 *
 */
public class CsvExtension implements Extension {

	public static final String NAME = "csv";
	public static final String HELP = "(input) (output) : create output as a comma-separated values (CSV) file";
	public static final int REQUIRED_ARGUMENTS = 2;

	@Override
	public boolean process(List<String> arguments) {
		boolean result = false;
		if (Objects.isNull(arguments) || arguments.size() != REQUIRED_ARGUMENTS) {
			result = false;
		} else {
			try {
				String inputFileName = arguments.get(0);
				String outputFileName = arguments.get(1);
				TableMap tableMap = new SimpleFormatParser(new FileReader(inputFileName)).parse();
				BufferedWriter output = new BufferedWriter(new FileWriter(outputFileName));
				CsvRenderer renderer = new CsvRenderer(output);
				renderer.render(tableMap);
				result = true;
			} catch (IOException e) {
				throw new ExtensionException(e);
			}
		}
		return result;
	}

	@Override
	public String getExtensionName() {
		return NAME;
	}

	@Override
	public String getHelp() {
		return HELP;
	}

	@Override
	public int getRequiredArguments() {
		return REQUIRED_ARGUMENTS;
	}

}
