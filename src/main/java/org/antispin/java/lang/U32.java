package org.antispin.java.lang;


/**
 * @author adam
 *
 */
public final class U32 {

	/**
	 * 32bit value holder
	 */
	final int value;
	
	/**
	 * Construct a U32 from a byte representation in little endian format.
	 * @param byteRepresentation
	 */
	public U32(byte[] byteArray) {
		this(byteArray, 0);
	}
	
	/**
	 * Construct a U32 from a byte representation in little endian format,
	 * starting at the specified offset within the supplied array.
	 * @param byteRepresentation
	 * @param offset
	 */
	public U32(byte[] byteArray, int offset) {
		value = toInt(byteArray, offset);
	}
	
	public U32(int value) {
		this.value = value;
	}
	
	public U32 add(int rhs) {
		return new U32(value + rhs);
	}
	
	public U32 add(U32 rhs) {
		return add(rhs.intValue());
	}
	
	public U32 subtract(int rhs) {
		return new U32(value - rhs);
	}
	
	public U32 subtract(U32 rhs) {
		return subtract(rhs.intValue());
	}
	
	public U32 multiply(int rhs) { 
		return new U32(value * rhs);
	}
	
	public U32 multiply(U32 rhs) {
		return multiply(rhs.intValue());
	}
	
	public U32 shiftLeft(int bits) {
		return new U32(value << bits);
	}
	
	public U32 shiftRight(int bits) {
		return new U32(value >>> bits);
	}
	
	public U32 and(int rhs) {
		return new U32(value & rhs);
	}
	
	public U32 and(U32 rhs) {
		return and(rhs.intValue());
	}
	
	public U32 or(int rhs) {
		return new U32(value | rhs);
	}
	
	public U32 or(U32 rhs) {
		return or(rhs.intValue());
	}
	
	public U32 modulo(int rhs) {
		return new U32(value % rhs);
	}
	
	public U32 modulo(U32 rhs) {
		return modulo(rhs.intValue());
	}
	
	public U32 xor(int rhs) {
		return new U32(value ^ rhs);
	}
	
	public U32 xor(U32 rhs) {
		return xor(rhs.intValue());
	}
	
	public int intValue() {
		return value;
	}
	
	public boolean equals(Object rhs) {
		if(rhs instanceof U32) {
			return value == ((U32) rhs).value;
		} else {
			return false;
		}
	}
	
	public String toString() {
		return Long.toString(value & 0xFFFFFFFFL);
	}
	
	public String toBinaryString() {
		return Integer.toBinaryString(value);
	}

	public static int toInt(byte[] byteArray, int offset) {
		int value = 0;
		
		value |= (byteArray[0 + offset] & 0xFF) << 0;
		value |= (byteArray[1 + offset] & 0xFF) << 8;
		value |= (byteArray[2 + offset] & 0xFF) << 16;
		value |= (byteArray[3 + offset] & 0xFF) << 24;

		return value;
	}

	/**
	 * Store the specified U32 in little endian format in buf at the supplied offset.
	 * @param buf
	 * @param i
	 * @param ch
	 */
	public static byte[] toByteArray(byte[] byteArray, int offset, int i) {
		byteArray[0 + offset] = (byte) ((i & 0x000000FF) >>> 0);
		byteArray[1 + offset] = (byte) ((i & 0x0000FF00) >>> 8);
		byteArray[2 + offset] = (byte) ((i & 0x00FF0000) >>> 16);
		byteArray[3 + offset] = (byte) ((i & 0xFF000000) >>> 24);
		
		return byteArray;
	}
	
	public static byte[] toByteArray(int i) {
		return toByteArray(new byte[4], 0, i);
	}

}
