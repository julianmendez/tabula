package de.tudresden.inf.lat.tabula.renderer;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import de.tudresden.inf.lat.tabula.datatype.CompositeTypeValue;
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

	public SimpleFormatRenderer(Writer output) {
		this.output = output;
	}

	public void renderAllRecords(UncheckedWriter output, Table table) {
		SimpleFormatRecordRenderer recordRenderer = new SimpleFormatRecordRenderer(output, table.getPrefixMap());
		output.write(ParserConstant.NEW_LINE);
		List<Record> list = table.getRecords();
		list.forEach(record -> {
			recordRenderer.render(output, record, table.getType().getFields());
			output.write(ParserConstant.NEW_LINE);
		});
		output.write(ParserConstant.NEW_LINE);
	}

	public void renderTypeSelection(UncheckedWriter output, String tableName, CompositeTypeValue table) {
		output.write(ParserConstant.NEW_LINE + ParserConstant.NEW_LINE);
		output.write(ParserConstant.TYPE_SELECTION_TOKEN + ParserConstant.SPACE);
		output.write(ParserConstant.EQUALS_SIGN);
		output.write(ParserConstant.SPACE);
		output.write(tableName);
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
			output.write(table.getType().getFieldType(field).get());
		});
		output.write(ParserConstant.NEW_LINE);
	}

	public void renderPrefixMap(UncheckedWriter output, Table table) {
		output.write(ParserConstant.NEW_LINE + ParserConstant.NEW_LINE);
		output.write(ParserConstant.PREFIX_MAP_TOKEN + ParserConstant.SPACE);
		output.write(ParserConstant.EQUALS_SIGN);

		table.getPrefixMap().keySet().forEach(prefix -> {
			output.write(ParserConstant.SPACE + ParserConstant.LINE_CONTINUATION_SYMBOL);
			output.write(ParserConstant.NEW_LINE);
			output.write(ParserConstant.SPACE);
			output.write(prefix.toASCIIString());
			output.write(ParserConstant.TYPE_SIGN);
			output.write(table.getPrefixMap().getOpt(prefix).get().toASCIIString());
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
			Table table = tableMap.getTable(tableName).get();
			renderTypeSelection(output, tableName, table);
			renderTypeDefinition(output, table);
			renderPrefixMap(output, table);
			renderOrder(output, table);
			renderAllRecords(output, table);
		});
		output.flush();
	}

	@Override
	public void render(TableMap tableMap) {
		render(new UncheckedWriterImpl(this.output), tableMap);
	}

}
