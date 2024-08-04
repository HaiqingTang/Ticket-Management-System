package concert;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator.OfDouble;
import tools.FileLoad;
import operation.Booking;
import operation.BookingDetails;
import operation.Ticket;
import tools.Constants;
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
 * Represents the venue for a concert with detailed layout and seating
 * arrangements.
 */
public class Venue {
	private int rows;
	private int seatInARow;
	private int left;
	private int right;
	private int middle;
	private int totalSeats;
	private ArrayList<ArrayList<Integer>> layout = new ArrayList<>();
	private int VRows;
	private int SRows;
	private int TRows;
	private int bookedSeat;// already booked seats
	private int unbookedSeat;// unbooked seats
	private int seatsLeft;

	/**
	 * Constructs a venue with specific layout and seating details.
	 * 
	 * @param rows       Total number of rows in the venue.
	 * @param seatInARow Number of seats in each row.
	 * @param left       Number of seats on the left side.
	 * @param right      Number of seats on the right side.
	 * @param middle     Number of seats in the middle section.
	 * @param totalSeats Total number of seats available in the venue.
	 * @param VRows      Number of VIP rows.
	 * @param SRows      Number of Seating rows.
	 * @param TRows      Number of Standing rows.
	 */
	public Venue(int rows, int seatInARow, int left, int right, int middle, int totalSeats, int VRows, int SRows,
			int TRows) {
		this.rows = rows;
		this.seatInARow = seatInARow;
		this.left = left;
		this.right = right;
		this.middle = middle;
		this.totalSeats = totalSeats;
		this.VRows = VRows;
		this.SRows = SRows;
		this.TRows = TRows;
		this.bookedSeat = 0;
		this.unbookedSeat = totalSeats;
		this.seatsLeft = totalSeats;
		for (int i = 0; i < rows; i++) {
			ArrayList<Integer> row = new ArrayList<>();
			for (int j = 1; j <= seatInARow; j++) {
				row.add(j);
			}
			this.layout.add(row);
		}
	}

	/**
	 * Selects seats based on the user's input and creates a booking for those
	 * seats. Calculates total price based on the type and location of seats chosen.
	 *
	 * @param VIPleftPrice     Price per seat in the VIP left zone.
	 * @param VIPmiddlePrice   Price per seat in the VIP middle zone.
	 * @param VIPrightPrice    Price per seat in the VIP right zone.
	 * @param SeatLeftPrice    Price per seat in the regular seating left zone.
	 * @param SeatMiddlePrice  Price per seat in the regular seating middle zone.
	 * @param SeatRightPrice   Price per seat in the regular seating right zone.
	 * @param StandLeftPrice   Price per seat in the standing left zone.
	 * @param StandMiddlePrice Price per seat in the standing middle zone.
	 * @param StandRightPrice  Price per seat in the standing right zone.
	 * @return Booking object containing details of the booked tickets.
	 */
	public Booking selectseats(double VIPleftPrice, double VIPmiddlePrice, double VIPrightPrice, double SeatLeftPrice,
			double SeatMiddlePrice, double SeatRightPrice, double StandLeftPrice, double StandMiddlePrice,
			double StandRightPrice) {
		Booking booking = new Booking();
		showBookingLayout();
		double totalPrice = 0;
		String input = "";
		int row = 0;
		// Enter seat selection location, coordinates and quantity
		System.out.print("Enter the aisle number: ");
		String aisleNum = Constants.INPUT_SCANNER.nextLine();
		System.out.print("Enter the seat number: ");
		int seatNum = Integer.parseInt(Constants.INPUT_SCANNER.nextLine());
		System.out.print("Enter the number of seats to be booked: ");
		String bookNumber = Constants.INPUT_SCANNER.nextLine();
		char seatType = aisleNum.charAt(0);
		int seatRow = Character.getNumericValue(aisleNum.charAt(1));
		// Determining seat type and location
		if (seatType == 'V') {
			row = seatRow;
		} else if (seatType == 'S') {
			row = seatRow + VRows;
		} else if (seatType == 'T') {
			row = seatRow + VRows + SRows;
		}
		ArrayList<Integer> arrayRow = this.getLayout().get(row - 1);// the number of row
		// Set the selected position to booked.
		for (int i = seatNum; i < seatNum + Integer.parseInt(bookNumber); i++) {
			arrayRow.set(i - 1, Constants.CONCERT_BOOKED);
			this.unbookedSeat--;
			this.bookedSeat++;

		}

		String zoneType = "";
		if (seatType == 'V') {
			zoneType = "VIP";
		} else if (seatType == 'S') {
			zoneType = "SEATING";
		} else if (seatType == 'T') {
			zoneType = "STANDING";
		}
		booking.setTotalTickets(bookNumber);

		for (int i = 0; i < Integer.parseInt(bookNumber); i++) {
			int ticketId = i + 1;
			int rowNumber = seatRow;
			int seatNumber = seatNum + i;
			// Determine the coordinates of the seat in the layout
			double ticketPrice = 0;
			if (seatType == 'V') {
				if (seatNum + i <= this.left) {
					ticketPrice = VIPleftPrice;
				} else if (seatNum + i <= this.left + this.middle && seatNum + i > left) {
					ticketPrice = VIPmiddlePrice;
				} else if (seatNum + i <= this.seatInARow && seatNum + i > left + middle) {
					ticketPrice = VIPrightPrice;
				}
			} else if (seatType == 'S') {

				if (seatNum + i <= this.left) {
					ticketPrice = SeatLeftPrice;
				} else if (seatNum + i <= this.left + this.middle && seatNum + i > left) {
					ticketPrice = SeatMiddlePrice;
				} else if (seatNum + i <= this.seatInARow && seatNum + i > left + middle) {
					ticketPrice = SeatRightPrice;
				}
			} else if (seatType == 'T') {
				if (seatNum + i <= this.left) {
					ticketPrice = StandLeftPrice;
				} else if (seatNum + i <= this.left + this.middle && seatNum + i > left) {
					ticketPrice = StandMiddlePrice;
				} else if (seatNum + i <= this.seatInARow && seatNum + i > left + middle) {
					ticketPrice = StandRightPrice;
				}
			}
			totalPrice = totalPrice + ticketPrice;
			// Create a new ticket object and pass the seat information to initialize it.
			Ticket ticket = new Ticket(i + 1, rowNumber, seatNumber, zoneType, ticketPrice);
			// Write to booking
			booking.getTickets().add(ticket);
		}

		// showBookingLayout();
		booking.setTotalPrice(totalPrice);

		return booking;

	}

	/**
	 * Constructs a Venue with specific seating parameters and initializes seat
	 * allocation. This method parses venue layout information and sets up the
	 * seating arrangement.
	 *
	 * @param lines          List of string data from a file representing the venue
	 *                       layout.
	 * @param bookingDetails Details of bookings for the venue.
	 * @param concertId      Identifier for the concert whose venue details are
	 *                       being loaded.
	 * @param fileName       Name of the file being processed.
	 * @return A Venue object fully initialized with seating and layout details.
	 * @throws IOException           If an I/O error occurs.
	 * @throws FileNotFoundException If the specified file is not found.
	 */
	public Venue loadVenue(List<String> lines, BookingDetails bookingDetails, String concertId, String fileName)
			throws IOException, FileNotFoundException {

		int totalSeats = 0;
		int totalRows = 0;
		int seatALine = 0;
		int left = 0;
		int middle = 0;
		int right = 0;
		int VRows = 0;
		int SRows = 0;
		int TRows = 0;
		int blank = -1;
		// Import venue information into layout
		FileLoad fileLoad = new FileLoad();
		ArrayList<Integer> layout = new ArrayList<>();
		String[] firstLineParts = lines.get(0).split(" ");
		for (String part : firstLineParts) {
			for (int i = 0; i < part.length(); i++) {
				// Calculate the number of left-center-right seats
				if (String.valueOf(part.charAt(i)).matches("\\[")) {
					if (blank == 0) {
						left++;
					} else if (blank == 1) {
						middle++;
					} else if (blank == 2) {
						right++;
					}
				}
			}
			blank++;
		}

		for (String line : lines) {

			try {
				fileLoad.validateLine(line, fileName);
				String[] parts = line.split(" ");
				String firstChar = parts[0];
				// Calculate the number of rows for each type of seat
				if (parts.length > 0 && !parts[0].isEmpty()) {
					if (parts[0].charAt(0) == 'V') {
						VRows++;
					} else if (parts[0].charAt(0) == 'S') {
						SRows++;
					} else if (parts[0].charAt(0) == 'T') {
						TRows++;
					}
				}
			} catch (InvalidLineException e) {
				System.out.println(e.getMessage());
			} catch (InvalidFormatException e) {
				System.out.println(e.getMessage());
			}

		}
		// Calculate the total number of rows and columns
		seatALine = left + middle + right;
		totalRows = VRows + SRows + TRows;
		totalSeats = totalRows * seatALine;

		Venue venue = new Venue(totalRows, seatALine, left, right, middle, totalSeats, VRows, SRows, TRows);
		venue.loadTicketVenue(bookingDetails, concertId);

		return venue;
	}

	/**
	 * Applies bookings from the booking details to the venue layout, marking seats
	 * as booked. This method updates the internal state of the venue to reflect the
	 * current bookings.
	 *
	 * @param bookingDetails Details of all bookings related to this venue.
	 * @param concertId      The concert identifier for which bookings are to be
	 *                       applied.
	 */
	public void loadTicketVenue(BookingDetails bookingDetails, String concertId) {
		List<Booking> bookings = new ArrayList<>();
		// Importing already booked seat information
		for (Booking temp : bookingDetails.getBookings()) {
			if (temp.getConcertID().equals(concertId)) {
				bookings.add(temp);
			}
		}

		for (Booking booking : bookings) {
			// Get seat information for the first ticket
			Ticket firstTicket = booking.getTickets().get(0);

			int totalNumber = Integer.parseInt(booking.getTotalTickets());
			int rowNumber = firstTicket.getRowNumber();
			int seatNumber = firstTicket.getSeatNumber();

			if (booking.getTickets().get(0).getZoneType().equals("SEATING")) {
				rowNumber = rowNumber + VRows;

			} else if (booking.getTickets().get(0).getZoneType().equals("STANDING")) {
				rowNumber = rowNumber + VRows + SRows;

			} else if (booking.getTickets().get(0).getZoneType().equals("VIP")) {

			}
			int i = rowNumber - 1;
			int j = seatNumber - 1;
			// Getting the total number of tickets
			ArrayList<Integer> row = this.getLayout().get(i);// the number of row
			// Modify the layout's seating information
			// and modify the unbookedseat and bookedseat
			while (totalNumber > 0) {
				row.set(j, Constants.CONCERT_BOOKED);
				this.unbookedSeat--;
				this.bookedSeat++;
				j++;
				totalNumber = totalNumber - 1;
			}
		}
	}

	/**
	 * Displays the seating layout of the venue, showing which seats are booked and
	 * available. This method visually represents the seat bookings on the console.
	 */
	public void showBookingLayout() {
		int rowNumber = 1;
		int rows = 1;
		int count = 0;
		int rowIndex = 0;

		// Output VIP seating layout
		for (int i = 1; i <= VRows; i++, rowIndex++) {
			System.out.print("V" + i + " ");
			for (int seat : layout.get(rowIndex)) {
				if (count == left || count == left + middle) {
					System.out.print(" ");

				}
				if (seat == Constants.CONCERT_BOOKED) {
					System.out.print("[" + "X" + "]");
				} else {
					System.out.print("[" + seat + "]");
				}

				count++;
			}
			System.out.println(" V" + i);
			count = 0;
		}
		System.out.println();

		// Output SEATING seating layout
		for (int i = 1; i <= SRows; i++, rowIndex++) {
			System.out.print("S" + i + " ");
			for (int seat : layout.get(rowIndex)) {
				if (count == left || count == left + middle) {
					System.out.print(" ");

				}
				if (seat == Constants.CONCERT_BOOKED) {
					System.out.print("[" + "X" + "]");
				} else {
					System.out.print("[" + seat + "]");
				}
				count++;
			}
			System.out.println(" S" + i);
			count = 0;
		}
		System.out.println();

		// Output STANDING seating layout
		for (int i = 1; i <= TRows; i++, rowIndex++) {
			System.out.print("T" + i + " ");
			for (int seat : layout.get(rowIndex)) {
				if (count == left || count == left + middle) {
					System.out.print(" ");

				}
				if (seat == Constants.CONCERT_BOOKED) {
					System.out.print("[" + "X" + "]");
				} else {
					System.out.print("[" + seat + "]");
				}
				count++;
			}
			System.out.println(" T" + i);
			count = 0;
		}

	}

	/**
	 * Returns the number of seats in a row.
	 *
	 * @return The number of seats per row.
	 */
	public int getSeatInARow() {
		return seatInARow;
	}

	/**
	 * Sets the number of seats per row.
	 *
	 * @param seatInARow The new number of seats in each row.
	 */
	public void setSeatInARow(int seatInARow) {
		this.seatInARow = seatInARow;
	}

	/**
	 * Default constructor for the Venue class.
	 */
	public Venue() {
		// Initialization logic if any
	}

	/**
	 * Gets the total number of rows in the venue.
	 *
	 * @return The total number of rows.
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Sets the total number of rows in the venue.
	 *
	 * @param rows The new total number of rows.
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * Gets the number of seats on the left side of each row.
	 *
	 * @return The number of left side seats.
	 */
	public int getLeft() {
		return left;
	}

	/**
	 * Sets the number of seats on the left side of each row.
	 *
	 * @param left The new number of left side seats.
	 */
	public void setLeft(int left) {
		this.left = left;
	}

	/**
	 * Gets the number of seats on the right side of each row.
	 *
	 * @return The number of right side seats.
	 */
	public int getRight() {
		return right;
	}

	/**
	 * Sets the number of seats on the right side of each row.
	 *
	 * @param right The new number of right side seats.
	 */
	public void setRight(int right) {
		this.right = right;
	}

	/**
	 * Gets the number of seats in the middle section of each row.
	 *
	 * @return The number of middle section seats.
	 */
	public int getMiddle() {
		return middle;
	}

	/**
	 * Sets the number of seats in the middle section of each row.
	 *
	 * @param middle The new number of middle section seats.
	 */
	public void setMiddle(int middle) {
		this.middle = middle;
	}

	/**
	 * Gets the total number of seats in the venue.
	 *
	 * @return The total number of seats.
	 */
	public int getTotalSeats() {
		return totalSeats;
	}

	/**
	 * Sets the total number of seats in the venue.
	 *
	 * @param totalSeats The new total number of seats.
	 */
	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	/**
	 * Gets the layout of the venue as a list of rows, each containing a list of
	 * seat numbers.
	 *
	 * @return The layout of the venue.
	 */
	public ArrayList<ArrayList<Integer>> getLayout() {
		return layout;
	}

	/**
	 * Sets the layout of the venue.
	 *
	 * @param layout A new layout for the venue.
	 */
	public void setLayout(ArrayList<ArrayList<Integer>> layout) {
		this.layout = layout;
	}

	/**
	 * Gets the number of VIP rows in the venue.
	 *
	 * @return The number of VIP rows.
	 */
	public int getVRows() {
		return VRows;
	}

	/**
	 * Sets the number of VIP rows in the venue.
	 *
	 * @param vRows The new number of VIP rows.
	 */
	public void setVRows(int vRows) {
		VRows = vRows;
	}

	/**
	 * Gets the number of Seating rows in the venue.
	 *
	 * @return The number of Seating rows.
	 */
	public int getSRows() {
		return SRows;
	}

	/**
	 * Sets the number of Seating rows in the venue.
	 *
	 * @param sRows The new number of Seating rows.
	 */
	public void setSRows(int sRows) {
		SRows = sRows;
	}

	/**
	 * Gets the number of Standing rows in the venue.
	 *
	 * @return The number of Standing rows.
	 */
	public int getTRows() {
		return TRows;
	}

	/**
	 * Sets the number of Standing rows in the venue.
	 *
	 * @param tRows The new number of Standing rows.
	 */
	public void setTRows(int tRows) {
		TRows = tRows;
	}

	/**
	 * Gets the number of seats that are already booked.
	 *
	 * @return The number of booked seats.
	 */
	public int getBookedSeat() {
		return bookedSeat;
	}

	/**
	 * Sets the number of seats that have been booked.
	 *
	 * @param bookedSeat The new number of booked seats.
	 */
	public void setBookedSeat(int bookedSeat) {
		this.bookedSeat = bookedSeat;
	}

	/**
	 * Gets the number of seats that are unbooked.
	 *
	 * @return The number of unbooked seats.
	 */
	public int getUnbookedSeat() {
		return unbookedSeat;
	}

	/**
	 * Sets the number of seats that are unbooked.
	 *
	 * @param unbookedSeat The new number of unbooked seats.
	 */
	public void setUnbookedSeat(int unbookedSeat) {
		this.unbookedSeat = unbookedSeat;
	}

	/**
	 * Gets the number of seats left unbooked.
	 *
	 * @return The number of seats left.
	 */
	public int getSeatsLeft() {
		return seatsLeft;
	}

	/**
	 * Sets the number of seats left unbooked.
	 *
	 * @param seatsLeft The new number of seats left.
	 */
	public void setSeatsLeft(int seatsLeft) {
		this.seatsLeft = seatsLeft;
	}
}

