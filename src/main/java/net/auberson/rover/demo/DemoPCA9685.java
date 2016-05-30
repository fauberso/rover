package net.auberson.rover.demo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.auberson.rover.Rover;
import net.auberson.rover.component.ServoBoard;

public class DemoPCA9685 {
	private final static Logger LOG = LoggerFactory.getLogger(DemoPCA9685.class);

	public static void main(String[] args) {
		LOG.info("PWM test procedure:");
		try {
			LOG.info("Initializing Controller...");
			Rover rover = Rover.getInstance();
			ServoBoard servoBoard = rover.getServoBoard();

			LOG.info("Testing servos");
			for (int i = 0; i < 4; i++) {
				LOG.info("Exercising " + i);
				servoBoard.set(i, 0);
				rover.waitfor(300);
				servoBoard.set(i, 1);
				rover.waitfor(300);
				servoBoard.set(i, 0.5f);
				rover.waitfor(600);
			}
			LOG.info("Test complete.");
		} catch (IOException e) {
			LOG.error("Demo failed:", e);
		}
	}
}
