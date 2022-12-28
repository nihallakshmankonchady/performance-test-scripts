
### Contains
* This folder contains performance helper scripts, test scripts, test data files of below MOSIP modules.
    1. IDP (OIDC API's)
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

