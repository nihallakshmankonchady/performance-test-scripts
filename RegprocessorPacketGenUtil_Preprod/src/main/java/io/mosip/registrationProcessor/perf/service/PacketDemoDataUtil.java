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

import io.mosip.registrationProcessor.perf.dto.DOBDto;
import io.mosip.registrationProcessor.perf.dto.RegDataCSVDto;
import io.mosip.registrationProcessor.perf.entity.Location;
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

	public RegProcIdDto modifyDemographicdata(RegDataCSVDto regData, Session session) {
		RegProcPerfDaoImpl dao = new RegProcPerfDaoImpl();
		TestDataUtility testDataUtility = new TestDataUtility();

		TestDataUtility testDataUtil = new TestDataUtility();

		String GOOGLE_LANG_CODE_ENG = "en";
		String GOOGLE_LANG_CODE_FR = "fr";
		String GOOGLE_LANG_CODE_AR = "ar";
		String LANG_CODE_FR = "fra";
		String LANG_CODE_AR = "ara";

		JSONUtil jsonUtil = new JSONUtil();
		RegProcIdDto regProcDemodto = jsonUtil.mapJsonFileToObject();
		Identity identityDto = regProcDemodto.getIdentity();
		List<FieldData> fullName = new ArrayList<>();
		String fullNameEng = regData.getFullName();
		String fullNameFr = translateText(fullNameEng, GOOGLE_LANG_CODE_ENG, GOOGLE_LANG_CODE_FR);
		String fullNameAr = translateText(fullNameEng, GOOGLE_LANG_CODE_ENG, GOOGLE_LANG_CODE_AR);
		FieldData fullNameFrData = new FieldData(LANG_CODE_FR, fullNameFr);
		FieldData fullNameArData = new FieldData(LANG_CODE_AR, fullNameAr);
		fullName.add(fullNameFrData);
		fullName.add(fullNameArData);
		identityDto.setFullName(fullName);

		identityDto.setDateOfBirth(regData.getDateOfBirth());
		identityDto.setAge(Integer.parseInt(regData.getAge()));

		List<FieldData> gender = new ArrayList<>();
		String genderFr = testDataUtility.frenchTexts.get(regData.getGender());
		String genderAr = testDataUtility.arabicTexts.get(regData.getGender());
		FieldData genderFrData = new FieldData(LANG_CODE_FR, genderFr);
		FieldData genderArData = new FieldData(LANG_CODE_AR, genderAr);
		gender.add(genderFrData);
		gender.add(genderArData);
		identityDto.setGender(gender);

		List<FieldData> residenceStatus = new ArrayList<>();
		String resStatusFr = testDataUtility.frenchTexts.get(regData.getResidenceStatus());
		String resStatusAr = testDataUtility.arabicTexts.get(regData.getResidenceStatus());
		FieldData resStatusFrData = new FieldData(LANG_CODE_FR, resStatusFr);
		FieldData resStatusArData = new FieldData(LANG_CODE_AR, resStatusAr);
		residenceStatus.add(resStatusFrData);
		residenceStatus.add(resStatusArData);
		identityDto.setResidenceStatus(residenceStatus);

		List<FieldData> addressLine1 = new ArrayList<>();
		String addressLine1_eng = regData.getAddressLine1();
		String addressLine1_fr = translateText(addressLine1_eng, GOOGLE_LANG_CODE_ENG, GOOGLE_LANG_CODE_FR);
		String addressLine1_ar = translateText(addressLine1_eng, GOOGLE_LANG_CODE_ENG, GOOGLE_LANG_CODE_AR);
		FieldData addressLine1_frData = new FieldData(LANG_CODE_FR, addressLine1_fr);
		FieldData addressLine1_arData = new FieldData(LANG_CODE_AR, addressLine1_ar);
		addressLine1.add(addressLine1_frData);
		addressLine1.add(addressLine1_arData);
		identityDto.setAddressLine1(addressLine1);

		List<FieldData> addressLine2 = new ArrayList<>();
		String addressLine2_eng = regData.getAddressLine2();
		String addressLine2_fr = translateText(addressLine2_eng, GOOGLE_LANG_CODE_ENG, GOOGLE_LANG_CODE_FR);
		String addressLine2_ar = translateText(addressLine2_eng, GOOGLE_LANG_CODE_ENG, GOOGLE_LANG_CODE_AR);
		FieldData addressLine2_frData = new FieldData(LANG_CODE_FR, addressLine2_fr);
		FieldData addressLine2_arData = new FieldData(LANG_CODE_AR, addressLine2_ar);
		addressLine2.add(addressLine2_frData);
		addressLine2.add(addressLine2_arData);
		identityDto.setAddressLine2(addressLine2);

		List<FieldData> addressLine3 = new ArrayList<>();
		String addressLine3_eng = regData.getAddressLine3();
		String addressLine3_fr = translateText(addressLine3_eng, GOOGLE_LANG_CODE_ENG, GOOGLE_LANG_CODE_FR);
		String addressLine3_ar = translateText(addressLine3_eng, GOOGLE_LANG_CODE_ENG, GOOGLE_LANG_CODE_AR);
		FieldData addressLine3_frData = new FieldData(LANG_CODE_FR, addressLine3_fr);
		FieldData addressLine3_arData = new FieldData(LANG_CODE_AR, addressLine3_ar);
		addressLine3.add(addressLine3_frData);
		addressLine3.add(addressLine3_arData);
		identityDto.setAddressLine3(addressLine3);
		Gson gson = new Gson();
		List<FieldData> region = new ArrayList<>();
		String regionValFr = convertLocationEngToFrench(regData.getRegion(), 1, session);

		String regionValAr = convertLocationEngToArabic(regData.getRegion(), 1, session);
		FieldData regionFr = new FieldData(LANG_CODE_FR, regionValFr);
		FieldData regionAr = new FieldData(LANG_CODE_AR, regionValAr);
		region.add(regionFr);
		region.add(regionAr);
		identityDto.setRegion(region);

		List<FieldData> province = new ArrayList<>();
		String provinceValFr = convertLocationEngToFrench(regData.getProvince(), 2, session);
		String provinceValAr = convertLocationEngToArabic(regData.getProvince(), 2, session);
		FieldData provinceFr = new FieldData(LANG_CODE_FR, provinceValFr);
		FieldData provinceAr = new FieldData(LANG_CODE_AR, provinceValAr);

		province.add(provinceFr);
		province.add(provinceAr);
		identityDto.setProvince(province);

		List<FieldData> city = new ArrayList<>();
		String cityValFr = convertLocationEngToFrench(regData.getCity(), 3, session);
		String cityValAr = convertLocationEngToArabic(regData.getCity(), 3, session);
		FieldData cityFr = new FieldData(LANG_CODE_FR, cityValFr);
		FieldData cityAr = new FieldData(LANG_CODE_AR, cityValAr);
		city.add(cityFr);
		city.add(cityAr);
		identityDto.setCity(city);

		List<FieldData> zone = new ArrayList<>();
		String zoneValFr = convertLocationEngToFrench(regData.getZone(), 4, session);
		String zoneValAr = convertLocationEngToArabic(regData.getZone(), 4, session);
		FieldData localAdministrativeAuthorityFr = new FieldData(LANG_CODE_FR, zoneValFr);
		FieldData localAdministrativeAuthorityAr = new FieldData(LANG_CODE_AR, zoneValAr);
		zone.add(localAdministrativeAuthorityFr);
		zone.add(localAdministrativeAuthorityAr);
		identityDto.setZone(zone);

		identityDto.setPostalCode(regData.getPostalCode());
		identityDto.setPhone(regData.getPhone());
		identityDto.setEmail(regData.getEmail());

		identityDto.setCNIENumber(regData.getCnieNumber());

		regProcDemodto.setIdentity(identityDto);
		System.out.println(regProcDemodto);
		return regProcDemodto;
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
		List<FieldData> fullName = new ArrayList<>();
		String fullNameEng = testDataUtil.generateFullName();
		String fullNameFr = translateText(fullNameEng, GOOGLE_LANG_CODE_ENG, GOOGLE_LANG_CODE_FR);
		String fullNameAr = translateText(fullNameEng, GOOGLE_LANG_CODE_ENG, GOOGLE_LANG_CODE_AR);
		FieldData fullNameFrData = new FieldData(LANG_CODE_FR, fullNameFr);
		FieldData fullNameArData = new FieldData(LANG_CODE_AR, fullNameAr);
		fullName.add(fullNameFrData);
		fullName.add(fullNameArData);
		identityDto.setFullName(fullName);

		DOBDto dobDto = testDataUtil.generateDOB();
		identityDto.setDateOfBirth(dobDto.getDate());
		identityDto.setAge(dobDto.getAge());

		List<FieldData> gender = new ArrayList<>();
		String genderEng = testDataUtil.genrateGenderEng();
		String genderFr = testDataUtility.frenchTexts.get(genderEng);
		String genderAr = testDataUtility.arabicTexts.get(genderEng);
		FieldData genderFrData = new FieldData(LANG_CODE_FR, genderFr);
		FieldData genderArData = new FieldData(LANG_CODE_AR, genderAr);
		gender.add(genderFrData);
		gender.add(genderArData);
		identityDto.setGender(gender);
		Gson gson = new Gson();
		List<FieldData> residenceStatus = new ArrayList<>();
		String resStatusEng = testDataUtility.generateResidenceStatusEng();
		String resStatusFr = testDataUtility.frenchTexts.get(resStatusEng);
		String resStatusAr = testDataUtility.arabicTexts.get(resStatusEng);
		FieldData resStatusFrData = new FieldData(LANG_CODE_FR, resStatusFr);
		FieldData resStatusArData = new FieldData(LANG_CODE_AR, resStatusAr);
		residenceStatus.add(resStatusFrData);
		residenceStatus.add(resStatusArData);
		identityDto.setResidenceStatus(residenceStatus);

		List<FieldData> addressLine1 = new ArrayList<>();
		String addressLine1_eng = testDataUtility.generateAddressLine();
		String addressLine1_fr = translateText(addressLine1_eng, GOOGLE_LANG_CODE_ENG, GOOGLE_LANG_CODE_FR);
		String addressLine1_ar = translateText(addressLine1_eng, GOOGLE_LANG_CODE_ENG, GOOGLE_LANG_CODE_AR);
		FieldData addressLine1_frData = new FieldData(LANG_CODE_FR, addressLine1_fr);
		FieldData addressLine1_arData = new FieldData(LANG_CODE_AR, addressLine1_ar);
		addressLine1.add(addressLine1_frData);
		addressLine1.add(addressLine1_arData);
		identityDto.setAddressLine1(addressLine1);

		List<FieldData> addressLine2 = new ArrayList<>();
		String addressLine2_eng = testDataUtility.generateAddressLine();
		String addressLine2_fr = translateText(addressLine2_eng, GOOGLE_LANG_CODE_ENG, GOOGLE_LANG_CODE_FR);
		String addressLine2_ar = translateText(addressLine2_eng, GOOGLE_LANG_CODE_ENG, GOOGLE_LANG_CODE_AR);
		FieldData addressLine2_frData = new FieldData(LANG_CODE_FR, addressLine2_fr);
		FieldData addressLine2_arData = new FieldData(LANG_CODE_AR, addressLine2_ar);
		addressLine2.add(addressLine2_frData);
		addressLine2.add(addressLine2_arData);
		identityDto.setAddressLine2(addressLine2);

		List<FieldData> addressLine3 = new ArrayList<>();
		String addressLine3_eng = testDataUtility.generateAddressLine();
		String addressLine3_fr = translateText(addressLine3_eng, GOOGLE_LANG_CODE_ENG, GOOGLE_LANG_CODE_FR);
		String addressLine3_ar = translateText(addressLine3_eng, GOOGLE_LANG_CODE_ENG, GOOGLE_LANG_CODE_AR);
		FieldData addressLine3_frData = new FieldData(LANG_CODE_FR, addressLine3_fr);
		FieldData addressLine3_arData = new FieldData(LANG_CODE_AR, addressLine3_ar);
		addressLine3.add(addressLine3_frData);
		addressLine3.add(addressLine3_arData);
		identityDto.setAddressLine3(addressLine3);

		List<FieldData> region = new ArrayList<>();
		String countryCode = testDataUtil.getCountryCode(session);
		Location regionObj = testDataUtil.getLocation(countryCode, 1, session);
		String regionName = regionObj.getName();
		String regionValFr = convertLocationEngToFrench(regionName, 1, session);

		String regionValAr = convertLocationEngToArabic(regionName, 1, session);

		FieldData regionFr = new FieldData(LANG_CODE_FR, regionValFr);
		FieldData regionAr = new FieldData(LANG_CODE_AR, regionValAr);
		region.add(regionFr);
		region.add(regionAr);
		identityDto.setRegion(region);

//		System.out.println("Region is");
//		System.out.println(gson.toJson(region));

		List<FieldData> province = new ArrayList<>();
		Location provinceObj = testDataUtil.getLocation(regionObj.getCode(), 2, session);
		String provinceName = provinceObj.getName();
		String provinceValFr = convertLocationEngToFrench(provinceName, 2, session);
		String provinceValAr = convertLocationEngToArabic(provinceName, 2, session);
		FieldData provinceFr = new FieldData(LANG_CODE_FR, provinceValFr);

		FieldData provinceAr = new FieldData(LANG_CODE_AR, provinceValAr);
		province.add(provinceFr);
		province.add(provinceAr);
		identityDto.setProvince(province);

		List<FieldData> city = new ArrayList<>();
		Location cityObj = testDataUtil.getLocation(provinceObj.getCode(), 3, session);
		String cityName = cityObj.getName();
		String cityValFr = convertLocationEngToFrench(cityName, 3, session);
		String cityValAr = convertLocationEngToArabic(cityName, 3, session);
		FieldData cityFr = new FieldData(LANG_CODE_FR, cityValFr);
		FieldData cityAr = new FieldData(LANG_CODE_AR, cityValAr);
		city.add(cityFr);
		city.add(cityAr);
		identityDto.setCity(city);
		identityDto.setReferenceIdentityNumber(testDataUtil.getReferenceIdentityNumber());

		List<FieldData> zone = new ArrayList<>();
		Location zoneObj = testDataUtil.getLocation(cityObj.getCode(), 4, session);
		String localAdministrativeAuthName = zoneObj.getName();
		String zoneValFr = convertLocationEngToFrench(localAdministrativeAuthName, 4, session);
		String zoneValAr = convertLocationEngToArabic(localAdministrativeAuthName, 4, session);
		FieldData zoneFr = new FieldData(LANG_CODE_FR, zoneValFr);
		FieldData zoneAr = new FieldData(LANG_CODE_AR, zoneValAr);
		zone.add(zoneFr);
		zone.add(zoneAr);
		identityDto.setZone(zone);

//		System.out.println("localAdministrativeAuthority is");
//		System.out.println(gson.toJson(localAdministrativeAuthority));

		Location postalCode = testDataUtil.getLocation(zoneObj.getCode(), 5, session);
		String postalCodeStr = postalCode.getName();
		identityDto.setPostalCode(postalCodeStr);
		identityDto.setPhone(testDataUtil.generatePhoneNumber().toString());
		String[] names = fullNameEng.split(" ");
		identityDto.setEmail(testDataUtil.generateEmailAddress(names[0], names[1]));

		identityDto.setCNIENumber(testDataUtil.generateCnieNumber());

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
