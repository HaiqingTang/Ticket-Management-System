package exceptions;

/**
 * Custom exception for handling cases where the format is correct but the
 * content is invalid.
 */
public class InvalidFormatException extends Exception {
	/**
	 * Constructs an InvalidFormatException with no detail message.
	 */
	public InvalidFormatException() {
		super();
	}

	/**
	 * Constructs an InvalidFormatException with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public InvalidFormatException(String msg) {
		super(msg);
	}
}

