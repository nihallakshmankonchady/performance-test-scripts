package io.mosip.client;

import io.mosip.kernel.core.exception.IOException;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.core.util.FileUtils;
import io.mosip.kernel.fsadapter.hdfs.impl.HDFSAdapterImpl;
import io.mosip.registration.processor.core.code.ModuleName;
import io.mosip.registration.processor.core.exception.ApisResourceAccessException;
import io.mosip.registration.processor.core.exception.PacketDecryptionFailureException;
import io.mosip.registration.processor.core.exception.util.PlatformSuccessMessages;
import io.mosip.registration.processor.core.spi.filesystem.manager.PacketManager;
import io.mosip.registration.processor.rest.client.config.RestConfigBean;
import io.mosip.registration.processor.status.dto.InternalRegistrationStatusDto;
import io.mosip.registration.processor.status.dto.RegistrationStatusDto;
import io.mosip.registration.processor.status.dto.SyncRegistrationDto;
import io.mosip.registration.processor.status.dto.SyncResponseDto;
import io.mosip.registration.processor.status.service.RegistrationStatusService;
import io.mosip.registration.processor.status.service.SyncRegistrationService;
import io.mosip.registration.processor.status.service.impl.SyncRegistrationServiceImpl;
import io.mosip.registrationProcessor.perf.client.RegistrationStatusClient;
import io.mosip.registrationProcessor.perf.client.SyncDataClient;
import io.mosip.registrationProcessor.perf.util.CmdRunnerUtil;
import io.mosip.registrationProcessor.perf.util.PropertiesUtil;
import io.mosip.registrationProcessor.perf.util.SSHUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

import com.google.gson.Gson;
import com.jcraft.jsch.Session;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {
		"io.mosip.*" }, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {
				RestConfigBean.class, SyncRegistrationServiceImpl.class }))
@PropertySource("classpath:bootstrap.properties")
public class RegistrationPacketSyncClient {

//	@Value("${file.path}")
//	private String filepath;

	@Autowired
	private SyncRegistrationService<SyncResponseDto, SyncRegistrationDto> service;

	private static final Logger LOGGER = LoggerFactory.getLogger(HDFSAdapterImpl.class.getName());

	@Autowired
	private io.mosip.registration.processor.packet.manager.decryptor.DecryptorImpl decryptor;

	@Autowired
	SyncDataClient syncDataClient;

	@Autowired
	RegistrationStatusService<String, InternalRegistrationStatusDto, RegistrationStatusDto> registrationStatusService;

	@Autowired
	SSHUtil sshUtil;

	@Autowired
	CmdRunnerUtil cmdRunner;

	public static void main(String[] args) {
		SpringApplication.run(RegistrationPacketSyncClient.class, args);
	}

	@PostConstruct
	public void run()
			throws FileNotFoundException, ApisResourceAccessException, PacketDecryptionFailureException, IOException {

		String CONFIG_FILE = "testdata.properties";
		PropertiesUtil prop = new PropertiesUtil(CONFIG_FILE);
		// performSyncOperation(prop);
		String reg_id = "10006100435595420200123091828";
		// executeLocalWindowsCommands(prop, reg_id);

		transferRegPacketToDmz();

	}

	private void executeLocalWindowsCommands(PropertiesUtil prop, String reg_id) {
		String command = "kubectl config get-contexts";
		String packetPath = prop.NEW_PACKET_FOLDER_PATH;
		packetPath += File.separator + "zipped";
		String zipfile = packetPath + File.separator + reg_id + ".zip";
		String podname = prop.REGPROC_PACKET_UPLOADER_POD;
		command = "kubectl cp " + zipfile + " default/" + podname + ":/home/ftp1/LANDING_ZONE";
		System.out.println("command is " + command);
		cmdRunner.executeCommand(command);
	}

	private void transferRegPacketToDmz() {
		String remoteDir = "/home/ftp1/test";
		String initPath = "/home/ftp1";
		String folder = "test";

//		sshUtil.openSFTPChannel();
//		String localFilepath = "C:\\MOSIP_PT\\Data1\\temp\\checksums.txt";
//		sshUtil.uploadFile(localFilepath);
//		sshUtil.disconnect();

		try {
			Session session = sshUtil.connect();
			sshUtil.openSFTPChannel(session);
			String localFilepath = "C:\\MOSIP_PT\\Data1\\temp\\checksums.txt";
			sshUtil.uploadFile(localFilepath);
			sshUtil.disconnect(session);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());

		}

		// try {
		// Session session = sshUtil.connect();
		// sshUtil.executeCommand("sudo cd " + initPath);
//			sshUtil.executeCommand("pwd");
//			sshUtil.executeCommand("ls -la");
//			sshUtil.executeCommand("sudo mkdir " + folder);
//			sshUtil.executeCommand("sudo chmod 777 " + folder);

		//
		// sshUtil.disconnect();
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			System.out.println(ex.getMessage());
//
//		}
	}

	private void performSyncOperation(PropertiesUtil prop) {
		int c = prop.NUMBER_PACKETS_TO_SYNC;
		List<SyncRegistrationDto> syncDTOs = syncDataClient.obtainSyncRegistrationDtos(prop);
		System.out.println("Size of syncDTOs list " + syncDTOs.size());
		System.out.println("Sync started");
		List<SyncResponseDto> result = service.sync(syncDTOs);
		System.out.println("Sync finished");
		result.stream().forEach(res -> System.out
				.println("Sync result for :-: reg id " + res.getRegistrationId() + " Status : " + res.getStatus()));
		int count = 1;

		for (SyncResponseDto syncResultDto : result) {

			String syncStatus = syncResultDto.getStatus();
			if (syncStatus.equalsIgnoreCase("success")) {
				InternalRegistrationStatusDto registrationStatusDto = null;
				String moduleId = PlatformSuccessMessages.PACKET_RECEIVER_VALIDATION_SUCCESS.name();
				String moduleName = ModuleName.PACKET_RECEIVER.name();
				registrationStatusDto = syncDataClient.obtainRegistrationStatusData(syncResultDto.getRegistrationId());
				registrationStatusService.addRegistrationStatus(registrationStatusDto, moduleId, moduleName);
				System.out.println("registration an dtransaction record created for "
						+ syncResultDto.getRegistrationId() + " " + count + "th packet");
			}
			count++;
		}
	}
}
