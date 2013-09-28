package org.antispin.udma.model.impl;

import java.util.List;

import org.antispin.udma.model.ICharacter;
import org.antispin.udma.model.IItem;

public class Character implements ICharacter {

	String name;
	List<IItem> items;
	long lastModified;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<IItem> getItems() {
		return items;
	}
	
	public void setItems(List<IItem> items) {
		this.items = items;
	}
	
	public long getLastModified() {
		return lastModified;
	}
	
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

}
