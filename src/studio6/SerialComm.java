package studio6;

import java.util.Scanner;

import jssc.*;

public class SerialComm {

	SerialPort port;

	private boolean debug; // Indicator of "debugging mode"

	// This function can be called to enable or disable "debugging mode"
	void setDebug(boolean mode) {
		debug = mode;
	}
	
	boolean getDebug() {
		return debug;
	}

	// Constructor for the SerialComm class
	public SerialComm(String name) throws SerialPortException {
		port = new SerialPort(name);
		port.openPort();
		port.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

		debug = false; // Default is to NOT be in debug mode
	}

	// Scans console input and writes it to a serial port
	void writeByte() {
		try {
			Scanner s = new Scanner(System.in);
			s.useDelimiter("");
			String str = "";
			while (s.hasNext()) {
				str = s.next();
				byte b = (byte) str.charAt(0);
				port.writeByte(b);
				if (debug)
					System.out.println(b);
			}
			s.close();
		} catch (Exception e) {
			this.setDebug(true);
			System.out.println(e);
		}
	}
	
	boolean available() {
		try {
			if(port.getInputBufferBytesCount() != 0) {
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			this.setDebug(true);
			System.out.println(e);
			return false;
		}
	}
	
	byte readByte() {
		try {
			byte[] b = new byte[1];
			b = port.readBytes(1);
			return b[0];
		} catch (Exception e) {
			this.setDebug(true);
			System.out.println(e);
			return -1;
		}
	}
	
	public static void main(String[] args) {
		SerialComm a;
		Scanner s = new Scanner(System.in);
		
		try {
			a = new SerialComm("COM4");
			while(true) {
				if(a.available()) {
					if(a.getDebug()) 
						System.out.print((char)(a.readByte()));
					else
						System.out.println(String.format("%02x", a.readByte()));
				}
				if(s.next() != "")
					a.setDebug(!a.getDebug());
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
