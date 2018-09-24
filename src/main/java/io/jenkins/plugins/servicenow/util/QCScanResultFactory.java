/*
 * © 2015 Quality Clouds Ltd. All Rights Reserved.
 */
package io.jenkins.plugins.servicenow.util;

import io.jenkins.plugins.servicenow.*;
import io.jenkins.plugins.servicenow.model.SNowScanRestResponse;

import java.util.Map;
/**
 * 
 * @author akramkohansal
 *
 */
public class QCScanResultFactory {
	
	private int statusCode;

	private final Map<String, Object> scanResult;
	
	public QCScanResultFactory(Map<String, Object> scanResult) {
		super();
		this.scanResult = scanResult;
	}
	
	public ResultAction getResultAction() {
		
		statusCode = (int) getScanResult().get(ResultAction.STATUS_CODE);
		
		switch(statusCode) {
			case 200:
				return new QCScanAction((SNowScanRestResponse) getScanResult().get("object"));
			case 400:
				return new QCScanFailedResultAction(INVALID_DATA);
			case 401:
				return new QCScanUnAuthenticatedUserAction(UNAUTHENTICATED);
			case 403:
				return new QCScanUnAuthorizedUserAction(UNAUTHORIZED_USER);
			case 404:
				return new QCScanFailedResultAction(SCAN_NOT_FOUND);
			case 422:
				return new QCScanFailedResultAction(INVALID_DATA);
			case 10000:
				return new QCScanFailedResultAction(SCAN_FAIL);
			case 10100:
				return new QCScanFailedResultAction(SCAN_FORM_VALIDATION_FAIL);
			case 10200:
				return new QCScanFailedResultAction(SCAN_FAIL);
			case 10300:
				return new QCScanFailedResultAction(SCAN_FAIL_ISSUES_THRESHOLD);
			case 10400:
				return new QCScanFailedResultAction(SCAN_FAIL_TECHNICAL_DEPT);
			case 10500:
				return new QCScanFailedResultAction(SCAN_FAIL_QUALITYCLOUD);
			case 10600:
				return new QCScanFailedResultAction(SCAN_FAIL_HIGH_SEVERITY);
			default:
				return new QCScanFailedResultAction(INTERNAL_ERROR);
				
		}
	}

	public int getStatusCode() {
		return statusCode;
	}


	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}


	private Map<String, Object> getScanResult() {
		return scanResult;
	}

	private static final String CONTACT_QC_SUPPORT = " If the error persists, please contact Quality Clouds support at" +
            " help@qualityclouds.com";

	private static final String INVALID_DATA = "The server could not understand the request." + CONTACT_QC_SUPPORT;
	private static final String INTERNAL_ERROR = "Unexpected Error." + CONTACT_QC_SUPPORT;
	private static final String UNAUTHENTICATED = "Authentication Error. Check your API key and Quality Clouds " +
            "subscription." + CONTACT_QC_SUPPORT;
	private static final String UNAUTHORIZED_USER = "Unauthorized to launch scan. Check your API key and Quality Clouds " +
            "subscription." + CONTACT_QC_SUPPORT;
	private static final String SCAN_NOT_FOUND = "Scan id not found." + CONTACT_QC_SUPPORT;
	private static final String SCAN_FAIL = "Scan Failed." + CONTACT_QC_SUPPORT;
	private static final String SCAN_FORM_VALIDATION_FAIL = "Invalid Input parameters, please make " +
            "sure all values are correct, then run build again.";
	private static final String SCAN_FAIL_ISSUES_THRESHOLD = "Issues Count Threshold Exceeded.";
	private static final String SCAN_FAIL_TECHNICAL_DEPT = "Technical Dept Threshold Exceeded.";
	private static final String SCAN_FAIL_QUALITYCLOUD = "Quality of Cloud is under specified Threshold.";
	private static final String SCAN_FAIL_HIGH_SEVERITY = "High Severity Issues Threshold Exceeded.";


	public static final int SCAN_SUCCESS_CODE = 200;
	public static final int SCAN_FORM_VALIDATION_FAIL_CODE = 10100;
	public static final int SCAN_FAIL_CODE = 10200;
	public static final int SCAN_FAIL_ISSUES_THRESHOLD_CODE = 10300;
	public static final int SCAN_FAIL_TECHNICAL_DEBT_CODE = 10400;
	public static final int SCAN_FAIL_QUALITYCLOUD_CODE = 10500;
	public static final int SCAN_FAIL_HIGH_SEVERITY_CODE = 10600;
	
}
