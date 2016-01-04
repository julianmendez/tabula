package de.tudresden.inf.lat.tabula.ext.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
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
import de.tudresden.inf.lat.tabula.parser.ParserConstant;
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

	public static final String GeneratedIdFieldName = "generatedId";
	public static final String SubItemsFieldName = "subItems";
	public static final String CalendarTypeLabel = "VCALENDAR";
	public static final String TimeZoneTypeLabel = "VTIMEZONE";
	public static final String DaylightTypeLabel = "DAYLIGHT";
	public static final String StandardTypeLabel = "STANDARD";
	public static final String EventTypeLabel = "VEVENT";
	public static final String AlarmTypeLabel = "VALARM";

	public static final String[] CalendarTypeFields = { GeneratedIdFieldName,
			SubItemsFieldName, "PRODID", "VERSION", "CALSCALE", "METHOD",
			"X-WR-CALNAME", "X-WR-TIMEZONE", };

	public static final String[] TimeZoneTypeFields = { GeneratedIdFieldName,
			SubItemsFieldName, "TZID", "X-LIC-LOCATION" };

	public static final String[] DaylightTypeFields = { GeneratedIdFieldName,
			SubItemsFieldName, "TZOFFSETFROM", "TZOFFSETTO", "TZNAME",
			"DTSTART", "RRULE" };

	public static final String[] StandardTypeFields = DaylightTypeFields;

	public static final String[] EventTypeFields = { GeneratedIdFieldName,
			SubItemsFieldName, "DTSTART", "DTEND", "RRULE", "ORGANIZER",
			"DTSTAMP", "UID", "ATTENDEE", "CREATED", "DESCRIPTION",
			"LAST-MODIFIED", "LOCATION", "SEQUENCE", "STATUS", "SUMMARY",
			"TRANSP", "X-ALT-DESC", "X-MICROSOFT-CDO-BUSYSTATUS", "CLASS" };

	public static final String[] AlarmTypeFields = { GeneratedIdFieldName,
			SubItemsFieldName, "ACTION", "DESCRIPTION", "SUMMARY", "ATTENDEE",
			"TRIGGER" };

	public static final SimplifiedCompositeType EventTyp = null;

	public static final char UnderscoreChar = '_';
	public static final char CommaChar = ',';
	public static final char QuotesChar = '"';
	public static final char ColonChar = ':';
	public static final char SemicolonChar = ';';
	public static final char SpaceChar = ' ';
	public static final char NewLineChar = '\n';
	public static final char GeneratedIdSeparatorChar = '.';
	public static final int FirstGeneratedIndex = 0;

	public static final String Underscore = "" + UnderscoreChar;

	public static final String NewEvent = "BEGIN:" + EventTypeLabel;
	public static final String BeginKeyword = "BEGIN";
	public static final String EndKeyword = "END";

	private Reader input = new InputStreamReader(System.in);

	public CalendarParser(Reader input0) {
		this.input = input0;
	}

	public String getKey(String line) {
		if (line == null) {
			return null;
		} else {
			int pos = line.indexOf(ColonChar);
			if (pos == -1) {
				return line;
			} else {
				int pos2 = line.indexOf(SemicolonChar);
				if (pos2 >= 0 && pos2 < pos) {
					pos = pos2;
				}
				return line.substring(0, pos).trim();
			}
		}
	}

	public String getValue(String line) {
		if (line == null) {
			return null;
		} else {
			int pos = line.indexOf(ColonChar);
			if (pos == -1) {
				return "";
			} else {
				return line.substring(pos + 1, line.length()).trim();
			}
		}
	}

	public boolean isBeginLine(String line) {
		return (line != null) && line.trim().startsWith(BeginKeyword);
	}

	public boolean isEndLine(String line) {
		return (line != null) && line.trim().startsWith(EndKeyword);
	}

	private PrimitiveTypeValue getTypedValue(String key, String value,
			CompositeType type0, int lineCounter) {
		if (key == null) {
			return new StringValue();
		} else {
			try {
				String typeStr = type0.getFieldType(key);
				if (typeStr == null) {
					throw new ParseException("Key '" + key
							+ "' has an undefined type.");
				} else {
					PrimitiveTypeValue ret = (new PrimitiveTypeFactory())
							.newInstance(typeStr, value);
					return ret;
				}
			} catch (ParseException e) {
				throw new ParseException(e.getMessage() + " (line "
						+ lineCounter + ")", e.getCause());
			}
		}
	}

	private List<Pair> preload(BufferedReader input) throws IOException {
		ArrayList<Pair> ret = new ArrayList<>();
		StringBuffer sbuf = new StringBuffer();
		boolean finish = false;
		int lineCounter = 0;
		while (!finish) {
			String line = input.readLine();
			if (line == null) {
				finish = true;
			} else if (line.startsWith("" + SpaceChar)) {
				sbuf.append(line);
			} else {
				ret.add(new Pair(lineCounter, sbuf.toString()));
				sbuf = new StringBuffer();
				sbuf.append(line);
			}
			lineCounter += 1;
		}
		return ret;
	}

	private void parseProperty(String line, TableImpl currentTable,
			Record record, int lineCounter) {
		if (currentTable == null) {
			throw new ParseException("New record was not declared (line "
					+ lineCounter + ")");
		}

		String key = getKey(line);
		String valueStr = getValue(line);
		PrimitiveTypeValue value = getTypedValue(key, valueStr,
				currentTable.getType(), lineCounter);
		if (key.equals(ParserConstant.IdKeyword)) {
			if (currentTable.getIdentifiers().contains(valueStr)) {
				throw new ParseException("Identifier '"
						+ ParserConstant.IdKeyword + ParserConstant.Space
						+ ParserConstant.EqualsSign + ParserConstant.Space
						+ valueStr + "' is duplicated (line " + lineCounter
						+ ").");
			}
		}
		record.set(key, value);
	}

	public String getGeneratedId(List<Integer> generatedIds, int level) {
		while (level >= generatedIds.size()) {
			generatedIds.add(FirstGeneratedIndex);
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
				sbuf.append(GeneratedIdSeparatorChar);
			}
			sbuf.append(counter);
		}
		return sbuf.toString();
	}

	public TableMap parseMap(BufferedReader input) throws IOException {
		TreeMap<String, TableImpl> map = new TreeMap<>();

		map.put(CalendarTypeLabel, new TableImpl(new SimplifiedCompositeType(
				CalendarTypeFields)));
		map.put(TimeZoneTypeLabel, new TableImpl(new SimplifiedCompositeType(
				TimeZoneTypeFields)));
		map.put(DaylightTypeLabel, new TableImpl(new SimplifiedCompositeType(
				DaylightTypeFields)));
		map.put(StandardTypeLabel, new TableImpl(new SimplifiedCompositeType(
				StandardTypeFields)));
		map.put(EventTypeLabel, new TableImpl(new SimplifiedCompositeType(
				EventTypeFields)));
		map.put(AlarmTypeLabel, new TableImpl(new SimplifiedCompositeType(
				AlarmTypeFields)));

		TableImpl currentTable = null;
		Record currentRecord = null;
		String currentTableId = null;

		Stack<String> tableIdStack = new Stack<String>();
		Stack<Record> recordStack = new Stack<Record>();
		Stack<TableImpl> tableStack = new Stack<TableImpl>();
		ArrayList<Integer> generatedIds = new ArrayList<>();

		List<Pair> lines = preload(input);
		int lineCounter = 0;
		boolean firstTime = true;
		for (Pair pair : lines) {
			String line = pair.getLine();
			lineCounter = pair.getLineCounter();
			if (line != null && !line.trim().isEmpty()) {
				if (isBeginLine(line)) {
					String value = getValue(line);
					if (firstTime) {
						firstTime = false;
					} else {
						tableIdStack.push(currentTableId);
						tableStack.push(currentTable);
						recordStack.push(currentRecord);
					}
					currentRecord = new RecordImpl();
					currentRecord.set(GeneratedIdFieldName, new StringValue(
							getGeneratedId(generatedIds, tableIdStack.size())));
					TableImpl refTable = map.get(value);
					if (refTable == null) {
						throw new ParseException("Unknown type '" + value
								+ "' (line " + lineCounter + ").");
					}
					currentTableId = value;
					currentTable = refTable;

				} else if (isEndLine(line)) {
					String foreignKey = currentRecord.get(GeneratedIdFieldName)
							.render();
					currentTable.add(currentRecord);
					String value = getValue(line);
					TableImpl refTable = map.get(value);
					if (refTable == null) {
						throw new ParseException("Unknown type '" + value
								+ "' (line " + lineCounter + ").");
					}
					if (!value.equals(currentTableId)) {
						throw new ParseException("Closing wrong type '" + value
								+ "' (line " + lineCounter + ").");
					}
					if (tableStack.isEmpty()) {
						throw new ParseException("Too many " + EndKeyword
								+ " keywords  (line " + lineCounter + ").");
					}
					currentTableId = tableIdStack.pop();
					currentTable = tableStack.pop();
					currentRecord = recordStack.pop();
					PrimitiveTypeValue subItems = currentRecord
							.get(SubItemsFieldName);
					if (subItems == null) {
						subItems = new StringValue(foreignKey);
					} else {
						subItems = new StringValue(subItems.render()
								+ SpaceChar + foreignKey);
					}
					currentRecord.set(SubItemsFieldName, subItems);

				} else {
					parseProperty(line, currentTable, currentRecord,
							lineCounter);

				}
			}
		}

		if (currentTable != null && currentRecord != null) {
			currentTable.add(currentRecord);
		}

		if (!tableStack.isEmpty()) {
			throw new ParseException("Too few " + EndKeyword
					+ " keywords  (line " + lineCounter + ").");
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
