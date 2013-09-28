package org.antispin.udma.persistence.mpq;

import org.antispin.java.lang.U32;

public class MPQCrypto {

	public static final int HASH_TABLE_OFFSET = 0x00;
	public static final int HASH_NAME_A = 0x01;
	public static final int HASH_NAME_B = 0x02;
	public static final int HASH_FILE_KEY3 = 0x03;
	
	static final int CRYPT_TABLE[] = new int[0x500];
	
	static {
		int seed = 0x00100001;
		
		for(int index1 = 0; index1 < 0x100; ++index1) {
			int index2 = index1;
			for(int i = 0; i < 5; ++i) {
				int temp1, temp2;
				
				seed = (seed * 125 + 3) % 0x2AAAAB;
	            temp1 = (seed & 0xFFFF) << 0x10;
                
	            seed  = (seed * 125 + 3) % 0x2AAAAB;
                temp2 = (seed & 0xFFFF);
				
				
				CRYPT_TABLE[index2] = temp1 | temp2;
				
				index2 += 0x100;
			}
		}
	}
	
	/**
	 * Compute the MPQ hash of the specified string.
	 * 
	 * @param s The string to be hashed
	 * @param hashType One of the HASH_TABLE_OFFSET, HASH_NAME_A or HASH_NAME_B constants
	 * @return
	 */
	public static int hash(String s, int hashType) {
		int seed1 = 0x7FED7FED;
		int seed2 = 0xEEEEEEEE;
		
		for(char c: s.toUpperCase().toCharArray()) {
			seed1 = CRYPT_TABLE[(hashType * 0x100) + c] ^ (seed1 + seed2);
			seed2 = c + seed1 + seed2 + (seed2 << 5) + 3;
		}
		
		return seed1;
	}
	
	/**
	 * Encrypt the buffer using the specified key.
	 * 
	 * @param buf Buffer containing content to be encrypted. Length must be multiple of four.
	 * @param key Encryption key
	 */
	public static void encrypt(byte[] buf, int key) {
		int seed = 0xEEEEEEEE;
		int ch;
		
		for(int i = 0; i < buf.length; i += 4) {
			seed += CRYPT_TABLE[0x400 + (key & 0xFF)];
			ch = U32.toInt(buf, i) ^ (key + seed);
			
			key = ((~key << 0x15) + 0x11111111) | (key >>> 0x0B);
			seed = U32.toInt(buf, i) + seed + (seed << 5) + 3;
			
			U32.toByteArray(buf, i, ch);
		}
	}

	/**
	 * Decrypt the buffer using the specified key.
	 * 
	 * @param buf Buffer containing content to be decrypted. Length must be multiple of four.
	 * @param key Decryption key
	 */

	public static void decrypt(byte[] buf, int key) {
		int seed = 0xEEEEEEEE;
		int ch;
		
		for(int i = 0; i < buf.length; i += 4) {
			seed += CRYPT_TABLE[0x400 + (key & 0xFF)];
			ch = U32.toInt(buf, i) ^ (key + seed);
			
			key = ((~key << 0x15) + 0x11111111) | (key >>> 0x0B);
			seed = ch + seed + (seed << 5) + 3;
			
			U32.toByteArray(buf, i, ch);
		}
	}
	
}
