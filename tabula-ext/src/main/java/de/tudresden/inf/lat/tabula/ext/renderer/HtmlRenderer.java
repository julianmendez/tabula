package de.tudresden.inf.lat.tabula.ext.renderer;

import java.io.IOException;
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
import de.tudresden.inf.lat.tabula.renderer.Renderer;
import de.tudresden.inf.lat.tabula.table.TableMap;

/**
 * Renderer of tables that creates an HTML document.
 */
public class HtmlRenderer implements Renderer {

	public static final String Prefix = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+ "\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">"
			+ "\n" + "\n<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\">" + "\n<head>"
			+ "\n  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />" + "\n  <title></title>"
			+ "\n</head>" + "\n<body>" + "\n  <div>" + "\n" + "\n<br />" + "\n" + "\n";

	public static final String Suffix = "\n" + "\n" + "\n  </div>" + "\n</body>" + "\n</html>" + "\n";

	private Writer output = new OutputStreamWriter(System.out);

	public HtmlRenderer(Writer output0) {
		output = output0;
	}

	public boolean writeStringIfNotEmpty(Writer output, StringValue str) throws IOException {
		if (str != null && !str.toString().trim().isEmpty()) {
			output.write(str.toString());
			output.write("\n");
			return true;
		} else {
			return false;
		}
	}

	public boolean writeParameterizedListIfNotEmpty(Writer output, ParameterizedListValue list) throws IOException {
		if (list != null) {
			for (PrimitiveTypeValue value : list) {
				if (value.getType().equals(new URIType())) {
					URIValue link = (new URIType()).castInstance(value);
					writeLinkIfNotEmpty(output, link);
				} else {
					StringValue strVal = (new StringType()).castInstance(value);
					writeStringIfNotEmpty(output, strVal);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean writeLinkIfNotEmpty(Writer output, URIValue link) throws IOException {
		if (link != null && !link.isEmpty()) {
			output.write("<a href=\"");
			output.write(link.getUriNoLabel().toASCIIString());
			output.write("\">(");
			output.write(link.getLabel());
			output.write(")</a>");
			output.write("\n");
			return true;
		} else {
			return false;
		}
	}

	public void render(Writer output, Record record, List<String> fields) throws IOException {
		for (String field : fields) {
			PrimitiveTypeValue value = record.get(field);
			if (value == null) {
				output.write("<td> </td>\n");
				output.write("\n");
			} else {
				if (value instanceof StringValue) {
					output.write("<td> ");
					StringValue strVal = (StringValue) value;
					writeStringIfNotEmpty(output, strVal);
					output.write(" </td>\n");

				} else if (value instanceof ParameterizedListValue) {
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
					throw new IllegalStateException("Invalid value '" + value.toString() + "'.");
				}

			}
		}
	}

	public void renderAllRecords(Writer output, CompositeTypeValue table) throws IOException {
		List<Record> list = table.getRecords();
		output.write("<table summary=\"\">\n");
		for (Record record : list) {
			output.write("<tr>\n");
			render(output, record, table.getType().getFields());
			output.write("</tr>\n");
		}
		output.write("</table>\n");
	}

	public void renderMap(Writer output, Map<String, String> map) throws IOException {
		output.write("<table summary=\"\" border=\"1\">\n");
		for (String key : map.keySet()) {
			String value = map.get(key);
			output.write("<tr>\n");
			output.write("<td>");
			output.write(key);
			output.write("</td>\n");
			output.write("<td>");
			output.write(value);
			output.write("</td>\n");
			output.write("</tr>\n");
		}
		output.write("</table>\n");
		output.write("\n");
	}

	public void render(Writer output, TableMap tableMap) throws IOException {
		output.write(Prefix);
		for (String tableId : tableMap.getTableIds()) {
			CompositeTypeValue table = tableMap.getTable(tableId);
			renderAllRecords(output, table);
		}
		output.write("\n");
		output.write("\n");
		output.write(Suffix);
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
