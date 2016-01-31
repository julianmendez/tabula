package de.tudresden.inf.lat.tabula.ext.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import de.tudresden.inf.lat.tabula.datatype.CompositeTypeImpl;
import de.tudresden.inf.lat.tabula.datatype.ParseException;
import de.tudresden.inf.lat.tabula.datatype.StringValue;
import de.tudresden.inf.lat.tabula.parser.Parser;
import de.tudresden.inf.lat.tabula.parser.ParserConstant;
import de.tudresden.inf.lat.tabula.table.RecordImpl;
import de.tudresden.inf.lat.tabula.table.TableImpl;
import de.tudresden.inf.lat.tabula.table.TableMap;
import de.tudresden.inf.lat.tabula.table.TableMapImpl;

/**
 * Parser of a table in comma-separated values format.
 * 
 */
public class CsvParser implements Parser {

	public static final char UNDERSCORE_CHAR = '_';
	public static final char COMMA_CHAR = ',';
	public static final char QUOTES_CHAR = '"';

	public static final String DEFAULT_TABLE_NAME = "defaultType";
	public static final String DEFAULT_FIELD_TYPE = "String";
	public static final String UNDERSCORE = "" + UNDERSCORE_CHAR;

	private Reader input = new InputStreamReader(System.in);

	/**
	 * Constructs a new parser.
	 *
	 * @param input
	 *            input
	 */
	public CsvParser(Reader input) {
		this.input = input;
	}

	public List<String> getColumns(String line0) {
		List<String> ret = new ArrayList<>();
		String line = Objects.isNull(line0) ? "" : line0.trim();
		StringBuffer current = new StringBuffer();
		boolean betweenQuotes = false;
		for (int index = 0; index < line.length(); index += 1) {
			char ch = line.charAt(index);
			if (ch == QUOTES_CHAR) {
				betweenQuotes = !betweenQuotes;
			} else if ((ch == COMMA_CHAR) && !betweenQuotes) {
				ret.add(current.toString());
				current = new StringBuffer();
			} else {
				current.append(ch);
			}
		}
		if (!current.toString().isEmpty()) {
			ret.add(current.toString());
		}
		return ret;
	}

	private TableImpl createSortedTable(List<String> fields) {
		CompositeTypeImpl tableType = new CompositeTypeImpl();
		fields.forEach(fieldName -> tableType.declareField(fieldName, DEFAULT_FIELD_TYPE));

		TableImpl ret = new TableImpl();
		ret.setType(tableType);
		return ret;
	}

	public String normalize(String fieldName) {
		String auxName = Objects.isNull(fieldName) ? UNDERSCORE : fieldName.trim();
		String name = auxName.isEmpty() ? UNDERSCORE : auxName;

		StringBuffer ret = new StringBuffer();
		IntStream.range(0, name.length()).forEach(index -> {
			char ch = name.charAt(index);
			if (!Character.isLetterOrDigit(ch)) {
				ch = UNDERSCORE_CHAR;
			}
			ret.append(ch);
		});
		return ret.toString();
	}

	public List<String> normalizeHeaders(List<String> headers, int lineCounter) {
		List<String> ret = new ArrayList<>();
		int idCount = 0;
		for (String header : headers) {
			String fieldName = normalize(header);
			if (fieldName.equals(ParserConstant.ID_KEYWORD)) {
				idCount += 1;
			}
			if (idCount > 1) {
				throw new ParseException("This cannot have two identifiers (field '" + ParserConstant.ID_KEYWORD
						+ "') (line " + lineCounter + ")");
			} else {
				ret.add(fieldName);
			}
		}
		return ret;
	}

	public TableMap parseMap(BufferedReader input) throws IOException {
		int lineCounter = 0;
		String line = input.readLine();
		lineCounter += 1;
		List<String> headers = getColumns(line);
		List<String> fieldNames = normalizeHeaders(headers, lineCounter);
		TableImpl currentTable = createSortedTable(fieldNames);

		while (Objects.nonNull(line)) {
			line = input.readLine();
			lineCounter += 1;
			if (Objects.nonNull(line) && !line.trim().isEmpty()) {
				List<String> columns = getColumns(line);
				if (columns.size() > fieldNames.size()) {
					throw new ParseException("Too many fields in line: " + columns.size() + " instead of "
							+ fieldNames.size() + " (line " + lineCounter + ")");
				}

				RecordImpl record = new RecordImpl();
				String currentId = null;
				int index = 0;
				for (String column : columns) {
					String field = fieldNames.get(index);
					if (field.equals(ParserConstant.ID_KEYWORD)) {
						currentId = column;
					}
					StringValue value = new StringValue(column);
					record.set(field, value);
					index += 1;
				}

				currentTable.add(record);
				if (Objects.nonNull(currentId)) {
					currentTable.addId(currentId);
				}
			}
		}

		TableMapImpl ret = new TableMapImpl();
		ret.put(DEFAULT_TABLE_NAME, currentTable);
		return ret;
	}

	@Override
	public TableMap parse() {
		try {
			return parseMap(new BufferedReader(this.input));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
