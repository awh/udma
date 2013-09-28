package org.antispin.udma.model;

import java.util.List;


public interface ICharacter {
	
	String getName();
	List<IItem> getItems();
	long getLastModified();
	
}
