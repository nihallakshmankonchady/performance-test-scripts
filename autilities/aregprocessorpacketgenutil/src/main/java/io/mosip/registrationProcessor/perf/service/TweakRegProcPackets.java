package io.mosip.registrationProcessor.perf.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

import io.mosip.registration.processor.perf.packet.dto.FieldValueArray;
import io.mosip.registration.processor.perf.packet.dto.Identity;
import io.mosip.registration.processor.perf.packet.dto.PacketMetaInfo;
import io.mosip.registrationProcessor.perf.regPacket.dto.RegProcIdDto;
import io.mosip.registrationProcessor.perf.util.EncrypterDecrypter;
import io.mosip.registrationProcessor.perf.util.JSONUtil;
import io.mosip.registrationProcessor.perf.util.PropertiesUtil;
import io.mosip.registrationProcessor.perf.util.RegCenterDetailFetcher;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class TweakRegProcPackets {

	private static Logger logger = Logger.getLogger(TweakRegProcPackets.class);

	public TweakRegProcPackets() {

	}

	public void decryptPacket(String token, String folderhavingEncryptedpacket, PropertiesUtil prop) {
		File filePath = new File(folderhavingEncryptedpacket);
		File[] listOfFiles = filePath.listFiles();
		File fileToOperate = null;
		String registrationId = "";
		String centerId = "";
		String machineId = "";
		for (File file : listOfFiles) {
			String fileName = file.getName();
			if (fileName.contains(".zip")) {
				registrationId = fileName.substring(0, fileName.lastIndexOf(".zip"));
				centerId = fileName.substring(0, 5);
				machineId = fileName.substring(5, 10);
				writeRegIdToPropertyFile(registrationId);
				writepacketDetailsToPropertyFile(registrationId, centerId, machineId);
				fileToOperate = file;
			}
		}

//		for (File file : listOfFiles) {
//
//			String filename = file.getName();
//			if (filename.contains(".zip")) {
//				System.out.println(folderhavingEncryptedpacket + filename);
//				String tempFolder = folderhavingEncryptedpacket + "TemporaryValidPackets";
//				System.out.println("Temp folder path:- " + tempFolder);
//				String extractedPath = extractZippedFile(file, tempFolder);
//				decryptFiles(token, extractedPath, prop);
//			}
//		}

		String tempFolder = folderhavingEncryptedpacket + "TemporaryValidPackets";
		System.out.println("Temp folder path:- " + tempFolder);
		String extractedPath = "";
		if (null != fileToOperate) {
			extractedPath = extractZippedFile(fileToOperate, tempFolder);
			decryptFiles(token, extractedPath, prop);
			File srcDir = new File(extractedPath);

		}

	}

	private String readRegIdFromPropertyFile(String key) {

		Properties properties = new Properties();
		String propertiespath = System.getProperty("user.dir") + "/packetFolderName.properties";
		try {
			FileReader reader = new FileReader(new File(propertiespath));
			properties.load(reader);
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String oldRegId = properties.getProperty(key);
		return oldRegId;

	}

	private void writeRegIdToPropertyFile(String registrationId) {

		String propertiespath = System.getProperty("user.dir") + "/packetFolderName.properties";
		try (OutputStream output = new FileOutputStream(propertiespath);) {
			Properties prop = new Properties();
			prop.setProperty("oldRegId", registrationId);
			prop.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
		}

	}

	private void decryptFiles(String token, String extractedPath, PropertiesUtil prop) {
		EncrypterDecrypter encryptDecrypt = new EncrypterDecrypter();
		File filePath = new File(extractedPath);
		File[] listOfFiles = filePath.listFiles();
		List<File> filesToDelete = new ArrayList<>();
		int counter = 0;
		try {
			for (File file : listOfFiles) {
				String fileName = file.getName();
				if (fileName.contains(".zip")) {
					filesToDelete.add(file);
					JSONObject requestBody = encryptDecrypt.generateCryptographicData(file);
					try {
						File decryptedPacket = encryptDecrypt.decryptFile(token, requestBody, extractedPath, fileName,
								prop);
						System.out.println("decryptedPacket " + decryptedPacket);
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("Error while decrypting: " + e.getMessage());
					}
					counter++;
					System.out.println("decrypted " + counter + "th packet");
				}

			}
			filesToDelete.forEach((fileD) -> {
				try {
					FileUtils.forceDelete(fileD);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("decrypted " + counter + " packets...");

	}

	private String extractZippedFile(File file, String tempFolderPath) {

		String destFile = tempFolderPath + "/" + file.getName();
		if (new File(destFile).exists()) {
			FileUtils.deleteQuietly(new File(destFile));
		}
		String destPath = destFile.substring(0, destFile.lastIndexOf('.'));
		if (new File(destPath).exists()) {
			try {
				FileUtils.deleteDirectory(new File(destPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {

			FileUtils.copyFile(file, new File(destFile));
			System.out.println("Copied to " + destFile);
			ZipFile zipFile = new ZipFile(destFile);

			System.out.println("Unzipping to::: " + destPath);
			// if (!new File(destPath).exists())
			// new File(destPath).mkdir();
			zipFile.extractAll(destPath);
		} catch (IOException | ZipException e) {
			e.printStackTrace();
		}
		System.out.println("extraction step over");
		return destPath;

	}

	public void decryptPacket_0(String token, String folderhavingEncryptedpacket, PropertiesUtil prop) {
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
					decryptedPacket = encryptDecrypt.decryptFile(token, requestBody, folderhavingEncryptedpacket,
							fileName, prop);
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

	private void copyPacketContentToWorkLocation(String newRegId, PropertiesUtil prop) {

		// Create parent directory with name as regid
		// set the created folder as curr working dir
		// Copy the three folders one by one

//		Properties folderPath = new Properties();
//		try {
//			FileReader reader = new FileReader(
//					new File(System.getProperty("user.dir") + "/src/configProperties/folderPaths.properties"));
//			folderPath.load(reader);
//			reader.close();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//
//		String folderhavingEncryptedpacket = folderPath.getProperty("validPacketForPacketGeneration");
//		String tempFolder = folderhavingEncryptedpacket + "TemporaryValidPackets";

		String originalRegId = readRegIdFromPropertyFile("oldRegId");
		String srcPath = prop.VALID_PACKET_PATH_FOR_PACKET_GENERATION + "/" + "TemporaryValidpackets" + "/"
				+ originalRegId;
		String destinationPath = prop.NEW_PACKET_FOLDER_PATH + "temp/" + newRegId;
		File destDir = new File(destinationPath);

		File srcDir = new File(srcPath);
		if (!destDir.exists())
			destDir.mkdir();
		String evidenceSrcPath = srcPath + File.separator + originalRegId + "_evidence";
		String idSrcPath = srcPath + File.separator + originalRegId + "_id";
		String optionalSrcPath = srcPath + File.separator + originalRegId + "_optional";

		String destOptionalPath = destinationPath + File.separator + newRegId + "_optional";
		String destIdPath = destinationPath + File.separator + newRegId + "_id";
		String destEvidencePath = destinationPath + File.separator + newRegId + "_evidence";

		new File(destOptionalPath).mkdir();
		new File(destIdPath).mkdir();
		new File(destEvidencePath).mkdir();

		copyDirectory(new File(evidenceSrcPath), new File(destEvidencePath));
		copyDirectory(new File(idSrcPath), new File(destIdPath));
		copyDirectory(new File(optionalSrcPath), new File(destOptionalPath));

		System.out.println("All directories are copied");
	}

	private void copyDirectory(File srcDir, File destDir) {
		try {
			FileUtils.copyDirectory(srcDir, destDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] modifyDemoDataOfDecryptedPacket(String newPacketFolderPath, String packetGenPath,
			String checksumslogFilePath, String token, PropertiesUtil prop, Session session) throws Exception {

		Gson gson = new Gson();
		Properties properties = new Properties();
		EncrypterDecrypter encryptDecrypt = new EncrypterDecrypter();
		JSONUtil jsonUtil = new JSONUtil();

		String packetInfoPropertiespath = System.getProperty("user.dir") + File.separator + "packetInfo.properties";
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

		String originalRegId = properties.getProperty("registrationId");
		String centerId = properties.getProperty("centerId");
		String machineId = properties.getProperty("machineId");
		String userId = "110119";

		PacketDemoDataUtil packetDataUtil = new PacketDemoDataUtil();
		/*
		 * Generate new registration ID
		 */
		String newRegId = packetDataUtil.generateRegId(centerId, machineId);
		String PARENT_FOLDER_PATH = prop.NEW_PACKET_FOLDER_PATH + "temp";
		String packetFolder = PARENT_FOLDER_PATH + "/" + newRegId;
		copyPacketContentToWorkLocation(newRegId, prop);

		// writeRegIdToPropertyFile(newRegId);
//		
//		File destDir = new File(destinationFolder);
//		String srcPath = prop.VALID_PACKET_PATH_FOR_PACKET_GENERATION + "/" + "TemporaryValidpackets" + "/"
//				+ originalRegId;
//		File srcDir = new File(srcPath);
//		try {
//			FileUtils.copyDirectory(srcDir, destDir);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		String identityFolder = packetFolder + File.separator + newRegId + "_id";

		String idJsonPath = packetFolder + File.separator + newRegId + "_id" + File.separator + "ID.json";
		RegProcIdDto updatedPacketDemoDto = packetDataUtil.modifyDemographicData1(prop, session, idJsonPath);

		// String demoJsonPath = tempDecryptedpacketContentPath +
		// "/Demographic/ID.json";
		jsonUtil.writeJsonToFile(gson.toJson(updatedPacketDemoDto), idJsonPath);
		String packetMetaInfoFile = packetFolder + File.separator + newRegId + "_id" + File.separator
				+ "packet_meta_info.json";
		packetDataUtil.modifyPacketMetaInfo(packetMetaInfoFile, newRegId, centerId, machineId, userId);

		System.out.println("packet meta info modified...._....");

		PacketMetaInfo jsonObj = new JSONUtil().parseMetaInfoFile(packetMetaInfoFile);
		Identity identity = jsonObj.getIdentity();
		List<FieldValueArray> hashSequence = identity.getHashSequence1();
		String checksum = "";
		@SuppressWarnings("unused")
		String encryptedTempFile;
		try {
			checksum = encryptDecrypt.generateCheckSum(hashSequence, packetFolder + File.separator + newRegId + "_id");
			System.out.println("Generated checksum is " + checksum);
			// checksumStr = new String(checkSum, StandardCharsets.UTF_8);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			logger.error(" modifyDemoDataOfDecryptedPacket generateChecksum: " + e.getMessage());
		}

		try {
			packetDataUtil.writeChecksumToFile(identityFolder + "/packet_data_hash.txt", checksum);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(" modifyDemoDataOfDecryptedPacket writeChecksumToFile: " + e.getMessage());
		}

		String zippedPacketFolder = prop.NEW_PACKET_FOLDER_PATH + "zipped";
		new File(zippedPacketFolder).mkdir();
		zipPacketContents(packetFolder, zippedPacketFolder, encryptDecrypt, token, prop);
		
		logRegIdCheckSumToFile(packetGenPath, checksumslogFilePath, newRegId, checksum);
		String regidLogFilePath = prop.REGID_LOG_FILE;
		logRegIdsToFile(regidLogFilePath, newRegId);
		
		
		deleteFolderInTempDir(PARENT_FOLDER_PATH, newRegId);
		return null;
	}

	private void zipPacketContents(String packetFolderPath, String zippedPacketFolder,
			EncrypterDecrypter encryptDecrypt, String token, PropertiesUtil prop) {

		String foldername = packetFolderPath.substring(packetFolderPath.length() - 29);
		System.out.println(foldername);
		String destFolder = zippedPacketFolder + "/" + foldername;
		new File(destFolder).mkdir();

		String srcFolder_evidence = packetFolderPath + "/" + foldername + "_evidence";
		String srcFolder_id = packetFolderPath + "/" + foldername + "_id";
		String srcFolder_optional = packetFolderPath + "/" + foldername + "_optional";

		zipInnerFoldersAndEncrypt(srcFolder_evidence, destFolder, foldername + "_evidence", encryptDecrypt, token,
				prop);
		zipInnerFoldersAndEncrypt(srcFolder_id, destFolder, foldername + "_id", encryptDecrypt, token, prop);
		zipInnerFoldersAndEncrypt(srcFolder_optional, destFolder, foldername + "_optional", encryptDecrypt, token,
				prop);
		System.out.println("all folder contents copied");

		deleteDirectories(destFolder, foldername);
		
		zipDirectory(zippedPacketFolder, foldername);

	}
	
	private void zipDirectory(String zippedPacketFolder, String foldername) {
		
		File dirToZip = new File(zippedPacketFolder + "/" + foldername);
		File zipFile = new File(zippedPacketFolder + "/" + foldername + ".zip");

		org.zeroturnaround.zip.ZipUtil.pack(dirToZip, zipFile);
		try {
			FileUtils.deleteDirectory(dirToZip);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void deleteDirectories(String directory, String regid) {

		File dir1 = new File(directory + "/" + regid + "_evidence");
		File dir2 = new File(directory + "/" + regid + "_id");
		File dir3 = new File(directory + "/" + regid + "_optional");

		try {
			FileUtils.deleteDirectory(dir1);
			FileUtils.deleteDirectory(dir2);
			FileUtils.deleteDirectory(dir3);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void zipInnerFoldersAndEncrypt(String packetFolder, String destPath, String destFolder,
			EncrypterDecrypter encryptDecrypt, String token, PropertiesUtil prop) {

		try {
			FileUtils.copyDirectory(new File(packetFolder), new File(destPath + "/" + destFolder));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			encryptDecrypt.encryptDirectory(destPath, destFolder, token, prop);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public byte[] modifyDemoDataOfDecryptedPacket_0(String newPacketFolderPath, String packetGenPath,
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
		originalRegId = "10006100360000720191015045421";
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
			checksum = encryptDecrypt.generateCheckSum_old(packetDir.listFiles());
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
			byte[] encryptedPacket = encryptDecrypt.encryptFile_0(packetDir, tempPath, newPacketFolderPath + "zipped",
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
				logger.info("deleteFolderInTempDir " + " deleted the packet directory:- " + folderToDelete);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}

	}

}
