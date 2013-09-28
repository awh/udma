package org.antispin.java.lang;

import org.antispin.java.lang.U32;

import junit.framework.TestCase;

public class U32Test extends TestCase {

	public void testAddition() throws Exception {
		U32 u32 = new U32(0xFFFFFFFF).add(1);
		assertEquals(u32.intValue(), 0x0);
	}
	
	public void testMultiply() throws Exception {
		U32 u32 = new U32(0xFFFFFFFF).multiply(0xFFFFFFFF);
		assertEquals(u32.intValue(), 1);
	}
	
}
