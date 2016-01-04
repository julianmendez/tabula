package de.tudresden.inf.lat.tabula.renderer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.Objects;

/**
 * This is the default implementation of {@link UncheckedWriter}.
 * 
 * @author Julian Mendez
 *
 */
public class UncheckedWriterImpl implements UncheckedWriter {

	private final Writer writer;

	/**
	 * Constructs a new unchecked writer.
	 * 
	 * @param writer
	 *            writer
	 */
	public UncheckedWriterImpl(Writer writer) {
		Objects.requireNonNull(writer);
		this.writer = writer;
	}

	@Override
	public void write(int character) {
		try {
			this.writer.write(character);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public void write(char[] charBuffer) {
		try {
			this.writer.write(charBuffer);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public void write(char[] charBuffer, int offset, int length) {
		try {
			this.writer.write(charBuffer, offset, length);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public void write(String str) {
		try {
			this.writer.write(str);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public void write(String str, int offset, int length) {
		try {
			this.writer.write(str, offset, length);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public void close() {
		try {
			this.writer.close();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public void flush() {
		try {
			this.writer.flush();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public UncheckedWriter append(char character) {
		try {
			this.writer.append(character);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return this;
	}

	@Override
	public UncheckedWriter append(CharSequence charSequence) {
		try {
			this.writer.append(charSequence);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return this;
	}

	@Override
	public UncheckedWriter append(CharSequence charSequence, int start, int end) {
		try {
			this.writer.append(charSequence, start, end);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return this;
	}

	@Override
	public Writer asWriter() {
		return this.writer;
	}

	@Override
	public int hashCode() {
		return this.writer.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof UncheckedWriter)) {
			return false;
		} else {
			UncheckedWriter other = (UncheckedWriter) obj;
			return asWriter().equals(other.asWriter());
		}
	}

	@Override
	public String toString() {
		return this.writer.toString();
	}

}
