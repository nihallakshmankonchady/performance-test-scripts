

### Contains
* This folder contains performance helper scripts, test scripts, test data files of below MOSIP modules.
    1. IDP (OIDC API's)

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


### Execution points for IDP OIDC API's
*  OIDC - Authorization Endpoint : Its a GET API with no preparations and application will do a browser redirect to this endpoint with  all required details passed as query parameters.

*  OIDC Token Endpoint :
   * OIDC Token Endpoint (Preparation) - This includes the OAuth Details, Send OTP, Authentication, Authorization Code API's in the preparation part. From Authorization Code api will get the code value which will be used in the execution. Client id and individual id will be read from a file. The code generated can only be used once so the preparation samples must be equal or higher to execution.
   * OIDC Token Endpoint (Execution) - Code created from the preparation will be used only once and a signed JWT key value is also required for which we are using a JSR223 Sampler. An access token will be generated in the response body.

*  OIDC UserInfo Endpoint :
   * OIDC UserInfo Endpoint (Preparation) - For the preparation we need 5 api's OAuth Details, Send OTP, Authentication, Authorization Code and Token Endpoint api from which a access token will be generated. Access token will be used for the execution. Access token can be used multiple times depending upon its expiration time.
   * OIDC UserInfo Endpoint (Execution) - For execution the generated access token from the token end point api is used. Till the token is not expired it can be used for multiple samples.

*  OIDC Configuration Endpoint : Open ID Connect dynamic provider discovery is not supported currently, this endpoint is only for facilitating the OIDC provider details in a standard way.

*  OIDC JSON Web Key Set Endpoint : Endpoint to fetch all the public keys of the IdP server.Returns public key set in the JWKS format.
   

### Additional points for Execution 
* JSR223 Sampler - This sampler is being used to generate the JWT token value from a client id and its private secret key which will be used for the execution.

**Contains**
This folder contains idp Helper and Test script.
IDP_Test_Script contains the below mentioned IdP: Management API's.
OIDC Client Endpoint 
Update OIDC Client Endpoint
Before executing the IDP Test Script with above api's have to update the Counter number otherwise it will give duplicate client id error.

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
* In the below mentioned api's we can reuse the client id : 
    * OAuth Details (Execution)
    * Send OTP (Preparation)
    * Authentication Endpoint - OTP (Preparation)
    * Authorization Code (Preparation)


	


