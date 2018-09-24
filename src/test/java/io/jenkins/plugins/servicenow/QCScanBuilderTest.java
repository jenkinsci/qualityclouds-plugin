package io.jenkins.plugins.servicenow;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

@Ignore
public class QCScanBuilderTest {
	
	private String apitoken = "test";
	@Rule
    public JenkinsRule jenkins = new JenkinsRule();


    @Test
    public void testConfigRoundtrip() throws Exception {
        FreeStyleProject project = jenkins.createFreeStyleProject();
        project.getBuildersList().add(new QCScanBuilder("apitoken", "url", 20, 20, 10, 20));
        project = jenkins.configRoundtrip(project);
        jenkins.assertEqualDataBoundBeans(new QCScanBuilder("apitoken", "url", 20, 20, 10, 20), project.getBuildersList().get(0));
    }
    
    @Test
    public void testBuild() throws Exception {
        FreeStyleProject project = jenkins.createFreeStyleProject();
        QCScanBuilder builder = new QCScanBuilder(apitoken, "https://dev58376.service-now.com", 20, 20, 10, 20);
        project.getBuildersList().add(builder);

        FreeStyleBuild build = jenkins.buildAndAssertSuccess(project);
        
    }

}
