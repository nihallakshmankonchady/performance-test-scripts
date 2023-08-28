### Contains
* This folder contains performance test script of Resident Service Endpoints.
    1. Authorize​ Admin​ Validate Token 
    2. Login​ Redirect URI 
    3. VID Policy
    4. Generate VID
    5. Get VIDs of the Resident
    6. Service History
    7. Service History Download
    8. Auth Lock Unlock
    9. Auth Lock Status 
    10. Request Card VID 
    11. Revoke VID
    


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

* Create Identities in MOSIP Authentication System (Setup) : This thread contains the authorization api's for regproc and idrepo from which the auth token will be generated. There is set of 4 api's generate RID, generate UIN, add identity and add VID. From here we will get the VID's which can be further used as individual id for required Resident Service endpoints. These 4 api's are present in the loop controller where we can define the number of samples for creating identities in which "freshIdentityCreationCount" is used as a variable. In whichever environment we are running the scripts we should have atleast few hundred VID's available handy and if not we can use this setup to create the identities as required. 

* Resident Id Access Token Creation (Setup) : This thread contains 4 esignet api's Oauth details, Send OTP, Authentication, Authorization Code and 1 reisdent Login​ Redirect URI api.  After the login is successful, the resident will be redirected to the resident portal’s logged-in page. From Login​ Redirect URI api will get the id and access token which will be used further in the headers for most of the resident service api's. So till the time id and access token are valid and not expired we can re-use it for the resident service api's. And as per the expiration time once it is not valid and expired, we need to re-run the setup as required.

* For execution purpose need to modify the below mentioned properties: For Performance testing we require a specific amount of data which needs to be used further for the resident service api's and it should be valid till the time of execution. So, We have modified the below properties to increase the expiry time, so that the data prepared to be used for execution is valid until the execution is completed.

   * esignet default properties: Update the value for the properties according to the execution setup. Perform the execution for esignet api's with redis setup. So check for the redis setup accordingly.
          mosip.esignet.cache.size - Enabled while not using the redis setup. Can keep the cache size around more than 100k.
          mosip.esignet.cache.expire-in-seconds (authcodegenerated) - 21600
          mosip.esignet.access-token-expire-seconds - 86400
          mosip.esignet.id-token-expire-seconds - 86400
          spring.cache.type=redis - check for this property and enable the redis.
   * application default properties: Update the value for the below property.
          mosip.kernel.otp.expiry-time - 86400
   * id-authentication default properties: Update the value for the below properties.
          otp.request.flooding.max-count - 100000

          

### Data prerequisite

* List of VID's as per environment which is valid and will be prepared from the above mentioned create identities setup.

### Execution points for Resident Service endpoints

* The id and access token generated from the Login​ Redirect URI api will be stored in a file and will be used in the headers of below mentioned api's :

    * Authorize​ Admin​ Validate Token 
    * Login​ Redirect URI
    * VID Policy
    * Generate VID
    * Get VIDs of the Resident
    * Service History 
    * Service History Download
    * Auth Lock Unlock 
    * Auth Lock Status 
    * Request Card VID  
    * Revoke VID

* Authorize​ Admin​ Validate Token (Execution) - The id and access token generated above in the setup will be used in the headers of this api and can be re-used untill they are not expired.

* Login​ Redirect URI (Preparation) - This thread contains set of 4 esignet api's Oauth details, Send OTP, Authentication, Authorization Code. The code generated will be stored in a file and can't be used for multiple runs as code can only be used once. 
* Login​ Redirect URI (Execution) - In this api will use the code generated from the above esignet api. The total preparation samples must be equal or higher in number. We cannot re-use the preparation as the generated code can only be used once.

* VID Policy - This API is to get the VID policy. The id and access token can be re-used untill they are not expired.

* Generate VID - This API generates the VID for the given VID type. The id and access token will be used in the headers of this api and can be re-used untill they are not expired.

* Get VIDs of the Resident - This API will retrieve the list of active VIDs of the VID of the logged in session. The id and access token can be re-used untill they are not expired.

* Service History - This API is to get the service history of one or more service types. The id and access token can be re-used untill they are not expired.

* Service History Download - This API is to download the View History Tables as a PDF. The id and access token can be re-used untill they are not expired.

* Auth Lock Unlock - Resident service to store the Auth type lock/unlock status with status_comment containing summary of what is locked /unlocked. The id and access token can be re-used untill they are not expired.

* Auth Lock Status - This API returns the lock/unlock status of Auth Types for a given VID. The id and access token can be re-used untill they are not expired. The id and access token can be re-used untill they are not expired.

* Request Card VID - This API is to request the PDF card generation for a specific VID of the logged in user. This will give back a event ID for tracking purpose and to use it to download the VID card from the notifications. The id and access token can be re-used untill they are not expired.


* Revoke VID (Preparation) - This thread contains Generate VID api from which we will get a VID which needs to be revoked.
* Revoke VID (Execution) - The generated VID can only be used once to be revoked so the samples created in the preparation must be equal or higher in number.