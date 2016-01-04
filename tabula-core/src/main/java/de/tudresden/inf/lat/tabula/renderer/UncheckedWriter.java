package de.tudresden.inf.lat.tabula.renderer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;

/**
 * This interface models an unchecked writer. This looks like a {@link Writer},
 * but throws an {@link UncheckedIOException} where a <code>Writer</code> throws
 * an {@link IOException}.
 * 
 * @author Julian Mendez
 *
 */
public interface UncheckedWriter {

	/**
	 * Writes a single character.
	 * 
	 * @param character
	 *            character
	 */
	void write(int character);

	/**
	 * Writes an array of characters.
	 * 
	 * @param charBuffer
	 *            array of characters
	 */
	void write(char[] charBuffer);

	/**
	 * Writes an array of characters.
	 * 
	 * @param charBuffer
	 *            array of characters
	 * @param offset
	 *            offset
	 * @param length
	 *            number of characters to write
	 */
	void write(char[] charBuffer, int offset, int length);

	/**
	 * Writes a string.
	 * 
	 * @param str
	 *            string
	 */
	void write(String str);

	/**
	 * Writes a string
	 * 
	 * @param str
	 *            string
	 * @param offset
	 *            offset
	 * @param length
	 *            number of characters to write
	 */
	void write(String str, int offset, int length);

	/**
	 * Closes the stream, flushing it first.
	 */
	void close();

	/**
	 * Flushes the stream.
	 */
	void flush();

	/**
	 * Appends a character to this writer.
	 * 
	 * @param character
	 *            character
	 * @return this writer
	 */
	UncheckedWriter append(char character);

	/**
	 * Appends a character sequence to this writer.
	 * 
	 * @param charSequence
	 *            character sequence
	 * @return this writer
	 */
	UncheckedWriter append(CharSequence charSequence);

	/**
	 * Appends a character subsequence to this writer.
	 * 
	 * @param charSequence
	 *            character sequence
	 * @param start
	 *            start of the sequence
	 * @param end
	 *            end of the sequence
	 * @return this writer
	 */
	UncheckedWriter append(CharSequence charSequence, int start, int end);

}
