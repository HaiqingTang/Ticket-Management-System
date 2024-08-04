package interfacefile;

import java.io.IOException;
import java.util.List;
import tools.Constants;
import tools.FileLoad;
import exceptions.FileNotFoundException;
import exceptions.IncorrectPasswordException;
import exceptions.InvalidFormatException;
import exceptions.InvalidLineException;
import exceptions.NotFoundException;

/**
 * @author Haiqing Tang
 * email: HAITANG@student.unimelb.edu.au
 * student number: 1477462
 */

/**
 * Provides an interface for loading information from a file into an object.
 * This interface is intended to be implemented by any class that needs to read
 * data from a file and convert it into an instance of a type specified by the
 * generic type parameter T.
 *
 * @param <T> The type of object that this loader will return after processing
 *            the file.
 */
public interface loadInfo<T> {
	/**
	 * Loads information from a specified file and converts it into an object of
	 * type T. This method is designed to be implemented in a way that it reads
	 * through a list of strings, which represent lines from a file, processes each
	 * line according to specific rules or formats, and ultimately constructs an
	 * object of type T based on the processed data.
	 *
	 * @param lines    A List of strings where each string is a line from the file
	 *                 being processed. Each line is expected to contain data that
	 *                 can be parsed and used to populate fields in the object of
	 *                 type T.
	 * @param fileName The name of the file from which the lines are read. This
	 *                 could be used for logging or error messages to provide
	 *                 context about the source of the data.
	 * @return An object of type T populated with the data read from the file.
	 * @throws IOException                   If an I/O error occurs while reading
	 *                                       from the file.
	 * @throws FileLoad.InvalidLineException If a line in the file does not conform
	 *                                       to the expected format, indicating that
	 *                                       the line is invalid for processing.
	 * @throws FileNotFoundException         If the file specified by fileName does
	 *                                       not exist.
	 * @throws InvalidFormatException        If the data in the file is correctly
	 *                                       formatted but not valid according to
	 *                                       business or data rules defined in the
	 *                                       implementation.
	 */
	T loadInfo(List<String> lines, String fileName)
			throws IOException, InvalidLineException, FileNotFoundException, InvalidFormatException;

}
