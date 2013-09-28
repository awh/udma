package org.antispin.udma.persistence.mpq;

import junit.framework.TestCase;

public class MPQCryptoTest extends TestCase {

	public void testHash() throws Exception {
		assertEquals(0xF4E6C69D, MPQCrypto.hash("arr\\units.dat", MPQCrypto.HASH_TABLE_OFFSET));
		assertEquals(0xA26067F3, MPQCrypto.hash("unit\\neutral\\acritter.grp", MPQCrypto.HASH_TABLE_OFFSET));
	}
	
	public void testEncrypt() throws Exception {
		
	}
	
	public void testDecrypt() throws Exception {
		
	}
	
}
