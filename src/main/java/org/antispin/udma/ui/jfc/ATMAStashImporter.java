package org.antispin.udma.ui.jfc;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.antispin.java.io.MonitoredInputStream;
import org.antispin.java.lang.U16;
import org.antispin.java.lang.U32;
import org.antispin.udma.model.factory.IItemFactory;
import org.antispin.udma.model.repository.IItemRepository;
import org.antispin.udma.persistence.d2s.ItemReader;

public final class ATMAStashImporter {

	/**
	 * ATMA Stash CRC accumulator.
	 */
	private static final class CRCAccumulator implements MonitoredInputStream.Listener {
		
		private long checkSum;
		private int count;
		
		public void notifyRead(int b) {
			b = (count >= 7 && count <= 10) ? 0 : b;
			final long upshift = checkSum << 33 >>> 32;
			final long add = b + ( ( checkSum >>> 31 ) == 1 ? 1 : 0 );
			checkSum = upshift + add;
			++count;
		}
		
		public long getCheckSum() {
			return checkSum;
		}
		
	}

	final InputStream inputStream;
	final IItemRepository itemRepository;
	final MonitoredInputStream monitoredInputStream;
	final ItemReader itemReader;
	final CRCAccumulator crcAccumulator;
	
	int version;
	int itemCount;
	long checkSum;
	
	public ATMAStashImporter(InputStream inputStream, IItemRepository itemRepository, IItemFactory itemFactory) throws IOException {
		this.inputStream = inputStream;
		this.itemRepository = itemRepository;
		this.monitoredInputStream = new MonitoredInputStream(inputStream);
		this.itemReader = new ItemReader(itemFactory, monitoredInputStream);
		this.crcAccumulator = new CRCAccumulator();
		monitoredInputStream.addListener(crcAccumulator);
	}
	
	public void execute() throws IOException {
		final byte[] headerBytes = arrayRead(new byte[7]);
		
		// check header magic
		if(headerBytes[0x00] != 'D' || headerBytes[0x01] != '2' || headerBytes[0x02] != 'X') {
			throw new IOException("Bad header magic");
		}

		itemCount = U16.toShort(headerBytes, 0x03);
		version = U16.toShort(headerBytes, 0x05);
		
		// read checksum (4 bytes)
		final byte[] checksumBytes = arrayRead(new byte[4]);
		checkSum = U32.toInt(checksumBytes, 0x00) & 0xFFFFFFFFL;
		
		// read items
		for(int i = 0; i < itemCount; ++i) {
			itemRepository.add(itemReader.readItem());
		}
		
		// assert checksum
		if(checkSum != crcAccumulator.getCheckSum()) {
			throw new IOException("Bad checksum: " + crcAccumulator.getCheckSum() + " (expected " + checkSum + ")");
		}
		
		// ensure we have exhausted file content
		if(inputStream.read() != -1) {
			throw new IOException("Trailing data");
		}
	}
	
	private byte[] arrayRead(byte[] array) throws IOException {
		if(monitoredInputStream.read(array) != array.length) {
			throw new EOFException();
		}
		return array;
	}
	
}
