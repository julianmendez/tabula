package de.tudresden.inf.lat.tabula.main;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.tudresden.inf.lat.tabula.parser.SimpleFormatParser;
import de.tudresden.inf.lat.tabula.renderer.SimpleFormatRenderer;
import de.tudresden.inf.lat.tabula.table.TableMap;

/**
 * This is a test of normalization of files.
 */
public class NormalizationTest {

	public static final String INPUT_FILE_NAME_0 = "example.properties";
	public static final String EXPECTED_OUTPUT_FILE_NAME_0 = "example-expected.properties";

	public static final String INPUT_FILE_NAME_1 = "multiple_tables.properties";
	public static final String EXPECTED_OUTPUT_FILE_NAME_1 = "multiple_tables-expected.properties";

	public static final String INPUT_FILE_NAME_2 = "another_example.properties";
	public static final String EXPECTED_OUTPUT_FILE_NAME_2 = "another_example-expected.properties";

	public static final String NEW_LINE = "\n";

	String getPath(String fileName) {
		return getClass().getClassLoader().getResource(fileName).getFile();
	}

	void testNormalizationOfFile(String inputFileName, String expectedFileName) throws IOException {
		TableMap tableMap = new SimpleFormatParser(new FileReader(getPath(inputFileName))).parse();
		String expectedResult = (new MainTest()).readFile(expectedFileName);
		StringWriter writer = new StringWriter();
		SimpleFormatRenderer renderer = new SimpleFormatRenderer(writer);
		renderer.render(tableMap);
		Assertions.assertEquals(expectedResult, writer.toString());
	}

	@Test
	public void testNormalization() throws IOException {
		testNormalizationOfFile(INPUT_FILE_NAME_0, EXPECTED_OUTPUT_FILE_NAME_0);
		testNormalizationOfFile(INPUT_FILE_NAME_1, EXPECTED_OUTPUT_FILE_NAME_1);
		testNormalizationOfFile(INPUT_FILE_NAME_2, EXPECTED_OUTPUT_FILE_NAME_2);
	}

}
