package org.antispin.udma.metamodel.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.antispin.udma.metamodel.IItemMetadata;
import org.antispin.udma.metamodel.IItemMetadataFactory;
import org.antispin.udma.metamodel.ItemMetadataType;
import org.antispin.udma.resource.ID2ResourceLocator;

public class LazyItemMetadataFactoryTest extends TestCase {

	private static final class MockD2ResourceLocator implements ID2ResourceLocator {
		
		public InputStream getArmourResource() throws IOException {
			return new ByteArrayInputStream("code\ttype\ttype2\narmour\tarmourType\tarmourSubtype".getBytes());
		}

		public InputStream getMiscResource() throws IOException {
			return new ByteArrayInputStream("code\ttype\ttype2\nmisc\tmiscType\tmiscSubtype".getBytes());
		}

		public InputStream getWeaponsResource() throws IOException {
			return new ByteArrayInputStream("code\ttype\ttype2\nweapon\tweaponType\tweaponSubtype".getBytes());
		}

		public InputStream getImageResource(String iconName) throws IOException {
			throw new UnsupportedOperationException();
		}

		public InputStream getInventoryPaletteResource() throws IOException {
			throw new UnsupportedOperationException();
		}
		
	}
	
	public void testArmourMetadata() throws Exception {
		final ID2ResourceLocator d2ResourceLocator = new MockD2ResourceLocator();
		final IItemMetadataFactory factory = new LazyItemMetadataFactory(d2ResourceLocator);
		final IItemMetadata itemMetadata = factory.getItemMetadata("armour");
		
		assertNotNull(itemMetadata);
		assertEquals(ItemMetadataType.ARMOUR, itemMetadata.getItemMetadataType());
		assertEquals("armourType", itemMetadata.getType());
		assertEquals("armourSubtype", itemMetadata.getSubType());
	}
	
	public void testWeaponMetadata() throws Exception {
		final ID2ResourceLocator d2ResourceLocator = new MockD2ResourceLocator();
		final IItemMetadataFactory factory = new LazyItemMetadataFactory(d2ResourceLocator);
		final IItemMetadata itemMetadata = factory.getItemMetadata("weapon");
		
		assertNotNull(itemMetadata);
		assertEquals(ItemMetadataType.WEAPON, itemMetadata.getItemMetadataType());
		assertEquals("weaponType", itemMetadata.getType());
		assertEquals("weaponSubtype", itemMetadata.getSubType());
	}
	
	public void testMiscMetadata() throws Exception {
		final ID2ResourceLocator d2ResourceLocator = new MockD2ResourceLocator();
		final IItemMetadataFactory factory = new LazyItemMetadataFactory(d2ResourceLocator);
		final IItemMetadata itemMetadata = factory.getItemMetadata("misc");
		
		assertNotNull(itemMetadata);
		assertEquals(ItemMetadataType.MISC, itemMetadata.getItemMetadataType());
		assertEquals("miscType", itemMetadata.getType());
		assertEquals("miscSubtype", itemMetadata.getSubType());
	}
	
	public void testUnfound() throws Exception {
		final ID2ResourceLocator d2ResourceLocator = new MockD2ResourceLocator();
		final IItemMetadataFactory factory = new LazyItemMetadataFactory(d2ResourceLocator);
		final IItemMetadata itemMetadata = factory.getItemMetadata("unknown");
		
		assertNull(itemMetadata);
	}

}
