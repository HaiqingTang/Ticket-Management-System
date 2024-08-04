package concert;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import tools.FileLoad;
import operation.Booking;
import operation.BookingDetails;
import operation.Ticket;
import tools.Constants;
import tools.FileHandler;
import tools.FileLoad;
import exceptions.FileNotFoundException;
import exceptions.IncorrectPasswordException;
import exceptions.InvalidFormatException;
import exceptions.InvalidLineException;
import exceptions.NotFoundException;

/**
 * @author Haiqing Tang email: HAITANG@student.unimelb.edu.au student number:
 *         1477462
 */

public class Concert {

	/**
	 * Default constructor for creating an instance of Concert without setting any
	 * initial details.
	 */
	public Concert() {
	}

	/**
	 * Constructs a new Concert with specific details.
	 *
	 * @param concertID        Unique identifier for the concert
	 * @param date             Date of the concert
	 * @param timing           Timing of the concert
	 * @param artistName       Name of the artist performing
	 * @param venueName        Name of the venue where the concert is held
	 * @param StandLeftPrice   Price of left standing zone tickets
	 * @param StandMiddlePrice Price of middle standing zone tickets
	 * @param StandRightPrice  Price of right standing zone tickets
	 * @param SeatLeftPrice    Price of left seating zone tickets
	 * @param SeatMiddlePrice  Price of middle seating zone tickets
	 * @param SeatRightPrice   Price of right seating zone tickets
	 * @param VIPleftPrice     Price of left VIP zone tickets
	 * @param VIPmiddlePrice   Price of middle VIP zone tickets
	 * @param VIPrightPrice    Price of right VIP zone tickets
	 */
	private String concertID;
	private String date;
	private String timing;
	private String artistName;
	private String venueName;
	private double VIPleftPrice;
	private double VIPmiddlePrice;
	private double VIPrightPrice;
	private double SeatLeftPrice;
	private double SeatMiddlePrice;
	private double SeatRightPrice;
	private double StandLeftPrice;
	private double StandMiddlePrice;
	private double StandRightPrice;
	private Venue venue;
	private FileHandler fh = new FileHandler();

	public Concert(String concertID, String date, String timing, String artistName, String venueName,
			double StandLeftPrice, double StandMiddlePrice, double StandRightPrice, double SeatLeftPrice,
			double SeatMiddlePrice, double SeatRightPrice, double VIPleftPrice, double VIPmiddlePrice,
			double VIPrightPrice) {
		this.concertID = concertID;
		this.date = date;
		this.timing = timing;
		this.artistName = artistName;
		this.venueName = venueName;
		this.VIPleftPrice = VIPleftPrice;
		this.VIPmiddlePrice = VIPmiddlePrice;
		this.VIPrightPrice = VIPrightPrice;
		this.SeatLeftPrice = SeatLeftPrice;
		this.SeatMiddlePrice = SeatMiddlePrice;
		this.SeatRightPrice = SeatRightPrice;
		this.StandLeftPrice = StandLeftPrice;
		this.StandMiddlePrice = StandMiddlePrice;
		this.StandRightPrice = StandRightPrice;
	}

	/**
	 * Allows the modification of ticket prices for different zones.
	 *
	 * @param file The file management system to update ticket prices in the concert
	 *             data file.
	 * @throws IOException If there is an error reading or writing the file.
	 */
	public void changePrice(FileLoad file) throws IOException {
		ticketCosts();
		// Enter the modified seat type and price
		System.out.print("Enter the zone : VIP, SEATING, STANDING: ");
		String zoneType = Constants.INPUT_SCANNER.nextLine();
		System.out.println();
		System.out.print("Left zone price: ");
		double leftPrice = Double.parseDouble(Constants.INPUT_SCANNER.nextLine());
		System.out.print("Centre zone price: ");
		double middlePrice = Double.parseDouble(Constants.INPUT_SCANNER.nextLine());
		System.out.print("Right zone price: ");
		double rightPrice = Double.parseDouble(Constants.INPUT_SCANNER.nextLine());
		// Determine the type of seat to be modified
		if (zoneType.equals("VIP")) {
			this.VIPleftPrice = leftPrice;
			this.VIPmiddlePrice = middlePrice;
			this.VIPrightPrice = rightPrice;
		} else if (zoneType.equals("SEATING")) {
			this.SeatLeftPrice = leftPrice;
			this.SeatMiddlePrice = middlePrice;
			this.SeatRightPrice = leftPrice;
		} else if (zoneType.equals("STANDING")) {
			this.StandLeftPrice = leftPrice;
			this.StandMiddlePrice = middlePrice;
			this.StandRightPrice = leftPrice;
		}
		// Read the concert file, modify the price data, write back to concrt.csv
		List<String> lines = fh.readLines(file.getConcertFilePath());
		List<String> newLines = new ArrayList<String>();
		for (String line : lines) {
			String[] parts = line.split(",");
			String oneLine = parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3] + "," + parts[4] + ",";
			String change = "";
			if (parts[0].equals(this.concertID)) {
				if (zoneType.equals("VIP")) {
					String[] price = parts[7].split(":");
					change = parts[6] + "," + parts[6] + "," + "VIP" + ":" + String.valueOf(this.VIPleftPrice) + ":"
							+ String.valueOf(this.VIPmiddlePrice) + ":" + String.valueOf(this.VIPrightPrice);
				} else if (zoneType.equals("SEATING")) {
					String[] price = parts[6].split(":");
					change = parts[5] + "," + "SEATING" + ":" + String.valueOf(this.SeatLeftPrice) + ":"
							+ String.valueOf(this.SeatMiddlePrice) + ":" + String.valueOf(this.SeatRightPrice)
							+ parts[7];
				} else if (zoneType.equals("STANDING")) {
					String[] price = parts[5].split(":");
					change = "STANDING" + ":" + String.valueOf(this.StandLeftPrice) + ":"
							+ String.valueOf(this.StandMiddlePrice) + ":" + String.valueOf(this.StandRightPrice) + ","
							+ parts[6] + "," + parts[7];
				}
				oneLine = oneLine + change;
				newLines.add(oneLine);

			} else {
				newLines.add(line);
			}
		}
		fh.saveFile(file.getConcertFilePath(), newLines);
	}

	/**
	 * Displays all bookings for this concert.
	 *
	 * @param bookingDetails The booking details including all bookings related to
	 *                       this concert.
	 */
	public void viewAllBookings(BookingDetails bookingDetails) {
		// Query the booking information of the current concert
		List<Booking> bookings = new ArrayList<>();
		for (Booking booking : bookingDetails.getBookings()) {
			Concert concert = new Concert();
			if (booking.getConcertID().equals(this.concertID)) {
				bookings.add(booking);
			}
		}
		// If the current concert does not have a booking
		if (bookings.isEmpty()) {
			System.out.println("No Bookings found for this concert");
			System.out.println();
			return;
		}
		// Print all bookings if there is a booking
		System.out.println("Bookings");
		System.out.println(
				"---------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("%-5s%-15s%-15s%-10s%-15s%-15s%-10s%n", "Id", "Concert Date", "Artist Name", "Timing",
				"Venue Name", "Seats Booked", "Total Price");
		System.out.println(
				"---------------------------------------------------------------------------------------------------------------------------");
		for (Booking bookingLoop : bookings) {
			System.out.printf("%-5s%-15s%-15s%-10s%-15s%-15s%-10s%n", bookingLoop.getBookingID(), this.getDate(),
					this.getArtistName(), this.getTiming(), this.getVenueName(), bookingLoop.getTotalTickets(),
					bookingLoop.getTotalPrice());
		}
		System.out.println(
				"---------------------------------------------------------------------------------------------------------------------------");
		System.out.println("");
		// Print the ticket information for each booking.
		System.out.println("Ticket Info");
		for (Booking bookingTicket : bookings) {
			System.out.println("############### Booking Id: " + bookingTicket.getBookingID() + " ####################");
			System.out.printf("%-5s%-15s%-15s%-10s%-10s%n", "Id", "Aisle Number", "Seat Number", "Seat Type", "Price");
			System.out.println("##################################################");
			for (Ticket ticket : bookingTicket.getTickets()) {
				System.out.printf("%-5s%-15s%-15s%-10s%-10s%n", ticket.getTicketID(), ticket.getRowNumber(),
						ticket.getSeatNumber(), ticket.getZoneType(), ticket.getPrice());

			}
			System.out.println("##################################################");
			System.out.println("");
		}
		System.out.println("");
	}

	/**
	 * Calculates and displays the total price from all bookings for this concert.
	 *
	 * @param bookingDetails The booking details to calculate the total revenue.
	 */
	public void totalPrice(BookingDetails bookingDetails) {
		double totalPrice = 0;
		List<Booking> bookings = new ArrayList<>();
		// Iterate over the booking information corresponding to the concert
		for (Booking bookingTemp : bookingDetails.getBookings()) {
			if (bookingTemp.getConcertID().equals(this.concertID)) {
				bookings.add(bookingTemp);
			}
		}
		// Get Price
		for (Booking booking : bookings) {
			totalPrice = totalPrice + booking.getTotalPrice();
		}
		System.out.printf("Total Price for this concert is AUD %.1f%n", totalPrice);

	}

	/**
	 * Displays the prices of tickets for different zones.
	 */
	public void ticketCosts() {
		// Print the price of STANDING seats
		System.out.printf("---------- %8s ----------%n", "STANDING");
		System.out.printf("%-14s%.1f%n", "Left Seats:", StandLeftPrice);
		System.out.printf("%-14s%.1f%n", "Center Seats:", StandMiddlePrice);
		System.out.printf("%-14s%.1f%n", "Right Seats:", StandRightPrice);
		System.out.println("------------------------------");
		// Print the price of SEATING seats
		System.out.printf("---------- %8s ----------%n", "SEATING");
		System.out.printf("%-14s%.1f%n", "Left Seats:", SeatLeftPrice);
		System.out.printf("%-14s%.1f%n", "Center Seats:", SeatMiddlePrice);
		System.out.printf("%-14s%.1f%n", "Right Seats:", SeatRightPrice);
		System.out.println("------------------------------");
		// Print the price of VIP seats
		System.out.printf("---------- %8s ----------%n", "VIP");
		System.out.printf("%-14s%.1f%n", "Left Seats:", VIPleftPrice);
		System.out.printf("%-14s%.1f%n", "Center Seats:", VIPmiddlePrice);
		System.out.printf("%-14s%.1f%n", "Right Seats:", VIPrightPrice);
		System.out.println("------------------------------");

	}

	/**
	 * Allows a customer to select seats for this concert and updates booking
	 * details accordingly.
	 *
	 * @param customerID     The customer's ID
	 * @param customerName   The name of the customer
	 * @param bookingDetails Current booking details
	 * @param bookingPath    Path to the booking file
	 * @throws FileNotFoundException If the specified file path does not exist.
	 */
	public void selectSeats(String customerID, String customerName, BookingDetails bookingDetails, String bookingPath)
			throws java.io.FileNotFoundException {
		// Calling venue's seat selection function
		Booking booking = venue.selectseats(VIPleftPrice, VIPmiddlePrice, VIPrightPrice, SeatLeftPrice, SeatMiddlePrice,
				SeatRightPrice, StandLeftPrice, StandMiddlePrice, StandRightPrice);
		// Get booking information, write to booking.csv
		booking.setConcertID(concertID);
		booking.setCustomerID(customerID);
		booking.setCustomerName(customerName);
		bookingDetails.addBooking(booking, bookingPath, customerID, this.concertID);

	}

	/**
	 * Allows a customer to view their booking details for this concert.
	 *
	 * @param customerID     The ID of the customer
	 * @param concertDetails Details of the concert
	 * @param bookingDetails Booking details including all bookings
	 */
	public void viewBookDetails(String customerID, ConcertDetails concertDetails, BookingDetails bookingDetails) {
		List<Booking> newBookings = new ArrayList<>();
		// Get the booking information of the customer in the specified concert.
		for (Booking booking : bookingDetails.getBookings()) {
			Concert concert = new Concert();
			if (booking.getConcertID().equals(this.concertID) && booking.getCustomerID().equals(customerID)) {
				newBookings.add(booking);
			}
		}
		// If the information is empty, print a message
		if (newBookings.isEmpty()) {
			System.out.println("No Bookings found for this concert");
			System.out.println();
			return;
		}
		// Print all booking information
		System.out.println("Bookings");
		System.out.println(
				"---------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("%-5s%-15s%-15s%-10s%-15s%-15s%-10s%n", "Id", "Concert Date", "Artist Name", "Timing",
				"Venue Name", "Seats Booked", "Total Price");
		System.out.println(
				"---------------------------------------------------------------------------------------------------------------------------");

		for (Booking booking : newBookings) {

			System.out.printf("%-5s%-15s%-15s%-10s%-15s%-15s%-10s%n", booking.getBookingID(), this.date,
					this.artistName, this.timing, this.venueName, booking.getTotalTickets(), booking.getTotalPrice());
		}
		System.out.println(
				"---------------------------------------------------------------------------------------------------------------------------");
		System.out.println("");
		System.out.println("Ticket Info");
		// Printing ticket information from booking
		for (Booking booking : newBookings) {
			System.out.println("############### Booking Id: " + booking.getBookingID() + " ####################");
			System.out.printf("%-5s%-15s%-15s%-10s%-10s%n", "Id", "Aisle Number", "Seat Number", "Seat Type", "Price");
			System.out.println("##################################################");
			for (Ticket ticket : booking.getTickets()) {
				System.out.printf("%-5s%-15s%-15s%-10s%-10s%n", ticket.getTicketID(), ticket.getRowNumber(),
						ticket.getSeatNumber(), ticket.getZoneType(), ticket.getPrice());
			}

			System.out.println("##################################################");
			System.out.println("");
		}
		System.out.println("");
	}

	/**
	 * Gets the associated venue of the concert.
	 * 
	 * @return the current venue of the concert
	 */
	public Venue getVenue() {
		return venue;
	}

	/**
	 * Sets the venue of the concert.
	 * 
	 * @param venue the venue to set for the concert
	 */
	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	/**
	 * Retrieves the concert identifier.
	 * 
	 * @return the concert identifier
	 */
	public String getConcertID() {
		return concertID;
	}

	/**
	 * Sets the concert identifier.
	 * 
	 * @param concertID the unique identifier to set for this concert
	 */
	public void setConcertID(String concertID) {
		this.concertID = concertID;
	}

	/**
	 * Gets the date of the concert.
	 * 
	 * @return the date on which the concert is scheduled
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the date of the concert.
	 * 
	 * @param date the date to set for the concert
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Gets the timing of the concert.
	 * 
	 * @return the timing of the concert
	 */
	public String getTiming() {
		return timing;
	}

	/**
	 * Sets the timing of the concert.
	 * 
	 * @param timing the timing to set for the concert
	 */
	public void setTiming(String timing) {
		this.timing = timing;
	}

	/**
	 * Gets the name of the artist performing at the concert.
	 * 
	 * @return the artist's name
	 */
	public String getArtistName() {
		return artistName;
	}

	/**
	 * Sets the name of the artist performing at the concert.
	 * 
	 * @param artistName the artist's name to set
	 */
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	/**
	 * Gets the name of the concert venue.
	 * 
	 * @return the name of the venue
	 */
	public String getVenueName() {
		return venueName;
	}

	/**
	 * Sets the name of the concert venue.
	 * 
	 * @param venueName the name of the venue to set
	 */
	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	/**
	 * Gets the price of VIP seats on the left side of the venue.
	 * 
	 * @return the left VIP seat price
	 */
	public double getVIPleftPrice() {
		return VIPleftPrice;
	}

	/**
	 * Sets the price of VIP seats on the left side of the venue.
	 * 
	 * @param vIPleftPrice the price to set for left VIP seats
	 */
	public void setVIPleftPrice(double vIPleftPrice) {
		VIPleftPrice = vIPleftPrice;
	}

	/**
	 * Gets the price of VIP seats in the middle of the venue.
	 * 
	 * @return the middle VIP seat price
	 */
	public double getVIPmiddlePrice() {
		return VIPmiddlePrice;
	}

	/**
	 * Sets the price of VIP seats in the middle of the venue.
	 * 
	 * @param vIPmiddlePrice the price to set for middle VIP seats
	 */
	public void setVIPmiddlePrice(double vIPmiddlePrice) {
		VIPmiddlePrice = vIPmiddlePrice;
	}

	/**
	 * Gets the price of VIP seats on the right side of the venue.
	 * 
	 * @return the right VIP seat price
	 */
	public double getVIPrightPrice() {
		return VIPrightPrice;
	}

	/**
	 * Sets the price of VIP seats on the right side of the venue.
	 * 
	 * @param vIPrightPrice the price to set for right VIP seats
	 */
	public void setVIPrightPrice(double vIPrightPrice) {
		VIPrightPrice = vIPrightPrice;
	}

	/**
	 * Gets the price of standard seating on the left side of the venue.
	 * 
	 * @return the left seating price
	 */
	public double getSeatLeftPrice() {
		return SeatLeftPrice;
	}

	/**
	 * Sets the price for standard seating on the left side of the venue.
	 * 
	 * @param seatLeftPrice the price to set for left seating
	 */
	public void setSeatLeftPrice(double seatLeftPrice) {
		SeatLeftPrice = seatLeftPrice;
	}

	/**
	 * Gets the price of standard seating in the middle of the venue.
	 * 
	 * @return the middle seating price
	 */
	public double getSeatMiddlePrice() {
		return SeatMiddlePrice;
	}

	/**
	 * Sets the price for standard seating in the middle of the venue.
	 * 
	 * @param seatMiddlePrice the price to set for middle seating
	 */
	public void setSeatMiddlePrice(double seatMiddlePrice) {
		SeatMiddlePrice = seatMiddlePrice;
	}

	/**
	 * Gets the price of standard seating on the right side of the venue.
	 * 
	 * @return the right seating price
	 */
	public double getSeatRightPrice() {
		return SeatRightPrice;
	}

	/**
	 * Sets the price for standard seating on the right side of the venue.
	 * 
	 * @param seatRightPrice the price to set for right seating
	 */
	public void setSeatRightPrice(double seatRightPrice) {
		SeatRightPrice = seatRightPrice;
	}

	/**
	 * Gets the price of standing area on the left side of the venue.
	 * 
	 * @return the left standing area price
	 */
	public double getStandLeftPrice() {
		return StandLeftPrice;
	}

	/**
	 * Sets the price for the standing area on the left side of the venue.
	 * 
	 * @param standLeftPrice the price to set for left standing area
	 */
	public void setStandLeftPrice(double standLeftPrice) {
		StandLeftPrice = standLeftPrice;
	}

	/**
	 * Gets the price of standing area in the middle of the venue.
	 * 
	 * @return the middle standing area price
	 */
	public double getStandMiddlePrice() {
		return StandMiddlePrice;
	}

	/**
	 * Sets the price for the standing area in the middle of the venue.
	 * 
	 * @param standMiddlePrice the price to set for middle standing area
	 */
	public void setStandMiddlePrice(double standMiddlePrice) {
		StandMiddlePrice = standMiddlePrice;
	}

	/**
	 * Gets the price of standing area on the right side of the venue.
	 * 
	 * @return the right standing area price
	 */
	public double getStandRightPrice() {
		return StandRightPrice;
	}

	/**
	 * Sets the price for the standing area on the right side of the venue.
	 * 
	 * @param standRightPrice the price to set for right standing area
	 */
	public void setStandRightPrice(double standRightPrice) {
		StandRightPrice = standRightPrice;
	}
}

