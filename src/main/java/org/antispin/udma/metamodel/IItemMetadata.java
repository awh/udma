package org.antispin.udma.metamodel;

import java.awt.Image;

public interface IItemMetadata {

	ItemMetadataType getItemMetadataType();
	
	Image getImage();
	String getType();
	String getSubType();
	
}
