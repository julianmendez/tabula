package de.tudresden.inf.lat.tabula.ext.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;
import java.util.TreeMap;

import de.tudresden.inf.lat.tabula.datatype.CompositeType;
import de.tudresden.inf.lat.tabula.datatype.ParseException;
import de.tudresden.inf.lat.tabula.datatype.PrimitiveTypeFactory;
import de.tudresden.inf.lat.tabula.datatype.PrimitiveTypeValue;
import de.tudresden.inf.lat.tabula.datatype.Record;
import de.tudresden.inf.lat.tabula.datatype.SimplifiedCompositeType;
import de.tudresden.inf.lat.tabula.datatype.StringValue;
import de.tudresden.inf.lat.tabula.parser.Parser;
import de.tudresden.inf.lat.tabula.table.RecordImpl;
import de.tudresden.inf.lat.tabula.table.TableImpl;
import de.tudresden.inf.lat.tabula.table.TableMap;
import de.tudresden.inf.lat.tabula.table.TableMapImpl;

/**
 * Parser of a calendar.
 * 
 */
public class CalendarParser implements Parser {

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

	public static final String GENERATED_ID_FIELD_NAME = "generatedId";
	public static final String SUB_ITEMS_FIELD_NAME = "subItems";
	public static final String CALENDAR_TYPE_LABEL = "VCALENDAR";
	public static final String TIME_ZONE_TYPE_LABEL = "VTIMEZONE";
	public static final String DAYLIGHT_TYPE_LABEL = "DAYLIGHT";
	public static final String STANDARD_TYPE_LABEL = "STANDARD";
	public static final String EVENT_TYPE_LABEL = "VEVENT";
	public static final String ALARM_TYPE_LABEL = "VALARM";

	public static final String[] CALENDAR_TYPE_FIELDS = { GENERATED_ID_FIELD_NAME, SUB_ITEMS_FIELD_NAME, "PRODID",
			"VERSION", "CALSCALE", "METHOD", "X-WR-CALNAME", "X-WR-TIMEZONE", };

	public static final String[] TIME_ZONE_TYPE_FIELDS = { GENERATED_ID_FIELD_NAME, SUB_ITEMS_FIELD_NAME, "TZID",
			"X-LIC-LOCATION" };

	public static final String[] DAYLIGHT_TYPE_FIELDS = { GENERATED_ID_FIELD_NAME, SUB_ITEMS_FIELD_NAME, "TZOFFSETFROM",
			"TZOFFSETTO", "TZNAME", "DTSTART", "RRULE" };

	public static final String[] STANDARD_TYPE_FIELDS = DAYLIGHT_TYPE_FIELDS;

	public static final String[] EVENT_TYPE_FIELDS = { GENERATED_ID_FIELD_NAME, SUB_ITEMS_FIELD_NAME, "DTSTART",
			"DTEND", "RRULE", "ORGANIZER", "DTSTAMP", "UID", "ATTENDEE", "CREATED", "DESCRIPTION", "LAST-MODIFIED",
			"LOCATION", "SEQUENCE", "STATUS", "SUMMARY", "TRANSP", "X-ALT-DESC", "X-MICROSOFT-CDO-BUSYSTATUS",
			"CLASS" };

	public static final String[] ALARM_TYPE_FIELDS = { GENERATED_ID_FIELD_NAME, SUB_ITEMS_FIELD_NAME, "ACTION",
			"DESCRIPTION", "SUMMARY", "ATTENDEE", "TRIGGER" };

	public static final SimplifiedCompositeType EventTyp = null;

	public static final char UNDERSCORE_CHAR = '_';
	public static final char COMMA_CHAR = ',';
	public static final char QUOTES_CHAR = '"';
	public static final char COLON_CHAR = ':';
	public static final char SEMICOLON_CHAR = ';';
	public static final char SPACE_CHAR = ' ';
	public static final char NEW_LINE_CHAR = '\n';
	public static final char GENERATED_ID_SEPARATOR_CHAR = '.';
	public static final int FIRST_GENERATED_INDEX = 0;

	public static final String UNDERSCORE = "" + UNDERSCORE_CHAR;

	public static final String NEW_EVENT = "BEGIN:" + EVENT_TYPE_LABEL;
	public static final String BEGIN_KEYWORD = "BEGIN";
	public static final String END_KEYWORD = "END";

	private Reader input = new InputStreamReader(System.in);

	public CalendarParser(Reader input) {
		this.input = input;
	}

	public Optional<String> getKey(String line) {
		if (Objects.isNull(line)) {
			return Optional.empty();
		} else {
			int pos = line.indexOf(COLON_CHAR);
			if (pos == -1) {
				return Optional.of(line);
			} else {
				int pos2 = line.indexOf(SEMICOLON_CHAR);
				if (pos2 >= 0 && pos2 < pos) {
					pos = pos2;
				}
				return Optional.of(line.substring(0, pos).trim());
			}
		}
	}

	public Optional<String> getValue(String line) {
		if (Objects.isNull(line)) {
			return Optional.empty();
		} else {
			int pos = line.indexOf(COLON_CHAR);
			if (pos == -1) {
				return Optional.of("");
			} else {
				return Optional.of(line.substring(pos + 1, line.length()).trim());
			}
		}
	}

	public boolean isBeginLine(String line) {
		return Objects.nonNull(line) && line.trim().startsWith(BEGIN_KEYWORD);
	}

	public boolean isEndLine(String line) {
		return Objects.nonNull(line) && line.trim().startsWith(END_KEYWORD);
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

	private List<Pair> preload(BufferedReader input) {
		List<Pair> ret = new ArrayList<>();
		StringBuffer[] sbuf = new StringBuffer[1];
		sbuf[0] = new StringBuffer();
		int[] lineCounter = new int[1];
		lineCounter[0] = 0;
		input.lines().forEach(line -> {
			if (line.startsWith("" + SPACE_CHAR)) {
				sbuf[0].append(line);
			} else {
				ret.add(new Pair(lineCounter[0], sbuf[0].toString()));
				sbuf[0] = new StringBuffer();
				sbuf[0].append(line);
			}
			lineCounter[0] += 1;
		});
		return ret;
	}

	private void parseProperty(String line, TableImpl currentTable, Record record, int lineCounter) {
		if (Objects.isNull(currentTable)) {
			throw new ParseException("New record was not declared (line " + lineCounter + ")");
		}

		Optional<String> optKey = getKey(line);
		Optional<String> optValueStr = getValue(line);
		if (optKey.isPresent() && optValueStr.isPresent()) {
			String key = optKey.get();
			String valueStr = optValueStr.get();
			PrimitiveTypeValue value = getTypedValue(key, valueStr, currentTable.getType(), lineCounter);
			record.set(key, value);
		}
	}

	public String getGeneratedId(List<Integer> generatedIds, int level) {
		while (level >= generatedIds.size()) {
			generatedIds.add(FIRST_GENERATED_INDEX);
		}
		int newValue = generatedIds.get(level) + 1;
		while (level < generatedIds.size()) {
			generatedIds.remove(generatedIds.size() - 1);
		}
		generatedIds.add(newValue);
		StringBuffer sbuf = new StringBuffer();
		boolean firstTime = true;
		for (Integer counter : generatedIds) {
			if (firstTime) {
				firstTime = false;
			} else {
				sbuf.append(GENERATED_ID_SEPARATOR_CHAR);
			}
			sbuf.append(counter);
		}
		return sbuf.toString();
	}

	public TableMap parseMap(BufferedReader input) throws IOException {
		Map<String, TableImpl> map = new TreeMap<>();

		map.put(CALENDAR_TYPE_LABEL, new TableImpl(new SimplifiedCompositeType(CALENDAR_TYPE_FIELDS)));
		map.put(TIME_ZONE_TYPE_LABEL, new TableImpl(new SimplifiedCompositeType(TIME_ZONE_TYPE_FIELDS)));
		map.put(DAYLIGHT_TYPE_LABEL, new TableImpl(new SimplifiedCompositeType(DAYLIGHT_TYPE_FIELDS)));
		map.put(STANDARD_TYPE_LABEL, new TableImpl(new SimplifiedCompositeType(STANDARD_TYPE_FIELDS)));
		map.put(EVENT_TYPE_LABEL, new TableImpl(new SimplifiedCompositeType(EVENT_TYPE_FIELDS)));
		map.put(ALARM_TYPE_LABEL, new TableImpl(new SimplifiedCompositeType(ALARM_TYPE_FIELDS)));

		TableImpl currentTable = null;
		Record currentRecord = null;
		String currentTableId = null;

		Stack<String> tableIdStack = new Stack<>();
		Stack<Record> recordStack = new Stack<>();
		Stack<TableImpl> tableStack = new Stack<>();
		List<Integer> generatedIds = new ArrayList<>();

		List<Pair> lines = preload(input);
		int lineCounter = 0;
		boolean firstTime = true;
		for (Pair pair : lines) {
			String line = pair.getLine();
			lineCounter = pair.getLineCounter();
			if (Objects.nonNull(line) && !line.trim().isEmpty()) {
				if (isBeginLine(line)) {
					String value = getValue(line).get();
					if (firstTime) {
						firstTime = false;
					} else {
						tableIdStack.push(currentTableId);
						tableStack.push(currentTable);
						recordStack.push(currentRecord);
					}
					currentRecord = new RecordImpl();
					currentRecord.set(GENERATED_ID_FIELD_NAME,
							new StringValue(getGeneratedId(generatedIds, tableIdStack.size())));
					currentTableId = value;
					currentTable = map.get(value);
					if (Objects.isNull(currentTable)) {
						throw new ParseException("Unknown type '" + value + "' (line " + lineCounter + ").");
					}

				} else if (isEndLine(line)) {
					String foreignKey = currentRecord.get(GENERATED_ID_FIELD_NAME).get().render();
					currentTable.add(currentRecord);
					String value = getValue(line).get();
					if (Objects.isNull(map.get(value))) {
						throw new ParseException("Unknown type '" + value + "' (line " + lineCounter + ").");
					}
					if (!value.equals(currentTableId)) {
						throw new ParseException("Closing wrong type '" + value + "' (line " + lineCounter + ").");
					}
					if (tableStack.isEmpty()) {
						throw new ParseException("Too many " + END_KEYWORD + " keywords  (line " + lineCounter + ").");
					}
					currentTableId = tableIdStack.pop();
					currentTable = tableStack.pop();
					currentRecord = recordStack.pop();
					Optional<PrimitiveTypeValue> optSubItems = currentRecord.get(SUB_ITEMS_FIELD_NAME);
					if (optSubItems.isPresent()) {
						currentRecord.set(SUB_ITEMS_FIELD_NAME,
								new StringValue(optSubItems.get().render() + SPACE_CHAR + foreignKey));
					} else {
						currentRecord.set(SUB_ITEMS_FIELD_NAME, new StringValue(foreignKey));
					}

				} else {
					parseProperty(line, currentTable, currentRecord, lineCounter);

				}
			}
		}

		if (Objects.nonNull(currentTable) && Objects.nonNull(currentRecord)) {
			currentTable.add(currentRecord);
		}

		if (!tableStack.isEmpty()) {
			throw new ParseException("Too few " + END_KEYWORD + " keywords  (line " + lineCounter + ").");
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
