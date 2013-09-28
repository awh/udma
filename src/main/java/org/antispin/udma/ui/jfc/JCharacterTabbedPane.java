package org.antispin.udma.ui.jfc;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.antispin.java.lang.IDisposable;
import org.antispin.udma.model.ICharacter;
import org.antispin.udma.service.D2CharacterService;

public class JCharacterTabbedPane extends JTabbedPane implements IDisposable, D2CharacterService.IListener {

	public JCharacterTabbedPane() {
		D2CharacterService.addListener(this);
	}

	public void dispose() {
		D2CharacterService.removeListener(this);
	}
	
	public void addedCharacter(final ICharacter character) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				JCharacterPanel characterPanel = new JCharacterPanel(character);
				JScrollPane scrollPane = new JScrollPane(characterPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			    scrollPane.setViewportBorder(null);
				addTab(character.getName(), scrollPane);
			}
		});
	}

	public void changedCharacter(ICharacter character) {
		// TODO Auto-generated method stub
	}

	public void removedCharacter(final ICharacter character) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				removeTabAt(getTabIndex(character.getName()));
				revalidate();
			}
		});
	}

	public int getTabIndex(String title) {
		for(int i = 0; i < getTabCount(); ++i) {
			if(title.equals(getTitleAt(i))) {
				return i;
			}
		}
		return -1;
	}
	
	
}
