package operation;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.*;
import tools.Constants;

/**
 * @author Haiqing Tang
 * email: HAITANG@student.unimelb.edu.au
 * student number: 1477462
 */
 
/**
 * Represents a booking for a concert, containing details about the customer,
 * concert, and purchased tickets.
 */
public class Booking {
	private String bookingID;
	private String customerID;
	private String customerName;
	private String concertID;
	private String totalTickets;
	private double totalPrice;
	private List<Ticket> tickets = new ArrayList<>();
	/**
     * constructors
     */
    public Booking(){ 
    }

	/**
	 * Gets the total price for all the tickets in this booking.
	 *
	 * @return The total price of the booking.
	 */
	public double getTotalPrice() {
		return totalPrice;
	}

	/**
	 * Sets the total price for this booking.
	 *
	 * @param totalPrice The total price to be set for this booking.
	 */
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	/**
	 * Gets the unique booking identifier.
	 *
	 * @return The booking ID.
	 */
	public String getBookingID() {
		return bookingID;
	}

	/**
	 * Sets the booking identifier.
	 *
	 * @param bookingID The unique identifier for the booking.
	 */
	public void setBookingID(String bookingID) {
		this.bookingID = bookingID;
	}

	/**
	 * Gets the customer ID associated with this booking.
	 *
	 * @return The customer's ID.
	 */
	public String getCustomerID() {
		return customerID;
	}

	/**
	 * Sets the customer ID for this booking.
	 *
	 * @param customerID The customer's ID to be associated with this booking.
	 */
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	/**
	 * Gets the customer name associated with this booking.
	 *
	 * @return The customer's name.
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * Sets the customer's name for this booking.
	 *
	 * @param customerName The name of the customer to be associated with this
	 *                     booking.
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * Gets the concert ID associated with this booking.
	 *
	 * @return The concert's ID.
	 */
	public String getConcertID() {
		return concertID;
	}

	/**
	 * Sets the concert ID for this booking.
	 *
	 * @param concertID The concert's ID to be associated with this booking.
	 */
	public void setConcertID(String concertID) {
		this.concertID = concertID;
	}

	/**
	 * Gets the total number of tickets purchased in this booking.
	 *
	 * @return The total number of tickets.
	 */
	public String getTotalTickets() {
		return totalTickets;
	}

	/**
	 * Sets the total number of tickets for this booking.
	 *
	 * @param totalTickets The total number of tickets to be set for the booking.
	 */
	public void setTotalTickets(String totalTickets) {
		this.totalTickets = totalTickets;
	}

	/**
	 * Gets the list of tickets associated with this booking.
	 *
	 * @return A list of tickets.
	 */
	public List<Ticket> getTickets() {
		return tickets;
	}

	/**
	 * Sets the list of tickets for this booking.
	 *
	 * @param tickets A list of tickets to be associated with this booking.
	 */
	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

}
