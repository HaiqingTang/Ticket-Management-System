package user;

import concert.ConcertDetails;
import operation.Booking;
import operation.BookingDetails;
import java.io.IOException;
import java.util.List;
import concert.Concert;
import concert.ConcertDetails;
import tools.FileLoad;
import user.Customer;
import tools.Constants;
import tools.FileHandler;

/**
 * @author Haiqing Tang
 * email: HAITANG@student.unimelb.edu.au
 * student number: 1477462
 */
/**
 * Admin class that provides administrative functionalities such as viewing and
 * updating concert details, viewing bookings, and managing ticket prices.
 */
public class Admin extends User {
	private FileHandler fh = new FileHandler();
	/**
     * constructors
     */
    public Admin(){ 
    }

	/**
	 * Displays the main menu options to the administrator.
	 */
	@Override
	public void mainMenu() {
		// TODO Auto-generated method stub
		System.out.println("Select an option to get started!");
		System.out.println("Press 1 to view all the concert details");
		System.out.println("Press 2 to update the ticket costs");
		System.out.println("Press 3 to view booking details");
		System.out.println("Press 4 to view total payment received for a concert");
		System.out.println("Press 5 to exit");
		System.out.print("> ");

	}

	/**
	 * Enters the administrative mode, allowing the admin to interact with the
	 * system via a menu-driven interface.
	 * 
	 * @param concertDetails Details of all concerts.
	 * @param bookingDetails Details of all bookings.
	 * @param file           Instance of FileLoad to handle file operations.
	 * @throws IOException If an input or output exception occurred.
	 */
	public void adminMode(ConcertDetails concertDetails, BookingDetails bookingDetails, FileLoad file)
			throws IOException {
		mainMenu();
		loopMenu(concertDetails, bookingDetails, file);
	}

	/**
	 * Handles the user input for different administrative actions within the main
	 * menu loop.
	 * 
	 * @param concertDetails Details of all concerts.
	 * @param bookingDetails Details of all bookings.
	 * @param file           Instance of FileLoad to handle file operations.
	 * @return A string indicating the termination ("0" or "5") or continuation of
	 *         the admin mode.
	 * @throws IOException If an input or output exception occurred.
	 */
	public String loopMenu(ConcertDetails concertDetails, BookingDetails bookingDetails, FileLoad file)
			throws IOException {
		String input = "";
		// Determine if user input is 0
		input = Constants.INPUT_SCANNER.nextLine();
		if (input.equals("0")) {
			System.out.print("Exiting admin mode");
			return "0";
		}
		while (input != null) {
			switch (input) {
			case "0":
				System.out.print("Exiting admin mode");
				return "0";
			case "1":
				welcomeMenu(concertDetails, bookingDetails);
				break;
			case "2":
				changePrice(concertDetails, bookingDetails, file);
				break;
			case "3":
				viewAllBookings(concertDetails, bookingDetails);
				break;
			case "4":
				totalPayment(concertDetails, bookingDetails);
				break;
			case "5":
				System.out.println("Exiting admin mode");
				return "5";
			default:
				System.out.println("Invalid Input");
				break;
			}
			mainMenu();
			input = Constants.INPUT_SCANNER.nextLine();
		}
		return "0";
	}

	/**
	 * Displays a summary of all concerts including details like date, artist name,
	 * timing, venue, and seating.
	 *
	 * @param concertDetails Details of all concerts.
	 * @param bookingDetails Details of all bookings.
	 */
	@Override
	public void welcomeMenu(ConcertDetails concertDetails, BookingDetails bookingDetails) {
		// Outputting Concert Messages
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
	 * Calculates and displays the total payment received for a selected concert.
	 *
	 * @param concertDetails Details of all concerts.
	 * @param bookingDetails Details of all bookings.
	 */
	public void totalPayment(ConcertDetails concertDetails, BookingDetails bookingDetails) {
		// Calculating booking prices
		welcomeMenu(concertDetails, bookingDetails);
		String concertNum = "";
		System.out.print("> ");
		concertNum = Constants.INPUT_SCANNER.nextLine();
		Concert concert = new Concert();
		for (Concert concertTemp : concertDetails.getConcerts()) {
			if (concertTemp.getConcertID().equals(concertNum)) {
				concert = concertTemp;
				break;
			}
		}
		concert.totalPrice(bookingDetails);
	}

	/**
	 * Allows the admin to change the price of tickets for a specific concert.
	 *
	 * @param concertDetails Details of all concerts.
	 * @param bookingDetails Details of all bookings.
	 * @param file           Instance of FileLoad to handle file operations.
	 * @throws IOException If an input or output exception occurred.
	 */
	public void changePrice(ConcertDetails concertDetails, BookingDetails bookingDetails, FileLoad file)
			throws IOException {
		// Modify Concert Price
		System.out.println("Select a concert or 0 to exit");
		welcomeMenu(concertDetails, bookingDetails);
		String concertNum = "";
		System.out.print("> ");
		concertNum = Constants.INPUT_SCANNER.nextLine();
		Concert concert = new Concert();
		for (Concert concertTemp : concertDetails.getConcerts()) {
			if (concertTemp.getConcertID().equals(concertNum)) {
				concert = concertTemp;
				break;
			}
		}
		concert.changePrice(file);
	}

	/**
	 * Displays all bookings for all concerts, allowing the admin to select a
	 * specific concert to view detailed bookings.
	 *
	 * @param concertDetails Details of all concerts.
	 * @param bookingDetails Details of all bookings.
	 */
	public void viewAllBookings(ConcertDetails concertDetails, BookingDetails bookingDetails) {
		// See all bookings
		System.out.println("Select a concert or 0 to exit");
		welcomeMenu(concertDetails, bookingDetails);
		String concertNum = "";
		System.out.print("> ");
		concertNum = Constants.INPUT_SCANNER.nextLine();
		Concert concert = new Concert();
		// View the booking corresponding to the selected certificate
		for (Concert concertTemp : concertDetails.getConcerts()) {
			if (concertTemp.getConcertID().equals(concertNum)) {
				concert = concertTemp;
				break;
			}
		}
		concert.viewAllBookings(bookingDetails);
	}
}

