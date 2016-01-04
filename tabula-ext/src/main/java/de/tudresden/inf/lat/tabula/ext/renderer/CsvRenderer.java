package de.tudresden.inf.lat.tabula.ext.renderer;

import java.io.IOException;
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
import de.tudresden.inf.lat.tabula.table.Table;
import de.tudresden.inf.lat.tabula.table.TableMap;

/**
 * Renderer of tables in comma-separated values format.
 */
public class CsvRenderer implements Renderer {

	public static final String Quotes = "\"";
	public static final String QuotesReplacement = "''";
	public static final String Null = "";
	public static final String Comma = ",";

	private Writer output = new OutputStreamWriter(System.out);

	public CsvRenderer(Writer output0) {
		output = output0;
	}

	public String sanitize(String str) {
		return str.replace(Quotes, QuotesReplacement);
	}

	public boolean writeStringIfNotEmpty(Writer output, String field, StringValue value) throws IOException {
		if (field != null && !field.trim().isEmpty() && value != null && !value.toString().trim().isEmpty()) {
			output.write(Quotes);
			output.write(sanitize(value.toString()));
			output.write(Quotes);
			return true;
		} else {
			output.write(Null);
			return false;
		}
	}

	public boolean writeParameterizedListIfNotEmpty(Writer output, String field, ParameterizedListValue list)
			throws IOException {
		if (list != null && !list.isEmpty()) {
			output.write(Quotes);
			for (PrimitiveTypeValue value : list) {
				output.write(sanitize(value.toString()));
				output.write(ParserConstant.Space);
			}
			output.write(Quotes);
			return true;
		} else {
			output.write(Null);
			return false;
		}
	}

	public boolean writeLinkIfNotEmpty(Writer output, String field, URIValue link) throws IOException {
		if (link != null && !link.isEmpty()) {
			output.write(Quotes);
			output.write(sanitize(link.toString()));
			output.write(Quotes);
			return true;
		} else {
			output.write(Null);
			return false;
		}
	}

	public void render(Writer output, Record record, List<String> fields) throws IOException {

		boolean first = true;
		for (String field : fields) {
			if (first) {
				first = false;
			} else {
				output.write(Comma);
			}
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
	}

	public void renderAllRecords(Writer output, CompositeTypeValue table) throws IOException {
		List<Record> list = table.getRecords();
		for (Record record : list) {
			render(output, record, table.getType().getFields());
		}
	}

	public void renderTypeSelection(Writer output, String tableName, CompositeTypeValue table) throws IOException {
		output.write(Quotes);
		output.write(tableName);
		output.write(Quotes);
		int n = table.getType().getFields().size();
		for (int index = 1; index < n; index += 1) {
			output.write(Comma);
			output.write(Null);
		}
		output.write(ParserConstant.NewLine);
	}

	public void renderTypeDefinition(Writer output, CompositeTypeValue table) throws IOException {
		boolean first = true;
		for (String field : table.getType().getFields()) {
			if (first) {
				first = false;
			} else {
				output.write(Comma);
			}
			output.write(Quotes);
			output.write(field);
			output.write(Quotes);
		}
		output.write(ParserConstant.NewLine);
	}

	public void render(Writer output, TableMap tableMap) throws IOException {
		for (String tableName : tableMap.getTableIds()) {
			Table table = tableMap.getTable(tableName);
			renderTypeSelection(output, tableName, table);
			renderTypeDefinition(output, table);
			renderAllRecords(output, table);
		}
		output.flush();
	}

	@Override
	public void render(TableMap tableMap) {
		try {
			render(this.output, tableMap);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
