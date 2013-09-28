package org.antispin.udma.metamodel.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antispin.udma.metamodel.IItemMetadata;
import org.antispin.udma.metamodel.IItemMetadataFactory;
import org.antispin.udma.resource.ID2ResourceLocator;

/**
 * Item metadata factory which loads lazily from the supplied resource locator,
 * trading increased I/O & CPU for decreased memory footprint.
 * 
 * @author Adam Harrison
 *
 */
public final class LazyItemMetadataFactory implements IItemMetadataFactory {

	protected static final String CODE_COLUMN = "code";
	protected static final String TYPE_COLUMN = "type";
	protected static final String SUBTYPE_COLUMN = "type2";
	
	private final Map<String, IItemMetadata> cache = new HashMap<String, IItemMetadata>();
	private final IItemMetadataFactory[] subcontractors;
	
	private static abstract class AbstractLazyFactory implements IItemMetadataFactory {
		private final ID2ResourceLocator d2ResourceLocator;
		
		public AbstractLazyFactory(ID2ResourceLocator d2ResourceLocator) {
			this.d2ResourceLocator = d2ResourceLocator;
		}
		
		protected ID2ResourceLocator getD2ResourceLocator() {
			return d2ResourceLocator;
		}
		
		public IItemMetadata getItemMetadata(String itemCode) throws IOException {
			final Map<String, String> metadata = findMetadata(getTableInputStream(), itemCode);
			if(metadata == null) {
				return null;
			} else {
				return newItemMetadata(metadata);
			}
		}
		
		/**
		 * Template method to return the correct table from the resource locator.
		 * 
		 * @return
		 */
		protected abstract InputStream getTableInputStream() throws IOException;
		
		
		/**
		 * Template method to instantiate a concrete metadata class from the supplied key value pairs.
		 * 
		 * @param metadata
		 * @return
		 */
		protected abstract IItemMetadata newItemMetadata(Map<String, String> metadata);
		
		/**
		 * Search the table for the specified item.
		 *  
		 * @param table
		 * @param itemCode
		 * @return
		 */
		private Map<String, String> findMetadata(InputStream table, String itemCode) throws IOException {
			final BufferedReader br = new BufferedReader(new InputStreamReader(table));
			final List<String> columnNames = Arrays.asList(br.readLine().split("\t"));
			final int codeIndex = columnNames.indexOf(CODE_COLUMN);
			
			String line;
			while((line = br.readLine()) != null) {
				String[] values = line.split("\t");
				if(values[codeIndex].equals(itemCode)) {
					final Map<String, String> metadata = new HashMap<String, String>();
					for(int i = 0; i < columnNames.size(); ++i) {
						metadata.put(columnNames.get(i), values[i]);
					}
					return metadata;
				}
			}
			
			return null;
		}
	}
	
	private static final class LazyArmourFactory extends AbstractLazyFactory {
		protected LazyArmourFactory(ID2ResourceLocator d2ResourceLocator) {
			super(d2ResourceLocator);
		}
		
		protected InputStream getTableInputStream() throws IOException {
			return getD2ResourceLocator().getArmourResource();
		}

		protected IItemMetadata newItemMetadata(Map<String, String> metadata) {
			final String code = metadata.get(CODE_COLUMN);
			final String type = metadata.get(TYPE_COLUMN);
			final String subType = metadata.get(SUBTYPE_COLUMN);
			return new ArmourMetadata(code, type, subType);
		}
	}
	
	private static final class LazyWeaponFactory extends AbstractLazyFactory {
		protected LazyWeaponFactory(ID2ResourceLocator d2ResourceLocator) {
			super(d2ResourceLocator);
		}
		
		protected InputStream getTableInputStream() throws IOException {
			return getD2ResourceLocator().getWeaponsResource();
		}

		protected IItemMetadata newItemMetadata(Map<String, String> metadata) {
			final String code = metadata.get(CODE_COLUMN);
			final String type = metadata.get(TYPE_COLUMN);
			final String subType = metadata.get(SUBTYPE_COLUMN);
			return new WeaponMetadata(code, type, subType);
		}
	}
	
	private static final class LazyMiscFactory extends AbstractLazyFactory {
		protected LazyMiscFactory(ID2ResourceLocator d2ResourceLocator) {
			super(d2ResourceLocator);
		}
		
		protected InputStream getTableInputStream() throws IOException {
			return getD2ResourceLocator().getMiscResource();
		}
		
		protected IItemMetadata newItemMetadata(Map<String, String> metadata) {
			final String code = metadata.get(CODE_COLUMN);
			final String type = metadata.get(TYPE_COLUMN);
			final String subType = metadata.get(SUBTYPE_COLUMN);
			return new MiscMetadata(code, type, subType);
		}
	}
	
	public LazyItemMetadataFactory(ID2ResourceLocator d2ResourceLocator) {
		this.subcontractors = new IItemMetadataFactory[] {
				new LazyArmourFactory(d2ResourceLocator),
				new LazyWeaponFactory(d2ResourceLocator),
				new LazyMiscFactory(d2ResourceLocator) };
	}
	
	public IItemMetadata getItemMetadata(String itemCode) throws IOException {
		if(!cache.containsKey(itemCode)) {
			cache.put(itemCode, newItemMetadata(itemCode));
		}
		return cache.get(itemCode);
	}

	private IItemMetadata newItemMetadata(String itemCode) throws IOException {
		for(IItemMetadataFactory subcontractor: subcontractors) {
			final IItemMetadata itemMetadata = subcontractor.getItemMetadata(itemCode);
			if(itemMetadata != null) {
				return itemMetadata;
			}
		}
		return null;
	}
	
}
