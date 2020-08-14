###### Contains
- This Repo contains performance test scripts,test data,utilities ,summary reports of below modules of mosip 

1. Pre-Registration (UI and batch Jobs)
2. Registration Processor
3. IDA
4. ID Repo
5. Kernel
- Open source Tools used:
1. Apache Jmeter (https://jmeter.apache.org/)
2. Glowroot (https://glowroot.org/)

###### How to run performance scripts using apache jmeter tool
- Download apche jmeter from https://jmeter.apache.org/download_jmeter.cgi
- Scripts are downloaded for each modules 
- To start JMeter, run the jmeter.bat (for Windows) or jmeter (for Unix) file. 
- validate the scripts for 1 user
- execute dry run for 10 min

##### Running JMeter using command line in non-GUI mode is very simple.
- Open command prompt
- Go into JMeter’s bin folder
- Enter following command, jmeter -n –t test.jmx -l testresults.jtl
-  -n: It specifies JMeter is to run in non-gui mode
-  -t: Name of JMX file that contains the Test Plan
-  -l: Name of JTL(JMeter text logs) file to log results
-  -j: Name of JMeter run log file
-  Other than these options, JMeter has several other parameters that can be used for running in the non-GUI mode.
-  -R: list of remote servers,
-  -H: proxy server hostname or ip address
-  -P: proxy server port