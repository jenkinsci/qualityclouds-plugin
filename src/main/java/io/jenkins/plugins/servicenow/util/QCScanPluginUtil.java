package io.jenkins.plugins.servicenow.util;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.*;

/**
 * 
 * @author akramkohansal
 *
 */
public class QCScanPluginUtil {

	 public static boolean isJUnitTest() {
	    	
	        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
	        List<StackTraceElement> list = asList(stackTrace);
	        
	        for (StackTraceElement element : list) {
	        	
	            if (element.getClassName().startsWith("org.junit.")) {
	                return true;
	            }           
	        }
	        return false;
	    }
}
