package io.mosip.registrationProcessor.perf.service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

import io.mosip.registration.processor.perf.packet.dto.FieldValueArray;
import io.mosip.registration.processor.perf.packet.dto.Identity;
import io.mosip.registration.processor.perf.packet.dto.PacketMetaInfo;
import io.mosip.registrationProcessor.perf.dto.RegCenterDetailDto;
import io.mosip.registrationProcessor.perf.regPacket.dto.RegProcIdDto;
import io.mosip.registrationProcessor.perf.util.EncrypterDecrypter;
import io.mosip.registrationProcessor.perf.util.JSONUtil;
import io.mosip.registrationProcessor.perf.util.PropertiesUtil;
import io.mosip.registrationProcessor.perf.util.RegCenterDetailFetcher;

public class TweakRegProcPackets {

	private static Logger logger = Logger.getLogger(TweakRegProcPackets.class);

//	PacketDemoDataUtil packetDataUtil;

//	EncrypterDecrypter encryptDecrypt;

//	File decryptedPacket;

	// private String regId;

	// String tempDecryptedpacketContentPath;

//	private String newPacketTempPath;

//	private String checksumStr;

//	private String encryptedTempFile;

	public TweakRegProcPackets() {
		// encryptDecrypt = new EncrypterDecrypter();
		// packetDataUtil = new PacketDemoDataUtil();
		// RegCenterDetailFetcher regCenterDetailFetcher =
		// RegCenterDetailFetcher.obtainRegCenterDetailFetcherInstance();
		// JSONUtil jsonUtil = new JSONUtil();
	}

	public void decryptPacket(String folderhavingEncryptedpacket, PropertiesUtil prop) {
		EncrypterDecrypter encryptDecrypt = new EncrypterDecrypter();
		File decryptedPacket;
		// String folderhavingEncryptedpacket = validPacketForPacketGeneration;
		File filePath = new File(folderhavingEncryptedpacket);
		File[] listOfFiles = filePath.listFiles();

		for (File file : listOfFiles) {
			String fileName = file.getName();
			// System.out.println(file.getName());
			if (fileName.contains(".zip")) {
				String registrationId = fileName.substring(0, fileName.lastIndexOf(".zip"));
				String centerId = fileName.substring(0, 5);
				String machineId = fileName.substring(5, 10);
				writepacketDetailsToPropertyFile(registrationId, centerId, machineId);
				JSONObject requestBody = encryptDecrypt.generateCryptographicData(file);
				try {
					decryptedPacket = encryptDecrypt.decryptFile(requestBody, folderhavingEncryptedpacket, fileName,
							prop);
					System.out.println("decryptedPacket " + decryptedPacket);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Error while decrypting: " + e.getMessage());
				}
			}

		}

	}

	public void writePropertiesToPropertyFile(String propertyFile, Map<String, String> properties) {
		try (OutputStream output = new FileOutputStream(propertyFile);) {
			Properties prop = new Properties();
			Set<Entry<String, String>> entries = properties.entrySet();
			Iterator<Entry<String, String>> iterator = entries.iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				prop.setProperty(entry.getKey(), entry.getValue());
			}
			prop.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
			logger.error("Error while writing to properties file: " + io.getMessage());
		}
	}

	private void writepacketDetailsToPropertyFile(String registrationId, String centerId, String machineId) {

		// String propertiespath = System.getProperty("user.dir") +
		// "/src/configProperties/packetInfo.properties";
		String propertiespath = System.getProperty("user.dir") + "/packetInfo.properties";
		try (OutputStream output = new FileOutputStream(propertiespath);) {
			Properties prop = new Properties();
			prop.setProperty("registrationId", registrationId);
			prop.setProperty("centerId", centerId);
			prop.setProperty("machineId", machineId);
			prop.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
		}

	}

//	public void copyDecryptedpacketToNewLocation(String newPacketFolderPath) {
//		// copy decrypted packets from decryptedPacket to newPacketFolderPath
//		// String temp = newPacketFolderPath +"/"
//		String decryptedpacketPath = decryptedPacket.getAbsolutePath();
//		String[] pathComponents = decryptedpacketPath.split("\\\\");
//		for (String s : pathComponents) {
//			System.out.println(s);
//		}
//		String folderName = pathComponents[pathComponents.length - 1];
//		// String propertiespath = System.getProperty("user.dir") +
//		// "/src/configProperties/packetFolderName.properties";
//		String propertiespath = System.getProperty("user.dir") + "/packetFolderName.properties";
//		Map<String, String> propertiesmap = new HashMap<>();
//		propertiesmap.put("folderName", folderName);
//		// writePropertiesToPropertyFile(propertiespath, propertiesmap);
//		System.out.println(folderName);
//		File newPath = new File(newPacketFolderPath + "/" + folderName);
//		newPath.mkdirs();
//		System.out.println(decryptedpacketPath);
//
//		try {
//			FileUtils.copyDirectory(decryptedPacket, newPath);
//		} catch (IOException e) {
//			e.printStackTrace();
//			logger.error("Error while copying file to " + newPacketFolderPath + " : " + e.getMessage());
//		}
//	}

	public byte[] modifyDemoDataOfDecryptedPacket(String newPacketFolderPath, String packetGenPath,
			String checksumslogFilePath, String token, PropertiesUtil prop, Session session) throws Exception {
		Gson gson = new Gson();
		Properties properties = new Properties();
		EncrypterDecrypter encryptDecrypt = new EncrypterDecrypter();
		PacketDemoDataUtil packetDataUtil = new PacketDemoDataUtil();
		RegCenterDetailFetcher regCenterDetailFetcherObj = new RegCenterDetailFetcher();
		String originalRegId = ""; // read from "src/"
		// TODO
		JSONUtil jsonUtil = new JSONUtil();
		String packetInfoPropertiespath = System.getProperty("user.dir") + "/packetInfo.properties";

		// String propertiespath = System.getProperty("user.dir") +
		// "/src/configProperties/packetInfo.properties";
		FileReader reader = null;
		try {
			reader = new FileReader(new File(packetInfoPropertiespath));
			properties.load(reader);

		} catch (IOException e1) {
			logger.error(" modifyDemoDataOfDecryptedPacket : " + e1.getMessage());
			throw new Exception(e1);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				throw new Exception(e);
			}
		}
		originalRegId = properties.getProperty("registrationId");
		// TODO Read center ID and machine ID and user ID from DB
		// String centerId = properties.getProperty("centerId");
		// String machineId = properties.getProperty("machineId");
		// String userId = "110032"; // Present in osiData of packet_meta_info.json

		/*
		 * if(regCenterDetailFetcherObj.getRegistratonCenters().size()==0) {
		 * regCenterDetailFetcher.initializeRegCenterDetailFetcher(); }
		 */

//		RegCenterDetailDto regCenterDto = regCenterDetailFetcherObj
//				.obtainNextData(regCenterDetailFetcherObj.getRegistratonCenters(session));
		// String centerId = regCenterDto.getRegCenterId();
		// String machineId = regCenterDto.getMachineId();
		// String userId = regCenterDto.getUserId();

		String centerId = "10006";
		String machineId = "10043";
		String userId = "110046";

		String regId = packetDataUtil.generateRegId(centerId, machineId);
		String tempPath = newPacketFolderPath + "temp";
		new File("tempPath").mkdirs();
		String tempDecryptedpacketContentPath = tempPath + "/" + regId;
		File tempPacketFile = new File(tempDecryptedpacketContentPath);

		tempPacketFile.mkdirs();
		String decryptedPacketPath = prop.VALID_PACKET_PATH_FOR_PACKET_GENERATION + "TemporaryValidPackets"
				+ File.separator + originalRegId;
		File srcDir = new File(decryptedPacketPath);
		File packetDir = new File(tempDecryptedpacketContentPath);
		try {
			FileUtils.copyDirectory(srcDir, packetDir);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(" modifyDemoDataOfDecryptedPacket : " + e.getMessage());
		}

		// Modify JSON data and write to ID.json inside the Demographic directory

		String idJsonPath = decryptedPacketPath + File.separator + "Demographic" + File.separator + "ID.json";

		RegProcIdDto updatedPacketDemoDto = packetDataUtil.modifyDemographicData1(prop, session, idJsonPath);
		String demoJsonPath = tempDecryptedpacketContentPath + "/Demographic/ID.json";
		jsonUtil.writeJsonToFile(gson.toJson(updatedPacketDemoDto), demoJsonPath);

		String packetMetaInfoFile = tempDecryptedpacketContentPath + "/packet_meta_info.json";
		packetDataUtil.modifyPacketMetaInfo(packetMetaInfoFile, regId, centerId, machineId, userId);
		String checksum = "";

		PacketMetaInfo jsonObj = new JSONUtil().parseMetaInfoFile(packetMetaInfoFile);
		Identity identity = jsonObj.getIdentity();
		List<FieldValueArray> hashSequence = identity.getHashSequence1();

		@SuppressWarnings("unused")
		String encryptedTempFile;
		try {
			// checksum = encryptDecrypt.generateCheckSum(packetDir.listFiles());
			checksum = encryptDecrypt.generateCheckSum(hashSequence, tempDecryptedpacketContentPath);
			System.out.println("Generated checksum is " + checksum);
			// checksumStr = new String(checkSum, StandardCharsets.UTF_8);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			logger.error(" modifyDemoDataOfDecryptedPacket generateChecksum: " + e.getMessage());
		}

		// packetDataUtil.logRegIdCheckSumToFile(regId, checksumStr);

		try {
			packetDataUtil.writeChecksumToFile(tempDecryptedpacketContentPath + "/packet_data_hash.txt", checksum);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(" modifyDemoDataOfDecryptedPacket writeChecksumToFile: " + e.getMessage());
		}

		try {
			byte[] encryptedPacket = encryptDecrypt.encryptFile(packetDir, tempPath, newPacketFolderPath + "zipped",
					regId, token, prop);
			logRegIdCheckSumToFile(packetGenPath, checksumslogFilePath, regId, checksum);
			String regidLogFilePath = prop.REGID_LOG_FILE;
			logRegIdsToFile(regidLogFilePath, regId);

			/*
			 * Delete the folders in temp directory on successful generation of encrypted
			 * packet to avoid unnecessary space waistage
			 */
			deleteFolderInTempDir(newPacketFolderPath + "temp", regId);
			/*
			 * Added code to check if able to decrypt the encrypted packet
			 */

//			String folderhavingEncryptedpacket = newPacketFolderPath + "zipped";
//			String fileName = regId + ".zip";
//			File encryptedFile = new File(folderhavingEncryptedpacket + File.separator + fileName);
//
//			JSONObject requestBody = encryptDecrypt.generateCryptographicData(encryptedFile);
//			try {
//				decryptedPacket = encryptDecrypt.decryptFile(requestBody, folderhavingEncryptedpacket, fileName, prop);
//				System.out.println("decryptedPacket " + decryptedPacket);
//			} catch (Exception e) {
//				e.printStackTrace();
//				logger.error("Error while decrypting: " + e.getMessage());
//			}

			return encryptedPacket;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" modifyDemoDataOfDecryptedPacket encryptPacket: " + e.getMessage());
			String fileToDelStr = newPacketFolderPath + "/zipped/" + regId + ".zip";
			File fileToDel = new File(fileToDelStr);
			fileToDel.exists();
			fileToDel.isDirectory();
			if (fileToDel.exists()) {
				fileToDel.delete();
				if (fileToDel.exists())
					FileUtils.deleteQuietly(fileToDel);
			}
		}
		return null;
	}

	private synchronized void logRegIdsToFile(String logFilePath, String regId) throws Exception {
		PacketDemoDataUtil packetDataUtil = new PacketDemoDataUtil();
		packetDataUtil.logRegIdsToFile(logFilePath, regId);
	}

	public synchronized void logRegIdCheckSumToFile(String packetGenPath, String logFilePath, String regId,
			String checksumStr) {
		String generatedPacket = packetGenPath + regId + ".zip";
		PacketDemoDataUtil packetDataUtil = new PacketDemoDataUtil();
		File f = new File(generatedPacket);
		if (f.exists() && f.isFile()) {
			long sizeInBytes = f.length();
			String center_machine_refID = regId.substring(0, 5) + "_" + regId.substring(5, 10);
			packetDataUtil.logRegIdCheckSumToFile(logFilePath, regId, checksumStr, sizeInBytes, center_machine_refID);
			// new SyncRequestCreater().createSyncRequest(regId, checksumStr, sizeInBytes);
		}

	}

	public synchronized void deleteFolderInTempDir(String basePath, String regId) {

		String folderToDelete = basePath + "/" + regId;
		File fileToDel = new File(folderToDelete);
		fileToDel.exists();
		fileToDel.isDirectory();
		if (fileToDel.exists() && fileToDel.isDirectory()) {
			try {
				FileUtils.deleteDirectory(fileToDel);
				logger.info("deleteFolderInTempDir " + " deleted the packet " + folderToDelete);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}

	}

}
