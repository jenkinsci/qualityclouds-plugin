package io.jenkins.plugins.servicenow;

import hidden.jth.org.apache.http.client.ClientProtocolException;
import hudson.util.LogTaskListener;
import io.jenkins.plugins.servicenow.model.SNowScanRestResponse;
import io.jenkins.plugins.servicenow.rest.QCScanRest;
import io.jenkins.plugins.servicenow.util.QCScanResultFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class QCScanRestTest {

	
	private LogTaskListener taskListener = new LogTaskListener(Logger.getGlobal(), Level.INFO);
	
	private QCScanRest qcScanRest = new QCScanRest();
	@Test
	public void testUnAuthorizedToken() {
		
		taskListener.getLogger().println("scan unauthorized call test start");

		Map<String, Object> result = qcScanRest.doScan("1234token", "https://dev58376.service-now.com/", taskListener, 10, 10, 10, 10);

		taskListener.getLogger().println("scan unauthorized call test finished");
		
		int statusCode = (int) result.get(ResultAction.STATUS_CODE);
		assertEquals(statusCode, 401);
		
	}
	
	@Test
	public void testInvalidParameters() {
		
		taskListener.getLogger().println("scan unauthorized call test start");

		Map<String, Object> result = qcScanRest.doScan("1234token", "https://dev58376.service-now.com/", taskListener, 0, 0, 0, 0);

		taskListener.getLogger().println("scan unauthorized call test finished");
		
		int statusCode = (int) result.get(ResultAction.STATUS_CODE);
		assertEquals( 401, statusCode);
		
	}
	
	@Test
	public void testEvaluateScanResultTechnicalDeptNOk() {
		SNowScanRestResponse sNowScanRestResponse = new SNowScanRestResponse();
		sNowScanRestResponse.setTechnicalDebt("8");
		int errorCode = qcScanRest.evaluateScanResult(sNowScanRestResponse, 0, 6, 0);
		assertEquals(QCScanResultFactory.SCAN_FAIL_TECHNICAL_DEBT_CODE, errorCode);
	}
	@Test
	public void testEevaluateScanResultTechnicalDeptOk() {
		SNowScanRestResponse sNowScanRestResponse = new SNowScanRestResponse();
		sNowScanRestResponse.setTechnicalDebt("5");
		int errorCode = qcScanRest.evaluateScanResult(sNowScanRestResponse, 0, 6, 0);
		assertEquals(QCScanResultFactory.SCAN_SUCCESS_CODE, errorCode);
	}
	@Test
	public void testEevaluateScanResultIssuesCountOk() {
		SNowScanRestResponse sNowScanRestResponse = new SNowScanRestResponse();
		sNowScanRestResponse.setNumberOfIssues("8");
		int errorCode = qcScanRest.evaluateScanResult(sNowScanRestResponse, 10, 0, 0);
		assertEquals(QCScanResultFactory.SCAN_SUCCESS_CODE, errorCode);
	}
	@Test
	public void testEevaluateScanResultIssuesCountNOk() {
		SNowScanRestResponse sNowScanRestResponse = new SNowScanRestResponse();
		sNowScanRestResponse.setNumberOfIssues("18");
		int errorCode = qcScanRest.evaluateScanResult(sNowScanRestResponse, 10, 0, 0);
		assertEquals(QCScanResultFactory.SCAN_FAIL_ISSUES_THRESHOLD_CODE, errorCode);
	}
	@Test
	public void testEevaluateScanResultQCOk() {
		SNowScanRestResponse sNowScanRestResponse = new SNowScanRestResponse();
		sNowScanRestResponse.setQualityOfCloud("12");
		int errorCode = qcScanRest.evaluateScanResult(sNowScanRestResponse, 0, 0, 10);
		assertEquals(QCScanResultFactory.SCAN_SUCCESS_CODE, errorCode);
	}
	@Test
	public void testEevaluateScanResultQCNOk() {
		SNowScanRestResponse sNowScanRestResponse = new SNowScanRestResponse();
		sNowScanRestResponse.setQualityOfCloud("8");
		int errorCode = qcScanRest.evaluateScanResult(sNowScanRestResponse, 0, 0, 10);
		assertEquals(QCScanResultFactory.SCAN_FAIL_QUALITYCLOUD_CODE, errorCode);
	}
	//@Test
	public void testHighSeverity() throws Exception{
		SNowScanRestResponse snowScan = new SNowScanRestResponse();
		snowScan.setId("7f93c281-3a06-4704-b1d4-4117e8386497");
		
		HttpClient client = new DefaultHttpClient();
		qcScanRest.checkHighSeverityIssuesCount(snowScan, 10, "mytoken", client, "http://dev.qualityclouds.com/api/v2/issue?filter[scan]=%s&filter[severity]=HIGH");
	}

}
