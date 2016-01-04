package de.tudresden.inf.lat.tabula.renderer;

import java.io.IOException;
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

	public static final String Prefix = "" + ParserConstant.CommentSymbol + " simple format 1.0.0"
			+ ParserConstant.NewLine;

	private Writer output = new OutputStreamWriter(System.out);

	public SimpleFormatRenderer(Writer output0) {
		output = output0;
	}

	public boolean writeIfNotEmpty(Writer output, String field, PrimitiveTypeValue value) throws IOException {
		if (field != null && !field.trim().isEmpty() && value != null && !value.isEmpty()) {
			output.write(ParserConstant.NewLine);
			output.write(field);
			output.write(ParserConstant.Space + ParserConstant.EqualsSign);
			if (value.getType().isList()) {
				List<String> list = value.renderAsList();
				for (String link : list) {
					output.write(ParserConstant.Space + ParserConstant.LineContinuationSymbol);
					output.write(ParserConstant.NewLine);
					output.write(ParserConstant.Space);
					output.write(link.toString());
				}

			} else {
				output.write(ParserConstant.Space);
				output.write(value.toString());

			}
			return true;
		} else {
			return false;
		}
	}

	public void render(Writer output, Record record, List<String> fields) throws IOException {

		output.write(ParserConstant.NewLine + ParserConstant.NewLine);
		output.write(ParserConstant.NewRecordToken + ParserConstant.Space);
		output.write(ParserConstant.EqualsSign + ParserConstant.Space);

		for (String field : fields) {
			PrimitiveTypeValue value = record.get(field);
			if (value != null) {
				writeIfNotEmpty(output, field, value);
			}
		}
	}

	public void renderAllRecords(Writer output, CompositeTypeValue table) throws IOException {
		List<Record> list = table.getRecords();
		for (Record record : list) {
			render(output, record, table.getType().getFields());
			output.write(ParserConstant.NewLine);
		}
		output.write(ParserConstant.NewLine);
	}

	public void renderTypeSelection(Writer output, String tableName, CompositeTypeValue table) throws IOException {
		output.write(ParserConstant.NewLine + ParserConstant.NewLine);
		output.write(ParserConstant.TypeSelectionToken + ParserConstant.Space);
		output.write(ParserConstant.EqualsSign);
		output.write(ParserConstant.Space);
		output.write(tableName + ParserConstant.Space);
		output.write(ParserConstant.NewLine);
	}

	public void renderTypeDefinition(Writer output, CompositeTypeValue table) throws IOException {
		output.write(ParserConstant.NewLine + ParserConstant.NewLine);
		output.write(ParserConstant.TypeDefinitionToken + ParserConstant.Space);
		output.write(ParserConstant.EqualsSign);

		for (String field : table.getType().getFields()) {
			output.write(ParserConstant.Space + ParserConstant.LineContinuationSymbol);
			output.write(ParserConstant.NewLine);
			output.write(ParserConstant.Space);
			output.write(field);
			output.write(ParserConstant.TypeSign);
			output.write(table.getType().getFieldType(field));
		}
		output.write(ParserConstant.NewLine);
	}

	public void renderOrder(Writer output, Table table) throws IOException {
		output.write(ParserConstant.NewLine + ParserConstant.NewLine);
		output.write(ParserConstant.SortingOrderDeclarationToken + ParserConstant.Space);
		output.write(ParserConstant.EqualsSign);

		for (String field : table.getSortingOrder()) {
			output.write(ParserConstant.Space + ParserConstant.LineContinuationSymbol);
			output.write(ParserConstant.NewLine);
			output.write(ParserConstant.Space);
			if (table.getFieldsWithReverseOrder().contains(field)) {
				output.write(ParserConstant.ReverseOrderSign);
			}
			output.write(field);
		}
		output.write(ParserConstant.NewLine);
	}

	public void render(Writer output, TableMap tableMap) throws IOException {
		output.write(Prefix);
		for (String tableName : tableMap.getTableIds()) {
			Table table = tableMap.getTable(tableName);
			renderTypeSelection(output, tableName, table);
			renderTypeDefinition(output, table);
			renderOrder(output, table);
			renderAllRecords(output, table);
		}
		output.write(ParserConstant.NewLine);
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
