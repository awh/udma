package org.antispin.udma.ui.jfc;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import org.antispin.java.lang.IDisposable;
import org.antispin.udma.model.IItem;
import org.antispin.udma.model.repository.IItemRepository;
import org.antispin.udma.resource.InventoryIconCache;

public class JItemList extends JList implements IDisposable, IItemRepository.Listener {

	private static final class ItemRenderer extends JLabel implements ListCellRenderer {

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			IItem item = (IItem) value;
			
			setText(item.getItemCode());
			setIcon(InventoryIconCache.getNormalIcon(item.getItemCode()));
			setOpaque(true);

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			return this;
	    }
	}
	
	final IItemRepository itemRepository;
	
	public JItemList(IItemRepository itemRepository) {
		this.itemRepository = itemRepository;
		this.itemRepository.addListener(this);
		setCellRenderer(new ItemRenderer());
		setListData(itemRepository.toArray());
	}
	
	public void dispose() {
		itemRepository.removeListener(this);
	}

	public void notifyRepositoryChange() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setListData(itemRepository.toArray());
			}
		});
	}
	
}
