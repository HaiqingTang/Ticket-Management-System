package tools;

import java.util.Scanner;

/**
 * @author Haiqing Tang
 * email: HAITANG@student.unimelb.edu.au
 * student number: 1477462
 */

/**
 * This class provides a central location for managing all constants used across
 * the Ticket Management System. It includes constants for input handling, error
 * checking parameters, and specific application-wide values.
 */
public class Constants {
	/**
	 * Scanner object for handling all user input throughout the application. This
	 * scanner reads from the standard input stream.
	 */
	public static final Scanner INPUT_SCANNER = new Scanner(System.in);

	/**
	 * The minimum number of segments expected in a valid concert data line, used to
	 * validate file data.
	 */
	public static final int CONCERT_INVALID_NUM = 7;

	/**
	 * Represents a booked seat in the seating layout. Used in array representations
	 * of venue seating.
	 */
	public static final int CONCERT_BOOKED = 0;

	/**
	 * The minimum number of segments expected in a valid booking data line, used to
	 * validate file data.
	 */
	public static final int BOOKING_INVALID_NUM = 5;

	/**
	 * The index in the booking data array that holds the total number of tickets
	 * for a booking.
	 */
	public static final int BOOKING_TOTAL_NUM = 4;

	/**
	 * The length of data representing a single ticket in the booking file, used for
	 * parsing ticket information.
	 */
	public static final int LENGTH_OF_A_TICKET = 5;

	/**
	 * The minimum number of arguments required for admin mode to initialize
	 * correctly.
	 */
	public static final int USER_INVALID_NUM = 4;

	/**
	 * The exact number of arguments expected when an existing user is initializing
	 * in customer mode.
	 */
	public static final int USER_EXIST = 5;

	/**
	 * The exact number of arguments expected when initializing a new customer
	 * account.
	 */
	public static final int USER_NEW = 3;

	/**
	 * The minimum number of segments expected in a valid user data line, used to
	 * validate file data.
	 */
	public static final int USER_INVALID_FILE = 3;

	/**
	 * A constant used to represent the command to exit from any menu or the
	 * application.
	 */
	public static final String USER_EXIT = "0";

	/**
	 * A constant used to represent the command to exit from a concert context
	 * within the application.
	 */
	public static final String CONCERT_EXIT = "5";
}
