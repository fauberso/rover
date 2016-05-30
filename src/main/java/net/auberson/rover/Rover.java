package net.auberson.rover;

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;

import net.auberson.rover.component.ServoBoard;

/**
 * Provides access to the Rover's equipment: Servo board, webcams, manipulator,
 * Radioisotope thermal generator...
 */
public class Rover {
	private static Rover instance = null;
	private ServoBoard servoBoard;

	protected Rover() throws IOException {
		servoBoard = new ServoBoard(I2CBus.BUS_1, 0x6f);
	}

	public static Rover getInstance() throws IOException {
		if (instance == null) {
			instance = new Rover();
		}
		return instance;
	}

	public ServoBoard getServoBoard() {
		return servoBoard;
	}

	public void waitfor(long howMuch) {
		try {
			Thread.sleep(howMuch);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}
}
