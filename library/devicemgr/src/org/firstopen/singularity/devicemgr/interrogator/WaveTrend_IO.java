/*
 * Copyright 2006 i-Konect LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.firstopen.singularity.devicemgr.interrogator;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.TooManyListenersException;
import java.util.zip.CRC32;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.singularity.config.DeviceProfile;
import org.firstopen.singularity.devicemgr.DeviceManagerTest;
import org.firstopen.singularity.system.ReaderEvent;
import org.firstopen.singularity.system.Sensor;
import org.firstopen.singularity.system.Shutdown;
import org.firstopen.singularity.system.ShutdownManager;
import org.firstopen.singularity.system.Tag;
import org.firstopen.singularity.util.InfrastructureException;

/**
 * Class declaration
 * 
 * @author Tom Rose
 * @version $Id: IPSADC_IPICO_IO.java 1242 2006-01-14 03:34:08Z TomRose $
 * 
 * 
 */
public class WaveTrend_IO implements SerialPortEventListener, Interrogator,
		Shutdown {

	private CommPortIdentifier portId;

	private Enumeration portList;

	protected InputStream inputStream;

	protected OutputStream outputStream;

	private SerialPort serialPort;

	protected ByteArrayOutputStream message = new ByteArrayOutputStream();

	private Log log = LogFactory.getLog(this.getClass());

	private DeviceProfile deviceProfile = null;

	private boolean commandline = false;

	private HashMap<WaveTrend_IO.command, String> commands = new HashMap<WaveTrend_IO.command, String>();

	protected final String RFON = "RFON";

	protected final String RFOFF = "RFOFF";

	protected int MAX_RECORD_LENGTH = 71;

	public enum command {
		RESTNET, AUTOPOLL, AUTOPOLLOFF, PING, SETNETID, SETRID, GETTAG, GETRSSI, SETRSSI, SETSITECODE, GETSITECODE, SETRXGAIN, GETRXGAIN, SETALARMFILTER, GETALARMFILTER, GETITAGS, GETVX, STARTRFWNC, GETRFWNR, SETBAUD, GETVER, HEADER, RESPONSE, RFON, RFOFF
	}

	private static final char kHexChars[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	protected InterrogatorIO interrogatorIO;

	private int nodeId;

	private int networkId;

	private int readerId;

	/**
	 * @return Returns the kHexChars.
	 */
	public static char[] getKHexChars() {
		return kHexChars;
	}

	/**
	 * @return Returns the commandline.
	 */
	public boolean isCommandline() {
		return commandline;
	}

	/**
	 * @param commandline The commandline to set.
	 */
	public void setCommandline(boolean commandline) {
		this.commandline = commandline;
	}

	/**
	 * @return Returns the commands.
	 */
	public HashMap<WaveTrend_IO.command, String> getCommands() {
		return commands;
	}

	/**
	 * @param commands The commands to set.
	 */
	public void setCommands(HashMap<WaveTrend_IO.command, String> commands) {
		this.commands = commands;
	}

	/**
	 * @return Returns the inputStream.
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @param inputStream The inputStream to set.
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * @return Returns the log.
	 */
	public Log getLog() {
		return log;
	}

	/**
	 * @param log The log to set.
	 */
	public void setLog(Log log) {
		this.log = log;
	}

	/**
	 * @return Returns the mAX_RECORD_LENGTH.
	 */
	public int getMAX_RECORD_LENGTH() {
		return MAX_RECORD_LENGTH;
	}

	/**
	 * @param max_record_length The mAX_RECORD_LENGTH to set.
	 */
	public void setMAX_RECORD_LENGTH(int max_record_length) {
		MAX_RECORD_LENGTH = max_record_length;
	}

	/**
	 * @return Returns the message.
	 */
	public ByteArrayOutputStream getMessage() {
		return message;
	}

	/**
	 * @param message The message to set.
	 */
	public void setMessage(ByteArrayOutputStream message) {
		this.message = message;
	}

	/**
	 * @return Returns the outputStream.
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * @param outputStream The outputStream to set.
	 */
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	/**
	 * @return Returns the portId.
	 */
	public CommPortIdentifier getPortId() {
		return portId;
	}

	/**
	 * @param portId The portId to set.
	 */
	public void setPortId(CommPortIdentifier portId) {
		this.portId = portId;
	}

	/**
	 * @return Returns the portList.
	 */
	public Enumeration getPortList() {
		return portList;
	}

	/**
	 * @param portList The portList to set.
	 */
	public void setPortList(Enumeration portList) {
		this.portList = portList;
	}

	/**
	 * @return Returns the rFOFF.
	 */
	public String getRFOFF() {
		return RFOFF;
	}

	/**
	 * @return Returns the rFON.
	 */
	public String getRFON() {
		return RFON;
	}

	/**
	 * @return Returns the serialPort.
	 */
	public SerialPort getSerialPort() {
		return serialPort;
	}

	/**
	 * @param serialPort The serialPort to set.
	 */
	public void setSerialPort(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	/**
	 * @return Returns the interrogatorIO.
	 */
	public InterrogatorIO getInterrogatorIO() {
		return interrogatorIO;
	}

	/**
	 * Main Method declaration for testing
	 * 
	 * @param args
	 * @throws Exception
	 * 
	 * @see
	 */
	public static void main(String[] args) {

		Log log = LogFactory.getLog("WaveTrend_IO");
		String defaultPort = null;
		String defaultBaud = null;

		char CR = '\r';
		char LF = '\n';
		StringBuffer CRLF = new StringBuffer();
		CRLF.append(CR);
		CRLF.append(LF);
		boolean useSimple = false;

		for (int i = 0; i < args.length; i++) {
			switch (i) {
			case 0:
				useSimple = Boolean.parseBoolean(args[i]);
			case 1:
				defaultPort = args[i];
				break;
			case 2:
				defaultBaud = args[i];
				break;

			}
		}// end for
		ShutdownManager sdm = new ShutdownManager();
		Runtime.getRuntime().addShutdownHook(sdm);

		try {
			Properties properties = WaveTrend_IO
					.getProperties("devicemgr.properties");
			if (defaultPort == null) {
			defaultPort = properties
					.getProperty("org.firstopen.singularity.devicemgr.commport");
			}
			
			if (defaultBaud == null) {
			defaultBaud = properties
					.getProperty("org.firstopen.singularity.devicemgr.baud");
			}

			DeviceProfile deviceProfile = new DeviceProfile("Dummy");

			deviceProfile.setPort(defaultPort);
			deviceProfile.setBaud(defaultBaud);
			Set<Sensor> sensors = new HashSet<Sensor>();
			sensors.add(new Sensor("embeddedAntenna"));
			deviceProfile.setSensorSet(sensors);

			InterrogatorIO interrogator = new DeviceManagerTest();
			
			WaveTrend_IO reader = new WaveTrend_IO(deviceProfile);
			reader.setInterrogatorIO(interrogator);

			reader
					.setNetworkId(Integer
							.parseInt(properties
									.getProperty("org.firstopen.singularity.devicemgr.networkId")));
			reader
					.setNodeId(Integer
							.parseInt(properties
									.getProperty("org.firstopen.singularity.devicemgr.nodeId")));
			reader
					.setReaderId(Integer
							.parseInt(properties
									.getProperty("org.firstopen.singularity.devicemgr.readerId")));

			reader.commandline = useSimple;

			reader.run();

			if (reader.serialPort == null) {
				throw new InfrastructureException(
						"cannot connect to  serial port");
			}
			if (reader.commandline == true) {
				reader.serialPort.addEventListener(new SimpleEventListener(
						reader.serialPort));
			}
			OutputStream outputStream = reader.serialPort.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));

			command[] commandlist = command.values();

			String commandLine = null;

			/*
			 * for(command aCommand : commandlist) { System.out.print(aCommand); }
			 */
			ShutdownManager.addManagedObject(reader);
			byte[] msgBuf = null;
			while (true) {
				try {
					System.out.print("enter command > ");
					commandLine = br.readLine();
					if (commandLine != null
							&& !commandLine.equalsIgnoreCase("exit")) {

						int commandNumber = Integer.parseInt(commandLine
								.toString());

						reader.log.debug("commandNumber is " + commandNumber);
						msgBuf = reader.createCommand(
								commandlist[commandNumber], null);

						outputStream.write(msgBuf);
						outputStream.flush();
					} else {
						System.exit(0);
					}

				} catch (NumberFormatException e) {
					log.error("Please enter a number value");
				} catch (Exception e) {
					log.error(e);
				}
			}

		} catch (Exception e) {

			log.error("failed", e);
		}
	}

	/**
	 * Constructor declaration
	 * 
	 * 
	 * @see
	 */
	public WaveTrend_IO() {
		setupCommands();
	}

	/**
	 * Constructor declaration
	 * 
	 * 
	 * @see
	 */
	public WaveTrend_IO(DeviceProfile deviceProfile) {
		this();
		this.setDeviceProfile(deviceProfile);

	}

	public void run() {
		try {

			String defaultPort = deviceProfile.getPort();
			int defaultBaud = Integer.parseInt(deviceProfile.getBaud());

			boolean portFound = false;

			portList = CommPortIdentifier.getPortIdentifiers();

			while (portList.hasMoreElements()) {

				portId = (CommPortIdentifier) portList.nextElement();
				log.debug("Found  port: " + portId.getName());
				if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					if (portId.getName().equals(defaultPort)) {
						log.debug("Found port match: " + defaultPort);
						portFound = true;
						break;

					}
				}
			}
			if (!portFound) {
				log.debug("port " + defaultPort + " not found.");
			}

			else {

				serialPort = (SerialPort) portId.open(
						this.getClass().getName(), 2000);

				inputStream = serialPort.getInputStream();

				outputStream = serialPort.getOutputStream();

				if (commandline == false)
					serialPort.addEventListener(this);

				serialPort.notifyOnDataAvailable(true);

				serialPort.setSerialPortParams(defaultBaud,
						SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				switchRF(command.AUTOPOLL);
			}
			/*
			 * register with shutdown manager
			 */
			ShutdownManager.addManagedObject(this);

		} catch (UnsupportedCommOperationException e) {
			log.error("unable to intatiate Interrogator ", e);
		} catch (TooManyListenersException e) {
			log.error("unable to intatiate Interrogator ", e);
		} catch (PortInUseException e) {
			log.error("unable to intatiate Interrogator ", e);
		} catch (IOException e) {
			log.error("unable to intatiate Interrogator ", e);
		}

	}

	/**
	 * Method declaration
	 * 
	 * 
	 * @param serialEvent
	 * @throws Exception
	 * 
	 * @see
	 */
	public void serialEvent(SerialPortEvent serialEvent) {
		switch (serialEvent.getEventType()) {

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

			try {

				/*
				 * Refresh the reader buffer, each call
				 */
				byte[] readBuffer = new byte[72];
				int numBytes = 0;
				while (inputStream.available() > 0) {
					numBytes = inputStream.read(readBuffer);
					message.write(readBuffer, 0, numBytes);
					log.debug("Bytes Read = " + numBytes);

				}
			} catch (IOException e) {
				log.error("Failed to read from serial input stream ", e);
			}
			log.debug("message is: " + bytesToHex(message.toByteArray()));
			/*
			 * see if there are any complete messages found in teh output stream
			 * (message)
			 */
			ArrayList<byte[]> messageList = findMessage(message);

			log.debug("message size is :" + messageList.size());

			if (messageList.size() > 0) {

				try {
					/*
					 * decode a list of messages multipe ecodings can exists
					 * this one asumes the ascii encoding.
					 */
					ArrayList<Tag> tagList = decodeData(messageList);

					Sensor sensor = deviceProfile.getSensorSet().iterator()
							.next();

					ReaderEvent event = new ReaderEvent(sensor);

					event.setTagIds(tagList);

					event.setReaderName(deviceProfile.getDeviceProfileID());

					interrogatorIO.sendEvent(event);

				} catch (InterrogatorException e) {
					log.error("undable to decode message, info ignored", e);
				} catch (Exception e) {
					log.error("unable to send event to Device Manager", e);
				}
			}// end if

			break;
		}// end switch
	}// end SerialEvent

	/**
	 * Header 1 Byte [0x55] 10. Length 1 Byte (Number of Bytes in data section)
	 * 11. Network ID 1 Byte 12. Receiver ID 1 Byte 13. Node ID 1 Byte 14.
	 * Command 1 Byte 15. Data Up to 64 Bytes of Data 16. Checksum 1 Byte (XOR
	 * from Length to Last Data Byte), CRC
	 * 
	 * @param message
	 * @return messageList
	 */
	protected ArrayList<byte[]> findMessage(ByteArrayOutputStream message) {

		ArrayList<byte[]> messageList = new ArrayList<byte[]>();

		synchronized (message) {

			// log.debug("message is " + message + " record length = " +
			// MAX_RECORD_LENGTH);

			byte[] buffer = message.toByteArray();

			message.reset();
			log.debug("found message is ->" + bytesToHex(buffer));
			for (int i = 0; i < buffer.length - 1; i++) {

				if (byteToHex(buffer[i]).equals(commands.get(command.RESPONSE))) {
					int dataLength = Integer.valueOf(byteToHex(buffer[i + 1]),
							16);
					/*
					 * if message is not complete place it back in the origianl
					 * message buffer
					 */
					if (i + 7 + dataLength - 1 > buffer.length) {
						message.write(buffer, i, buffer.length - i);
						break;
					} else {
						byte[] messageBuf = new byte[dataLength + 7];
						System.arraycopy(buffer, i, messageBuf, 0,
								dataLength + 7);
						log.debug("message found, and added to list -> "
								+ bytesToHex(messageBuf));
						messageList.add(messageBuf);
					}

				}// end if start message found
			}
		}
		return messageList;

	}

	/**
	 * 
	 * @throws InterrogatorException
	 * @throws Exception
	 * @throws Exception
	 */
	public ArrayList<Tag> decodeData(ArrayList<byte[]> messageList)
			throws InterrogatorException {

		ArrayList<Tag> tagList = new ArrayList<Tag>();

		for (int j = 0; j < messageList.size(); j++) {

			byte[] buf = messageList.get(j);

			byte[] checkBuf = new byte[buf.length - 2];

			/*
			 * length (byte2) through data (buflen-1)
			 */
			System.arraycopy(buf, 1, checkBuf, 0, buf.length - 2);

			byte crc = buf[buf.length - 1];
			byte calccrc = crcXor(checkBuf);

			if (crc != calccrc) {
				log.debug("checkBuf = " + bufferToHex(checkBuf));
				throw new InterrogatorException(
						"invalid message from reader, CRC error. crc = " + crc + "  , calculated = " + calccrc );
			}

			if (!byteToHex(buf[0]).equals(commands.get(command.RESPONSE))) {
				throw new InterrogatorException(
						"invalid message from reader, wrong header");
			}

			int dataLength = Integer.valueOf(byteToHex(buf[1]), 16);

			String networkId = byteToHex(buf[2]);
			String readerId = byteToHex(buf[3]);
			String nodeId = byteToHex(buf[4]);
			String command = byteToHex(buf[5]);

			String data = null;
			if (dataLength > 0) {
				data = bufferToHex(buf, 6, dataLength);
			}
			if ( data != null) {
			byte[] tagId = new byte[4];
			
			System.arraycopy(buf,23,tagId,0,4);
			
			log.debug("networkId = " + networkId);
			log.debug("readerId = " + readerId);
			log.debug("nodeId = " + nodeId);
			log.debug("command = " + command);
			log.debug("data length = " + dataLength);
			log.debug("tag Data = " + bufferToHex(tagId, 0, 4));

		
			Tag currentTag = new Tag();
			currentTag.setValue(bufferToHex(tagId, 0, 4));
			tagList.add(currentTag);
			currentTag.setCount(1);
			}

		}// end for all messages
		return tagList;
	}

	public void setDeviceProfile(DeviceProfile deviceProfile) {
		this.deviceProfile = deviceProfile;

	}

	public DeviceProfile getDeviceProfile() {
		return deviceProfile;
	}

	public void setInterrogatorIO(InterrogatorIO interrogatorIO) {
		this.interrogatorIO = interrogatorIO;

	}

	public synchronized void on() {
		if (serialPort == null) {
			run();
		} else {
			log.warn("Interorgator is already running");
			switchRF(command.AUTOPOLL);
		}

	}

	public synchronized void off() {
		if (serialPort != null) {
			switchRF(command.AUTOPOLLOFF);
		}

	}

	protected synchronized void switchRF(command command) {

		try {
			outputStream.write(getCommand(command));
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			log.error("Unable to send commands to Reader");
		}
	}

	/**
	 * 
	 * @param command
	 * @return
	 */

	public byte[] getCommand(command commandName) {

		return commands.get(commandName).getBytes();

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

	/**
	 * cleans up when device manager is shutdown
	 */
	public boolean shutdown() {

		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
		return true;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#queryRep()
	 */
	public void queryRep() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#ack()
	 */
	public void ack() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#query()
	 */
	public void query() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#queryAdjust()
	 */
	public void queryAdjust() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#select()
	 */
	public void select() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#nak()
	 */
	public void nak() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#reqRN()
	 */
	public void reqRN() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#read()
	 */
	public void read() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#write()
	 */
	public void write() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#kill()
	 */
	public void kill() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#lock()
	 */
	public void lock() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#access()
	 */
	public void access() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#blockWrite()
	 */
	public void blockWrite() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#blockErase()
	 */
	public void blockErase() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#getDescription()
	 */
	public static String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	protected byte[] toBinArray(String hexStr) {
		byte bArray[] = new byte[hexStr.length() / 2];
		for (int i = 0; i < bArray.length; i++) {
			bArray[i] = Byte.parseByte(hexStr.substring(2 * i, 2 * i + 2), 16);
		}
		return bArray;
	}

	/**
	 * Calc CRC-32 with Java method
	 * 
	 * @param b
	 *            byte array to compute CRC on
	 * 
	 * @return 32-bit CRC, signed
	 */
	protected byte crcXor(byte[] b) {
//		 create a new CRC-calculating object
	
		 byte crc = 0;
		 
		// loop, calculating CRC for each byte of the string
		for (int i = 0; i < b.length; i++) {
			crc ^= b[i];
		}
		log.debug(crc);
		return crc;
	}

	public byte[] createCommand(command aCommand, String data) {
		if (aCommand == null) {
			throw new InfrastructureException("Cannot have a null command");
		}

		log.debug(aCommand);
		int datalength = 0;
		StringBuffer commandBuffer = new StringBuffer();

		if (data != null) {
			datalength = data.length();
		}
		commandBuffer.append(integerToHex(datalength));
		commandBuffer.append(integerToHex(networkId));
		commandBuffer.append(integerToHex(readerId));
		commandBuffer.append(integerToHex(nodeId));
		log.debug("command is->" + commands.get(aCommand));
		commandBuffer.append(commands.get(aCommand));
		if (data != null) {

			commandBuffer.append(data);
		}
		commandBuffer.append(byteToHex(crcXor(hexToBuffer(commandBuffer.toString()))));
		String hexString = commands.get(command.HEADER).concat(
				commandBuffer.toString());
		log.debug(hexString);
		byte[] returnvalue = hexToBuffer(hexString);
		for (byte aByte : returnvalue) {
			log.debug("Create Command to send " + byteToHex(aByte) + " ");
		}
		log.debug(returnvalue);
		return returnvalue;

	}

	protected byte[] createResponse(String data) {
		nodeId = 0;
		networkId = 0;
		readerId = 0; // receiverId
		int datalength = 0;
		StringBuffer commandBuffer = new StringBuffer();

		if (data != null) {
			datalength = data.length();
		}
		commandBuffer.append(integerToHex(datalength));
		commandBuffer.append(integerToHex(networkId));
		commandBuffer.append(integerToHex(readerId));
		commandBuffer.append(integerToHex(nodeId));
		if (data != null) {

			commandBuffer.append(data);
		}
		commandBuffer.append(byteToHex(crcXor(commandBuffer.toString()
				.getBytes())));

		String hexString = commands.get(command.RESPONSE).concat(
				commandBuffer.toString());
		log.debug(hexString);
		byte[] returnvalue = hexToBuffer(hexString);
		for (byte aByte : returnvalue) {
			log.debug("create response to send "  + byteToHex(aByte) + " ");
		}
		log.debug(returnvalue);
		return returnvalue;

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
	 * Returns a string containing the hexadecimal representation of the input
	 * string. Each byte in the input string is converted to a two-digit
	 * hexadecimal value. Thus the returned string is twice the length of the
	 * input string. The output hex characters are upper case. The conversion
	 * assumes the default charset; making the charset explicit could be
	 * accomplished by just adding a parameter here and passing it through to
	 * getBytes().
	 * 
	 * @param s
	 *            a string to convert to hex
	 * @return the hex string version of the input string
	 */
	public String stringToHex(String s) {
		byte[] stringBytes = s.getBytes();

		return bufferToHex(stringBytes);
	}

	/**
	 * Returns a string containing the hexadecimal representation of the input
	 * byte array. Each byte in the input array is converted to a two-digit
	 * hexadecimal value. Thus the returned string is twice the length of the
	 * input byte array. The output hex characters are upper case.
	 * 
	 * @param buffer
	 *            a buffer to convert to hex
	 * @return the hex string version of the input buffer
	 */
	public String bufferToHex(byte buffer[]) {
		return bufferToHex(buffer, 0, buffer.length);
	}

	/**
	 * Returns a string containing the hexadecimal representation of the input
	 * byte array. Each byte in the input array is converted to a two-digit
	 * hexadecimal value. Thus the returned string is twice the length of the
	 * specified amount of the input byte array. The output hex characters are
	 * upper case.
	 * 
	 * @param buffer
	 *            a buffer to convert to hex
	 * @param startOffset
	 *            the offset of the first byte in the buffer to process
	 * @param length
	 *            the number of bytes in the buffer to process
	 * @return the hex string version of the input buffer
	 */
	public String bufferToHex(byte buffer[], int startOffset, int length) {
		StringBuffer hexString = new StringBuffer(2 * length);
		int endOffset = startOffset + length;

		for (int i = startOffset; i < endOffset; i++) {
			appendHexPair(buffer[i], hexString);
		}

		return hexString.toString();
	}

	/**
	 * Returns a string built from the byte values represented by the input
	 * hexadecimal string. That is, each pair of hexadecimal characters in the
	 * input string is decoded into the byte value that they represent, and that
	 * byte value is appended to a string which is ultimately returned. This
	 * function doesn't care whether the hexadecimal characters are upper or
	 * lower case, and it also can handle odd-length hex strings by assuming a
	 * leading zero digit. If any character in the input string is not a valid
	 * hexadecimal digit, it throws a NumberFormatException, in keeping with the
	 * behavior of Java functions like Integer.parseInt(). The conversion
	 * assumes the default charset; making the charset explicit could be
	 * accomplished by just adding a parameter here and passing it through to
	 * the String constructor.
	 * 
	 * @param hexString
	 *            a string of hexadecimal characters
	 * @return a String built from the bytes indicated by the input string
	 * @throws NumberFormatException
	 */
	public String hexToString(String hexString) throws NumberFormatException {
		byte[] bytes = hexToBuffer(hexString);

		return new String(bytes);
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
	 * Appends a hexadecimal representation of a particular char value to a
	 * string buffer. That is, two hexadecimal digits are appended to the
	 * string.
	 * 
	 * @param b
	 *            a byte whose hex representation is to be obtained
	 * @param hexString
	 *            the string to append the hex digits to
	 */
	private void appendHexPair(byte b, StringBuffer hexString) {
		char highNibble = kHexChars[(b & 0xF0) >> 4];
		char lowNibble = kHexChars[b & 0x0F];

		hexString.append(highNibble);
		hexString.append(lowNibble);
	}

	public static Properties getProperties(String fileName) throws Exception {
		// getResource at the momment hard-wire for now.
		InputStream is = ClassLoader.getSystemResourceAsStream(fileName);
		Properties properties = new Properties();

		if (is == null) {
			throw new Exception(fileName + " not found");
		}
		properties.load(is);

		return properties;

	}

	/**
	 * @return Returns the networkId.
	 */
	public int getNetworkId() {
		return networkId;
	}

	/**
	 * @param networkId
	 *            The networkId to set.
	 */
	public void setNetworkId(int networkId) {
		this.networkId = networkId;
	}

	/**
	 * @return Returns the nodeId.
	 */
	public int getNodeId() {
		return nodeId;
	}

	/**
	 * @param nodeId
	 *            The nodeId to set.
	 */
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * @return Returns the readerId.
	 */
	public int getReaderId() {
		return readerId;
	}

	/**
	 * @param readerId
	 *            The readerId to set.
	 */
	public void setReaderId(int readerId) {
		this.readerId = readerId;
	}

	public String integerToHex(int value) {
		String retVal = Integer.toHexString(value);
		if ((retVal.length() % 2) == 1) {
			retVal = "0" + retVal;
		}

		return retVal;
	}
}
