package org.antispin.udma.model.impl;

import org.antispin.udma.model.GemQuality;
import org.antispin.udma.model.GemType;
import org.antispin.udma.model.IGem;
import org.antispin.udma.model.IItem;
import org.antispin.udma.model.factory.IItemFactory;

public class DefaultItemFactory implements IItemFactory {

	public IGem newGem(GemType gemType, GemQuality gemQuality) {
		return new Gem(gemType, gemQuality);
	}

	public IItem newSimpleItem(Byte[] itemBytes, String itemCode) {
		return new Item(itemBytes, itemCode);
	}

}
