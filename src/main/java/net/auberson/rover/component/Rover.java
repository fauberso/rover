package net.auberson.rover.component;

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;

/**
 * Provides access to the Rover's equipment: Servo board, webcams, manipulator,
 * Radioisotope thermal generator...
 */
public class Rover {
	private static Rover instance = null;
	private final ServoBoard servoBoard;

	protected Rover() throws IOException {
		servoBoard = new ServoBoard(I2CBus.BUS_1, 0x6f);
	}

	public static Rover getInstance() throws IOException {
		if (instance == null) {
			instance = new Rover();
		}
		return instance;
	}

	public void waitfor(long howMuch) {
		try {
			Thread.sleep(howMuch);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}
	
	//
	// Actuator methods
	//
	
	public void setCamElevation(float value) {
		servoBoard.setServo(servoBoard.camElevation, value);
	}
	
	public void setCamAzimuth(float value) {
		servoBoard.setServo(servoBoard.camAzimuth, value);
	}
	
	public void setLeftTrack(float value, boolean forwards) {
		servoBoard.set(forwards?servoBoard.leftTrackBackwards:servoBoard.leftTrackForward, servoBoard.RAW_MIN);
		servoBoard.set(forwards?servoBoard.leftTrackForward:servoBoard.leftTrackBackwards, servoBoard.RAW_MAX);
		servoBoard.setFullRange(servoBoard.leftTrackSpeed, value);
	}
	
	public void stopLeftTrack(boolean brake) {
		servoBoard.set(servoBoard.leftTrackForward, servoBoard.RAW_MIN);
		servoBoard.set(servoBoard.leftTrackBackwards, servoBoard.RAW_MIN);
		servoBoard.set(servoBoard.leftTrackSpeed, brake?servoBoard.RAW_MAX:servoBoard.RAW_MIN);
	}

	public void setRightTrack(float value, boolean forwards) {
		servoBoard.set(forwards?servoBoard.rightTrackBackwards:servoBoard.rightTrackForward, servoBoard.RAW_MIN);
		servoBoard.set(forwards?servoBoard.rightTrackForward:servoBoard.rightTrackBackwards, servoBoard.RAW_MAX);
		servoBoard.setFullRange(servoBoard.rightTrackSpeed, value);
	}
	
	public void stopRightTrack(boolean brake) {	
		servoBoard.set(servoBoard.rightTrackForward, servoBoard.RAW_MIN);
		servoBoard.set(servoBoard.rightTrackBackwards, servoBoard.RAW_MIN);
		servoBoard.set(servoBoard.rightTrackSpeed, brake?servoBoard.RAW_MAX:servoBoard.RAW_MIN);
	}
}
