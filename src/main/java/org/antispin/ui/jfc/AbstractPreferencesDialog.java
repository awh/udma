package org.antispin.ui.jfc;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

public abstract class AbstractPreferencesDialog extends JDialog {

	final JToolBar radioBar = new JToolBar();
	final ButtonGroup radioButtonGroup = new ButtonGroup();
	final CardLayout cardLayout = new CardLayout();
	final JPanel preferencePanelStack = new JPanel(cardLayout);
	final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	final JButton okButton = new JButton(new AbstractAction("Ok") {
		public void actionPerformed(ActionEvent e) {
			ok();
		}
	});
	
	final JButton applyButton = new JButton(new AbstractAction("Apply") {
		public void actionPerformed(ActionEvent e) {
			apply();
		}
	});
	
	final JButton cancelButton = new JButton(new AbstractAction("Cancel") {
		public void actionPerformed(ActionEvent e) {
			AbstractPreferencesDialog.this.dispose();
		}
	});
	
	public AbstractPreferencesDialog(JFrame owner, String title) {
		super(owner, title, true);
		radioBar.setFloatable(false);

		buttonPanel.add(okButton);
		buttonPanel.add(applyButton);
		buttonPanel.add(cancelButton);
		
		add(radioBar, BorderLayout.NORTH);
		add(preferencePanelStack, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
				
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
	}
	
	public void addPreferencesPanel(final String label, final Icon icon, final JPanel preferencePanel) {
		final JToggleButton button = new JToggleButton(new AbstractAction(label, icon) {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(preferencePanelStack, label);
			}
			
		});

		button.setBorderPainted(false);
		button.setFocusPainted(false);
		//button.setContentAreaFilled(false);
		
		// automatically select first button
		if(radioButtonGroup.getButtonCount() == 0) {
			button.setSelected(true);
		}
		
		radioBar.add(button);
		radioButtonGroup.add(button);
		preferencePanelStack.add(preferencePanel, label);		
	}
	
	public abstract void ok();
	
	public abstract void apply();
}
