package org.antispin.udma.ui.jfc;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.antispin.udma.model.ICharacter;
import org.antispin.udma.resource.D2ResourceFactory;

public class JCharacterPanel extends JPanel {
	
	// character
	final ICharacter character;
	
	// cache to hold desaturated background icons for equip trays
	static final Map<String, Icon> backgroundIconCache = new HashMap<String, Icon>(); 
	
	// clothing
	final JItemTray helmetTray = new JItemTray(2, 2, getBackgroundIcon("invhlmu"));
	final JItemTray armourTray = new JItemTray(3, 2, getBackgroundIcon("invaaru"));
	final JItemTray beltTray = new JItemTray(1, 2, getBackgroundIcon("invmbl"));
	final JItemTray glovesTray = new JItemTray(2, 2, getBackgroundIcon("invlgl"));
	final JItemTray bootsTray = new JItemTray(2, 2, getBackgroundIcon("invmbt"));

	// merc clothing
	final JItemTray mercHelmetTray = new JItemTray(2, 2, getBackgroundIcon("invhlmu"));
	final JItemTray mercArmourTray = new JItemTray(3, 2, getBackgroundIcon("invaaru"));

	// jewelry
	final JItemTray amuletTray = new JItemTray(1, 1, getBackgroundIcon("invamu"));
	final JItemTray leftRingTray = new JItemTray(1, 1, getBackgroundIcon("invrin"));
	final JItemTray rightRingTray = new JItemTray(1, 1, getBackgroundIcon("invrin4"));
	
	// weapons
	final JItemTray leftWeaponTray = new JItemTray(4, 2, getBackgroundIcon("invpa3"));
	final JItemTray rightWeaponTray = new JItemTray(4, 2, getBackgroundIcon("invhal"));
	final JItemTray altLeftWeaponTray = new JItemTray(4, 2, getBackgroundIcon("invpa3"));
	final JItemTray altRightWeaponTray = new JItemTray(4, 2, getBackgroundIcon("invhbw"));

	// merc weapons
	final JItemTray mercLeftWeaponTray = new JItemTray(4, 2, getBackgroundIcon("invpa3"));
	final JItemTray mercRightWeaponTray = new JItemTray(4, 2, getBackgroundIcon("invgix"));

	// storage areas - inventory, private stash, horadric cube and belted potions
	final JItemTray inventoryTray = new JItemTray(4, 10);
	final JItemTray stashTray = new JItemTray(8, 6);
	final JItemTray cubeTray = new JItemTray(4, 3);
	final JItemTray potionTray = new JItemTray(4, 4);
	
	public JCharacterPanel(ICharacter character) {
		this.character = character;
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		add(createEquipPanel());
		add(new JSeparator());
		add(createMercEquipPanel());
		add(new JSeparator());
		add(createInventoryPanel());
	}
	
	/**
	 * Create a panel containing the item trays used for equipping
	 * a character.
	 * 
	 * @return
	 */
	private JPanel createEquipPanel() {
		JPanel equipPanel = new JPanel(new GridBagLayout());

		equipPanel.add(helmetTray,    getConstraints(1, 0, 2, 1));
		equipPanel.add(amuletTray,    getConstraints(1, 1, 2, 1));
		equipPanel.add(armourTray,    getConstraints(1, 2, 2, 1));
		equipPanel.add(rightRingTray, getConstraints(1, 3, 1, 1));
		equipPanel.add(leftRingTray,  getConstraints(2, 3, 1, 1));
		equipPanel.add(beltTray,      getConstraints(1, 4, 2, 1));
		
		equipPanel.add(rightWeaponTray,    getConstraints(0, 0, 1, 2));
		equipPanel.add(altRightWeaponTray, getConstraints(0, 2, 1, 1));
		equipPanel.add(glovesTray,         getConstraints(0, 3, 1, 2));
		
		equipPanel.add(leftWeaponTray,    getConstraints(3, 0, 1, 2));
		equipPanel.add(altLeftWeaponTray, getConstraints(3, 2, 1, 1));
		equipPanel.add(bootsTray,         getConstraints(3, 3, 1, 2));
		
		//equipPanel.setBorder(createTitledBorder("Equipment"));
		
		return equipPanel;
	}
	
	/**
	 * Create a panel containing the item trays used for equipping
	 * a mercenary.
	 * 
	 * @return
	 */
	private JPanel createMercEquipPanel() {
		JPanel mercPanel = new JPanel(new GridBagLayout());
		
		mercPanel.add(mercHelmetTray, getConstraints(1, 0, 1, 1));
		mercPanel.add(mercArmourTray,  getConstraints(1, 1, 1, 1));
		mercPanel.add(mercRightWeaponTray, getConstraints(0, 0, 1, 2));
		mercPanel.add(mercLeftWeaponTray, getConstraints(2, 0, 1, 2));
		
		//mercPanel.setBorder(createTitledBorder("Mercenary"));
		
		return mercPanel;
	}
	
	/**
	 * Create a panel containing the remaining trays -
	 * inventory, stash, horadric cube & belt.
	 * 
	 * @return
	 */
	private JPanel createInventoryPanel() {
		JPanel inventoryPanel = new JPanel(new GridBagLayout());

		inventoryPanel.add(inventoryTray, getConstraints(0, 0, 2, 1));
		inventoryPanel.add(stashTray,     getConstraints(0, 1, 1, 2));
		inventoryPanel.add(potionTray,    getConstraints(1, 1, 1, 1));
		inventoryPanel.add(cubeTray,      getConstraints(1, 2, 1, 1));
		
		//inventoryPanel.setBorder(createTitledBorder("Inventory"));
		
		return inventoryPanel;
	}
	
	private Border createTitledBorder(String title) {
		TitledBorder titledBorder = new TitledBorder(null, title, TitledBorder.CENTER, TitledBorder.TOP, null, null);
		titledBorder.setTitleFont(titledBorder.getTitleFont().deriveFont(Font.ITALIC));
		return titledBorder;
	}
	
	/**
	 * Create grid bag constraints with the specified parameters.
	 * 
	 * @return
	 */
	private GridBagConstraints getConstraints(int gridx, int gridy, int gridwidth, int gridheight) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		gbc.anchor = GridBagConstraints.CENTER;
		return gbc;
	}
	
	/**
	 * Lookup the specified inventory icon, desaturate it and apply a 20%
	 * alpha channel.
	 * 
	 * @param name
	 * @return
	 * @throws IOException 
	 */
	private static Icon getBackgroundIcon(String name) {
		if(!backgroundIconCache.containsKey(name)) {
			try {
				BufferedImage image = D2ResourceFactory.getInventoryImage(name);
				
				WritableRaster wr = image.getRaster();
				int[] samples = new int[4];
				for(int x = 0; x < wr.getWidth(); ++x) {
					for(int y = 0; y < wr.getHeight(); ++y) {
						wr.getPixel(x, y, samples);
						// desaturate
						//samples[0] = samples[1] = samples[2] = (samples[0] + samples[1] + samples[2]) / 3;
						// set alpha on non transparent pixels
						samples[3] = samples[3] == 0 ? 0 : 64;
						wr.setPixel(x, y, samples);
					}
				}
				
				backgroundIconCache.put(name, new ImageIcon(image));
			} catch(IOException ioe) {
				// ignore - we just return a null icon
			}
		}
		return backgroundIconCache.get(name);
	}
}
