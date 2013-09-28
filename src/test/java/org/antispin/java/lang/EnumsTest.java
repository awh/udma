package org.antispin.java.lang;

import junit.framework.TestCase;

public class EnumsTest extends TestCase {

	private static enum TestEnum {
		ONE, TWO, THREE
	}
	
	public void testPrevious() throws Exception {
		assertEquals(TestEnum.ONE, Enums.previous(TestEnum.TWO));
	}
	
	public void testNext() throws Exception {
		assertEquals(TestEnum.THREE, Enums.next(TestEnum.TWO));
	}
	
	public void testIllegalPrevious() throws Exception {
		try {
			Enums.previous(TestEnum.ONE);
			fail();
		} catch(IllegalArgumentException iae) {
		}
	}
	
	public void testIllegalNext() throws Exception {
		try {
			Enums.next(TestEnum.THREE);
			fail();
		} catch(IllegalArgumentException iae) {
		}
	}

}
