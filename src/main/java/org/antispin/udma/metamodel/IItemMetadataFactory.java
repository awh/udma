package org.antispin.udma.metamodel;

import java.io.IOException;

public interface IItemMetadataFactory {

	IItemMetadata getItemMetadata(String itemCode) throws IOException;
	
}
