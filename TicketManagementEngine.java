import java.io.IOException;
import java.util.ArrayList;
import concert.Concert;
import concert.ConcertDetails;
import operation.BookingDetails;
import tools.FileLoad;
import user.Admin;
import user.Customer;
import tools.Constants;
import exceptions.FileNotFoundException;
import exceptions.IncorrectPasswordException;
import exceptions.InvalidFormatException;
import exceptions.InvalidLineException;
import exceptions.NotFoundException;
import tools.Constants;

/**
 * @author Haiqing Tang
 * email: HAITANG@student.unimelb.edu.au
 * student number: 1477462
 */
/**
 * The main engine for the Ticket Management System. This class handles
 * initialization, user authentication, and redirection to appropriate user
 * modes (customer or admin) based on the user's credentials and role.
 */
public class TicketManagementEngine {
	/**
	 * The main method to start the application. It initializes the system, loads
	 * necessary data, and handles user authentication.
	 *
	 * @param args Command line arguments passed to configure user mode and paths.
	 * @throws IOException                If an I/O error occurs during file
	 *                                    operations.
	 * @throws InvalidLineException       If a line in any of the read files does
	 *                                    not conform to the expected format.
	 * @throws InvalidFormatException     If the format of the data in the files is
	 *                                    incorrect.
	 * @throws IncorrectPasswordException If the user password does not match the
	 *                                    expected value.
	 */
	public static void main(String[] args)
			throws IOException, InvalidLineException, InvalidFormatException, IncorrectPasswordException {
		try {
			// Initialize all variables
			TicketManagementEngine tme = new TicketManagementEngine();
			FileLoad file = new FileLoad();
			ConcertDetails concertDetails = new ConcertDetails();
			BookingDetails bookingDetails = new BookingDetails();
			// Verify input user information and process
			boolean state = file.PreloadUser(args);
			if (!state) {
				return;
			}
			// Initialize user information
			Customer customer = new Customer();
			Admin admin = new Admin();
			// Determining User Mode
			if (file.getUserMode().equals("--customer")) {
				customer.setCustomerID(file.getCustomerID());////////////////////////////////////

				customer.setCustomerName(file.loadCustomerInfo(customer.getCustomerID()));
			} else if (file.getUserMode().equals("--admin")) {
				file.loadCustomerInfo("0");
			} else {
				System.out.print("Invalid user mode. Terminating program now.");
				return;
			}
			// load concerts info
			concertDetails = file.loadConcertsInfo();
			// load bookings info
			bookingDetails = file.loadBookingDetails();
			// load venues info
			for (Concert concert : concertDetails.getConcerts()) {
				String nameString = concert.getVenueName().toLowerCase();
				concert.setVenue(file.loadVenue(nameString, bookingDetails, concert.getConcertID()));

			}
			// Outputting the welcome statement
			if (file.getUserMode().equals("--customer")) {

				System.out.print("Welcome " + customer.getCustomerName() + " to Ticket Management System");
				System.out.println();

			} else if (file.getUserMode().equals("--admin")) {
				System.out.print("Welcome to Ticket Management System Admin Mode.");
				System.out.println();

			} else {
				System.out.print("Invalid user mode. Terminating program now.");
				System.out.println();

				return;

			}
			// Judge and enter the corresponding user mode
			tme.displayMessage();
			if (file.getUserMode().equals("--customer")) {
				customer.customerMode(concertDetails, bookingDetails, file.getBookingFilePath(), file);

			} else if (file.getUserMode().equals("--admin")) {
				admin.adminMode(concertDetails, bookingDetails, file);

			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IncorrectPasswordException e) {
			System.out.println(e.getMessage());
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}

		// save the booking/customer/concert data back to files
	}

	/**
	 * Displays a custom ASCII art message to the console.
	 */
	public void displayMessage(){


        System.out.print("\n" +
                " ________  ___ _____ \n" +
                "|_   _|  \\/  |/  ___|\n" +
                "  | | | .  . |\\ `--. \n" +
                "  | | | |\\/| | `--. \\\n" +
                "  | | | |  | |/\\__/ /\n" +
                "  \\_/ \\_|  |_/\\____/ \n" +
                "                    \n" +
                "                    \n");
    }

}

