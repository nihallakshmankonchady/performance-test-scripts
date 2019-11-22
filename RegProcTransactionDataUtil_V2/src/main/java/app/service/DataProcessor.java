package app.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.Map.Entry;

import org.hibernate.Session;

import app.dbaccess.DBAccessImpl;
import app.entity.TransactionEntity;
import app.util.CSVUtil;
import app.util.ExcelUtil;
import app.util.PropertiesUtil;

public class DataProcessor {

	DBAccessImpl dbAccess;

	private List<String> activities;

	private final String TOTAL_TIME = " TOTAL_TIME (ms)";
	private final String TOTAL_TIME_SEC = "TOTAL_TIME (Sec)";

	public void processData(Session session) {

		List<TransactionEntity> trList;

		dbAccess = new DBAccessImpl();

		List<String> regidList = null;

		String csvFile = PropertiesUtil.REGID_LOG_FILE;

		regidList = CSVUtil.loadRegIds(csvFile);

		// Map<String, Map<String, Long>> regIdsMap = new HashMap<>();

		// activities = new ArrayList<>();

		String[] activityArray = { "PACKET_RECEIVER", "UPLOAD_PACKET", "VALIDATE_PACKET", "QUALITY_CHECK",
				"OSI_VALIDATE", "EXTERNAL_INTEGRATION", "DEMOGRAPHIC_VERIFICATION", "BIOGRAPHIC_VERIFICATION",
				"UIN_GENERATOR", "PRINT_SERVICE", "PRINT_POSTAL_SERVICE", "NOTIFICATION", "PACKET_REPROCESS",
				TOTAL_TIME, TOTAL_TIME_SEC, "TOTAL_TIME (from registration table)" };
		activities = Arrays.asList(activityArray);

//		int c = 0;
//		for (String regid : regidList) {
//			trList = dbAccess.getRegTransactionForRegId(regid);
//			Map<String, Long> diffMap = processForRegId(trList);
//			regIdsMap.put(regid, diffMap);
//			if (c == 0) {
//				activities = diffMap.keySet();
//			} else {
//				Set<String> activities1 = diffMap.keySet();
//				if (activities1.size() != activities.size()) {
//					System.err.println("Number of activities not matching for regid:- " + regid);
//				}
//			}
//
//		}
//
//		computeAverageDifference(regIdsMap, activities);

		// regidList = filterOutErroneousRegIds(session, regidList);

		obtainActivitiesMappedByRegistrationIds(session, regidList);

	}

	private List<String> filterOutErroneousRegIds(Session session, List<String> regidList) {

		List<String> regIds = new ArrayList<>();
		// List<TransactionEntity> trList = dbAccess.getRegTransactionForRegId(session,
		// regidList);
		for (String regid : regidList) {
			List<TransactionEntity> trEntityList = dbAccess.getRegTransactionForRegId(session, regid);
			if (checkTransactionsForErrors(trEntityList)) {
				regIds.add(regid);
			}
		}

		return regIds;
	}

	private boolean checkTransactionsForErrors(List<TransactionEntity> trEntityList) {
		boolean result = false;
		for (TransactionEntity trEntity : trEntityList) {
			String statusComment = trEntity.getStatusComment();
			if (statusComment.contains("FAIL") || statusComment.contains("fail") || statusComment.contains("error")
					|| statusComment.contains("Error") || statusComment.contains("error")
					|| statusComment.contains("Error")) {
				result = true;
				break;
			}
		}
		return result;
	}

	private boolean checkTransactionsForReprocess(List<TransactionEntity> trEntityList) {
		boolean result = true;
		for (TransactionEntity trEntity : trEntityList) {
			String statusComment = trEntity.getStatusCode();
			if (statusComment.contains("REPROCESS") || statusComment.contains("reprocess")) {
				result = false;
				break;
			}
		}
		return result;
	}

	private void obtainActivitiesMappedByRegistrationIds(Session session, List<String> regidList) {
		Map<String, Map<String, Object>> transactionTimesByRegIds = new HashMap<>();
		int c = 0;
		List<TransactionEntity> trList;
		// List<TransactionEntity> trList = dbAccess.getRegTransactionForRegId(session,
		// regidList);
		for (String regid : regidList) {
			trList = dbAccess.getRegTransactionForRegId(session, regid);

//			boolean trHasError = checkTransactionsForErrors(trList);
//			if (trHasError)
//				continue;

//			boolean noReprocess = checkTransactionsForReprocess(trList);
//			if (!noReprocess) {
//				continue;
//			}

			/*
			 * trTimeMap stores the time taken in each stage, key of the map contains the
			 * transaction name (or name of the stage)
			 */
			Map<String, Object> trTimeMap = obtainTimeSpentTransactionWise(trList);

			/*
			 * Fetches the least value of cr_dtimes
			 */
			// String leastCreationTime = obtainLeastCreationTime(trList);

			Set<String> activities1 = trTimeMap.keySet();
			if (activities1.size() <= activities.size()) {
				transactionTimesByRegIds.put(regid, trTimeMap);
			} else {
				System.err.println("Number of activities not matching for regid:- " + regid);
			}

//			
//			if (c == 0) {
//
////				Set<String> activitiesSet = trTimeMap.keySet();
////				for (String activity : activitiesSet) {
////					activities.add(activity);
////				}
//				// activities = trTimeMap.keySet();
//			} else {
////				Set<String> activities1 = trTimeMap.keySet();
////				if (activities1.size() <= activities.size()) {
////					transactionTimesByRegIds.put(regid, trTimeMap);
////				} else {
////					System.err.println("Number of activities not matching for regid:- " + regid);
////				}
////				if (activities1.size() != activities.size()) {
////					System.err.println("Number of activities not matching for regid:- " + regid);
////				}
//
//			}
//			c++;
			// regIdsMappedByStartTime.put(leastCreationTime, trTimeMap);

		}

		try {

			new ExcelUtil().processDataToWriteToExcel(session, transactionTimesByRegIds, activities);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private String obtainLeastCreationTime(List<TransactionEntity> trList) {
		Collections.sort(trList);
		LocalDateTime localDateTime = trList.get(0).getCreateDateTime();
		return localDateTime.toString();
	}

	private void computeAverageDifference(Map<String, Map<String, Object>> regIdsMap, Set<String> activities) {
		Set<Entry<String, Map<String, Object>>> entrySet = regIdsMap.entrySet();

		Map<String, Double> avgMap = new HashMap<>();
		Map<String, Object> sumsMap = new HashMap<>();

		int countRecords = regIdsMap.size();

		for (Entry<String, Map<String, Object>> entry : entrySet) {
			String regId = entry.getKey();
			Map<String, Object> activityMap = entry.getValue();

//			Set<Entry<String, Long>> activitySet = activityMap.entrySet();
//			Iterator<Entry<String, Long>> iterator = activitySet.iterator();
			for (String activity : activities) {

				if (null == sumsMap.get(activity)) {
					sumsMap.put(activity, activityMap.get(activity));
				} else {
					sumsMap.put(activity, (Long) sumsMap.get(activity) + (Long) activityMap.get(activity));
				}

			}
		}
		sumsMap.entrySet();
		for (Entry<String, Object> entry : sumsMap.entrySet()) {

			avgMap.put(entry.getKey(), ((Long) entry.getValue() / new Double(countRecords)));

		}

		for (Entry<String, Double> entry : avgMap.entrySet()) {

			System.out.println(entry.getKey() + "\t" + entry.getValue());
		}

	}

	private Map<String, Object> obtainTimeSpentTransactionWise(List<TransactionEntity> trList) {

		Collections.sort(trList); // transaction list will be sorted by order of creation time

		Map<String, Object> timeSpentTrMap = new HashMap<>();

		for (int i = 0; i < trList.size() - 1; i++) {
			TransactionEntity te1 = trList.get(i);
			TransactionEntity te2 = trList.get(i + 1);

			LocalDateTime date1 = te1.getCreateDateTime();
			// System.out.println("date1 : " + date1.toString());
			LocalDateTime date2 = te2.getCreateDateTime();
			// System.out.println("date2 : " + date2.toString());
			// UTC
			// ZonedDateTime zdt1 = date1.atZone(ZoneId.of("UTC"));
			// ZonedDateTime zdt2 = date2.atZone(ZoneId.of("UTC"));

			long localDTInMilli1 = date1.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
			long localDTInMilli2 = date2.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();

			long difference = localDTInMilli2 - localDTInMilli1;
			// System.out.println("difference : " + difference);
			String key = te2.getTrntypecode();
			Long existingDiff = 0L;
			if (null != timeSpentTrMap.get(key)) {
				// key += "1";
				existingDiff = (Long) timeSpentTrMap.get(key);
			}
			timeSpentTrMap.put(key, existingDiff + difference);
		}

		Long totalTimeSpentMillis = computeTotalMilliSeconds(timeSpentTrMap);

		Double totalTimeSpentSec = computeTotalSeconds(timeSpentTrMap);

		timeSpentTrMap.put(TOTAL_TIME, totalTimeSpentMillis);
		timeSpentTrMap.put(TOTAL_TIME_SEC, totalTimeSpentSec);

		return timeSpentTrMap;

	}

	private Double computeTotalSeconds(Map<String, Object> timeSpentTrMap) {
		Double result = 0.0;
		Long sum = 0L;
		for (Map.Entry<String, Object> entry : timeSpentTrMap.entrySet()) {

			sum += (Long) entry.getValue();

		}
		result = (double) (sum / 1000);
		return result;
	}

	private Long computeTotalMilliSeconds(Map<String, Object> timeSpentTrMap) {

		Long result = 0L;

		for (Map.Entry<String, Object> entry : timeSpentTrMap.entrySet()) {

			result += (Long) entry.getValue();

		}
		return result;
	}

}
