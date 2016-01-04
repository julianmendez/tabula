package de.tudresden.inf.lat.tabula.ext.renderer;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import de.tudresden.inf.lat.tabula.datatype.CompositeTypeValue;
import de.tudresden.inf.lat.tabula.datatype.ParameterizedListValue;
import de.tudresden.inf.lat.tabula.datatype.PrimitiveTypeValue;
import de.tudresden.inf.lat.tabula.datatype.Record;
import de.tudresden.inf.lat.tabula.datatype.StringType;
import de.tudresden.inf.lat.tabula.datatype.StringValue;
import de.tudresden.inf.lat.tabula.datatype.URIType;
import de.tudresden.inf.lat.tabula.datatype.URIValue;
import de.tudresden.inf.lat.tabula.parser.ParserConstant;
import de.tudresden.inf.lat.tabula.renderer.Renderer;
import de.tudresden.inf.lat.tabula.renderer.UncheckedWriter;
import de.tudresden.inf.lat.tabula.renderer.UncheckedWriterImpl;
import de.tudresden.inf.lat.tabula.table.TableMap;

/**
 * Renderer of tables that creates WikiText.
 */
public class WikitextRenderer implements Renderer {

	private Writer output = new OutputStreamWriter(System.out);

	public WikitextRenderer(Writer output0) {
		output = output0;
	}

	public boolean writeStringIfNotEmpty(UncheckedWriter output, String prefix, StringValue str) {
		if (str != null && !str.toString().trim().isEmpty()) {
			output.write(prefix);
			output.write(str.toString());
			output.write("\n");
			return true;
		} else {
			return false;
		}
	}

	public boolean writeParameterizedListIfNotEmpty(UncheckedWriter output, String prefix,
			ParameterizedListValue list) {
		if (list != null) {
			output.write(prefix);
			for (PrimitiveTypeValue value : list) {
				if (value.getType().equals(new URIType())) {
					URIValue link = (new URIType()).castInstance(value);
					writeLinkIfNotEmpty(output, "", link);
				} else {
					StringValue strVal = (new StringType()).castInstance(value);
					writeStringIfNotEmpty(output, "", strVal);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean writeLinkIfNotEmpty(UncheckedWriter output, String prefix, URIValue link) {
		if (link != null && !link.isEmpty()) {
			output.write(prefix);
			output.write("[");
			output.write(link.getUriNoLabel().toASCIIString());
			output.write(" (");
			output.write(link.getLabel());
			output.write(")]");
			output.write("\n");
			return true;
		} else {
			return false;
		}
	}

	public void render(UncheckedWriter output, Record record, List<String> fields) {

		for (String field : fields) {
			PrimitiveTypeValue value = record.get(field);
			output.write("|");
			if (value == null) {
				output.write("\n");
			} else {
				String prefix = field + ParserConstant.EqualsSign;
				if (value instanceof StringValue) {
					StringValue strVal = (StringValue) value;
					writeStringIfNotEmpty(output, prefix, strVal);

				} else if (value instanceof ParameterizedListValue) {
					ParameterizedListValue list = (ParameterizedListValue) value;
					writeParameterizedListIfNotEmpty(output, prefix, list);

				} else if (value instanceof URIValue) {
					URIValue link = (URIValue) value;
					writeLinkIfNotEmpty(output, prefix, link);

				} else {
					throw new IllegalStateException("Invalid value '" + value.toString() + "'.");
				}

			}
		}
	}

	public void renderAllRecords(UncheckedWriter output, CompositeTypeValue table) {
		List<Record> list = table.getRecords();
		output.write("{|\n");
		output.write("|-\n");
		for (Record record : list) {
			render(output, record, table.getType().getFields());
			output.write("|-\n");
		}
		output.write("|}\n");
	}

	public void renderMap(UncheckedWriter output, Map<String, String> map) {
		output.write("{| border=\"1\"\n");
		output.write("|-\n");
		for (String key : map.keySet()) {
			String value = map.get(key);
			output.write("| ");
			output.write(key);
			output.write("\n");
			output.write("| ");
			output.write(value);
			output.write("\n");
			output.write("|-\n");
		}
		output.write("|}\n");
		output.write("\n");
	}

	public void render(UncheckedWriter output, TableMap tableMap) {
		output.write("\n");
		for (String tableId : tableMap.getTableIds()) {
			CompositeTypeValue table = tableMap.getTable(tableId);
			renderAllRecords(output, table);
		}
		output.write("\n");
		output.write("\n");
		output.flush();
	}

	@Override
	public void render(TableMap tableMap) {
		render(new UncheckedWriterImpl(this.output), tableMap);
	}

}
