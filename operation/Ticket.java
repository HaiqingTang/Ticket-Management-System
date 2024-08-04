package operation;

/**
 * @author Haiqing Tang
 * email: HAITANG@student.unimelb.edu.au
 * student number: 1477462
 */
 
/**
 * Represents an individual ticket for a concert, including details about the
 * seat and pricing.
 */
public class Ticket {
	private int ticketID;
	private int rowNumber;
	private int seatNumber;
	private String zoneType;
	private double price;

	/**
	 * Constructs a new Ticket with specified details.
	 * 
	 * @param ticketID   The unique identifier for the ticket.
	 * @param rowNumber  The row number of the seat.
	 * @param seatNumber The seat number within the row.
	 * @param zoneType   The zone type of the seat (e.g., VIP, General, etc.).
	 * @param price      The price of the ticket.
	 */
	public Ticket(int ticketID, int rowNumber, int seatNumber, String zoneType, double price) {
		this.ticketID = ticketID;
		this.rowNumber = rowNumber;
		this.seatNumber = seatNumber;
		this.zoneType = zoneType;
		this.price = price;
	}
	/**
     * constructors
     */
    public Ticket(){ 
    }

	/**
	 * Gets the ticket ID.
	 * 
	 * @return The unique identifier for this ticket.
	 */
	public int getTicketID() {
		return ticketID;
	}

	/**
	 * Sets the ticket ID.
	 * 
	 * @param ticketID The unique identifier to set for this ticket.
	 */
	public void setTicketID(int ticketID) {
		this.ticketID = ticketID;
	}

	/**
	 * Gets the row number of the seat for this ticket.
	 * 
	 * @return The row number of the seat.
	 */
	public int getRowNumber() {
		return rowNumber;
	}

	/**
	 * Sets the row number of the seat for this ticket.
	 * 
	 * @param rowNumber The row number to set for the seat.
	 */
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	/**
	 * Gets the seat number of this ticket.
	 * 
	 * @return The seat number within its row.
	 */
	public int getSeatNumber() {
		return seatNumber;
	}

	/**
	 * Sets the seat number for this ticket.
	 * 
	 * @param seatNumber The seat number to set within its row.
	 */
	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}

	/**
	 * Gets the zone type of the seat for this ticket.
	 * 
	 * @return The zone type of the seat (e.g., VIP, General, etc.).
	 */
	public String getZoneType() {
		return zoneType;
	}

	/**
	 * Sets the zone type of the seat for this ticket.
	 * 
	 * @param zoneType The zone type to set for the seat (e.g., VIP, General, etc.).
	 */
	public void setZoneType(String zoneType) {
		this.zoneType = zoneType;
	}

	/**
	 * Gets the price of this ticket.
	 * 
	 * @return The price of the ticket.
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Sets the price for this ticket.
	 * 
	 * @param price The price to set for this ticket.
	 */
	public void setPrice(double price) {
		this.price = price;
	}
}

