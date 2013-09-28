package org.antispin.udma.ui.jfc;

public interface IBackgroundTaskListener {

	void finished();
	
	void setPercentageComplete(int percentage);
	void setStatusMessage(String statusMessage); 
	
}
