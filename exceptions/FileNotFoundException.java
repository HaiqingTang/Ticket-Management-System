package exceptions;

/**
 * Custom exception for handling cases where a specified file cannot be found.
 */
public class FileNotFoundException extends Exception {
	/**
	 * Constructs a FileNotFoundException with no detail message.
	 */
	public FileNotFoundException() {
		super();
	}

	/**
	 * Constructs a FileNotFoundException with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public FileNotFoundException(String msg) {
		super(msg);
	}
}

