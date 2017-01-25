package de.tudresden.inf.lat.tabula.datatype;

/**
 * This models a primitive type.
 *
 */
public interface PrimitiveType extends DataType {

	/**
	 * Returns the name of this type.
	 * 
	 * @return the name of this type
	 */
	String getTypeName();

	/**
	 * Tells whether this type is a list.
	 * 
	 * @return <code>true</code> if and only if this type is a list
	 */
	boolean isList();

	/**
	 * Returns a value based on the given string.
	 * 
	 * @param str
	 *            string
	 * @return a value based on the given string
	 * @throws ParseException
	 *             if something went wrong during parsing
	 */
	PrimitiveTypeValue parse(String str) throws ParseException;

}
