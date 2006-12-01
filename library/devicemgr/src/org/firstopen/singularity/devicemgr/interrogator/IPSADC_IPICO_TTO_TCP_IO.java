/* 
 * Copyright 2005 i-Konect LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.firstopen.singularity.devicemgr.interrogator;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.singularity.config.DeviceProfile;
import org.firstopen.singularity.system.ReaderEvent;
import org.firstopen.singularity.system.Sensor;
import org.firstopen.singularity.system.ShutdownManager;
import org.firstopen.singularity.system.Tag;

/**
 * @author TomRose
 * @version $Id: IPSADC_IPICO_TTO_TCP_IO.java 1242 2006-01-14 03:34:08Z TomRose $
 * 
 */
public class IPSADC_IPICO_TTO_TCP_IO extends IPSADC_IPICO_TTO_IO {

	private boolean continueReading = false;

	private Log log = LogFactory.getLog(this.getClass());

	private Socket aSocket = null;

	/**
	 * @param deviceProfile
	 */
	public IPSADC_IPICO_TTO_TCP_IO(DeviceProfile deviceProfile) {
		super(deviceProfile);

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
		String defaultPort = "3002";
		String defaultIP = "192.168.5.172";
		String defaultBaud = "0";
		for (int i = 0; i < args.length; i++) {
			switch (i) {

			case 0:
				defaultIP = args[i];
				break;
			case 1:
				defaultPort = args[i];
				break;

			}
		}// end for
		ShutdownManager sdm = new ShutdownManager();
		Runtime.getRuntime().addShutdownHook(sdm);

		try {

			DeviceProfile deviceProfile = new DeviceProfile("Dummy");
			deviceProfile.setPort(defaultPort);
			deviceProfile.setBaud(defaultBaud);
			deviceProfile.setIpAddress(defaultIP);

			Set<Sensor> sensors = new HashSet<Sensor>();
			sensors.add(new Sensor("embeddedAntenna"));
			deviceProfile.setSensorSet(sensors);

			IPSADC_IPICO_TTO_TCP_IO reader = new IPSADC_IPICO_TTO_TCP_IO(
					deviceProfile);
			ShutdownManager.addManagedObject(reader);

			DummyDM dummyDM = reader.new DummyDM();
			reader.setInterrogatorIO(dummyDM);
			reader.on();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.firstopen.singularity.devicemgr.interrogator.IPSADC_IPICO_IO#off()
	 */
	@Override
	public synchronized void off() {
		switchRF(this.RFOFF);
		continueReading = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.firstopen.singularity.devicemgr.interrogator.IPSADC_IPICO_IO#on()
	 */
	@Override
	public synchronized void on() {
		if (continueReading == false) {
			continueReading = true;
			Thread thread = new Thread(this);
			thread.start();
		} else {
			log.warn("Interorgator is already running");
			// switchRF(this.RFON);
		}
	}

	public void run() {

		

		try {

			log.info("connecting...");
			connect();
			log.debug("connected...");
			int numBytes = 0;
			byte[] readBuffer = new byte[RECORD_LENGTH * 4];
			while (continueReading) {
				log.debug("reading...");

				try {

					log.debug("processing...");

					numBytes = inputStream.read(readBuffer);
					message.write(readBuffer, 0, numBytes);
					log.debug("Bytes Read = " + numBytes);
					
					/*
					 * see if there are any complete messages found in teh
					 * output stream (message)
					 */
					ArrayList<byte[]> messageList = findMessage(message);

					log.debug("messages found =" + messageList.size());

					if (messageList.size() > 0) {

						try {
							/*
							 * decode a list of messages multipe ecodings can
							 * exists this one asumes the ascii encoding.
							 */
							ArrayList<Tag> tagList = decodeData(messageList);

							log.trace("valid tags decoded =" + tagList.size());
							
							Sensor sensor = getDeviceProfile().getSensorSet().iterator().next();
                            
							ReaderEvent event = new ReaderEvent(sensor);

							event.setTagIds(tagList);

							event.setReaderName(getDeviceProfile()
									.getDeviceProfileID());

							interrogatorIO.sendEvent(event);

							log.debug("valid unique tags sent =" + event.getTagIds().size());
							
						} catch (InterrogatorException e) {
							log.error(
									"undable to decode message, info ignored",
									e);
						} catch (Exception e) {
							log.error("unable to send event to Device Manager",
									e);
						}
					}// end if
				} catch (IOException e) {
					log.error("unable to read from socket", e);

					try {
						Thread.sleep(5000);
						connect();
					} catch (UnknownHostException e1) {
						log.error("unable to connect to Reader Socket", e1);
					} catch (IOException e1) {
						log.error("unable to connect to Reader Socket", e1);
					} catch (InterruptedException e1) {
						log.error("unable to sleep before retry of connection",
								e1);
					}
				}

			}// end while

			aSocket.close();
		} catch (UnknownHostException e1) {
			log.error("unable to connect to Reader Socket", e1);
		} catch (IOException e1) {
			log.error("unable to connect to Reader Socket", e1);
		}

	}// end run

	/**
	 * cleans up when device manager is shutdown
	 */
	public boolean shutdown() {

		off();
		return true;
	}

	protected void connect() throws UnknownHostException, IOException{
		int portInt = Integer.valueOf(getDeviceProfile().getPort())
		.intValue();
		if (aSocket != null){
			aSocket.close();
		}
		aSocket = new Socket(getDeviceProfile().getIpAddress(), portInt);
		inputStream = aSocket.getInputStream();
		outputStream = aSocket.getOutputStream();
	}
	
	class DummyDM implements InterrogatorIO {
		int tagReads = 0;

		long startTime = 0;

		long now = 0;

		public void sendEvent(ReaderEvent event) throws Exception {

			if ((System.currentTimeMillis() - startTime) > 5000) {
				System.out.println("valid tag reads = " + tagReads);
				startTime = System.currentTimeMillis();
				tagReads = 0;
			} else {
				Collection<Tag> tags = event.getTagIds();
				for (Iterator<Tag> iter = tags.iterator(); iter.hasNext();) {
					Tag element =  iter.next();
					tagReads = tagReads + element.getCount();
					
				}

			}

		}

	}// end class DummyDM

    /**
     * 
     */
    public IPSADC_IPICO_TTO_TCP_IO() {
        super();
        // TODO Auto-generated constructor stub
    }

}
