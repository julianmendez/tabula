package de.tudresden.inf.lat.tabula.main;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

import de.tudresden.inf.lat.tabula.parser.SimpleFormatParser;
import de.tudresden.inf.lat.tabula.renderer.SimpleFormatRenderer;
import de.tudresden.inf.lat.tabula.table.TableMap;

/**
 * This is a test of normalization of files.
 */
public class NormalizationTest {

	public static final String INPUT_FILE_NAME_0 = "src/test/resources/example.properties";
	public static final String EXPECTED_OUTPUT_FILE_NAME_0 = "src/test/resources/example-expected.properties";

	public static final String INPUT_FILE_NAME_1 = "src/test/resources/multiple_tables.properties";
	public static final String EXPECTED_OUTPUT_FILE_NAME_1 = "src/test/resources/multiple_tables-expected.properties";

	public static final String NEW_LINE = "\n";

	void testNormalizationOfFile(String inputFileName, String expectedFileName) throws IOException {
		TableMap tableMap = new SimpleFormatParser(new FileReader(inputFileName)).parse();
		String expectedResult = MainTest.readFile(expectedFileName);
		StringWriter writer = new StringWriter();
		SimpleFormatRenderer renderer = new SimpleFormatRenderer(writer);
		renderer.render(tableMap);
		Assert.assertEquals(expectedResult, writer.toString());
	}

	@Test
	public void testNormalization() throws IOException {
		testNormalizationOfFile(INPUT_FILE_NAME_0, EXPECTED_OUTPUT_FILE_NAME_0);
		testNormalizationOfFile(INPUT_FILE_NAME_1, EXPECTED_OUTPUT_FILE_NAME_1);
	}

}
