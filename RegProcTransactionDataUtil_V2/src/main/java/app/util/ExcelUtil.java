package app.util;

import java.io.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;

import app.dbaccess.DBAccessImpl;

public class ExcelUtil {

	private static String excelFile = PropertiesUtil.EXCEL_FILE;
	DBAccessImpl dbAccess;

	public ExcelUtil() {

	}

	public void processDataToWriteToExcel(Session session, Map<String, Map<String, Object>> transactionTimesByRegIds,
			List<String> transactions) throws Exception {

		dbAccess = new DBAccessImpl();
		Object[][] records = new Object[transactionTimesByRegIds.size() + 1][transactions.size() + 1];
		// transactionTimesByRegIds.entrySet();
		int row = 0, col = 0;
		// records[row][col] = new String();
		records[row][col] = "Activities";
		Set<String> regIds = transactionTimesByRegIds.keySet();

		/*
		 * Write regIds in first row skipping the first cell
		 */
		// addRegIdsToTable(records, regIds);

		/*
		 * Write activity name sin first column skipping the first cell
		 */
		addTransactionsToTable(records, transactions);

		/*
		 * Write the main content i.e. time taken in each stage registration ID wise to
		 * the table
		 */
		// addTransactionTimesToTable1(session, records, transactionTimesByRegIds,
		// transactions);
		addTransactionTimesToTable2(session, records, transactionTimesByRegIds, transactions);
		// addTransactionTimesToTable(records, transactionTimesByRegIds, transactions);

		// writeFirstRowToExcel(records);

//		for (Object[] rows : records) {
//			for (Object cell : rows) {
//				System.out.print(cell + " ");
//			}
//			System.out.println();
//		}

		writeRecordsToExcel(records);

	}

	private void addTransactionTimesToTable2(Session session, Object[][] records,
			Map<String, Map<String, Object>> transactionTimesByRegIds, List<String> transactions) throws Exception {

		int col = 0;
		int row = 1;
		for (Entry<String, Map<String, Object>> entry : transactionTimesByRegIds.entrySet()) {

			String regid = entry.getKey();

			/*
			 * registration ID will be stored in the cells along first column; i.e column
			 * index is fixed
			 */
			records[row][0] = regid;
			int t_col = transactions.size();
			int iterations = transactions.size();
			for (int i = 1; i <= iterations; i++) {

				Map<String, Object> transactionTimeMap = entry.getValue();
				records[row][i] = transactionTimeMap.get(records[0][i]);
			}

			Long totalTime = dbAccess.getAggregateTimeTakenForRegistrationId(session, regid);
			records[row][t_col] = totalTime;
			row++;
		}

	}

	private void addTransactionTimesToTable1(Session session, Object[][] records,
			Map<String, Map<String, Long>> transactionTimesByRegIds, List<String> transactions) throws Exception {
		int col = 1;
		int row = 1;
		for (Entry<String, Map<String, Long>> entry : transactionTimesByRegIds.entrySet()) {

			// entry.getKey();

			String regid = entry.getKey();

			records[0][col] = regid; // Storing reg id in 0th row
			// row++;
//			entry.getValue().forEach((transactionName, time) -> {
//				
//				
//				
//			} );

			// int iterations = entry.getValue().size();
			int iterations = transactions.size();
			int t_row = transactions.size();

			for (int i = 1; i <= iterations; i++) {
				Map<String, Long> transactionTimeMap = entry.getValue();
				// Long time = transactionTimeMap.get(records[row][0]);
				// System.out.println(time);
				// System.out.println("i " + i + ", row: " + row + ", col: " + col);
				records[i][col] = transactionTimeMap.get(records[i][0]);
				// System.out.println(records[i][0] + ": " + records[i][col]);
			}

			Long totalTime = dbAccess.getAggregateTimeTakenForRegistrationId(session, regid);
			records[t_row][col] = totalTime;
			col++;

		}

	}

	private void writeRecordsToExcel(Object[][] records) {

		// Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet sheet = workbook.createSheet("Activities Time");
		int rowNum = 0;
		for (Object[] rowData : records) {
			Row row = sheet.createRow(rowNum++);
			int colNum = 0;
			for (Object field : rowData) {
				Cell cell = row.createCell(colNum++);
				if (field instanceof String) {
					cell.setCellValue((String) field);
				} else if (field instanceof Long) {
					cell.setCellValue((Long) field);
				}
			}
		}

		try {
			FileOutputStream outputStream = new FileOutputStream(excelFile);
			workbook.write(outputStream);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void writeFirstRowToExcel(Object[][] records) {
		// Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet sheet = workbook.createSheet("Activities Time");
		int rowNum = 0;
		for (Object[] rowData : records) {
			Row row = sheet.createRow(rowNum++);
			int colNum = 0;
			for (Object field : rowData) {
				Cell cell = row.createCell(colNum++);
				if (field instanceof String) {
					cell.setCellValue((String) field);
				} else if (field instanceof Long) {
					cell.setCellValue((Long) field);
				}
			}
		}

		try {
			FileOutputStream outputStream = new FileOutputStream(excelFile);
			workbook.write(outputStream);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addTransactionTimesToTable(Object[][] records, Map<String, Map<String, Long>> transactionTimesByRegIds,
			List<String> activities) {
		int row = 1, col = 1;
		for (Entry<String, Map<String, Long>> entry : transactionTimesByRegIds.entrySet()) {
			// String time = entry.getKey();
			Map<String, Long> activitiesMap = entry.getValue();
			// activitiesMap.entrySet();
			row = 1;
			for (Entry<String, Long> activity : activitiesMap.entrySet()) {

				String activityInTable = (String) records[row][0];
				String activityName = activity.getKey();
				if (activityInTable.contains(activityName)) {
					Long activityTime = activity.getValue();
					System.out.println("row: " + row + ", col : " + col);
					records[row][col] = activityTime;

				}
				row++;

			}
			col++;
		}
	}

	private void addTransactionsToTable(Object[][] records, List<String> activities) {

		final int row = 0;
		int col = 1;

		for (String activity : activities) {
			records[row][col++] = activity;
		}

		System.out.println(records.toString());

	}

	private void addRegIdsToTable(Object[][] records, Set<String> times) {

		int row = 0;
		int col = 1;

		for (String time : times) {
			records[row][col] = time;
			col++;
		}
	}

	private void writeFirstColumnToExcel(Set<String> activities) {

	}

}
