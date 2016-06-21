package net.auberson.rover.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinOutput;
import com.pi4j.io.gpio.RaspiPin;

public final class PiGpio {

	private final static Logger LOG = LoggerFactory.getLogger(PiGpio.class);

	private final List<GpioPinOutput> outputs = new ArrayList<GpioPinOutput>(32);

	final GpioPinDigitalOutput leftFwd;
	final GpioPinDigitalOutput leftRev;
	final GpioPinDigitalOutput rightRev;
	final GpioPinDigitalOutput rightFwd;
	
	public PiGpio() throws IOException {
		LOG.debug("Initializing On-board GPIO Pins");
		final GpioController gpioController = GpioFactory.getInstance();

		// Define outputs and how they are used in the Rover.
		// Initialize all pins that are actually accessible on the header, note
		// where the reserved ones are used

		leftFwd = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00, "H-Bridge IN1");
		outputs.add(leftFwd);
		leftRev = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_01, "H-Bridge IN2");
		outputs.add(leftRev);
		rightRev = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_02, "H-Bridge IN3");
		outputs.add(rightRev);
		rightFwd = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_03, "H-Bridge IN4");
		outputs.add(rightFwd);

		outputs.add(gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Output 04 / Pin 16"));
		outputs.add(gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Output 05 / Pin 18"));

		outputs.add(gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_06, "Output 06 / Pin 22"));
		outputs.add(gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_10, "Output 10 / Pin 24"));
		outputs.add(gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_11, "Output 11 / Pin 26"));

		outputs.add(gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_07, "Output 07 / Pin 07"));
		
		// RaspiPin.GPIO_08 reserved: In use for I2C Bus 0 (SDA)
		// RaspiPin.GPIO_09 reserved: In use for I2C Bus 0 (SCL)		

		outputs.add(gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_12, "Output 12 / Pin 19"));
		outputs.add(gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_13, "Output 13 / Pin 21"));
		outputs.add(gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_14, "Output 14 / Pin 23"));

		// Also used as UART:
		outputs.add(gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_15, "Output 15 / Pin 08"));
		outputs.add(gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_16, "Output 16 / Pin 10"));

	}

}
