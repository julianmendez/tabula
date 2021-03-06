package de.tudresden.inf.lat.tabula.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

import de.tudresden.inf.lat.tabula.datatype.CompositeType;
import de.tudresden.inf.lat.tabula.datatype.CompositeTypeImpl;
import de.tudresden.inf.lat.tabula.datatype.ParameterizedListValue;
import de.tudresden.inf.lat.tabula.datatype.ParseException;
import de.tudresden.inf.lat.tabula.datatype.PrimitiveTypeFactory;
import de.tudresden.inf.lat.tabula.datatype.PrimitiveTypeValue;
import de.tudresden.inf.lat.tabula.datatype.Record;
import de.tudresden.inf.lat.tabula.datatype.StringValue;
import de.tudresden.inf.lat.tabula.datatype.URIType;
import de.tudresden.inf.lat.tabula.datatype.URIValue;
import de.tudresden.inf.lat.tabula.table.PrefixMap;
import de.tudresden.inf.lat.tabula.table.PrefixMapImpl;
import de.tudresden.inf.lat.tabula.table.RecordImpl;
import de.tudresden.inf.lat.tabula.table.TableImpl;
import de.tudresden.inf.lat.tabula.table.TableMap;
import de.tudresden.inf.lat.tabula.table.TableMapImpl;
import de.tudresden.inf.lat.util.map.OptMap;
import de.tudresden.inf.lat.util.map.OptMapImpl;

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
		Optional<String> result = Optional.empty();
		if (Objects.isNull(line)) {
			result = Optional.empty();
		} else {
			int pos = line.indexOf(ParserConstant.EQUALS_SIGN);
			if (pos == -1) {
				result = Optional.of(line);
			} else {
				result = Optional.of(line.substring(0, pos).trim());
			}
		}
		return result;
	}

	public boolean hasKey(String line, String key) {
		Optional<String> optKey = getKey(line);
		boolean result = (optKey.isPresent() && optKey.get().equals(key));
		return result;
	}

	public Optional<String> getValue(String line) {
		Optional<String> result = Optional.empty();
		if (Objects.isNull(line)) {
			result = Optional.empty();
		} else {
			int pos = line.indexOf(ParserConstant.EQUALS_SIGN);
			if (pos == -1) {
				result = Optional.of("");
			} else {
				result = Optional.of(line.substring(pos + ParserConstant.EQUALS_SIGN.length(), line.length()).trim());
			}
		}
		return result;
	}

	private CompositeTypeImpl parseTypes(String line, int lineCounter) {
		CompositeTypeImpl result = new CompositeTypeImpl();
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
					result.declareField(key, value);
				} else {
					throw new ParseException("Type '" + value + "' is undefined. (line " + lineCounter + ")");
				}
			}
		}
		return result;
	}

	private URI asUri(String uriStr, int lineCounter) {
		URI result = null;
		try {
			result = new URI(uriStr);
		} catch (URISyntaxException e) {
			throw new ParseException("String '" + uriStr + "' is not a valid URI. (line " + lineCounter + ")");
		}
		return result;
	}

	public PrefixMap parsePrefixMap(String line, int lineCounter) {
		PrefixMap ret = new PrefixMapImpl();
		StringTokenizer stok = new StringTokenizer(getValue(line).get());
		while (stok.hasMoreTokens()) {
			String token = stok.nextToken();
			int pos = token.indexOf(ParserConstant.PREFIX_SIGN);
			if (pos == -1) {
				throw new ParseException(
						"Prefix '" + line + "' does not have a definition. (line " + lineCounter + ")");
			} else {
				String key = token.substring(0, pos);
				String value = token.substring((pos + ParserConstant.PREFIX_SIGN.length()), token.length());
				ret.put(asUri(key, lineCounter), asUri(value, lineCounter));
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

	public boolean isPrefixMapDefinition(String line) {
		return Objects.nonNull(line) && line.trim().startsWith(ParserConstant.PREFIX_MAP_TOKEN);
	}

	public boolean isSortingOrderDeclaration(String line) {
		return Objects.nonNull(line) && line.trim().startsWith(ParserConstant.SORTING_ORDER_DECLARATION_TOKEN);
	}

	public boolean isNewRecord(String line) {
		return Objects.nonNull(line) && line.trim().startsWith(ParserConstant.NEW_RECORD_TOKEN);
	}

	private PrimitiveTypeValue getTypedValue(String key, String value, CompositeType type0, PrefixMap prefixMap,
			int lineCounter) {
		PrimitiveTypeValue result = new StringValue();
		if (Objects.isNull(key)) {
			result = new StringValue();
		} else {
			try {
				Optional<String> optTypeStr = type0.getFieldType(key);
				if (optTypeStr.isPresent()) {
					String typeStr = optTypeStr.get();
					result = (new PrimitiveTypeFactory()).newInstance(typeStr, value);
					if (result.getType().equals(new URIType())) {
						URIValue uri = (URIValue) result;
						result = new URIValue(prefixMap.getWithoutPrefix(uri.getUri()));
					} else if (result instanceof ParameterizedListValue) {
						ParameterizedListValue list = (ParameterizedListValue) result;
						if (list.getParameter().equals(new URIType())) {
							ParameterizedListValue newList = new ParameterizedListValue(new URIType());
							list.forEach(elem -> {
								URIValue uri = (URIValue) elem;
								newList.add(new URIValue(prefixMap.getWithoutPrefix(uri.getUri())));
							});
							result = newList;
						}
					}
					return result;
				} else {
					throw new ParseException("Key '" + key + "' has an undefined type.");
				}
			} catch (ParseException e) {
				throw new ParseException(e.getMessage() + " (line " + lineCounter + ")", e.getCause());
			}
		}
		return result;
	}

	public boolean isMultiLine(String line) {
		return line.trim().endsWith(ParserConstant.LINE_CONTINUATION_SYMBOL);
	}

	public String getCleanLine(String line) {
		String result = "";
		String trimmedLine = line.trim();
		if (isMultiLine(line)) {
			result = trimmedLine.substring(0, trimmedLine.length() - ParserConstant.LINE_CONTINUATION_SYMBOL.length())
					.trim();
		} else {
			result = trimmedLine;
		}
		return result;
	}

	public Pair readMultiLine(BufferedReader input, int lineCounter0) throws IOException {
		Pair result = null;
		int lineCounter = lineCounter0;
		String line = input.readLine();
		if (Objects.isNull(line)) {
			result = new Pair(lineCounter, null);
		} else {
			lineCounter += 1;
			if (line.startsWith(ParserConstant.COMMENT_SYMBOL)) {
				result = new Pair(lineCounter, "");
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

				result = new Pair(lineCounter, sb.toString());
			}
		}
		return result;
	}

	private boolean isIdProperty(String line) {
		boolean result = false;
		Optional<String> optKey = getKey(line);
		if (optKey.isPresent()) {
			result = optKey.get().equals(ParserConstant.ID_KEYWORD);
		} else {
			result = false;
		}
		return result;
	}

	private Optional<String> getIdProperty(String line) {
		Optional<String> result = Optional.empty();
		Optional<String> optKey = getKey(line);
		Optional<String> optValueStr = getValue(line);
		if (optKey.isPresent() && optValueStr.isPresent() && optKey.get().equals(ParserConstant.ID_KEYWORD)) {
			result = Optional.of(optValueStr.get());
		} else {
			result = Optional.empty();
		}
		return result;
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
			PrimitiveTypeValue value = getTypedValue(key, valueStr, currentTable.getType(), currentTable.getPrefixMap(),
					lineCounter);
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
		OptMap<String, TableImpl> mapOfTables = new OptMapImpl<>(new TreeMap<>());
		OptMap<String, Set<String>> mapOfRecordIdsOfTables = new OptMapImpl<>(new TreeMap<>());

		String line = "";
		TableImpl currentTable = new TableImpl();
		Set<String> recordIdsOfCurrentTable = Collections.emptySet();
		Optional<String> optCurrentId = Optional.empty();
		Record record = new RecordImpl();
		int lineCounter = 0;
		boolean isDefiningType = false;

		while (Objects.nonNull(line)) {
			Pair pair = readMultiLine(input, lineCounter);
			line = pair.getLine();
			lineCounter = pair.getLineCounter();
			if (Objects.nonNull(line) && !line.trim().isEmpty()) {
				if (hasKey(line, ParserConstant.TYPE_SELECTION_TOKEN)) {
					isDefiningType = true;
					Optional<String> optTableName = getValue(line);
					if (optTableName.isPresent()) {
						String tableName = optTableName.get();
						if (!mapOfTables.containsKey(tableName)) {
							mapOfTables.put(tableName, new TableImpl());
							mapOfRecordIdsOfTables.put(tableName, new TreeSet<>());
						}
						currentTable = mapOfTables.get(tableName).get();
						recordIdsOfCurrentTable = mapOfRecordIdsOfTables.get(tableName).get();
					}

				} else if (isDefiningType && hasKey(line, ParserConstant.TYPE_DEFINITION_TOKEN)) {
					currentTable.setType(parseTypes(line, lineCounter));

				} else if (isDefiningType && hasKey(line, ParserConstant.PREFIX_MAP_TOKEN)) {
					currentTable.setPrefixMap(parsePrefixMap(line, lineCounter));

				} else if (isDefiningType && hasKey(line, ParserConstant.SORTING_ORDER_DECLARATION_TOKEN)) {
					setSortingOrder(line, currentTable);

				} else if (hasKey(line, ParserConstant.NEW_RECORD_TOKEN)) {
					isDefiningType = false;
					record = new RecordImpl();
					currentTable.add(record);
					optCurrentId = Optional.empty();

				} else {
					parseProperty(line, currentTable, recordIdsOfCurrentTable, record, lineCounter);
					if (isIdProperty(line)) {
						boolean successful = false;
						if (!optCurrentId.isPresent()) {
							optCurrentId = getIdProperty(line);
							if (optCurrentId.isPresent()) {
								successful = recordIdsOfCurrentTable.add(optCurrentId.get());
							}
						}
						if (!successful) {
							throw new ParseException("Identifier has been already defined ('" + optCurrentId.get()
									+ "') (line " + lineCounter + ")");
						}
					}

				}
			}
		}

		TableMapImpl result = new TableMapImpl();
		mapOfTables.keySet().forEach(key -> result.put(key, mapOfTables.get(key).get()));
		return result;
	}

	@Override
	public TableMap parse() {
		TableMap result = null;
		try {
			result = parseMap(new BufferedReader(this.input));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

}
