package de.tudresden.inf.lat.tabula.renderer;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import de.tudresden.inf.lat.tabula.datatype.PrimitiveTypeValue;
import de.tudresden.inf.lat.tabula.datatype.Record;
import de.tudresden.inf.lat.tabula.parser.ParserConstant;

/**
 * Renderer of records in simple format.
 */
public class SimpleFormatRecordRenderer implements RecordRenderer {

	private Writer output = new OutputStreamWriter(System.out);

	public SimpleFormatRecordRenderer(Writer output) {
		Objects.requireNonNull(output);
		this.output = output;
	}

	public SimpleFormatRecordRenderer(UncheckedWriter output) {
		Objects.requireNonNull(output);
		this.output = output.asWriter();
	}

	public boolean writeIfNotEmpty(UncheckedWriter output, String field, PrimitiveTypeValue value) {
		if (Objects.nonNull(field) && !field.trim().isEmpty() && Objects.nonNull(value) && !value.isEmpty()) {
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
