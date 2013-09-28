package org.antispin.udma.model.impl;

import org.antispin.udma.model.IItem;

public class Item implements IItem {

	private long id;
	private String itemCode;
	private Byte[] itemBytes; 
	
//	private boolean identified;
//	private boolean socketed;
//	private boolean ethereal;
//	private boolean hasRuneWord;
//	private BasicLocation basicLocation;
//	private EquippedLocation equippedLocation;
//	private int column;
//	private int row;
//	private ExtendedLocation extendedLocation;
//	private int usedSockets;
	
	/**
	 * Hibernate constructor.
	 */
	protected Item() {
	}
	
	protected Item(Byte[] itemBytes, String itemCode) {
		this.itemBytes = itemBytes;
		this.itemCode = itemCode;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Byte[] getItemBytes() {
		return itemBytes;
	}

	public void setItemBytes(Byte[] itemBytes) {
		this.itemBytes = itemBytes;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
//	protected Item(
//			boolean identified,
//			boolean socketed,
//			boolean ethereal,
//			boolean hasRuneWord,
//			BasicLocation basicLocation,
//			EquippedLocation equippedLocation,
//			int column,
//			int row,
//			ExtendedLocation extendedLocation,
//			String itemCode,
//			int usedSockets) {
//		this.identified = identified;
//		this.socketed = socketed;
//		this.ethereal = ethereal;
//		this.hasRuneWord = hasRuneWord;
//		this.basicLocation = basicLocation;
//		this.equippedLocation = equippedLocation;
//		this.column = column;
//		this.row = row;
//		this.extendedLocation = extendedLocation;
//		this.itemCode = itemCode;
//		this.usedSockets = usedSockets;
//	}

	
}
