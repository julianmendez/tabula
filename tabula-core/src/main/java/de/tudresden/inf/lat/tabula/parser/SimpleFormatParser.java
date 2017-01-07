package de.tudresden.inf.lat.tabula.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

		public Pair(int lineCounter0, String line0) {
			this.line = line0;
			this.lineCounter = lineCounter0;
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

	public Optional<String> getKey(String line) {
		if (Objects.isNull(line)) {
			return Optional.empty();
		} else {
			int pos = line.indexOf(ParserConstant.EQUALS_SIGN);
			if (pos == -1) {
				return Optional.of(line);
			} else {
				return Optional.of(line.substring(0, pos).trim());
			}
		}
	}

	public Optional<String> getValue(String line) {
		if (Objects.isNull(line)) {
			return Optional.empty();
		} else {
			int pos = line.indexOf(ParserConstant.EQUALS_SIGN);
			if (pos == -1) {
				return Optional.of("");
			} else {
				return Optional.of(line.substring(pos + ParserConstant.EQUALS_SIGN.length(), line.length()).trim());
			}
		}
	}

	private CompositeTypeImpl parseTypes(String line, int lineCounter) {
		CompositeTypeImpl ret = new CompositeTypeImpl();
		StringTokenizer stok = new StringTokenizer(getValue(line).get());
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
		StringTokenizer stok = new StringTokenizer(getValue(line).get());
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
		return Objects.nonNull(line) && line.trim().startsWith(ParserConstant.TYPE_SELECTION_TOKEN);
	}

	public boolean isTypeDefinition(String line) {
		return Objects.nonNull(line) && line.trim().startsWith(ParserConstant.TYPE_DEFINITION_TOKEN);
	}

	public boolean isSortingOrderDeclaration(String line) {
		return Objects.nonNull(line) && line.trim().startsWith(ParserConstant.SORTING_ORDER_DECLARATION_TOKEN);
	}

	public boolean isNewRecord(String line) {
		return Objects.nonNull(line) && line.trim().startsWith(ParserConstant.NEW_RECORD_TOKEN);
	}

	private PrimitiveTypeValue getTypedValue(String key, String value, CompositeType type0, int lineCounter) {
		if (Objects.isNull(key)) {
			return new StringValue();
		} else {
			try {
				Optional<String> optTypeStr = type0.getFieldType(key);
				if (optTypeStr.isPresent()) {
					return (new PrimitiveTypeFactory()).newInstance(optTypeStr.get(), value);
				} else {
					throw new ParseException("Key '" + key + "' has an undefined type.");
				}
			} catch (ParseException e) {
				throw new ParseException(e.getMessage() + " (line " + lineCounter + ")", e.getCause());
			}
		}
	}

	public boolean isMultiLine(String line) {
		return line.trim().endsWith(ParserConstant.LINE_CONTINUATION_SYMBOL);
	}

	public String getCleanLine(String line) {
		String trimmedLine = line.trim();
		if (isMultiLine(line)) {
			return trimmedLine.substring(0, trimmedLine.length() - ParserConstant.LINE_CONTINUATION_SYMBOL.length())
					.trim();
		} else {
			return trimmedLine;
		}
	}

	public Pair readMultiLine(BufferedReader input, int lineCounter0) throws IOException {
		int lineCounter = lineCounter0;
		String line = input.readLine();
		if (Objects.isNull(line)) {
			return new Pair(lineCounter, null);
		} else {
			lineCounter += 1;
			if (line.startsWith(ParserConstant.COMMENT_SYMBOL)) {
				return new Pair(lineCounter, "");
			} else {
				StringBuilder sb = new StringBuilder();
				while (isMultiLine(line)) {
					sb.append(getCleanLine(line));
					sb.append(ParserConstant.SPACE);
					line = input.readLine();
					if (Objects.nonNull(line)) {
						lineCounter += 1;
					}
				}
				sb.append(getCleanLine(line));

				return new Pair(lineCounter, sb.toString());
			}
		}
	}

	private boolean isIdProperty(String line) {
		Optional<String> optKey = getKey(line);
		if (optKey.isPresent()) {
			return optKey.get().equals(ParserConstant.ID_KEYWORD);
		} else {
			return false;
		}
	}

	private Optional<String> getIdProperty(String line) {
		Optional<String> optKey = getKey(line);
		Optional<String> optValueStr = getValue(line);
		if (optKey.isPresent() && optValueStr.isPresent() && optKey.get().equals(ParserConstant.ID_KEYWORD)) {
			return Optional.of(optValueStr.get());
		} else {
			return Optional.empty();
		}
	}

	private void parseProperty(String line, TableImpl currentTable, Set<String> recordIdsOfCurrentTable, Record record,
			int lineCounter) {
		if (Objects.isNull(currentTable)) {
			throw new ParseException("New record was not declared (line " + lineCounter + ")");
		}

		Optional<String> optKey = getKey(line);
		Optional<String> optValueStr = getValue(line);
		if (optKey.isPresent() && optValueStr.isPresent()) {
			String key = optKey.get();
			String valueStr = optValueStr.get();
			PrimitiveTypeValue value = getTypedValue(key, valueStr, currentTable.getType(), lineCounter);
			if (key.equals(ParserConstant.ID_KEYWORD)) {
				if (recordIdsOfCurrentTable.contains(valueStr)) {
					throw new ParseException("Identifier '" + ParserConstant.ID_KEYWORD + ParserConstant.SPACE
							+ ParserConstant.EQUALS_SIGN + ParserConstant.SPACE + optValueStr.get()
							+ "' is duplicated (line " + lineCounter + ").");
				}
			}
			record.set(key, value);
		}
	}

	public TableMap parseMap(BufferedReader input) throws IOException {
		Map<String, TableImpl> mapOfTables = new TreeMap<>();
		Map<String, Set<String>> mapOfRecordIdsOfTables = new TreeMap<>();

		String line = "";
		TableImpl currentTable = null;
		Set<String> recordIdsOfCurrentTable = null;
		String currentId = null;
		Record record = null;
		int lineCounter = 0;

		while (Objects.nonNull(line)) {
			Pair pair = readMultiLine(input, lineCounter);
			line = pair.getLine();
			lineCounter = pair.getLineCounter();
			if (Objects.nonNull(line) && !line.trim().isEmpty()) {
				if (isTypeSelection(line)) {

					Optional<String> optTableName = getValue(line);
					if (optTableName.isPresent()) {
						String tableName = optTableName.get();
						if (!mapOfTables.containsKey(tableName)) {
							mapOfTables.put(tableName, new TableImpl());
							mapOfRecordIdsOfTables.put(tableName, new TreeSet<>());
						}
						currentTable = mapOfTables.get(tableName);
						recordIdsOfCurrentTable = mapOfRecordIdsOfTables.get(tableName);
					}

				} else if (isTypeDefinition(line)) {
					currentTable.setType(parseTypes(line, lineCounter));

				} else if (isSortingOrderDeclaration(line)) {
					setSortingOrder(line, currentTable);

				} else if (isNewRecord(line)) {
					record = new RecordImpl();
					currentTable.add(record);
					currentId = null;

				} else {
					parseProperty(line, currentTable, recordIdsOfCurrentTable, record, lineCounter);
					if (isIdProperty(line)) {
						boolean successful = false;
						if (Objects.isNull(currentId)) {
							Optional<String> optCurrentId = getIdProperty(line);
							if (optCurrentId.isPresent()) {
								currentId = optCurrentId.get();
								successful = recordIdsOfCurrentTable.add(currentId);
							}
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
		mapOfTables.keySet().forEach(key -> ret.put(key, mapOfTables.get(key)));
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
