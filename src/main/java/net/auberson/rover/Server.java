package net.auberson.rover;

import java.io.IOException;

import net.auberson.rover.demo.DemoPCA9685;

/**
 * Main server class for the Rover's mainboard. Automatically started on
 * deployment. For now, this is only used to start one of the test programs, it
 * will later contain the actual server.
 */
public class Server {

	public static void main(String[] args) throws IOException {
		DemoPCA9685.main(args);
	}

}
