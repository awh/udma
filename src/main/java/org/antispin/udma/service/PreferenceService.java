package org.antispin.udma.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class PreferenceService {

	public enum Preference {
		D2LOD_INSTALLATION_PATH(new File("C:\\Program Files\\Diablo II")),
		D2LOD_CHARACTER_PATH(new File("C:\\Program Files\\Diablo II\\save"));
		
		final Object defaultValue;
		
		Preference(Object defaultValue) {
			this.defaultValue = defaultValue;
		}
	}
	
	static final List<IListener> listeners = new ArrayList<IListener>();
	
	public interface IListener {
		void preferenceChanged(Preference preference, Object value);
	}
	
	public static void addListener(IListener listener) {
		listeners.add(listener);
	}
	
	public static void removeListener(IListener listener) {
		listeners.remove(listener);
	}
	
	public static File getFile(Preference preference) {
		return (File) preference.defaultValue;
	}
	
	public static String getString(Preference preference) {
		return preference.defaultValue.toString();
	}
	
	public static void setFile(Preference preference, File file) {
		notifyPreferenceChange(preference, file);
	}
	
	private static void notifyPreferenceChange(Preference preference, Object value) {
		for(IListener listener: listeners) {
			listener.preferenceChanged(preference, value);
		}
	}
}