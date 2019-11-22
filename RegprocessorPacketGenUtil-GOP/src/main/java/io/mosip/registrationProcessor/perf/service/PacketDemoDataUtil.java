package io.mosip.registrationProcessor.perf.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.json.simple.JSONObject;

import com.google.gson.Gson;

import io.mosip.dbdto.Address;
import io.mosip.dbdto.DocumentDetails;
import io.mosip.dbdto.FullName;
import io.mosip.dbdto.PlaceOfBirth;
import io.mosip.registrationProcessor.perf.dto.DOBDto;
import io.mosip.registrationProcessor.perf.dto.RegDataCSVDto;
import io.mosip.registrationProcessor.perf.entity.Location;
import io.mosip.registrationProcessor.perf.regPacket.dto.DocumentData;
import io.mosip.registrationProcessor.perf.regPacket.dto.FieldData;
import io.mosip.registrationProcessor.perf.regPacket.dto.Identity;
import io.mosip.registrationProcessor.perf.regPacket.dto.RegProcIdDto;
import io.mosip.registrationProcessor.perf.util.JSONUtil;
import io.mosip.registrationProcessor.perf.util.PropertiesUtil;
import io.mosip.registrationProcessor.perf.util.TestDataUtility;
import io.mosip.resgistrationProcessor.perf.dbaccess.RegProcPerfDaoImpl;

public class PacketDemoDataUtil {

	private static Logger logger = Logger.getLogger(PacketDemoDataUtil.class);

	/*
	 * String GOOGLE_API_KEY = "AIzaSyCm--C8RpN6FvNQtHtPKdtM20_k0R0284M"; String
	 * GOOGLE_LANG_CODE_ENG = "en"; String GOOGLE_LANG_CODE_FR = "fr"; String
	 * GOOGLE_LANG_CODE_AR = "ar"; String LANG_CODE_FR = "fra"; String LANG_CODE_AR
	 * = "ara"; String LANG_CODE_ENG = "eng";
	 */
	// JSONUtil jsonUtil;

	public PacketDemoDataUtil() {

		RegProcPerfDaoImpl dao = new RegProcPerfDaoImpl();
		TestDataUtility testDataUtility = new TestDataUtility();
		JSONUtil jsonUtil = new JSONUtil();
		TestDataUtility testDataUtil = new TestDataUtility();
	}

	String translateText(String sourceText, String sourceLang, String destLang) {
		// English en
		// French fr
		// Arabic ar
		// Hindi hi
//		String text = null;
//		GoogleTranslate translator = new GoogleTranslate(GOOGLE_API_KEY);
//		text = translator.translate(sourceText, sourceLang, destLang);
//		if (text == null) {
//			System.out.println("null is returned from Google Translator");
//			text = sourceText;
//		}
//		System.out.println("Google translated " + sourceText + " of source language " + sourceLang + " to " + text
//				+ " in " + destLang);
//		return text;

		return sourceText;
	}


	public RegProcIdDto modifyDemographicData1(PropertiesUtil prop, Session session, String idJsonPath) {
		// RegProcPerfDaoImpl dao = new RegProcPerfDaoImpl();
		TestDataUtility testDataUtility = new TestDataUtility();
		JSONUtil jsonUtil = new JSONUtil();
		TestDataUtility testDataUtil = new TestDataUtility();
		// String GOOGLE_API_KEY = "AIzaSyCm--C8RpN6FvNQtHtPKdtM20_k0R0284M";
		String GOOGLE_LANG_CODE_ENG = "en";
		String GOOGLE_LANG_CODE_FR = "fr";
		String GOOGLE_LANG_CODE_AR = "ar";
		String LANG_CODE_FR = "fra";
		String LANG_CODE_AR = "ara";
		String LANG_CODE_ENG = "eng";

		// String idJsonPath = "";
		RegProcIdDto regProcDemodto = jsonUtil.mapJsonFileToObject(idJsonPath);
		Identity identityDto = regProcDemodto.getIdentity();
		
		
		
		FullName fullName=new FullName();
		List<FieldData> firstName = new ArrayList<>();
		FieldData name = new FieldData(LANG_CODE_ENG, testDataUtil.generateRandomName());
		firstName.add(name);
		List<FieldData> lastName = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateRandomName());
		lastName.add(name);
		List<FieldData> middleName = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateRandomName());
		middleName.add(name);
		List<FieldData> suffix = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateRandomName());
		suffix.add(name);
		identityDto.setFullName(fullName);
		//fullName is set
		
		FullName introducerFullName=new FullName();
		firstName = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateRandomName());
		firstName.add(name);
		lastName = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateRandomName());
		lastName.add(name);
		middleName = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateRandomName());
		middleName.add(name);
		suffix = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateRandomName());
		suffix.add(name);
		identityDto.setIntroducerFullName(introducerFullName);
		//introducerfullName is set
		
		
		DocumentDetails documentDetails=new DocumentDetails();
		List<FieldData> documentType1 = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateRandomName());
		documentType1.add(name);
		List<FieldData> documentType2 = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateRandomName());
		documentType2.add(name);
		List<FieldData> documentType3 = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateRandomName());
		documentType3.add(name);
		List<FieldData> documentNumber1 = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateRandomName());
		documentNumber1.add(name);
		List<FieldData> documentNumber2 = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateRandomName());
		documentNumber2.add(name);
		List<FieldData> documentNumber3 = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateRandomName());
		documentNumber3.add(name);
		documentDetails.setDocumentNumber1(documentNumber1);
		documentDetails.setDocumentNumber2(documentNumber2);
		documentDetails.setDocumentNumber3(documentNumber3);
		documentDetails.setDocumentType1(documentType1);
		documentDetails.setDocumentType2(documentType2);
		documentDetails.setDocumentType3(documentType3);
		identityDto.setDocumentDetails(documentDetails);
		//document details is set
		
		
		
		
		Address presentAddress=new Address();
		List<FieldData> addressLine1 = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateAddressLine());
		addressLine1.add(name);
		List<FieldData> addressLine2 = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateAddressLine());
		addressLine2.add(name);
		List<FieldData> addressLine3 = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateAddressLine());
		addressLine3.add(name);
		List<FieldData> addressLine4 = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateAddressLine());
		addressLine4.add(name);
		List<FieldData> country = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, prop.COUNTRY_NAME);
		country.add(name);
		presentAddress.setAddressLine1(addressLine1);
		presentAddress.setAddressLine2(addressLine2);
		presentAddress.setAddressLine3(addressLine3);
		presentAddress.setAddressLine4(addressLine4);
		presentAddress.setCountry(country);
		identityDto.setPresentAddress(presentAddress);
		//present address is set
		
		
		Address permanentAddress=new Address();
		addressLine1 = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateAddressLine());
		addressLine1.add(name);
		addressLine2 = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateAddressLine());
		addressLine2.add(name);
		addressLine3 = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateAddressLine());
		addressLine3.add(name);
		addressLine4 = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, testDataUtil.generateAddressLine());
		addressLine4.add(name);
		country = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, prop.COUNTRY_NAME);
		country.add(name);
		permanentAddress.setAddressLine1(addressLine1);
		permanentAddress.setAddressLine2(addressLine2);
		permanentAddress.setAddressLine3(addressLine3);
		permanentAddress.setAddressLine4(addressLine4);
		permanentAddress.setCountry(country);
		identityDto.setPresentAddress(permanentAddress);
		//permanant address is set
		
		PlaceOfBirth placeOfBirth=new PlaceOfBirth();
		country = new ArrayList<>();
		name = new FieldData(LANG_CODE_ENG, prop.COUNTRY_NAME);
		country.add(name);
		placeOfBirth.setCountry(country);
		identityDto.setPlaceOfBirth(placeOfBirth);
		//place Of Birth is Set
		
		

		DOBDto dobDto = testDataUtil.generateDOB();
		identityDto.setDateOfBirth(dobDto.getDate());
		identityDto.setAge(dobDto.getAge());
		//DOB and Age is set

		List<FieldData> gender = new ArrayList<>();
		String genderEng = testDataUtil.genrateGenderEng();
		FieldData genderEngData = new FieldData(LANG_CODE_FR, genderEng);
		gender.add(genderEngData);
		identityDto.setGender(gender);
		//gender is set
		
		List<FieldData> residenceStatus = new ArrayList<>();
		name = new FieldData(LANG_CODE_FR, prop.RESIDENT_STATUS);
		residenceStatus.add(name);
		identityDto.setResidenceStatus(residenceStatus);
		//residence status is set
		
		List<FieldData> bloodGroup = new ArrayList<>();
		name = new FieldData(LANG_CODE_FR, prop.BLOOD_GROUP);
		bloodGroup.add(name);
		identityDto.setBloodType(bloodGroup);
		//residence status is set
		
		DocumentData proofOfRegistration=new DocumentData();
		proofOfRegistration.setFormat(prop.PROOF_OF_REGISTRATION_TYPE);
		proofOfRegistration.setValue(prop.PROOF_OF_REGISTRATION);
		proofOfRegistration.setType(prop.PROOF_OF_REGISTRATION);
		identityDto.setProofOfRegistration(proofOfRegistration);
		//proof of reg is set

		
		regProcDemodto.setIdentity(identityDto);
		return regProcDemodto;
	}

	public String convertLocationEngToFrench(String locationName, int hierarchy_level, Session session) {

		String LANG_CODE_FR = "fra";
		RegProcPerfDaoImpl dao = new RegProcPerfDaoImpl();

		String result = dao.getTranslatedLocation(locationName, LANG_CODE_FR, hierarchy_level, session);
//		System.out.println(
//				LANG_CODE_FR + " of " + locationName + " at hierarchy level " + hierarchy_level + " is " + result);
		return result;
	}

	public String convertLocationEngToArabic(String locationName, int hierarchy_level, Session session) {

		String LANG_CODE_AR = "ara";
		RegProcPerfDaoImpl dao = new RegProcPerfDaoImpl();
		String result = dao.getTranslatedLocation(locationName, LANG_CODE_AR, hierarchy_level, session);
//		System.out.println(
//				LANG_CODE_AR + " of " + locationName + " at hierarchy level " + hierarchy_level + " is " + result);
		return result;
	}

	public String generateRegId(String centerId, String machineId) {
		String regID = "";
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		timeStamp.replaceAll(".", "");
		int n = 10000 + new Random().nextInt(90000);
		String randomNumber = String.valueOf(n);

		regID = centerId + machineId + randomNumber + timeStamp;
		return regID;
	}

	public static void writeChecksumToFile(String file, String checksum) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(file);
		writer.print(checksum);
		writer.close();
	}

	public synchronized void logRegIdsToFile(String logFilePath, String regId) throws Exception {
		try (FileWriter f = new FileWriter(logFilePath, true);
				BufferedWriter b = new BufferedWriter(f);
				PrintWriter p = new PrintWriter(b);) {

			p.println(regId);
			b.close();
			f.close();
		} catch (IOException i) {
			i.printStackTrace();
			throw new Exception(i);
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public synchronized void logRegIdCheckSumToFile(String checkSumLogFile, String regId, String checksumStr,
			Long sizeInBytes, String center_machine_refID) {

		// String checkSumLogFile = "E:\\MOSIP_PT\\Data\\checksums.txt";
		try (FileWriter f = new FileWriter(checkSumLogFile, true);
				BufferedWriter b = new BufferedWriter(f);
				PrintWriter p = new PrintWriter(b);) {

			p.println(regId + "," + checksumStr + "," + sizeInBytes + "," + center_machine_refID);
			b.close();
			f.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	public void modifyPacketMetaInfo(String packetMetaInfoFile, String regId, String centerId, String machineId,
			String userId) {
		JSONUtil jsonUtil = new JSONUtil();
		JSONObject jsonObject = jsonUtil.loadJsonFromFile(packetMetaInfoFile);
		com.google.gson.internal.LinkedTreeMap identity = null;
		java.util.ArrayList metadata = null;
		java.util.ArrayList osiData = null;
		if (!new File(packetMetaInfoFile).exists()) {
			System.out.println(packetMetaInfoFile + " not found");
			logger.error("File " + packetMetaInfoFile + " doesn't exist");
			System.out.println("Stopping thread " + Thread.currentThread().getName());
			Thread.currentThread().stop();
		} else {
			for (Object key : jsonObject.keySet()) {
				if ("identity".equals((String) key)) {
					Object inObj = jsonObject.get(key);
					identity = (com.google.gson.internal.LinkedTreeMap) inObj;
					for (Object key1 : identity.keySet()) {
						if ("metaData".equals((String) key1)) {
							Object metadataObj = identity.get(key1);
							metadata = (java.util.ArrayList) metadataObj;

						} else if ("osiData".equals((String) key1)) {
							Object osiDataObj = identity.get(key1);
							osiData = (java.util.ArrayList) osiDataObj;
						}
					}

					break;
				}
			}
			int counter = 1;
			// System.out.println(packetMetaInfoFile);
			// System.out.println("line 425 PacketDemoDataUtil " + metadata.toString());
			for (int i = 0; i < metadata.size(); i++) {

				Map keyVal = (Map) metadata.get(i);
				if ("registrationId".equals(keyVal.get("label"))) {
					keyVal.put("value", regId);
					counter++;
				} else if ("machineId".equals(keyVal.get("label"))) {
					keyVal.put("value", machineId);
					counter++;
				} else if ("centerId".equals(keyVal.get("label"))) {
					keyVal.put("value", centerId);
					counter++;
				} else if ("creationDate".equals(keyVal.get("label"))) {
					keyVal.put("value", getCurrDate());
					counter++;
				} else if (counter == 5) {
					break;
				}

			}

			for (int i = 0; i < osiData.size(); i++) {

				Map keyVal = (Map) osiData.get(i);
				if ("officerId".equals(keyVal.get("label"))) {
					keyVal.put("value", userId);
					break;
				}

			}

			identity.put("metaData", metadata);
			identity.put("osiData", osiData);
			jsonObject.put("identity", identity);
			// System.out.println(identity);
			jsonUtil.writeJSONToFile(packetMetaInfoFile, jsonObject);
			// System.out.println(gson.toJson(jsonObject));

		}

	}

	String getCurrDate() {
		String timestamp = "";
		String timestampFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
		Date currDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currDate);
		// cal.add(Calendar.HOUR, -6);
		Date date = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(timestampFormat);
		timestamp = sdf.format(date);
		// System.out.println(timestamp);
		timestamp = timestamp + "Z";
		return timestamp;
	}

}
