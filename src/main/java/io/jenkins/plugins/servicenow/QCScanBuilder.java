/*
 * © 2015 Quality Clouds Ltd. All Rights Reserved.
 */
package io.jenkins.plugins.servicenow;

import com.cloudbees.plugins.credentials.CredentialsMatcher;
import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.AbstractIdCredentialsListBoxModel;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.cloudbees.plugins.credentials.domains.URIRequirementBuilder;
import com.cloudbees.plugins.credentials.impl.BaseStandardCredentials;
import com.cloudbees.plugins.credentials.matchers.AllOfMatcher;
import com.cloudbees.plugins.credentials.matchers.AnyOfMatcher;
import com.cloudbees.plugins.credentials.matchers.ConstantMatcher;
import hudson.*;
import hudson.model.*;
import hudson.security.ACL;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import hudson.util.IOUtils;
import hudson.util.ListBoxModel;
import io.jenkins.plugins.servicenow.rest.QCScanRest;
import io.jenkins.plugins.servicenow.util.QCScanResultFactory;
import jenkins.model.Jenkins;
import jenkins.tasks.SimpleBuildStep;
import org.acegisecurity.Authentication;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.plaincredentials.FileCredentials;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;
import org.jenkinsci.plugins.plaincredentials.impl.FileCredentialsImpl;
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.springframework.util.StopWatch;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;

import static com.cloudbees.plugins.credentials.CredentialsMatchers.instanceOf;

/**
 * This class is a step builder in jenkins build and entry point of QualityClouds Scan plugin responsible for creating
 * page and delivering
 * input parameters. 
 * 
 * @author akramkohansal
 *
 */
public class QCScanBuilder extends Builder implements SimpleBuildStep{

	private final String apiToken;

	private final String instanceUrl;

	private final String credentialsId;


	
	private final int issuesCountThreshold, techDebtThreshold, qcThreshold, highSeverityThreshold;

	@DataBoundConstructor
	public QCScanBuilder(String credentialsId, String apiToken, String instanceUrl, int issuesCountThreshold,
                         int techDebtThreshold,
			int qcThreshold, int highSeverityThreshold) {
		super();
		this.credentialsId = credentialsId;
		this.apiToken = apiToken;
		this.instanceUrl = instanceUrl;
		this.issuesCountThreshold = issuesCountThreshold;
		this.techDebtThreshold = techDebtThreshold;
		this.qcThreshold = qcThreshold;
		this.highSeverityThreshold = highSeverityThreshold;
	}
	

	public String getApiToken() {
		return apiToken;
	}
	public String getInstanceUrl() {
		return instanceUrl;
	}
	
	public int getIssuesCountThreshold() {
		return issuesCountThreshold;
	}


	public int getTechDebtThreshold() {
		return techDebtThreshold;
	}


	public int getQcThreshold() {
		return qcThreshold;
	}


	public int getHighSeverityThreshold() {
		return highSeverityThreshold;
	}


	@Override
	public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath filPath,@Nonnull Launcher launcher,
						@Nonnull TaskListener taskListener)
			throws IOException {

		StopWatch stopWatch = new StopWatch();
        stopWatch.start();
		taskListener.getLogger().println("Scan starting ....");

            FileCredentials fileCredentials = CredentialsProvider.findCredentialById(credentialsId, FileCredentials.class,run,
                    new DomainRequirement());

            String apiKeyFromCredentials = IOUtils.toString(fileCredentials.getContent());




//		run.get
		
		
		QCScanRest qcScanRest = new QCScanRest();
//		Map<String, Object> result = qcScanRest.doScan(apiToken, instanceUrl, taskListener, issuesCountThreshold, techDebtThreshold, qcThreshold, highSeverityThreshold);
		Map<String, Object> result = qcScanRest.doScan(apiKeyFromCredentials, instanceUrl, taskListener, issuesCountThreshold,
                techDebtThreshold, qcThreshold, highSeverityThreshold);

		QCScanResultFactory factory = new QCScanResultFactory(result);
		
		ResultAction resultAction = factory.getResultAction();
		stopWatch.stop();
		
		Long durationInMillis = stopWatch.getTotalTimeMillis();

		long millis = durationInMillis % 1000;
		long second = (durationInMillis / 1000) % 60;
		long minute = (durationInMillis / (1000 * 60)) % 60;
		long hour = (durationInMillis / (1000 * 60 * 60)) % 24;
		
		String time = String.format("%02d:%02d:%02d.%d", hour, minute, second, millis);


		
		//resultAction.setTimeElapse(stopWatch.getTotalTimeSeconds()+"");
		
		resultAction.setTimeElapse(time);
		run.addAction(resultAction);
		
		taskListener.getLogger().println("Scan complete.");
		taskListener.getLogger().println(String.format("Elapsed Time: [%f] seconds", stopWatch.getTotalTimeSeconds()));

		if(factory.getStatusCode() != QCScanResultFactory.SCAN_SUCCESS_CODE) {
			
			throw new AbortException("Build Failed");
		}
		
		// add dashboard link of quality clouds.
		run.addAction(new ScanQualityDashboardAction());
		
	}
	
	@Extension
	@Symbol("Quality Clouds Scan")
    public static class Descriptor extends BuildStepDescriptor<Builder> {

        public ListBoxModel doFillCredentialsIdItems(
                @AncestorInPath Item item,
                @QueryParameter String credentialsId
        ) {

            Collection<Job> jobs = (Collection<Job>) item.getAllJobs();

            Job job = jobs.iterator().next();

            StandardListBoxModel result = new StandardListBoxModel();
            if (item == null) {
                if (!Jenkins.getActiveInstance().hasPermission(Jenkins.ADMINISTER)) {
//                    result.add()
                    return result.add("");
                }
            } else {
                if (!item.hasPermission(Item.EXTENDED_READ)
                        && !item.hasPermission(CredentialsProvider.USE_ITEM)) {
                    return result.add(credentialsId);
                }
            }

            return new StandardListBoxModel().withEmptySelection()
                    .withMatching(CredentialsMatchers.anyOf(
                            instanceOf(FileCredentials.class),
                            instanceOf(StringCredentials.class)),
                            CredentialsProvider.lookupCredentials(StandardCredentials.class, item));

        }
 
		public FormValidation doCheckApiToken(@QueryParameter String value) {

            Jenkins.getInstance().checkPermission(Jenkins.ADMINISTER);

            Properties p = System.getProperties();

            Map<String,String> m = System.getenv();

            String envKey = m.get("key");

            if (StringUtils.isEmpty(envKey)) {
                System.out.println("se jodio desde el env");
            }

            String key = (String) p.get("key");

            if (StringUtils.isEmpty(key)) {
                System.out.println("se jodio");
            }

            if(StringUtils.isEmpty(value)) {
            		return FormValidation.error(Messages.QCScanBuilder_DescriptorImpl_TokenIsNull());
            }


            return FormValidation.ok();
        }

		public FormValidation doCheckInstanceUrl(@QueryParameter String value) {

            Jenkins.getInstance().checkPermission(Jenkins.ADMINISTER);

            if(!value.startsWith("https://")) {
                return FormValidation.error(Messages.QCScanBuilder_DescriptorImpl_InvalidURL());
            }

            if(StringUtils.isEmpty(value)) {
            		return FormValidation.error(Messages.QCScanBuilder_DescriptorImpl_URLIsNull());
            }
			
            return FormValidation.ok();
        }
		public FormValidation doCheckIssuesCountThreshold(@QueryParameter String value,
														  @QueryParameter String techDebtThreshold,
														  @QueryParameter String qcThreshold,
														  @QueryParameter String highSeverityThreshold) {

            Jenkins.getInstance().checkPermission(Jenkins.ADMINISTER);

            if (allParametersAreEmpty(value, techDebtThreshold, qcThreshold,
					highSeverityThreshold)) {

				return FormValidation.error(Messages.QCScanBuilder_DescriptorImpl_NoBuildFailConditionSpecified());

			}

            if(isPositiveNumber(highSeverityThreshold)
                    || isPositiveNumber(techDebtThreshold)
                    || isPositiveNumber(value)
                    || (isPositiveNumber(qcThreshold) && Integer.parseInt(qcThreshold) <= 100)) {

                return FormValidation.ok();
            }

			if (!isPositiveNumber(value)) {
			    return FormValidation.error(Messages.QCScanBuilder_DescriptorImpl_IssuesCountThreshold());
            }

			return FormValidation.ok();
		}

		public FormValidation doCheckTechDebtThreshold(@QueryParameter String value,
                                                       @QueryParameter String issuesCountThreshold,
                                                       @QueryParameter String qcThreshold,
                                                       @QueryParameter String highSeverityThreshold) {

            Jenkins.getInstance().checkPermission(Jenkins.ADMINISTER);

            if (allParametersAreEmpty(value, issuesCountThreshold, qcThreshold,
                    highSeverityThreshold)) {
                return FormValidation.error(Messages.QCScanBuilder_DescriptorImpl_NoBuildFailConditionSpecified());
            }


            if(isPositiveNumber(highSeverityThreshold)
                    || isPositiveNumber(issuesCountThreshold)
                    || isPositiveNumber(value)
                    || (isPositiveNumber(qcThreshold) && Integer.parseInt(qcThreshold) <= 100)) {

                return FormValidation.ok();
            }


            if(!isPositiveNumber(value)) {
            		return FormValidation.error(Messages.QCScanBuilder_DescriptorImpl_TechDebtThreshold());
            }

            return FormValidation.ok();
        }

		public FormValidation doCheckQcThreshold(@QueryParameter String value,
                                                 @QueryParameter String issuesCountThreshold,
                                                 @QueryParameter String techDebtThreshold,
                                                 @QueryParameter String highSeverityThreshold) {

            Jenkins.getInstance().checkPermission(Jenkins.ADMINISTER);

            if (allParametersAreEmpty(value, issuesCountThreshold, techDebtThreshold,
                    highSeverityThreshold)) {
                return FormValidation.error(Messages.QCScanBuilder_DescriptorImpl_NoBuildFailConditionSpecified());
            }

            if(isPositiveNumber(highSeverityThreshold)
                    || isPositiveNumber(issuesCountThreshold)
                    || isPositiveNumber(techDebtThreshold)
                    || (isPositiveNumber(value) && Integer.parseInt(value) <= 100)) {

                return FormValidation.ok();
            }


            if(!isPositiveNumber(value) || Integer.parseInt(value)>100) {
            		return FormValidation.error(Messages.QCScanBuilder_DescriptorImpl_QCThreshold());
            }

            return FormValidation.ok();
        }
		public FormValidation doCheckHighSeverityThreshold(@QueryParameter String value,
                                                           @QueryParameter String issuesCountThreshold,
                                                           @QueryParameter String techDebtThreshold,
                                                           @QueryParameter String qcThreshold) {


            Jenkins.getInstance().checkPermission(Jenkins.ADMINISTER);

            if (allParametersAreEmpty(value, issuesCountThreshold, qcThreshold,
                    techDebtThreshold)) {
                return FormValidation.error(Messages.QCScanBuilder_DescriptorImpl_NoBuildFailConditionSpecified());
            }

            if(isPositiveNumber(value)
                    || isPositiveNumber(issuesCountThreshold)
                    || isPositiveNumber(techDebtThreshold)
                    || (isPositiveNumber(qcThreshold) && Integer.parseInt(qcThreshold) <= 100)) {

                return FormValidation.ok();
            }

            if(!isPositiveNumber(value)) {
            		return FormValidation.error(Messages.QCScanBuilder_DescriptorImpl_HighSeverityThreshold());
            }

            return FormValidation.ok();
        }
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {

            return FreeStyleProject.class.isAssignableFrom(jobType);
        }

        @Override
        @Nonnull
        public String getDisplayName() {
            return Messages.QCScanBuilder_DescriptorImpl_DisplayName();
        }

		private boolean allParametersAreEmpty(String p1, String p2, String p3,
											  String p4) {

			return (StringUtils.isEmpty(p1) && StringUtils.isEmpty(p2) && StringUtils.isEmpty(p3)
					&& StringUtils.isEmpty(p4));
		}



        private boolean isPositiveNumber(String value) {

            int i;

            try {
                i = Integer.parseInt(value);
            } catch (NumberFormatException nfe) {
                return false;
            }

            return i >= 0;

        }
    }

}

