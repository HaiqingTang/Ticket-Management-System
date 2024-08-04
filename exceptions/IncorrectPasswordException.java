package exceptions;

/**
 * Custom exception for handling cases where a password does not match the
 * expected password.
 */
public class IncorrectPasswordException extends Exception {
	/**
	 * Constructs an IncorrectPasswordException with no detail message.
	 */
	public IncorrectPasswordException() {
		super();
	}

	/**
	 * Constructs an IncorrectPasswordException with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public IncorrectPasswordException(String msg) {
		super(msg);
	}
}

