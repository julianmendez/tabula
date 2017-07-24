package de.tudresden.inf.lat.tabula.datatype;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This models a decimal value.
 * 
 */
public class DecimalValue implements PrimitiveTypeValue {

	private BigDecimal number = BigDecimal.ZERO;

	/**
	 * Constructs a new empty decimal value.
	 */
	public DecimalValue() {
	}

	/**
	 * Constructs a new decimal value using a string.
	 * 
	 * @param str
	 *            string
	 * @throws ParseException
	 *             <code>str</code> is not a valid representation of a decimal
	 *             value.
	 * 
	 */
	public DecimalValue(String str) throws ParseException {
		Objects.requireNonNull(str);
		try {
			this.number = new BigDecimal(str);
		} catch (NumberFormatException e) {
			throw new ParseException(e);
		}
	}

	/**
	 * Constructs a new decimal value using another decimal value.
	 * 
	 * @param other
	 *            a decimal value
	 */
	public DecimalValue(DecimalValue other) {
		this.number = other.number;
	}

	@Override
	public PrimitiveType getType() {
		return new DecimalType();
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
		int result = 0;
		if (other instanceof DecimalValue) {
			DecimalValue otherValue = (DecimalValue) other;
			result = this.number.compareTo(otherValue.number);
		} else {
			result = render().compareTo(other.render());
		}
		return result;
	}

	@Override
	public int hashCode() {
		return this.number.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (this == obj) {
			result = true;
		} else if (obj instanceof DecimalValue) {
			DecimalValue other = (DecimalValue) obj;
			result =  this.number.equals(other.number);
		} else {
			result = false;
		}
		return result;
	}

	@Override
	public String toString() {
		return this.number.toString();
	}

}
