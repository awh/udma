package org.antispin.udma.persistence.d2s;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.antispin.udma.model.ICharacter;
import org.antispin.udma.model.impl.Character;

public class D2SReader {

	static final int MAX_NAME_LENGTH = 15;
	
	byte[] buf;
	int count;
	int mark;
	
	final Character character = new Character(); 
	
	public D2SReader(File characterFile) throws IOException {
		FileInputStream fis = new FileInputStream(characterFile);
		
		character.setLastModified(characterFile.lastModified());
		
		count = (int) characterFile.length();
		buf = new byte[count];
		mark = 0;
		
		fis.read(buf);
	}
	
	public ICharacter parseCharacter() {		
		// assert magic
		if(readLong() != 0xaa55aa55L) {
			throw new RuntimeException("Invalid identification field in file header");
		}
		
		// assert format version
		if(readLong() != 0x60L) {
			throw new RuntimeException("Unsupported format version");
		}
		
		// assert file size
		if(readLong() != count) {
			throw new RuntimeException("Header length field does not match actual file length");
		}
		
		// assert checksum
		if(readLong() != checksum()) {
			throw new RuntimeException("Invalid header checksum");
		}
		
		// skip unknown long
		readLong();
		
		// read character name
		character.setName(readString(MAX_NAME_LENGTH));

		return character;
	}
	
	/**
	 * Read a null-terminated string up to the specified maxLength.
	 * @param maxLength
	 * @return
	 */
	private String readString(int maxLength) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < maxLength; ++i) {
			char c = (char) readByte();
			if(c == 0) {
				break;
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
	private long readLong() {
		long l = 0;
		l |= readByte() << 0;
		l |= readByte() << 8;
		l |= readByte() << 16;
		l |= readByte() << 24;
		return l;
	}
	
	private long readByte() {
		return buf[mark++] & 0xff;
	}
	
	private long checksum() {
		long checksum = 0;
		for(int i = 0; i < buf.length; ++i) {
			// get byte value at index, simulate zeroed checksum field
			byte b = (i > 11 && i < 16) ? 0 : buf[i];
			long l = b & 0xff;
			
			// rotate left one position
			long msb = (checksum & 0x80000000L) >>> 31;
			checksum = (checksum << 1) & 0xffffffffL;
			checksum = checksum | (0xffffffffL & msb);
			
			// add the next byte
			checksum += l;
		}
		return checksum;
	}
	
	
}
