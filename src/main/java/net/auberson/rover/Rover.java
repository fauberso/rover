package net.auberson.rover;

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;

import net.auberson.pi4j.drivers.PCA9685;

/**
 * Provides access to the Rover's equipment: Servo board, webcams, manipulator,
 * Radioisotope thermal generator...
 */
public class Rover {
	private static Rover instance = null;
	private PCA9685 servoBoard;
	
	protected Rover() throws IOException{
		servoBoard = new PCA9685(I2CBus.BUS_1, 0x6f);
	}

	public static Rover getInstance() throws IOException{
		if (instance == null) {
			instance = new Rover();
		}
		return instance;
	}
	
	public PCA9685 getServoBoard() {
		return servoBoard;
	}
}
