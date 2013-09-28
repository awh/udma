package org.antispin.udma.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.antispin.udma.persistence.mpq.MPQArchive;

public final class DefaultD2ResourceLocator implements ID2ResourceLocator {

	public static final String ARMOUR_PATH  = "data\\global\\excel\\armour.txt";
	public static final String WEAPONS_PATH = "data\\global\\excel\\weapons.txt";
	public static final String MISC_PATH    = "data\\global\\excel\\Misc.txt";
	public static final String ICON_PATH    = "data\\global\\items\\%s.dc6";
	public static final String PALETTE_PATH = "data\\global\\palette\\ACT1\\pal.dat";
	
	private static final Map<File, MPQArchive> mpqArchiveCache = new HashMap<File, MPQArchive>();
	private static String resourcePath = "/home/adam/workspace/udma/resources";
	
	public InputStream getArmourResource() throws IOException {
		return getResourceAsInputStream(ARMOUR_PATH);
	}

	public InputStream getImageResource(String iconName) throws IOException {
		if(!iconName.matches("^[a-zA-Z0-9]*$")) {
			throw new IllegalArgumentException("invalid icon name");
		}
		return getResourceAsInputStream(String.format(ICON_PATH, iconName));
	}

	public InputStream getMiscResource() throws IOException {
		return getResourceAsInputStream(MISC_PATH);
	}

	public InputStream getInventoryPaletteResource() throws IOException {
		return getResourceAsInputStream(PALETTE_PATH);
	}
	
	public InputStream getWeaponsResource() throws IOException {
		return getResourceAsInputStream(WEAPONS_PATH);
	}

	/**
	 * Recursively search the resource path for the specified resource,
	 * returning the first one found.
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private static InputStream getResourceAsInputStream(String resource) throws IOException {
		for(String resourceContainer: resourcePath.split(":")) {
			File resourceContainerFile = new File(resourceContainer);
			if(resourceContainerFile.exists()) {
				if(resourceContainer.endsWith(".mpq") && resourceContainerFile.isFile()) {
					MPQArchive mpqArchive = getMPQArchive(resourceContainerFile);
					InputStream is = mpqArchive.getResourceAsInputStream(resource);
					if(is != null) {
						return is;
					}
				} else if(resourceContainerFile.isDirectory()) {					
					File resourceFile = new File(resourceContainer + "/" + resource.replace("\\", "/"));
					if(resourceFile.exists()) {
						return new FileInputStream(resourceFile);
					}
				}
			}
		}
		
		// finally check classpath
		return D2ResourceFactory.class.getResourceAsStream("/" + resource.replace("\\", "/"));
	}
	
	/**
	 * Open the specified MPQ archive and cache the result for further use.
	 * 
	 * @param archiveFile
	 * @return
	 * @throws IOException 
	 */
	private static MPQArchive getMPQArchive(File archiveFile) throws IOException {
		if(!mpqArchiveCache.containsKey(archiveFile)) {
			mpqArchiveCache.put(archiveFile, new MPQArchive(archiveFile));
		}
		return mpqArchiveCache.get(archiveFile);
	}
	
}
