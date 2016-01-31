package de.tudresden.inf.lat.tabula.ext.renderer;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import de.tudresden.inf.lat.tabula.datatype.CompositeTypeValue;
import de.tudresden.inf.lat.tabula.datatype.ParameterizedListValue;
import de.tudresden.inf.lat.tabula.datatype.PrimitiveTypeValue;
import de.tudresden.inf.lat.tabula.datatype.Record;
import de.tudresden.inf.lat.tabula.datatype.StringValue;
import de.tudresden.inf.lat.tabula.datatype.URIValue;
import de.tudresden.inf.lat.tabula.parser.ParserConstant;
import de.tudresden.inf.lat.tabula.renderer.Renderer;
import de.tudresden.inf.lat.tabula.renderer.UncheckedWriter;
import de.tudresden.inf.lat.tabula.renderer.UncheckedWriterImpl;
import de.tudresden.inf.lat.tabula.table.Table;
import de.tudresden.inf.lat.tabula.table.TableMap;

/**
 * Renderer of tables in SQL format.
 */
public class SqlRenderer implements Renderer {

	public static final int DEFAULT_SIZE = 0x800;
	public static final String DEFAULT_DATABASE_NAME = "tabula";
	public static final String CREATE_DATABASE = "create database";
	public static final String USE = "use";
	public static final String CREATE_TABLE = "create table";
	public static final String LEFT_PAR = "(";
	public static final String RIGHT_PAR = ")";
	public static final String DEFAULT_FIELD_TYPE = "varchar(" + DEFAULT_SIZE + ")";
	public static final String COMMA = ",";
	public static final String SEMICOLON = ";";
	public static final String VALUES = "values";
	public static final String NULL = "null";
	public static final String APOSTROPHE = "'";
	public static final String INSERT_INTO = "insert into";
	public static final String APOSTROPHE_REPLACEMENT = "%27";
	public static final String ASC = "asc";
	public static final String DESC = "desc";
	public static final String SELECT_ALL_FROM = "select * from";
	public static final String ORDER_BY = "order by";

	private Writer output = new OutputStreamWriter(System.out);

	public SqlRenderer(Writer output) {
		this.output = output;
	}

	public String sanitize(String str) {
		return str.replace(APOSTROPHE, APOSTROPHE_REPLACEMENT);
	}

	public boolean writeStringIfNotEmpty(UncheckedWriter output, String field, StringValue value) {
		if (Objects.nonNull(field) && !field.trim().isEmpty() && Objects.nonNull(value)
				&& !value.toString().trim().isEmpty()) {
			output.write(APOSTROPHE);
			output.write(sanitize(value.toString()));
			output.write(APOSTROPHE);
			return true;
		} else {
			output.write(NULL);
			return false;
		}
	}

	public boolean writeParameterizedListIfNotEmpty(UncheckedWriter output, String field, ParameterizedListValue list) {
		if (Objects.nonNull(list) && !list.isEmpty()) {
			output.write(APOSTROPHE);
			list.forEach(strVal -> {
				output.write(sanitize(strVal.toString()));
				output.write(ParserConstant.SPACE);
			});
			output.write(APOSTROPHE);
			return true;
		} else {
			output.write(NULL);
			return false;
		}
	}

	public boolean writeLinkIfNotEmpty(UncheckedWriter output, String field, URIValue link) {
		if (Objects.nonNull(link) && !link.isEmpty()) {
			output.write(APOSTROPHE);
			output.write(sanitize(link.toString()));
			output.write(APOSTROPHE);
			return true;
		} else {
			output.write(NULL);
			return false;
		}
	}

	public void render(UncheckedWriter output, String tableName, Record record, List<String> fields) {

		output.write(ParserConstant.NEW_LINE);
		output.write(INSERT_INTO);
		output.write(ParserConstant.SPACE);
		output.write(tableName);
		output.write(ParserConstant.SPACE);
		output.write(VALUES);
		output.write(ParserConstant.SPACE);
		output.write(LEFT_PAR);
		output.write(ParserConstant.SPACE);

		boolean first = true;
		for (String field : fields) {
			if (first) {
				first = false;
			} else {
				output.write(COMMA);
			}
			output.write(ParserConstant.NEW_LINE);
			Optional<PrimitiveTypeValue> optValue = record.get(field);
			if (optValue.isPresent()) {
				PrimitiveTypeValue value = optValue.get();
				if (value instanceof StringValue) {
					StringValue strVal = (StringValue) value;
					writeStringIfNotEmpty(output, field, strVal);

				} else if (value instanceof ParameterizedListValue) {
					ParameterizedListValue list = (ParameterizedListValue) value;
					writeParameterizedListIfNotEmpty(output, field, list);

				} else if (value instanceof URIValue) {
					URIValue link = (URIValue) value;
					writeLinkIfNotEmpty(output, field, link);

				} else {
					throw new IllegalStateException("Invalid value '" + value.toString() + "'.");
				}

			} else {
				output.write(NULL);
			}
		}
		output.write(ParserConstant.NEW_LINE);
		output.write(RIGHT_PAR);
		output.write(SEMICOLON);
	}

	public void renderAllRecords(UncheckedWriter output, String tableName, CompositeTypeValue table) {
		output.write(ParserConstant.NEW_LINE);
		List<Record> list = table.getRecords();
		list.forEach(record -> {
			render(output, tableName, record, table.getType().getFields());
			output.write(ParserConstant.NEW_LINE);
		});
		output.write(ParserConstant.NEW_LINE);
	}

	public void renderTypes(UncheckedWriter output, String tableName, CompositeTypeValue table) {
		output.write(ParserConstant.NEW_LINE + ParserConstant.NEW_LINE);
		output.write(CREATE_TABLE + ParserConstant.SPACE);
		output.write(tableName + ParserConstant.SPACE);
		output.write(LEFT_PAR);
		output.write(ParserConstant.NEW_LINE);
		boolean first = true;
		for (String field : table.getType().getFields()) {
			if (first) {
				first = false;
			} else {
				output.write(COMMA);
				output.write(ParserConstant.NEW_LINE);
			}
			output.write(field);
			output.write(ParserConstant.SPACE);
			output.write(DEFAULT_FIELD_TYPE);
		}
		output.write(ParserConstant.NEW_LINE);
		output.write(RIGHT_PAR);
		output.write(SEMICOLON);
		output.write(ParserConstant.NEW_LINE);
		output.write(ParserConstant.NEW_LINE);
	}

	public void renderOrder(UncheckedWriter output, String tableName, Table table) {
		output.write(ParserConstant.NEW_LINE);
		output.write(SELECT_ALL_FROM);
		output.write(ParserConstant.SPACE);
		output.write(tableName);
		output.write(ParserConstant.NEW_LINE);
		output.write(ORDER_BY);
		output.write(ParserConstant.SPACE);

		boolean first = true;
		for (String field : table.getSortingOrder()) {
			if (first) {
				first = false;
			} else {
				output.write(COMMA);
				output.write(ParserConstant.SPACE);
			}
			output.write(field);
			output.write(ParserConstant.SPACE);
			if (table.getFieldsWithReverseOrder().contains(field)) {
				output.write(DESC);
			} else {
				output.write(ASC);
			}
		}
		output.write(SEMICOLON);
		output.write(ParserConstant.NEW_LINE);
		output.write(ParserConstant.NEW_LINE);
	}

	public void renderPrefix(UncheckedWriter output) {
		output.write(ParserConstant.NEW_LINE);
		output.write(CREATE_DATABASE + ParserConstant.SPACE + DEFAULT_DATABASE_NAME + SEMICOLON);
		output.write(ParserConstant.NEW_LINE);
		output.write(ParserConstant.NEW_LINE);
		output.write(USE + ParserConstant.SPACE + DEFAULT_DATABASE_NAME + SEMICOLON);
		output.write(ParserConstant.NEW_LINE);
		output.write(ParserConstant.NEW_LINE);
	}

	public void render(UncheckedWriter output, TableMap tableMap) {
		renderPrefix(output);
		tableMap.getTableIds().forEach(tableName -> {
			Table table = tableMap.getTable(tableName);
			renderTypes(output, tableName, table);
			renderAllRecords(output, tableName, table);
			renderOrder(output, tableName, table);
		});
		output.write(ParserConstant.NEW_LINE);
		output.flush();
	}

	@Override
	public void render(TableMap tableMap) {
		render(new UncheckedWriterImpl(this.output), tableMap);
	}

}
