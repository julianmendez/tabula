package de.tudresden.inf.lat.tabula.ext.renderer;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

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

	public static final int DefaultSize = 0x800;
	public static final String DefaultDatabaseName = "tabula";
	public static final String CreateDatabase = "create database";
	public static final String Use = "use";
	public static final String CreateTable = "create table";
	public static final String OpenPar = "(";
	public static final String ClosePar = ")";
	public static final String DefaultFieldType = "varchar(" + DefaultSize + ")";
	public static final String Comma = ",";
	public static final String Semicolon = ";";
	public static final String Values = "values";
	public static final String Null = "null";
	public static final String Apostrophe = "'";
	public static final String InsertInto = "insert into";
	public static final String ApostropheReplacement = "%27";
	public static final String Asc = "asc";
	public static final String Desc = "desc";
	public static final String SelectAllFrom = "select * from";
	public static final String OrderBy = "order by";

	private Writer output = new OutputStreamWriter(System.out);

	public SqlRenderer(Writer output0) {
		output = output0;
	}

	public String sanitize(String str) {
		return str.replace(Apostrophe, ApostropheReplacement);
	}

	public boolean writeStringIfNotEmpty(UncheckedWriter output, String field, StringValue value) {
		if (field != null && !field.trim().isEmpty() && value != null && !value.toString().trim().isEmpty()) {
			output.write(Apostrophe);
			output.write(sanitize(value.toString()));
			output.write(Apostrophe);
			return true;
		} else {
			output.write(Null);
			return false;
		}
	}

	public boolean writeParameterizedListIfNotEmpty(UncheckedWriter output, String field, ParameterizedListValue list) {
		if (list != null && !list.isEmpty()) {
			output.write(Apostrophe);
			list.forEach(strVal -> {
				output.write(sanitize(strVal.toString()));
				output.write(ParserConstant.Space);
			});
			output.write(Apostrophe);
			return true;
		} else {
			output.write(Null);
			return false;
		}
	}

	public boolean writeLinkIfNotEmpty(UncheckedWriter output, String field, URIValue link) {
		if (link != null && !link.isEmpty()) {
			output.write(Apostrophe);
			output.write(sanitize(link.toString()));
			output.write(Apostrophe);
			return true;
		} else {
			output.write(Null);
			return false;
		}
	}

	public void render(UncheckedWriter output, String tableName, Record record, List<String> fields) {

		output.write(ParserConstant.NewLine);
		output.write(InsertInto);
		output.write(ParserConstant.Space);
		output.write(tableName);
		output.write(ParserConstant.Space);
		output.write(Values);
		output.write(ParserConstant.Space);
		output.write(OpenPar);
		output.write(ParserConstant.Space);

		boolean first = true;
		for (String field : fields) {
			if (first) {
				first = false;
			} else {
				output.write(Comma);
			}
			output.write(ParserConstant.NewLine);
			PrimitiveTypeValue value = record.get(field);
			if (value != null) {
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
				output.write(Null);
			}
		}
		output.write(ParserConstant.NewLine);
		output.write(ClosePar);
		output.write(Semicolon);
	}

	public void renderAllRecords(UncheckedWriter output, String tableName, CompositeTypeValue table) {
		output.write(ParserConstant.NewLine);
		List<Record> list = table.getRecords();
		list.forEach(record -> {
			render(output, tableName, record, table.getType().getFields());
			output.write(ParserConstant.NewLine);
		});
		output.write(ParserConstant.NewLine);
	}

	public void renderTypes(UncheckedWriter output, String tableName, CompositeTypeValue table) {
		output.write(ParserConstant.NewLine + ParserConstant.NewLine);
		output.write(CreateTable + ParserConstant.Space);
		output.write(tableName + ParserConstant.Space);
		output.write(OpenPar);
		output.write(ParserConstant.NewLine);
		boolean first = true;
		for (String field : table.getType().getFields()) {
			if (first) {
				first = false;
			} else {
				output.write(Comma);
				output.write(ParserConstant.NewLine);
			}
			output.write(field);
			output.write(ParserConstant.Space);
			output.write(DefaultFieldType);
		}
		output.write(ParserConstant.NewLine);
		output.write(ClosePar);
		output.write(Semicolon);
		output.write(ParserConstant.NewLine);
		output.write(ParserConstant.NewLine);
	}

	public void renderOrder(UncheckedWriter output, String tableName, Table table) {
		output.write(ParserConstant.NewLine);
		output.write(SelectAllFrom);
		output.write(ParserConstant.Space);
		output.write(tableName);
		output.write(ParserConstant.NewLine);
		output.write(OrderBy);
		output.write(ParserConstant.Space);

		boolean first = true;
		for (String field : table.getSortingOrder()) {
			if (first) {
				first = false;
			} else {
				output.write(Comma);
				output.write(ParserConstant.Space);
			}
			output.write(field);
			output.write(ParserConstant.Space);
			if (table.getFieldsWithReverseOrder().contains(field)) {
				output.write(Desc);
			} else {
				output.write(Asc);
			}
		}
		output.write(Semicolon);
		output.write(ParserConstant.NewLine);
		output.write(ParserConstant.NewLine);
	}

	public void renderPrefix(UncheckedWriter output) {
		output.write(ParserConstant.NewLine);
		output.write(CreateDatabase + ParserConstant.Space + DefaultDatabaseName + Semicolon);
		output.write(ParserConstant.NewLine);
		output.write(ParserConstant.NewLine);
		output.write(Use + ParserConstant.Space + DefaultDatabaseName + Semicolon);
		output.write(ParserConstant.NewLine);
		output.write(ParserConstant.NewLine);
	}

	public void render(UncheckedWriter output, TableMap tableMap) {
		renderPrefix(output);
		tableMap.getTableIds().forEach(tableName -> {
			Table table = tableMap.getTable(tableName);
			renderTypes(output, tableName, table);
			renderAllRecords(output, tableName, table);
			renderOrder(output, tableName, table);
		});
		output.write(ParserConstant.NewLine);
		output.flush();
	}

	@Override
	public void render(TableMap tableMap) {
		render(new UncheckedWriterImpl(this.output), tableMap);
	}

}
