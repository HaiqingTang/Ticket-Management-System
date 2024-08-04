package exceptions;

/**
 * Custom exception for handling cases where a required entity is not found.
 */
public class NotFoundException extends Exception {
	/**
	 * Constructs a NotFoundException with no detail message.
	 */
	public NotFoundException() {
		super();
	}

	/**
	 * Constructs a NotFoundException with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public NotFoundException(String msg) {
		super(msg);
	}
}

