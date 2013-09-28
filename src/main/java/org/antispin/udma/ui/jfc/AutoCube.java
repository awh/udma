package org.antispin.udma.ui.jfc;

import java.util.EnumSet;
import java.util.List;

import org.antispin.java.lang.Enums;
import org.antispin.udma.model.GemQuality;
import org.antispin.udma.model.GemType;
import org.antispin.udma.model.IGem;
import org.antispin.udma.model.factory.IItemFactory;
import org.antispin.udma.model.repository.IItemRepository;

/**
 * Automatically cube up the gems in a repository.
 * 
 * @author Adam Harrison
 *
 */
public class AutoCube implements Runnable {
	
	final IItemRepository itemRepository;
	final IItemFactory itemFactory;

	public AutoCube(IItemRepository itemRepository, IItemFactory itemFactory) {
		this.itemRepository = itemRepository;
		this.itemFactory = itemFactory;
	}
	
	public void run() {
		for(final GemType gemType: GemType.values()) {
			for(final GemQuality gemQuality: EnumSet.range(GemQuality.CHIPPED, GemQuality.FLAWLESS)) {
				final List<IGem> gems = itemRepository.getGems(gemType, gemQuality);
				
				for(int i = 0; i <= gems.size() - 3; i += 3) {
					itemRepository.removeAll(gems.subList(i, i + 3));
					itemRepository.add(itemFactory.newGem(gemType, Enums.next(gemQuality)));
				}
			}
		}
	}
	
}
