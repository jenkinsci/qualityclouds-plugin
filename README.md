# README #

How to install Quality Clouds Jenkins Plugin


### Prerequisite ###

* Need Jenkins to be installed.
* Need Maven to be installed.

### Installation Steps are: ###

* run mvn package to Generating hpi in {projectdirectory}/snowjenkinsplugin/target/qcscan.hpi

* login to jenkins, go to Manage Jenkins -> Manage Plugins -> advanced tab -> upload plugin (upload qcscan.hpi)

* Create a freestyle project.

* Add build step QCScan.

* Input parameter are as below : 

	String "Instance URL" : Instance URL to scan

	String "API Token" : Customer API token (you will receive this during your onboarding as a Quality Clouds customer)

	Integer "Issues Count Threshold" : Maximum issues count to have a success build.

	Integer "Technical Debt Threshold"  : Maximum technical debt count to have a success build.

	Integer "QualityCloud Threshold" : Minimum Quality of Cloud  to have a success build.

	Integer "High Severity Issues Threshold" : Maximum High Severity issues count to have a success build.

