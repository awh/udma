package org.antispin.java.lang;

import java.util.Arrays;

import org.antispin.java.lang.Base64;

import junit.framework.TestCase;

public class Base64Test extends TestCase {

	public void testEncodeDecode() throws Exception {
		byte[] test;
		
		test = new byte[] { 'M' };
		assertTrue(Arrays.equals(test, Base64.decode(Base64.encode(test))));
		
		test = new byte[] { 'M', 'a' };
		assertTrue(Arrays.equals(test, Base64.decode(Base64.encode(test))));

		test = new byte[] { 'M', 'a', 'n' };
		assertTrue(Arrays.equals(test, Base64.decode(Base64.encode(test))));

	}
	
}
