package org.antispin.udma.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Facade to conceal the iterative lookup of D2 resources across the classpath,
 * exploded directories and MPQ files.
 * 
 * @author Adam Harrison
 */
public interface ID2ResourceLocator {

	InputStream getImageResource(String iconName) throws IOException;
	InputStream getInventoryPaletteResource() throws IOException;
	InputStream getArmourResource() throws IOException;
	InputStream getWeaponsResource() throws IOException;
	InputStream getMiscResource() throws IOException;
	
}
