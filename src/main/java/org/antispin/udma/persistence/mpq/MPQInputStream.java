package org.antispin.udma.persistence.mpq;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class MPQInputStream extends InputStream {

	// sector compression mask flags
	public static final int COMPRESSED_IMA_ADPCM_MONO   = 0x40;
	public static final int COMPRESSED_IMA_ADPCM_STEREO = 0x80;
	public static final int COMPRESSED_HUFFMAN          = 0x01;
	public static final int COMPRESSED_DEFLATED         = 0x02;
	public static final int COMPRESSED_IMPLODED         = 0x08;
	public static final int COMPRESSED_BZIP2            = 0x10;
	
	private interface BlockDecoder {
		int read() throws IOException;
	}
	
	private class CompressedSectorBlockDecoder implements BlockDecoder {

		final List<Integer> sectorOffsets = new ArrayList<Integer>();
		final ByteBuffer decompressionBuffer;
		
		int sector = 0;
		int mark = 0;
		
		public CompressedSectorBlockDecoder() {
			// read the sector list
			byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
			
			for(int sectorOffset = byteBuffer.getInt();
				sectorOffset != blockTableEntry.blockSize;
				sectorOffset = byteBuffer.getInt()) {
				sectorOffsets.add(sectorOffset);
			}
			
			decompressionBuffer = ByteBuffer.allocate(512 * 2 ^ sectorSizeShift);
		}
		
		public int read() throws IOException {
			return -1;
		}
		
	}
	
	final short sectorSizeShift;
	final MPQArchive.BlockTableEntry blockTableEntry;
	final ByteBuffer byteBuffer;
	final BlockDecoder blockDecoder;

	MPQInputStream(short sectorSizeShift, MPQArchive.BlockTableEntry blockTableEntry, ByteBuffer mappedByteBuffer) throws IOException {
		this.sectorSizeShift = sectorSizeShift;
		this.blockTableEntry = blockTableEntry;
		this.byteBuffer = mappedByteBuffer;
		this.blockDecoder = getBlockDecoder();
	}
	
	@Override
	public int read() throws IOException {
		return blockDecoder.read();
	}
	
	private BlockDecoder getBlockDecoder() throws IOException {
		if(!blockTableEntry.isFile() || !blockTableEntry.isCompressed()) {
			throw new IOException("Unsupported block table entry flags");
		}
		
		return new CompressedSectorBlockDecoder();
	}

}
