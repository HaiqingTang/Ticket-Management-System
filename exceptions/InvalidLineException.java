package exceptions;

/**
 * Custom exception for handling cases where a line in a file does not conform
 * to the expected format.
 */
public class InvalidLineException extends Exception {
	/**
	 * Constructs an InvalidLineException with no detail message.
	 */
	public InvalidLineException() {
		super();
	}

	/**
	 * Constructs an InvalidLineException with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public InvalidLineException(String msg) {
		super(msg);
	}
}

