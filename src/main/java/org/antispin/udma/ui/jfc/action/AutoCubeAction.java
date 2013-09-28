package org.antispin.udma.ui.jfc.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.antispin.udma.model.factory.IItemFactory;
import org.antispin.udma.model.repository.IItemRepository;
import org.antispin.udma.ui.jfc.AutoCube;

public class AutoCubeAction extends AbstractAction {

	final IItemRepository itemRepository;
	final IItemFactory itemFactory;
	
	public AutoCubeAction(IItemRepository itemRepository, IItemFactory itemFactory) {
		super("Auto Cube");
		this.itemRepository = itemRepository;
		this.itemFactory = itemFactory;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		final AutoCube autoCube = new AutoCube(itemRepository, itemFactory);
		autoCube.run();
	}

}
