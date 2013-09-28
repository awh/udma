package org.antispin.udma.metamodel;

public enum ItemMetadataType {
	ARMOUR, WEAPON, MISC;

	public static boolean isArmour(IItemMetadata itemMetadata) {
		return itemMetadata.getItemMetadataType() == ARMOUR;
	}
	
	public static boolean isWeapon(IItemMetadata itemMetadata) {
		return itemMetadata.getItemMetadataType() == WEAPON;
	}
}
