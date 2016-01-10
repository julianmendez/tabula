package de.tudresden.inf.lat.tabula.renderer;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import de.tudresden.inf.lat.tabula.datatype.CompositeTypeValue;
import de.tudresden.inf.lat.tabula.datatype.PrimitiveTypeValue;
import de.tudresden.inf.lat.tabula.datatype.Record;
import de.tudresden.inf.lat.tabula.parser.ParserConstant;
import de.tudresden.inf.lat.tabula.table.Table;
import de.tudresden.inf.lat.tabula.table.TableMap;

/**
 * Renderer of tables in simple format.
 */
public class SimpleFormatRenderer implements Renderer {

	public static final String PREFIX = "" + ParserConstant.COMMENT_SYMBOL + " simple format 1.0.0"
			+ ParserConstant.NEW_LINE;

	private Writer output = new OutputStreamWriter(System.out);

	public SimpleFormatRenderer(Writer output0) {
		output = output0;
	}

	public boolean writeIfNotEmpty(UncheckedWriter output, String field, PrimitiveTypeValue value) {
		if (field != null && !field.trim().isEmpty() && value != null && !value.isEmpty()) {
			output.write(ParserConstant.NEW_LINE);
			output.write(field);
			output.write(ParserConstant.SPACE + ParserConstant.EQUALS_SIGN);
			if (value.getType().isList()) {
				List<String> list = value.renderAsList();
				list.forEach(link -> {
					output.write(ParserConstant.SPACE + ParserConstant.LINE_CONTINUATION_SYMBOL);
					output.write(ParserConstant.NEW_LINE);
					output.write(ParserConstant.SPACE);
					output.write(link.toString());
				});

			} else {
				output.write(ParserConstant.SPACE);
				output.write(value.toString());

			}
			return true;
		} else {
			return false;
		}
	}

	public void render(UncheckedWriter output, Record record, List<String> fields) {

		output.write(ParserConstant.NEW_LINE + ParserConstant.NEW_LINE);
		output.write(ParserConstant.NEW_RECORD_TOKEN + ParserConstant.SPACE);
		output.write(ParserConstant.EQUALS_SIGN + ParserConstant.SPACE);

		fields.forEach(field -> {
			PrimitiveTypeValue value = record.get(field);
			if (value != null) {
				writeIfNotEmpty(output, field, value);
			}
		});
	}

	public void renderAllRecords(UncheckedWriter output, CompositeTypeValue table) {
		List<Record> list = table.getRecords();
		list.forEach(record -> {
			render(output, record, table.getType().getFields());
			output.write(ParserConstant.NEW_LINE);
		});
		output.write(ParserConstant.NEW_LINE);
	}

	public void renderTypeSelection(UncheckedWriter output, String tableName, CompositeTypeValue table) {
		output.write(ParserConstant.NEW_LINE + ParserConstant.NEW_LINE);
		output.write(ParserConstant.TYPE_SELECTION_TOKEN + ParserConstant.SPACE);
		output.write(ParserConstant.EQUALS_SIGN);
		output.write(ParserConstant.SPACE);
		output.write(tableName + ParserConstant.SPACE);
		output.write(ParserConstant.NEW_LINE);
	}

	public void renderTypeDefinition(UncheckedWriter output, CompositeTypeValue table) {
		output.write(ParserConstant.NEW_LINE + ParserConstant.NEW_LINE);
		output.write(ParserConstant.TYPE_DEFINITION_TOKEN + ParserConstant.SPACE);
		output.write(ParserConstant.EQUALS_SIGN);

		table.getType().getFields().forEach(field -> {
			output.write(ParserConstant.SPACE + ParserConstant.LINE_CONTINUATION_SYMBOL);
			output.write(ParserConstant.NEW_LINE);
			output.write(ParserConstant.SPACE);
			output.write(field);
			output.write(ParserConstant.TYPE_SIGN);
			output.write(table.getType().getFieldType(field));
		});
		output.write(ParserConstant.NEW_LINE);
	}

	public void renderOrder(UncheckedWriter output, Table table) {
		output.write(ParserConstant.NEW_LINE + ParserConstant.NEW_LINE);
		output.write(ParserConstant.SORTING_ORDER_DECLARATION_TOKEN + ParserConstant.SPACE);
		output.write(ParserConstant.EQUALS_SIGN);

		table.getSortingOrder().forEach(field -> {
			output.write(ParserConstant.SPACE + ParserConstant.LINE_CONTINUATION_SYMBOL);
			output.write(ParserConstant.NEW_LINE);
			output.write(ParserConstant.SPACE);
			if (table.getFieldsWithReverseOrder().contains(field)) {
				output.write(ParserConstant.REVERSE_ORDER_SIGN);
			}
			output.write(field);
		});
		output.write(ParserConstant.NEW_LINE);
	}

	public void render(UncheckedWriter output, TableMap tableMap) {
		output.write(PREFIX);
		tableMap.getTableIds().forEach(tableName -> {
			Table table = tableMap.getTable(tableName);
			renderTypeSelection(output, tableName, table);
			renderTypeDefinition(output, table);
			renderOrder(output, table);
			renderAllRecords(output, table);
		});
		output.write(ParserConstant.NEW_LINE);
		output.flush();
	}

	@Override
	public void render(TableMap tableMap) {
		render(new UncheckedWriterImpl(this.output), tableMap);
	}

}
