package tools;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

/**
 * @author Haiqing Tang
 * email: HAITANG@student.unimelb.edu.au
 * student number: 1477462
 */

/**
 * Handles file operations, specifically reading from and writing to files. This
 * class provides methods to read lines from a file into a list and to write a
 * list of strings to a file.
 */
public class FileHandler {

	/**
	 * Reads all lines from a specified file and returns them as a list of strings.
	 * This method opens a file, reads each line sequentially, and adds each line to
	 * a List of strings.
	 * 
	 * @param fileName The path and name of the file to read from.
	 * @return A list containing all lines read from the file.
	 * @throws IOException If an I/O error occurs opening or reading from the file.
	 */
	public List<String> readLines(String fileName) throws IOException {
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;
		List<String> result = new ArrayList<>();
		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				result.add(line);
			}
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (fileReader != null) {
				fileReader.close();
			}
		}
		return result;
	}

	/**
	 * Saves a list of strings to a file, writing each string as a separate line.
	 * This method opens or creates a file at the specified path and writes each
	 * string from the list to the file.
	 *
	 * @param fileName The path and name of the file to write to.
	 * @param lines    A list of strings to be written to the file. Each string
	 *                 represents a separate line in the file.
	 * @throws FileNotFoundException If the file does not exist and cannot be
	 *                               created, or if it cannot be opened for any
	 *                               other reason.
	 */
	public void saveFile(String fileName, List<String> lines) throws FileNotFoundException {
		PrintWriter writer = null;

		writer = new PrintWriter(fileName);
		try {
			for (String line : lines) {
				writer.println(line);
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
}

