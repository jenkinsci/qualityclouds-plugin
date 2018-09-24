/*
 * © 2015 Quality Clouds Ltd. All Rights Reserved.
 */
package io.jenkins.plugins.servicenow;

import hudson.model.RootAction;

public class ScanQualityDashboardAction implements RootAction{

	@Override
	public String getIconFileName() {
		return "clipboard.png";
	}

	@Override
	public String getDisplayName() {
		return "Access your Quality Clouds Dashboards";
	}

	@Override
	public String getUrlName() {
		return "https://scan.qualityclouds.com";
	}

}
