### Contains
* This Folder contains performance test scripts, test data,summary reports of kernel module

 				
### How to run jmeter scripts for kernel module
* Execute the scripts in testplan https://github.com/mosip/mosip-performance-tests-mt/blob/master/commons/kernel/scripts/02.kernel_auth_service.jmx
	1. point the sandbox url to ${BASE_URL}
	2. set the parameters of sandbox database like db_host,db_port,db_name,db_user,db_password
		1. db_host -- host name of the kernel database
		2. db_port -- portnumber of the kernel database
		3. db_name -- name of the kernel database
		4. db_user -- username of the kernel database
		5. db_password -- password of kernel database
    3. execute the script for desired number of concurrent users set in thread group
* Use the same instrunctions for other scripts as well