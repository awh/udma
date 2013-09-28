package org.antispin.udma.ui.jfc;

import javax.swing.SwingUtilities;


public abstract class AbstractBackgroundTask extends Thread implements IBackgroundTask {

	private boolean canReportPercentageComplete = false;
	private boolean canReportStatus = false;
	private boolean cancelled = false;
	private IBackgroundTaskListener backgroundTaskListener;
	
	AbstractBackgroundTask(boolean canReportPercentageComplete, boolean canReportStatus) {
		this.canReportPercentageComplete = canReportPercentageComplete;
		this.canReportStatus = canReportStatus;
	}
	
	public boolean canReportPercentageComplete() {
		return canReportPercentageComplete;
	}

	public boolean canReportStatus() {
		return canReportStatus;
	}

	public void cancel() {
		cancelled = true;
	}

	boolean isCancelled() {
		return cancelled;
	}
	
	void notifyFinished() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				backgroundTaskListener.finished();
			}
		});
	}
	
	void notifyPercentageComplete(final int percentage) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				backgroundTaskListener.setPercentageComplete(percentage);
			}
		});
		
	}
	
	void notifyStatus(final String statusMessage) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				backgroundTaskListener.setStatusMessage(statusMessage);
			}
		});		
	}
	
	public void execute(IBackgroundTaskListener backgroundTaskListener) {
		this.backgroundTaskListener = backgroundTaskListener;
		start();
	}
	
	public void run() {
		try {
			execute();
		} catch(Exception e) {
			e.printStackTrace();
			notifyStatus("EXCEPTION: " + e.getMessage());
		}
		notifyFinished();
	}
	
	public abstract void execute() throws Exception;

}
