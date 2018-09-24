/*
 * © 2015 Quality Clouds Ltd. All Rights Reserved.
 */
package io.jenkins.plugins.servicenow.model;

import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jasminb.jsonapi.annotations.Id;
import com.github.jasminb.jsonapi.annotations.Type;


@Type("scan")
public class SNowScanRestResponse {

	@Id
	private String id;
	private String url;
	private String target;
	
	@JsonProperty("customer-id")
	private String customerId;
	
	@JsonProperty("user-id")
	private String userId;
	
	@JsonProperty("scan-date")
	private String scanDate;
	
	@JsonProperty("internal-code")
	private String internalCode;
	
	@JsonProperty("exit-code")
	private String exitCode;
	
	private String status;
	
	@JsonProperty("process-time")
	private String processTime;
	
	@JsonProperty("number-of-issues")
	private String numberOfIssues;
	
	@JsonProperty("technical-debt")
	private String technicalDebt;
	
	@JsonProperty("quality-of-cloud")
	private String qualityOfCloud;
	
	@JsonProperty("best-practices-number")
	private String bestPracticesNumber;
	
	@JsonProperty("scanned-configuration-elements")
	private String scannedConfigurationElements;
	
	@JsonProperty("code-lines")
	private String codeLines;
	
	@JsonProperty("changes-issues-ratio")
	private String changesIssuesRatio;
	
	@JsonProperty("code-changes-issues-ratio")
	private String codeChangesIssuesRatio;
	
	private String administrators;
	
	private String users;
	
	@JsonProperty("instance-build-date")
	private String instanceBuildDate;
	
	@JsonProperty("last-upgrade-date")
	private String lastUpgradeDate;
	
	@JsonProperty("last-update-set-date")
	private String lastUpdateSetDate;
	
	@JsonProperty("issues-variation")
	private String issuesVariation;
	
	@JsonProperty("technical-debt-variation")
	private String technicalDebtVariation;
	
	@JsonProperty("quality-of-cloud-variation")
	private String qualityOfCloudVariation;
	
	@JsonProperty("code-lines-added")
	private String codeLinesAdded;
	
	@JsonProperty("engine-version")
	private String engineVersion;
		
	
	public SNowScanRestResponse() {
		super();
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
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


	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getScanDate() {
		return scanDate;
	}


	public void setScanDate(String scanDate) {
		this.scanDate = scanDate;
	}


	public String getInternalCode() {
		return internalCode;
	}


	public void setInternalCode(String internalCode) {
		this.internalCode = internalCode;
	}


	public String getExitCode() {
		return exitCode;
	}


	public void setExitCode(String exitCode) {
		this.exitCode = exitCode;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getProcessTime() {
		return processTime;
	}


	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}


	public String getNumberOfIssues() {
		return numberOfIssues;
	}


	public void setNumberOfIssues(String numberOfIssues) {
		this.numberOfIssues = numberOfIssues;
	}


	public String getTechnicalDebt() {
		return technicalDebt;
	}


	public void setTechnicalDebt(String technicalDebt) {
		this.technicalDebt = technicalDebt;
	}


	public String getQualityOfCloud() {
		return qualityOfCloud;
	}


	public void setQualityOfCloud(String qualityOfCloud) {
		this.qualityOfCloud = qualityOfCloud;
	}


	public String getBestPracticesNumber() {
		return bestPracticesNumber;
	}


	public void setBestPracticesNumber(String bestPracticesNumber) {
		this.bestPracticesNumber = bestPracticesNumber;
	}


	public String getScannedConfigurationElements() {
		return scannedConfigurationElements;
	}


	public void setScannedConfigurationElements(String scannedConfigurationElements) {
		this.scannedConfigurationElements = scannedConfigurationElements;
	}


	public String getCodeLines() {
		return codeLines;
	}


	public void setCodeLines(String codeLines) {
		this.codeLines = codeLines;
	}


	public String getChangesIssuesRatio() {
		return changesIssuesRatio;
	}


	public void setChangesIssuesRatio(String changesIssuesRatio) {
		this.changesIssuesRatio = changesIssuesRatio;
	}


	public String getCodeChangesIssuesRatio() {
		return codeChangesIssuesRatio;
	}


	public void setCodeChangesIssuesRatio(String codeChangesIssuesRatio) {
		this.codeChangesIssuesRatio = codeChangesIssuesRatio;
	}


	public String getAdministrators() {
		return administrators;
	}


	public void setAdministrators(String administrators) {
		this.administrators = administrators;
	}


	public String getUsers() {
		return users;
	}


	public void setUsers(String users) {
		this.users = users;
	}


	public String getInstanceBuildDate() {
		return instanceBuildDate;
	}


	public void setInstanceBuildDate(String instanceBuildDate) {
		this.instanceBuildDate = instanceBuildDate;
	}


	public String getLastUpgradeDate() {
		return lastUpgradeDate;
	}


	public void setLastUpgradeDate(String lastUpgradeDate) {
		this.lastUpgradeDate = lastUpgradeDate;
	}


	public String getLastUpdateSetDate() {
		return lastUpdateSetDate;
	}


	public void setLastUpdateSetDate(String lastUpdateSetDate) {
		this.lastUpdateSetDate = lastUpdateSetDate;
	}


	public String getIssuesVariation() {
		return issuesVariation;
	}


	public void setIssuesVariation(String issuesVariation) {
		this.issuesVariation = issuesVariation;
	}


	public String getTechnicalDebtVariation() {
		return technicalDebtVariation;
	}


	public void setTechnicalDebtVariation(String technicalDebtVariation) {
		this.technicalDebtVariation = technicalDebtVariation;
	}


	public String getQualityOfCloudVariation() {
		return qualityOfCloudVariation;
	}


	public void setQualityOfCloudVariation(String qualityOfCloudVariation) {
		this.qualityOfCloudVariation = qualityOfCloudVariation;
	}


	public String getCodeLinesAdded() {
		return codeLinesAdded;
	}


	public void setCodeLinesAdded(String codeLinesAdded) {
		this.codeLinesAdded = codeLinesAdded;
	}


	public String getEngineVersion() {
		return engineVersion;
	}


	public void setEngineVersion(String engineVersion) {
		this.engineVersion = engineVersion;
	}
	//@XmlTransient
	
}
