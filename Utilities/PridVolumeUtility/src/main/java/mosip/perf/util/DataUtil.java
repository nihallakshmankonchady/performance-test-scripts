package mosip.perf.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

public class DataUtil {

	public DataUtil() {

	}

	protected String getVidStatus() {
		String[] statuses = { "AVAILABLE", "ASSIGNED" };
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(statuses.length);
		return statuses[randomInt];
	}

	protected LocalDateTime getCurrLocalDateTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		Date currDate = new Date();
		formatter.format(currDate);
		Instant instant = currDate.toInstant();
		LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
		// System.out.println("localDateTime : " + localDateTime);
		return localDateTime;
	}

	protected String generateRandomVID() {
		String vid = "";

		Random random = new Random();
		int r = random.nextInt(9) + 1;
		vid += r;
		for (int i = 0; i <= 14; i++) {
			vid += random.nextInt(9);
		}

		return vid;

	}

	protected String generateRandomPrid() {
		String vid = "";

		Random random = new Random();
		int r = random.nextInt(9) + 1;
		vid += r;
		for (int i = 1; i <= 9; i++) {
			vid += random.nextInt(9);
		}

		return vid;

	}

}
