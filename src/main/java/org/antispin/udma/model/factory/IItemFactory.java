package org.antispin.udma.model.factory;

import org.antispin.udma.model.GemQuality;
import org.antispin.udma.model.GemType;
import org.antispin.udma.model.IGem;
import org.antispin.udma.model.IItem;

public interface IItemFactory {

	/**
	 * Create a new simple item with the specified attributes.
	 * 
	 * @param itemBytes
	 * @param itemCode
	 * @return
	 */
	IItem newSimpleItem(Byte[] itemBytes, String itemCode);
	
	/**
	 * Create a new gem of the specified type and quality.
	 * 
	 * @param gemType
	 * @param gemQuality
	 * @return
	 */
	IGem newGem(GemType gemType, GemQuality gemQuality);
	//IRune newRune(RuneType runeType);
	
}
