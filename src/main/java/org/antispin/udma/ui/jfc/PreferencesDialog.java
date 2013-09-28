package org.antispin.udma.ui.jfc;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.antispin.udma.service.PreferenceService;
import org.antispin.ui.jfc.AbstractPreferencesDialog;

public class PreferencesDialog extends AbstractPreferencesDialog {

	class D2LODPanel extends JPanel {
		
		final JTextField installationPathField = new JTextField();
		final JTextField characterPathField = new JTextField();
		
		final JFileChooser fileChooser = new JFileChooser();
		
		public D2LODPanel() {
			super(new GridBagLayout());
			
			installationPathField.setText(PreferenceService.getString(PreferenceService.Preference.D2LOD_INSTALLATION_PATH));
			characterPathField.setText(PreferenceService.getString(PreferenceService.Preference.D2LOD_CHARACTER_PATH));
			
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			add(new JLabel("Installation path", SwingConstants.RIGHT), getConstraints(0, 0, 1, 1));
			add(installationPathField, getConstraints(1, 0, 1, 1));
			add(new JButton(new AbstractAction("...") {
				public void actionPerformed(ActionEvent e) {
					if(fileChooser.showDialog(D2LODPanel.this, "Select") == JFileChooser.APPROVE_OPTION) {
						installationPathField.setText(fileChooser.getSelectedFile().getPath());
					}
				}
			}), getConstraints(2, 0, 1, 1));
			
			add(new JLabel("Character path", SwingConstants.RIGHT), getConstraints(0, 1, 1, 1));
			add(characterPathField, getConstraints(1, 1, 1, 1));
			add(new JButton(new AbstractAction("...") {
				public void actionPerformed(ActionEvent e) {
					if(fileChooser.showDialog(D2LODPanel.this, "Select") == JFileChooser.APPROVE_OPTION) {
						characterPathField.setText(fileChooser.getSelectedFile().getPath());
					}
				}
			}), getConstraints(2, 1, 1, 1));
		}
		
	}
	
	class StashPanel extends JPanel {
		
		public StashPanel() {
			super(new GridBagLayout());
			
		}
	}
	
	final D2LODPanel d2LODPanel = new D2LODPanel();
	final StashPanel stashPanel = new StashPanel();
	
	public PreferencesDialog() {
		super(null, "Udma Preferences");
		addPreferencesPanel("D2:LoD", null, d2LODPanel);
		addPreferencesPanel("Stash", null, stashPanel);
	}
	
	public void ok() {
		apply();
		dispose();
	}
	
	public void apply() {
		PreferenceService.setFile(PreferenceService.Preference.D2LOD_INSTALLATION_PATH, new File(d2LODPanel.installationPathField.getText()));
		PreferenceService.setFile(PreferenceService.Preference.D2LOD_CHARACTER_PATH, new File(d2LODPanel.characterPathField.getText()));
		
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
		gbc.fill = GridBagConstraints.HORIZONTAL;
		return gbc;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PreferencesDialog pd = new PreferencesDialog();
		pd.setSize(new Dimension(400, 300));
		pd.setLocationRelativeTo(null);
		pd.setVisible(true);
	}

}
