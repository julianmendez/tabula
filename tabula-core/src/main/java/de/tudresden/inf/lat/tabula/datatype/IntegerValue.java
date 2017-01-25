package de.tudresden.inf.lat.tabula.datatype;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This models an integer value.
 * 
 */
public class IntegerValue implements PrimitiveTypeValue {

	private BigInteger number = BigInteger.ZERO;

	/**
	 * Constructs a new empty integer value.
	 */
	public IntegerValue() {
	}

	/**
	 * Constructs a new integer value using a string.
	 * 
	 * @param str
	 *            string
	 * @throws ParseException
	 *             <code>str</code> is not a valid representation of an integer
	 *             value.
	 * 
	 */
	public IntegerValue(String str) throws ParseException {
		Objects.requireNonNull(str);
		try {
			this.number = new BigInteger(str);
		} catch (NumberFormatException e) {
			throw new ParseException(e);
		}
	}

	/**
	 * Constructs a new integer value using another integer value.
	 * 
	 * @param other
	 *            an integer value
	 */
	public IntegerValue(IntegerValue other) {
		this.number = other.number;
	}

	@Override
	public PrimitiveType getType() {
		return new IntegerType();
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public String render() {
		return this.number.toString();
	}

	@Override
	public List<String> renderAsList() {
		List<String> ret = new ArrayList<>();
		ret.add(render());
		return Collections.unmodifiableList(ret);
	}

	@Override
	public int compareTo(PrimitiveTypeValue other) {
		if (other instanceof IntegerValue) {
			IntegerValue otherValue = (IntegerValue) other;
			return this.number.compareTo(otherValue.number);
		} else {
			return render().compareTo(other.render());
		}
	}

	@Override
	public int hashCode() {
		return this.number.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof IntegerValue) {
			IntegerValue other = (IntegerValue) obj;
			return this.number.equals(other.number);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return this.number.toString();
	}

}
