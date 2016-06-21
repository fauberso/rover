package net.auberson.rover.demo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.auberson.rover.Rover;
import net.auberson.rover.component.ServoBoard;

public class DemoPCA9685 {
	private final static Logger LOG = LoggerFactory.getLogger(DemoPCA9685.class);

	private final static int TRUE = 3000;
	private final static int FALSE = 0;

	public static void main(String[] args) throws IOException {
		boolean exit = false;

		int camElevation = 15;
		int camAzimuth = 15;

		float leftSpeed = 0f;
		boolean leftFwd = true;

		LOG.info("Initializing Controller...");
		Rover rover = Rover.getInstance();
		ServoBoard servoBoard = rover.getServoBoard();
		LOG.info("Controller Ready.");
		test(rover, servoBoard);
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
				servoBoard.set(servoBoard.camElevation, ++camElevation * 100);
				System.out.println("camElevation=" + camElevation);
				break;
			case 's':
				servoBoard.set(servoBoard.camElevation, --camElevation * 100);
				System.out.println("camElevation=" + camElevation);
				break;
			case 'a':
				servoBoard.set(servoBoard.camAzimuth, ++camAzimuth * 100);
				System.out.println("camAzimuth=" + camAzimuth);
				break;
			case 'd':
				servoBoard.set(servoBoard.camAzimuth, --camAzimuth * 100);
				System.out.println("camAzimuth=" + camAzimuth);
				break;
			case 'r':
				if (!leftFwd) {
					leftSpeed = 0;
					leftFwd = true;
				}
				leftSpeed = leftSpeed + .1f;
				servoBoard.setLeftTrack(leftSpeed, leftFwd);
				System.out.println("leftFwd=" + leftSpeed);
				break;
			case 'f':
				if (leftFwd) {
					leftSpeed = 0;
					leftFwd = false;
				}
				leftSpeed = leftSpeed + .1f;
				servoBoard.setLeftTrack(leftSpeed, leftFwd);
				System.out.println("leftBack=" + leftSpeed);
				break;
			case 't':
				test(rover, servoBoard);
				break;
			default:
				break;
			}
		}
	}

	public static void test(Rover rover, ServoBoard servoBoard) {
		LOG.info("PWM test procedure:");

		LOG.info("Testing left track");
		servoBoard.setLeftTrack(1f, true);
		rover.waitfor(10000);
		servoBoard.setLeftTrack(.8f, false);
		rover.waitfor(5000);
		servoBoard.stopLeftTrack(true);
		rover.waitfor(500);

		LOG.info("Testing right track");
		servoBoard.setRightTrack(1f, true);
		rover.waitfor(2500);
		servoBoard.setRightTrack(.8f, false);
		rover.waitfor(1500);
		servoBoard.stopRightTrack(true);
		rover.waitfor(500);

		LOG.info("Testing cam azimuth");
		servoBoard.setServo(servoBoard.camAzimuth, 0f);
		rover.waitfor(500);
		servoBoard.setServo(servoBoard.camAzimuth, 1f);
		rover.waitfor(500);
		servoBoard.setServo(servoBoard.camAzimuth, 0.5f);
		rover.waitfor(500);

		LOG.info("Testing cam elevation");
		servoBoard.setServo(servoBoard.camElevation, 0f);
		rover.waitfor(500);
		servoBoard.setServo(servoBoard.camElevation, 1f);
		rover.waitfor(500);
		servoBoard.setServo(servoBoard.camElevation, 0.5f);
		rover.waitfor(500);

		LOG.info("Test complete.");

	}
}
