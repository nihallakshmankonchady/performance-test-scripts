### Contains
* This folder contains performance test scripts, test data, summary reports of Registration Processor. 

### How to create test data 
* We need to generate packets by running the [registration processor packet utility](/utilities/regprocessorpacketgenutil)
* Check below property in config.properties file located in src/main/resources for test data generation   
    1. NUMBER_OF_TEST_PACKETS=100 (number of packets)
    2. ENVIRONMENT=Sandbox environment
    3. BASE_URL=Sandbox URL
	
* Run below 2 utilities to generate packets and test data for sync
    1. packet_gen - To generate packets
    2. sync_data - To generate sync data(test data to registration processor sync API)
  
### How to run jmeter scripts 
* Execute the script 01.regproc_packet_upload_preprod.jmx
    1. point the sandbox URL to ${BASE_URL}
    2. Modify the thread count as per number of packets to upload
    3. Execute script for 1 user
	4. Execute the script for desired number of packets uploaded
	
* Check the status of the packet uploaded ,execute 02.regprocpacketstatus_from_db.jmx script
    1. point the sandbox URL to ${BASE_URL}
	2. set the parameters of sandbox database like db_host,db_port,db_name,db_user,db_password
		1. db_host -- host name of the registration processor database
		2. db_port -- port number of the registration processor database
		3. db_name -- name of the registration processor database
		4. db_user -- user name of the registration processor database
		5. db_password -- password of the registration processor database
    2. Modify the thread count as per number of packets to upload
    3. Execute script for 1 user
	4. Execute the script for desired number of packets uploaded

* Calculate the transaction times by running the [registration processor transaction utility](https://github.com/mosip/mosip-performance-tests-mt/tree/master/utilities/regproc_transactiondata_util_v2.2).
    1. point the sandbox url to ${BASE_URL}
    2. Provide generated regids in regid_file.csv
    3. Once above java utility is executed ,It will generates the transaction_times.xlsx which has all the transaction times of each stages of each packets

### Documentation

MOSIP documentation is available on [Wiki](https://github.com/mosip/documentation/wiki)

### License
This project is licensed under the terms of [Mozilla Public License 2.0](https://github.com/mosip/mosip-platform/blob/master/LICENSE)