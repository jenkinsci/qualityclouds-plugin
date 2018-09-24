/*
 * © 2015 Quality Clouds Ltd. All Rights Reserved.
 */
package io.jenkins.plugins.servicenow;

import hudson.model.Action;

public interface ResultAction extends Action{

	  String STATUS_CODE = "StatusCode";
	  int CODE200 = 200;
	  int CODE400 = 400;
	  int CODE401 = 401;
	
	 void setTimeElapse(String scanDuration);
}
