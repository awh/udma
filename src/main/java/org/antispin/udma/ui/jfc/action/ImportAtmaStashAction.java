package org.antispin.udma.ui.jfc.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.antispin.udma.model.factory.IItemFactory;
import org.antispin.udma.model.repository.IItemRepository;
import org.antispin.udma.ui.jfc.BackgroundTaskProgressDialog;
import org.antispin.udma.ui.jfc.ImportAtmaStashBackgroundTask;

public class ImportAtmaStashAction extends AbstractAction {

	final JFrame parent;
	final IItemRepository itemRepository;
	final IItemFactory itemFactory;
	
	public ImportAtmaStashAction(JFrame parent, IItemRepository itemRepository, IItemFactory itemFactory) {
		super("Atma Stash");
		this.parent = parent;
		this.itemRepository = itemRepository;
		this.itemFactory = itemFactory;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		JFileChooser fileChooser = new JFileChooser("C:\\Program Files\\ATMA V\\ATMA_CONFIG");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Atma Stashes", new String[] { "d2x" });
		fileChooser.setFileFilter(filter);
		
		switch(fileChooser.showDialog(parent, "Import")) {
			case JFileChooser.APPROVE_OPTION:
				ImportAtmaStashBackgroundTask task = new ImportAtmaStashBackgroundTask(fileChooser.getSelectedFile(), itemRepository, itemFactory);
				BackgroundTaskProgressDialog progressDialog = new BackgroundTaskProgressDialog(parent, task);
				progressDialog.beginTaskExecution();
				progressDialog.setLocationRelativeTo(parent);
				progressDialog.setVisible(true);
			case JFileChooser.CANCEL_OPTION:
			default:
				// do nothing
		}
	}

}
