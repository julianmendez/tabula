package de.tudresden.inf.lat.tabula.ext.renderer;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

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
 * Renderer of tables in comma-separated values format.
 */
public class CsvRenderer implements Renderer {

	public static final String QUOTES = "\"";
	public static final String QUOTES_REPLACEMENT = "''";
	public static final String NULL = "";
	public static final String COMMA = ",";

	private Writer output = new OutputStreamWriter(System.out);

	public CsvRenderer(Writer output) {
		this.output = output;
	}

	public String sanitize(String str) {
		return str.replace(QUOTES, QUOTES_REPLACEMENT);
	}

	public boolean writeStringIfNotEmpty(UncheckedWriter output, String field, StringValue value) {
		if (Objects.nonNull(field) && !field.trim().isEmpty() && Objects.nonNull(value)
				&& !value.toString().trim().isEmpty()) {
			output.write(QUOTES);
			output.write(sanitize(value.toString()));
			output.write(QUOTES);
			return true;
		} else {
			output.write(NULL);
			return false;
		}
	}

	public boolean writeParameterizedListIfNotEmpty(UncheckedWriter output, String field, ParameterizedListValue list) {
		if (Objects.nonNull(list) && !list.isEmpty()) {
			output.write(QUOTES);
			list.forEach(value -> {
				output.write(sanitize(value.toString()));
				output.write(ParserConstant.SPACE);
			});
			output.write(QUOTES);
			return true;
		} else {
			output.write(NULL);
			return false;
		}
	}

	public boolean writeLinkIfNotEmpty(UncheckedWriter output, String field, URIValue link) {
		if (Objects.nonNull(link) && !link.isEmpty()) {
			output.write(QUOTES);
			output.write(sanitize(link.toString()));
			output.write(QUOTES);
			return true;
		} else {
			output.write(NULL);
			return false;
		}
	}

	public void render(UncheckedWriter output, Record record, List<String> fields) {

		boolean first = true;
		for (String field : fields) {
			if (first) {
				first = false;
			} else {
				output.write(COMMA);
			}
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
	}

	public void renderAllRecords(UncheckedWriter output, CompositeTypeValue table) {
		List<Record> list = table.getRecords();
		list.forEach(record -> {
			render(output, record, table.getType().getFields());
		});
	}

	public void renderTypeSelection(UncheckedWriter output, String tableName, CompositeTypeValue table) {
		output.write(QUOTES);
		output.write(tableName);
		output.write(QUOTES);
		int n = table.getType().getFields().size();
		IntStream.range(1, n).forEach(index -> {
			output.write(COMMA);
			output.write(NULL);
		});
		output.write(ParserConstant.NEW_LINE);
	}

	public void renderTypeDefinition(UncheckedWriter output, CompositeTypeValue table) {
		boolean first = true;
		for (String field : table.getType().getFields()) {
			if (first) {
				first = false;
			} else {
				output.write(COMMA);
			}
			output.write(QUOTES);
			output.write(field);
			output.write(QUOTES);
		}
		output.write(ParserConstant.NEW_LINE);
	}

	public void render(UncheckedWriter output, TableMap tableMap) {
		tableMap.getTableIds().forEach(tableName -> {
			Table table = tableMap.getTable(tableName);
			renderTypeSelection(output, tableName, table);
			renderTypeDefinition(output, table);
			renderAllRecords(output, table);
		});
		output.flush();
	}

	@Override
	public void render(TableMap tableMap) {
		render(new UncheckedWriterImpl(this.output), tableMap);
	}

}
