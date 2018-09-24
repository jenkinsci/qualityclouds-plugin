/*
 * © 2015 Quality Clouds Ltd. All Rights Reserved.
 */
package io.jenkins.plugins.servicenow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jasminb.jsonapi.annotations.Id;
import com.github.jasminb.jsonapi.annotations.Type;
/**
 * 
 * @author akramkohansal
 *
 */
/*
 * {
    "meta": {
        "current-page": 1,
        "per-page": 1000,
        "from": 1,
        "to": 1,
        "total": 1,
        "last-page": 1
    },
    "links": {
        "first": "http://dev.qualityclouds.com/api/v2/issue?filter%5Bscan%5D=7f93c281-3a06-4704-b1d4-4117e8386497&filter%5Bseverity%5D=HIGH&page%5Bnumber%5D=1&page%5Bsize%5D=1000",
        "last": "http://dev.qualityclouds.com/api/v2/issue?filter%5Bscan%5D=7f93c281-3a06-4704-b1d4-4117e8386497&filter%5Bseverity%5D=HIGH&page%5Bnumber%5D=1&page%5Bsize%5D=1000"
    },
    "data": [
        {
            "type": "issue",
            "id": "3360981",
            "attributes": {
                "scan-uuid": "7f93c281-3a06-4704-b1d4-4117e8386497",
                "affected-element-sys-id": "https://dev58376.service-now.com//nav_to.do?uri=sys_script.do?sys_id=6ee3fd55db33130080ac2fb748961916",
                "affected-element-name": "QCTestBR",
                "created-by": "admin",
                "created-on": "2018-08-07 14:01:08",
                "updated-by": "admin",
                "updated-on": "2018-08-28 09:29:06",
                "service-now-func": "Custom Applications in Global Scope",
                "configuration-element-type": "Business Rule",
                "severity": "HIGH",
                "impact-area": "SECURITY",
                "issue-type": "JavaScript -  Avoid use of WebDB",
                "line-number": "6",
                "best-practice-link": "https://www.owasp.org/index.php/Top_10_2013-A6-Sensitive_Data_Exposure",
                "hide-issue": false,
                "active": "true"
            },
            "links": {
                "self": "http://dev.qualityclouds.com/api/v2/issue/3360981"
            }
        }
    ]
}
 */
@Type("issue")
public class SNowScanIssuesBreakDownResponse {

	
	@Id
	private String id;

	@JsonProperty("scan-uuid")
	private String scanUUId;
	
	@JsonProperty("affected-element-sys-id")
	private String affectedElementSysId;
	
	@JsonProperty("affected-element-name")
	private String affectedElementName;
	
	@JsonProperty("created-by")
	private String createdBy;
	
	@JsonProperty("created-on")
	private String createdOn;
	
	@JsonProperty("updated-by")
	private String updatedBy;
	
	@JsonProperty("updated-on")
	private String updatedOn;
	
	@JsonProperty("service-now-func")
	private String serviceNowFunc;
	
	@JsonProperty("configuration-element-type")
	private String configurationElementType;
	
	private String severity;
	
	@JsonProperty("impact-area")
	private String impactArea;
	
	@JsonProperty("issue-type")
	private String issueType;
	
	@JsonProperty("line-number")
	private String lineNumber;
	
	@JsonProperty("best-practice-link")
	private String bestPracticeLink;
	
	@JsonProperty("hide-issue")
	private String hideIssue;
	
	private String active;
	
	public SNowScanIssuesBreakDownResponse() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScanUUId() {
		return scanUUId;
	}

	public void setScanUUId(String scanUUId) {
		this.scanUUId = scanUUId;
	}

	public String getAffectedElementSysId() {
		return affectedElementSysId;
	}

	public void setAffectedElementSysId(String affectedElementSysId) {
		this.affectedElementSysId = affectedElementSysId;
	}

	public String getAffectedElementName() {
		return affectedElementName;
	}

	public void setAffectedElementName(String affectedElementName) {
		this.affectedElementName = affectedElementName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getServiceNowFunc() {
		return serviceNowFunc;
	}

	public void setServiceNowFunc(String serviceNowFunc) {
		this.serviceNowFunc = serviceNowFunc;
	}

	public String getConfigurationElementType() {
		return configurationElementType;
	}

	public void setConfigurationElementType(String configurationElementType) {
		this.configurationElementType = configurationElementType;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getImpactArea() {
		return impactArea;
	}

	public void setImpactArea(String impactArea) {
		this.impactArea = impactArea;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getBestPracticeLink() {
		return bestPracticeLink;
	}

	public void setBestPracticeLink(String bestPracticeLink) {
		this.bestPracticeLink = bestPracticeLink;
	}

	public String getHideIssue() {
		return hideIssue;
	}

	public void setHideIssue(String hideIssue) {
		this.hideIssue = hideIssue;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}
	
}
