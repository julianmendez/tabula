package de.tudresden.inf.lat.tabula.extension;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import de.tudresden.inf.lat.tabula.parser.SimpleFormatParser;
import de.tudresden.inf.lat.tabula.renderer.SimpleFormatRenderer;
import de.tudresden.inf.lat.tabula.table.TableMap;

/**
 * Normalization extension. It reads and writes using the same file.
 *
 */
public class NormalizationExtension implements Extension {

	public static final String NAME = "normalize";
	public static final String HELP = "(input) : normalize a file with a simple text format";
	public static final int REQUIRED_ARGUMENTS = 1;

	@Override
	public boolean process(List<String> arguments) {
		boolean result = false;
		if (Objects.isNull(arguments) || arguments.size() != REQUIRED_ARGUMENTS) {
			result = false;
		} else {
			try {
				String inputFileName = arguments.get(0);
				String outputFileName = inputFileName;
				TableMap tableMap = new SimpleFormatParser(new FileReader(inputFileName)).parse();
				BufferedWriter output = new BufferedWriter(new FileWriter(outputFileName));
				SimpleFormatRenderer renderer = new SimpleFormatRenderer(output);
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
