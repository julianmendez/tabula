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
	 * @throws UncheckedIOException
	 *             if something goes wrong with input/output
	 */
	void write(int character);

	/**
	 * Writes an array of characters.
	 * 
	 * @param charBuffer
	 *            array of characters
	 * @throws UncheckedIOException
	 *             if something goes wrong with input/output
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
	 * @throws UncheckedIOException
	 *             if something goes wrong with input/output
	 */
	void write(char[] charBuffer, int offset, int length);

	/**
	 * Writes a string.
	 * 
	 * @param str
	 *            string
	 * @throws UncheckedIOException
	 *             if something goes wrong with input/output
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
	 * @throws UncheckedIOException
	 *             if something goes wrong with input/output
	 */
	void write(String str, int offset, int length);

	/**
	 * Closes the stream, flushing it first.
	 * 
	 * @throws UncheckedIOException
	 *             if something goes wrong with input/output
	 */
	void close();

	/**
	 * Flushes the stream.
	 * 
	 * @throws UncheckedIOException
	 *             if something goes wrong with input/output
	 */
	void flush();

	/**
	 * Appends a character to this writer.
	 * 
	 * @param character
	 *            character
	 * @return this writer
	 * @throws UncheckedIOException
	 *             if something goes wrong with input/output
	 */
	UncheckedWriter append(char character);

	/**
	 * Appends a character sequence to this writer.
	 * 
	 * @param charSequence
	 *            character sequence
	 * @return this writer
	 * @throws UncheckedIOException
	 *             if something goes wrong with input/output
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
	 * @throws UncheckedIOException
	 *             if something goes wrong with input/output
	 */
	UncheckedWriter append(CharSequence charSequence, int start, int end);

	/**
	 * Returns this as a writer.
	 * 
	 * @return this as a writer
	 */
	Writer asWriter();

}
