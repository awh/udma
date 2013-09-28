package org.antispin.java.io;

import java.io.ByteArrayInputStream;
import java.io.EOFException;

import junit.framework.TestCase;

import org.antispin.java.lang.BinaryString;

public class BitInputStreamTest extends TestCase {
	
	public void testSimpleRead() throws Exception {
		final byte[] bytes = { BinaryString.byteValue("11011011") };
		final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		final BitInputStream bitInputStream = new BitInputStream(inputStream);
		
		assertEquals(0, bitInputStream.readInt(0));
		assertEquals(BinaryString.intValue("1011"), bitInputStream.readInt(4));
		assertEquals(BinaryString.intValue("101"), bitInputStream.readInt(3));
		assertEquals(BinaryString.intValue("1"), bitInputStream.readInt(1));

		try {
			bitInputStream.readInt(1);
			fail();
		} catch(EOFException ee) {
			// pass
		}
	}
	
}
