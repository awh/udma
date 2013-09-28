package org.antispin.udma.metamodel.impl;

import org.antispin.udma.metamodel.ItemMetadataType;

public final class ArmourMetadata extends AbstractItemMetadata {

	protected ArmourMetadata(String code, String type, String subType) {
		super(code, type, subType);
	}
	
	public ItemMetadataType getItemMetadataType() {
		return ItemMetadataType.ARMOUR;
	}
	
}
