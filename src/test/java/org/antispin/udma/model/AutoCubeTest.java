package org.antispin.udma.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.Position;

import junit.framework.TestCase;

import org.antispin.udma.model.factory.IItemFactory;
import org.antispin.udma.model.repository.IItemRepository;
import org.antispin.udma.model.repository.ListItemRepository;
import org.antispin.udma.ui.jfc.AutoCube;

public class AutoCubeTest extends TestCase {
	
	private static class MockGem implements IGem {

		private final GemType gemType;
		private final GemQuality gemQuality;
		
		public MockGem(GemType gemType, GemQuality gemQuality) {
			this.gemType = gemType;
			this.gemQuality = gemQuality;
		}
		
		public GemQuality getGemQuality() {
			return gemQuality;
		}

		public GemType getGemType() {
			return gemType;
		}

		public String getItemCode() {
			throw new UnsupportedOperationException();
		}

		public Position getPosition() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	private static class MockItemFactory implements IItemFactory {

		public IGem newGem(GemType gemType, GemQuality gemQuality) {
			return new MockGem(gemType, gemQuality);
		}

		public IItem newSimpleItem(Byte[] itemBytes, String itemCode) {
			throw new UnsupportedOperationException();
		}
		
	}
	
	public void testCubeChipped() {
		final IItemRepository itemRepository = new ListItemRepository();
		final MockItemFactory itemFactory = new MockItemFactory();
		final AutoCube autoCube = new AutoCube(itemRepository, itemFactory);
		
		autoCube.run();
		
		itemRepository.add(itemFactory.newGem(GemType.DIAMOND, GemQuality.CHIPPED));
		itemRepository.add(itemFactory.newGem(GemType.DIAMOND, GemQuality.CHIPPED));
		itemRepository.add(itemFactory.newGem(GemType.DIAMOND, GemQuality.CHIPPED));
		
		autoCube.run();
		
		assertEquals(1, itemRepository.getGems(GemType.DIAMOND, GemQuality.FLAWED).size());
		assertEquals(1, itemRepository.size());
	}
	
	public void testCubeCascade() {
		final IItemRepository itemRepository = new ListItemRepository();
		final MockItemFactory itemFactory = new MockItemFactory();
		final AutoCube autoCube = new AutoCube(itemRepository, itemFactory);
		
		itemRepository.add(itemFactory.newGem(GemType.DIAMOND, GemQuality.CHIPPED));
		itemRepository.add(itemFactory.newGem(GemType.DIAMOND, GemQuality.CHIPPED));
		itemRepository.add(itemFactory.newGem(GemType.DIAMOND, GemQuality.CHIPPED));

		itemRepository.add(itemFactory.newGem(GemType.DIAMOND, GemQuality.FLAWED));
		itemRepository.add(itemFactory.newGem(GemType.DIAMOND, GemQuality.FLAWED));

		itemRepository.add(itemFactory.newGem(GemType.DIAMOND, GemQuality.NORMAL));
		itemRepository.add(itemFactory.newGem(GemType.DIAMOND, GemQuality.NORMAL));

		itemRepository.add(itemFactory.newGem(GemType.DIAMOND, GemQuality.FLAWLESS));
		itemRepository.add(itemFactory.newGem(GemType.DIAMOND, GemQuality.FLAWLESS));
		
		autoCube.run();
		
		assertEquals(1, itemRepository.getGems(GemType.DIAMOND, GemQuality.PERFECT).size());
		assertEquals(1, itemRepository.size());
	}
	
}
