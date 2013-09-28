package org.antispin.udma.metamodel.impl;

import java.awt.Image;

import org.antispin.udma.metamodel.IItemMetadata;

public abstract class AbstractItemMetadata implements IItemMetadata {

	private final String code;
	private final String type;
	private final String subType;
	
	protected AbstractItemMetadata(String code, String type, String subType) {
		this.code = code;
		this.type = type;
		this.subType = subType;
	}
	
	public String getCode() {
		return code;
	}

	public String getType() {
		return type;
	}

	public String getSubType() {
		return subType;
	}

	public Image getImage() {
		throw new UnsupportedOperationException();
	}
	
}
