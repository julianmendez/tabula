package de.tudresden.inf.lat.tabula.extension;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import de.tudresden.inf.lat.tabula.parser.SimpleFormatParser;
import de.tudresden.inf.lat.tabula.renderer.SimpleFormatRenderer;
import de.tudresden.inf.lat.tabula.table.TableMap;

/**
 * Normalization extension. It reads and writes using the same file.
 *
 */
public class NormalizationExtension implements Extension {

	public static final String Name = "normalize";
	public static final String Help = "(input) : normalize a file with a simple text format";
	public static final int RequiredArguments = 1;

	@Override
	public boolean process(List<String> arguments) {
		if (arguments == null || arguments.size() != RequiredArguments) {
			return false;
		} else {
			try {
				String inputFileName = arguments.get(0);
				String outputFileName = inputFileName;
				TableMap tableMap = new SimpleFormatParser(new FileReader(
						inputFileName)).parse();
				BufferedWriter output = new BufferedWriter(new FileWriter(
						outputFileName));
				SimpleFormatRenderer renderer = new SimpleFormatRenderer(output);
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
