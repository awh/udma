package org.antispin.udma.model.impl;

import org.antispin.udma.model.GemQuality;
import org.antispin.udma.model.GemType;
import org.antispin.udma.model.IGem;

public class Gem extends Item implements IGem {

	private GemType gemType;
	private GemQuality gemQuality;
	
	protected Gem(GemType gemType, GemQuality gemQuality) {
		super(null, null);
		this.gemType = gemType;
		this.gemQuality = gemQuality;
	}

	public GemType getGemType() {
		return this.gemType;
	}
	
	public GemQuality getGemQuality() {
		return this.gemQuality;
	}
	
}
