package org.antispin.udma.model.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.EventListenerList;

import org.antispin.udma.model.GemQuality;
import org.antispin.udma.model.GemType;
import org.antispin.udma.model.IGem;
import org.antispin.udma.model.IItem;

/**
 * An in-memory item repository based on an ArrayList.
 * 
 * @author Adam Harrison
 *
 */
public final class ListItemRepository extends AbstractItemRepository {

	private final List<IItem> items = new ArrayList<IItem>();
	
	public void add(IItem item) {
		this.items.add(item);
		notifyRepositoryChange();
	}

	public void removeAll(Collection<? extends IItem> items) {
		this.items.removeAll(items);
		notifyRepositoryChange();
	}
	
	public List<IGem> getGems(GemType gemType, GemQuality gemQuality) {
		final List<IGem> gems = new ArrayList<IGem>();
		for(IItem item: items) {
			if(item instanceof IGem) {
				final IGem gem = (IGem) item;
				if(gem.getGemType() == gemType && gem.getGemQuality() == gemQuality) {
					gems.add(gem);
				}
			}
		}
		return gems;		
	}

	public Object[] toArray() {
		return items.toArray();
	}

	public Iterator<IItem> iterator() {
		return items.iterator();
	}

	public int size() {
		return items.size();
	}
	
}
