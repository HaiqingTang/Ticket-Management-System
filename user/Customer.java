package user;

import concert.ConcertDetails;
import operation.Booking;
import operation.BookingDetails;
import java.io.File;
import java.io.IOException;
import java.util.List;
import concert.Concert;
import concert.ConcertDetails;
import tools.FileLoad;
import user.Customer;
import tools.Constants;
import tools.FileHandler;
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
 * Represents a customer in the booking system. This class provides
 * functionalities for a customer to interact with the system, including viewing
 * and booking concerts, updating personal information, and viewing bookings.
 */
public class Customer extends User {
	private String customerID;

	private String customerName;
	private FileLoad file = new FileLoad();//////
	private FileHandler fh = new FileHandler();

	/**
	 * Default constructor initializes customer with empty ID and name.
	 */
	public Customer() {
		this.customerID = "";
		this.customerName = "";
	}

	/**
	 * Registers a new customer and adds their information to the system.
	 *
	 * @param id   The customer ID to assign to the new customer.
	 * @param psw  The password for the new customer.
	 * @param path The path to the file where customer data is stored.
	 * @return The unique number identifier of the new customer as a string.
	 * @throws FileLoad.FileNotFoundException If the specified file does not exist.
	 * @throws IOException                    If an I/O error occurs during file
	 *                                        handling.
	 */
	public String newCustomer(String id, String psw, String path) throws FileNotFoundException, IOException {
		FileLoad fileLoad = new FileLoad();
		File file = new File(path);
		// If the customer file does not exist
		if (!file.exists()) {
			// throw an exception
			throw new FileNotFoundException(path + " (No such file or directory)");
		}
		// Read in existing user information and calculate new user id
		List<String> lines = fh.readLines(path);
		int num = 1;
		for (String line : lines) {
			num++;
		}
		// Adding new user information to user information
		String newLine = num + "," + id + "," + psw;
		lines.add(newLine);
		// save file
		fh.saveFile(path, lines);
		return String.valueOf(num);

	}

	/**
	 * Handles the customer mode operations allowing interaction with concert and
	 * booking details.
	 *
	 * @param concertDetails Details of all concerts available for booking.
	 * @param bookingDetails Details of all current bookings.
	 * @param bookingPath    Path to the booking data file.
	 * @param file           Instance of FileLoad to handle file operations.
	 * @throws IOException            If an I/O error occurs.
	 * @throws FileNotFoundException  If a required file is not found.
	 * @throws InvalidLineException   If a line in any of the read files does not
	 *                                conform to the expected format.
	 * @throws InvalidFormatException If the format of the data in the files is
	 *                                invalid.
	 */
	public void customerMode(ConcertDetails concertDetails, BookingDetails bookingDetails, String bookingPath,
			FileLoad file) throws IOException, FileNotFoundException, InvalidLineException, InvalidFormatException {
		Concert concert = new Concert();

		welcomeMenu(concertDetails, bookingDetails);
		// Getting user input
		String input = "";
		System.out.print("> ");
		input = Constants.INPUT_SCANNER.nextLine();
		// If the user opts out, the message is printed
		if (input.equals(Constants.USER_EXIT)) {
			System.out.println("Exiting customer mode");
			return;
		}
		// Otherwise query the concert information selected by the user
		for (Concert concertTemp : concertDetails.getConcerts()) {
			if (concertTemp.getConcertID().equals(input)) {
				concert = concertTemp;
			}
		}
		mainMenu();
		while (!input.equals(Constants.USER_EXIT)) {

			input = loopMenu(concertDetails, bookingDetails, concert, bookingPath, file);
			// When the user exit the program
			if (input.equals(Constants.USER_EXIT)) {
				System.out.println("Exiting customer mode");
				return;
				// When the user exit the concert
			} else if (input.equals(Constants.CONCERT_EXIT)) {
				welcomeMenu(concertDetails, bookingDetails);
				System.out.print("> ");
				input = Constants.INPUT_SCANNER.nextLine();
				if (input.equals(Constants.USER_EXIT)) {
					break;
				}
				mainMenu();
				// Prints information about all concerts
				for (Concert concertTemp : concertDetails.getConcerts()) {
					if (concertTemp.getConcertID().equals(input)) {
						concert = concertTemp;
					}
				}
			}
		}

		System.out.println("Exiting customer mode");

	}

	/**
	 * Provides a loop menu for customer operations like booking tickets and viewing
	 * concert details.
	 *
	 * @param concertDetails Details of all concerts.
	 * @param bookingDetails Details of all bookings.
	 * @param concert        The current concert being interacted with.
	 * @param bookingPath    Path to the booking data file.
	 * @param file           Instance of FileLoad to handle file operations.
	 * @return A string indicating whether the customer is exiting the menu or
	 *         continuing.
	 * @throws IOException            If an I/O error occurs.
	 * @throws FileNotFoundException  If a required file is not found.
	 * @throws InvalidLineException   If a line in any of the read files does not
	 *                                conform to the expected format.
	 * @throws InvalidFormatException If the format of the data in the files is
	 *                                invalid.
	 */
	public String loopMenu(ConcertDetails concertDetails, BookingDetails bookingDetails, Concert concert,
			String bookingPath, FileLoad file)
			throws IOException, FileNotFoundException, InvalidLineException, InvalidFormatException {
		String input = "";
		input = Constants.INPUT_SCANNER.nextLine();
		// Access to functions based on user input
		while (input != null) {
			switch (input) {
			case "0":
				// Exiting User Mode
				return Constants.USER_EXIT;

			case "1":
				// View Ticket Prices
				concert.ticketCosts();
				break;
			case "2":
				// View avenue layout
				concert.getVenue().showBookingLayout();
				break;
			case "3":
				// select seats
				concert.selectSeats(customerID, customerName, bookingDetails, bookingPath);
				bookingDetails = updateBookingDetails(bookingPath, file);
				break;
			case "4":
				// View booking information
				concert.viewBookDetails(customerID, concertDetails, bookingDetails);
				break;
			case "5":
				// Exit the concert
				System.out.println("Exiting this concert");
				return Constants.CONCERT_EXIT;

			default:
				// Prompt when input is invalid
				System.out.println("Invalid Input");
				break;
			}
			mainMenu();
			input = Constants.INPUT_SCANNER.nextLine();

		}
		return Constants.CONCERT_EXIT;
	}

	/**
	 * Updates and returns the booking details after changes have been made, such as
	 * a new booking.
	 *
	 * @param bookingPath Path to the booking data file.
	 * @param file        Instance of FileLoad used for file operations.
	 * @return Updated booking details.
	 * @throws IOException            If an I/O error occurs.
	 * @throws FileNotFoundException  If the booking file is not found.
	 * @throws InvalidLineException   If a line in the booking file does not conform
	 *                                to the expected format.
	 * @throws InvalidFormatException If the format of the data in the booking file
	 *                                is invalid.
	 */
	public BookingDetails updateBookingDetails(String bookingPath, FileLoad file)
			throws IOException, FileNotFoundException, InvalidLineException, InvalidFormatException {
		// Update bookingDetails,read the file
		BookingDetails bookingDetails = file.loadBookingDetails();
		// load the information in file
		bookingDetails = bookingDetails.loadCustomerBookingDetails(this.customerID, bookingDetails);
		return bookingDetails;
	}

	/**
	 * Displays a welcome menu listing all concerts.
	 *
	 * @param concertDetails Details of all concerts.
	 * @param bookingDetails Details of all bookings.
	 */
	public void welcomeMenu(ConcertDetails concertDetails, BookingDetails bookingDetails) {
		// Prints information about all concerts
		System.out.println("Select a concert or 0 to exit");
		System.out.println(
				"---------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("%-5s%-15s%-15s%-15s%-30s%-15s%-15s%-15s\n", "#", "Date", "Artist Name", "Timing",
				"Venue Name", "Total Seats", "Seats Booked", "Seats Left");
		System.out.println(
				"---------------------------------------------------------------------------------------------------------------------------");
		for (Concert concert : concertDetails.getConcerts()) {
			System.out.printf("%-5s%-15s%-15s%-15s%-30s%-15s%-15s%-15s\n", concert.getConcertID(), concert.getDate(),
					concert.getArtistName(), concert.getTiming(), concert.getVenueName(),
					concert.getVenue().getTotalSeats(), concert.getVenue().getBookedSeat(),
					concert.getVenue().getUnbookedSeat());

		}

		System.out.println(
				"---------------------------------------------------------------------------------------------------------------------------");
	}

	/**
	 * Displays the main menu specific to customer operations.
	 */
	@Override
	public void mainMenu() {
		// Printing system functions that allow the user to choose
		System.out.println("Select an option to get started!");
		System.out.println("Press 1 to look at the ticket costs");
		System.out.println("Press 2 to view seats layout");
		System.out.println("Press 3 to book seats");
		System.out.println("Press 4 to view booking details");
		System.out.println("Press 5 to exit");
		System.out.print("> ");
	}

	/**
	 * Gets the customer ID.
	 * 
	 * @return The customer ID.
	 */
	public String getCustomerID() {
		return customerID;
	}

	/**
	 * Sets the customer ID.
	 * 
	 * @param customerID The new customer ID to set.
	 */
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	/**
	 * Gets the customer name.
	 * 
	 * @return The customer name.
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * Sets the customer name.
	 * 
	 * @param customerName The new customer name to set.
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}

