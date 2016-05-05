package de.tudresden.inf.lat.tabula.renderer;

import java.util.List;

import de.tudresden.inf.lat.tabula.datatype.Record;

/**
 * Record renderer.
 */
@FunctionalInterface
public interface RecordRenderer {

	void render(Record record, List<String> fields);

}
