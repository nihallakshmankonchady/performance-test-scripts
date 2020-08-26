### Contains
* This Folder contains performance test scripts, test data,summary reports of resident services


### How to run jmeter scripts for prereg UI scenario
* Execute the script 01_prereg_bookingfullflow.jmx
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
	
### How to run jmeter scripts for prereg consumed batchjob
* Execute the 10.consumed_batchjob_script.jmx
    1. point the sandbox url to ${BASE_URL}
    2. provide the list of prereg ids to be consumed 
    3. Execute script for 1 user
	4. Execute the script for desired number of users/according to preregids to be consumed
	