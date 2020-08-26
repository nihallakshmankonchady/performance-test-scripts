### Contains
* This Folder contains performance test scripts, test data,summary reports of registration processor 

### How to create test data 
* We need to generate packets by running the java utility https://github.com/mosip/mosip-performance-tests-mt/tree/master/utilities/regprocessorpacketgenutil
* Check below property in config.properties file located in src/main/resources for test data generation   
    1. NUMBER_OF_TEST_PACKETS=100 (number of packets)
    2. ENVIRONMENT=Sandbox environment
    3. BASE_URL=Sandbox url
	
* Run below 2 utilities to generate packets and test data for sync
    1. packet_gen - To generate packets
    2. sync_data - To generate sync data(test data to reg proc sync API)
  
### How to run jmeter scripts 
* Execute the script 01.regproc_packet_upload_preprod.jmx
    1. point the sandbox url to ${BASE_URL}
    2. Modify the thread count as per number of packets to upload
    3. Execute script for 1 user
	4. Execute the script for desired number of packets uploaded
	
* Check the status of the packet uploaded ,execute 02.regprocpacketstatus_from_db.jmx script
    1. point the sandbox url to ${BASE_URL}
	2. set the parameters of sandbox database like db_host,db_port,db_name,db_user,db_password
		1. db_host -- host name of the registrationprocessor databse
		2. db_port -- portnumber of the registrationprocessor databse
		3. db_name -- name of the registrationprocessor databse
		4. db_user -- username of the registrationprocessor databse
		5. db_password -- password of the registrationprocessor databse
    2. Modify the thread count as per number of packets to upload
    3. Execute script for 1 user
	4. Execute the script for desired number of packets uploaded

* Calculate the transaction times by running the java utility https://github.com/mosip/mosip-performance-tests-mt/tree/master/utilities/regproc_transactiondata_util_v2.2
    1. point the sandbox url to ${BASE_URL}
    2. Provide generated regids in regid_file.csv
    3. Once above java utility is executed ,It will generates the transaction_times.xlsx which has all the transaction times of each stages of each packets
