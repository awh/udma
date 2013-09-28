package org.antispin.udma.resource;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class InventoryIconCache {

	static final Map<String, Icon> normalIcons = new HashMap<String, Icon>();
	static final Map<String, Icon> uniqueIcons = new HashMap<String, Icon>();
	static final Map<String, Icon> setIcons = new HashMap<String, Icon>();	
	
	static D2Table armorTable;
	static D2Table miscTable;
	static D2Table weaponTable;
	
	static {
		try {
			armorTable = new D2Table(D2ResourceFactory.getResourceAsInputStream("data\\global\\excel\\armor.txt"));
			miscTable = new D2Table(D2ResourceFactory.getResourceAsInputStream("data\\global\\excel\\Misc.txt"));
			weaponTable = new D2Table(D2ResourceFactory.getResourceAsInputStream("data\\global\\excel\\weapons.txt"));
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	private static void loadIcon(String code) throws IOException {
		preloadIcons(code, normalIcons, armorTable, "invfile");
		//preloadIcons(uniqueIcons, armorTable, "uniqueinvfile");
		//preloadIcons(setIcons, armorTable, "setinvfile");
		
		preloadIcons(code, normalIcons, miscTable, "invfile");
		//preloadIcons(uniqueIcons, miscTable, "uniqueinvfile");
		
		preloadIcons(code, normalIcons, weaponTable, "invfile");
		//preloadIcons(uniqueIcons, weaponTable, "uniqueinvfile");
		//preloadIcons(setIcons, weaponTable, "setinvfile");		
	}
	
	private static void preloadIcons(String code, Map<String, Icon> iconMap, D2Table table, String imageColumn) throws IOException {
		final List<String> codes = table.getColumn("code");
		final List<String> images = table.getColumn(imageColumn);
		final int i = codes.indexOf(code);

		if(i >= 0) {
			try {
				final BufferedImage image = D2ResourceFactory.getInventoryImage(images.get(i));
				iconMap.put(codes.get(i), new ImageIcon(enhance(image)));
				//iconMap.put(codes.get(i), new ImageIcon(image));
			} catch(NullPointerException npe) {
				System.err.println("Could not load icon [" + images.get(i) + "]");
			}
		}
	}
	
	public static Icon getNormalIcon(String code) {
		if(!normalIcons.containsKey(code)) {
			try {
				loadIcon(code);
			} catch(IOException ioe) {
			}
		}
		return normalIcons.get(code);
	}
	
	public static Image enhance(final BufferedImage input) {
		final BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());		
		final WritableRaster src = input.getRaster();
		final WritableRaster dest = output.getRaster();
		final int[] sample = new int[4];
		
		for(int x = 0; x < src.getWidth(); ++x) {
			for(int y = 0; y < src.getHeight(); ++y) {
				src.getPixel(x, y, sample);
				float overA = Math.max(sample[0], Math.max(sample[1], sample[2])) / 255f;
				
				final int alphaBlur = alphaBlur(src, x, y);
				final float underA = alphaBlur / 255f;
				final int underRGB = (255 - alphaBlur);
				
				final int overR = (int) (sample[0] / overA);
				final int overG = (int) (sample[1] / overA);
				final int overB = (int) (sample[2] / overA);
				overA *= sample[3] / 255f;
				
				final float compositeA = overA + (underA * (1 - overA));
				
				sample[0] = (int) ((overR * overA) + (underRGB * underA * (1 - overA)));
				sample[1] = (int) ((overG * overA) + (underRGB * underA * (1 - overA)));
				sample[2] = (int) ((overB * overA) + (underRGB * underA * (1 - overA)));
				sample[3] = (int) (255 * compositeA);
				
				dest.setPixel(x, y, sample);
			}
		}
		
		return output.getScaledInstance(input.getWidth() * 2, input.getHeight() * 2, BufferedImage.SCALE_SMOOTH);
	}
	
	public static int alphaBlur(WritableRaster src, int x, int y) {
		final float[][] matrix = new float[][]{
			{ 0.05472157f, 0.11098164f, 0.05472157f },
			{ 0.11098164f, 0.22508352f, 0.11098164f },
			{ 0.05472157f, 0.11098164f, 0.05472157f },
		};
		
		final int sx = x > 0 ? x - 1 : x;
		final int sy = y > 0 ? y - 1 : y;
		final int ex = x < (src.getWidth() - 1) ? x + 1 : x;
		final int ey = y < (src.getHeight() - 1) ? y + 1 : y;
		final int[] sample = new int[4];
		
		float total = 0;
		float divider = 0;
		
		for(int i = sx; i <= ex; ++i) {
			for(int j = sy; j <= ey; ++j) {
				src.getPixel(i, j, sample);
				total += sample[3] * matrix[(i - x) + 1][(j - y) + 1];
			}
		}
		
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				divider += matrix[i][j];
			}
		}
		
		return (int) (total / divider);
	}
	
}
