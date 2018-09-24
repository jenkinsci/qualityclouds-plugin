/*
 * © 2015 Quality Clouds Ltd. All Rights Reserved.
 */
package io.jenkins.plugins.servicenow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hudson.model.Run;
import io.jenkins.plugins.servicenow.model.SNowScanRestResponse;
import jenkins.model.RunAction2;
/**
 * 
 * @author akramkohansal
 *
 */
public class QCScanAction implements RunAction2, ResultAction{

	private transient Run run;
	
	private String bestPracticesNumber;
	private String changesIssuesRatio;
	private String codeLines;
	private String codeChangesIssuesRatio;
	private String codeLinesAdded;
	private String numberOfIssues;
	private String processTime;
	private String qualityOfCloud;
	private String scanDate;
	private String scannedConfigurationElements;
	private String technicalDebt;
	private String url;
	private String target;
	private String scandate;
	private String elapsedTime;
	
	public QCScanAction() {
		super();
	}
	public QCScanAction(SNowScanRestResponse snowScanRestResponse) {
		super();
		this.bestPracticesNumber = snowScanRestResponse.getBestPracticesNumber();
		this.changesIssuesRatio = snowScanRestResponse.getChangesIssuesRatio();
		this.codeLines = snowScanRestResponse.getCodeLines();
		this.codeChangesIssuesRatio = snowScanRestResponse.getCodeChangesIssuesRatio();
		this.codeLinesAdded = snowScanRestResponse.getCodeLinesAdded();
		this.numberOfIssues = snowScanRestResponse.getNumberOfIssues();
		this.processTime = snowScanRestResponse.getProcessTime();
		this.qualityOfCloud = snowScanRestResponse.getQualityOfCloud();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        try {
            Date date = formatter.parse(snowScanRestResponse.getScanDate());
            this.scanDate = date.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
		this.scanDate = snowScanRestResponse.getScanDate();
		this.scannedConfigurationElements = snowScanRestResponse.getScannedConfigurationElements();
		this.technicalDebt = snowScanRestResponse.getTechnicalDebt();
		this.target = snowScanRestResponse.getTarget();
		this.url = snowScanRestResponse.getUrl();
	}
	
	public String getBestPracticesNumber() {
		return bestPracticesNumber;
	}
	public void setBestPracticesNumber(String bestPracticesNumber) {
		this.bestPracticesNumber = bestPracticesNumber;
	}
	public String getChangesIssuesRatio() {
		return changesIssuesRatio;
	}
	public void setChangesIssuesRatio(String changesIssuesRatio) {
		this.changesIssuesRatio = changesIssuesRatio;
	}
	public String getCodeLines() {
		return codeLines;
	}
	public void setCodeLines(String codeLines) {
		this.codeLines = codeLines;
	}
	public String getCodeChangesIssuesRatio() {
		return codeChangesIssuesRatio;
	}
	public void setCodeChangesIssuesRatio(String codeChangesIssuesRatio) {
		this.codeChangesIssuesRatio = codeChangesIssuesRatio;
	}
	public String getCodeLinesAdded() {
		return codeLinesAdded;
	}
	public void setCodeLinesAdded(String codeLinesAdded) {
		this.codeLinesAdded = codeLinesAdded;
	}
	public String getNumberOfIssues() {
		return numberOfIssues;
	}
	public void setNumberOfIssues(String numberOfIssues) {
		this.numberOfIssues = numberOfIssues;
	}
	public String getProcessTime() {
		return processTime;
	}
	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}
	public String getQualityOfCloud() {
		return qualityOfCloud;
	}
	public void setQualityOfCloud(String qualityOfCloud) {
		this.qualityOfCloud = qualityOfCloud;
	}
	public String getScanDate() {
		return scanDate;
	}
	public void setScanDate(String scanDate) {
		this.scanDate = scanDate;
	}
	public String getScannedConfigurationElements() {
		return scannedConfigurationElements;
	}
	public void setScannedConfigurationElements(String scannedConfigurationElements) {
		this.scannedConfigurationElements = scannedConfigurationElements;
	}
	public String getTechnicalDebt() {
		return technicalDebt;
	}
	public void setTechnicalDebt(String technicalDebt) {
		this.technicalDebt = technicalDebt;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	
	public String getScandate() {
		return scandate;
	}
	public void setScandate(String scandate) {
		this.scandate = scandate;
	}
	
	public String getElapsedTime() {
		return elapsedTime;
	}
	private void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime;
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
		setElapsedTime(scanDuration);
	}

}
