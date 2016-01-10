package de.tudresden.inf.lat.tabula.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

import de.tudresden.inf.lat.tabula.datatype.CompositeType;
import de.tudresden.inf.lat.tabula.datatype.CompositeTypeImpl;
import de.tudresden.inf.lat.tabula.datatype.PrimitiveTypeValue;
import de.tudresden.inf.lat.tabula.datatype.Record;
import de.tudresden.inf.lat.tabula.datatype.StringValue;
import de.tudresden.inf.lat.tabula.parser.SimpleFormatParser;
import de.tudresden.inf.lat.tabula.renderer.SimpleFormatRenderer;
import de.tudresden.inf.lat.tabula.table.Table;
import de.tudresden.inf.lat.tabula.table.TableImpl;
import de.tudresden.inf.lat.tabula.table.TableMap;
import de.tudresden.inf.lat.tabula.table.TableMapImpl;

/**
 * This is a test of modification of a Tabula file.
 */
public class MainTest {

	public static final String INPUT_FILE_NAME = "src/test/resources/example.properties";
	public static final String EXPECTED_OUTPUT_FILE_NAME = "src/test/resources/example-modified.properties";

	public static final String TYPE_NAME_RECORD = "record";
	public static final String FIELD_NAME_AUTHORS = "authors";
	public static final String FIELD_NAME_NUMBER_OF_AUTHORS = "numberOfAuthors";
	public static final String TYPE_OF_NUMBER_OF_AUTHORS = "String";
	public static final String NEW_LINE = "\n";

	/**
	 * Returns the number of authors for a given record.
	 * 
	 * @param record
	 *            record
	 * @return the number of authors for a given record
	 */
	StringValue computeFieldValue(Record record) {
		PrimitiveTypeValue value = record.get(FIELD_NAME_AUTHORS);
		int size = (value == null) ? 0 : value.renderAsList().size();
		return new StringValue("" + size);
	}

	@Test
	public void addNewFieldOldTest() throws IOException {

		// This is an example of source code where the number of authors is
		// a computed value

		// Read the table map
		TableMap oldTableMap = new SimpleFormatParser(new FileReader(INPUT_FILE_NAME)).parse();

		// Make a copy of the tableMap
		// TableMapImpl newTableMap = new TableMapImpl(oldTableMap);
		TableMapImpl newTableMap = new TableMapImpl();
		oldTableMap.getTableIds().forEach(tableId -> newTableMap.put(tableId, oldTableMap.getTable(tableId)));

		// Get the main table
		Table table = newTableMap.getTable(TYPE_NAME_RECORD);

		// Make a copy of the main table
		TableImpl newTable = new TableImpl(table);

		// Add the new table to the new table map
		newTableMap.put(TYPE_NAME_RECORD, newTable);

		// Get type of main table
		CompositeType oldType = table.getType();

		// Make a copy of type
		// CompositeTypeImpl newType = new CompositeTypeImpl(oldType);
		CompositeTypeImpl newType = new CompositeTypeImpl();
		oldType.getFields().forEach(field -> newType.declareField(field, oldType.getFieldType(field)));

		// Add new declaration with number of authors
		if (!newType.getFields().contains(FIELD_NAME_NUMBER_OF_AUTHORS)) {
			newType.declareField(FIELD_NAME_NUMBER_OF_AUTHORS, TYPE_OF_NUMBER_OF_AUTHORS);
		}

		// Update type of table
		newTable.setType(newType);

		// Compute the number of authors for each record
		table.getRecords().forEach(record -> record.set(FIELD_NAME_NUMBER_OF_AUTHORS, computeFieldValue(record)));

		// Store the new table map
		StringWriter writer = new StringWriter();
		SimpleFormatRenderer renderer = new SimpleFormatRenderer(writer);
		renderer.render(newTableMap);

		// Read the expected output
		StringBuffer sbuf = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(EXPECTED_OUTPUT_FILE_NAME));
		reader.lines().forEach(line -> sbuf.append(line + NEW_LINE));
		reader.close();

		// Compare the expected output with the actual output
		Assert.assertEquals(sbuf.toString(), writer.toString());
	}

}
