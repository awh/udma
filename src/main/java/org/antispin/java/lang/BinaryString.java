package org.antispin.java.lang;

public final class BinaryString {

	private BinaryString() {
	}
	
	public static byte byteValue(String binaryString) {
		if(binaryString.length() > 8) {
			throw new IllegalArgumentException("String length " + binaryString.length() + " exceeds maximum of 32");
		}
		
		int mask = 1 << (binaryString.length() - 1);
		byte byteValue = 0;
		
		for(char c: binaryString.toCharArray()) {
			if(c == '1') {
				byteValue |= mask;
			} else if(c != '0') {
				throw new IllegalArgumentException();
			}
			mask >>>= 1;
		}
		
		return byteValue;
	}

	public static char charValue(String binaryString) {
		if(binaryString.length() > 16) {
			throw new IllegalArgumentException("String length " + binaryString.length() + " exceeds maximum of 32");
		}
		throw new UnsupportedOperationException();
	}
	
	public static int intValue(String binaryString) {
		if(binaryString.length() > 32) {
			throw new IllegalArgumentException("String length " + binaryString.length() + " exceeds maximum of 32");
		}

		int mask = 1 << (binaryString.length() - 1);
		int intValue = 0;
		
		for(char c: binaryString.toCharArray()) {
			if(c == '1') {
				intValue |= mask;
			} else if(c != '0') {
				throw new IllegalArgumentException();
			}
			mask >>>= 1;
		}
		
		return intValue;
	}
	
	public static long longValue(String binaryString) {
		if(binaryString.length() > 64) {
			throw new IllegalArgumentException("String length " + binaryString.length() + " exceeds maximum of 32");
		}
		throw new UnsupportedOperationException();
	}
	
}
