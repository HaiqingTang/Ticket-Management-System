package user;

import concert.ConcertDetails;
import operation.BookingDetails;

/**
 * @author Haiqing Tang
 * email: HAITANG@student.unimelb.edu.au
 * student number: 1477462
 */

/**
 * Abstract base class for users of the booking system.
 * This class defines the common interface that all user types must implement,
 * ensuring they provide their own main menu functionality.
 */
public abstract class User {

    /**
     * Displays the main menu specific to the type of user. This method must be implemented by all subclasses
     * to tailor the main menu interface according to the user's role (e.g., Admin, Customer).
     */
    public abstract void mainMenu();
	/**
     * constructors
     */
    public User(){ 
    }

    /**
     * Displays a welcome menu that lists concert details and booking information. This method provides
     * a generic implementation that can be used or overridden by subclasses if specific behavior is needed.
     *
     * @param concertDetails Details of concerts to be displayed in the welcome menu.
     * @param bookingDetails Booking details to be included in the welcome menu.
     */
    public void welcomeMenu(ConcertDetails concertDetails, BookingDetails bookingDetails) {
        // Default implementation can be overridden by subclasses to provide specific functionality.
    }
}

