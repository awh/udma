package org.antispin.java.lang;

public final class Base64 {

	public static final String MAP = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	
	public static final String encode(byte[] input) {
		final int padding = (input.length % 3) == 0 ? 0 : 3 - (input.length % 3);
		final StringBuffer base64 = new StringBuffer();
		
		for(int offset = 0; offset < (input.length + padding); offset += 3) {
			int triplet = 0;
			triplet |= (input[offset] & 0xFF) << 16;
			triplet |= (offset + 1 >= input.length) ? 0 : (input[offset + 1] & 0xFF) << 8;
			triplet |= (offset + 2 >= input.length) ? 0 : (input[offset + 2] & 0xFF) << 0;
			
			base64.append(MAP.charAt(((triplet >>> 18) & 63)));
			base64.append(MAP.charAt(((triplet >>> 12) & 63)));
			base64.append(MAP.charAt(((triplet >>> 6) & 63)));
			base64.append(MAP.charAt(((triplet >>> 0) & 63)));
		}
		
		// replace final bytes with padding indicators
		if(padding > 0) base64.setCharAt(base64.length() - 1, '=');
		if(padding > 1) base64.setCharAt(base64.length() - 2, '=');
		
		return base64.toString();
	}
	
	public static final byte[] decode(String base64) {
		final int padding = base64.endsWith("==") ? 2 : base64.endsWith("=") ? 1 : 0; 
		final byte[] output = new byte[(base64.length() * 3 / 4) - padding];
		int index = 0;
		
		for(int offset = 0; offset < base64.length(); offset += 4) {
			int triplet = 0;
			triplet = MAP.indexOf(base64.charAt(offset)) << 18;
			triplet |= MAP.indexOf(base64.charAt(offset + 1)) << 12;
			triplet |= (offset + 2 >= base64.length() - padding) ? 0 : MAP.indexOf(base64.charAt(offset + 2)) << 6;
			triplet |= (offset + 3 >= base64.length() - padding) ? 0 : MAP.indexOf(base64.charAt(offset + 3));
			
			output[index++] = (byte) ((triplet >>> 16) & 0xFF);
			if(index < output.length) output[index++] = (byte) ((triplet >>> 8) & 0xFF);
			if(index < output.length) output[index++] = (byte) ((triplet >>> 0) & 0xFF);
		}
		
		return output;
	}
	
}
