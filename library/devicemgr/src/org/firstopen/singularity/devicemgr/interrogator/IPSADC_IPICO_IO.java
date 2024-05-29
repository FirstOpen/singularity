/*
 * Copyright (c) 2005 J. Thomas Rose. All rights reserved.
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
import java.util.Set;
import java.util.TooManyListenersException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.singularity.config.DeviceProfile;
import org.firstopen.singularity.system.ReaderEvent;
import org.firstopen.singularity.system.Sensor;
import org.firstopen.singularity.system.Shutdown;
import org.firstopen.singularity.system.ShutdownManager;
import org.firstopen.singularity.system.Tag;

/**
 * Class declaration
 * 
 * @author Tom Rose
 * @version $Id: IPSADC_IPICO_IO.java 1242 2006-01-14 03:34:08Z TomRose $
 * 
 * 
 */
public class IPSADC_IPICO_IO implements SerialPortEventListener, Interrogator,
		Shutdown {

    private  CommPortIdentifier portId;

	private  Enumeration portList;

	protected InputStream inputStream;

	protected OutputStream outputStream;

	private SerialPort serialPort;

	protected ByteArrayOutputStream message = new ByteArrayOutputStream();

	private Log log = LogFactory.getLog(this.getClass());

	private DeviceProfile deviceProfile = null;

	private boolean commandline = false;

	private HashMap<String, String> commands = new HashMap<String, String>();

	protected final String RFON = "RFON";

	protected final String RFOFF = "RFOFF";

	protected int RECORD_LENGTH = 38;

	protected InterrogatorIO interrogatorIO;

	/**
	 * Main Method declaration for testing
	 * 
	 * @param args
	 * @throws Exception
	 * 
	 * @see
	 */
	public static void main(String[] args) {
    
		String defaultPort = "/dev/tty.usbserial-FTCAYDDH";
		String defaultBaud = "9600";

		char CR = '\r';
		char LF = '\n';
		StringBuffer CRLF = new StringBuffer();
		CRLF.append(CR);
		CRLF.append(LF);

		for (int i = 0; i < args.length; i++) {
			switch (i) {
			case 0:
				defaultPort = args[i];
				break;
			case 1:
				defaultBaud = args[i];
				break;
			}
		}// end for
		ShutdownManager sdm = new ShutdownManager();
		Runtime.getRuntime().addShutdownHook(sdm);
		try {

			DeviceProfile deviceProfile = new DeviceProfile("Dummy");

			deviceProfile.setPort(defaultPort);
			deviceProfile.setBaud(defaultBaud);
			Set<Sensor> sensors = new HashSet<Sensor>();
			sensors.add(new Sensor("embeddedAntenna"));
			deviceProfile.setSensorSet(sensors);

			IPSADC_IPICO_IO reader = new IPSADC_IPICO_IO(deviceProfile);

			reader.commandline = true;

			reader.run();

			reader.serialPort.addEventListener(new SimpleEventListener(
					reader.serialPort));

			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));

			String commandLine = null;

			OutputStream outputStream = reader.serialPort.getOutputStream();

			String sLRC = null;

			while (true) {
				System.out.print("enter command > ");
				commandLine = br.readLine();
				sLRC = reader.calcLRC(commandLine.getBytes());

				commandLine = commandLine + sLRC + CRLF;
				System.out.println("Length is " + commandLine.length());
				System.out.println("LRC is: " + sLRC);

				outputStream.write(commandLine.getBytes());
				outputStream.flush();
			}

		} catch (IOException ioe) {
			System.out.println("IO error trying to read command");

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * Constructor declaration
	 * 
	 * 
	 * @see
	 */
	public IPSADC_IPICO_IO() {
		setupCommands();
	}

	/**
	 * Constructor declaration
	 * 
	 * 
	 * @see
	 */
	public IPSADC_IPICO_IO(DeviceProfile deviceProfile) {
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
						log.info("Found port match: " + defaultPort);
						portFound = true;

					}
				}
			}
			if (!portFound) {
				System.out.println("port " + defaultPort + " not found.");
			}

			else {

				serialPort = (SerialPort) portId.open(
						this.getClass().getName(), 2000);

				inputStream = serialPort.getInputStream();

				outputStream = serialPort.getOutputStream();

				if (commandline == false)
					serialPort.addEventListener(this);
			}
			serialPort.notifyOnDataAvailable(true);

			serialPort.setSerialPortParams(defaultBaud, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			switchRF(this.RFON);

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
				byte[] readBuffer = new byte[64];
				int numBytes = 0;
				while (inputStream.available() > 0) {
					numBytes = inputStream.read(readBuffer);
					message.write(readBuffer, 0, numBytes);
					log.debug("Bytes Read = " + numBytes);

				}
			} catch (IOException e) {
				log.error("Failed to read from serial input stream ", e);
			}
			log.debug("message is: " + message);
			/*
			 * see if there are any complete messages found in teh output stream
			 * (message)
			 */
			ArrayList<byte[]> messageList = findMessage(message);

			log.info("message size is :" + messageList.size());

			if (messageList.size() > 0) {

				try {
					/*
					 * decode a list of messages multipe ecodings can exists
					 * this one asumes the ascii encoding.
					 */
					ArrayList<Tag> tagList = decodeData(messageList);

					Sensor sensor = deviceProfile.getSensorSet().iterator().next();

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
	 * find ascii encoded messages in the OutputStream a valid message begins
	 * with "aa" and ends with "<CR><LF>" Thee may be 1 or more messages in
	 * the buffer. The buffer is reset, and then partial messsages are added
	 * back to the buffer for processing on the serial event.
	 * 
	 * @param message
	 * @return messageList
	 */
	protected ArrayList<byte[]> findMessage(ByteArrayOutputStream message) {

		ArrayList<byte[]> messageList = new ArrayList<byte[]>();

		synchronized (message) {

			// log.debug("message is " + message + " record length = " +
			// RECORD_LENGTH);

			byte[] buffer = message.toByteArray();
			byte[] messageBuf = new byte[RECORD_LENGTH];

			message.reset();
			for (int i = 0; i < buffer.length - 1; i++) {
				if (buffer[i] == 97 && buffer[i + 1] == 97) {
					if (i + RECORD_LENGTH - 1 > buffer.length) {
						
						message.write(buffer, i, buffer.length - i);
						break;
					}
					if (buffer[i + RECORD_LENGTH - 2] == 13
							&& buffer[i + RECORD_LENGTH - 1] == 10) {
						messageBuf = new byte[RECORD_LENGTH];
						System.arraycopy(buffer, i, messageBuf, 0,
								RECORD_LENGTH);
						messageList.add(messageBuf);
						i = i + RECORD_LENGTH - 1; // index to end of message
					}
					
				}// end if start message found
			}
		}
		return messageList;

	}

	/**
	 * Byte Description Info - Hexadecimal ASCII delimited records 0 Header
	 * character 1 Frame header, 1 Header character 2-3 Reader ID 0-255 in ASCII
	 * hex 4-15 Tag ID MS digit first 16-19 I and Q channel counter Binary
	 * counters 0-255 in ASCII hex 20-33 Date/Time Date and time with 10ms
	 * resolution. 390ms/10 = 39 =(27 = 0x32 + 0x37) and the month 12 is 0x31+
	 * 0x32. 34-35 LRC Checksum on bytes 2 to 33 36-37 End of packet (CR, LF)
	 * 0x0d, 0x0a
	 * 
	 * (i.e. ASCII converison of byte array =
	 * aa0000000003e52e0102050625025124319b
	 * 
	 * 
	 * Instantaneous ID hits and IDs in transit are spooled in small packets,
	 * one for each ID. These can be considered as stand alone data-grams
	 * similar to the UDP protocol. Table 1 depicts the content of these
	 * data-grams for the hexadecimal ASCII format. This format converts the
	 * binary data to ASCII but with a hexadecimal base (e.g. 0b00101111 is
	 * converted to �2f�). All ASCII characters are lower case. In addition, the
	 * ID CRC bytes are stripped off. A simple 8-bit LRC is appended to the
	 * packet and is a modulo 28 addition of all the data in the packet but not
	 * the Frame header bytes.
	 * 
	 * Example: ASCII spooled ID, �aa400000000123450a2a01123018455927a7<CR><LF>�
	 * (38 bytes) Reader ID 0x40 (64d), tag ID 0x000000012345, I counter 0x0a
	 * (10d), Q counter 0x2a (42d), date (20)01-12- 30, 18:45:59.39, checksum
	 * 0xa7. (Note the millennium and century value is discarded in date stamp)
	 * ASCII LRC checksum calculation: �4�+�0� + �.. �2� + �7� Calculations are
	 * done in hexadecimal on ASCII data, (i.e ASCII character �a� is 0x61) 0x34 +
	 * 0x30 + 0x30 + 0x30 + 0x30 + 0x30 + 0x30 + 0x30 + 0x30 + 0x31 + 0x32 +
	 * 0x33 + 0x34 + 0x35 + 0x30 + 0x61 + 0x32 + 0x61 + 0x30 + 0x31 + 0x31 +
	 * 0x32 + 0x33 + 0x30 + 0x31 + 0x38 + 0x34 + 0x35 + 0x35 + 0x39 + 0x32 +
	 * 0x37 = 0x06a7, discard carry thus LRC = 0xa7 or ASCII 0x61, 0x37
	 * 
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

			if (buf[0] != 97)
				throw new InterrogatorException("invalid message from reader");
			int buffer = 0;
			char cstring[] = new char[buf.length - 2];

			for (int i = 0; i < buf.length - 2; i++) {
				buffer = 0;
				buffer = (0x000000FF & ((int) buf[i]));
				cstring[i] = (char) buffer;
			}

			String readBuffer = new String(cstring);
			log.debug("readbuffer : " + readBuffer);

			String lrc = readBuffer.substring(RECORD_LENGTH - 4,
					RECORD_LENGTH - 2);
			int iLRC = new BigInteger(lrc, 16).intValue();

			buffer = 0;
			for (int i = 2; i <= RECORD_LENGTH - 5; i++) {
				buffer = buf[i] + buffer;
			}
			buffer = (0x000000FF & (buffer));
			if (iLRC != buffer)
				throw new InterrogatorException(
						"invalid message, checksum does not match");

			String header = readBuffer.substring(0, 2);
			String readerID = readBuffer.substring(2, 4);
			int iReaderID = new BigInteger(readerID, 16).intValue();

			String tagID = readBuffer.substring(4, 16);
			int iTagID = new BigInteger(tagID, 16).intValue();

			String iiChannelCounter = readBuffer.substring(16, 18);
			int iIChannelCounter = new BigInteger(iiChannelCounter, 16)
					.intValue();

			String qChannelCounter = readBuffer.substring(18, 20);
			int iQChannelCounter = new BigInteger(qChannelCounter, 16)
					.intValue();


			String dateTime = readBuffer.substring(20, 34);
			int iDateTime = new BigInteger(dateTime, 16).intValue();

			String hour = dateTime.substring(6, 8);
			String minutes = dateTime.substring(8, 10);
			String seconds = dateTime.substring(10, 12);
			String msec = dateTime.substring(12);
			String ftime = hour + ":" + minutes + ":" + seconds + "." + msec
					+ "0";

			String packetEnd = readBuffer.substring(RECORD_LENGTH - 2);

			log.debug("header = " + header);
			log.debug("readerID = " + readerID + "= " + iReaderID);
			log.debug("tagID = " + tagID + "= " + iTagID);
			log.debug("IChannel = " + iIChannelCounter );
			log.debug("QChannel = " + iQChannelCounter );
		    log.debug("dateTime = " + dateTime.substring(0, 10) + ftime + "="
					+ iDateTime);
			log.debug("lrc = " + lrc + "= " + iLRC);
			log.debug("packetEnd = " + packetEnd);

			Tag currentTag = new Tag();
			currentTag.setValue(tagID);
			tagList.add(currentTag);
			currentTag.setCount(iIChannelCounter + iQChannelCounter);

			/*
			 * TODO: uncomment if physical readerID is perfered, need to make
			 * this part of the configuration to allow physical IDs as well as
			 * assined IDs for the physical compoents.
			 * deviceProfile.setDeviceProfileID(readerID);
			 */

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
			switchRF(this.RFON);
		}

	}

	public synchronized void off() {
		if (serialPort != null) {
			switchRF(this.RFOFF);
		}

	}

	protected synchronized void switchRF(String command) {

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

	public byte[] getCommand(String commandName) {

		return commands.get(commandName).getBytes();

	}

	protected void setupCommands() {

		char CR = '\r';
		char LF = '\n';
		StringBuffer CRLF = new StringBuffer();
		CRLF.append(CR);
		CRLF.append(LF);

		commands.put(this.RFON, "ab0001060188" + CRLF);
		commands.put(this.RFOFF, "ab0001060088" + CRLF);

	}

	/**
	 * cleans up when device manager is shutdown
	 */
	public boolean shutdown() {

		serialPort.removeEventListener();
		serialPort.close();

		return true;
	}

	protected String calcLRC(byte[] buf) {
		int buffer = 0;
		for (int i = 2; i < buf.length; i++) {
			buffer = buf[i] + buffer;
		}
		buffer = (0x000000FF & (buffer));

		return (Integer.toHexString(buffer));
	}

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#queryRep()
     */
    public void queryRep() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#ack()
     */
    public void ack() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#query()
     */
    public void query() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#queryAdjust()
     */
    public void queryAdjust() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#select()
     */
    public void select() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#nak()
     */
    public void nak() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#reqRN()
     */
    public void reqRN() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#read()
     */
    public void read() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#write()
     */
    public void write() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#kill()
     */
    public void kill() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#lock()
     */
    public void lock() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#access()
     */
    public void access() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#blockWrite()
     */
    public void blockWrite() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#blockErase()
     */
    public void blockErase() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.interrogator.Interrogator#getDescription()
     */
    public static String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

   
}
