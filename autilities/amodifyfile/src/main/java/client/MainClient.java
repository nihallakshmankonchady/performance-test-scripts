package client;

import java.io.File;
import java.util.List;

import fileutil.FileUtil;

public class MainClient {

	public static void main(String[] args) {

		// removeDuplicateTextsFromFile();
		// createNewFileWithoutText();
		countUniqueTexts();

	}

	private static void countUniqueTexts() {
		String filepath = "C:\\MOSIP_PT\\Scripts\\IdRepo\\Data";
		String filename = "uin_list_preprod_02Mar.txt";
		FileUtil fileUtil = new FileUtil();
		File file = new File(filepath + File.separator + filename);
		fileUtil.countUniqueTexts(file);
	}

	private static void removeDuplicateTextsFromFile() {

		String filepath = "C:\\MOSIP_PT\\Scripts\\IdRepo\\Data";
		String filename = "uin_list_preprod_02Mar.txt";
		FileUtil fileUtil = new FileUtil();

		File file = new File(filepath + File.separator + filename);
		fileUtil.removeDuplicates(file);

	}

	private static void createNewFileWithoutText() {
		String filepath = "C:\\MOSIP_PT\\Scripts\\IdRepo\\Data";
		String filename = "uin_list_Feb10.txt";
		FileUtil fileUtil = new FileUtil();
		String targetText = "uin_not_generated";
		fileUtil.createNewFileWithoutText(filepath, filename, targetText);
	}

	private static void removeText() {

		String filepath = "C:\\MOSIP_PT\\POC";
		String filename = "uin_vid_list.txt";
		FileUtil fileUtil = new FileUtil();
		String targetText = "vid_not_found";
		// File modifiedFile = fileUtil.removeSpace(filepath, filename,targetText);
		// fileUtil.removeDuplicates(modifiedFile);
		List<File> modifiedFiles = fileUtil.removeTextLines(filepath, filename, targetText);
		fileUtil.removeDuplicates(modifiedFiles.get(1));

	}

}
