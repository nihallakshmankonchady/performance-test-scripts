### Contains
* This folder contains performance helper scripts, test scripts, test data files of below MOSIP modules.
    1. IDP (UI API's)
* Open source Tools used,
    1. [Apache JMeter](https://jmeter.apache.org/)

### How to run performance scripts using Apache JMeter tool
* Download Apache JMeter from https://jmeter.apache.org/download_jmeter.cgi
* Download scripts for the required module.
* Start JMeter by running the jmeter.bat file for Windows or jmeter file for Unix. 
* Validate the scripts for one user.
* Execute a dry run for 10 min.
* Execute performance run with various loads in order to achieve targeted NFR's.

### Execution points for IDP UI API's
*  UI - OAuth Details : 
   * OAuth Details (Execution) - Client id will be loaded from a file created by Create Initial OIDC Clients (Setup). Total samples created during execution can be higher in number as compared to the samples present in the file.
*  UI - Send OTP :
   * Send OTP (Preparation) - OAuth Details api will be used here in which created client id would be used.
   * Send OTP (Execution) - Same execution scenario applied as of OAuth details i.e. execution samples can be higher of preparation. Transaction id will be used which is created from the preparation part. Registered individual id also need to be passed in the body for which have separately added the setup thread group for creating identity.
*  UI - Authentication Endpoint :
   * Authentication Endpoint - OTP (Preparation) - For the preparation we need 2 api's OAuth Details and Send OTP from which we will get the transaction id and required OTP respectively.
   * Authentication Endpoint - OTP (Execution) - For the execution the total preparation samples must be equal or higher in number. Transaction id will be used which is created from OAuth details api. Registered individual id also need to be passed in the body along with the OTP.
*  UI - Authorization Code : 
   * Authorization Code (Preparation) - There will be 3 api's included in the preparation i.e. OAuth Details, Send OTP, Authentication Endpoint - OTP from which we will get the transaction id, OTP which will be used in the execution.
   * Authorization Code (Execution) - The total number of samples for preparation should be equal or higher in number as compared to execution. Have to pass the Transaction id generated from the preparation.

### Additional points for Execution 
* Create Initial OIDC Clients - Without IDA (Setup) : File is generated which contains the client id without IDA registeration to be used for the UI IDP API's. Need to manage the number of threads and loop count according to the requirement of total samples.
* Create Identities in MOSIP - IDA (Setup) : This thread contains the authorization api's for regproc and idrepo from which the auth token will be generated. There is set of 3 api's generate RID, generate UIN, add identity. From here we will get the UIN which can be further used as individual id. These 3 api's are present in the loop controller where we can define the number of samples for creating identities in which "addIdentitySetup" is used as a variable.
* We can reuse the client id prepared from the setup thread group i.e. Create Initial OIDC Clients in other api's where client id is required.


	

