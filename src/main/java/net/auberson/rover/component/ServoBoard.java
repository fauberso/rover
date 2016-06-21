package net.auberson.rover.component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

public final class ServoBoard {

	private final static Logger LOG = LoggerFactory.getLogger(ServoBoard.class);

	private final PCA9685GpioProvider gpioProvider;
	
	private final List<GpioPinPwmOutput> pwmOutputs = new ArrayList<GpioPinPwmOutput>(32);

	final GpioPinPwmOutput camAzimuth;
	final GpioPinPwmOutput camElevation;

	final GpioPinPwmOutput leftTrackSpeed;
	final GpioPinPwmOutput rightTrackSpeed;

	static final int RAW_MIN = 80;
	static final int SERVO_MIN = 900;
	static final int SERVO_MAX = 2100;
	static final int RAW_MAX = 20400;	

	static final int SERVO_RANGE = SERVO_MAX - SERVO_MIN; // 1200
	static final int SERVO_NEUTRAL = (SERVO_RANGE / 2) + SERVO_MIN; // 1500

	static final int RAW_RANGE = SERVO_MAX - RAW_MIN; // 1200
	static final int RAW_NEUTRAL = (RAW_RANGE / 2) + RAW_MIN; // 1500
	
	public ServoBoard(int busNumber, int deviceAddress) throws IOException {
		LOG.debug("Initializing PCA9685 at Bus " + busNumber + ", device " + deviceAddress);

        // This would theoretically lead into a resolution of 5 microseconds per step: 
        // 4096 Steps (12 Bit) 
        // T = 4096 * 0.000005s = 0.02048s 
        // f = 1 / T = 48.828125 
		BigDecimal frequency = new BigDecimal("48.828");
		
		// Correction factor: actualFreq / targetFreq
		// e.g. measured actual frequency is: 51.69 Hz
		// Calculate correction factor: 51.65 / 48.828 = 1.0578
		// --> To measure actual frequency set frequency without correction
		// factor(or set to 1)
		BigDecimal frequencyCorrectionFactor = new BigDecimal("1.0578");
		
		// Create custom PCA9685 GPIO provider
		I2CBus bus = I2CFactory.getInstance(busNumber);
		gpioProvider = new PCA9685GpioProvider(bus, deviceAddress, frequency, frequencyCorrectionFactor);

		final GpioController gpioController = GpioFactory.getInstance();
		
		// Define outputs and how they are used in the Rover. 
		// Initialise all as PWM outputs, they can't be used in any other mode

		pwmOutputs.add(gpioController.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_00, "Output 01"));
		pwmOutputs.add(gpioController.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_01, "Output 02"));
		pwmOutputs.add(gpioController.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_02, "Output 02"));
		pwmOutputs.add(gpioController.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_03, "Output 03"));
		pwmOutputs.add(gpioController.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_04, "Output 04"));
		pwmOutputs.add(gpioController.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_05, "Output 05"));
		
		camAzimuth = gpioController.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_06, "CamAzimuth");
		pwmOutputs.add(camAzimuth);
		camElevation = gpioController.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_07, "CamElevation");
		pwmOutputs.add(camElevation);
		
		leftTrackSpeed = gpioController.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_08, "LeftTrackSpeed");
		pwmOutputs.add(leftTrackSpeed);
		rightTrackSpeed = gpioController.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_09, "rightTrackSpeed");
		pwmOutputs.add(rightTrackSpeed);
		pwmOutputs.add(gpioController.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_10, "Output 10"));
		pwmOutputs.add(gpioController.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_11, "Output 11"));
		pwmOutputs.add(gpioController.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_12, "Output 12"));
		pwmOutputs.add(gpioController.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_13, "Output 13"));
		pwmOutputs.add(gpioController.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_14, "Output 14"));
		pwmOutputs.add(gpioController.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_15, "Output 15"));
		
		// Reset outputs
		gpioProvider.reset();
	}
	
	void setServo(GpioPinPwmOutput output, float value) {
		output.setPwm(Math.round(SERVO_RANGE * value) + SERVO_MIN);
	}
	
	void setFullRange(GpioPinPwmOutput output, float value) {
		output.setPwm(Math.round(RAW_RANGE * value) + RAW_MIN);
	}
	
	void setDuration(GpioPinPwmOutput output, int duration) {
		output.setPwm(duration);
	}

}
