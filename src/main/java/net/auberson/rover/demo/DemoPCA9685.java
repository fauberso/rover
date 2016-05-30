package net.auberson.rover.demo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.auberson.pi4j.drivers.PCA9685;
import net.auberson.rover.Rover;

public class DemoPCA9685 {
	private final static Logger LOG = LoggerFactory.getLogger(DemoPCA9685.class);

	public static void main(String[] args) {
		LOG.info("PWM test procedure:");
		try {
			LOG.info("Initializing Controller...");
			PCA9685 servoBoard = Rover.getInstance().getServoBoard();
			LOG.info("Setting PWM Frequency...");
			servoBoard.setPWMFreq(60); // Set frequency to 60 Hz
			int servoMin = 150; // Min pulse length out of 4096
			int servoMax = 600; // Max pulse length out of 4096

			final int SERVO_CHANNEL = 1;

			LOG.info("Testing servos");
			for (int i = 0; i < 10; i++) {
				LOG.info("Testing servo" + i);
				servoBoard.setPWM(SERVO_CHANNEL, 0, servoMin);
				servoBoard.waitfor(1000);
				servoBoard.setPWM(SERVO_CHANNEL, 0, servoMax);
				servoBoard.waitfor(1000);
			}
			LOG.info("Resetting servo...");
			servoBoard.setPWM(SERVO_CHANNEL, 0, 0);
			LOG.info("Test complete.");
		} catch (IOException e) {
			LOG.error("Demo failed:", e);
		}
	}
}
