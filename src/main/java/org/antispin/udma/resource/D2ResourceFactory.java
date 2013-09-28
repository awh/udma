package org.antispin.udma.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.antispin.udma.persistence.mpq.MPQArchive;

public class D2ResourceFactory {

	static final Map<File, MPQArchive> mpqArchiveCache = new HashMap<File, MPQArchive>();
	static String resourcePath = "/home/adam/workspace/udma/resources";
	
	/**
	 * Set the resource path on which to search for resources.
	 * 
	 * @param resourcePath
	 */
	public void setResourcePath(String resourcePath) {
		D2ResourceFactory.resourcePath = resourcePath;
	}
	
	/**
	 * Read the specified Diablo II inventory icon from the game data files.
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage getInventoryImage(String name) throws IOException {
		return ImageIO.read(getResourceAsInputStream("data\\global\\items\\" + name + ".dc6"));
	}
	
	/**
	 * Read the specified Diablo II palette from the game data files.
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static int[][] getPalette(String resource) throws IOException {
		final InputStream is = getResourceAsInputStream(resource);
		final int[][] palette = new int[256][3];
		
		for(int i = 0; i < 256; ++i) {
			for(int j = 2; j >= 0; --j) {
				palette[i][j] = is.read();
			}
		}
		
		return palette;
	}

	/**
	 * Recursively search the resource path for the specified resource,
	 * returning the first one found.
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static InputStream getResourceAsInputStream(String resource) throws IOException {
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
