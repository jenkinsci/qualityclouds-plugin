/*
 * © 2015 Quality Clouds Ltd. All Rights Reserved.
 */
package io.jenkins.plugins.servicenow;

import hudson.model.Run;
import jenkins.model.RunAction2;

public class QCScanFailedResultAction implements RunAction2, ResultAction{
	
	
	private transient Run run;
	private String msg;

	
	public QCScanFailedResultAction(String msg) {
		super();
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String getDisplayName() {
		return "ScanResult";
	}

	@Override
	public String getIconFileName() {
		return "document.png";
	}

	@Override
	public String getUrlName() {
		return "qcscanresult";
	}

	@Override
	public void onAttached(Run<?, ?> run) {
		this.run = run;
		
	}

	@Override
	public void onLoad(Run<?, ?> run) {
		this.run = run;
		
	}
	public Run getRun() {
		return run;
	}


	@Override
	public void setTimeElapse(String scanDuration) {
		// TODO Auto-generated method stub
		
	}

}
