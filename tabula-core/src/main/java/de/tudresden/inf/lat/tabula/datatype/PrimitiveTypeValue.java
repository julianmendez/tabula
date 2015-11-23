package de.tudresden.inf.lat.tabula.datatype;

import java.util.List;

/**
 * This models a value of a primitive type.
 *
 */
public interface PrimitiveTypeValue extends Comparable<PrimitiveTypeValue> {

	/**
	 * Returns the primitive type
	 * 
	 * @return the primitive type
	 */
	PrimitiveType getType();

	/**
	 * Returns a string representing this value.
	 * 
	 * @return a string representing this value
	 */
	String render();

	/**
	 * Returns a list of strings representing this value.
	 * 
	 * @return a list of strings representing this value
	 */
	List<String> renderAsList();

	/**
	 * Tell whether this value represents an empty value.
	 * 
	 * @return <code>true</code> if and only if this value represents an empty
	 *         value
	 */
	boolean isEmpty();

}
