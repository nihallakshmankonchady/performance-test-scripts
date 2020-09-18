### Contains
* This folder contains performance test scripts, test data, summary reports of Pre-registration


### How to run JMeter scripts for pre-registration UI scenario
* Execute the script 01_prereg_bookingfullflow.jmx
    1. point the sandbox URL to ${BASE_URL}
    2. Modify the thread count as per number of users to be tested
    3. Execute script for 1 user
	4. Execute the script for desired number of users
	5. set the parameters of sandbox database like db_host,db_port,db_name,db_user,db_password
		1. db_host -- host name of the kernel database
		2. db_port -- port number of the kernel database
		3. db_name -- name of the kernel database
		4. db_user -- user name of the kernel database
		5. db_password -- password of the kernel database
	
### How to run JMeter scripts for pre-registration consumed batch job
* Execute the 10.consumed_batchjob_script.jmx
    1. point the sandbox URL to ${BASE_URL}
    2. provide the list of pre-registration ids to be consumed 
    3. Execute script for 1 user
	4. Execute the script for desired number of users/according to pre-registration ids to be consumed

### APIs
API documentation available on Wiki: [Pre-Registration APIs](https://github.com/mosip/documentation/wiki/Pre-Registration-APIs)

### Documentation

MOSIP documentation is available on [Wiki](https://github.com/mosip/documentation/wiki)

### License
This project is licensed under the terms of [Mozilla Public License 2.0](https://github.com/mosip/mosip-platform/blob/master/LICENSE)