/**
 * 
 */
package org.antispin.udma.model.repository;

import java.util.Collection;
import java.util.EventListener;
import java.util.List;

import org.antispin.udma.model.GemQuality;
import org.antispin.udma.model.GemType;
import org.antispin.udma.model.IGem;
import org.antispin.udma.model.IItem;

public interface IItemRepository extends Iterable<IItem> {
	
	public interface Listener extends EventListener {
		void notifyRepositoryChange();
	}

	// listener management
	void addListener(Listener listener);
	void removeListener(Listener listener);

	// adding and removing items
	void add(IItem item);
	void removeAll(Collection<? extends IItem> items);

	// standard collection helpers
	int size();
	Object[] toArray();
	
	// queries
	List<IGem> getGems(GemType gemType, GemQuality gemQuality);
	
}