package org.antispin.imageio.plugins.dc6;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

public final class DC6ImageReaderSpi extends ImageReaderSpi {
	
	/**
	 * Header block with 0xEE termination
	 */
	static final byte[] EE_HEADER = new byte[] {
		(byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0xEE, (byte) 0xEE, (byte) 0xEE, (byte) 0xEE,
	};
	
	/**
	 * Header block with 0xCD termination
	 */
	static final byte[] CD_HEADER = new byte[] {
		(byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0xCD, (byte) 0xCD, (byte) 0xCD, (byte) 0xCD,
	};

	/**
	 * Header block with 0x00 termination
	 */
	static final byte[] ZERO_HEADER = new byte[] {
		(byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
	};
	
	public DC6ImageReaderSpi() {
		super("antispin.org",
			  "1.0",
			  new String[] { "DC6", "dc6" },
			  new String[] { "dc6" },
			  new String[] { "image/dc6" },
			  "org.antispin.imageio.plugins.dc6.DC6ImageReader",
			  STANDARD_INPUT_TYPE,
			  null,
			  false,
			  null,
			  null,
			  null,
			  null,
			  false,
			  null,
			  null,
			  null,
			  null);
	}
	
	@Override
	public boolean canDecodeInput(Object source) throws IOException {
        if (!(source instanceof ImageInputStream)) {
            return false;
        }
        
        ImageInputStream stream = (ImageInputStream)source;
        byte[] header = new byte[16];
        stream.mark();
        stream.readFully(header);
        stream.reset();
        
        return Arrays.equals(header, EE_HEADER) || Arrays.equals(header, CD_HEADER) || Arrays.equals(header, ZERO_HEADER);
	}

	@Override
	public ImageReader createReaderInstance(Object extension) throws IOException {
		return new DC6ImageReader(this);
	}

	@Override
	public String getDescription(Locale locale) {
		return "Blizzard DC6 Image Reader";
	}

}
