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
import de.tudresden.inf.lat.tabula.datatype.StringType;
import de.tudresden.inf.lat.tabula.datatype.StringValue;
import de.tudresden.inf.lat.tabula.datatype.URIType;
import de.tudresden.inf.lat.tabula.datatype.URIValue;
import de.tudresden.inf.lat.tabula.renderer.Renderer;
import de.tudresden.inf.lat.tabula.renderer.UncheckedWriter;
import de.tudresden.inf.lat.tabula.renderer.UncheckedWriterImpl;
import de.tudresden.inf.lat.tabula.table.TableMap;

/**
 * Renderer of tables that creates an HTML document.
 */
public class HtmlRenderer implements Renderer {

	public static final String PREFIX = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+ "\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"https://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">"
			+ "\n" + "\n<html xmlns=\"https://www.w3.org/1999/xhtml\" lang=\"en\">" + "\n<head>"
			+ "\n  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />" + "\n  <title></title>"
			+ "\n</head>" + "\n<body>" + "\n  <div>" + "\n" + "\n<br />" + "\n" + "\n";

	public static final String SUFFIX = "\n" + "\n" + "\n  </div>" + "\n</body>" + "\n</html>" + "\n";

	private Writer output = new OutputStreamWriter(System.out);

	public HtmlRenderer(Writer output) {
		this.output = output;
	}

	public boolean writeAsStringIfNotEmpty(UncheckedWriter output, PrimitiveTypeValue value) {
		boolean result = false;
		if (Objects.nonNull(value) && !value.toString().trim().isEmpty()) {
			output.write(value.toString());
			output.write("\n");
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	public boolean writeParameterizedListIfNotEmpty(UncheckedWriter output, ParameterizedListValue list) {
		boolean result = false;
		if (Objects.nonNull(list)) {
			list.forEach(value -> {
				if (value.getType().equals(new URIType())) {
					URIValue link = (new URIType()).castInstance(value);
					writeLinkIfNotEmpty(output, link);
				} else {
					StringValue strVal = (new StringType()).castInstance(value);
					writeAsStringIfNotEmpty(output, strVal);
				}
			});
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	public boolean writeLinkIfNotEmpty(UncheckedWriter output, URIValue link) {
		boolean result = false;
		if (Objects.nonNull(link) && !link.isEmpty()) {
			output.write("<a href=\"");
			output.write(link.getUriNoLabel().toASCIIString());
			output.write("\">(");
			output.write(link.getLabel());
			output.write(")</a>");
			output.write("\n");
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	public void render(UncheckedWriter output, Record record, List<String> fields) {
		fields.forEach(field -> {
			Optional<PrimitiveTypeValue> optValue = record.get(field);
			if (optValue.isPresent()) {
				PrimitiveTypeValue value = optValue.get();
				if (value instanceof ParameterizedListValue) {
					output.write("<td> ");
					ParameterizedListValue list = (ParameterizedListValue) value;
					writeParameterizedListIfNotEmpty(output, list);
					output.write(" </td>\n");

				} else if (value instanceof URIValue) {
					output.write("<td> ");
					URIValue link = (URIValue) value;
					writeLinkIfNotEmpty(output, link);
					output.write(" </td>\n");

				} else {
					output.write("<td> ");
					writeAsStringIfNotEmpty(output, value);
					output.write(" </td>\n");

				}

			} else {
				output.write("<td> </td>\n");
				output.write("\n");
			}
		});
	}

	public void renderAllRecords(UncheckedWriter output, CompositeTypeValue table) {
		List<Record> list = table.getRecords();
		output.write("<table summary=\"\">\n");
		list.forEach(record -> {
			output.write("<tr>\n");
			render(output, record, table.getType().getFields());
			output.write("</tr>\n");
		});
		output.write("</table>\n");
	}

	public void render(UncheckedWriter output, TableMap tableMap) {
		output.write(PREFIX);
		tableMap.getTableIds().forEach(tableId -> {
			CompositeTypeValue table = tableMap.getTable(tableId).get();
			renderAllRecords(output, table);
		});
		output.write("\n");
		output.write("\n");
		output.write(SUFFIX);
		output.flush();
	}

	@Override
	public void render(TableMap tableMap) {
		render(new UncheckedWriterImpl(this.output), tableMap);
	}

}
