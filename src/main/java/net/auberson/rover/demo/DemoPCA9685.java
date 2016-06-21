package net.auberson.rover.demo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.auberson.rover.component.Rover;
import net.auberson.rover.component.ServoBoard;

public class DemoPCA9685 {
	private final static Logger LOG = LoggerFactory.getLogger(DemoPCA9685.class);

	private final static int TRUE = 3000;
	private final static int FALSE = 0;

	public static void main(String[] args) throws IOException {
		boolean exit = false;

		int camElevation = 5;
		int camAzimuth = 5;

		float leftSpeed = 0f;
		boolean leftFwd = true;

		LOG.info("Initializing Controller...");
		Rover rover = Rover.getInstance();
		LOG.info("Controller Ready.");
		test(rover);
		LOG.info("Controller Ready. Accepting commands.");

		while (!exit) {
			System.out.print(">");
			char input = (char) System.in.read();
			switch (input) {
			case 'q':
				System.out.println("Exit program");
				exit = true;
				break;
			case 'w':
				rover.setCamElevation(++camElevation / 10f);
				System.out.println("camElevation=0." + camElevation);
				break;
			case 's':
				rover.setCamElevation(--camElevation / 10f);
				System.out.println("camElevation=0." + camElevation);
				break;
			case 'a':
				rover.setCamAzimuth(++camAzimuth / 10f);
				System.out.println("camAzimuth=0." + camAzimuth);
				break;
			case 'd':
				rover.setCamAzimuth(--camAzimuth / 10f);
				System.out.println("camAzimuth=0." + camAzimuth);
				break;
			case 'r':
				if (!leftFwd) {
					leftSpeed = 0;
					leftFwd = true;
				}
				leftSpeed = leftSpeed + .1f;
				rover.setLeftTrack(leftSpeed, leftFwd);
				System.out.println("leftFwd=" + leftSpeed);
				break;
			case 'f':
				if (leftFwd) {
					leftSpeed = 0;
					leftFwd = false;
				}
				leftSpeed = leftSpeed + .1f;
				rover.setLeftTrack(leftSpeed, leftFwd);
				System.out.println("leftBack=" + leftSpeed);
				break;
			case 't':
				test(rover);
				break;
			default:
				break;
			}
		}
	}

	public static void test(Rover rover) {
		LOG.info("PWM test procedure:");

		LOG.info("Testing left track");
		rover.setLeftTrack(1f, true);
		rover.waitfor(10000);
		rover.setLeftTrack(.8f, false);
		rover.waitfor(5000);
		rover.stopLeftTrack(true);
		rover.waitfor(500);

		LOG.info("Testing right track");
		rover.setRightTrack(1f, true);
		rover.waitfor(2500);
		rover.setRightTrack(.8f, false);
		rover.waitfor(1500);
		rover.stopRightTrack(true);
		rover.waitfor(500);

		LOG.info("Testing cam azimuth");
		rover.setCamAzimuth(0f);
		rover.waitfor(500);
		rover.setCamAzimuth(1f);
		rover.waitfor(500);
		rover.setCamAzimuth(0.5f);
		rover.waitfor(500);

		LOG.info("Testing cam elevation");
		rover.setCamElevation(0f);
		rover.waitfor(500);
		rover.setCamElevation(1f);
		rover.waitfor(500);
		rover.setCamElevation(0.5f);
		rover.waitfor(500);

		LOG.info("Test complete.");

	}
}
