package tools;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import concert.Concert;
import concert.ConcertDetails;
import concert.Venue;
import operation.Booking;
import operation.BookingDetails;
import operation.Ticket;
import exceptions.FileNotFoundException;
import exceptions.IncorrectPasswordException;
import exceptions.InvalidFormatException;
import exceptions.InvalidLineException;
import exceptions.NotFoundException;
import user.Customer;
import tools.Constants;

/**
 * @author Haiqing Tang
 * email: HAITANG@student.unimelb.edu.au
 * student number: 1477462
 */
/**
 * Handles the loading and validation of different types of files related to a
 * booking system, including customer, concert, and booking files.
 */
public class FileLoad {

	private String userMode;
	private String customerID;
	private String customerPwd;
	private String customerFilePath;
	private String concertFilePath;
	private String bookingFilePath;
	private List<String> venueFilePaths = new ArrayList<>();
	private FileHandler fh = new FileHandler();
	/**
     * constructors
     */
    public FileLoad(){ 
    }

	/**
	 * Preloads user information based on command line arguments. This method
	 * initializes file paths and user information based on the mode (admin or
	 * customer).
	 *
	 * @param args Command line arguments providing user mode and file paths.
	 * @return True if the user is loaded correctly, False otherwise.
	 * @throws IncorrectPasswordException If the password provided is incorrect.
	 * @throws IOException                If there is an issue reading a file.
	 * @throws FileNotFoundException      If a specified file cannot be found.
	 * @throws NotFoundException          If a required entity is not found in the
	 *                                    files.
	 */
	public boolean PreloadUser(String[] args)
			throws IncorrectPasswordException, IOException, FileNotFoundException, NotFoundException {
		// Load information
		String userMode = args[0];
		String customerID = null;
		String customerPwd = null;
		String customerFilePath = null;
		String concertFilePath = null;
		String bookingFilePath = null;
		List<String> venueFilePaths = new ArrayList<>();
		// Determine if the user is admin
		if ("--admin".equals(userMode)) {
			if (args.length < Constants.USER_INVALID_NUM) {
				System.out.println("args.length < 4");
				return false;
			}
			// Initialization Path
			this.userMode = userMode;
			this.customerFilePath = args[1];
			this.concertFilePath = args[2];
			this.bookingFilePath = args[3];
			// Initialize avenue path
			if (args.length >= Constants.USER_INVALID_NUM) {
				for (int i = Constants.USER_INVALID_NUM; i < args.length; i++) {
					this.venueFilePaths.add(args[i]);
				}
			}
			// If the user is customer
		} else if ("--customer".equals(userMode)) {
			// Determine whether to enter the account password
			if (!args[1].contains("customer.csv")) {
				// Load Path Data
				this.userMode = userMode;
				this.customerID = args[1];
				this.customerPwd = args[2];
				this.customerFilePath = args[3];
				// Verify user identity
				authenticateUser(this.customerID, this.customerPwd, this.customerFilePath);

				this.concertFilePath = args[4];
				this.bookingFilePath = args[5];
				if (args.length > Constants.USER_EXIST) {
					for (int i = Constants.USER_EXIST + 1; i < args.length; i++) {
						this.venueFilePaths.add(args[i]);
					}
				}
			} else {
				// new customer
				this.userMode = userMode;
				this.customerFilePath = args[1];
				System.out.print("Enter your name: ");
				String newCustomerName = Constants.INPUT_SCANNER.nextLine();
				System.out.print("Enter your password: ");
				String newCustomerPsw = Constants.INPUT_SCANNER.nextLine();
				Customer cust = new Customer();
				String newCustomerId = cust.newCustomer(newCustomerName, newCustomerPsw, this.customerFilePath);
				// Load Path Data
				this.concertFilePath = args[2];
				this.bookingFilePath = args[3];
				this.customerID = newCustomerId;
				this.customerPwd = newCustomerPsw;
				if (args.length > Constants.USER_NEW) {
					for (int i = Constants.USER_NEW + 1; i < args.length; i++) {
						this.venueFilePaths.add(args[i]);
					}
				}

			}
			return true;

		} else {
			System.out.println("Invalid user mode. Terminating program now.");
			return false;
		}
		return true;

	}

	/**
	 * Authenticates a user by verifying their ID and password against stored
	 * credentials.
	 *
	 * @param userID           The user's ID.
	 * @param userPsw          The user's password.
	 * @param customerFilePath The file path containing customer data.
	 * @throws IncorrectPasswordException If the password does not match the stored
	 *                                    password.
	 * @throws IOException                If there is an issue reading the file.
	 * @throws FileNotFoundException      If the customer file cannot be found.
	 * @throws NotFoundException          If the customer is not found in the file.
	 */
	public void authenticateUser(String userID, String userPsw, String customerFilePath)
			throws IncorrectPasswordException, IOException, FileNotFoundException, NotFoundException {
		File file = new File(customerFilePath);
		// Load the user file and compare it with the input information
		if (!file.exists()) {
			throw new FileNotFoundException(customerFilePath + " (No such file or directory)");
		}
		List<String> lines = fh.readLines(customerFilePath);
		int customerCount = 0;
		int compCount = 0;
		// Iterate through user information to see if there is a match
		for (String line : lines) {
			customerCount++;
			String[] userInfo = line.split(",");
			String id = userInfo[0].trim();
			String password = userInfo[2].trim();
			if (id.equals(userID) && password.equals(userPsw)) {
				break;

			} else if (id.equals(userID) && !password.equals(userPsw)) {
				// If the userid matches but the password does not, throw an exception.
				throw new IncorrectPasswordException("Incorrect Password. Terminating Program");
			} else if (!id.equals(userID)) {
				compCount++;
			}
		}
		// Throws an exception if the userid does not exist.
		if (customerCount == compCount) {

			throw new NotFoundException("Customer does not exist. Terminating Program");
		}

	}

	/**
	 * Loads customer information from a file.
	 *
	 * @param customerID The ID of the customer to load.
	 * @return The name of the customer.
	 * @throws IOException            If there is an issue reading the file.
	 * @throws FileNotFoundException  If the customer file cannot be found.
	 * @throws InvalidLineException   If a line in the file does not conform to the
	 *                                expected format.
	 * @throws InvalidFormatException If the data format is correct but not valid.
	 */
	public String loadCustomerInfo(String customerID)
			throws IOException, FileNotFoundException, InvalidLineException, InvalidFormatException {

		File file = new File(this.customerFilePath);
//Determine if a file path exists
		if (!file.exists()) {
			throw new FileNotFoundException(this.customerFilePath + " (No such file or directory)");
		}

		FileLoad fileLoad = new FileLoad();
//Read in user information sequentially
		List<String> lines = fh.readLines(this.customerFilePath);
		String name = "";
//Process user information sequentially
		for (String line : lines) {
			try {
				String[] parts = line.split(",");
				fileLoad.validateLine(line, this.customerFilePath);
				if (parts.length < Constants.USER_INVALID_FILE) {
					throw new InvalidLineException("Invalid Customer Files. Skipping this line.");
				}
				// Get user name
				if (parts[0].trim().equals(customerID)) {
					name = parts[1].trim();
					return name;
				}

			} catch (InvalidLineException e) {
				System.out.println(e.getMessage());
			} catch (InvalidFormatException e) {
				System.out.println(e.getMessage());
			}

		}
		return name;

	}

	/**
	 * Loads concert information from a file into a ConcertDetails object.
	 *
	 * @return A ConcertDetails object containing all concerts loaded from the file.
	 * @throws IOException            If there is an issue reading the file.
	 * @throws FileNotFoundException  If the concert file cannot be found.
	 * @throws InvalidLineException   If a line in the file does not conform to the
	 *                                expected format.
	 * @throws InvalidFormatException If the data format is correct but not valid.
	 */
	public ConcertDetails loadConcertsInfo()
			throws IOException, FileNotFoundException, InvalidLineException, InvalidFormatException {
		File file = new File(this.concertFilePath);
		// Detecting the existence of concert file
		if (!file.exists()) {
			throw new FileNotFoundException(this.concertFilePath + " (No such file or directory)");
		}
		ConcertDetails cd = new ConcertDetails();
		// Read the contents of the file sequentially
		List<String> lines = fh.readLines(this.concertFilePath);
		cd = cd.loadInfo(lines, this.concertFilePath);

		return cd;

	}

	/**
	 * Loads booking details from a file into a BookingDetails object.
	 *
	 * @return A BookingDetails object containing all bookings loaded from the file.
	 * @throws IOException            If there is an issue reading the file.
	 * @throws FileNotFoundException  If the booking file cannot be found.
	 * @throws InvalidLineException   If a line in the file does not conform to the
	 *                                expected format.
	 * @throws InvalidFormatException If the data format is correct but not valid.
	 */
	public BookingDetails loadBookingDetails()
			throws IOException, FileNotFoundException, InvalidLineException, InvalidFormatException {

		File file = new File(this.bookingFilePath);
		// Detecting the existence of booking file
		if (!file.exists()) {
			throw new FileNotFoundException(this.bookingFilePath + " (No such file or directory)");
		}
		BookingDetails bookingDetails = new BookingDetails();
		// Read the contents of the file sequentially
		List<String> lines = fh.readLines(this.bookingFilePath);
		bookingDetails = bookingDetails.loadInfo(lines, this.bookingFilePath);

		return bookingDetails;

	}

	/**
	 * Loads venue details from a file and integrates them with a specific concert's
	 * booking details.
	 *
	 * @param venueName      The name of the venue to load.
	 * @param bookingDetails Booking details to be integrated with venue
	 *                       information.
	 * @param concertId      The ID of the concert for which venue details are being
	 *                       loaded.
	 * @return A Venue object with details loaded and integrated.
	 * @throws IOException           If there is an issue reading the file.
	 * @throws FileNotFoundException If the venue file cannot be found.
	 * @throws InvalidLineException  If a line in the file does not conform to the
	 *                               expected format.
	 */
	public Venue loadVenue(String venueName, BookingDetails bookingDetails, String concertId)
			throws IOException, FileNotFoundException, InvalidLineException {
		Venue venue1 = new Venue();
		String oneVenuePath = "";
		String loadPath = "";// load to venue
		// Detecting the existence of path
		if (this.venueFilePaths.isEmpty()) {

			loadPath = this.customerFilePath.replace("customer.csv", "venue_default.txt");
		}
		for (String filePath : this.venueFilePaths) {

			File file = new File(filePath);
			String fileName = file.getName();
			if (fileName.contains(venueName)) {
				// Detecting the existence of venue file
				if (!file.exists()) {
					throw new FileNotFoundException(filePath + " (No such file or directory)");
				}
				loadPath = filePath;
				break;
			}
		}
		// Judge the concert corresponding to the avenue
		if (loadPath.equals("")) {
			if (this.venueFilePaths.get(0).contains("mcg")) {
				loadPath = this.venueFilePaths.get(0);
				loadPath = loadPath.replace("mcg", "default");
			} else if (this.venueFilePaths.get(0).contains("marvel")) {
				loadPath = this.venueFilePaths.get(0);
				// If not, set to defalut
				loadPath = loadPath.replace("marvel", "default");
			}
		}
		List<String> lines = fh.readLines(loadPath);
		venue1 = venue1.loadVenue(lines, bookingDetails, concertId, loadPath);
		return venue1;
	}

	/**
	 * Validates a line from a file to ensure it meets the required format and data
	 * expectations.
	 *
	 * @param line     The line of data to validate.
	 * @param fileName The name of the file from which the line is read.
	 * @throws InvalidLineException   If the line does not meet the expected format.
	 * @throws FileNotFoundException  If the file cannot be found.
	 * @throws InvalidFormatException If the format is correct but the data is not
	 *                                valid.
	 */
	public void validateLine(String line, String fileName)
			throws InvalidLineException, FileNotFoundException, InvalidFormatException {

		if (fileName.contains("concert.csv")) {
			// Detect whether the length of each line of data in the concert is normal or
			// not
			if (line.length() < Constants.CONCERT_INVALID_NUM) {
				throw new InvalidLineException("Invalid Concert Files. Skipping this line.");

			} else {
				// /Detect if the id of the concert is a number
				String[] data = line.split(",");
				if (!data[0].matches("\\d+")) {
					throw new InvalidFormatException("Concert Id is in incorrect format. Skipping this line.");

				}
			}
			// Detect whether the length of each line of data in the customer is normal or
			// not
		} else if (fileName.contains("customer.csv")) {
			if (line.length() < Constants.USER_INVALID_FILE) {
				System.err.println("Invalid Customer Files. Skipping this line.");
				throw new InvalidLineException("Invalid Customer Files. Skipping this line.");
			} else {
				// Detect if the id of the customer is a number
				String[] data = line.split(",");
				if (!data[0].matches("\\d+")) {
					throw new InvalidFormatException("Customer Id is in incorrect format. Skipping this line.");
				}

			}
			// Detect whether the length of each line of data in the booking is normal or
			// not
		} else if (fileName.contains("bookings.csv")) {
			if (line.length() < Constants.BOOKING_INVALID_NUM) {////////
				throw new InvalidLineException("Invalid Booking Files. Skipping this line.");
			} else {
				// Detect if the id of the booking is a number
				String[] data = line.split(",");
				if (!data[0].matches("\\d+")) {

					throw new FileNotFoundException("Booking Id is in incorrect format. Skipping this line.");
					// Detect if the total number of tickets is 0
				} else if (data[Constants.BOOKING_TOTAL_NUM] == "0") {
					throw new InvalidFormatException("Incorrect Number of Tickets. Skipping this line.");

				}
			}
			// Detect whether the length of each line of data in the venue is normal or not
		} else if (fileName.contains("venue")) {
			String[] data = line.split(" ");

			if (!line.equals("")) {
				char firstChar = data[0].charAt(0);
				// Detect if the row name of avenue belongs to V, S, T
				if (!(firstChar == 'V' || firstChar == 'S' || firstChar == 'T')) {
					throw new InvalidFormatException("Invalid Zone Type. Skipping this line.");
				}
			}

		}

	}

	/**
	 * Gets the current user mode of the application.
	 * 
	 * @return The current user mode.
	 */
	public String getUserMode() {
		return userMode;
	}

	/**
	 * Sets the user mode of the application.
	 * 
	 * @param userMode The new user mode.
	 */
	public void setUserMode(String userMode) {
		this.userMode = userMode;
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
	 * @param customerID The new customer ID.
	 */
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	/**
	 * Gets the customer password.
	 * 
	 * @return The customer password.
	 */
	public String getCustomerPwd() {
		return customerPwd;
	}

	/**
	 * Sets the customer password.
	 * 
	 * @param customerPwd The new customer password.
	 */
	public void setCustomerPwd(String customerPwd) {
		this.customerPwd = customerPwd;
	}

	/**
	 * Gets the path to the customer data file.
	 * 
	 * @return The path to the customer data file.
	 */
	public String getCustomerFilePath() {
		return customerFilePath;
	}

	/**
	 * Sets the path to the customer data file.
	 * 
	 * @param customerFilePath The new path to the customer data file.
	 */
	public void setCustomerFilePath(String customerFilePath) {
		this.customerFilePath = customerFilePath;
	}

	/**
	 * Gets the path to the concert data file.
	 * 
	 * @return The path to the concert data file.
	 */
	public String getConcertFilePath() {
		return concertFilePath;
	}

	/**
	 * Sets the path to the concert data file.
	 * 
	 * @param concertFilePath The new path to the concert data file.
	 */
	public void setConcertFilePath(String concertFilePath) {
		this.concertFilePath = concertFilePath;
	}

	/**
	 * Gets the path to the booking data file.
	 * 
	 * @return The path to the booking data file.
	 */
	public String getBookingFilePath() {
		return bookingFilePath;
	}

	/**
	 * Sets the path to the booking data file.
	 * 
	 * @param bookingFilePath The new path to the booking data file.
	 */
	public void setBookingFilePath(String bookingFilePath) {
		this.bookingFilePath = bookingFilePath;
	}

	/**
	 * Gets the list of paths to venue data files.
	 * 
	 * @return The list of paths to venue data files.
	 */
	public List<String> getVenueFilePaths() {
		return venueFilePaths;
	}

	/**
	 * Sets the list of paths to venue data files.
	 * 
	 * @param venueFilePaths The new list of paths to venue data files.
	 */
	public void setVenueFilePaths(List<String> venueFilePaths) {
		this.venueFilePaths = venueFilePaths;
	}
}

