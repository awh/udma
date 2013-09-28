package org.antispin.udma.persistence.d2s;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.antispin.java.io.BitInputStream;
import org.antispin.java.io.MonitoredInputStream;
import org.antispin.udma.metamodel.IItemMetadata;
import org.antispin.udma.metamodel.IItemMetadataFactory;
import org.antispin.udma.metamodel.ItemMetadataType;
import org.antispin.udma.model.IItem;
import org.antispin.udma.model.factory.IItemFactory;

public class ItemReader {

	public static class ItemDecodeException extends IOException {
		
	}
	
	public static enum BasicLocation {
		STORED, EQUIPPED, BELT, MOUSE, SOCKET;
		
		public static BasicLocation valueOf(int i) {
			return null;
		}
		
	}
	
	public static enum EquippedLocation {
		HEAD, NECK, TORSO, RIGHT_HAND, LEFT_HAND, RIGHT_FINGER, LEFT_FINGER,
		WAIST, FEET, HANDS, ALT_LEFT_HAND, ALT_RIGHT_HAND;
		
		public static EquippedLocation valueOf(int i) {
			return null;
		}
	}
	
	public static enum ExtendedLocation {
		ELSEWHERE, INVENTORY, HORADRIC_CUBE, STASH;
		
		public static ExtendedLocation valueOf(int i) {
			return null;
		}
	}
	
	public static enum ItemQuality {
		LOW, NORMAL, HIGH, MAGIC, SET, RARE, UNIQUE, CRAFTED;
		
		public static ItemQuality valueOf(int i) {
			return null;
		}
	}
	
	public static enum LowQualityItemQuality {
		CRUDE, CRACKED, DAMANAGED, LOW;
		
		public static LowQualityItemQuality valueOf(int i) {
			return null;
		}
	}
	
	private IItemMetadataFactory itemMetadataFactory;
	private final IItemFactory itemFactory;
	private final MonitoredInputStream monitoredInputStream;
	private final BitInputStream bitInputStream;
	private final List<Byte> itemBytes = new ArrayList<Byte>();
	
	public ItemReader(IItemFactory itemFactory, InputStream inputStream) {
		this.itemFactory = itemFactory;
		this.monitoredInputStream = new MonitoredInputStream(inputStream);		
		this.bitInputStream = new BitInputStream(monitoredInputStream);
		
		this.monitoredInputStream.addListener(new MonitoredInputStream.Listener() {
			public void notifyRead(int b) {
				// as we pull bits from the input stream, we
				// simultaneously build an array of consumed bytes
				ItemReader.this.itemBytes.add(Byte.valueOf((byte)b));
			}
		});
	}
	
	/**
	 * Read an ItemState object from the specified stream.
	 * @return
	 * @throws IOException
	 */
	public IItem readItem() throws IOException {
		IItem item  = null;
		itemBytes.clear();
		
		final int magic = readInt(16);
		if(magic != 0x4d4a) {
			throw new IOException("Bad item magic: " + Integer.toHexString(magic));
		}
		
		skip(4);
		final boolean identified = readBoolean();
		skip(6);
		final boolean socketed = readBoolean();
		skip(4);
		final boolean playerEar = readBoolean();
		final boolean startingItem = readBoolean();
		skip(3);
		final boolean simpleItem = readBoolean();
		final boolean ethereal = readBoolean();
		skip(1);
		final boolean personalised = readBoolean();
		skip(1);
		final boolean hasRuneWord = readBoolean();
		skip(15);
		final BasicLocation basicLocation = BasicLocation.valueOf(readInt(3));
		final EquippedLocation equippedLocation = EquippedLocation.valueOf(readInt(4));
		final int column = readInt(4);
		final int row = readInt(4);
		final ExtendedLocation extendedLocation = ExtendedLocation.valueOf(readInt(3));
		if(playerEar) {
			final String code = "ear";
		} else {
			final char[] codeChars = new char[4];
			for(int i = 0; i < 4; ++i) {
				codeChars[i] = (char) readInt(8);
			}

			final String itemCode = String.valueOf(codeChars).trim();						
			final int usedSockets = readInt(3);
			
			if(simpleItem) {
				final Byte[] byteArray = new Byte[itemBytes.size()];
				item = itemFactory.newSimpleItem(itemBytes.toArray(byteArray), itemCode);
			} else {
				final long itemGUID = readLong(32);
				final int itemLevel = readInt(7);
				final ItemQuality itemQuality = ItemQuality.valueOf(readInt(4));
				final boolean hasImageVariant = readBoolean();
				final int imageVariant = readInt(3);
				
				skip(20);
				
				switch(itemQuality) {
				case LOW:
					final LowQualityItemQuality lowQualityItemQuality = LowQualityItemQuality.valueOf(readInt(3));
					break;
				case NORMAL:
					break;
				case HIGH:
					skip(3);
					break;
				case MAGIC:
					final int magicPrefix = readInt(11);
					final int magicSuffix = readInt(11);
				case SET:
					final int setID = readInt(12);
				case RARE:
					final int rareFirstNameIndex = readInt(8);
					final int rareLastNameIndex = readInt(8);
				case UNIQUE:
					final int uniqueID = readInt(12);
				}

				if(hasRuneWord) {
					final int runeWordID = readInt(12);
					skip(4);
				}
				
				if(personalised) {
					
				}
				
				final IItemMetadata itemMetadata = itemMetadataFactory.getItemMetadata(itemCode);
				final int defense = ItemMetadataType.isArmour(itemMetadata) ? readInt(10) : 0;

				
				skip(1);
			}
		}
		
		bitInputStream.skipToByteBoundary();
		
		return item;
	}
	
	private int skip(int bits) throws IOException {
		return bitInputStream.skip(bits);
	}
	
	private boolean readBoolean() throws IOException {
		return bitInputStream.readInt(1) == 1;
	}
	
	private int readInt(int bitCount) throws IOException {
		return bitInputStream.readInt(bitCount);
	}
	
	private long readLong(int bitCount) throws IOException {
		return bitInputStream.readLong(bitCount);
	}
	
}
