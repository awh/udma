package org.antispin.java.lang;


public final class U16 {
	
	public static short toShort(byte[] byteArray, int offset) {
		short value = 0;
		
		value |= (byteArray[0 + offset] & 0xFF) << 0;
		value |= (byteArray[1 + offset] & 0xFF) << 8;

		return value;
	}

	/**
	 * Store the specified U16 in little endian format in buf at the supplied offset.
	 * @param buf
	 * @param i
	 * @param ch
	 */
	public static byte[] toByteArray(byte[] byteArray, int offset, short s) {
		byteArray[0] = (byte) ((s & 0x000000FF) >>> 0);
		byteArray[1] = (byte) ((s & 0x0000FF00) >>> 8);
		
		return byteArray;
	}
	
	public static byte[] toByteArray(short s) {
		return toByteArray(new byte[4], 0, s);
	}

}
