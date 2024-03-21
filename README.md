# README #

## This plugin is archived as of March 2024
## Visit https://www.qualityclouds.com for alternatives

This plugin allows Quality Clouds customers to run Quality Clouds scans against their ServiceNow and Salesforce
instances from a Jenkins build.

Different parameters can be set to fail the build.
*   Maximum number of Issues
*   Maximun amount of Technical Debt (in minutes)
*   Maximum number of High Severity Issues
*   Minimum value for the Quality of Cloud Indicator (between 0 and 100)

At least one valid parameter must be specified.


### Prerequisites for local installation ###

* Need Jenkins to be installed.
* Credentials plugin required.
* Need Maven to be installed.

### Installation Steps are: ###

* run mvn package to Generating hpi in {projectdirectory}/snowjenkinsplugin/target/qcscan.hpi

* Login to jenkins, go to Manage Jenkins -> Manage Plugins -> advanced tab -> upload plugin (upload qcscan.hpi)


### Build configuration Steps are: ###

* Create a freestyle project.

* Create a credential of type Secret File and store the the Quality Clouds API token on this file
  (you will receive this token during your onboarding as a Quality Clouds customer)

* The credential can be defined in the Global domain. If you want to use a specific Domain for the Quality Clouds credentials,
you will need to create a specific domain and configure a hostname specification of "qualityclouds.com". See
https://docs.qualityclouds.com/display/QCD/Quality+Clouds+Jenkins+Plugin for details

* Add build step QCScan.

* The configuration parameters for this build step are:

	String "Instance URL" : Instance URL to scan.

	String "API Token Secret File" : Select the credential you created to hold the API key.

	Integer "Issues Count Threshold" : Maximum issues count to have a success build.

	Integer "Technical Debt Threshold"  : Maximum technical debt count to have a success build.

	Integer "Quality of Cloud Threshold" : Minimum Quality of Cloud to have a success build.

	Integer "High Severity Issues Threshold" : Maximum High Severity issues count to have a success build.

* Please refer to https://docs.qualityclouds.com/display/QCD/Quality+Clouds+Jenkins+Plugin or contact help@qualityclouds.com in case of issues configuring your
build.


