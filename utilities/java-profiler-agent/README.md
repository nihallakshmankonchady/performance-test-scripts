## Helm chart to setup profiling for a java application running in a kubernetes cluster

Steps to setup the profiler agent helm chart in the environment:

1. ssh to the environment's console machine where all the deployment script and persistent storage is maintained

2. Create a folder named 'java-profiler-agent' in the /srv/nfs/mosip/ of console.sb machine

3. Copy the profiler agent folder under this new folder

4. Copy this helm chart folder to the machine to the environment's console machine (console.sb)

5. Update the profileAppService property under the service section of values.yaml file to the kubernetes pod app name of the application that need to be profiled

6. Start this helm chart using the below command
````
        helm1 install java-profiler-agent .     //Navigate to the folder of the java-profiler-agent helm chart
````

7. Open a port in load balancer / Nginx for the profiler UI application to connect and route the same to any one of the nodes with the node port configured (30070)


Steps to reconfigure the app to be profiled:

1. Open the source Docker file of the application that needs profiling and copy the ENTRYPOINT and CMD line that starts the application

eg. 
````
ENTRYPOINT [ "./configure_start.sh" ]
CMD wget -q --show-progress "${iam_adapter_url_env}" -O "${loader_path_env}"/kernel-auth-adapter.jar; \
    java -jar -Djava.security.debug=sunpkcs11 -Dspring.cloud.config.label="${spring_config_label_env}" -Dspring.profiles.active="${active_profile_env}" -Dspring.cloud.config.uri="${spring_config_url_env}" -Dloader.path="${loader_path_env}" -Dfile.encoding="UTF-8" ${current_module_env}.jar ; \
````

2. Change the ENTRYPOINT and CMD line to kubernetes command format with few env variable replaced with actual value that is not declared in deployment file. Also include the profiler agent argument in the command.

eg.
````
command: 
- sh
- -c
- | 
  ./configure_start.sh wget -q --show-progress "$(iam_adapter_url_env)" -O /home/mosip/additional_jars/kernel-auth-adapter.jar; \
  java -agentpath:/mnt/java-profiler-agent/jprofiler12/bin/linux-x64/libjprofilerti.so=port=8849,nowait -jar -Djava.security.debug=sunpkcs11 -Dspring.cloud.config.label="$(spring_config_label_env)" -Dspring.profiles.active="$(active_profile_env)" -Dspring.cloud.config.uri="$(spring_config_url_env)" -Dloader.path="/home/mosip/additional_jars" -Dfile.encoding="UTF-8" authentication-internal-service.jar
````

3. Open the deployment file of the application what needs profiling and do the following changes 
	a. Add the command formed in step-2 under the spec.template.spec.containers section
	b. Add the below section under the spec.template.spec.containers section
````
		volumeMounts:
		- mountPath: /mnt/java-profiler-agent
		  name: profiler-agent
````
	c. Add the below section under the spec.template.spec section
````
        volumes:
        - name: profiler-agent
          persistentVolumeClaim:
            claimName: java-profiler-agent-pvc
````
4. Save the changes and restart the application deployment / helm chart of the application

5. Update the profileAppService property under the service section of values.yaml in java-profiler-agent helm chart to the kubernetes pod app name of the application that need to be profiled

6. Restart the java-profiler-agent helm chart using the below commands
````
	helm1 uninstall java-profiler-agent
	helm1 install java-profiler-agent .     //Navigate to the folder of the java-profiler-agent helm chart
````

