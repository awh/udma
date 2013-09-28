package org.antispin.udma.model.repository;

import javax.swing.event.EventListenerList;

public abstract class AbstractItemRepository implements IItemRepository {

	private final EventListenerList listeners = new EventListenerList();

	public void addListener(Listener listener) {
		listeners.add(Listener.class, listener);
	}

	public void removeListener(Listener listener) {
		listeners.remove(Listener.class, listener);
	}
	
	protected void notifyRepositoryChange() {
		for(Listener listener: listeners.getListeners(Listener.class)) {
			try {
				listener.notifyRepositoryChange();
			} catch(RuntimeException re) {
				// not our responsibility to deal with this - print a warning
				re.printStackTrace();
			}
		}
	}
	
}
