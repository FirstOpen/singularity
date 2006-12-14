/*

 * Copyright 2005 i-Konect LLC

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.firstopen.singularity.config.DeviceProfile;
import org.firstopen.singularity.system.ReaderEvent;
import org.firstopen.singularity.system.Tag;

public class IF5_Intermec_IO implements Interrogator

{

	private boolean keepRunning = true;

	private SimpleDateFormat sdfObject = new SimpleDateFormat(
			"dd/MM/yy : HH:mm:ss");

	private Socket aSocket;

	private InputStream readFromInterrogatorStream;

	private OutputStream writeToInterrogatorStream;

	private StreamTokenizer sTokenizer;

	private Hashtable ecSpecHash; // key/value pair = String ecSpecId / Long
									// endTime

	private Map rawDataHash; // key/value pari = String ecSpecId / List
								// rawDataList
	private InterrogatorIO interrogatorIO;
	
    private Logger log = null;

	private String interrogatorId;

	private long endTime = 0L;

	private String interrogatorCommand = "\nREAD\n";

	private DeviceProfile deviceProfile = null;
	
	/**
     * 
     */
    public IF5_Intermec_IO() {
        super();
        // TODO Auto-generated constructor stub
    }

    public IF5_Intermec_IO(DeviceProfile deviceProfile)

	{

		aSocket = null;

		try

		{

			

			log = Logger.getLogger(this.getClass());

			log.debug("Constructor()  interrogatorId = " + interrogatorId);

			int portInt = Integer.valueOf(deviceProfile.getPort()).intValue();

			aSocket = new Socket(deviceProfile.getIpAddress(), portInt);

			readFromInterrogatorStream = aSocket.getInputStream();

			writeToInterrogatorStream = aSocket.getOutputStream();

			ecSpecHash = new Hashtable();

			rawDataHash = new HashMap();

		}

		catch (Exception x)

		{

			x.printStackTrace();

		}

	}

	public boolean registerECSpec(String ecSpecName, Long endTime)
			throws Exception

	{

		log.debug("registerECSpec() specName = " + ecSpecName
				+ " and endTime = " + endTime);

		setEndTime(endTime.longValue());

		ecSpecHash.put(ecSpecName, endTime);

		rawDataHash.put(ecSpecName, new ArrayList());

		return true;

	}

	public boolean unRegisterECSpec(String ecSpecName) throws Exception

	{

		ecSpecHash.remove(ecSpecName);

		rawDataHash.remove(ecSpecName);

		return true;

	}

	public List getEPCDataList(String ecSpecName) throws Exception

	{

		return (List) rawDataHash.get(ecSpecName);

	}

	public void run()

	{

		log.debug("starting interrogatorId = " + interrogatorId);

		boolean shouldWrite = true;

		while (keepRunning)

		{

			while (ecSpecHash.size() > 0
					&& (System.currentTimeMillis() < getEndTime()))

			{

				if (shouldWrite)

					writeToInterrogator();

				shouldWrite = readFromInterrogator();

			}

		}

	}

	private void writeToInterrogator()

	{

		try

		{

			byte[] command = getInterrogatorCommand().getBytes();

			log.debug("writeToInterrogator() command = " + new String(command));

			writeToInterrogatorStream.write(command);

			writeToInterrogatorStream.flush();

		}

		catch (Exception x)

		{

			x.printStackTrace();

		}

	}

	private boolean readFromInterrogator()

	{

		String token;

		String timeStamp;

		try

		{
			Reader r = new BufferedReader(new InputStreamReader(readFromInterrogatorStream));
		
			sTokenizer = new StreamTokenizer(r);

			while (keepRunning
					&& sTokenizer.nextToken() != StreamTokenizer.TT_EOF)

			{

				switch (sTokenizer.ttype)

				{

				case StreamTokenizer.TT_NUMBER:

					log.debug("readFromInterrogator() number = "
							+ sTokenizer.nval);

					Double epcDouble = new Double(sTokenizer.nval);

					Enumeration ecSpecEnum = ecSpecHash.keys();

					ReaderEvent readerEvent = new ReaderEvent();
                    
                    readerEvent.setReaderName(getDeviceProfile()
                            .getDeviceProfileID());
                    
					Tag tag = new Tag();
                    tag.setValue(epcDouble.toString());
                    tag.increment();
				
					ArrayList<Tag> tagIds = new ArrayList<Tag>();
					tagIds.add(tag);
					readerEvent.addTags(tagIds);
					
					interrogatorIO.sendEvent(readerEvent);
					
                  break;
                  
				case StreamTokenizer.TT_WORD:

					// log.debug("readFromInterrogator() "+sTokenizer.sval);

					if (sTokenizer.sval == null)

						break;

					if (!sTokenizer.sval.startsWith("OK")
							&& !sTokenizer.sval.endsWith("ead"))

					{

						token = sTokenizer.sval;

						log.debug("readFromInterrogator() token = " + token);

						// timeStamp = sdfObject.format(new Date());

					}

					else if (sTokenizer.sval.startsWith("OK"))

					{

						return true;

					}

					break;
					
				default:

					break;

				}

			}

			return false;

		}

		catch (Exception x)

		{

			x.printStackTrace();

			return false;

		}

	}

	private void close() throws IOException 

	{

		log.debug("close()");

		sTokenizer = null;

		readFromInterrogatorStream.close();

		writeToInterrogatorStream.close();

		aSocket.close();

	}

	public boolean hasMoreECSpecsRegistered() throws Exception

	{

		return !ecSpecHash.isEmpty();

	}

	private long getEndTime()

	{
		return endTime;
	}

	private void setEndTime(long x)

	{
		endTime = x;
	}

	private String getInterrogatorCommand()

	{
		return interrogatorCommand;
	}

	private void setInterrogatorCommand(String x)

	{
		interrogatorCommand = x;
	}

	private String getCommand()

	{
		return interrogatorCommand;
	}

	private void setCommand(String x)

	{
		interrogatorCommand = x;
	}


		
	/**
	 * @return Returns the deviceProfile.
	 */
	public DeviceProfile getDeviceProfile() {
		return deviceProfile;
	}

	/**
	 * @param deviceProfile The deviceProfile to set.
	 */
	public void setDeviceProfile(DeviceProfile deviceProfile) {
		this.deviceProfile = deviceProfile;
	}

	public void on() {
		keepRunning = true;
		new Thread(this).start();
	}

	public void off() {
		keepRunning = false;
		try {
			close();
		} catch (IOException e) {
			
			log.error("unable to close intterogator",e);
		}
		
	}

	public void setInterrogatorIO(InterrogatorIO interrogatorIO) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return Returns the interrogatorIO.
	 */
	public InterrogatorIO getInterrogatorIO() {
		return interrogatorIO;
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
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

}
