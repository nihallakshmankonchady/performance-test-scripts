### Contains
* This Folder contains performance test scripts, test data,summary reports of resident services


### How to run jmeter scripts for resident services scenarios
* Execute the script resident_all_scripts.jmx
    1. point the sandbox url to ${BASE_URL}
    2. Modify the thread count as per number of users to be tested
    3. Execute script for 1 user
	4. Execute the script for desired number of users
	5. set the parameters of sandbox database like db_host,db_port,db_name,db_user,db_password
		1. db_host -- host name of the kernel databse
		2. db_port -- portnumber of the kernel databse
		3. db_name -- name of the kernel databse
		4. db_user -- username of the kernel databse
		5. db_password -- password of the kernel databse
	6. set the parameters like uin,vid,misp_lk,partnerID,PartnerApiKey,auth_types
		1. misp_lk -- licence key
		2. partnerID -- partnerid
		3. PartnerApiKey -- partner api key
		4. uin -- uin
		5. vid -- vid
		6. auth_types - auth types demo,bio-Finger,bio-Iris,bio-FACE
    7. continue above steps for remanining scripts as well 
