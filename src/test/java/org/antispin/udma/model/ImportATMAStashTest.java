package org.antispin.udma.model;

import java.io.FileInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import org.antispin.udma.model.factory.IItemFactory;
import org.antispin.udma.model.impl.DefaultItemFactory;
import org.antispin.udma.model.repository.IItemRepository;
import org.antispin.udma.model.repository.ListItemRepository;
import org.antispin.udma.ui.jfc.ATMAStashImporter;

public class ImportATMAStashTest extends TestCase {

	public void testImport() throws Exception {
/*
		final InputStream inputStream = new FileInputStream("/home/adam/udmatest.d2x");
		final IItemRepository itemRepository = new ListItemRepository();
		final IItemFactory itemFactory = new DefaultItemFactory();
		
		final ATMAStashImporter importer = new ATMAStashImporter(inputStream, itemRepository, itemFactory);
		
		importer.execute();
		
		assertEquals(18, itemRepository.size());
*/
	}
	
}
