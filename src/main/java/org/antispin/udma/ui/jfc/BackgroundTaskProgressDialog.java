package org.antispin.udma.ui.jfc;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class BackgroundTaskProgressDialog extends JDialog implements IBackgroundTaskListener {

	private class OKAction extends AbstractAction {
		
		public OKAction() {
			super("Ok");
			// remain disabled until the task is done
			setEnabled(false);
		}
		
		public void actionPerformed(ActionEvent actionEvent) {
			BackgroundTaskProgressDialog.this.dispose();
		}
		
	}
	
	private class CancelAction extends AbstractAction {

		public CancelAction() {
			super("Cancel");
		}
		
		public void actionPerformed(ActionEvent actionEvent) {
			// stop user from clicking again
			setEnabled(false);
			
			// ask the task to abort
			BackgroundTaskProgressDialog.this.backgroundTask.cancel();
		}
		
	}
	
	final Action okAction;
	final Action cancelAction;
	final IBackgroundTask backgroundTask;

	final JLabel statusMessageLabel = new JLabel();
	final JProgressBar progressBar = new JProgressBar(0, 100);
	
	public BackgroundTaskProgressDialog(JFrame parent, IBackgroundTask backgroundTask) {
		super(parent);
		this.backgroundTask = backgroundTask;		
		
		this.okAction = new OKAction();
		this.cancelAction = new CancelAction();
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(new JButton(okAction));
		buttonPanel.add(new JButton(cancelAction));
		
		JPanel infoPanel = new JPanel();
		infoPanel.add(statusMessageLabel);
		infoPanel.add(progressBar);

		add(infoPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setSize(new Dimension(300, 200));
	}

	public void beginTaskExecution() {
		backgroundTask.execute(this);
	}
	
	public void setPercentageComplete(int percentage) {
		progressBar.setValue(percentage);
	}

	public void setStatusMessage(String statusMessage) {
		statusMessageLabel.setText(statusMessage);
	}

	public void finished() {
		// prevent cancellation and allow the user to dismiss the dialog
		cancelAction.setEnabled(false);
		okAction.setEnabled(true);
	}
}
