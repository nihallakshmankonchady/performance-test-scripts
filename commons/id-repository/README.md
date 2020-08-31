### Contains
* This Folder contains performance test scripts, test data,summary reports of id-repository module


### How to run jmeter scripts for data generation scenarios
* We need to generate packets by running the java utility https://github.com/mosip/mosip-performance-tests-mt/tree/master/utilities/regprocessorpacketgenutil
* Check below property in config.properties file located in src/main/resources for test data generation   
    1. NUMBER_OF_TEST_DATA=10000 ( set desired number of test data ex:10000)
	
* Run below utility to generate test data for id-repository
    1. test_data - To generate test data for idrepo create identity API
 				
### How to run jmeter scripts for id-repository
* Execute the scripts in testplan https://github.com/mosip/mosip-performance-tests-mt/blob/master/commons/id-repository/scripts/01.idrepo_create_fullflow.jmx
    1. execute the IdRepo-create script to generate test data
	2. point the sandbox url to ${BASE_URL} 
	3. set the parameters of below from reg_data.csv file 
		fullName,dateOfBirth,age,gender,residenceStatus,addressLine1,addressLine2,addressLine3,region,province,city,postalCode,phone,email,localAdministrativeAuthority,cnieNumber	 
	4. set the parameters like reg_id Generator
		1. reg_id Generator -- any 6 digit random number
	5. generate uin 
 	6. create identity
	7. generate vid
* Use the same instrunctions for other scripts as well