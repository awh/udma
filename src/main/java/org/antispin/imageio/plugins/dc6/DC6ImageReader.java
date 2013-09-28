package org.antispin.imageio.plugins.dc6;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Iterator;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

/**
 * ImageIO implementation based on the DC6 format documentation in the following article:
 * 
 * http://phrozenkeep.planetdiablo.gamespy.com/forum/viewtopic.php?t=724#148076
 * 
 * @author adam
 *
 */
public final class DC6ImageReader extends ImageReader {

	static int[][] palette;
	
	public static void setPalette(int[][] palette) {
		DC6ImageReader.palette = palette;
	}
	
	public DC6ImageReader(ImageReaderSpi originatingProvider) {
		super(originatingProvider);
	}
	
	@Override
	public int getHeight(int imageIndex) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public IIOMetadata getImageMetadata(int imageIndex) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<ImageTypeSpecifier> getImageTypes(int imageIndex)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getNumImages(boolean allowSearch) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public IIOMetadata getStreamMetadata() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getWidth(int imageIndex) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public BufferedImage read(int imageIndex, ImageReadParam param) throws IOException {
		ImageInputStream iis = (ImageInputStream) getInput();
		
		// set byte order and skip header
		iis.setByteOrder(ByteOrder.LITTLE_ENDIAN);
		iis.seek(0x10);
		
		// read number of directions and frames per direction
		final int directions = iis.readInt();
		final int frames = iis.readInt();
		
		// read frame pointers
		final int[][] framePointers = new int[directions][frames];
		for(int direction = 0; direction < directions; ++direction) {
			for(int frame = 0; frame < frames; ++frame) {
				framePointers[direction][frame] = iis.readInt();
			}
		}
		
		// seek to requested image
		iis.seek(framePointers[imageIndex / frames][imageIndex % frames]);
		
		// read frame header
		final int flip = iis.readInt();
		final int width = iis.readInt();
		final int height = iis.readInt();
		final int offsetX = iis.readInt();
		final int offsetY = iis.readInt();
		final int unknown = iis.readInt();
		final int nextBlock = iis.readInt();
		final int length = iis.readInt();
		
		// create buffer to store image as we read it in
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		WritableRaster raster = bufferedImage.getRaster();
		
		// read the image
		final int[] rgba = new int[4];
		int x = 0;
		int y = height - 1;
		
		do {
			int b = iis.readByte() & 0xFF;
			if(b == 0x80) {
				// move up a line
				x = 0; --y;
			} else if((b & 0x80) == 0x80) {
				// run length encoded transparent pixel
				for(int i = 0; i < (b & 0x7F); ++i) {
					rgba[0] = rgba[1] = rgba[2] = rgba[3] = 0;
					raster.setPixel(x++, y, rgba);
				}
			} else {
				// 'b' worth of palette indices
				for(int i = 0; i < b; ++i) {
					int paletteIndex = iis.readByte() & 0xFF;
					rgba[0] = palette[paletteIndex][0];
					rgba[1] = palette[paletteIndex][1];
					rgba[2] = palette[paletteIndex][2];
					rgba[3] = 0xFF;
					raster.setPixel(x++, y, rgba);
				}
			}
		} while(y >= 0);
		
		return bufferedImage;
	}

}
