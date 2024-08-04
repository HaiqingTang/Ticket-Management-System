package concert;

import java.io.*;
import java.util.*;
import tools.FileLoad;
import exceptions.FileNotFoundException;
import exceptions.IncorrectPasswordException;
import exceptions.InvalidFormatException;
import exceptions.InvalidLineException;
import exceptions.NotFoundException;
import interfacefile.*;
import operation.BookingDetails;
import tools.Constants;

/**
 * @author Haiqing Tang
 * email: HAITANG@student.unimelb.edu.au
 * student number: 1477462
 */

/**
 * Manages the details of concerts including loading concert information from
 * data files. Implements loadInfo interface to load concert details.
 */
public class ConcertDetails implements loadInfo<ConcertDetails> {
	/**
	 * A list of concerts managed by this ConcertDetails instance.
	 */
	private List<Concert> concerts = new ArrayList<>();
	/**
	 * The venue associated with the concerts.
	 */
	private Venue venue;

	/**
	 * Loads concert information from a list of strings, each representing a line
	 * from a file. Each line is expected to contain details for a single concert.
	 * 
	 * @param lines    A list of strings, where each string is a line from a file
	 *                 containing concert details.
	 * @param fileName The name of the file from which the lines are being read.
	 *                 Used for error messages.
	 * @return A new instance of ConcertDetails filled with the loaded concert data.
	 * @throws IOException                     if there's an error reading the file.
	 * @throws FileLoad.InvalidLineException   if a line does not contain valid
	 *                                         concert data.
	 * @throws FileLoad.InvalidFormatException if the format of the data in a line
	 *                                         is incorrect.
	 * @throws FileNotFoundException           if the file specified by fileName
	 *                                         cannot be found.
	 */
	/**
     * constructors
     */
    public ConcertDetails(){ 
    }
	@Override
	public ConcertDetails loadInfo(List<String> lines, String fileName)
			throws IOException, InvalidLineException, InvalidFormatException, FileNotFoundException {
		// Processing the rows of data passed by concert.csv
		ConcertDetails cd = new ConcertDetails();
		FileLoad fileLoad = new FileLoad();
		for (String line : lines) {
			try {
				String[] data = line.split(",");
				// If the data length is abnormal
				if (data.length < Constants.CONCERT_INVALID_NUM) {
					throw new InvalidLineException("Invalid Concert Files. Skipping this line.");
				}
				// Load the data into the respective variables
				fileLoad.validateLine(line, fileName);
				String concertID = data[0].trim();
				char conId = concertID.charAt(0);
				// If the loaded data is incorrect, an exception is thrown
				if (!Character.isDigit(conId)) {
					throw new InvalidFormatException("Concert Id is in incorrect format. Skipping this line.");
				}
				String date = data[1].trim();
				String timing = data[2].trim();
				String artistName = data[3].trim();
				String venueName = data[4].trim();
				String[] vipPrices = data[5].split(":");
				String[] seatingPrices = data[6].split(":");
				String[] standingPrices = data[7].split(":");
				// Creating a Concert Object
				Concert concert = new Concert(concertID, date, timing, artistName, venueName,
						Double.parseDouble(vipPrices[1].trim()), Double.parseDouble(vipPrices[2].trim()),
						Double.parseDouble(vipPrices[3].trim()), Double.parseDouble(seatingPrices[1]),
						Double.parseDouble(seatingPrices[2]), Double.parseDouble(seatingPrices[3]),
						Double.parseDouble(standingPrices[1]), Double.parseDouble(standingPrices[2]),
						Double.parseDouble(standingPrices[3]));
				cd.getConcerts().add(concert);

			} catch (InvalidLineException e) {
				System.out.println(e.getMessage());
			} catch (InvalidFormatException e) {
				System.out.println(e.getMessage());
			}
		}
		return cd;
	}

	/**
	 * Gets the list of concerts.
	 * 
	 * @return A list of concerts managed by this instance.
	 */
	public List<Concert> getConcerts() {
		return concerts;
	}

	/**
	 * Sets the list of concerts.
	 * 
	 * @param concerts A list of Concert objects to be managed by this instance.
	 */
	public void setConcerts(List<Concert> concerts) {
		this.concerts = concerts;
	}

	/**
	 * Gets the venue associated with the concerts.
	 * 
	 * @return The venue associated with the concerts.
	 */
	public Venue getVenue() {
		return venue;
	}

	/**
	 * Sets the venue associated with the concerts.
	 * 
	 * @param venue The venue to be associated with the concerts.
	 */
	public void setVenue(Venue venue) {
		this.venue = venue;
	}
}

