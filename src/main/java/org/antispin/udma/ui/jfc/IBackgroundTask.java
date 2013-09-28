package org.antispin.udma.ui.jfc;

public interface IBackgroundTask {

	void execute(IBackgroundTaskListener backgroundTaskListener);
	void cancel();
	
	boolean canReportPercentageComplete();
	boolean canReportStatus();
	
}
