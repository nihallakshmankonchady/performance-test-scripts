### Contains
* This folder contains performance test scripts, test data, summary reports of Resident Services.


### How to run jmeter scripts for resident services scenarios
* Execute the script resident_all_scripts.jmx
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
	6. set the parameters like uin, vid, misp_lk, partnerID, PartnerApiKey, auth_types
		1. misp_lk -- license key
		2. partnerID -- partner id
		3. PartnerApiKey -- partner API key
		4. uin -- uin
		5. vid -- vid
		6. auth_types -- demo, bio-Finger, bio-Iris, bio-FACE
    7. continue above steps for remaining scripts as well 

### Documentation

MOSIP documentation is available on [Wiki](https://github.com/mosip/documentation/wiki)

### License
This project is licensed under the terms of [Mozilla Public License 2.0](https://github.com/mosip/mosip-platform/blob/master/LICENSE)