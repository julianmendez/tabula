package de.tudresden.inf.lat.tabula.datatype;

import java.util.Optional;
import java.util.TreeMap;

import de.tudresden.inf.lat.util.map.OptMap;
import de.tudresden.inf.lat.util.map.OptMapImpl;

/**
 * This models a factory of primitive types.
 *
 */
public class PrimitiveTypeFactory {

	private OptMap<String, PrimitiveType> map = new OptMapImpl<>(new TreeMap<>());

	private void add(PrimitiveType primType) {
		this.map.put(primType.getTypeName(), primType);
	}

	/**
	 * Constructs a new primitive type factory.
	 */
	public PrimitiveTypeFactory() {
		add(new EmptyType());
		add(new StringType());
		add(new ParameterizedListType(new StringType()));
		add(new URIType());
		add(new ParameterizedListType(new URIType()));
		add(new IntegerType());
		add(new ParameterizedListType(new IntegerType()));
		add(new DecimalType());
		add(new ParameterizedListType(new DecimalType()));
	}

	/**
	 * Tells whether this factory contains the given primitive type.
	 * 
	 * @param primType
	 *            primitive type
	 * @return <code>true</code> if and only if this factory contains the given
	 *         primitive type
	 */
	public boolean contains(String primType) {
		return this.map.containsKey(primType);
	}

	/**
	 * Returns a new value of the specified type.
	 * 
	 * @param typeName
	 *            type name
	 * @param value
	 *            value
	 * @return a new value of the specified type
	 */
	public PrimitiveTypeValue newInstance(String typeName, String value) {
		PrimitiveTypeValue result = null;
		Optional<PrimitiveType> optPrimType = this.map.get(typeName);
		if (!optPrimType.isPresent()) {
			throw new ParseException("Type '" + typeName + "' is undefined.");
		} else {
			result = optPrimType.get().parse(value);
		}
		return result;
	}

}
