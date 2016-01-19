package de.tudresden.inf.lat.tabula.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

import de.tudresden.inf.lat.tabula.datatype.CompositeType;
import de.tudresden.inf.lat.tabula.datatype.CompositeTypeImpl;
import de.tudresden.inf.lat.tabula.datatype.ParseException;
import de.tudresden.inf.lat.tabula.datatype.PrimitiveTypeFactory;
import de.tudresden.inf.lat.tabula.datatype.PrimitiveTypeValue;
import de.tudresden.inf.lat.tabula.datatype.Record;
import de.tudresden.inf.lat.tabula.datatype.StringValue;
import de.tudresden.inf.lat.tabula.table.RecordImpl;
import de.tudresden.inf.lat.tabula.table.TableImpl;
import de.tudresden.inf.lat.tabula.table.TableMap;
import de.tudresden.inf.lat.tabula.table.TableMapImpl;

/**
 * Parser of a table in simple format.
 * 
 */
public class SimpleFormatParser implements Parser {

	private Reader input = new InputStreamReader(System.in);

	private class Pair {

		private String line;
		private int lineCounter;

		public Pair(int lineCounter, String line) {
			this.line = line;
			this.lineCounter = lineCounter;
		}

		public String getLine() {
			return this.line;
		}

		public int getLineCounter() {
			return this.lineCounter;
		}
	}

	public SimpleFormatParser(Reader input) {
		this.input = input;
	}

	public String getKey(String line) {
		if (line == null) {
			return null;
		} else {
			int pos = line.indexOf(ParserConstant.EQUALS_SIGN);
			if (pos == -1) {
				return line;
			} else {
				return line.substring(0, pos).trim();
			}
		}
	}

	public String getValue(String line) {
		if (line == null) {
			return null;
		} else {
			int pos = line.indexOf(ParserConstant.EQUALS_SIGN);
			if (pos == -1) {
				return "";
			} else {
				return line.substring(pos + ParserConstant.EQUALS_SIGN.length(), line.length()).trim();
			}
		}
	}

	private CompositeTypeImpl parseTypes(String line, int lineCounter) {
		CompositeTypeImpl ret = new CompositeTypeImpl();
		StringTokenizer stok = new StringTokenizer(getValue(line));
		PrimitiveTypeFactory factory = new PrimitiveTypeFactory();
		while (stok.hasMoreTokens()) {
			String token = stok.nextToken();
			int pos = token.indexOf(ParserConstant.TYPE_SIGN);
			if (pos == -1) {
				throw new ParseException("Field '" + line + "' does not have a type. (line " + lineCounter + ")");
			} else {
				String key = token.substring(0, pos);
				String value = token.substring((pos + ParserConstant.TYPE_SIGN.length()), token.length());
				if (factory.contains(value)) {
					ret.declareField(key, value);
				} else {
					throw new ParseException("Type '" + value + "' is undefined. (line " + lineCounter + ")");
				}
			}
		}
		return ret;
	}

	private void setSortingOrder(String line, TableImpl table) {
		Set<String> fieldsWithReverseOrder = new TreeSet<>();
		List<String> list = new ArrayList<>();
		StringTokenizer stok = new StringTokenizer(getValue(line));
		while (stok.hasMoreTokens()) {
			String token = stok.nextToken();
			if (token.startsWith(ParserConstant.STANDARD_ORDER_SIGN)) {
				token = token.substring(ParserConstant.STANDARD_ORDER_SIGN.length());
			} else if (token.startsWith(ParserConstant.REVERSE_ORDER_SIGN)) {
				token = token.substring(ParserConstant.REVERSE_ORDER_SIGN.length());
				fieldsWithReverseOrder.add(token);
			}
			list.add(token);
		}
		table.setSortingOrder(list);
		table.setFieldsWithReverseOrder(fieldsWithReverseOrder);
	}

	public boolean isTypeSelection(String line) {
		return (line != null) && line.trim().startsWith(ParserConstant.TYPE_SELECTION_TOKEN);
	}

	public boolean isTypeDefinition(String line) {
		return (line != null) && line.trim().startsWith(ParserConstant.TYPE_DEFINITION_TOKEN);
	}

	public boolean isSortingOrderDeclaration(String line) {
		return (line != null) && line.trim().startsWith(ParserConstant.SORTING_ORDER_DECLARATION_TOKEN);
	}

	public boolean isNewRecord(String line) {
		return (line != null) && line.trim().startsWith(ParserConstant.NEW_RECORD_TOKEN);
	}

	private PrimitiveTypeValue getTypedValue(String key, String value, CompositeType type0, int lineCounter) {
		if (key == null) {
			return new StringValue();
		} else {
			try {
				String typeStr = type0.getFieldType(key);
				if (typeStr == null) {
					throw new ParseException("Key '" + key + "' has an undefined type.");
				} else {
					PrimitiveTypeValue ret = (new PrimitiveTypeFactory()).newInstance(typeStr, value);
					return ret;
				}
			} catch (ParseException e) {
				throw new ParseException(e.getMessage() + " (line " + lineCounter + ")", e.getCause());
			}
		}
	}

	private Pair readMultiLine(BufferedReader input, int lineCounter0) throws IOException {
		int lineCounter = lineCounter0;
		String line = input.readLine();
		if (line == null) {
			return new Pair(lineCounter, null);
		} else {
			lineCounter += 1;
			if (line.startsWith(ParserConstant.COMMENT_SYMBOL)) {
				return new Pair(lineCounter, "");
			} else {
				String multiLine = line.trim();
				while (multiLine.endsWith(ParserConstant.LINE_CONTINUATION_SYMBOL)) {
					multiLine = multiLine.substring(0,
							multiLine.length() - ParserConstant.LINE_CONTINUATION_SYMBOL.length())
							+ ParserConstant.SPACE;
					line = input.readLine();
					if (line != null) {
						line = line.trim();
						lineCounter += 1;
						multiLine += line;
					}
				}
				return new Pair(lineCounter, multiLine);
			}
		}
	}

	private boolean isIdProperty(String line) {
		String key = getKey(line);
		return key.equals(ParserConstant.ID_KEYWORD);
	}

	private String getIdProperty(String line) {
		String key = getKey(line);
		String valueStr = getValue(line);
		if (key.equals(ParserConstant.ID_KEYWORD)) {
			return valueStr;
		} else {
			return null;
		}
	}

	private void parseProperty(String line, TableImpl currentTable, Record record, int lineCounter) {
		if (currentTable == null) {
			throw new ParseException("New record was not declared (line " + lineCounter + ")");
		}

		String key = getKey(line);
		String valueStr = getValue(line);
		PrimitiveTypeValue value = getTypedValue(key, valueStr, currentTable.getType(), lineCounter);
		if (key.equals(ParserConstant.ID_KEYWORD)) {
			if (currentTable.getIdentifiers().contains(valueStr)) {
				throw new ParseException(
						"Identifier '" + ParserConstant.ID_KEYWORD + ParserConstant.SPACE + ParserConstant.EQUALS_SIGN
								+ ParserConstant.SPACE + valueStr + "' is duplicated (line " + lineCounter + ").");
			}
		}
		record.set(key, value);
	}

	public TableMap parseMap(BufferedReader input) throws IOException {
		Map<String, TableImpl> map = new TreeMap<>();

		String line = "";
		TableImpl currentTable = null;
		String currentId = null;
		Record record = null;
		int lineCounter = 0;
		while (line != null) {
			Pair pair = readMultiLine(input, lineCounter);
			line = pair.getLine();
			lineCounter = pair.getLineCounter();
			if (line != null && !line.trim().isEmpty()) {
				if (isTypeSelection(line)) {
					String tableName = getValue(line);
					if (!map.containsKey(tableName)) {
						map.put(tableName, new TableImpl());
					}
					currentTable = map.get(tableName);

				} else if (isTypeDefinition(line)) {
					currentTable.setType(parseTypes(line, lineCounter));

				} else if (isSortingOrderDeclaration(line)) {
					setSortingOrder(line, currentTable);

				} else if (isNewRecord(line)) {
					record = new RecordImpl();
					currentTable.add(record);
					currentId = null;

				} else {
					parseProperty(line, currentTable, record, lineCounter);
					if (isIdProperty(line)) {
						boolean successful = false;
						if (currentId == null) {
							currentId = getIdProperty(line);
							successful = currentTable.addId(currentId);
						}
						if (!successful) {
							throw new ParseException("Identifier has been already defined ('" + currentId + "') (line "
									+ lineCounter + ")");
						}
					}

				}
			}
		}

		TableMapImpl ret = new TableMapImpl();
		map.keySet().forEach(key -> ret.put(key, map.get(key)));
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
