### Contains
* This Folder contains performance test scripts, test data,summary reports of registration processor 

### How to create test data 
* we need to generate packets by running the java utility https://github.com/mosip/mosip-performance-tests-mt/tree/master/utilities/regproc_transactiondata_util_v2.2 
* Check below property in config.properties file located in src/main/resources for test data generation
   *NUMBER_OF_TEST_PACKETS=100
   *ENVIRONMENT=Sandbox environment
   *BASE_URL=Sandbox url
*Run below 2 utilities to generate packets and test data for sync
   *packet_gen - To generate packets
   *sync_data - To generate sync data(test data to reg proc sync API)
   
### How to run jmeter scripts 
 *Open the script https://github.com/mosip/mosip-performance-tests-mt/blob/master/registration/registrationprocessor/scripts/01.regproc_packet_upload_preprod.jmx
 *point the sandbox url to ${BASE_URL}
 *Modify the thread count as per number of packets to upload
 *Execute script for 1 user
 *Execute the script for desired number of packets uploaded
 *Inorder to check the status of the packet uploaded ,execute the https://github.com/mosip/mosip-performance-tests-mt/blob/master/registration/registrationprocessor/scripts/02.regprocpacketstatus_from_db.jmx script



