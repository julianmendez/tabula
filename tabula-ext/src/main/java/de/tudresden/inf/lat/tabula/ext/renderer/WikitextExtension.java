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
 * This models an extension that writes the output in Wikitext.
 *
 */
public class WikitextExtension implements Extension {

	public static final String NAME = "wikitext";
	public static final String HELP = "(input) (output) : create output as Wiki text file";
	public static final int REQUIRED_ARGUMENTS = 2;

	@Override
	public boolean process(List<String> arguments) {
		if (Objects.isNull(arguments) || arguments.size() != REQUIRED_ARGUMENTS) {
			return false;
		} else {
			try {
				String inputFileName = arguments.get(0);
				String outputFileName = arguments.get(1);
				TableMap tableMap = new SimpleFormatParser(new FileReader(inputFileName)).parse();
				BufferedWriter output = new BufferedWriter(new FileWriter(outputFileName));
				WikitextRenderer renderer = new WikitextRenderer(output);
				renderer.render(tableMap);
				return true;
			} catch (IOException e) {
				throw new ExtensionException(e);
			}
		}
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
