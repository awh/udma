package org.antispin.udma.persistence.mpq;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import org.antispin.java.lang.U16;
import org.antispin.java.lang.U32;

public class MPQArchive {

	static class Header {
		
		public static final int SIZE = 0x20;
		
		final int headerSize;
		final int archiveSize;
		final short formatVersion;
		final short sectorSizeShift; 
		final int hashTableOffset;
		final int blockTableOffset;
		final int hashTableEntries;
		final int blockTableEntries;		

		public Header(byte[] header) {
			// check format identifier
			if(U32.toInt(header, 0x00) != 0x1A51504D) {
				throw new RuntimeException("Bad magic");
			}
			
			// extract fields
			headerSize = U32.toInt(header, 0x04);
			archiveSize = U32.toInt(header, 0x08);
			formatVersion = U16.toShort(header, 0x0C);
			sectorSizeShift = U16.toShort(header, 0x0E);
			hashTableOffset = U32.toInt(header, 0x10);
			blockTableOffset = U32.toInt(header, 0x14);
			hashTableEntries = U32.toInt(header, 0x18);
			blockTableEntries = U32.toInt(header, 0x1C);
		}
		
		public String toString() {
			StringBuffer sb = new StringBuffer();
			String nl = System.getProperty("line.separator");
			sb.append("Header size         : ").append(headerSize).append(nl);
			sb.append("Archive size        : ").append(archiveSize).append(nl);
			sb.append("Format version      : ").append(formatVersion).append(nl);
			sb.append("Sector size shift   : ").append(sectorSizeShift).append(nl);
			sb.append("Hash table offset   : ").append(hashTableOffset).append(nl);
			sb.append("Block table offset  : ").append(blockTableOffset).append(nl);
			sb.append("Hash table entries  : ").append(hashTableEntries).append(nl);
			sb.append("Block table entries : ").append(blockTableEntries).append(nl);
			return sb.toString();
		}
		
	}
	
	static class HashTableEntry {
		
		public static final int SIZE = 0x10;
		public static final int EMPTY = 0xFFFFFFFF;
		public static final int DELETED = 0xFFFFFFFE;
		
		final int filePathHashA;
		final int filePathHashB;
		final short language;
		final short platform;
		final int fileBlockIndex;
		
		public HashTableEntry(byte[] hashTable, int index) {
			int offset = index * SIZE;
			filePathHashA = U32.toInt(hashTable, 0x00 + offset);
			filePathHashB = U32.toInt(hashTable, 0x04 + offset);
			language = U16.toShort(hashTable, 0x08 + offset);
			platform = U16.toShort(hashTable, 0x0A + offset);
			fileBlockIndex = U32.toInt(hashTable, 0x0C + offset);			
		}
		
		public boolean isEmpty() {
			return fileBlockIndex == EMPTY;
		}
		
		public boolean isDeleted() {
			return fileBlockIndex == DELETED;
		}
		
		public boolean matches(String path, short language, short platform) {
			int nameHashA = MPQCrypto.hash(path, MPQCrypto.HASH_NAME_A);
			int nameHashB = MPQCrypto.hash(path, MPQCrypto.HASH_NAME_B);

			return filePathHashA == nameHashA && filePathHashB == nameHashB && this.language == language && this.platform == platform;
		}
		
		public String toString() {
			StringBuffer sb = new StringBuffer();
			String nl = System.getProperty("line.separator");
			sb.append("File path hash A : 0x").append(Integer.toHexString(filePathHashA)).append(nl);
			sb.append("File path hash B : 0x").append(Integer.toHexString(filePathHashB)).append(nl);
			sb.append("Language         : 0x").append(Integer.toHexString(language)).append(nl);
			sb.append("Platform         : ").append(platform).append(nl);
			sb.append("File block index : ").append(fileBlockIndex).append(nl);
			return sb.toString();
		}
	}
	
	static class BlockTableEntry {
		
		public static final int SIZE = 0x10;
		
		public static final int BLOCK_IS_FILE    = 0x80000000;
		public static final int BLOCK_WHOLE_FILE = 0x01000000;
		public static final int BLOCK_OFFSET_KEY = 0x00020000;
		public static final int BLOCK_ENCRYPTED  = 0x00010000;
		public static final int BLOCK_COMPRESSED = 0x00000200;
		public static final int BLOCK_IMPLODED   = 0x00000100;
		
		final int blockOffset;
		final int blockSize;
		final int fileSize;
		final int flags;
		
		public BlockTableEntry(byte[] blockTable, int index) {
			int offset = index * SIZE;
			blockOffset = U32.toInt(blockTable, 0x00 + offset);
			blockSize = U32.toInt(blockTable, 0x04 + offset);
			fileSize = U32.toInt(blockTable, 0x08 + offset);
			flags = U32.toInt(blockTable, 0x0C + offset);
		}
		
		public String toString() {
			StringBuffer sb = new StringBuffer();
			String nl = System.getProperty("line.separator");
			sb.append("Block offset : ").append(blockOffset).append(nl);
			sb.append("Block size   : ").append(blockSize).append(nl);
			sb.append("File size    : ").append(fileSize).append(nl);
			sb.append("Flags        : ").append(Integer.toHexString(flags)).append(nl);
			if(testFlag(BLOCK_IS_FILE)) { sb.append("  block is file"); }
			if(testFlag(BLOCK_WHOLE_FILE)) { sb.append("  block contains whole file"); }
			if(testFlag(BLOCK_OFFSET_KEY)) { sb.append("  offset encryption key"); }
			if(testFlag(BLOCK_ENCRYPTED)) { sb.append("  block is encrypted"); }
			if(testFlag(BLOCK_COMPRESSED)) { sb.append("  block is compressed"); }
			if(testFlag(BLOCK_IMPLODED)) { sb.append("  block is imploded"); }
			return sb.toString();
		}
		
		private boolean testFlag(int flag) {
			return (flags & flag) != 0;
		}
		
		public boolean isFile() {
			return testFlag(BLOCK_IS_FILE);
		}
		
		public boolean isCompressed() {
			return testFlag(BLOCK_COMPRESSED);
		}
	}
	
	// random access file for reading from archive
	final RandomAccessFile archiveRAF;
	
	final Header header;
	final HashTableEntry[] hashTableEntries;
	final BlockTableEntry[] blockTableEntries;

	public MPQArchive(File archiveFile) throws IOException {
		archiveRAF = new RandomAccessFile(archiveFile, "r");
	
		header = readHeader();
		hashTableEntries = readHashTableEntries();
		blockTableEntries = readBlockTableEntries();
	}
	
	private Header readHeader() throws IOException {
		byte[] buf = new byte[Header.SIZE];
		archiveRAF.read(buf);
		return new Header(buf);
	}

	private HashTableEntry[] readHashTableEntries() throws IOException {
		HashTableEntry[] hashTableEntries = new HashTableEntry[header.hashTableEntries];
		byte[] hashTable = new byte[header.hashTableEntries * HashTableEntry.SIZE];
		
		archiveRAF.seek(header.hashTableOffset);
		archiveRAF.read(hashTable);
		
		// decrypt hash table
		int key = MPQCrypto.hash("(hash table)", MPQCrypto.HASH_FILE_KEY3);
		MPQCrypto.decrypt(hashTable, key);
		
		for(int i = 0; i < header.hashTableEntries; ++i) {
			hashTableEntries[i] = new HashTableEntry(hashTable, i);
		}
		
		return hashTableEntries;
	}
	
	private BlockTableEntry[] readBlockTableEntries() throws IOException {
		BlockTableEntry[] blockTableEntries = new BlockTableEntry[header.blockTableEntries];
		byte[] blockTable = new byte[header.blockTableEntries * BlockTableEntry.SIZE];
		
		archiveRAF.seek(header.blockTableOffset);
		archiveRAF.read(blockTable);
		
		// decrypt block table
		int key = MPQCrypto.hash("(block table)", MPQCrypto.HASH_FILE_KEY3);
		MPQCrypto.decrypt(blockTable, key);
		
		for(int i = 0; i < header.blockTableEntries; ++i) {
			blockTableEntries[i] = new BlockTableEntry(blockTable, i);
		}
		
		return blockTableEntries;
	}
	
	private HashTableEntry lookup(String path, short language, short platform) {
		int initialIndex = MPQCrypto.hash(path, MPQCrypto.HASH_TABLE_OFFSET) & (header.hashTableEntries - 1);
		
		if(hashTableEntries[initialIndex].isEmpty()) {
			return null;
		}
		
		int currentIndex = initialIndex;
		
		do {
			HashTableEntry hte = hashTableEntries[currentIndex];
			
			if(!hte.isDeleted() && hte.matches(path, language, platform)) {
				return hte;
			}
			
			currentIndex = (currentIndex + 1) & (header.hashTableEntries - 1);
		} while(currentIndex != initialIndex && !hashTableEntries[currentIndex].isEmpty());
		
		return null;
	}
	
	public MPQInputStream getResourceAsInputStream(String path) throws IOException {
		// lookup the file in the hashtable
		HashTableEntry hte = lookup(path, (short) 0x0, (short) 0x0);
		if(hte != null) {
			// found it, mmap the block into memory
			BlockTableEntry bte = blockTableEntries[hte.fileBlockIndex];
			FileChannel channel = archiveRAF.getChannel();
			MappedByteBuffer mbb = channel.map(MapMode.READ_ONLY, bte.blockOffset, bte.blockSize);
			
			return new MPQInputStream(header.sectorSizeShift, bte, mbb);
		}
		return null;
	}
	
	public static void main(String[] args) throws IOException {
		MPQArchive mpqa = new MPQArchive(new File("/home/adam/tmp/d2data.mpq"));
		
		
		for(BlockTableEntry bte: mpqa.blockTableEntries) {
			if(!bte.isCompressed()) {
				System.out.println(bte.toString());
			}
		}
		
//		MPQInputStream mpqis = mpqa.getResourceAsInputStream("data\\global\\excel\\Armor.txt");
//		
//		BufferedReader br = new BufferedReader(new InputStreamReader(mpqis));
//		
//		for(String line = br.readLine(); line != null; line = br.readLine()) {
//			System.out.println(line);
//		}

	}
	
}
