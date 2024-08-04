package operation;

import java.io.*;
import java.util.*;
import exceptions.FileNotFoundException;
import exceptions.IncorrectPasswordException;
import exceptions.InvalidFormatException;
import exceptions.InvalidLineException;
import exceptions.NotFoundException;
import tools.FileHandler;
import tools.FileLoad;
import interfacefile.*;
import tools.Constants;

/**
 * @author Haiqing Tang
 * email: HAITANG@student.unimelb.edu.au
 * student number: 1477462
 */

/**
 * Manages detailed information about bookings including loading, adding, and
 * retrieving bookings. Implements the loadInfo interface to load booking
 * details from a file.
 */
public class BookingDetails implements loadInfo<BookingDetails> {
	private List<Booking> bookings = new ArrayList<>();
	private FileHandler fh = new FileHandler();

	/**
	 * Retrieves the list of all bookings managed by this instance.
	 * 
	 * @return A list of bookings.
	 */
	public List<Booking> getBookings() {
		return bookings;
	}
	/**
     * constructors
     */
    public BookingDetails(){ 
    }

	/**
	 * Sets the list of bookings managed by this instance.
	 * 
	 * @param bookings The list of Booking objects to be managed.
	 */
	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	/**
	 * Loads booking details from a specified file and returns an instance of
	 * BookingDetails populated with bookings. It parses the file line by line to
	 * create Booking objects and adds them to the BookingDetails instance.
	 * 
	 * @param lines    A list of strings where each string represents a line in the
	 *                 file being processed.
	 * @param fileName The name of the file from which booking data is being loaded.
	 * @return A BookingDetails object containing all bookings loaded from the file.
	 * @throws IOException                   If an I/O error occurs during file
	 *                                       reading.
	 * @throws FileLoad.InvalidLineException If a line does not conform to the
	 *                                       expected format.
	 * @throws FileNotFoundException         If the file specified by fileName
	 *                                       cannot be found.
	 * @throws InvalidFormatException        If the data format is correct but not
	 *                                       valid.
	 */
	@Override
	public BookingDetails loadInfo(List<String> lines, String fileName)
			throws IOException, InvalidLineException, FileNotFoundException, InvalidFormatException {
		BookingDetails bookingDetails = new BookingDetails();
		FileLoad fileLoad = new FileLoad();
		// Processing multiple booking messages in sequence
		for (String line : lines) {

			try {
				Booking booking = new Booking();
				String[] data = line.split(",");
				// If the data is anomalous, it is skipped
				if (data.length < Constants.BOOKING_INVALID_NUM) {
					throw new InvalidLineException("Invalid booking Files. Skipping this line.");
				}
				char tNumber = data[4].trim().charAt(0);
				// If the number of tickets is 0 or not a number
				if (data[4].trim().equals("0") || (!Character.isDigit(tNumber))) {
					throw new InvalidFormatException("Incorrect Number of Tickets. Skipping this line.");
				}
				fileLoad.validateLine(line, fileName);
				// Assign the separated data to the booking in order.
				String bookingID = data[0].trim();
				String customerID = data[1].trim();
				String customerName = data[2].trim();
				String concertID = data[3].trim();
				String totalTickets = data[4].trim();
				double totalPrice = 0;
				booking.setBookingID(bookingID);
				booking.setConcertID(concertID);
				booking.setCustomerID(customerID);
				booking.setCustomerName(customerName);
				booking.setTotalTickets(totalTickets);

				List<Ticket> tickets = new ArrayList<>();
				// Loading ticket information into booking
				for (int i = 0; i < Integer.parseInt(totalTickets); i++) {
					Ticket ticket = new Ticket(
							Integer.parseInt(data[(i + 1) * Constants.LENGTH_OF_A_TICKET + 0].trim()),
							Integer.parseInt(data[(i + 1) * Constants.LENGTH_OF_A_TICKET + 1].trim()),
							Integer.parseInt(data[(i + 1) * Constants.LENGTH_OF_A_TICKET + 2].trim()),
							data[(i + 1) * Constants.LENGTH_OF_A_TICKET + 3].trim(),
							Double.parseDouble(data[(i + 1) * Constants.LENGTH_OF_A_TICKET + 4].trim()));

					booking.getTickets().add(ticket);
					totalPrice = totalPrice
							+ Double.parseDouble(data[(i + 1) * Constants.LENGTH_OF_A_TICKET + 4].trim());
					booking.setTotalPrice(totalPrice);
				}
				// Putting booking information into bookingDetail
				bookingDetails.getBookings().add(booking);
			} catch (InvalidLineException e) {
				System.out.println(e.getMessage());
			} catch (InvalidFormatException e) {
				System.out.println(e.getMessage());
			}
		}

		return bookingDetails;

	}

	/**
	 * Retrieves all bookings for a specified customer from the provided
	 * BookingDetails.
	 * 
	 * @param customerID     The customer identifier for which bookings are to be
	 *                       retrieved.
	 * @param bookingDetails The BookingDetails from which to extract
	 *                       customer-specific bookings.
	 * @return A BookingDetails instance containing only the bookings for the
	 *         specified customer.
	 */
	public BookingDetails loadCustomerBookingDetails(String customerID, BookingDetails bookingDetails) {
		FileLoad file = new FileLoad();
		BookingDetails customerBookings = new BookingDetails();
		// Match all bookings for this user
		for (Booking booking : bookingDetails.getBookings()) {
			if (booking.getCustomerID().equals(customerID)) {
				customerBookings.getBookings().add(booking);
			}
		}
		return customerBookings;
	}

	/**
	 * Adds a new booking to the existing list of bookings and saves it to a file.
	 * 
	 * @param booking     The booking to be added.
	 * @param bookingPath The file path where the booking should be saved.
	 * @param customerID  The customer ID associated with the booking.
	 * @param concertID   The concert ID associated with the booking.
	 * @throws FileNotFoundException If the file specified by bookingPath cannot be
	 *                               found.
	 */
	public void addBooking(Booking booking, String bookingPath, String customerID, String concertID)
			throws java.io.FileNotFoundException {

		int bookingId = 1;
		int temp = 0;
		// Iterate through the user's already existing bookings for a given concert
		for (Booking newBooking : bookings) {
			if (newBooking.getCustomerID().equals(customerID) && newBooking.getConcertID().equals(concertID)) {
				if (temp < Integer.parseInt(newBooking.getBookingID())) {
					temp = Integer.parseInt(newBooking.getBookingID());
				}
			}
		}
		bookingId = temp + bookingId;
		booking.setBookingID(String.valueOf(bookingId));
		// Get information about all the concerts the user has attended
		bookings.add(booking);
		List<String> lines = new ArrayList<String>();
		// Write data processed to lines ready to be written to file
		for (Booking b : bookings) {
			String ticket = "";
			String line = b.getBookingID() + "," + b.getCustomerID() + "," + b.getCustomerName() + ","
					+ b.getConcertID() + "," + b.getTotalTickets();
			for (Ticket t : b.getTickets()) {
				ticket = ticket + "," + t.getTicketID() + "," + t.getRowNumber() + "," + t.getSeatNumber() + ","
						+ t.getZoneType() + "," + t.getPrice();
			}
			line = line + ticket;
			lines.add(line);

		}
		// save to booking.csv file
		fh.saveFile(bookingPath, lines);
	}

}

