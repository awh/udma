package org.antispin.udma.service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.antispin.udma.model.ICharacter;
import org.antispin.udma.persistence.d2s.D2SReader;
import org.antispin.udma.service.PreferenceService.Preference;

public abstract class D2CharacterService {

	public static final String D2S_EXTENSION = ".d2s";
	
	public interface IListener {
		public void addedCharacter(ICharacter character);
		public void changedCharacter(ICharacter character);
		public void removedCharacter(ICharacter character);
	}
	
	final static PreferenceService.IListener preferenceListener = new PreferenceService.IListener() {
		public void preferenceChanged(Preference preference, Object value) {
			switch(preference) {
			case D2LOD_CHARACTER_PATH:
				setCharacterPath((File) value);
			}
		}
	};
	
	static Timer timer;
	static boolean running = false;
	static File characterPath;
	
	final static Map<File, ICharacter> characters = new HashMap<File, ICharacter>();
	final static List<IListener> listeners = new ArrayList<IListener>();
	
	public synchronized static void startService() {
		if(running) throw new IllegalStateException("Service already started");
		
		// set initial character path
		setCharacterPath(PreferenceService.getFile(PreferenceService.Preference.D2LOD_CHARACTER_PATH));
		
		// listen for changes and update character path as necessary
		PreferenceService.addListener(preferenceListener);
		
		// schedule a repetitive timer to scan for changes to the on-disc character save files
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				scanForChanges();
			}			
		}, 0, 1000);
	}
	
	public synchronized static void stopService() {
		if(!running) throw new IllegalStateException("Service already stopped");
		
		// kill change scan timer
		timer.cancel();
		
		// remove listener
		PreferenceService.removeListener(preferenceListener);
	}
	
	private static void setCharacterPath(File characterPath) {
		D2CharacterService.characterPath = characterPath;
		scanForChanges();
	}
	
	private static Set<File> getCharacterFiles() {
		File[] fileArray = characterPath.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.getPath().endsWith(D2S_EXTENSION);
			}
		});
		
		return fileArray == null ? Collections.EMPTY_SET : new HashSet<File>(Arrays.asList(fileArray));
	}
	
	/**
	 * Compare in-memory characters with current disc state and update as necessary.
	 */
	private synchronized static void scanForChanges() {
		Set<File> characterFiles = getCharacterFiles();
		
		// process removals
		for(File characterFile: characters.keySet()) {
			if(!characterFiles.contains(characterFile)) {
				notifyRemovedCharacter(characters.get(characterFile));
				characters.remove(characterFile);
			}
		}
		
		// process changes & additions
		for(File characterFile: characterFiles) {
			if(characters.containsKey(characterFile)) {
				final ICharacter character = characters.get(characterFile);
				if(characterFile.lastModified() > character.getLastModified()) {
					notifyChangedCharacter(character);
				}
			} else {
				try {
					final ICharacter character = new D2SReader(characterFile).parseCharacter();
					notifyAddedCharacter(character);
					characters.put(characterFile, character);
				} catch(IOException ioe) {
					// couldn't parse character - log
				}
			}
		}
	}

	public synchronized static void addListener(IListener listener) {
		listeners.add(listener);
	}
	
	public synchronized static void removeListener(IListener listener) {
		listeners.remove(listener);
	}
	
	private static void notifyAddedCharacter(ICharacter character) {
		for(IListener listener: listeners) {
			listener.addedCharacter(character);
		}
	}
	
	private static void notifyChangedCharacter(ICharacter character) {
		for(IListener listener: listeners) {
			listener.changedCharacter(character);
		}
	}
	
	private static void notifyRemovedCharacter(ICharacter character) {
		for(IListener listener: listeners) {
			listener.removedCharacter(character);
		}
	}
	
}
