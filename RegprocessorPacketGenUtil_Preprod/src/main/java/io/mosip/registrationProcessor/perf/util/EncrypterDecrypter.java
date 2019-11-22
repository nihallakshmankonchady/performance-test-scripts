package io.mosip.registrationProcessor.perf.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.mosip.dbdto.CryptomanagerDto;
import io.mosip.dbdto.CryptomanagerRequestDto;
import io.mosip.dbdto.DecrypterDto;
import io.mosip.dbentity.TokenGenerationEntity;
import io.mosip.registration.processor.perf.packet.dto.FieldValueArray;
import io.mosip.registrationProcessor.perf.packet.constants.PacketFiles;
import io.mosip.util.CryptoUtil;
import io.mosip.util.HMACUtils;
import io.restassured.response.Response;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * 
 * @author M1047227
 *
 */

public class EncrypterDecrypter {
	private static Logger logger = Logger.getLogger(EncrypterDecrypter.class);
	// static ApplicationLibrary applnMethods = new ApplicationLibrary();
	// RegProcApiRequests apiRequests = new RegProcApiRequests();
	// private final String decrypterURL = "/v1/cryptomanager/decrypt";
	// private final String encrypterURL = "/v1/cryptomanager/encrypt";
	// private String applicationId = "REGISTRATION";
	// InputStream outstream = null;
	// TokenGeneration generateToken = new TokenGeneration();
	// TokenGenerationEntity tokenEntity = new TokenGenerationEntity();
	// String validToken = "";

	public String getToken(String tokenType, PropertiesUtil prop) throws IOException {
		TokenGeneration generateToken = new TokenGeneration();
		TokenGenerationEntity tokenEntity = new TokenGenerationEntity();
		String tokenGenerationProperties = generateToken.readPropertyFile(tokenType);
		tokenEntity = generateToken.createTokenGeneratorDto(tokenGenerationProperties);
		String token = generateToken.getToken(tokenEntity, prop);
		return token;
	}

	public void generateHash(byte[] fileByte) {
		if (fileByte != null) {
			HMACUtils.update(fileByte);
		}
	}

	/**
	 * 
	 * @param file
	 * @param destinationPath
	 * @return
	 * @throws IOException
	 * @throws ZipException
	 * @throws ParseException
	 */
	public File decryptFile(JSONObject decryptDto, String destinationPath, String fileName, PropertiesUtil prop)
			throws IOException, ZipException, ParseException {
		RegProcApiRequests apiRequests = new RegProcApiRequests();
		String validToken = "";
		InputStream outstream = null;
		logger.info(destinationPath);
		destinationPath = destinationPath + "//TemporaryValidPackets";
		File folder = new File(destinationPath);
		folder.mkdirs();
		destinationPath = destinationPath + "//" + fileName;
		// Response response=applnMethods.postRequestToDecrypt(decryptDto,
		// decrypterURL);
		validToken = getToken("syncTokenGenerationFilePath", prop);
		boolean tokenStatus = apiRequests.validateToken(validToken, prop);
		while (!tokenStatus) {
			validToken = getToken("syncTokenGenerationFilePath", prop);
			tokenStatus = apiRequests.validateToken(validToken, prop);
		}
		String decrypterURL = "/v1/cryptomanager/decrypt";
		Response response = apiRequests.postRequestToDecrypt(decrypterURL, decryptDto, MediaType.APPLICATION_JSON,
				MediaType.APPLICATION_JSON, validToken, prop);
		JSONObject data = (JSONObject) new JSONParser().parse(response.asString());
		JSONObject responseObject = (JSONObject) data.get("response");
		byte[] decryptedPacket = CryptoUtil.decodeBase64(responseObject.get("data").toString());
		outstream = new ByteArrayInputStream(decryptedPacket);
		logger.info("Outstream is " + outstream);
		FileOutputStream fos = new FileOutputStream(destinationPath);
		fos.write(decryptedPacket);
		fos.close();
		outstream.close();
		ZipFile zipFile = new ZipFile(destinationPath);
		zipFile.extractAll(destinationPath.substring(0, destinationPath.lastIndexOf('.')));
		File extractedFile = new File(destinationPath.substring(0, destinationPath.lastIndexOf('.')));
		return extractedFile;
	}

	public File extractFromDecryptedPacket(String destinationPath, String fileName) {
		String temporaryPath = destinationPath + "//" + fileName;
		destinationPath = destinationPath + "//TemporaryValidPackets";
		File folder = new File(destinationPath);
		folder.mkdirs();
		destinationPath = destinationPath + "//" + fileName;
		try {
			FileUtils.copyFile(new File(temporaryPath), new File(destinationPath));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(destinationPath);
			logger.info("Path : " + destinationPath);
			logger.info("zip : " + zipFile.isValidZipFile());
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			zipFile.extractAll(destinationPath.substring(0, destinationPath.lastIndexOf('.')));
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File extractedFile = new File(destinationPath.substring(0, destinationPath.lastIndexOf('.')));
		return extractedFile;
	}

	public String encryptFile(File f, String sourcePath, String destinationPath, String fileName, PropertiesUtil prop)
			throws Exception {
		// ApplicationLibrary applnMethods = new ApplicationLibrary();
		RegProcApiRequests apiRequests = new RegProcApiRequests();
		String decrypterURL = "/v1/cryptomanager/decrypt";
		String encrypterURL = "/v1/cryptomanager/encrypt";
		String applicationId = "REGISTRATION";
		InputStream outstream = null;
		TokenGeneration generateToken = new TokenGeneration();
		TokenGenerationEntity tokenEntity = new TokenGenerationEntity();
		String validToken = "";
		// sourcePath = sourcePath + "//TemporaryValidPackets";
		File folder = new File(destinationPath);
		folder.mkdirs();
		org.zeroturnaround.zip.ZipUtil.pack(new File(sourcePath + "/" + f.getName()),
				new File(destinationPath + "/" + fileName + ".zip"));
		File file1 = new File(destinationPath + "/" + fileName + ".zip");
		JSONObject decryptedFileBody = new JSONObject();
		decryptedFileBody = generateCryptographicDataEncryption(file1);
		logger.info("encrypt request packet  : " + decryptedFileBody);
		boolean tokenStatus = apiRequests.validateToken(validToken, prop);
		System.out.println("status of the token is " + tokenStatus);
		while (!tokenStatus) {
			validToken = getToken("syncTokenGenerationFilePath", prop);
			tokenStatus = apiRequests.validateToken(validToken, prop);
		}
		System.out.println("After while loop, status of the token is " + tokenStatus);
		Response response = apiRequests.postRequestToEncrypt(encrypterURL, decryptedFileBody,
				MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, validToken, prop);
		try {
			JSONObject data = (JSONObject) new JSONParser().parse(response.asString());
			JSONObject responseObject = (JSONObject) data.get("response");
			// String encryptedPacketString=
			// CryptoUtil.encodeBase64(data.get("data").toString().getBytes());
			byte[] encryptedPacket = responseObject.get("data").toString().getBytes();
			outstream = new ByteArrayInputStream(encryptedPacket);
			logger.info("Outstream is " + outstream);
			String encryptedFile = destinationPath + "/" + fileName + ".zip";
			FileOutputStream fos = new FileOutputStream(encryptedFile);
			fos.write(encryptedPacket);
			fos.close();
			outstream.close();
			return encryptedFile;
		} catch (ParseException e) {
			System.out.println("response object as String:- " + response.toString());
			throw new Exception(e);
		} catch (Exception ex) {
			System.out.println("response object as String:- " + response.toString());
			throw new Exception(ex);
		}
		// return fileName;

	}

	public byte[] encryptFile(File f, String sourcePath, String destinationPath, String fileName, String token,
			PropertiesUtil prop) throws Exception {
		RegProcApiRequests apiRequests = new RegProcApiRequests();
		String decrypterURL = "/v1/cryptomanager/decrypt";
		String encrypterURL = "/v1/cryptomanager/encrypt";
		String applicationId = "REGISTRATION";
		InputStream outstream = null;
		TokenGeneration generateToken = new TokenGeneration();
		TokenGenerationEntity tokenEntity = new TokenGenerationEntity();
		String validToken = "";
		// sourcePath = sourcePath + "//TemporaryValidPackets";
		File folder = new File(destinationPath);
		folder.mkdirs();
		org.zeroturnaround.zip.ZipUtil.pack(new File(sourcePath + "/" + f.getName()),
				new File(destinationPath + "/" + fileName + ".zip"));
		File file1 = new File(destinationPath + "/" + fileName + ".zip");
		JSONObject decryptedFileBody = new JSONObject();
		decryptedFileBody = generateCryptographicDataEncryption(file1);
		logger.info("encrypt request packet  : " + decryptedFileBody);
		Response response = apiRequests.postRequestToEncrypt(encrypterURL, decryptedFileBody,
				MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, token, prop);
		try {
			JSONObject data = (JSONObject) new JSONParser().parse(response.asString());
			JSONObject responseObject = (JSONObject) data.get("response");
			// String encryptedPacketString=
			// CryptoUtil.encodeBase64(data.get("data").toString().getBytes());
			System.out.println(
					"responseObject.toString().contains(\"data\") :- " + responseObject.toString().contains("data"));
			byte[] encryptedPacket = responseObject.get("data").toString().getBytes();
			outstream = new ByteArrayInputStream(encryptedPacket);
			logger.info("Outstream is " + outstream);
			FileOutputStream fos = new FileOutputStream(destinationPath + "/" + fileName + ".zip");
			fos.write(encryptedPacket);
			fos.close();
			outstream.close();
			return encryptedPacket;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Response of encryption:- " + response.asString());
			e.printStackTrace();
			throw new Exception(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

	}

	public void destroyFiles(String filePath) throws IOException {
		logger.info("Destroying Files");
		filePath = filePath + "//TemporaryValidPackets";
		File file = new File(filePath);
		File[] listOfFiles = file.listFiles();

		for (File f : listOfFiles) {
			if (f.isFile()) {
				try {
					FileDeleteStrategy.FORCE.delete(f);
					logger.info("File Was Deleted");
				} catch (Exception e) {
					logger.info(f.getName() + " Was Not Deleted");
					e.printStackTrace();
				}
			} else if (f.isDirectory()) {
				try {
					FileUtils.deleteDirectory(f);
					logger.info("Folder Was Deleted");
				} catch (Exception e) {
					logger.info(f.getName() + "  Was Not Deleted");
				}
			}
		}
		try {
			FileUtils.deleteDirectory(file);
			logger.info("Decrypted File Was Deleted");
		} catch (Exception e) {
			logger.info("Decrypted File Has Some Files In It");
		}
	}

	/*
	 * public void revertPacketToValid(String filePath) throws
	 * FileNotFoundException, IOException { File zipFile = new
	 * File(filePath+".zip"); JSONObject cryptographicRequest=new JSONObject();
	 * cryptographicRequest=generateCryptographicDataEncryption(zipFile); Response
	 * response=applnMethods.postRequestToDecrypt(cryptographicRequest,
	 * encrypterURL); try { JSONObject data= (JSONObject) new
	 * JSONParser().parse(response.asString()); byte[] encryptedPacket =
	 * CryptoUtil.decodeBase64(data.get("data").toString()); outstream = new
	 * ByteArrayInputStream(encryptedPacket); logger.info("Outstream is "+
	 * outstream); FileOutputStream fos= new FileOutputStream(filePath+".zip");
	 * fos.write(encryptedPacket); fos.close(); outstream.close(); } catch
	 * (ParseException e) { // TODO Auto-generated catch block e.printStackTrace();
	 * } }
	 */
	@SuppressWarnings("unchecked")
	public JSONObject generateCryptographicData(File file) {
		RegProcApiRequests apiRequests = new RegProcApiRequests();
		String decrypterURL = "/v1/cryptomanager/decrypt";
		String encrypterURL = "/v1/cryptomanager/encrypt";
		String applicationId = "REGISTRATION";
		InputStream outstream = null;
		TokenGeneration generateToken = new TokenGeneration();
		TokenGenerationEntity tokenEntity = new TokenGenerationEntity();
		String validToken = "";
		JSONObject cryptographicRequest = new JSONObject();
		JSONObject decryptionRequest = new JSONObject();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'T'HHmmssSSS");
		InputStream encryptedPacket = null;
		DecrypterDto decrypterDto = new DecrypterDto();
		CryptomanagerRequestDto cryptoRequest = new CryptomanagerRequestDto();
		CryptomanagerDto request = new CryptomanagerDto();
		String centerId = file.getName().substring(0, 5);
		String machineId = file.getName().substring(5, 10);
		try {
			encryptedPacket = new FileInputStream(file);
			byte[] fileInBytes = FileUtils.readFileToByteArray(file);
			// String encryptedPacketString=
			// Base64.getEncoder().encodeToString(fileInBytes);
			String encryptedPacketString = IOUtils.toString(encryptedPacket, "UTF-8");
			encryptedPacketString = encryptedPacketString.replaceAll("\\s+", "");
			String registrationId = file.getName().substring(0, file.getName().lastIndexOf('.'));
			String packetCreatedDateTime = registrationId.substring(registrationId.length() - 14);
			int n = 100 + new Random().nextInt(900);
			String milliseconds = String.valueOf(n);
			encryptedPacket.close();
			Date date = formatter.parse(packetCreatedDateTime.substring(0, 8) + "T"
					+ packetCreatedDateTime.substring(packetCreatedDateTime.length() - 6) + milliseconds);
			LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
			Date currentDate = new Date();
			LocalDateTime requestTime = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault());

			decrypterDto.setApplicationId(applicationId);
			decrypterDto.setReferenceId(centerId + "_" + machineId);
			decrypterDto.setData(encryptedPacketString);
			decrypterDto.setTimeStamp(ldt);
			request.setRequesttime(requestTime);
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			cryptographicRequest.put("applicationId", applicationId);
			cryptographicRequest.put("data", encryptedPacketString);
			cryptographicRequest.put("referenceId", centerId + "_" + machineId);
			cryptographicRequest.put("timeStamp", decrypterDto.getTimeStamp().atOffset(ZoneOffset.UTC).toString());
			decryptionRequest.put("id", "");
			decryptionRequest.put("metadata", "");
			decryptionRequest.put("request", cryptographicRequest);
			decryptionRequest.put("requesttime", request.getRequesttime().atOffset(ZoneOffset.UTC).toString());
			decryptionRequest.put("version", "");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.info("Could Not ");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return decryptionRequest;
	}

	@SuppressWarnings("unchecked")
	public JSONObject generateCryptographicDataEncryption(File file) {
		RegProcApiRequests apiRequests = new RegProcApiRequests();
		String decrypterURL = "/v1/cryptomanager/decrypt";
		String encrypterURL = "/v1/cryptomanager/encrypt";
		String applicationId = "REGISTRATION";
		InputStream outstream = null;
		TokenGeneration generateToken = new TokenGeneration();
		TokenGenerationEntity tokenEntity = new TokenGenerationEntity();
		String validToken = "";
		JSONObject encryptRequest = new JSONObject();
		CryptomanagerDto request = new CryptomanagerDto();
		JSONObject cryptographicRequest = new JSONObject();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'T'HHmmssSSS");
		InputStream encryptedPacket = null;
		DecrypterDto decrypterDto = new DecrypterDto();
		// String centerId=file.getName().substring(0,5);
		String refId = file.getName().substring(0, 5) + "_" + file.getName().substring(5, 10);
		try {
			encryptedPacket = new FileInputStream(file);
			byte[] fileInBytes = FileUtils.readFileToByteArray(file);
			String encryptedPacketString = Base64.getEncoder().encodeToString(fileInBytes);
			// String encryptedPacketString = IOUtils.toString(encryptedPacket, "UTF-8");
			encryptedPacketString = encryptedPacketString.replaceAll("\\s+", "");
			String registrationId = file.getName().substring(0, file.getName().lastIndexOf('.'));
			String packetCreatedDateTime = registrationId.substring(registrationId.length() - 14);
			int n = 100 + new Random().nextInt(900);
			String milliseconds = String.valueOf(n);
			encryptedPacket.close();
			Date date = formatter.parse(packetCreatedDateTime.substring(0, 8) + "T"
					+ packetCreatedDateTime.substring(packetCreatedDateTime.length() - 6) + milliseconds);
			LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
			Date currentDate = new Date();
			LocalDateTime requestTime = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault());
			decrypterDto.setApplicationId(applicationId);
			decrypterDto.setReferenceId(refId);
			decrypterDto.setData(encryptedPacketString);
			decrypterDto.setTimeStamp(ldt);
			request.setRequesttime(requestTime);
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			cryptographicRequest.put("applicationId", applicationId);
			cryptographicRequest.put("data", encryptedPacketString);
			// System.out.println("encrypter request data : " + encryptedPacketString);
			cryptographicRequest.put("referenceId", refId);
			cryptographicRequest.put("timeStamp", decrypterDto.getTimeStamp().atOffset(ZoneOffset.UTC).toString());
			encryptRequest.put("id", "");
			encryptRequest.put("metadata", "");
			encryptRequest.put("request", cryptographicRequest);
			encryptRequest.put("requesttime", request.getRequesttime().atOffset(ZoneOffset.UTC).toString());
			encryptRequest.put("version", "");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encryptRequest;
	}

	public synchronized String generateCheckSum(List<FieldValueArray> hashSequence, String packetPath)
			throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		for (FieldValueArray fieldValueArray : hashSequence) {

			if (PacketFiles.APPLICANTBIOMETRICSEQUENCE.name().equalsIgnoreCase(fieldValueArray.getLabel())) {

				generateBiometricsHash(fieldValueArray.getValue(), packetPath, outputStream);

			} else if (PacketFiles.INTRODUCERBIOMETRICSEQUENCE.name().equalsIgnoreCase(fieldValueArray.getLabel())) {

				generateBiometricsHash(fieldValueArray.getValue(), packetPath, outputStream);

			} else if (PacketFiles.APPLICANTDEMOGRAPHICSEQUENCE.name().equalsIgnoreCase(fieldValueArray.getLabel())) {

				generateDemographicHash(fieldValueArray.getValue(), packetPath, outputStream);
			}
		}

		byte[] dataByte = HMACUtils.generateHash(outputStream.toByteArray());
		String hashCodeGenerated = new String(HMACUtils.digestAsPlainText(dataByte).getBytes(), StandardCharsets.UTF_8);
		return hashCodeGenerated;
	}

	/**
	 * Generate biometric infos hash.
	 *
	 * @param hashOrder      the hash order
	 * @param registrationId the registration id
	 * @param personType     the person type
	 */
	private synchronized void generateBiometricsHash(List<String> hashOrder, String packetPath,
			ByteArrayOutputStream outputStream) {
		hashOrder.forEach(file -> {
			byte[] fileByte = null;
			try {
//				InputStream fileStream = adapter.getFile(registrationId,
//						PacketFiles.BIOMETRIC.name() + FILE_SEPARATOR + file.toUpperCase());
				String filePath = packetPath + File.separator + PacketFiles.BIOMETRIC.name() + File.separator
						+ file.toUpperCase();
				if (filePath.toUpperCase().contains("APPLICANT_BIO_CBEFF")) {
					filePath += ".xml";
				}
				InputStream fileStream = new FileUtil().getInputStream(filePath);

				fileByte = IOUtils.toByteArray(fileStream);
				outputStream.write(fileByte);
				fileStream.close();
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		});
	}

	/**
	 * Generate demographic hash.
	 *
	 * @param fieldValueArray the field value array
	 * @param registrationId  the registration id
	 */
	private synchronized void generateDemographicHash(List<String> hashOrder, String packetPath,
			ByteArrayOutputStream outputStream) {
		hashOrder.forEach(document -> {
			byte[] fileByte = null;
			try {
				// InputStream fileStream = adapter.getFile(registrationId,
				// PacketFiles.DEMOGRAPHIC.name() + FILE_SEPARATOR + document.toUpperCase());

				String filePath = packetPath + File.separator + PacketFiles.DEMOGRAPHIC.name() + File.separator
						+ document.toUpperCase();
				if (filePath.toUpperCase().endsWith("ID")) {
					filePath += ".json";
				} else {
					filePath += ".jpg";
				}
				InputStream fileStream = new FileUtil().getInputStream(filePath);

				fileByte = IOUtils.toByteArray(fileStream);
				outputStream.write(fileByte);
				fileStream.close();
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		});
	}

	public synchronized byte[] generatePacketOSIHash(List<FieldValueArray> hashSequence2, String packetPath) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		for (FieldValueArray fieldValueArray : hashSequence2) {
			List<String> hashValues = fieldValueArray.getValue();
			hashValues.forEach(value -> {
				byte[] valuebyte = null;
				try {
					// InputStream fileStream = adapter.getFile(registrationId,
					// value.toUpperCase());

					String filePath = packetPath + File.separator + value.toUpperCase();
					InputStream fileStream = new FileUtil().getInputStream(filePath);

					valuebyte = IOUtils.toByteArray(fileStream);
					outputStream.write(valuebyte);
					fileStream.close();
				} catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			});
		}
		byte[] osiByte = HMACUtils.generateHash(outputStream.toByteArray());

		return HMACUtils.digestAsPlainText(osiByte).getBytes();

	}

	public String generateCheckSum_old(File[] listOfFiles) throws FileNotFoundException, IOException, ParseException {
		JSONArray hashSequence1;
		String hashCodeGenerated = null;
		for (File f : listOfFiles) {
			if (f.getName().contains("packet_meta_info.json")) {
				FileReader metaFileReader = new FileReader(f.getPath());
				JSONObject objectData = (JSONObject) new JSONParser().parse(metaFileReader);
				JSONObject identity = (JSONObject) objectData.get("identity");
				metaFileReader.close();
				hashSequence1 = (JSONArray) identity.get("hashSequence1");
				logger.info("hashSequence1....... : " + hashSequence1);
				for (Object obj : hashSequence1) {
					JSONObject label = (JSONObject) obj;
					logger.info("obj : " + label.get("label"));
					if (label.get("label").equals("applicantBiometricSequence")) {
						@SuppressWarnings("unchecked")
						List<String> docs = (List<String>) label.get("value");
						logger.info("list of documents :: " + docs);
						generateBiometricsHash(docs, listOfFiles);
					} else if (label.get("label").equals("introducerBiometricSequence")) {
						@SuppressWarnings("unchecked")
						List<String> docs = (List<String>) label.get("value");
						logger.info("list of documents :: " + docs);
						generateBiometricsHash(docs, listOfFiles);
					} else if (label.get("label").equals("applicantDemographicSequence")) {
						@SuppressWarnings("unchecked")
						List<String> docs = (List<String>) label.get("value");
						logger.info("list of documents :: " + docs);
						generateDemographicsHash(docs, listOfFiles);
					}
				}
				byte[] checksumBytes = HMACUtils.digestAsPlainText(HMACUtils.updatedHash()).getBytes();
				// hashCodeGenerated = HMACUtils.digestAsPlainText(HMACUtils.updatedHash());
				hashCodeGenerated = new String(checksumBytes, StandardCharsets.UTF_8);
			}
		}
		return hashCodeGenerated;
	}

	private void generateBiometricsHash(List<String> docs, File[] listOfFiles) {
		byte[] fileByte = null;
		for (File file : listOfFiles) {
			if (file.getName().equalsIgnoreCase("Biometric")) {
				File[] demographicFiles = file.listFiles();
				for (File demoFiles : demographicFiles) {
					for (String fileName : docs) {
						if (fileName.equals(demoFiles.getName().substring(0, demoFiles.getName().lastIndexOf('.')))) {
							try {
								FileInputStream inputStream = new FileInputStream(demoFiles);
								fileByte = IOUtils.toByteArray(inputStream);
								generateHash(fileByte);
								inputStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	private void generateDemographicsHash(List<String> docs, File[] listOfFiles) {
		byte[] fileByte = null;
		for (File file : listOfFiles) {
			if (file.getName().equalsIgnoreCase("Demographic")) {
				File[] demographicFiles = file.listFiles();
				for (File demoFiles : demographicFiles) {
					for (String fileName : docs) {
						if (fileName.equals(demoFiles.getName().substring(0, demoFiles.getName().lastIndexOf('.')))) {
							try {
								FileInputStream inputStream = new FileInputStream(demoFiles);
								fileByte = IOUtils.toByteArray(inputStream);
								generateHash(fileByte);
								inputStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

}
