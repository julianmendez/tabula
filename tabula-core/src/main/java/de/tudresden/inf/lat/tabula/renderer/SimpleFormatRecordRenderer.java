package de.tudresden.inf.lat.tabula.renderer;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

import de.tudresden.inf.lat.tabula.common.OptMap;
import de.tudresden.inf.lat.tabula.common.OptMapImpl;
import de.tudresden.inf.lat.tabula.datatype.ParameterizedListValue;
import de.tudresden.inf.lat.tabula.datatype.PrimitiveTypeValue;
import de.tudresden.inf.lat.tabula.datatype.Record;
import de.tudresden.inf.lat.tabula.datatype.URIType;
import de.tudresden.inf.lat.tabula.parser.ParserConstant;

/**
 * Renderer of records in simple format.
 */
public class SimpleFormatRecordRenderer implements RecordRenderer {

	private Writer output = new OutputStreamWriter(System.out);
	private OptMap<URI, URI> prefixMap = new OptMapImpl<>(new TreeMap<>());

	public SimpleFormatRecordRenderer(Writer output, OptMap<URI, URI> prefixMap) {
		Objects.requireNonNull(output);
		this.output = output;
		this.prefixMap = prefixMap;
	}

	public SimpleFormatRecordRenderer(UncheckedWriter output, OptMap<URI, URI> prefixMap) {
		Objects.requireNonNull(output);
		this.output = output.asWriter();
		this.prefixMap = prefixMap;
	}

	public String renderWithPrefix(String uriStr) {
		String[] ret = new String[1];
		ret[0] = uriStr;
		boolean[] found = new boolean[1];
		found[0] = false;
		prefixMap.keySet().forEach(key -> {
			String expansion = prefixMap.get(key).get().toASCIIString();
			if (!found[0] && uriStr.startsWith(expansion)) {
				String keyStr = key.toASCIIString();
				if (keyStr.isEmpty()) {
					ret[0] = "";
				} else {
					ret[0] = ParserConstant.PREFIX_AMPERSAND + keyStr + ParserConstant.PREFIX_SEMICOLON;
				}
				ret[0] += uriStr.substring(expansion.length());
				found[0] = true;
			}
		});
		return ret[0];
	}

	public boolean writeIfNotEmpty(UncheckedWriter output, String field, PrimitiveTypeValue value) {
		if (Objects.nonNull(field) && !field.trim().isEmpty() && Objects.nonNull(value) && !value.isEmpty()) {
			output.write(ParserConstant.NEW_LINE);
			output.write(field);
			output.write(ParserConstant.SPACE + ParserConstant.EQUALS_SIGN);
			if (value.getType().isList()) {
				boolean[] hasUris = new boolean[1];
				hasUris[0] = false;
				if (value instanceof ParameterizedListValue) {
					hasUris[0] = ((ParameterizedListValue) value).getParameter().equals(new URIType());
				}
				value.getType();
				List<String> list = value.renderAsList();
				list.forEach(elem -> {
					output.write(ParserConstant.SPACE + ParserConstant.LINE_CONTINUATION_SYMBOL);
					output.write(ParserConstant.NEW_LINE);
					output.write(ParserConstant.SPACE);
					if (hasUris[0]) {
						output.write(renderWithPrefix(elem));
					} else {
						output.write(elem.toString());
					}
				});

			} else {
				output.write(ParserConstant.SPACE);
				if (value.getType().equals(new URIType())) {
					output.write(renderWithPrefix(value.toString()));
				} else {
					output.write(value.toString());
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public void render(UncheckedWriter output, Record record, List<String> fields) {
		output.write(ParserConstant.NEW_LINE);
		output.write(ParserConstant.NEW_RECORD_TOKEN + ParserConstant.SPACE);
		output.write(ParserConstant.EQUALS_SIGN + ParserConstant.SPACE);

		fields.forEach(field -> {
			Optional<PrimitiveTypeValue> optValue = record.get(field);
			if (optValue.isPresent()) {
				writeIfNotEmpty(output, field, optValue.get());
			}
		});

		output.write(ParserConstant.NEW_LINE);
		output.flush();
	}

	@Override
	public void render(Record record, List<String> fields) {
		render(new UncheckedWriterImpl(this.output), record, fields);
	}

}
