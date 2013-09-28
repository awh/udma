package org.antispin.udma.ui.jfc;

import java.io.File;
import java.io.FileInputStream;

import org.antispin.udma.model.factory.IItemFactory;
import org.antispin.udma.model.repository.IItemRepository;

public class ImportAtmaStashBackgroundTask extends AbstractBackgroundTask {

	final File atmaStashFile;
	final IItemRepository itemRepository;
	final IItemFactory itemFactory;
	
	public ImportAtmaStashBackgroundTask(File atmaStashFile, IItemRepository itemRepository, IItemFactory itemFactory) {
		super(true, true);
		this.atmaStashFile = atmaStashFile;
		this.itemRepository = itemRepository;
		this.itemFactory = itemFactory;
	}
	
	public void execute() throws Exception {
		final FileInputStream inputStream = new FileInputStream(atmaStashFile);
		final ATMAStashImporter importer = new ATMAStashImporter(inputStream, itemRepository, itemFactory);
		importer.execute();
		notifyStatus("Import completed successfully.");
		notifyFinished();
	}
	
}
