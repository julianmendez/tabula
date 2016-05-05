package de.tudresden.inf.lat.tabula.parser;

import de.tudresden.inf.lat.tabula.table.TableMap;

/**
 * Parser.
 * 
 */
@FunctionalInterface
public interface Parser {

	TableMap parse();

}
