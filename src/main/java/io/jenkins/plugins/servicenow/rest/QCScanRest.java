/*
 * © 2015 Quality Clouds Ltd. All Rights Reserved.
 */
package io.jenkins.plugins.servicenow.rest;

import com.github.jasminb.jsonapi.JSONAPIDocument;
import com.github.jasminb.jsonapi.ResourceConverter;
import hudson.model.TaskListener;
import io.jenkins.plugins.servicenow.model.SNowScanIssuesBreakDownResponse;
import io.jenkins.plugins.servicenow.model.SNowScanRestResponse;
import io.jenkins.plugins.servicenow.util.QCScanResultFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
/**
 * 
 * @author akramkohansal
 * 
 * This class is to make connection to QC API, run scan and fetch the result. 
 *
 */
public class QCScanRest {
	
	/**
	 * This method is responsible to do scan 
	 * @param credential  The API key
	 * @param instanceUrl The URL to scan
	 * @param taskListener The task Listener - logging, etc
	 * @param issuesCountThreshold Number of issues to fail the build
	 * @param techDebtThreshold Technical debt minutes to fail the build
	 * @param qcThreshold Minimum Quality of Cloud to fail the build
	 * @param highSeverityThreshold Number of high severity issues to fail the build
	 * @return The results of the scan
	 */
	public  Map<String, Object> doScan(String credential, String instanceUrl, TaskListener taskListener, int issuesCountThreshold, int techDebtThreshold, int qcThreshold, int highSeverityThreshold) {
		
		HttpClient client = new DefaultHttpClient();
        
        Map<String, Object> result = new HashMap<>(); 
        result.put("StatusCode", 10000);
        /*
    		DateFormat df = new SimpleDateFormat("hh:mm:ss EEEEEEE, dd MMMMMMMMM, yyyy");
		Date date = new Date();
		*/
		if(credential == null || credential.isEmpty() || instanceUrl == null || instanceUrl.isEmpty()) {
			
			taskListener.getLogger().println("------ InValid  Parameter ");
			
			taskListener.getLogger().println("credential --" + credential);
			taskListener.getLogger().println("instanceUrl --" + instanceUrl);
			taskListener.getLogger().println("issuesCountThreshold --" + issuesCountThreshold);
			taskListener.getLogger().println("techDebtThreshold --" + techDebtThreshold);
			taskListener.getLogger().println("qcThreshold --" + qcThreshold);
			taskListener.getLogger().println("highSeverityThreshold --" + highSeverityThreshold);
			
			result.put("StatusCode", QCScanResultFactory.SCAN_FORM_VALIDATION_FAIL_CODE);
    		
    		return result;
		}
		
		StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Properties prop = new Properties();

		try {

			prop.load(QCScanRest.class.getClassLoader().getResourceAsStream("qcscan.properties"));

		} catch (IOException e) {
			e.printStackTrace();
		}
        String scanUrl = prop.getProperty("qc.scanurl");
        String issueUrl = prop.getProperty("qc.issueurl");
        
		if (scanUrl == null || issueUrl == null) {

			result.put("StatusCode", QCScanResultFactory.SCAN_FAIL_CODE);

			return result;
		}
        HttpPost post;
        HttpGet get;
        try {
        	 	
	        	ResourceConverter converter = new ResourceConverter(SNowScanRestResponse.class);

	        	post = new HttpPost(scanUrl);
        		post.setHeader("Content-Type", "application/vnd.api+json");
        		post.setHeader("Accept", "application/vnd.api+json");
        		post.setHeader("Authorization", "Bearer " + credential);
        		JSONObject json = createInputEntity(instanceUrl);

        		StringEntity params = new StringEntity(json.toString());
        		post.setEntity(params);

        		HttpResponse response = client.execute(post);

			if (200 == (response.getStatusLine().getStatusCode())
					|| 202 == (response.getStatusLine().getStatusCode())) {

				JSONAPIDocument<SNowScanRestResponse> scanDocument = converter
						.readDocument(response.getEntity().getContent(), SNowScanRestResponse.class);
				SNowScanRestResponse snowScan = scanDocument.get();

				get = new HttpGet(scanUrl + "/" + snowScan.getId());
				get.setHeader("Content-Type", "application/vnd.api+json");
				get.setHeader("Authorization", "Bearer " + credential);

				response = client.execute(get);

				scanDocument = converter.readDocument(response.getEntity().getContent(), SNowScanRestResponse.class);
				snowScan = scanDocument.get();

				if ("FAIL".equalsIgnoreCase(snowScan.getStatus())) {

					result.put("StatusCode", 10000);
					return result;

				} else if ("SUCCESS".equalsIgnoreCase(snowScan.getStatus())) {

					//result.put("StatusCode", response.getStatusLine().getStatusCode());

					if (200 == (response.getStatusLine().getStatusCode())) {
						result.put("object", snowScan);
					}

				}
				result.put("StatusCode", response.getStatusLine().getStatusCode());

				while (snowScan.getStatus() == null || "RUNNING".equalsIgnoreCase(snowScan.getStatus())) {

					taskListener.getLogger().println("scan is running.....");
					Thread.sleep(6000);

					response = client.execute(get);

					scanDocument = converter.readDocument(response.getEntity().getContent(),
							SNowScanRestResponse.class);
					snowScan = scanDocument.get();

				}
				if ("FAIL".equalsIgnoreCase(snowScan.getStatus())) {
					
					result.put("StatusCode", 10000);
					return result;
					
				} else if ("SUCCESS".equalsIgnoreCase(snowScan.getStatus())) {
					
					int validationCode = evaluateScanResult(snowScan, issuesCountThreshold, techDebtThreshold, qcThreshold);
					
					if (QCScanResultFactory.SCAN_SUCCESS_CODE == (response.getStatusLine().getStatusCode())) {

						if (validationCode == QCScanResultFactory.SCAN_SUCCESS_CODE) {

							int highSeverityCodeCheck = QCScanResultFactory.SCAN_SUCCESS_CODE;

							if (highSeverityThreshold > 0) {

								highSeverityCodeCheck = checkHighSeverityIssuesCount(snowScan, highSeverityThreshold,
										credential, client, issueUrl);
							}

							if (QCScanResultFactory.SCAN_SUCCESS_CODE != highSeverityCodeCheck) {
								result.put("StatusCode", highSeverityCodeCheck);
								return result;
							}

							result.put("StatusCode", response.getStatusLine().getStatusCode());

							result.put("object", snowScan);

							taskListener.getLogger().println("url is ----" + snowScan.getUrl());
							taskListener.getLogger().println("target is ----" + snowScan.getTarget());

						} else {

							result.put("StatusCode", validationCode);
						}
					}

				}
			} else {
				result.put("StatusCode", response.getStatusLine().getStatusCode());
				
			}
        }catch (IOException e) {
        	
            e.printStackTrace();
            
            result.put("StatusCode", QCScanResultFactory.SCAN_FAIL_CODE);
            
        } catch (InterruptedException e) {
        	
        		result.put("StatusCode", QCScanResultFactory.SCAN_FAIL_CODE);
        		
			e.printStackTrace();
		}finally {
  	      
  	      client.getConnectionManager().shutdown();
  	      
  	    }
        return result;
    }

	/**
	 * This method is to create HTTP headers by setting Authorization Bearer
	 * @param  token The API token
	 * @return Inited http headers
	 */
	private HttpHeaders buildRequestHeaders(String token) {

		HttpHeaders headers = new HttpHeaders();

		headers.add("Authorization", "Bearer " + token);
		headers.add("Accept", "application/vnd.api+json");
		headers.add("Content-Type", "application/vnd.api+json");

		return headers;
	}
	private  JSONObject createInputEntity(String instanceUrl) {
		
		JSONObject json= new JSONObject();
		JSONObject data= new JSONObject();
		JSONObject attr = new JSONObject();
		attr.put("url", instanceUrl);
		//attr.put("urlId", 204);
		attr.put("scanType", 2);
		data.put("attributes", attr);
		data.put("type", "scan");
		json.put("data", data);
		return json;
	}
	/**
	 * 
	 * @param snowScan The results of the scan
	 * @param issuesCountThreshold Number of issues to fail the build
	 * @param techDebtThreshold Technical debt to fail the build
	 * @param qcThreshold Minimum Quality of Cloud to fail the build
	 * @return The scan result code
	 */
	public  int evaluateScanResult(SNowScanRestResponse snowScan, int issuesCountThreshold, int techDebtThreshold, int qcThreshold) {
		
		if(issuesCountThreshold > 0 &&(snowScan.getNumberOfIssues() != null && Integer.parseInt(snowScan.getNumberOfIssues()) > issuesCountThreshold)) {
			
			return QCScanResultFactory.SCAN_FAIL_ISSUES_THRESHOLD_CODE;
		}
		if(qcThreshold > 0 && (snowScan.getQualityOfCloud() != null && Double.parseDouble(snowScan.getQualityOfCloud()) < qcThreshold)) {
			
			return QCScanResultFactory.SCAN_FAIL_QUALITYCLOUD_CODE;
		}
		if(techDebtThreshold > 0 && (snowScan.getTechnicalDebt() != null && Double.parseDouble(snowScan.getTechnicalDebt()) > techDebtThreshold)) {
			
			return QCScanResultFactory.SCAN_FAIL_TECHNICAL_DEBT_CODE;
		}
		else {
			
			return QCScanResultFactory.SCAN_SUCCESS_CODE;
		}

	}

	public int checkHighSeverityIssuesCount(SNowScanRestResponse snowScan, int highSeverityThreshold,
			String credential, HttpClient client, String issueUrl) throws IOException {
		
		String url = String.format(issueUrl, snowScan.getId());
		
		HttpGet get = new HttpGet(url);
		get.setHeader("Content-Type", "application/vnd.api+json");
		get.setHeader("Accept", "application/vnd.api+json");
		get.setHeader("Client-Name", "GenericHttpClient");
		
		get.setHeader("Authorization", "Bearer " + credential);

		HttpResponse response = client.execute(get);
		
		ResourceConverter converter = new ResourceConverter(SNowScanIssuesBreakDownResponse.class);
		
		JSONAPIDocument<SNowScanIssuesBreakDownResponse> issueDocument = converter.readDocument(response.getEntity().getContent(), SNowScanIssuesBreakDownResponse.class);
		Map meta = issueDocument.getMeta();
		int highSeverityIssuesCount = (int) meta.get("total");
		//SNowScanIssuesBreakDownResponse snowIssue = issueDocument.get();
		
		if(highSeverityIssuesCount > highSeverityThreshold) {
			return QCScanResultFactory.SCAN_FAIL_HIGH_SEVERITY_CODE;
		}
		return QCScanResultFactory.SCAN_SUCCESS_CODE;

	}

}
