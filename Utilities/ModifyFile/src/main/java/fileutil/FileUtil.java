package fileutil;

import java.io.*;

import java.util.*;

public class FileUtil {

	private String originalFile;

	public File removeSpace(String filepath, String filename, String targetText) {
		originalFile = filepath + File.separator + filename;

		String filename1 = filename.substring(0, filename.lastIndexOf(".")) + "_modified.txt";
		File file_new = new File(filepath + File.separator + filename1);
		try {

			FileWriter fw = new FileWriter(file_new, true);

			BufferedReader br = new BufferedReader(new FileReader(originalFile));

			String readLine = "";

			System.out.println("Reading file using Buffered Reader");
			BufferedWriter bw = new BufferedWriter(fw);

			while ((readLine = br.readLine()) != null) {
				if (!readLine.contains(targetText) && !readLine.isEmpty()) {
					// System.out.println(readLine);
					bw.write(readLine + "\r\n");
				}

			}
			bw.close();
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return file_new;

	}

	public void removeDuplicates(File file) {
		Set<String> set = new HashSet<>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String readLine = "";
			System.out.println("Going to delete " + file.getAbsolutePath());
			// if (new File(originalFile).exists())
			// new File(originalFile).delete();
			while ((readLine = br.readLine()) != null) {
				set.add(readLine);
//				if (set.size() >= 20) {
//					writeToOriginalFile(set);
//					set.clear();
//				}
			}

			System.out.println("set size " + set.size());

			br.close();
			if (set.size() > 0) {
				file.delete();
				writeToFile(set, file);
				set.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void writeToFile(Set<String> dataSet, File file) {

		FileWriter fw;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file, true);

			bw = new BufferedWriter(fw);
			for (String line : dataSet) {
				bw.write(line + "\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void writeToOriginalFile(Set<String> set) throws IOException {

		FileWriter fw = new FileWriter(new File(originalFile), true);
		BufferedWriter bw = new BufferedWriter(fw);
		for (String line : set) {
			bw.write(line + "\r\n");
		}
		bw.close();
	}

	public List<File> removeTextLines(String filepath, String filename, String targetText) {
		List<File> files = new ArrayList<>();
		originalFile = filepath + File.separator + filename;
		String filename1 = filename.substring(0, filename.lastIndexOf(".")) + "_modified.txt";
		File fileWithoutText = new File(filepath + File.separator + filename1);
		File fileWithUinsToResend = new File(
				filepath + File.separator + filename.substring(0, filename.lastIndexOf(".")) + "_resend.txt");
		FileWriter fw;
		FileWriter fw1;
		BufferedWriter bw = null;
		BufferedWriter bw1 = null;
		try {
			fw = new FileWriter(fileWithoutText, true);
			fw1 = new FileWriter(fileWithUinsToResend, true);

			BufferedReader br = new BufferedReader(new FileReader(originalFile));
			bw = new BufferedWriter(fw);
			bw1 = new BufferedWriter(fw1);
			String readLine = "";
			while ((readLine = br.readLine()) != null) {
				if (!readLine.contains(targetText) && !readLine.isEmpty()) {
					// System.out.println(readLine);
					bw.write(readLine + "\r\n");
				} else if (readLine.contains(targetText)) {
					bw1.write(readLine + "\r\n");
				}

			}
			files.add(fileWithoutText);
			files.add(fileWithUinsToResend);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bw.close();
				bw1.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return files;
	}

	public void createNewFileWithoutText(String filepath, String filename, String targetText) {

		Set<String> set = new HashSet<>();

		String file = filepath + File.separator + filename;
		String file1 = filepath + File.separator + "uin_list.txt";
		BufferedReader br = null;
		try {

			br = new BufferedReader(new FileReader(file));
			String readLine = "";
			while ((readLine = br.readLine()) != null) {
				if (!readLine.contains(targetText) && !readLine.isEmpty()) {
					set.add(readLine);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (set.size() > 0) {
			new File(file).delete();
			writeToFile(set, new File(file));
		}
	}

	public void countUniqueTexts(File file) {
		List<String> list = new LinkedList<>();
		Set<String> set = new LinkedHashSet<>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String readLine = "";
			System.out.println("Going to delete " + file.getAbsolutePath());

			while ((readLine = br.readLine()) != null) {
				list.add(readLine);
				set.add(readLine);

			}
			System.out.println("List size: " + list.size());
			System.out.println("set size " + set.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
