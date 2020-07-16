package mosip.perf.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class RegProcsessorUtil {

	public String generateRegId() {
		String centerId = "10002";
		String machineId = "10032";
		return generateRegId(centerId, machineId);
	}

	private String generateRegId(String centerId, String machineId) {
		String regID = "";
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		timeStamp.replaceAll(".", "");
		int n = 10000 + new Random().nextInt(90000);
		String randomNumber = String.valueOf(n);

		regID = centerId + machineId + randomNumber + timeStamp;
		return regID;
	}

	public LocalDateTime getCurrLocalDateTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		Date currDate = new Date();
		formatter.format(currDate);
		Instant instant = currDate.toInstant();
		LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
		// System.out.println("localDateTime : " + localDateTime);
		return localDateTime;
	}

	public String getRandomUuid() {
		return UUID.randomUUID().toString();
	}

	public String getPacketHashValue() {
		String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder builder = new StringBuilder();
		int count = 50;
		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}

	public DOBDto generateDOB() {
		// YYYY/mm/DD
		DOBDto dobDto = new DOBDto();
		DecimalFormat formatter = new DecimalFormat("00");
		int YYYY = generateYear();
		String MM = formatter.format(generateMonth());
		String dd = formatter.format(generateDate());

		String date = YYYY + "/" + MM + "/" + dd;
		int age = 2019 - YYYY;
		dobDto.setDate(date);
		dobDto.setAge(age);
		return dobDto;
	}

	private int generateYear() {
		int minYear = 1975;
		int yearLimit = 35;
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(yearLimit) + 1;
		int year = minYear + randomInt;
		return year;
	}

	private int generateMonth() {
		int monthLimit = 11;
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(monthLimit) + 1;
		int month = 1 + randomInt;
		return month;

	}

	private int generateDate() {
		int dateLimit = 26;
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(dateLimit) + 1;
		int date = 1 + randomInt;
		return date;
	}

	public String generateFullName() {
		String firstName = generateRandomName();
		String lastName = generateRandomName();
		String fullName = firstName + " " + lastName;
		return fullName;
	}

	private String generateRandomName() {
		String name = "";
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(26);
		int capitalAscii = 65 + randomInt;
		name += (char) capitalAscii;
		randomInt = randomGenerator.nextInt(3);
		int nameLength = 3 + randomInt;
		for (int i = 0; i < nameLength; i++) {
			randomInt = randomGenerator.nextInt(26);
			int randomAscii = 97 + randomInt;
			name += (char) randomAscii;
		}
		return name;
	}

	public String genrateGenderEng() {
		String[] genderEngArr = new String[] { "Male", "Female" };
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(2);
		return genderEngArr[randomInt];
	}

	public String getRandomAppCode() {
		String[] appcodes = new String[] { "ABIS1", "ABIS2", "ABIS3", "ABIS4" };
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(2);
		return appcodes[randomInt] + Thread.currentThread().getName();
	}

	public String getRandomRequestType() {
		String[] appcodes = new String[] { "IDENTIFY", "INSERT" };
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(2);
		return appcodes[randomInt];
	}

}
