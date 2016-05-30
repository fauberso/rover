package net.auberson.pi4j.drivers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class PCA9685 {
	// Register Definitions as per PCA9685 Datasheet, Chapter 7.3:
	public final static int MODE1 = 0;
	public final static int MODE2 = 1;

	public final static int SUBADR1 = 2;
	public final static int SUBADR2 = 3;
	public final static int SUBADR3 = 4;

	public final static int ALLCALLADR = 5;

	public final static int LED0_ON_L = 6;
	public final static int LED0_ON_H = 7;
	public final static int LED0_OFF_L = 8;
	public final static int LED0_OFF_H = 9;
	public final static int LED1_ON_L = 10;
	public final static int LED1_ON_H = 11;
	public final static int LED1_OFF_L = 12;
	public final static int LED1_OFF_H = 13;
	public final static int LED2_ON_L = 14;
	public final static int LED2_ON_H = 15;
	public final static int LED2_OFF_L = 16;
	public final static int LED2_OFF_H = 17;
	public final static int LED3_ON_L = 18;
	public final static int LED3_ON_H = 19;
	public final static int LED3_OFF_L = 20;
	public final static int LED3_OFF_H = 21;
	public final static int LED4_ON_L = 22;
	public final static int LED4_ON_H = 23;
	public final static int LED4_OFF_L = 24;
	public final static int LED4_OFF_H = 25;
	public final static int LED5_ON_L = 26;
	public final static int LED5_ON_H = 27;
	public final static int LED5_OFF_L = 28;
	public final static int LED5_OFF_H = 29;
	public final static int LED6_ON_L = 30;
	public final static int LED6_ON_H = 31;
	public final static int LED6_OFF_L = 32;
	public final static int LED6_OFF_H = 33;
	public final static int LED7_ON_L = 34;
	public final static int LED7_ON_H = 35;
	public final static int LED7_OFF_L = 36;
	public final static int LED7_OFF_H = 37;
	public final static int LED8_ON_L = 38;
	public final static int LED8_ON_H = 39;
	public final static int LED8_OFF_L = 40;
	public final static int LED8_OFF_H = 41;
	public final static int LED9_ON_L = 42;
	public final static int LED9_ON_H = 43;
	public final static int LED9_OFF_L = 44;
	public final static int LED9_OFF_H = 45;
	public final static int LED10_ON_L = 46;
	public final static int LED10_ON_H = 47;
	public final static int LED10_OFF_L = 48;
	public final static int LED10_OFF_H = 49;
	public final static int LED11_ON_L = 50;
	public final static int LED11_ON_H = 51;
	public final static int LED11_OFF_L = 52;
	public final static int LED11_OFF_H = 53;
	public final static int LED12_ON_L = 54;
	public final static int LED12_ON_H = 55;
	public final static int LED12_OFF_L = 56;
	public final static int LED12_OFF_H = 57;
	public final static int LED13_ON_L = 58;
	public final static int LED13_ON_H = 59;
	public final static int LED13_OFF_L = 60;
	public final static int LED13_OFF_H = 61;
	public final static int LED14_ON_L = 62;
	public final static int LED14_ON_H = 63;
	public final static int LED14_OFF_L = 64;
	public final static int LED14_OFF_H = 65;
	public final static int LED15_ON_L = 66;
	public final static int LED15_ON_H = 67;
	public final static int LED15_OFF_L = 68;
	public final static int LED15_OFF_H = 69;

	public final static int ALL_LED_ON_L = 250;
	public final static int ALL_LED_ON_H = 251;
	public final static int ALL_LED_OFF_L = 252;
	public final static int ALL_LED_OFF_H = 253;

	public final static int PRESCALE = 254;

	public final static int TESTMODE = 255;

	private I2CBus bus;
	private I2CDevice device;

	private final static Logger LOG = LoggerFactory.getLogger(PCA9685.class);

	/*
	 * Check out the /dev directory to see which I2C buses exist, e.g.: pi@rover
	 * ~ $ ls /dev/i2c-1 /dev/i2c-1
	 */

	/*
	 * Use i2cdetect to discover address, e.g.: pi@rover ~ $ sudo i2cdetect -y 1
	 * 0 1 2 3 4 5 6 7 8 9 a b c d e f 00: -- -- -- -- -- -- -- -- -- -- -- --
	 * -- 10: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 20: -- -- -- -- --
	 * -- -- -- -- -- -- -- -- -- -- -- 30: -- -- -- -- -- -- -- -- -- -- -- --
	 * -- -- -- -- 40: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 50: -- --
	 * -- -- -- -- -- -- -- -- -- -- -- -- -- -- 60: -- -- -- -- -- -- -- -- --
	 * -- -- -- -- -- -- 6f 70: 70 -- -- -- -- -- -- --
	 */

	public PCA9685(int busNumber, int deviceAddress) throws IOException {
		bus = I2CFactory.getInstance(busNumber);
		device = bus.getDevice(deviceAddress);
		device.write(MODE1, (byte) 0x00);
	}

	public void setPWMFreq(int freq) {
		float prescaleval = 25000000.0f; // 25MHz
		prescaleval /= 4096.0; // 12-bit
		prescaleval /= freq;
		prescaleval -= 1.0;

			LOG.debug("Setting PWM frequency to " + freq + " Hz");
			LOG.debug("Estimated pre-scale: " + prescaleval);
		
		double prescale = Math.floor(prescaleval + 0.5);
			LOG.debug("Final pre-scale: " + prescale);

		try {
			byte oldmode = (byte) device.read(MODE1);
			byte newmode = (byte) ((oldmode & 0x7F) | 0x10); // sleep
			device.write(MODE1, newmode); // go to sleep
			device.write(PRESCALE, (byte) (Math.floor(prescale)));
			device.write(MODE1, oldmode);
			waitfor(5);
			device.write(MODE1, (byte) (oldmode | 0x80));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void setPWM(int channel, int on, int off) {
		try {
			device.write(LED0_ON_L + 4 * channel, (byte) (on & 0xFF));
			device.write(LED0_ON_H + 4 * channel, (byte) (on >> 8));
			device.write(LED0_OFF_L + 4 * channel, (byte) (off & 0xFF));
			device.write(LED0_OFF_H + 4 * channel, (byte) (off >> 8));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void waitfor(long howMuch) {
		try {
			Thread.sleep(howMuch);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}

	public void setServoPulse(int channel, int pulse) {
		int pulseLength = 1000000; // 1,000,000 us per second
		pulseLength /= 60; // 60 Hz
			LOG.debug(pulseLength + " us per period");
		pulseLength /= 4096; // 12 bits of resolution
			LOG.debug(pulseLength + " us per bit");
		pulse *= 1000;
		pulse /= pulseLength;
		this.setPWM(channel, 0, pulse);
	}
}
