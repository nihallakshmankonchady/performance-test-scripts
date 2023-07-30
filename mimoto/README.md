### Contains
* This folder contains performance helper script and test script of Mimoto API endpoints.
    1. Request OTP 
    2. Auth Lock 
    3. Auth Unlock
    4. Generate VID
    5. Request Credential
    6. Credential Share Check Status
    7. Credentialshare download
    8. Req IndividualID OTP
    9. Aid Get-Individual-ID


* Open source Tools used,
    1. [Apache JMeter](https://jmeter.apache.org/)

### How to run performance scripts using Apache JMeter tool
* Download Apache JMeter from https://jmeter.apache.org/download_jmeter.cgi
* Download scripts for the required module.
* Start JMeter by running the jmeter.bat file for Windows or jmeter file for Unix. 
* Validate the scripts for one user.
* Execute a dry run for 10 min.
* Execute performance run with various loads in order to achieve targeted NFR's.

### Setup points for Execution

* Create Identities in MOSIP Authentication System (Setup) : This thread contains the authorization api's for regproc and idrepo from which the auth token will be generated. There is set of 4 api's generate RID, generate UIN, add identity and add VID. From here we will get the UIN which can be further used as individual id for required mimoto api's endpoints. These 4 api's are present in the loop controller where we can define the number of samples for creating identities in which "addIdentitySetup" is used as a variable.

### Data prerequisite

* List of UIN's as per environment which is valid and will be prepared from the above mentioned create identities setup.

### Execution points for Mimoto API endpoints

* Request OTP (Execution) - This thread contains the request otp api which is used to request for a new OTP and a valid UIN is required which will be prepared from the above mentioned ideities setup.

* Auth Lock (Preparation) - This thread contains the same request otp api which will be used for Auth Lock.
* Auth Lock (Execution) - In Auth Lock api will require a valid UIN along with otp which will be generated from the preparation part.

* Auth Unlock (Preparation) - This thread contains the same request otp api which will be used for Auth Unlock.
* Auth Unlock (Execution) - In Auth Unlock api will require a valid UIN along with otp which will be generated from the preparation part. 

* Generate VID (Preparation) - This thread contains the same request otp api which will be used for Generate VID.
* Generate VID (Execution) - In Generate VID api will require a valid UIN along with otp which will be generated from the preparation part. A VID will be generated from the provided UIN.

* Request Credential (Preparation) - This thread contains the same request otp api which will be used for request new credential endpoint.
* Request Credential (Execution) - This api is used to request for new credential using a valid UIN and otp which will be generated from the preparation part. 

* Credential Share Check Status (Preparation) - This thread contains 2 set of api's request otp and request credential api from which the status of the new credentials will be checked.
* Credential Share Check Status (Execution) - In this api will check the status of the credetials which are prepared from request credential api. The status for the credential must be "ISSUED". Request id created from the request credential api will be used.

* Credentialshare download (Preparation) - This thread contains 2 set of api's request otp and request credential api which will generate a request id to be used.
* Credentialshare download (Execution) - This api will download the credentials. A valid UIN and the created request id will be required which is generated in the preparation part.

* Req IndividualID OTP (Execution) - This thread contains the request otp api which is used to request for a new OTP using individual id. A valid RID is required which will be prepared from the above mentioned ideities setup. 

* Aid Get-Individual-ID (Preparation) - This thread group contains the req individual id otp api which will generate a otp to be used in the execution.

* Aid Get-Individual-ID (Execution) - This api is used to get individual id using application id and here application id is a valid RID. It will also use the otp generated from the preparation.

