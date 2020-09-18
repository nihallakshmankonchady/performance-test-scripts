### Contains
* This folder contains performance test scripts, test data, summary reports of ID Authentication module

### How to run JMeter scripts for data generation scenarios
* Execute the scripts in test plan from [here](https://github.com/mosip/mosip-performance-tests-mt/tree/master/id-authentication/scripts)
    1. execute the 01-demo_address_data_generate-uin script to generate test data
	2. point the sandbox URL to ${BASE_URL} 
	3. set the parameters of sandbox database like db_host, db_port, db_name, db_user, db_password
		1. db_host -- host name of the kernel database
		2. db_port -- port number of the kernel database
		3. db_name -- name of the kernel database
		4. db_user -- user name of the kernel database
		5. db_password -- password of the kernel database
    4. set the parameters like uin, vid, misp_lk, partnerID, PartnerApiKey, auth_types
		1. misp_lk -- license key
		2. partnerID -- partner id
		3. PartnerApiKey -- partner API key
		4. uin -- uin
		5. vid -- vid
		6. auth_types - demo, bio-Finger, bio-Iris, bio-FACE
    5. run the partner demo authentication jar for ex: authentication-partnerdemo-service-1.0.8-SNAPSHOT.jar in local
    6. set the parameters like demoApp_host,demoApp_port and execute the request having URL v1/identity/encrypt to encrypt data 
		1. demoApp_host -- host name of the partner demo application. Default host name is localhost.
		2. demoApp_port -- port number of the partner demo application. Default port is 7654.
    7. store all the values of parameters like uin_address,vid_address,reg_id in a file for authentication
 		1. uin_address -- UIN address
		2. vid_address -- VID address
		3. reg_id -- registration id-authentication
    8. Follow the above steps for other scripts demographic, OTP, e-KYC based test data generations present in the test plan.
 				
### How to run jmeter scripts for authentication
 * Execute the scripts in testplan from [here](https://github.com/mosip/mosip-performance-tests-mt/tree/master/id-authentication/scripts/authentication)
    1. execute the 01- demo_address_auth-uin script to generate test data
	2. point the sandbox url to ${BASE_URL} 
	3. set the parameters of sandbox database like db_host, db_port, db_name, db_user, db_password
		1. db_host -- host name of the kernel database
		2. db_port -- portnumber of the kernel database
		3. db_name -- name of the kernel database
		4. db_user -- username of the kernel database
		5. db_password -- password of the kernel database
    4. set the parameters like uin, vid, misp_lk, partnerID, PartnerApiKey, auth_types
		1. misp_lk -- licence key
		2. partnerID -- partnerid
		3. PartnerApiKey -- partner api key
		4. uin -- uin
		5. vid -- vid
		6. auth_types -- demo, bio-Finger, bio-Iris, bio-FACE
    7. provide test data generated using above instrunctions for authentication script
 		1. uin_address -- UIN address
		2. vid_address -- VID address
		3. reg_id -- registration id-authentication 	
    8. Follow the above steps for other scripts demographic, OTP, e-KYC based authentications present in the test plan
	
### Documentation

MOSIP documentation is available on [Wiki](https://github.com/mosip/documentation/wiki)

### License
This project is licensed under the terms of [Mozilla Public License 2.0](https://github.com/mosip/mosip-platform/blob/master/LICENSE)