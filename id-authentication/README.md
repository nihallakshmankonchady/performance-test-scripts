### Contains
* This Folder contains performance test scripts, test data,summary reports of id-authentication module


### How to run jmeter scripts for data generation scenarios
* Execute the scripts in testplan https://github.com/mosip/mosip-performance-tests-mt/tree/master/id-authentication/scripts/data-generate
    1. execute the 01-demo_address_data_generate-uin script to generate test data
	2. point the sandbox url to ${BASE_URL} 
	3. set the parameters of sandbox database like db_host,db_port,db_name,db_user,db_password
		1. db_host -- host name of the kernel databse
		2. db_port -- portnumber of the kernel databse
		3. db_name -- name of the kernel databse
		4. db_user -- username of the kernel databse
		5. db_password -- password of the kernel databse
    4. set the parameters like uin,vid,misp_lk,partnerID,PartnerApiKey,auth_types
		1. misp_lk -- licence key
		2. partnerID -- partnerid
		3. PartnerApiKey -- partner api key
		4. uin -- uin
		5. vid -- vid
		6. auth_types - auth types demo,bio-Finger,bio-Iris,bio-FACE
    5. run the partner demo authentication jar for ex: authentication-partnerdemo-service-1.0.8-SNAPSHOT.jar in local
    6. set the parameters like demoApp_host,demoApp_port and execute the request having url v1/identity/encrypt to encrypt data 
		1. demoApp_host -- hostname of the partner demo app default is localhost
		2. demoApp_port -- port number of the partner demo app default is 7654
    7. store all the values of parameters like uin_address,vid_address,reg_id in a file for authentication
 		1. uin_address -- UIN address
		2. vid_address -- VID address
		3. reg_id -- registration id-authentication 	
    8. Follow the above steps for other scripts demographic,OTP ,ekyc based test data genertations present in the test plan
 				
### How to run jmeter scripts for authentication
 * Execute the scripts in testplan https://github.com/mosip/mosip-performance-tests-mt/tree/master/id-authentication/scripts/authentication
    1. execute the 01- demo_address_auth-uin script to generate test data
	2. point the sandbox url to ${BASE_URL} 
	3. set the parameters of sandbox database like db_host,db_port,db_name,db_user,db_password
		1. db_host -- host name of the kernel databse
		2. db_port -- portnumber of the kernel databse
		3. db_name -- name of the kernel databse
		4. db_user -- username of the kernel databse
		5. db_password -- password of the kernel databse
    4. set the parameters like uin,vid,misp_lk,partnerID,PartnerApiKey,auth_types
		1. misp_lk -- licence key
		2. partnerID -- partnerid
		3. PartnerApiKey -- partner api key
		4. uin -- uin
		5. vid -- vid
		6. auth_types - auth types demo,bio-Finger,bio-Iris,bio-FACE
    7. provide test data generated using above instrunctions for authentication script
 		1. uin_address -- UIN address
		2. vid_address -- VID address
		3. reg_id -- registration id-authentication 	
    8. Follow the above steps for other scripts demographic,OTP , ekyc based authentications present in the test plan