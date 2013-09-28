package org.antispin.java.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.EventListener;

import javax.swing.event.EventListenerList;

/**
 * InputStream decorator cabable of notifying listeners of the results
 * of read operations before they are returned to the caller.
 * 
 * @author Adam Harrison
 *
 */
public final class MonitoredInputStream extends InputStreamDecorator {

	private final EventListenerList listeners = new EventListenerList();
	
	public static interface Listener extends EventListener {
		void notifyRead(int b);
	}
	
	public MonitoredInputStream(InputStream inputStream) {
		super(inputStream);
	}

	public int read() throws IOException {
		final int b = super.read();
		notifyRead(b);
		return b;
	}
	
    public int read(byte b[]) throws IOException {
    	final int count = super.read(b);
    	for(int i = 0; i < count; ++i) {
    		notifyRead(b[i]);
    	}
    	return count;
    }

    public int read(byte b[], int off, int len) throws IOException {
    	// don't support monitoring this method yet
    	throw new UnsupportedOperationException();
    }
	
	public void addListener(Listener listener) {
		listeners.add(Listener.class, listener);
	}

	public void removeListener(Listener listener) {
		listeners.remove(Listener.class, listener);
	}
	
	private void notifyRead(int b) {
		for(Listener listener: listeners.getListeners(Listener.class)) {
			try {
				listener.notifyRead(b);
			} catch(RuntimeException re) {
				// not our responsibility to deal with this - print a warning
				re.printStackTrace();
			}
		}
	}
	
}
