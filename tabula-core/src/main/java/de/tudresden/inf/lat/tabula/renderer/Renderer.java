package de.tudresden.inf.lat.tabula.renderer;

import de.tudresden.inf.lat.tabula.table.TableMap;

/**
 * Renderer.
 *
 */
@FunctionalInterface
public interface Renderer {

	void render(TableMap table);

}
