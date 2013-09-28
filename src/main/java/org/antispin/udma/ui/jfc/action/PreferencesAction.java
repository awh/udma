package org.antispin.udma.ui.jfc.action;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.antispin.udma.ui.jfc.PreferencesDialog;

public class PreferencesAction extends AbstractAction {

	final PreferencesDialog preferencesDialog = new PreferencesDialog();
	
	public PreferencesAction() {
		super("Preferences...");
	}
	
	public void actionPerformed(ActionEvent arg0) {
		preferencesDialog.setSize(new Dimension(400, 300));
		preferencesDialog.setLocationRelativeTo(null);
		preferencesDialog.setVisible(true);

	}

}
