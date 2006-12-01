package org.firstopen.singularity.devicemgr.interrogator;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.zip.CRC32;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.singularity.devicemgr.interrogator.WaveTrend_IO.command;

public class SimpleEventListener implements SerialPortEventListener {

	InputStream inputStream;

	OutputStream outputStream;

	SerialPort serialPort;

	Log log = LogFactory.getLog(this.getClass());

	private HashMap<SimpleEventListener.command, String> commands = new HashMap<SimpleEventListener.command, String>();

	public enum command {
		RESTNET, AUTOPOLL, AUTOPOLLOFF, PING, SETNETID, SETRID, GETTAG, GETRSSI, SETRSSI, SETSITECODE, GETSITECODE, SETRXGAIN, GETRXGAIN, SETALARMFILTER, GETALARMFILTER, GETITAGS, GETVX, STARTRFWNC, GETRFWNR, SETBAUD, GETVER, HEADER, RESPONSE, RFON, RFOFF
	}

	/**
	 * @return Returns the outputStream.
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * @param outputStream
	 *            The outputStream to set.
	 */
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	/**
	 * @return Returns the serialPort.
	 */
	public SerialPort getSerialPort() {
		return serialPort;
	}

	/**
	 * @param serialPort
	 *            The serialPort to set.
	 */
	public void setSerialPort(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	/**
	 * @param port
	 */
	public SimpleEventListener(SerialPort port) {
		super();

		setupCommands();
		serialPort = port;
		try {
			inputStream = serialPort.getInputStream();
			outputStream = serialPort.getOutputStream();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Method declaration
	 * 
	 * 
	 * @param event
	 * 
	 * @see
	 */
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {

		case SerialPortEvent.BI:

		case SerialPortEvent.OE:

		case SerialPortEvent.FE:

		case SerialPortEvent.PE:

		case SerialPortEvent.CD:

		case SerialPortEvent.CTS:

		case SerialPortEvent.DSR:

		case SerialPortEvent.RI:

		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;

		case SerialPortEvent.DATA_AVAILABLE:
			ByteArrayOutputStream message = new ByteArrayOutputStream();
			try {

				/*
				 * Refresh the reader buffer, each call
				 */
				byte[] readBuffer = new byte[64];
				int numBytes = 0;
				while (inputStream.available() > 0) {
					numBytes = inputStream.read(readBuffer);
					message.write(readBuffer, 0, numBytes);
					log.debug("Bytes Read = " + numBytes);
				}
				log.debug(bytesToHex(message.toByteArray()));
				int counter = 0;
				while (true) {
					counter++;

					outputStream
							.write(createResponse(command.RESTNET,
									"212A2A31000E42430000E803D941495400037443330058C020500104000E0A0D"));

					outputStream
							.write(createResponse(command.RESTNET,
									"212A2A31000E42430000E803D941495400037589330058C020500104000E0A0D"));

					outputStream
							.write(createResponse(command.RESTNET,
									"212A2A31000E42430000E803D9414954000375D8330058C020500104000E0A0D"));

					outputStream
							.write(createResponse(command.RESTNET,
									"212A2A31000E42430000E803D94149540003758B330058C020500104000E0A0D"));
					if (counter > 1000) {
						Thread.sleep(20000);
						counter = 0;
					}
				}

			} catch (IOException e) {
				log.error("Failed to read from serial input stream ", e);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

	/**
	 * Convenience method to convert a byte array to a hex string.
	 * 
	 * @param data
	 *            the byte[] to convert
	 * @return String the converted byte[]
	 */
	public String bytesToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			buf.append(byteToHex(data[i]).toUpperCase());
		}
		return (buf.toString());
	}

	/**
	 * method to convert a byte to a hex string.
	 * 
	 * @param data
	 *            the byte to convert
	 * @return String the converted byte
	 */
	public String byteToHex(byte data) {
		StringBuffer buf = new StringBuffer();
		buf.append(toHexChar((data >>> 4) & 0x0F));
		buf.append(toHexChar(data & 0x0F));
		return buf.toString();
	}

	/**
	 * Convenience method to convert an int to a hex char.
	 * 
	 * @param i
	 *            the int to convert
	 * @return char the converted char
	 */
	public char toHexChar(int i) {
		if ((0 <= i) && (i <= 9)) {
			return (char) ('0' + i);
		} else {
			return (char) ('a' + (i - 10));
		}
	}

	/**
	 * @return Returns the inputStream.
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @param inputStream
	 *            The inputStream to set.
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	protected byte[] createResponse(command aCommand, String data) {
		int nodeId = 1;
		int networkId = 4;
		int readerId = 0; // receiverId
		int datalength = 0;
		StringBuffer commandBuffer = new StringBuffer();

		if (data != null) {
			datalength = hexToBuffer(data).length;
		}
		commandBuffer.append(integerToHex(datalength));
		commandBuffer.append(integerToHex(networkId));
		commandBuffer.append(integerToHex(readerId));
		commandBuffer.append(integerToHex(nodeId));
		commandBuffer.append(commands.get(aCommand));
		if (data != null) {

			commandBuffer.append(data);
		}
		commandBuffer
				.append(byteToHex(crc(hexToBuffer(commandBuffer.toString()))));

		String hexString = commands.get(command.RESPONSE).concat(
				commandBuffer.toString());
		log.debug(hexString);
		byte[] returnvalue = hexToBuffer(hexString);
		
		log.debug(returnvalue);
		return returnvalue;

	}

	/**
	 * Returns a byte array built from the byte values represented by the input
	 * hexadecimal string. That is, each pair of hexadecimal characters in the
	 * input string is decoded into the byte value that they represent, and that
	 * byte value is appended to a byte array which is ultimately returned. This
	 * function doesn't care whether the hexadecimal characters are upper or
	 * lower case, and it also can handle odd-length hex strings by assuming a
	 * leading zero digit. If any character in the input string is not a valid
	 * hexadecimal digit, it throws a NumberFormatException, in keeping with the
	 * behavior of Java functions like Integer.parseInt().
	 * 
	 * @param hexString
	 *            a string of hexadecimal characters
	 * @return a byte array built from the bytes indicated by the input string
	 * @throws NumberFormatException
	 */
	public byte[] hexToBuffer(String hexString) throws NumberFormatException {
		int length = hexString.length();
		byte[] buffer = new byte[(length + 1) / 2];
		boolean evenByte = true;
		byte nextByte = 0;
		int bufferOffset = 0;

		// If given an odd-length input string, there is an implicit
		// leading '0' that is not being given to us in the string.
		// In that case, act as if we had processed a '0' first.
		// It's sufficient to set evenByte to false, and leave nextChar
		// as zero which is what it would be if we handled a '0'.
		if ((length % 2) == 1)
			evenByte = false;

		for (int i = 0; i < length; i++) {
			char c = hexString.charAt(i);
			int nibble; // A "nibble" is 4 bits: a decimal 0..15

			if ((c >= '0') && (c <= '9'))
				nibble = c - '0';
			else if ((c >= 'A') && (c <= 'F'))
				nibble = c - 'A' + 0x0A;
			else if ((c >= 'a') && (c <= 'f'))
				nibble = c - 'a' + 0x0A;
			else
				throw new NumberFormatException("Invalid hex digit '" + c
						+ "'.");

			if (evenByte) {
				nextByte = (byte) (nibble << 4);
			} else {
				nextByte += (byte) nibble;
				buffer[bufferOffset++] = nextByte;
			}

			evenByte = !evenByte;
		}

		return buffer;
	}

	/**
	 * Calc CRC-32 with Java method
	 * 
	 * @param b
	 *            byte array to compute CRC on
	 * 
	 * @return 32-bit CRC, signed
	 */
	protected byte crc(byte[] b) {

		// create a new CRC-calculating object

		byte crc = 0;

		// loop, calculating CRC for each byte of the string
		for (int i = 0; i < b.length; i++) {
			crc ^= b[i];
		}
		log.debug(crc);
		return crc;
	}

	protected void setupCommands() {

		char CR = '\r';
		char LF = '\n';
		StringBuffer CRLF = new StringBuffer();
		CRLF.append(CR);
		CRLF.append(LF);

		commands.put(command.RESTNET, "00");
		commands.put(command.AUTOPOLL, "01");
		commands.put(command.AUTOPOLLOFF, "02");
		commands.put(command.PING, "03");
		commands.put(command.SETNETID, "04");
		commands.put(command.SETRID, "05");
		commands.put(command.GETTAG, "06");

		commands.put(command.GETRSSI, "07");
		commands.put(command.SETRSSI, "08");
		commands.put(command.SETSITECODE, "09");
		commands.put(command.GETSITECODE, "0A");
		commands.put(command.SETRXGAIN, "0B");
		commands.put(command.GETRXGAIN, "0C");
		commands.put(command.SETALARMFILTER, "0D");
		commands.put(command.GETALARMFILTER, "0E");
		commands.put(command.GETITAGS, "0F");
		commands.put(command.GETVX, "10");
		commands.put(command.STARTRFWNC, "11");
		commands.put(command.GETRFWNR, "12");
		commands.put(command.SETBAUD, "FE");
		commands.put(command.GETVER, "FF");
		commands.put(command.HEADER, "AA");
		commands.put(command.RESPONSE, "55");

		/*
		 * 0x07 Get RSSI Value Reply Packet + RSSI 0x08 Set RSSI Value Reply
		 * Packet 0x09 Set Site Code Reply Packet 0x0A Get Site Code Reply
		 * Packet + Site Code 0x0B Set Receiver Gain Reply Packet 0x0C Get
		 * Receiver Gain Reply Packet + Gain 0x0D Set Alarm Filter Reply Packet
		 * 0x0E Get Alarm Filter Reply Packet + Status 0x0F Get Number of
		 * invalid Tags Reply Packet + Counter 0x10 Get Supply Voltage Reply
		 * Packet + Voltage 0x11 Start RF white noise calculation Reply Packet
		 * 0x12 Get RF white noise result Reply Packet + Result 0xFE Set Baud
		 * Rate No Reply – Broadcast only 0xFF Get Version Information Reply
		 * Packet + Version Data
		 */

	}

	public String integerToHex(int value) {
		String retVal = Integer.toHexString(value);
		if ((retVal.length() % 2) == 1) {
			retVal = "0" + retVal;
		}

		return retVal;
	}
}
